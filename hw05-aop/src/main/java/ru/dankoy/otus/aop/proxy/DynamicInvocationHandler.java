package ru.dankoy.otus.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.aop.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicInvocationHandler implements InvocationHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(
            DynamicInvocationHandler.class);

    private final Object classForAop;
    private final Class<?> classForAopInterface;

    /**
     * В конструктор передаются два аргумента:
     * 1) Объект проксируемого класса
     * 2) Интерфейс проксируемого класса
     *
     * @param classForAop
     * @param classForAopInterface
     */
    public DynamicInvocationHandler(Object classForAop, Class<?> classForAopInterface) {
        this.classForAop = classForAop;
        this.classForAopInterface = classForAopInterface;
    }

    public Object createClassForAop() {
        InvocationHandler handler = new DynamicInvocationHandler(classForAop, this.classForAopInterface);
        return Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{classForAopInterface}, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (checkAnnotation(method)) {
            LOGGER.info("executed method: {}, params: {}", method.getName(), args);
        }

        return method.invoke(classForAop, args);

    }

    /**
     * Проверка всех аннотаций вызываемого метода. Если у метода есть аннотация @Log, то пишем логи.
     *
     * @param method
     * @return
     */
    private boolean checkAnnotation(Method method) {

        boolean result = false;
        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            result = annotation.annotationType().equals(Log.class);
        }

        return result;

    }

}
