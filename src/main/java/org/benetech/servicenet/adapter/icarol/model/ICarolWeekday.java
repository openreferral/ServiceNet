package org.benetech.servicenet.adapter.icarol.model;

public enum ICarolWeekday {
    MON(0),
    TUE(1),
    WED(2),
    THU(3),
    FRI(4),
    SAT(5),
    SUN(6);

    private int number;

    ICarolWeekday(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
