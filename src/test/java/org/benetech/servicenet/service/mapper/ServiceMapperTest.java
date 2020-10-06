package org.benetech.servicenet.service.mapper;

import static org.benetech.servicenet.mother.ServiceMother.ACCREDITATIONS;
import static org.benetech.servicenet.mother.ServiceMother.ALTERNATE_NAME;
import static org.benetech.servicenet.mother.ServiceMother.APPLICATION_PROCESS;
import static org.benetech.servicenet.mother.ServiceMother.DESCRIPTION;
import static org.benetech.servicenet.mother.ServiceMother.EMAIL;
import static org.benetech.servicenet.mother.ServiceMother.FEES;
import static org.benetech.servicenet.mother.ServiceMother.INTERPRETATION_SERVICES;
import static org.benetech.servicenet.mother.ServiceMother.LICENSES;
import static org.benetech.servicenet.mother.ServiceMother.NAME;
import static org.benetech.servicenet.mother.ServiceMother.STATUS;
import static org.benetech.servicenet.mother.ServiceMother.TYPE;
import static org.benetech.servicenet.mother.ServiceMother.URL;
import static org.benetech.servicenet.mother.ServiceMother.WAIT_TIME;
import static org.junit.Assert.assertEquals;

import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.mother.ServiceMother;
import org.benetech.servicenet.service.dto.provider.SimpleServiceDTO;
import org.junit.Test;

public class ServiceMapperTest {

    private ServiceMapper mapper = new ServiceMapperImpl();

    @Test
    public void shouldMapServiceToRecord() {
        Service entry = ServiceMother.createDefault();

        SimpleServiceDTO result = mapper.toSimpleDto(entry);

        assertEquals(NAME, result.getService().getName());
        assertEquals(ALTERNATE_NAME, result.getService().getAlternateName());
        assertEquals(DESCRIPTION, result.getService().getDescription());

        assertEquals(URL, result.getService().getUrl());
        assertEquals(EMAIL, result.getService().getEmail());
        assertEquals(STATUS, result.getService().getStatus());
        assertEquals(INTERPRETATION_SERVICES, result.getService().getInterpretationServices());
        assertEquals(APPLICATION_PROCESS, result.getService().getApplicationProcess());
        assertEquals(WAIT_TIME, result.getService().getWaitTime());
        assertEquals(FEES, result.getService().getFees());
        assertEquals(ACCREDITATIONS, result.getService().getAccreditations());
        assertEquals(LICENSES, result.getService().getLicenses());
        assertEquals(TYPE, result.getService().getType());
    }
}
