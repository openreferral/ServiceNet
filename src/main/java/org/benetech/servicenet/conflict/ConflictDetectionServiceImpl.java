package org.benetech.servicenet.conflict;

import org.benetech.servicenet.conflict.detector.ConflictDetector;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
import org.benetech.servicenet.matching.model.EntityEquivalent;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.MetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConflictDetectionServiceImpl implements ConflictDetectionService {

    private static final String CONFLICT_DETECTOR_SUFFIX = "ConflictDetector";

    private final Logger log = LoggerFactory.getLogger(ConflictDetectionServiceImpl.class);

    private final ApplicationContext context;

    private final EntityManager em;

    private final OrganizationEquivalentsService organizationEquivalentsService;

    private final ConflictService conflictService;

    private final MetadataService metadataService;

    public ConflictDetectionServiceImpl(ApplicationContext context,
                                        EntityManager em,
                                        OrganizationEquivalentsService organizationEquivalentsService,
                                        ConflictService conflictService,
                                        MetadataService metadataService) {
        this.context = context;
        this.em = em;
        this.organizationEquivalentsService = organizationEquivalentsService;
        this.conflictService = conflictService;
        this.metadataService = metadataService;
    }

    @Async
    @Override
    @Transactional
    public void detect(List<OrganizationMatch> matches) {
        List<Conflict> conflicts = new LinkedList<>();

        long detectionStartTime = System.currentTimeMillis();
        for (OrganizationMatch match : matches) {
            long startTime = System.currentTimeMillis();
            OrganizationEquivalent orgEquivalent = organizationEquivalentsService.generateEquivalent(
                match.getOrganizationRecord(), match.getPartnerVersion());

            List<EntityEquivalent> equivalents = gatherAllEquivalents(orgEquivalent);

            conflicts.addAll(detect(equivalents, match.getOrganizationRecord().getAccount(),
                match.getPartnerVersion().getAccount()));
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            //TODO: Remove time counting logic (#264)
            log.debug("Searching for conflicts between " +
                match.getOrganizationRecord().getAccount().getName() + "'s organization '" +
                match.getOrganizationRecord().getName() + "' and " +
                match.getOrganizationRecord().getAccount().getName() + "'s organization '" +
                match.getOrganizationRecord().getName() + "' took: " + elapsedTime + "ms");
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - detectionStartTime;
        //TODO: Remove time counting logic (#264)
        log.info("Searching for conflicts took " + elapsedTime + "ms");
        persistConflicts(conflicts);
    }

    private List<EntityEquivalent> gatherAllEquivalents(OrganizationEquivalent orgEquivalent) {
        List<EntityEquivalent> equivalents = new LinkedList<>(orgEquivalent.getUnwrappedEntities());
        equivalents.add(orgEquivalent);
        return equivalents;
    }

    private List<Conflict> detect(List<EntityEquivalent> equivalents, SystemAccount owner, SystemAccount accepted) {
        List<Conflict> conflicts = new LinkedList<>();

        for (EntityEquivalent eq : equivalents) {
            Object current = em.find(eq.getClazz(), eq.getBaseResourceId());
            Object mirror = em.find(eq.getClazz(), eq.getPartnerResourceId());

            try {
                ConflictDetector detector = context.getBean(
                    eq.getClazz().getSimpleName() + CONFLICT_DETECTOR_SUFFIX, ConflictDetector.class);
                List<Conflict> innerConflicts = detector.detect(current, mirror);
                innerConflicts.forEach(c -> addConflictingDates(eq, c));
                innerConflicts.forEach(c -> addAccounts(c, owner, accepted));

                conflicts.addAll(innerConflicts);
            } catch (NoSuchBeanDefinitionException ex) {
                log.warn("There is no conflict detector for {}", eq.getClazz().getSimpleName());
            }
        }
        return conflicts;
    }

    private void addConflictingDates(EntityEquivalent eq, Conflict conflict) {
        Optional<Metadata> currentMetadata = metadataService.findMetadataForConflict(
            eq.getBaseResourceId().toString(), conflict.getFieldName(), conflict.getCurrentValue());
        currentMetadata.ifPresent(m -> conflict.setCurrentValueDate(m.getLastActionDate()));

        Optional<Metadata> offeredMetadata = metadataService.findMetadataForConflict(
            eq.getPartnerResourceId().toString(), conflict.getFieldName(), conflict.getOfferedValue());
        offeredMetadata.ifPresent(m -> conflict.setOfferedValueDate(m.getLastActionDate()));
    }

    private void addAccounts(Conflict conflict, SystemAccount owner, SystemAccount accepted) {
        conflict.setOwner(owner);
        conflict.addAcceptedThisChange(accepted);
    }

    private void persistConflicts(List<Conflict> conflicts) {
        conflicts.forEach(conflict -> {
            rejectAllOutdatedConflicts(conflict);
            em.persist(conflict);
        });
    }

    private void rejectAllOutdatedConflicts(Conflict c) {
        Set<Conflict> outdated = new HashSet<>(conflictService.findAllConflictsWhichOffersTheSameValue(
            c.getResourceId(), c.getFieldName(), c.getOfferedValue()));
        outdated.addAll(conflictService.findAllConflictsWhichHoldsTheSameValue(
            c.getResourceId(), c.getFieldName(), c.getCurrentValue()));

        outdated.forEach(out -> {
            out.setState(ConflictStateEnum.REJECTED);
            out.setStateDate(ZonedDateTime.now());
            em.persist(out);
        });
    }

}
