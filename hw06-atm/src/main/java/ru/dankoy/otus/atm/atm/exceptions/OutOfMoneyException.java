package ru.dankoy.otus.atm.atm.exceptions;

import ru.dankoy.otus.atm.atm.AutomatedTellerMachine;

/**
 * @author Evgeny
 * Исключение выбрасывается, когда сопрошенная сумма клиентом больше чем общее количество денег в банкомате.
 */
public class OutOfMoneyException extends Exception {

    public OutOfMoneyException(int moneyRequested, AutomatedTellerMachine atm) {
        super("Error: requested: " + moneyRequested + " is out of " + atm.getSumOfAllBanknotesInAtm());
    }

}
