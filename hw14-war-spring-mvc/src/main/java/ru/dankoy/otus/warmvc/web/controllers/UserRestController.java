package ru.dankoy.otus.warmvc.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.otus.warmvc.core.dao.UserDao;
import ru.dankoy.otus.warmvc.core.model.User;

import java.util.List;

@RestController
public class UserRestController {

    private final UserDao userDao;

    public UserRestController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping({"/api/user"})
    public List<User> getClients() {
        return userDao.getAllUsers();
    }

}
