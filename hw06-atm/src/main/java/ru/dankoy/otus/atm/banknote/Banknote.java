package ru.dankoy.otus.atm.banknote;

/**
 * @author Evgeny 04-09-2020
 * Описание банкноты.
 */
public class Banknote {

    private final Bill bill;

    public Bill getBill() {
        return this.bill;
    }

    public Banknote(Bill bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "bill=" + bill +
                '}';
    }

}
