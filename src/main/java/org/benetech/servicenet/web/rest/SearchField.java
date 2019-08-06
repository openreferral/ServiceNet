package org.benetech.servicenet.web.rest;

public enum SearchField {
    NAME("name"),
    ALTERNATE_NAME("alternateName"),
    DESCRIPTION("description"),
    EMAIL("email"),
    URL("url"),
    TAX_STATUS("taxStatus"),
    TAX_ID("taxId"),
    LEGAL_STATUS("legalStatus"),
    EXTERNAL_DB_ID("externalDbId"),
    STATUS("status"),
    INTERPRETATION_SERVICES("interpretationServices"),
    APPLICATION_PROCESS("applicationProcess"),
    WAIT_TIME("waitTime"),
    FEES("fees"),
    ACCREDITATIONS("accreditations"),
    LICENSES("licenses"),
    TYPE("type"),
    PROVIDER_NAME("providerName"),
    TRANSPORTATION("transportation");

    private String value;

    SearchField(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SearchField fromValue(String value) {
        for (SearchField so : SearchField.values()) {
            if (so.value.equalsIgnoreCase(value)) {
                return so;
            }
        }
        return null;
    }
}
