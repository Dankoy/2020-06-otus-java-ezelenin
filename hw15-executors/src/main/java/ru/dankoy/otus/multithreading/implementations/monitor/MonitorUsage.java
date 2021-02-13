package ru.dankoy.otus.multithreading.implementations.monitor;

import ru.dankoy.otus.multithreading.implementations.Multithreading;


/**
 * @author Evgeny
 * Пример работы двух потоков. Первый всегда печатает нечетные числа, а второк потом печатает четные числа.
 */
public class MonitorUsage extends Multithreading {

    /**
     * Точка старта
     */
    @Override
    public void go() {

        new Thread(() -> this.action("first")).start();
        new Thread(() -> this.action("second")).start();

    }

}
