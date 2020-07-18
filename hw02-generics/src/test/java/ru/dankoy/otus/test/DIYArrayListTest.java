package ru.dankoy.otus.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.dankoy.otus.DIYArrayListMain;
import ru.dankoy.otus.generics.DIYArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DIYArrayListTest {

    private static List<Integer> arrayList1 = new DIYArrayList<>();
    private static List<Integer> arrayList2 = new DIYArrayList<>();
    private static List<Integer> assertToArrayList1;
    private static List<Integer> assertToArrayList2;

    // Заполняет листы данными
    @BeforeAll
    public static void setUp() {

        DIYArrayListMain.populateArrayWithIntegers(arrayList1, 50);
        DIYArrayListMain.populateArrayWithIntegers(arrayList2, 50);

        assertToArrayList1 = new ArrayList<>(arrayList1);
        assertToArrayList2 = new ArrayList<>(arrayList2);

    }

    @Test
    public void collectionsSortMethodTest() {

        //Сортировка обычного ArrayList
        Collections.sort(assertToArrayList1);
        Collections.sort(assertToArrayList2);
        System.out.println(assertToArrayList1);

        // Сортировка DIY листа
        Collections.sort(arrayList1);
        Collections.sort(arrayList2);
        DIYArrayListMain.printArray(arrayList1);

        // Проверка, что отсортированные DIY листы равны отсортированным ArrayList
        assertThat(arrayList1.toArray()).isEqualTo(assertToArrayList1.toArray());
        assertThat(arrayList2.toArray()).isEqualTo(assertToArrayList2.toArray());

    }

    @Test
    public void collectionsSortWithComparatorMethodTest() {

        //Сортировка обычного ArrayList
        Collections.sort(assertToArrayList1, Collections.reverseOrder());
        Collections.sort(assertToArrayList2, Collections.reverseOrder());
        System.out.println(assertToArrayList1);

        // Сортировка DIY листа
        Collections.sort(arrayList1, Collections.reverseOrder());
        Collections.sort(arrayList2, Collections.reverseOrder());
        DIYArrayListMain.printArray(arrayList1);

        // Проверка, что отсортированные DIY листы равны отсортированным ArrayList
        assertThat(arrayList1.toArray()).isEqualTo(assertToArrayList1.toArray());
        assertThat(arrayList2.toArray()).isEqualTo(assertToArrayList2.toArray());

    }

    @Test
    public void collectionsCopyMethodTest() {

        Collections.copy(assertToArrayList1, assertToArrayList2);
        System.out.println(assertToArrayList1);

        Collections.copy(arrayList1, arrayList2);
        DIYArrayListMain.printArray(arrayList1);

        // Проверка, что копирование DIY листов работает идентично копированию ArrayList
        assertThat(arrayList1.toArray()).isEqualTo(assertToArrayList1.toArray());

    }

    @Test
    public void collectionsAddAllMethodTest() {

        Collections.addAll(assertToArrayList1, 5, 1, 9, 100, 18, 33);
        System.out.println(assertToArrayList1);

        Collections.addAll(arrayList1, 5, 1, 9, 100, 18, 33);
        DIYArrayListMain.printArray(arrayList1);

        // Проверка, что копирование DIY листов работает идентично копированию ArrayList
        assertThat(arrayList1.toArray()).isEqualTo(assertToArrayList1.toArray());

    }

}
