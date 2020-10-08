package ru.dankoy.otus.jdbc.dao;

import ru.dankoy.otus.core.dao.AccountDao;
import ru.dankoy.otus.core.dao.UserDaoException;
import ru.dankoy.otus.core.model.Account;
import ru.dankoy.otus.core.sessionmanager.SessionManager;
import ru.dankoy.otus.jdbc.mapper.JdbcMapper;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoJdbcMapper implements AccountDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbcMapper(SessionManagerJdbc sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {

        return jdbcMapper.findById(id, Account.class);
    }

    @Override
    public long insertAccount(Account account) {

        try {
            return jdbcMapper.insert(account);
        } catch (Exception e) {
            throw new UserDaoException(e);
        }

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
