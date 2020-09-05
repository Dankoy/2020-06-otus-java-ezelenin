package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.atm.exceptions.OutOfMoneyException;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;

public interface AutomatedTellerMachine {

    long getSumOfAllBanknotesInAtm();

    void putMoney(List<Banknote> banknotes);

    List<Banknote> claimMoney(int money) throws Exception;

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
