package ru.dankoy.otus.diploma.core.dao;

import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.sessionmanager.SessionManager;
import ru.dankoy.otus.diploma.core.sessionmanager.SessionManagerException;

import java.util.List;

public interface CrashDao {

    List<Crash> getAllCrashes() throws SessionManagerException;

    List<Crash> getAllCrashesWithNonMotorists() throws SessionManagerException;

    SessionManager getSessionManager();
}
