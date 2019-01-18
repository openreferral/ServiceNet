package org.benetech.servicenet.adapter.smcconnect.model;

public enum SmcWeekday {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private int number;

    SmcWeekday(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
