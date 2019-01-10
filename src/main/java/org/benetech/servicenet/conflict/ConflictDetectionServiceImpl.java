package org.benetech.servicenet.conflict;

import org.benetech.servicenet.conflict.detector.OrganizationConflictDetector;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ConflictDetectionServiceImpl {

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
     * @param //TODO
     */
    @Async
    public List<Conflict> detect(UUID resourceId, UUID mirrorId) {
        log.debug("Request to detect conflicts for {} organization", resourceId);
        // TODO
        //        Class clazz = Organization.class;
        //        Object obj = em.find(clazz, resourceId);
        //        Object mirrorObj = em.find(clazz, mirrorId);
        Organization current = organizationRepository.getOne(resourceId);
        Organization mirror = organizationRepository.getOne(mirrorId);

        // TODO context.getBean(providerName + DATA_ADAPTER_SUFFIX, AbstractDataAdapter.class); from DataAdapterFactory
        List<Conflict> conflicts = organizationConflictDetector.detect(current, mirror);
        for (Conflict c : conflicts) {
            c.setOwner(current.getAccount());
            em.persist(c);
        }
        return conflicts;
    }

}
