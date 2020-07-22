package ru.dankoy.otus;

import ru.dankoy.otus.generics.DIYArrayList;

import java.util.*;

public class DIYArrayListMain {

    public static void main(String[] args) {

        List<Integer> integerList1 = new DIYArrayList<>();
        List<Integer> integerList2 = new DIYArrayList<>();
//        List<Double> doubleList = new DIYArrayList<>();

        populateArrayWithIntegers(integerList1, 30);
        populateArrayWithIntegers(integerList2, 30);
//        populateArrayWithDoubles(doubleList);

        printArray(integerList1);
        printArray(integerList2);
//        printArray(doubleList);

//        Заменяет элементы первого листа вторым
        Collections.copy(integerList1, integerList2);
        printArray(integerList1);

        // Сортировка, работает с компаратором и без
        Collections.sort(integerList1, Collections.reverseOrder());
        printArray(integerList1);

        // Добавление элементов к листу
        Collections.addAll(integerList1, 1,2,3,4,5);
        printArray(integerList1);

    }

    public static void populateArrayWithIntegers(List<Integer> integerList, int size) {

        for (int i = 0; i < size; i++) {
            integerList.add((int) (Math.random() * 1000));
        }

    }

    public static void populateArrayWithDoubles(List<Double> doubleList) {

        for (int i = 0; i < doubleList.size(); i++) {
            doubleList.add((Math.random() * 1000));
        }

    }

    public static void printArray(List<?> arrayList) {

        System.out.println("Size: " + arrayList.size());
        Iterator listIterator = arrayList.listIterator();
        int k = 0;
        while (listIterator.hasNext()) {
            System.out.println(k + ": "  + listIterator.next());
            k++;
        }
    }

}
