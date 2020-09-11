package ru.dankoy.otus.atm;

import ru.dankoy.otus.atm.atm.AutomatedTellerMachine;
import ru.dankoy.otus.atm.atm.AutomatedTellerMachineImpl;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {

//        AutomatedTellerMachine atm = new AutomatedTellerMachineImpl(1, 1,
//                1, 1, 1);
//        System.out.println(atm);
//        System.out.println(atm.getSumOfAllBanknotesInAtm());

        // Определяем сколько купюр и какого номинала достаем из штанин.
//        List<BanknoteTupleHelper> banknoteTupleHelperList = new ArrayList<>();
//        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.TEN, 2));
//        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.FIFTY, 2));
//        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.HUNDRED, 2));
//        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.FIVE_HUNDRED, 2));
//        banknoteTupleHelperList.add(new BanknoteTupleHelper(Bill.THOUSAND, 2));

//        List<Banknote> clientBanknotes = AutomatedTellerMachineImpl.cashPopulationHelper(banknoteTupleHelperList);

        // Проверка метода putMoney
//        atm.putMoney(clientBanknotes);

//        System.out.println(atm);
//        System.out.println(atm.getSumOfAllBanknotesInAtm());

//        List<Banknote> claimedMoney = atm.claimMoney(3180);
//        System.out.println(claimedMoney);
//
//        System.out.println(atm);

        AutomatedTellerMachine atm2 =
                new AutomatedTellerMachineImpl().newBuilder().setBanknoteFiftyRub(10).setBanknoteTenRub(10)
                        .setBanknoteHundredRub(10).setBanknoteFiveHundredRub(10).setBanknoteThousandRub(10).build();
        System.out.println(atm2);

        Map<Bill, Long> money = new HashMap<>();
        money.put(Bill.TEN, 10L);

        List<Banknote> ban = new ArrayList<>();
        ban.add(new Banknote(Bill.FIFTY));
        ban.add(new Banknote(Bill.FIFTY));
        ban.add(new Banknote(Bill.FIFTY));
        ban.add(new Banknote(Bill.TEN));
        ban.add(new Banknote(Bill.TEN));
        ban.add(new Banknote(Bill.THOUSAND));

        atm2.putMoney(ban);
        System.out.println(atm2);

        List<Banknote> claimed = atm2.claimMoney(5600L);
        System.out.println(atm2);
        System.out.println(claimed);

    }

}
