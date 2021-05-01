package ru.dankoy.otus.diploma.core.service.crashservice;

import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.List;

public interface DBServiceCrash {

    List<Crash> getAllCrashes();


    List<Crash> getCrashesWithNonMotorists();

    List<Crash> getCrashesWithNonMotoristsInMapBounds(double north, double south, double west, double east);

    List<Crash> getAllCrashesInMapBounds(double north, double south, double west, double east);
}
