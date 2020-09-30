package ru.dankoy.otus.messagehandler.processor;

import ru.dankoy.otus.messagehandler.Message;

public class ProcessorProxy implements Processor {

    private Processor processor;

    public ProcessorProxy(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) throws Exception {
        String field11 = message.getField11();
        String field13 = message.getField13();
        return message.toBuilder().field11(field13).field13(field11).build();
    }
}
