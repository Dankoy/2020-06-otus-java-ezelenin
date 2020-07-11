package ru.dankoy.otus;

import ru.dankoy.otus.generics.DIYArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DIYArrayListMain {

    public static void main(String[] args) {

        List<Integer> integerList = new DIYArrayList<>();
        List<Double> doubleList = new DIYArrayList<>();

//        populateArrayWithIntegers(integerList);
        populateArrayWithDoubles(doubleList);


//        printArray(integerList);
        printArray(doubleList);


//        Collections.copy(integerList, integerList);

    }

    private static void populateArrayWithIntegers(List<Integer> integerList) {

        for (int i = 0; i < integerList.size(); i++) {
            integerList.add((int) (Math.random() * 1000));
        }

    }

    private static void populateArrayWithDoubles(List<Double> doubleList) {

        for (int i = 0; i < doubleList.size(); i++) {
            doubleList.add((Math.random() * 1000));
        }

    }

    private static void printArray(List<?> arrayList) {

        System.out.println("Size: " + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i));
        }

    }

}
