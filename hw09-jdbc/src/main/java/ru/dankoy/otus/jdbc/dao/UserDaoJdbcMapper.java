package ru.dankoy.otus.jdbc.dao;

import ru.dankoy.otus.core.dao.UserDao;
import ru.dankoy.otus.core.model.User;
import ru.dankoy.otus.core.sessionmanager.SessionManager;
import ru.dankoy.otus.jdbc.mapper.JdbcMapper;

import java.util.Optional;

public class UserDaoJdbcMapper implements UserDao {

    private SessionManager sessionManager;
    private JdbcMapper<?> jdbcMapper;

    public UserDaoJdbcMapper(SessionManager sessionManager, JdbcMapper<?> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        return 0;
    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }
}
