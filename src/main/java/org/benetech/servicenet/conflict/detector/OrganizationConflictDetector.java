package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("OrganizationConflictDetector")
public class OrganizationConflictDetector extends Detector<Organization> implements ConflictDetector<Organization> {

    @Override
    public List<Conflict> detect(Organization current, Organization offered) {
        List<Conflict> conflicts = new LinkedList<>();

        conflicts.addAll(detectConflict(current, current.getName(), offered.getName()
            , "name"));
        conflicts.addAll(detectConflict(current, current.getAlternateName(), offered.getAlternateName()
            , "alternateName"));
        conflicts.addAll(detectConflict(current, current.getDescription(), offered.getDescription()
            , "description"));
        conflicts.addAll(detectConflict(current, current.getEmail(), offered.getEmail()
            , "email"));
        conflicts.addAll(detectConflict(current, current.getUrl(), offered.getUrl()
            , "url"));
        conflicts.addAll(detectConflict(current, current.getTaxStatus(), offered.getTaxStatus()
            , "taxStatus"));
        conflicts.addAll(detectConflict(current, current.getTaxId(), offered.getTaxId()
            , "taxId"));
        conflicts.addAll(detectConflict(current, current.getYearIncorporated(), offered.getYearIncorporated()
            , "yearIncorporated"));
        conflicts.addAll(detectConflict(current, current.getLegalStatus(), offered.getLegalStatus()
            , "legalStatus"));
        conflicts.addAll(detectConflict(current, current.getActive(), offered.getActive()
            , "active"));
        conflicts.addAll(detectConflict(current, current.getProviderName(), offered.getProviderName()
            , "providerName"));

        return conflicts;
    }

    @Override
    protected <Z> Conflict createConflict(Organization current, Z currentValue, Z offeredValue, String fieldName) {
        Conflict conflict = super.createConflict(current, currentValue, offeredValue, fieldName);
        conflict.setResourceId(current.getId());
        return conflict;
    }
}
