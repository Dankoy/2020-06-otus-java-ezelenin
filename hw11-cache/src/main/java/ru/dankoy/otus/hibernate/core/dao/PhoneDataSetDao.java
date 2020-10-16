package ru.dankoy.otus.hibernate.core.dao;

import ru.dankoy.otus.hibernate.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface PhoneDataSetDao {
    Optional<PhoneDataSet> findById(long id);

    long insertPhone(PhoneDataSet phoneDataSet);

    void updatePhone(PhoneDataSet phoneDataSet);

    void insertOrUpdate(PhoneDataSet phoneDataSet);

    SessionManager getSessionManager();

}
