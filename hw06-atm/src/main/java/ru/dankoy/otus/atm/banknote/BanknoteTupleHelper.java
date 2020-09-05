package ru.dankoy.otus.atm.banknote;

/**
 * @author Evgeny
 * Вспомогательный класс, наследующийся от класса Banknote, и содержащий поле - количество банкнот определенного типа.
 * В целом нужен был только для создания списка банкнот, которые можно положить в ATM.
 */
public class BanknoteTupleHelper extends Banknote {

    private int amount;

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return super.toString() + ", amount=" + amount + '}';
    }

    public BanknoteTupleHelper(Bill bill, int amount) {
        super(bill);
        this.amount = amount;
    }
}
