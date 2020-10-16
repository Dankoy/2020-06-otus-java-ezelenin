package ru.dankoy.otus.hibernate.cache.core.dao;

import ru.dankoy.otus.hibernate.cache.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.cache.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AddressDataSetDao {
    Optional<AddressDataSet> findById(long id);

    long insertAddress(AddressDataSet addressDataSet);

    void updateAddress(AddressDataSet addressDataSet);

    void insertOrUpdate(AddressDataSet addressDataSet);

    SessionManager getSessionManager();
}
