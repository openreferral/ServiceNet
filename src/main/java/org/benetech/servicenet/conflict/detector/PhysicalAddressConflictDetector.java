package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("PhysicalAddressConflictDetector")
public class PhysicalAddressConflictDetector implements ConflictDetector<PhysicalAddress> {

    @Override
    public List<Conflict> detect(PhysicalAddress current, PhysicalAddress offered) {
        List<Conflict> conflicts = new LinkedList<>();

        if (!this.equals(current.getAttention(), offered.getAttention())) {
            conflicts.add(createConflict(current, current.getAttention(), offered.getAttention()));
        }
        if (!this.equals(current.getAddress1(), offered.getAddress1())) {
            conflicts.add(createConflict(current, current.getAddress1(), offered.getAddress1()));
        }
        if (!this.equals(current.getCity(), offered.getCity())) {
            conflicts.add(createConflict(current, current.getCity(), offered.getCity()));
        }
        if (!this.equals(current.getRegion(), offered.getRegion())) {
            conflicts.add(createConflict(current, current.getRegion(), offered.getRegion()));
        }
        if (!this.equals(current.getStateProvince(), offered.getStateProvince())) {
            conflicts.add(createConflict(current, current.getStateProvince(), offered.getStateProvince()));
        }
        if (!this.equals(current.getPostalCode(), offered.getPostalCode())) {
            conflicts.add(createConflict(current, current.getPostalCode(), offered.getPostalCode()));
        }
        if (!this.equals(current.getCountry(), offered.getCountry())) {
            conflicts.add(createConflict(current, current.getCountry(), offered.getCountry()));
        }

        return conflicts;
    }

    private Conflict createConflict(PhysicalAddress current, String currentValue, String offeredValue) {
        return Conflict.builder()
            .currentValue(currentValue)
            .offeredValue(offeredValue)
            .resourceId(current.getId())
            .build();
    }
}
