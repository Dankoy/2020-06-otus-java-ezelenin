package ru.dankoy.otus.atm;

import ru.dankoy.otus.atm.atm.AutomatedTellerMachine;
import ru.dankoy.otus.atm.atm.AutomatedTellerMachineImpl;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.BanknoteTupleHelper;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String... args) throws Exception {

        AutomatedTellerMachine atm = new AutomatedTellerMachineImpl(1, 1,
                1, 1, 1);
        System.out.println(atm);
        System.out.println(atm.getSumOfAllBanknotesInAtm());

        // Определяем сколько купюр и какого номинала достаем из штанин.
        List<BanknoteTupleHelper> banknoteTupleHelperList = new ArrayList<>();
        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.TEN, 2));
        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.FIFTY, 2));
        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.HUNDRED, 2));
        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.FIVE_HUNDRED, 2));
        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.THOUSAND, 2));

        List<Banknote> clientBanknotes = AutomatedTellerMachineImpl.cashPopulationHelper(banknoteTupleHelperList);

        // Проверка метода putMoney
        atm.putMoney(clientBanknotes);

        System.out.println(atm);
        System.out.println(atm.getSumOfAllBanknotesInAtm());

        List<Banknote> claimedMoney = atm.claimMoney(3180);
        System.out.println(claimedMoney);

        System.out.println(atm);

    }

}
