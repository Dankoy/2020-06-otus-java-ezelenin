package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.atm.exceptions.NotEnoughBanknotesException;
import ru.dankoy.otus.atm.atm.exceptions.OutOfMoneyException;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomatedTellerMachineImpl implements AutomatedTellerMachine {

    private final Map<Bill, Long> money;
    private long sumOfAllBanknotesInAtm;

    public AutomatedTellerMachineImpl() {
        money = new HashMap<>();
    }

    /**
     * Получение билдера
     *
     * @return
     */
    public static AutomatedTellerMachineBuilder newBuilder() {
        return new AutomatedTellerMachineImpl().new AutomatedTellerMachineBuilder();
    }

    @Override
    public long getSumOfAllBanknotesInAtm() {
        return getSumOfAllBanknotes();
    }

    @Override
    public String toString() {
        return "AutomatedTellerMachineImpl{" +
                "money=" + money +
                ", sumOfAllBanknotesInAtm=" + getSumOfAllBanknotes() +
                '}';
    }

    /**
     * Метод реализующий прием различных купюр.
     *
     * @param money
     */
    @Override
    public void putMoney(List<Banknote> money) {

        Map<Bill, Long> result = convertListOfMoneyToMap(money);

        for (Map.Entry<Bill, Long> entry : result.entrySet()) {
            long newAmountOfMoney = entry.getValue() + this.money.get(entry.getKey());
            switchCheckBillTypeAndInsertBanknote(entry.getKey(), newAmountOfMoney);
        }

        this.sumOfAllBanknotesInAtm = getSumOfAllBanknotes();

    }

    /**
     * Возвращает массив банкнот запрошенного клиентом.
     *
     * @return
     */
    @Override
    public List<Banknote> claimMoney(long moneyToClaim) throws Exception {

        Map<Bill, Long> moneyHelper = new HashMap<>();

        if (moneyToClaim > this.getSumOfAllBanknotes()) {
            throw new OutOfMoneyException(moneyToClaim, this);
        } else {

            Bill[] bills = Bill.values();

            for (int i = Bill.values().length - 1; i >= 0; i--) {
                if (moneyToClaim >= bills[i].getValue()) {
                    moneyHelper.put(bills[i],
                            getAmountOfBanknotesNeeded(bills[i], moneyToClaim));
                }
                moneyToClaim = moneyToClaim % bills[i].getValue();
            }
        }

        try {

            checkIfThereAreEnoughRequestedBanknotes(moneyHelper);
            return cashPopulationHelper(moneyHelper);

        } catch (NotEnoughBanknotesException e) {
            throw e;
        }
    }

    /**
     * Метод который из списка объектов BanknoteTupleHelper, содержащих тип купюры и ее количество, формирует список
     * из банкнот нужных номиналов в нужном количестве.
     *
     * @param cash
     * @return
     */
    public List<Banknote> cashPopulationHelper(Map<Bill, Long> cash) {

        List<Banknote> banknotes = new ArrayList<>();

        for (Map.Entry<Bill, Long> entry : cash.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                banknotes.add(new Banknote(entry.getKey()));
            }
        }

        return banknotes;

    }

    /**
     * Приватный метод определяющий необходимое количество купюр нужного номинала и возвращает объект
     * BanknoteTupleHelper, содержащий тип банкноты и ее количество.
     *
     * @param bill
     * @param moneyToClaim
     * @return
     */
    private long getAmountOfBanknotesNeeded(Bill bill, long moneyToClaim) {
        return moneyToClaim / bill.getValue();
    }

    /**
     * Вставка купюры в нужную ячейку
     *
     * @param bill
     * @param amount
     */
    private void switchCheckBillTypeAndInsertBanknote(Bill bill, long amount) {

        this.money.put(bill, amount);

    }

    /**
     * Возвращает количество денег в банкомате
     *
     * @return
     */
    private long getSumOfAllBanknotes() {

        long sum = 0;

        for (Map.Entry<Bill, Long> entry : this.money.entrySet()) {
            sum = sum + (entry.getValue() * entry.getKey().getValue());
        }

        return sum;

    }

    /**
     * Проверка и изъятие банкнот из банкомата из каждой ячейки.
     * Если банкнот определенного типа в банкомате меньше чем запрошено, то выбрасывается исключение
     * NotEnoughBanknotesException
     *
     * @param requestedBankotes
     */
    private void checkIfThereAreEnoughRequestedBanknotes(Map<Bill, Long> requestedBankotes) throws
            NotEnoughBanknotesException {

        for (Map.Entry<Bill, Long> entry : requestedBankotes.entrySet()) {

            if (entry.getValue() > this.money.get(entry.getKey())) {
                throw new NotEnoughBanknotesException(requestedBankotes);
            } else {
                subtractRequestedMoney(entry.getKey(), entry.getValue());
            }

        }
    }

    /**
     * Уменьшает количество банкнот указанного номинала в банкомате
     *
     * @param bill
     * @param amount
     */
    private void subtractRequestedMoney(Bill bill, long amount) {
        long updatedAmount = this.money.get(bill) - amount;
        this.money.put(bill, updatedAmount);
    }

    /**
     * Билдер ATM
     */
    public class AutomatedTellerMachineBuilder {

        private AutomatedTellerMachineBuilder() {
            // private constructor
        }

        public AutomatedTellerMachineBuilder setBanknoteTenRub(long amount) {
            AutomatedTellerMachineImpl.this.money.put(Bill.TEN, amount);
            return this;
        }

        public AutomatedTellerMachineBuilder setBanknoteFiftyRub(long amount) {
            AutomatedTellerMachineImpl.this.money.put(Bill.FIFTY, amount);
            return this;
        }

        public AutomatedTellerMachineBuilder setBanknoteHundredRub(long amount) {
            AutomatedTellerMachineImpl.this.money.put(Bill.HUNDRED, amount);
            return this;
        }

        public AutomatedTellerMachineBuilder setBanknoteFiveHundredRub(long amount) {
            AutomatedTellerMachineImpl.this.money.put(Bill.FIVE_HUNDRED, amount);
            return this;
        }

        public AutomatedTellerMachineBuilder setBanknoteThousandRub(long amount) {
            AutomatedTellerMachineImpl.this.money.put(Bill.THOUSAND, amount);
            return this;
        }

        public AutomatedTellerMachine build() {
            return AutomatedTellerMachineImpl.this;
        }

    }

}
