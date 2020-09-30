package ru.dankoy.otus.messagehandler;

import ru.dankoy.otus.messagehandler.handler.ComplexProcessor;
import ru.dankoy.otus.messagehandler.listener.ListenerHistorySaver;
import ru.dankoy.otus.messagehandler.listener.ListenerPrinter;
import ru.dankoy.otus.messagehandler.processor.LoggerProcessor;
import ru.dankoy.otus.messagehandler.processor.ProcessorConcatFields;
import ru.dankoy.otus.messagehandler.processor.ProcessorProxy;
import ru.dankoy.otus.messagehandler.processor.ProcessorUpperField10;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13
       2. Сделать процессор, который поменяет местами значения field11 и field13
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду
       4. Сделать Listener для ведения истории: старое сообщение - новое
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()), new ProcessorProxy(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var listenerHistorySaver = new ListenerHistorySaver();
        complexProcessor.addListener(listenerHistorySaver);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field13("field13")
                .build();

        var message2 = new Message.Builder()
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field13("field13")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        var result2 = complexProcessor.handle(message2);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);

        System.out.println(listenerHistorySaver.getHistory());

    }
}
