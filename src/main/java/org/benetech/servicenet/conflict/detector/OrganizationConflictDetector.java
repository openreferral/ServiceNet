package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("OrganizationConflictDetector")
public class OrganizationConflictDetector extends AbstractDetector<Organization> implements ConflictDetector<Organization> {

    @Override
    public List<Conflict> detect(Organization current, Organization offered) {
        List<Conflict> conflicts = new LinkedList<>();

        conflicts.addAll(detectConflicts(current, current.getName(), offered.getName(), "name"));
        conflicts.addAll(detectConflicts(current, current.getAlternateName(), offered.getAlternateName(),
            "alternateName"));
        conflicts.addAll(detectConflicts(current, current.getDescription(), offered.getDescription(),
            "description"));
        conflicts.addAll(detectConflicts(current, current.getEmail(), offered.getEmail(), "email"));
        conflicts.addAll(detectConflicts(current, current.getUrl(), offered.getUrl(), "url"));
        conflicts.addAll(detectConflicts(current, current.getTaxStatus(), offered.getTaxStatus(), "taxStatus"));
        conflicts.addAll(detectConflicts(current, current.getTaxId(), offered.getTaxId(), "taxId"));
        conflicts.addAll(detectConflicts(current, current.getYearIncorporated(), offered.getYearIncorporated(),
            "yearIncorporated"));
        conflicts.addAll(detectConflicts(current, current.getLegalStatus(), offered.getLegalStatus(),
            "legalStatus"));
        conflicts.addAll(detectConflicts(current, current.getActive(), offered.getActive(), "active"));
        conflicts.addAll(detectConflicts(current, current.getProviderName(), offered.getProviderName(),
            "providerName"));

        return conflicts;
    }

    @Override
    protected <Z> Conflict createConflict(Organization current, Z currentValue, Z offeredValue, String fieldName) {
        Conflict conflict = super.createConflict(current, currentValue, offeredValue, fieldName);
        conflict.setResourceId(current.getId());
        return conflict;
    }
}
