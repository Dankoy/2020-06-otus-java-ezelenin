package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;

public interface AutomatedTellerMachine {

    long getSumOfAllBanknotes();

    void putMoney(List<Banknote> banknotes);

    Banknote[] claimMoney();

    /**
     * Метод создающий массив банкнот заданного типа
     *
     * @param bill
     * @param amount
     * @return
     */
    default List<Banknote> populateBanknote(Bill bill, int amount) {

        List<Banknote> banknotes = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            banknotes.add(new Banknote(bill));
        }

        return banknotes;

    }

}
