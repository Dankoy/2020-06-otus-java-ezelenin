package ru.dankoy.otus.diploma.core.service.crashservice;

import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.List;

public interface DBServiceCrash {

    List<Crash> getAllCrashes();


    List<Crash> getCrashesWithNonMotorists();
}
