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

        AutomatedTellerMachine atm2 =
                new AutomatedTellerMachineImpl().newBuilder().setBanknoteFiftyRub(1).setBanknoteTenRub(1)
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

        List<Banknote> claimed = atm2.claimMoney(10690L);
        System.out.println(atm2);
        System.out.println(claimed);

    }

}
