package ru.dankoy.otus.messagehandler.processor;

import ru.dankoy.otus.messagehandler.Message;

public class LoggerProcessor implements Processor, CurrentSecond {
    //todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду

    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) throws Exception {
        if(getCurrentSecond() % 2 == 0) {
            throw new RuntimeException();
        }
        System.out.println("log processing message:" + message);
        return processor.process(message);
    }
}
