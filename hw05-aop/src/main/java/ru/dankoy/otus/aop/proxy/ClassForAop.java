package ru.dankoy.otus.aop.proxy;

import ru.dankoy.otus.aop.annotations.Log;

public interface ClassForAop {

    @Log
    void calculation(int... ints);

    void calculation2(int... ints);
}
