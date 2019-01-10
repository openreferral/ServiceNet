package org.benetech.servicenet.conflict;

import org.benetech.servicenet.conflict.detector.OrganizationConflictDetector;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ConflictDetectionServiceImpl implements ConflictDetectionService {

    private final Logger log = LoggerFactory.getLogger(ConflictDetectionServiceImpl.class);

    private final EntityManager em;

    private final OrganizationConflictDetector organizationConflictDetector;

    private final OrganizationRepository organizationRepository;

    public ConflictDetectionServiceImpl(EntityManager em, OrganizationConflictDetector organizationConflictDetector,
        OrganizationRepository organizationRepository) {
        this.em = em;
        this.organizationConflictDetector = organizationConflictDetector;
        this.organizationRepository = organizationRepository;
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
            // TODO
            //        Class clazz = Organization.class;
            //        Object obj = em.find(clazz, resourceId);
            //        Object mirrorObj = em.find(clazz, mirrorId);
            Organization organization = organizationRepository.getOne(match.getOrganizationRecord().getId());
            Organization mirror = organizationRepository.getOne(match.getPartnerVersion().getId());

            // TODO context.getBean(providerName + DATA_ADAPTER_SUFFIX, AbstractDataAdapter.class); from DataAdapterFactory
            List<Conflict> noAccountConflicts = organizationConflictDetector.detect(organization, mirror);
            for (Conflict c : noAccountConflicts) {
                c.setOwner(organization.getAccount());
                em.persist(c);
                conflicts.add(c);
            }
        }
        return conflicts;
    }

}
