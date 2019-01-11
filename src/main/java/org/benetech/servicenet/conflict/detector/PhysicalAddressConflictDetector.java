package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("PhysicalAddressConflictDetector")
public class PhysicalAddressConflictDetector extends Detector<PhysicalAddress> implements ConflictDetector<PhysicalAddress> {

    @Override
    public List<Conflict> detect(PhysicalAddress current, PhysicalAddress offered) {
        List<Conflict> conflicts = new LinkedList<>();

        conflicts.addAll(detectConflict(current, current.getAttention(), offered.getAttention(), "attention"));
        conflicts.addAll(detectConflict(current, current.getAddress1(), offered.getAddress1(), "address1"));
        conflicts.addAll(detectConflict(current, current.getCity(), offered.getCity(), "city"));
        conflicts.addAll(detectConflict(current, current.getRegion(), offered.getRegion(), "region"));
        conflicts.addAll(detectConflict(current, current.getStateProvince(), offered.getStateProvince(),
            "stateProvince"));
        conflicts.addAll(detectConflict(current, current.getPostalCode(), offered.getPostalCode(), "postalCode"));
        conflicts.addAll(detectConflict(current, current.getCountry(), offered.getCountry(), "country"));

        return conflicts;
    }

    @Override
    protected <Z> Conflict createConflict(PhysicalAddress current, Z currentValue, Z offeredValue, String fieldName) {
        Conflict conflict = super.createConflict(current, currentValue, offeredValue, fieldName);
        conflict.setResourceId(current.getId());
        return conflict;
    }
}
