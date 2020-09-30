package ru.dankoy.otus.core.dao;

import ru.dankoy.otus.core.model.User;
import ru.dankoy.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    //void updateUser(User user);
    //void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
