package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.benetech.servicenet.TestConstants.EXISTING_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.NEW_STRING;
import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class TaxonomyImportServiceTest {

    @Autowired
    private TaxonomyImportService importService;

    @Autowired
    private TestPersistanceHelper helper;

    @Autowired
    private TaxonomyService taxonomyService;

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateTaxonomy() {
        Taxonomy taxonomy = helper.generateNewTaxonomy();

        assertEquals(0, taxonomyService.findAll().size());
        importService.createOrUpdateTaxonomy(taxonomy, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<TaxonomyDTO> all = taxonomyService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateTaxonomy() {
        Taxonomy newTaxonomy = helper.generateNewTaxonomy();
        helper.generateExistingTaxonomy();

        assertEquals(1, taxonomyService.findAll().size());
        importService.createOrUpdateTaxonomy(newTaxonomy, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<TaxonomyDTO> all = taxonomyService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
    }
}
