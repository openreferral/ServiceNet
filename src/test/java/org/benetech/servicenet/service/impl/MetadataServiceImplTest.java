package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.benetech.servicenet.mother.MetadataMother;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.service.MetadataService;
import org.benetech.servicenet.service.mapper.MetadataMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class MetadataServiceImplTest {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private EntityManager em;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        metadataService = new MetadataServiceImpl(metadataRepository, metadataMapper);
    }

    @Test
    @Transactional
    public void shouldFindCurrentMetadataForConflict() {
        Metadata metadataCurrent = MetadataMother.createDefaultAndPersist(em);
        MetadataMother.createDifferentAndPersist(em);

        Optional<Metadata> result = metadataService.findMetadataForConflict(metadataCurrent.getResourceId(),
            metadataCurrent.getFieldName(), metadataCurrent.getReplacementValue());

        assertTrue(result.isPresent());
        assertEquals(metadataCurrent.getResourceId(), result.get().getResourceId());
        assertEquals(metadataCurrent.getLastActionDate(), result.get().getLastActionDate());
        assertEquals(metadataCurrent.getLastActionType(), result.get().getLastActionType());
        assertEquals(metadataCurrent.getFieldName(), result.get().getFieldName());
        assertEquals(metadataCurrent.getPreviousValue(), result.get().getPreviousValue());
        assertEquals(metadataCurrent.getReplacementValue(), result.get().getReplacementValue());
        assertEquals(metadataCurrent.getResourceClass(), result.get().getResourceClass());
    }

    @Test
    @Transactional
    public void shouldFindOfferedMetadataForConflict() {
        MetadataMother.createDefaultAndPersist(em);
        Metadata metadataOffered = MetadataMother.createDifferentAndPersist(em);

        Optional<Metadata> result = metadataService.findMetadataForConflict(metadataOffered.getResourceId(),
            metadataOffered.getFieldName(), metadataOffered.getReplacementValue());

        assertTrue(result.isPresent());
        assertEquals(metadataOffered.getResourceId(), result.get().getResourceId());
        assertEquals(metadataOffered.getLastActionDate(), result.get().getLastActionDate());
        assertEquals(metadataOffered.getLastActionType(), result.get().getLastActionType());
        assertEquals(metadataOffered.getFieldName(), result.get().getFieldName());
        assertEquals(metadataOffered.getPreviousValue(), result.get().getPreviousValue());
        assertEquals(metadataOffered.getReplacementValue(), result.get().getReplacementValue());
        assertEquals(metadataOffered.getResourceClass(), result.get().getResourceClass());
    }

    @Test
    @Transactional
    public void shouldFindAllFieldsMetadataForConflict() {
        MetadataMother.createDefaultAndPersist(em);
        Metadata metadataAllFields = MetadataMother.createAllFieldsAndPersist(em);

        Optional<Metadata> result = metadataService.findMetadataForConflict(metadataAllFields.getResourceId(),
            metadataAllFields.getFieldName(), metadataAllFields.getReplacementValue());

        assertTrue(result.isPresent());
        assertEquals(metadataAllFields.getResourceId(), result.get().getResourceId());
        assertEquals(metadataAllFields.getLastActionDate(), result.get().getLastActionDate());
        assertEquals(ActionType.CREATE, result.get().getLastActionType());
        assertEquals(metadataAllFields.getFieldName(), result.get().getFieldName());
        assertNull(result.get().getPreviousValue());
        assertNull(result.get().getReplacementValue());
        assertEquals(metadataAllFields.getResourceClass(), result.get().getResourceClass());
    }
}
