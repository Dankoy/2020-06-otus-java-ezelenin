package ru.dankoy.otus;

import ru.dankoy.otus.generics.DIYArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DIYArrayListMain {

    public static void main(String[] args) {

        List<Integer> integerList = new DIYArrayList<>(20);
        System.out.println(integerList.size());
        integerList.add(10);
        System.out.println(integerList.get(0));

//        Collections.copy(integerList, integerList);

    }

}
