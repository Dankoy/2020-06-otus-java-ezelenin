package ru.dankoy.otus.messagehandler.processor;

import java.time.LocalTime;

public interface CurrentSecond {

    default int getCurrentSecond() {
        return LocalTime.now().getSecond();
    }

}
