package ru.dankoy.otus.jpql.core.dao;

import ru.dankoy.otus.jpql.core.model.PhoneDataSet;
import ru.dankoy.otus.jpql.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface PhoneDataSetDao {
    Optional<PhoneDataSet> findById(long id);

    long insertPhone(PhoneDataSet phoneDataSet);

    void updatePhone(PhoneDataSet phoneDataSet);

    void insertOrUpdate(PhoneDataSet phoneDataSet);

    SessionManager getSessionManager();

}
