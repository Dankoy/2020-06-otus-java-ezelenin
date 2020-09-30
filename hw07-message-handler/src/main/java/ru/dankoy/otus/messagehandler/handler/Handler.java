package ru.dankoy.otus.messagehandler.handler;

import ru.dankoy.otus.messagehandler.Message;
import ru.dankoy.otus.messagehandler.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
