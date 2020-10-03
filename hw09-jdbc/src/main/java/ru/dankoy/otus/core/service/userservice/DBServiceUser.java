package ru.dankoy.otus.core.service.userservice;

import ru.dankoy.otus.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
