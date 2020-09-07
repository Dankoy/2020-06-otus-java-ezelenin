package ru.dankoy.otus.aop;

import ru.dankoy.otus.aop.proxy.ClassForAop;
import ru.dankoy.otus.aop.proxy.ClassForAopImpl;
import ru.dankoy.otus.aop.proxy.DynamicInvocationHandler;

import java.util.Map;

/**
 * @author Evgeny 02-09-2020
 */
public class Main {

    public static void main(String[] args) {

        DynamicInvocationHandler handler = new DynamicInvocationHandler(new ClassForAopImpl(), ClassForAop.class);
        ClassForAop classForAopObject = (ClassForAop) handler.getProxy();
        classForAopObject.calculation(5, 5, 76, 8, 9);
        classForAopObject.calculation(1, 2, "dsfsdf", true, false, Map.of(10, "hfs", "123", false));
        classForAopObject.calculation("asd", 82);
        classForAopObject.calculation2("asd", 82);

    }

}
