package org.benetech.servicenet.web.rest;

public enum SearchOn {
    ORGANIZATION("organization"),
    SERVICES("services"),
    LOCATIONS("locations");

    private String value;

    SearchOn(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SearchOn fromValue(String value) {
        for (SearchOn so : SearchOn.values()) {
            if (so.value.equalsIgnoreCase(value)) {
                return so;
            }
        }
        return null;
    }
}
