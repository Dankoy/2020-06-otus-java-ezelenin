package ru.dankoy.otus.atm.atm.exceptions;

import ru.dankoy.otus.atm.banknote.Bill;

import java.util.Map;

/**
 * @author Evgeny
 * Исключение выбрасывается, когда купюр какого-то номинала не хватило, что бы выдать клиенту.
 */
public class NotEnoughBanknotesException extends Exception {

    public NotEnoughBanknotesException(Map<Bill, Long> requestedBanknotes) {
        super("Error: ATM doesn't have enough banknotes " + requestedBanknotes);
    }

}
