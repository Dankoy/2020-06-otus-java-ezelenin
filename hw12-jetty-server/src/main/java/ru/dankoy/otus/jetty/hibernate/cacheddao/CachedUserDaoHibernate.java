package ru.dankoy.otus.jetty.hibernate.cacheddao;

import ru.dankoy.otus.jetty.cache.CustomCache;
import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.sessionmanager.SessionManager;

import java.util.Optional;

public class CachedUserDaoHibernate implements UserDao {

    private final UserDao userDaoHibernate;
    private final CustomCache<Long, Optional<User>> cache;

    public CachedUserDaoHibernate(UserDao userDaoHibernate, CustomCache<Long, Optional<User>> cache) {
        this.userDaoHibernate = userDaoHibernate;
        this.cache = cache;
    }


    @Override
    public Optional<User> findById(long id) {

        Optional<User> userFromCache = cache.get(id);
        if (userFromCache.isEmpty()) {
            Optional<User> foundUser = userDaoHibernate.findById(id);
            cache.put(id, foundUser);
            return foundUser;
        } else {
            return userFromCache;
        }

    }

    @Override
    public long insertUser(User user) {

        long userId = userDaoHibernate.insertUser(user);
        cache.put(userId, Optional.ofNullable(user));

        return userId;
    }

    @Override
    public void updateUser(User user) {

        userDaoHibernate.updateUser(user);
        cache.put(user.getId(), Optional.of(user));

    }

    @Override
    public void insertOrUpdate(User user) {

        userDaoHibernate.insertOrUpdate(user);
        cache.put(user.getId(), Optional.of(user));

    }

    @Override
    public SessionManager getSessionManager() {
        return userDaoHibernate.getSessionManager();
    }
}
