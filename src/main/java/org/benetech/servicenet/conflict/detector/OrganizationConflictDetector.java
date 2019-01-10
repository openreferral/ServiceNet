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

        conflicts.addAll(detectConflict(current, current.getName(), offered.getName()));
        conflicts.addAll(detectConflict(current, current.getAlternateName(), offered.getAlternateName()));
        conflicts.addAll(detectConflict(current, current.getDescription(), offered.getDescription()));
        conflicts.addAll(detectConflict(current, current.getEmail(), offered.getEmail()));
        conflicts.addAll(detectConflict(current, current.getUrl(), offered.getUrl()));
        conflicts.addAll(detectConflict(current, current.getTaxStatus(), offered.getTaxStatus()));
        conflicts.addAll(detectConflict(current, current.getTaxId(), offered.getTaxId()));
        conflicts.addAll(detectConflict(current, current.getYearIncorporated(), offered.getYearIncorporated()));
        conflicts.addAll(detectConflict(current, current.getLegalStatus(), offered.getLegalStatus()));
        conflicts.addAll(detectConflict(current, current.getActive(), offered.getActive()));
        conflicts.addAll(detectConflict(current, current.getProviderName(), offered.getProviderName()));

        return conflicts;
    }
}
