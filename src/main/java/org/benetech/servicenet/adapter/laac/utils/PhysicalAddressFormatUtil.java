package org.benetech.servicenet.adapter.laac.utils;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.PhysicalAddress;

public final class PhysicalAddressFormatUtil {

    private static final String COMMA_DELIMITER = ",";
    private static final String SPACE_DELIMITER = " ";
    private static final String NEW_LINE_DELIMITER = "\n";
    private static final String NOT_AVAILABLE = "N/A";
    private static final int MAX_LINES_NUMBER = 4;

    private PhysicalAddressFormatUtil() { }

    public static PhysicalAddress resolveAddress(String addressString) {
        if (StringUtils.isBlank(addressString)) {
            return null;
        }
        PhysicalAddress physicalAddress = new PhysicalAddress();
        String[] addressLines = addressString.split(NEW_LINE_DELIMITER);
        if (addressLines.length < 2 || addressLines.length > MAX_LINES_NUMBER) {
            return null;
        }
        // when == 3 1st is address
        // when == 4 1st is address, 2nd is attention
        // last is Country, last - 1 is City, State Postal Code
        final int countryIndex = addressLines.length - 1;
        final int cityStatePostalIndex = countryIndex - 1;
        int addressIndex = -1;
        int attentionIndex = -1;
        if (addressLines.length > 2) {
            addressIndex = 0;
            if (addressLines.length == MAX_LINES_NUMBER) {
                attentionIndex = 1;
            }
        }

        physicalAddress.setCountry(addressLines[countryIndex]);
        boolean result = handleCityStateAndPostalCode(physicalAddress, addressLines[cityStatePostalIndex]);
        if (!result) {
            return null;
        }

        if (addressIndex != -1) {
            physicalAddress.setAddress1(addressLines[addressIndex]);
        } else {
            physicalAddress.setAddress1(NOT_AVAILABLE);
        }
        if (attentionIndex != -1) {
            physicalAddress.setAttention(addressLines[attentionIndex]);
        }
        return physicalAddress;
    }

    private static boolean handleCityStateAndPostalCode(PhysicalAddress physicalAddress, String cityStatePostal) {
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
}
