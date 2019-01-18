package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechRawData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component("ShelterTechDataAdapter")
public class ShelterTechDataAdapter extends SingleDataAdapter {

    private final Logger log = LoggerFactory.getLogger(ShelterTechDataAdapter.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(SingleImportData importData) {
        if (importData.isFileUpload()) {
            ShelterTechRawData data = ShelterTechParser.collectData(importData.getSingleObjectData());
        }

        return null;
    }

}
