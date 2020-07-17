package ru.dankoy.otus.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.dankoy.otus.DIYArrayListMain;
import ru.dankoy.otus.generics.DIYArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DIYArrayListTest {

    private static List<Integer> arrayList1 = new DIYArrayList<>();
    private static List<Integer> arrayList2 = new DIYArrayList<>();
    private static List<Integer> assertToArrayList1 = new ArrayList<>();
    private static List<Integer> assertToArrayList2 = new ArrayList<>();

    // Заполняет листы данными
    @BeforeAll
    public static void setUp() {

        DIYArrayListMain.populateArrayWithIntegers(arrayList1, 5);
        DIYArrayListMain.populateArrayWithIntegers(arrayList2, 5);

        copyArrays(assertToArrayList1, arrayList1);
        copyArrays(assertToArrayList2, arrayList2);

    }

    @Test
    public void collectionsSortMethodTest() {

        //Сортировка обычного ArrayList
        Collections.sort(assertToArrayList1);
        Collections.sort(assertToArrayList2);
        DIYArrayListMain.printArray(assertToArrayList1);

        // Сортировка DIY листа
        Collections.sort(arrayList1);
        Collections.sort(arrayList2);
        DIYArrayListMain.printArray(arrayList1);

//        assertThat(arrayList1).isEqualsTo(assertToArrayList2);
//        assertEquals(arrayList2, assertToArrayList2);

    }

    private static void copyArrays(List<Integer> list1, List<Integer> list2) {

        for (int i = 0; i < list2.size(); i++) {
            list1.add(list2.get(i));
        }

    }

}
