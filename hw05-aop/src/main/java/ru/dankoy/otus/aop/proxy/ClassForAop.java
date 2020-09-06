package ru.dankoy.otus.aop.proxy;

import ru.dankoy.otus.aop.annotations.Log;

/**
 * @author Evgeny 02-09-2020
 * Для метода с аннотацией @Log будет выведено сообщения логгера в консоль.
 * Для метода без аннотации @Log не будет выведено никаких сообщений в консоль.
 */
public interface ClassForAop {

    void calculation(int... ints);

    void calculation2(int... ints);

    void calculation3(int n1, int n2, String n3, boolean tr, boolean fls);
}
