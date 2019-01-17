package org.benetech.servicenet.conflict;

import org.benetech.servicenet.conflict.detector.ConflictDetector;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.matching.model.EntityEquivalent;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.ConflictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ConflictDetectionServiceImpl implements ConflictDetectionService {

    private static final String CONFLICT_DETECTOR_SUFFIX = "ConflictDetector";

    private final Logger log = LoggerFactory.getLogger(ConflictDetectionServiceImpl.class);

    private final ApplicationContext context;

    private final EntityManager em;

    private final OrganizationRepository organizationRepository;

    private final OrganizationEquivalentsService organizationEquivalentsService;

    private final ConflictService conflictService;

    public ConflictDetectionServiceImpl(ApplicationContext context,
                                        EntityManager em,
                                        OrganizationRepository organizationRepository,
                                        OrganizationEquivalentsService organizationEquivalentsService,
                                        ConflictService conflictService) {
        this.context = context;
        this.em = em;
        this.organizationRepository = organizationRepository;
        this.organizationEquivalentsService = organizationEquivalentsService;
        this.conflictService = conflictService;
    }

    @Async
    @Override
    @Transactional
    public void detect(List<OrganizationMatch> matches) {
        List<Conflict> conflicts = new LinkedList<>();

        for (OrganizationMatch match : matches) {
            log.debug("Request to detect conflicts for {} organization", match.getOrganizationRecord().getName());
            OrganizationEquivalent orgEquivalent = organizationEquivalentsService.generateEquivalent(
                match.getOrganizationRecord(), match.getPartnerVersion());

            List<EntityEquivalent> equivalents = gatherAllEquivalents(orgEquivalent);

            conflicts.addAll(detect(equivalents, match.getOrganizationRecord().getAccount(),
                match.getPartnerVersion().getAccount()));
        }

        conflicts = updateExistingConflictsOrCreate(conflicts);
        persistConflicts(conflicts);

    }

    private List<EntityEquivalent> gatherAllEquivalents(OrganizationEquivalent orgEquivalent) {
        List<EntityEquivalent> equivalents = new LinkedList<>(orgEquivalent.getUnwrappedEntities());
        equivalents.add(orgEquivalent);
        return equivalents;
    }

    private void persistConflicts(List<Conflict> conflicts) {
        for (Conflict c : conflicts) {
            em.persist(c);
        }
    }

    private List<Conflict> detect(List<EntityEquivalent> equivalents, SystemAccount owner, SystemAccount accepted) {
        List<Conflict> conflicts = new LinkedList<>();

        for (EntityEquivalent eq : equivalents) {
            Object current = em.find(eq.getClazz(), eq.getBaseResourceId());
            Object mirror = em.find(eq.getClazz(), eq.getPartnerResourceId());

            try {
                ConflictDetector detector = context.getBean(
                    eq.getClazz().getSimpleName() + CONFLICT_DETECTOR_SUFFIX, ConflictDetector.class);
                List<Conflict> noAccountConflicts = detector.detect(current, mirror);
                conflicts.addAll(
                    addAccounts(noAccountConflicts, owner, accepted));
            } catch (NoSuchBeanDefinitionException ex) {
                log.warn("There is no conflict detector for {}", eq.getClazz().getSimpleName());
            }
        }

        return conflicts;
    }

    private List<Conflict> addAccounts(List<Conflict> noAccountConflicts, SystemAccount owner, SystemAccount accepted) {
        List<Conflict> conflicts = new LinkedList<>();

        for (Conflict conflict : noAccountConflicts) {
            conflict.setOwner(owner);
            conflict.addAcceptedThisChange(accepted);
            conflicts.add(conflict);
        }

        return conflicts;
    }

    private List<Conflict> updateExistingConflictsOrCreate(List<Conflict> unchecked) {
        List<Conflict> conflicts = new LinkedList<>();

        for (Conflict conflict : unchecked) {
            Optional<Conflict> databaseOpt = conflictService.findExistingConflict(conflict.getResourceId(),
                conflict.getCurrentValue(), conflict.getOfferedValue(), conflict.getOwner());
            if (databaseOpt.isPresent()) {
                Conflict databaseConflict = databaseOpt.get();
                conflict.getAcceptedThisChange().forEach(databaseConflict::addAcceptedThisChange);

                conflicts.add(databaseConflict);
            } else {
                conflicts.add(conflict);
            }
        }

        return conflicts;
    }

}
