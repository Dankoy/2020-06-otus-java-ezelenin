package ru.dankoy.otus.hibernate.hibernate.cacheddao;

import ru.dankoy.otus.hibernate.cache.CustomCache;
import ru.dankoy.otus.hibernate.cache.CustomCacheImpl;
import ru.dankoy.otus.hibernate.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.hibernate.core.dao.UserDao;
import ru.dankoy.otus.hibernate.core.model.User;
import ru.dankoy.otus.hibernate.core.sessionmanager.SessionManager;

import java.util.Optional;

public class CachedUserDaoHibernate implements UserDao {

    private final UserDao userDaoHibernate;
    private final CustomCache<Long, Optional<User>> cache;

    public CachedUserDaoHibernate(UserDao userDaoHibernate) {
        this.userDaoHibernate = userDaoHibernate;
        this.cache = new CustomCacheImpl<>();
        this.cache.addListener(new CustomCacheListenerImpl<>());
    }


    @Override
    public Optional<User> findById(long id) {

        Optional<User> userFromCache = cache.get(id);
        if (userFromCache.isEmpty()) {
            return userDaoHibernate.findById(id);
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

        Optional<User> userFromCache = cache.get(user.getId());

        if (userFromCache.isEmpty()) {
            userDaoHibernate.updateUser(user);
        } else {
            userDaoHibernate.updateUser(userFromCache.get());
        }

    }

    @Override
    public void insertOrUpdate(User user) {

        Optional<User> userFromCache = cache.get(user.getId());

        if (userFromCache.isEmpty()) {
            userDaoHibernate.insertOrUpdate(user);
        } else {
            userDaoHibernate.insertOrUpdate(userFromCache.get());
        }

    }

    @Override
    public SessionManager getSessionManager() {
        return userDaoHibernate.getSessionManager();
    }
}
