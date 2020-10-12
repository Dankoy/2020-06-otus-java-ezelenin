package ru.dankoy.otus.jpql.core.dao;

import ru.dankoy.otus.jpql.core.model.AddressDataSet;
import ru.dankoy.otus.jpql.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AddressDataSetDao {
    Optional<AddressDataSet> findById(long id);

    long insertAddress(AddressDataSet addressDataSet);

    void updateAddress(AddressDataSet addressDataSet);

    void insertOrUpdate(AddressDataSet addressDataSet);

    SessionManager getSessionManager();
}
