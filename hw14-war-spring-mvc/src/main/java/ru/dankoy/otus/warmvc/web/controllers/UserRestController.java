package ru.dankoy.otus.warmvc.web.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.userservice.DBServiceUser;
import ru.dankoy.otus.warmvc.web.serializator.CustomJsonSerializer;

import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final DBServiceUser dbServiceUser;
    private final CustomJsonSerializer customJsonSerializatior;

    public UserRestController(DBServiceUser dbServiceUser, CustomJsonSerializer customJsonSerializatior) {
        this.dbServiceUser = dbServiceUser;
        this.customJsonSerializatior = customJsonSerializatior;
    }

    @GetMapping({"/api/user"})
    public String getClients() {

        List<User> users = dbServiceUser.getAllUsers();

        logger.info("Users list from database: {}", users);

        Gson gson = customJsonSerializatior.getGson();
        String jsonString = gson.toJson(users);

        return jsonString;
    }

    @GetMapping("/api/user/{id}")
    public String getUserById(@PathVariable(name = "id") long id) {
        Optional<User> optionalUser = dbServiceUser.getUser(id);

        logger.info("User by id {}: {}", id, optionalUser.get());

        Gson gson = customJsonSerializatior.getGson();
        String jsonString = gson.toJson(optionalUser.get());

        return jsonString;
    }

}
