package ru.dankoy.otus.jpql.jdbc.dao;

import ru.dankoy.otus.jpql.core.dao.UserDao;
import ru.dankoy.otus.jpql.core.dao.UserDaoException;
import ru.dankoy.otus.jpql.core.model.User;
import ru.dankoy.otus.jpql.core.sessionmanager.SessionManager;
import ru.dankoy.otus.jpql.jdbc.mapper.JdbcMapper;
import ru.dankoy.otus.jpql.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class UserDaoJdbcMapper implements UserDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<User> jdbcMapper;

    public UserDaoJdbcMapper(SessionManagerJdbc sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {

        Optional<User> user = Optional.ofNullable(jdbcMapper.findById(id, User.class));

        return user;
    }

    @Override
    public long insertUser(User user) {

        try {
            return jdbcMapper.insert(user);
        } catch (Exception e) {
            throw new UserDaoException(e);
        }

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
