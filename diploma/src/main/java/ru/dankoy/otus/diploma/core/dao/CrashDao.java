package ru.dankoy.otus.diploma.core.dao;

import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.sessionmanager.SessionManager;

import java.util.List;

public interface CrashDao {

    List<Crash> getAllCrashes();

    List<Crash> getAllCrashesWithNonMotorists();

    List<Crash> getCrashesWithNonMotoristsInMapBounds(double north, double south, double west, double east);

    List<Crash> getAllCrashesInMapBounds(double north, double south, double west, double east);

    SessionManager getSessionManager();
}
