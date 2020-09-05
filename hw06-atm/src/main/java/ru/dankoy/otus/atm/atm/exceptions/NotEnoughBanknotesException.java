package ru.dankoy.otus.atm.atm.exceptions;

import ru.dankoy.otus.atm.banknote.BanknoteTupleHelper;

/**
 * @author Evgeny
 * Исключение выбрасывается, когда купюр какого-то номинала не хватило, что бы выдать клиенту.
 */
public class NotEnoughBanknotesException extends Exception {

    public NotEnoughBanknotesException(BanknoteTupleHelper banknoteTupleHelper) {
        super("Error: ATM doesn't have enough banknotes " + banknoteTupleHelper);
    }

}
