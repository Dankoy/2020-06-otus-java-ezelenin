package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface AutomatedTellerMachine {

    long getSumOfAllBanknotesInAtm();

    void putMoney(List<Banknote> money);

    List<Banknote> claimMoney(long money) throws Exception;

    /**
     * Метод создающий массив банкнот заданного типа
     *
     * @param bill
     * @param amount
     * @return
     */
    @Deprecated
    default List<Banknote> populateBanknote(Bill bill, int amount) {

        List<Banknote> banknotes = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            banknotes.add(new Banknote(bill));
        }

        return banknotes;

    }

    /**
     * Метод конвертирующий List из банкнот, в Map<Bill, Long>: номинал - количество
     *
     * @param banknotes
     * @return
     */
    default Map<Bill, Long> convertListOfMoneyToMap(List<Banknote> banknotes) {
        return banknotes.stream().collect(
                Collectors.groupingBy(
                        banknote -> banknote.getBill(), Collectors.counting()
                )
        );
    }

}
