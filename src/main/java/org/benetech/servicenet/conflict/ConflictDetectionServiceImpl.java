package org.benetech.servicenet.conflict;

import org.benetech.servicenet.conflict.detector.ConflictDetector;
import org.benetech.servicenet.conflict.detector.OrganizationConflictDetector;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.model.EntityEquivalent;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ConflictDetectionServiceImpl implements ConflictDetectionService {

    private static final String CONFLICT_DETECTOR_SUFFIX = "ConflictDetector";

    private final Logger log = LoggerFactory.getLogger(ConflictDetectionServiceImpl.class);

    private final ApplicationContext context;

    private final EntityManager em;

    private final OrganizationConflictDetector organizationConflictDetector;

    private final OrganizationRepository organizationRepository;

    private final OrganizationEquivalentsService organizationEquivalentsService;

    public ConflictDetectionServiceImpl(ApplicationContext context,
                                        EntityManager em,
                                        OrganizationConflictDetector organizationConflictDetector,
                                        OrganizationRepository organizationRepository,
                                        OrganizationEquivalentsService organizationEquivalentsService) {
        this.context = context;
        this.em = em;
        this.organizationConflictDetector = organizationConflictDetector;
        this.organizationRepository = organizationRepository;
        this.organizationEquivalentsService = organizationEquivalentsService;
    }

    /**
     * Detect and persist conflicts for entities asynchronously.
     *
     * @param matches a list of organization matches
     */
    @Async
    public List<Conflict> detect(List<OrganizationMatch> matches) {
        List<Conflict> conflicts = new LinkedList<>();

        for (OrganizationMatch match : matches) {
            log.debug("Request to detect conflicts for {} organization", match.getOrganizationRecord().getName());
            OrganizationEquivalent orgEquivalent = organizationEquivalentsService.generateEquivalent(
                match.getOrganizationRecord(), match.getPartnerVersion());
            Organization organization = organizationRepository.getOne(match.getOrganizationRecord().getId());

            List<EntityEquivalent> equivalents = new LinkedList<>(orgEquivalent.getUnwrappedEntities());
            equivalents.add(orgEquivalent);

            for (EntityEquivalent eq : equivalents) {
                Object current = em.find(eq.getClazz(), eq.getBaseResourceId());
                Object mirror = em.find(eq.getClazz(), eq.getPartnerResourceId());

                ConflictDetector detector = context.getBean(
                    eq.getClazz().getSimpleName() + CONFLICT_DETECTOR_SUFFIX, ConflictDetector.class);
                List<Conflict> noAccountConflicts = detector.detect(current, mirror);

                for (Conflict c : noAccountConflicts) {
                    c.setOwner(organization.getAccount());
                    em.persist(c);
                    conflicts.add(c);
                }
            }
        }
        return conflicts;
    }

}
