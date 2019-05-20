package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:booleanExpressionComplexity")
@Component("PhysicalAddressConflictDetector")
public class PhysicalAddressConflictDetector extends AbstractDetector<PhysicalAddress> implements
    ConflictDetector<PhysicalAddress> {

    @Override
    public List<Conflict> detectConflicts(PhysicalAddress current, PhysicalAddress offered) {
        List<Conflict> conflicts = new LinkedList<>();

        conflicts.addAll(detectConflicts(current, offered, current.getAttention(), offered.getAttention(), "attention"));
        conflicts.addAll(detectConflicts(current, offered, current.getAddress1(), offered.getAddress1(), "address1"));
        conflicts.addAll(detectConflicts(current, offered, current.getCity(), offered.getCity(), "city"));
        conflicts.addAll(detectConflicts(current, offered, current.getRegion(), offered.getRegion(), "region"));
        conflicts.addAll(detectConflicts(current, offered, current.getStateProvince(), offered.getStateProvince(),
            "stateProvince"));
        conflicts.addAll(detectConflicts(current, offered, current.getPostalCode(), offered.getPostalCode(), "postalCode"));
        conflicts.addAll(detectConflicts(current, offered, current.getCountry(), offered.getCountry(), "country"));

        return conflicts;
    }
}
