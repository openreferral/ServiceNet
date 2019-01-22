package org.benetech.servicenet.adapter.laac.utils;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.PhysicalAddress;

import java.util.Optional;

public final class PhysicalAddressFormatUtil {

    private static final String COMMA_DELIMITER = ",";
    private static final String SPACE_DELIMITER = " ";
    private static final String NEW_LINE_DELIMITER = "\n";
    private static final String NOT_AVAILABLE = "N/A";
    private static final int MAX_LINES_NUMBER = 4;

    private PhysicalAddressFormatUtil() { }

    public static Optional<PhysicalAddress> resolveAddress(String addressString) {
        if (StringUtils.isBlank(addressString)) {
            return Optional.empty();
        }
        // when == 3 1st is address
        // when == 4 1st is address, 2nd is attention
        // last is Country, last - 1 is City, State Postal Code
        String[] addressLines = addressString.split(NEW_LINE_DELIMITER);
        if (addressLines.length < 2 || addressLines.length > MAX_LINES_NUMBER) {
            return Optional.empty();
        }

        PhysicalAddress physicalAddress = new PhysicalAddress();
        boolean dataPresent = setCityStateAndPostalCodeIfPresent(physicalAddress, addressLines);
        if (!dataPresent) {
            return Optional.empty();
        }
        physicalAddress.setCountry(getCountry(addressLines));
        physicalAddress.setAddress1(getAddress(addressLines));
        physicalAddress.setAttention(getAttention(addressLines));

        return Optional.of(physicalAddress);
    }

    private static boolean setCityStateAndPostalCodeIfPresent(PhysicalAddress physicalAddress, String[] addressLines) {
        final int cityStatePostalIndex = addressLines.length - 2;
        String cityStatePostal = addressLines[cityStatePostalIndex];
        String[] addressParts = cityStatePostal.split(COMMA_DELIMITER);
        if (addressParts.length != 2) {
            return false;
        }
        physicalAddress.setCity(addressParts[0]);
        String[] statePostalParts = addressParts[1].split(SPACE_DELIMITER);
        physicalAddress.setStateProvince(statePostalParts[statePostalParts.length - 2]);
        physicalAddress.setPostalCode(statePostalParts[statePostalParts.length - 1]);
        return true;
    }

    private static String getAddress(String[] addressLines) {
        if (addressLines.length > 2) {
            return addressLines[0];
        }
        return NOT_AVAILABLE;
    }

    private static String getAttention(String[] addressLines) {
        if (addressLines.length == MAX_LINES_NUMBER) {
            return addressLines[1];
        }
        return null;
    }

    private static String getCountry(String[] addressLines) {
        return addressLines[addressLines.length - 1];
    }
}
