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

        conflicts.addAll(detectConflict(current, current.getAttention(), offered.getAttention()));
        conflicts.addAll(detectConflict(current, current.getAddress1(), offered.getAddress1()));
        conflicts.addAll(detectConflict(current, current.getCity(), offered.getCity()));
        conflicts.addAll(detectConflict(current, current.getRegion(), offered.getRegion()));
        conflicts.addAll(detectConflict(current, current.getStateProvince(), offered.getStateProvince()));
        conflicts.addAll(detectConflict(current, current.getPostalCode(), offered.getPostalCode()));
        conflicts.addAll(detectConflict(current, current.getCountry(), offered.getCountry()));

        return conflicts;
    }

    @Override
    protected <Z> Conflict createConflict(PhysicalAddress current, Z currentValue, Z offeredValue) {
        Conflict conflict = super.createConflict(current, currentValue, offeredValue);
        conflict.setResourceId(current.getId());
        return conflict;
    }
}
