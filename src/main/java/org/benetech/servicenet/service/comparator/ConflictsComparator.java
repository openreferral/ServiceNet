package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.ConflictDTO;

import java.util.Comparator;

public class ConflictsComparator implements Comparator<ConflictDTO> {

    private static final int ADDRESS_PRIORITY = 30;
    private static final int ORG_PHONE_PRIORITY = 20;
    private static final int ORG_NAME_PRIORITY = 10;

    @Override
    public int compare(ConflictDTO c1, ConflictDTO c2) {
        return compareFieldNames(c1, c2);
    }

    private int compareFieldNames(ConflictDTO c1, ConflictDTO c2) {
        return Integer.compare(
            getFieldPriority(c2.getFieldName(), c2.getEntityPath()),
            getFieldPriority(c1.getFieldName(), c1.getEntityPath()));
    }

    private int getFieldPriority(String fieldName, String entityPath) {
        if (isAddress(entityPath)) {
            return ADDRESS_PRIORITY;
        } else if (isOrganization(entityPath)) {
            return getOrganizationFieldPriority(fieldName);
        } else {
            return 0;
        }
    }

    private boolean isOrganization(String entityPath) {
        return entityPath.equals(Organization.class.getCanonicalName());
    }

    private boolean isAddress(String entityPath) {
        return entityPath.equals(Location.class.getCanonicalName())
        || entityPath.equals(PhysicalAddress.class.getCanonicalName())
        || entityPath.equals(PostalAddress.class.getCanonicalName());
    }

    private int getOrganizationFieldPriority(String name) {
        //TODO: prioritize Service Type when it will be supported
        switch (name) {
            case "phone":
                return ORG_PHONE_PRIORITY;
            case "name":
                return ORG_NAME_PRIORITY;
            default:
                return 0;
        }
    }
}
