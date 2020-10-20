package ru.dankoy.otus.jetty.core.service.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jetty.cache.CustomCache;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.service.DbServiceException;

import java.util.Optional;

public class DbServiceUserCacheImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserCacheImpl.class);

    private final DBServiceUser dbServiceUser;
    private final CustomCache<Long, Optional<User>> cache;

    public DbServiceUserCacheImpl(DBServiceUser dbServiceUser, CustomCache<Long, Optional<User>> cache) {
        this.dbServiceUser = dbServiceUser;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        try {
            var userId = dbServiceUser.saveUser(user);
            cache.put(userId, Optional.ofNullable(user));
            return userId;
        } catch (DbServiceException e) {
            throw e;
        }
    }

    @Override
    public Optional<User> getUser(long id) {

        var user = cache.get(id);

        if (user.isPresent()) {
            return user;
        } else {
            return dbServiceUser.getUser(id);
        }
    }
}
