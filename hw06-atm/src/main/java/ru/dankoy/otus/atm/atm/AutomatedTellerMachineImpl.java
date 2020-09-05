package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.atm.exceptions.NotEnoughBanknotesException;
import ru.dankoy.otus.atm.atm.exceptions.OutOfMoneyException;
import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.BanknoteTupleHelper;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.ArrayList;
import java.util.List;

public class AutomatedTellerMachineImpl implements AutomatedTellerMachine {

    private List<Banknote> banknoteTenRub;
    private List<Banknote> banknoteFiftyRub;
    private List<Banknote> banknoteOneHundredRub;
    private List<Banknote> banknoteFiveHundredRub;
    private List<Banknote> banknoteOneThousandRub;
    private long sumOfAllBanknotesInAtm;

    @Override
    public long getSumOfAllBanknotesInAtm() {
        return sumOfAllBanknotesInAtm;
    }

    @Override
    public String toString() {
        return "AutomatedTellerMachine{" +
                "banknoteTen=" + banknoteTenRub +
                ", banknoteFifty=" + banknoteFiftyRub +
                ", banknoteOneHundred=" + banknoteOneHundredRub +
                ", banknoteFiveHundred=" + banknoteFiveHundredRub +
                ", banknoteOneThousand=" + banknoteOneThousandRub +
                '}';
    }

    /**
     * Конструктор ATM заполняющий банкноты на старте.
     * Указывается количество банкнот каждого типа.
     *
     * @param banknoteTenAmount
     * @param banknoteFiftyAmount
     * @param banknoteOneHundredAmount
     * @param banknoteFiveHundredAmount
     * @param banknoteOneThousandAmount
     */
    public AutomatedTellerMachineImpl(int banknoteTenAmount, int banknoteFiftyAmount, int banknoteOneHundredAmount,
            int banknoteFiveHundredAmount, int banknoteOneThousandAmount) {

        this.banknoteTenRub = populateBanknote(Bill.TEN, banknoteTenAmount);
        this.banknoteFiftyRub = populateBanknote(Bill.FIFTY, banknoteFiftyAmount);
        this.banknoteOneHundredRub = populateBanknote(Bill.HUNDRED, banknoteOneHundredAmount);
        this.banknoteFiveHundredRub = populateBanknote(Bill.FIVE_HUNDRED, banknoteFiveHundredAmount);
        this.banknoteOneThousandRub = populateBanknote(Bill.THOUSAND, banknoteOneThousandAmount);
        this.sumOfAllBanknotesInAtm = getSumOfAllBanknotes();

    }

    /**
     * Метод реализующий прием различных купюр.
     *
     * @param banknotes
     */
    @Override
    public void putMoney(List<Banknote> banknotes) {

        for (Banknote banknote : banknotes) {

            switchCheckBillTypeAndInsertBanknote(banknote);

        }

        this.sumOfAllBanknotesInAtm = getSumOfAllBanknotes();

    }

    /**
     * Возвращает массив банкнот запрошенного клиентом.
     *
     * @return
     */
    @Override
    public List<Banknote> claimMoney(int moneyToClaim) throws Exception {

        List<BanknoteTupleHelper> banknoteTupleHelperList = new ArrayList<>();

        if (moneyToClaim > this.getSumOfAllBanknotes()) {
            throw new OutOfMoneyException(moneyToClaim, this);
        } else {

            Bill[] bills = Bill.values();

            for (int i = Bill.values().length - 1; i >= 0; i--) {
                if (moneyToClaim >= bills[i].getValue()) {
                    banknoteTupleHelperList.add(getAmountOfBanknotesNeeded(bills[i], moneyToClaim));
                }
                moneyToClaim = moneyToClaim % bills[i].getValue();
            }
        }

        try {

            checkIfThereAreEnoughRequestedBanknotes(banknoteTupleHelperList);
            return cashPopulationHelper(banknoteTupleHelperList);

        } catch (NotEnoughBanknotesException e) {
            throw e;
        }


    }

    /**
     * Метод который из списка объектов BanknoteTupleHelper, содержащих тип купюры и ее количество, формирует список
     * из банкнот нужных номиналов в нужном количестве.
     *
     * @param banknoteTupleHelpers
     * @return
     */
    public static List<Banknote> cashPopulationHelper(List<BanknoteTupleHelper> banknoteTupleHelpers) {

        List<Banknote> banknotes = new ArrayList<>();

        for (BanknoteTupleHelper banknoteTupleHelper : banknoteTupleHelpers) {

            for (int i = 0; i < banknoteTupleHelper.getAmount(); i++) {
                banknotes.add(new Banknote(banknoteTupleHelper.getBill()));
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
    private BanknoteTupleHelper getAmountOfBanknotesNeeded(Bill bill, int moneyToClaim) {
        int wholed = moneyToClaim / bill.getValue();
        return new BanknoteTupleHelper(bill, wholed);
    }

    /**
     * Проверка типа купюры и вставка в нужную ячейку
     *
     * @param banknote
     */
    private void switchCheckBillTypeAndInsertBanknote(Banknote banknote) {

        switch (banknote.getBill()) {
            case TEN -> this.banknoteTenRub.add(banknote);
            case FIFTY -> this.banknoteFiftyRub.add(banknote);
            case HUNDRED -> this.banknoteOneHundredRub.add(banknote);
            case FIVE_HUNDRED -> this.banknoteFiveHundredRub.add(banknote);
            case THOUSAND -> this.banknoteOneThousandRub.add(banknote);
        }

    }

    /**
     * Возвращает количество денег в банкомате
     *
     * @return
     */
    private long getSumOfAllBanknotes() {

        return ((banknoteTenRub.size() * Bill.TEN.getValue()) + banknoteFiftyRub.size() * Bill.FIFTY.getValue() +
                banknoteOneHundredRub.size() * Bill.HUNDRED.getValue() + banknoteFiveHundredRub.size() * Bill.FIVE_HUNDRED.getValue() +
                banknoteOneThousandRub.size() * Bill.THOUSAND.getValue());

    }

    /**
     * Проверка и изъятие банкнот из банкомата из каждой ячейки.
     * Если банкнот определенного типа в банкомате меньше чем запрошено, то выбрасывается исключение
     * NotEnoughBanknotesException
     *
     * @param requestedBankotes
     */
    private void checkIfThereAreEnoughRequestedBanknotes(List<BanknoteTupleHelper> requestedBankotes) throws
            NotEnoughBanknotesException {

        for (BanknoteTupleHelper requestedBanknote : requestedBankotes) {

            switch (requestedBanknote.getBill()) {
                case TEN:
                    for (int i = requestedBanknote.getAmount() - 1; i >= 0; i--) {
                        try {
                            banknoteTenRub.remove(i);
                        } catch (Exception e) {
                            throw new NotEnoughBanknotesException(requestedBanknote);
                        }
                    }
                    break;
                case FIFTY:
                    for (int i = requestedBanknote.getAmount() - 1; i >= 0; i--) {
                        try {
                            banknoteFiftyRub.remove(i);
                        } catch (Exception e) {
                            throw new NotEnoughBanknotesException(requestedBanknote);
                        }
                    }
                    break;
                case HUNDRED:
                    for (int i = requestedBanknote.getAmount() - 1; i >= 0; i--) {
                        try {
                            banknoteOneHundredRub.remove(i);
                        } catch (Exception e) {
                            throw new NotEnoughBanknotesException(requestedBanknote);
                        }
                    }
                    break;
                case FIVE_HUNDRED:
                    for (int i = requestedBanknote.getAmount() - 1; i >= 0; i--) {
                        try {
                            banknoteFiveHundredRub.remove(i);
                        } catch (Exception e) {
                            throw new NotEnoughBanknotesException(requestedBanknote);
                        }
                    }
                    break;
                case THOUSAND:
                    for (int i = requestedBanknote.getAmount() - 1; i >= 0; i--) {
                        try {
                            banknoteOneThousandRub.remove(i);
                        } catch (Exception e) {
                            throw new NotEnoughBanknotesException(requestedBanknote);
                        }
                    }
                    break;
            }

        }
    }

}
