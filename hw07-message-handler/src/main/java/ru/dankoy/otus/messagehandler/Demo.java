package ru.dankoy.otus.messagehandler;

import ru.dankoy.otus.messagehandler.Message;
import ru.dankoy.otus.messagehandler.handler.ComplexProcessor;
import ru.dankoy.otus.messagehandler.listener.ListenerPrinter;
import ru.dankoy.otus.messagehandler.processor.LoggerProcessor;
import ru.dankoy.otus.messagehandler.processor.ProcessorConcatFields;
import ru.dankoy.otus.messagehandler.processor.ProcessorUpperField10;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
