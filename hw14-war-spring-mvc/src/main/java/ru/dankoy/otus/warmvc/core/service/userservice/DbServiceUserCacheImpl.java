package ru.dankoy.otus.warmvc.core.service.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.warmvc.cache.CustomCache;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.DbServiceException;

import java.util.List;
import java.util.Optional;

public class DbServiceUserCacheImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserCacheImpl.class);

    private final DBServiceUser dbServiceUser;
    private final CustomCache<Long, User> cache;

    public DbServiceUserCacheImpl(DBServiceUser dbServiceUser, CustomCache<Long, User> cache) {
        this.dbServiceUser = dbServiceUser;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        try {
            var userId = dbServiceUser.saveUser(user);
            cache.put(userId, user);
            return userId;
        } catch (DbServiceException e) {
            throw e;
        }
    }

    @Override
    public Optional<User> getUser(long id) {

        Optional<User> userFromCache = Optional.ofNullable(cache.get(id));
        if (userFromCache.isEmpty()) {
            Optional<User> foundUser = dbServiceUser.getUser(id);
            cache.put(id, foundUser.orElse(null));
            return foundUser;
        } else {
            return userFromCache;
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = dbServiceUser.getAllUsers();
        users.forEach(user -> cache.put(user.getId(), user));

        return users;

    }

}
