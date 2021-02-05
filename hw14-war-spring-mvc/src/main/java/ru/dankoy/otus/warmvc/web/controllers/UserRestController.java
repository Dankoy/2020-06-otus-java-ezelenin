package ru.dankoy.otus.warmvc.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.userservice.DBServiceUser;

import java.util.List;

@RestController
public class UserRestController {

    private final DBServiceUser dbServiceUser;

    public UserRestController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping({"/api/user"})
    public List<User> getClients() {
        return dbServiceUser.getAllUsers();
    }

}
