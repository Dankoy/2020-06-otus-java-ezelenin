package ru.dankoy.otus.jetty.hibernate.cacheddao;

import ru.dankoy.otus.jetty.cache.CustomCache;
import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public class CachedUserDaoHibernate implements UserDao {

    private final UserDao userDaoHibernate;
    private final CustomCache<Long, User> cache;

    public CachedUserDaoHibernate(UserDao userDaoHibernate, CustomCache<Long, User> cache) {
        this.userDaoHibernate = userDaoHibernate;
        this.cache = cache;
    }


    @Override
    public Optional<User> findById(long id) {

        Optional<User> userFromCache = Optional.ofNullable(cache.get(id));
        if (userFromCache.isEmpty()) {
            Optional<User> foundUser = userDaoHibernate.findById(id);
            cache.put(id, foundUser.orElse(null));
            return foundUser;
        } else {
            return userFromCache;
        }

    }

    @Override
    public long insertUser(User user) {

        long userId = userDaoHibernate.insertUser(user);
        cache.put(userId, user);

        return userId;
    }

    @Override
    public void updateUser(User user) {

        userDaoHibernate.updateUser(user);
        cache.put(user.getId(), user);

    }

    @Override
    public void insertOrUpdate(User user) {

        userDaoHibernate.insertOrUpdate(user);
        cache.put(user.getId(), user);

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDaoHibernate.getAllUsers();

        users.forEach(user -> cache.put(user.getId(), user));

        return users;
    }


    @Override
    public SessionManager getSessionManager() {
        return userDaoHibernate.getSessionManager();
    }
}
