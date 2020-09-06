package ru.dankoy.otus.aop.proxy;

import ru.dankoy.otus.aop.annotations.Log;

/**
 * @author Evgeny 02-09-2020
 */
public class ClassForAopImpl implements ClassForAop {

    @Log
    @Override
    public void calculation(int... ints) {
    }

    @Override
    public void calculation2(int... ints) {
    }

    @Log
    @Override
    public void calculation3(int n1, int n2, String n3, boolean tr, boolean fls) {

    }

}
