package ru.dankoy.otus.atm.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dankoy.otus.atm.atm.AutomatedTellerMachine;
import ru.dankoy.otus.atm.atm.AutomatedTellerMachineImpl;
import ru.dankoy.otus.atm.atm.exceptions.NotEnoughBanknotesException;
import ru.dankoy.otus.atm.atm.exceptions.OutOfMoneyException;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AtmTest {

    private AutomatedTellerMachine atm;
    private List<Banknote> clientBanknotes = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        // Общая сумма в банкомате при инициализации - 8270
        atm = new AutomatedTellerMachineImpl().newBuilder().setBanknoteFiftyRub(5).setBanknoteTenRub(2)
                .setBanknoteHundredRub(5).setBanknoteFiveHundredRub(5).setBanknoteThousandRub(5).build();

        // Определяем сколько купюр и какого номинала достаем из штанин.
        // Общая сумма равна - 3320
        clientBanknotes.add(new Banknote(Bill.FIFTY));
        clientBanknotes.add(new Banknote(Bill.FIFTY));
        clientBanknotes.add(new Banknote(Bill.TEN));
        clientBanknotes.add(new Banknote(Bill.TEN));
        clientBanknotes.add(new Banknote(Bill.HUNDRED));
        clientBanknotes.add(new Banknote(Bill.HUNDRED));
        clientBanknotes.add(new Banknote(Bill.FIVE_HUNDRED));
        clientBanknotes.add(new Banknote(Bill.FIVE_HUNDRED));
        clientBanknotes.add(new Banknote(Bill.THOUSAND));
        clientBanknotes.add(new Banknote(Bill.THOUSAND));

//        clientBanknotes = AutomatedTellerMachineImpl.cashPopulationHelper(banknoteTupleHelperList);

    }

    @Test
    public void testInsertMoneyInAtm() {

        // Проверка метода putMoney
        atm.putMoney(clientBanknotes);
        assertThat(atm.getSumOfAllBanknotesInAtm()).isEqualTo(8270 + 3320);

    }

    @Test
    public void testClaimMoneyFromAtm() throws Exception {

        List<Banknote> claimedMoney = atm.claimMoney(3170);
        assertThat(claimedMoney).extracting(Banknote::getBill).hasSize(7).contains(
                Bill.HUNDRED, Bill.TEN, Bill.TEN, Bill.FIFTY, Bill.THOUSAND, Bill.THOUSAND, Bill.THOUSAND);

    }

    @Test
    public void testClaimTooMuchMoneyFromAtm() throws Exception {

        assertThatThrownBy(() -> {
            List<Banknote> claimedMoney = atm.claimMoney(100000);
        }).isInstanceOf(OutOfMoneyException.class);

    }

    @Test
    public void testClaimAtmDoesntHaveEnoughOfBanknotes() throws Exception {

        assertThatThrownBy(() -> {
            List<Banknote> claimedMoney = atm.claimMoney(5590);
        }).isInstanceOf(NotEnoughBanknotesException.class);

    }

}
