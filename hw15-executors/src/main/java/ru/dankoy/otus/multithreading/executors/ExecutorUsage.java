package ru.dankoy.otus.multithreading.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUsage {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorUsage.class);
    private String lastStream = "second";
    private int count = 1;
    private boolean up = true; // флаг направления - true если прибавляем числа, false если отнимаем

    /**
     * Точка старта
     */
    public void go() {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> action("first"));
        executorService.submit(() -> action("second"));

        executorService.shutdown();

    }

    /**
     * Увеличивает или уменьшает счетчик в зависимости от текущего значения счетчика. Синхронизируется по имени потока.
     *
     * @param streamName
     */
    private synchronized void action(String streamName) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                //поэтому не if
                while (lastStream.equals(streamName)) {
                    this.wait();
                }

                logger.info("Current stream '{}' writes '{}'", streamName, count);
                lastStream = streamName;

                countModify();
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new ExecutorUsage.NotInterestingException(ex);
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }

    /**
     * Проверка направления счета. Когда достигли числа 10, то уменьшаем счетчик, если достигли числа 1, то
     * увеличиваем.
     */
    private void countModify() {

        if (count == 10) {
            up = false;
        } else if (count == 1) {
            up = true;
        }

        if (up) {
            count += 1;
        } else {
            count -= 1;
        }
    }

}
