package ru.dankoy.otus.warmvc.core.dao;

import ru.dankoy.otus.warmvc.core.model.PhoneDataSet;
import ru.dankoy.otus.warmvc.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface PhoneDataSetDao {
    Optional<PhoneDataSet> findById(long id);

    long insertPhone(PhoneDataSet phoneDataSet);

    void updatePhone(PhoneDataSet phoneDataSet);

    void insertOrUpdate(PhoneDataSet phoneDataSet);

    SessionManager getSessionManager();

}
