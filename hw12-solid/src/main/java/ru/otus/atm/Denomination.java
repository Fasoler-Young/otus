package ru.otus.atm;

public enum Denomination {
    _5000(5000),
    _1000(1000),
    _500(500),
    _100(100),
    _50(50),
    _10(10);

    public final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
