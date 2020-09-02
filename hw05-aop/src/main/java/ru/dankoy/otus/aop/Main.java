package ru.dankoy.otus.aop;

import ru.dankoy.otus.aop.proxy.ClassForAop;
import ru.dankoy.otus.aop.proxy.ClassForAopImpl;
import ru.dankoy.otus.aop.proxy.DynamicInvocationHandler;

/**
 * @author Evgeny 02-09-2020
 */
public class Main {

    public static void main(String[] args) {

        DynamicInvocationHandler handler = new DynamicInvocationHandler(new ClassForAopImpl(), ClassForAop.class);
        ClassForAop classForAopObject = (ClassForAop) handler.createClassForAop();
        classForAopObject.calculation(5, 5, 76, 8, 9);
        classForAopObject.calculation(0);
        classForAopObject.calculation2(5, 5, 76, 8, 9);
        classForAopObject.calculation2(0);

    }

}
