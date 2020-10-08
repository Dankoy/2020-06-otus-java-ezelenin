package ru.dankoy.otus.core.dao;

import ru.dankoy.otus.core.model.Account;
import ru.dankoy.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {

    Optional<Account> findById(long id);

    long insertAccount(Account account);

    SessionManager getSessionManager();

}
