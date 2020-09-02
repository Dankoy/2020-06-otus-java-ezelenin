package ru.dankoy.otus.aop.proxy;

import ru.dankoy.otus.aop.annotations.Log;

/**
 * @author Evgeny 02-09-2020
 * Для метода с аннотацией @Log будет выведено сообщения логгера в консоль.
 * Для метода без аннотации @Log не будет выведено никаких сообщений в консоль.
 */
public interface ClassForAop {

    @Log
    void calculation(int... ints);

    void calculation2(int... ints);
}
