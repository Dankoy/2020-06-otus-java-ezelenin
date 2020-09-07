package ru.dankoy.otus.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.aop.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Evgeny 02-09-2020
 */
public class DynamicInvocationHandler implements InvocationHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(
            DynamicInvocationHandler.class);

    private final Object classForAop;
    private final Class<?> classForAopInterface;
    private final Set<String> isLoggingSet;
    private final Object proxy;

    public Object getProxy() {
        return proxy;
    }

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
        this.proxy = Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{classForAopInterface}, this);
        this.isLoggingSet = getIsLoggingMap(classForAop);

    }

    public Object createClassForAop() {
        InvocationHandler handler = new DynamicInvocationHandler(classForAop, this.classForAopInterface);
        return Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{classForAopInterface}, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (isLoggingSet.contains(method.toString().substring(method.toString().indexOf(method.getName())))) {
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
        Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {
            result = annotation.annotationType().equals(Log.class);
        }

        return result;

    }

    /**
     * Проверка всех методов объекта класса, и возвращение Set<String> сета, которая содержит имя метода у которого
     * присутствует аннотация @Log
     *
     * @param object
     * @return
     */
    private Set<String> getIsLoggingMap(Object object) {
        Set<String> result = new HashSet<>();

        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            if (checkAnnotation(method))
                result.add(method.toString().substring(method.toString().indexOf(method.getName())));
        }

        return result;
    }

}
