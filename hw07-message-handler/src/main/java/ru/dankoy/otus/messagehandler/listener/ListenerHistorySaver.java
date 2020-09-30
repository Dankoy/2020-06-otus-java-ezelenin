package ru.dankoy.otus.messagehandler.listener;

import ru.dankoy.otus.messagehandler.Message;

import java.util.*;

public class ListenerHistorySaver implements Listener {

    private final static Map<Message, Message> historyOfChanges = new LinkedHashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyOfChanges.put(oldMsg, newMsg);
    }

    public Map<Message, Message> getHistory() {
        return historyOfChanges;
    }

}
