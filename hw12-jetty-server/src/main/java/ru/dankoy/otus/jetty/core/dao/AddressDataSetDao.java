package ru.dankoy.otus.jetty.core.dao;

import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AddressDataSetDao {
    Optional<AddressDataSet> findById(long id);

    long insertAddress(AddressDataSet addressDataSet);

    void updateAddress(AddressDataSet addressDataSet);

    void insertOrUpdate(AddressDataSet addressDataSet);

    SessionManager getSessionManager();
}
