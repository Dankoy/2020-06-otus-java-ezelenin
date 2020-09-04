package ru.dankoy.otus.atm.atm;

import ru.dankoy.otus.atm.banknote.Banknote;
import ru.dankoy.otus.atm.banknote.Bill;

import java.util.List;

public class AutomatedTellerMachineImpl implements AutomatedTellerMachine {

    private List<Banknote> banknoteTwentyRub;
    private List<Banknote> banknoteFiftyRub;
    private List<Banknote> banknoteOneHundredRub;
    private List<Banknote> banknoteFiveHundredRub;
    private List<Banknote> banknoteOneThousandRub;

    @Override
    public String toString() {
        return "AutomatedTellerMachine{" +
                "banknoteTwenty=" + banknoteTwentyRub +
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
     * @param banknoteTwentyAmount
     * @param banknoteFiftyAmount
     * @param banknoteOneHundredAmount
     * @param banknoteFiveHundredAmount
     * @param banknoteOneThousandAmount
     */
    public AutomatedTellerMachineImpl(int banknoteTwentyAmount, int banknoteFiftyAmount, int banknoteOneHundredAmount,
            int banknoteFiveHundredAmount, int banknoteOneThousandAmount) {

        this.banknoteTwentyRub = populateBanknote(Bill.TEN, banknoteTwentyAmount);
        this.banknoteFiftyRub = populateBanknote(Bill.FIFTY, banknoteFiftyAmount);
        this.banknoteOneHundredRub = populateBanknote(Bill.HUNDRED, banknoteOneHundredAmount);
        this.banknoteFiveHundredRub = populateBanknote(Bill.FIVE_HUNDRED, banknoteFiveHundredAmount);
        this.banknoteOneThousandRub = populateBanknote(Bill.THOUSAND, banknoteOneThousandAmount);

    }

    /**
     * Возвращает количество денег в банкомате
     *
     * @return
     */
    @Override
    public long getSumOfAllBanknotes() {

        return ((banknoteTwentyRub.size() * Bill.TEN.getValue()) + banknoteFiftyRub.size() * Bill.FIFTY.getValue() +
                banknoteOneHundredRub.size() * Bill.HUNDRED.getValue() + banknoteFiveHundredRub.size() * Bill.FIVE_HUNDRED.getValue() +
                banknoteOneThousandRub.size() * Bill.THOUSAND.getValue());

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

    }

    //TODO Дописать
    /**
     * Возвращает массив банкнот запрошенного клиентом.
     *
     * @return
     */
    @Override
    public Banknote[] claimMoney() {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверка типа купюры и вставка в нужную ячейку
     *
     * @param banknote
     */
    private void switchCheckBillTypeAndInsertBanknote(Banknote banknote) {

        switch (banknote.getBill()) {
            case TEN -> this.banknoteTwentyRub.add(banknote);
            case FIFTY -> this.banknoteFiftyRub.add(banknote);
            case HUNDRED -> this.banknoteOneHundredRub.add(banknote);
            case FIVE_HUNDRED -> this.banknoteFiveHundredRub.add(banknote);
            case THOUSAND -> this.banknoteOneThousandRub.add(banknote);
        }

    }


}
