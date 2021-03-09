package ru.dankoy.otus.multithreading.implementations.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.multithreading.implementations.NumberSequence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Evgeny
 * Пример работы двух потоков, управляемых ExecutorService. Первый всегда печатает нечетные числа, а второк потом печатает
 * четные числа. По сути дела отличия только в методе go()
 */
public class ExecutorUsage extends NumberSequence {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorUsage.class);
    private String lastStream = "second";
    private int count = 1;
    private boolean up = true; // флаг направления - true если прибавляем числа, false если отнимаем

    /**
     * Точка старта
     */
    @Override
    public void go() {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> action("first"));
        executorService.submit(() -> action("second"));

        executorService.shutdown();

    }

}
