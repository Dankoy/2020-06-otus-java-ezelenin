package ru.dankoy.otus.hibernate.cache.core.dao;

import ru.dankoy.otus.hibernate.cache.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.cache.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface PhoneDataSetDao {
    Optional<PhoneDataSet> findById(long id);

    long insertPhone(PhoneDataSet phoneDataSet);

    void updatePhone(PhoneDataSet phoneDataSet);

    void insertOrUpdate(PhoneDataSet phoneDataSet);

    SessionManager getSessionManager();

}
