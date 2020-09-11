package ru.dankoy.otus.atm.banknote;

/**
 * @author Evgeny 04-09-2020
 * Emun для различных сумм банкнот
 */
public enum Bill {

    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000);

    private int value;

    Bill(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
