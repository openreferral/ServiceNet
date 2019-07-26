package org.benetech.servicenet.domain;

public interface Address {
    String DELIMITER = ", ";
    int MAX_ADDRESS_LENGTH = 255;

    String getAddress();

    default String extract255AddressChars() {
        return getAddress().length() <= MAX_ADDRESS_LENGTH
            ? getAddress() : getAddress().substring(0, MAX_ADDRESS_LENGTH);
    }
}
