package ru.dankoy.otus.multithreading;

import ru.dankoy.otus.multithreading.executors.ExecutorUsage;
import ru.dankoy.otus.multithreading.monitor.MonitorUsage;

public class Main {

    public static void main(String[] args) throws Exception {

//        monitorsDemo();

        executorsDemo();

    }

    /**
     * Работа с монитором и блоком synchronized
     *
     * @throws Exception
     */
    private static void monitorsDemo() throws Exception {

        MonitorUsage monitorUsage = new MonitorUsage();
        monitorUsage.go();

    }


    /**
     * Работа с ExecutorService
     */
    private static void executorsDemo() {

        ExecutorUsage executorUsage = new ExecutorUsage();
        executorUsage.go();

    }


}
