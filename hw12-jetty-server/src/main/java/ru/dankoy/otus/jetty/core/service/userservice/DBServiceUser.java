package ru.dankoy.otus.jetty.core.service.userservice;

import ru.dankoy.otus.jetty.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
