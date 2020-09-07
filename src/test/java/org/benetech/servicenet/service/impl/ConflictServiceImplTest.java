package org.benetech.servicenet.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.service.ConflictService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ConflictServiceImplTest {

    @Autowired
    private ConflictRepository conflictRepository;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void shouldDeleteByResourceId() {
        UUID resourceId = UUID.randomUUID();

        SystemAccount systemAccount = SystemAccountMother.createDefaultAndPersist(em);

        Conflict conflict = new Conflict();
        conflict.setOwner(systemAccount);
        conflict.setResourceId(resourceId);
        em.persist(conflict);

        Conflict conflictMirror = new Conflict();
        conflictMirror.setOwner(systemAccount);
        conflictMirror.setPartnerResourceId(resourceId);
        em.persist(conflictMirror);

        em.flush();

        assertEquals(2, conflictRepository.findAll().size());

        conflictService.deleteByResourceOrPartnerResourceId(resourceId);

        assertEquals(0, conflictRepository.findAll().size());
    }
}
