package ru.dankoy.otus.aop.proxy;

import ru.dankoy.otus.aop.annotations.Log;

import java.util.Map;

/**
 * @author Evgeny 02-09-2020
 */
public class ClassForAopImpl implements ClassForAop {

    @Log
    @Override
    public void calculation(int... ints) {
    }

//    @Log
    @Override
    public void calculation(String str1, int n1) {
    }

    @Log
    @Override
    public void calculation(int n1, int n2, String n3, boolean tr, boolean fls, Map<Object, Object> map) {
    }

}
