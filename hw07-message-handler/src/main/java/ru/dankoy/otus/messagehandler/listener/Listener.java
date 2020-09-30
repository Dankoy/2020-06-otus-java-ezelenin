package ru.dankoy.otus.messagehandler.listener;

import ru.dankoy.otus.messagehandler.Message;

public interface Listener {

    void onUpdated(Message oldMsg, Message newMsg);

    //todo: 4. Сделать Listener для ведения истории: старое сообщение - новое
}
