package ru.dankoy.otus.messagehandler.processor;

import ru.dankoy.otus.messagehandler.Message;

public interface Processor {

    Message process(Message message) throws Exception;

    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field13
}
