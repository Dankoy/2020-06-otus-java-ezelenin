package ru.dankoy.otus.jetty.core.dao;

import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    List<User> getAllUsers();

    SessionManager getSessionManager();
}
