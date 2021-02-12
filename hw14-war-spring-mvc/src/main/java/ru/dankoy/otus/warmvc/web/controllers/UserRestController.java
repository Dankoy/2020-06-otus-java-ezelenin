package ru.dankoy.otus.warmvc.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.dankoy.otus.warmvc.core.model.AddressDataSet;
import ru.dankoy.otus.warmvc.core.model.PhoneDataSet;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.userservice.DBServiceUser;

import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final DBServiceUser dbServiceUser;

    public UserRestController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping(value = {"/api/user"})
    public List<User> getClients() {

        return dbServiceUser.getAllUsers();
    }

    @GetMapping(value = {"/api/user/{id}"})
    public User getUserById(@PathVariable(name = "id") long id) {
        Optional<User> optionalUser = dbServiceUser.getUser(id);

        return optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping(value = {"/api/user"})
    @ResponseBody
    public User saveNewUser(@RequestBody User user) {

        long newUserId = saveUser(user);
        return getNewUserFromDataBase(newUserId);
    }

    /**
     * Получает json описание нового созданного юзера
     *
     * @param id
     * @return
     */
    private User getNewUserFromDataBase(long id) {

        Optional<User> foundUser = dbServiceUser.getUser(id);

        return foundUser.orElseThrow(() -> new RuntimeException("User not found"));

    }

    /**
     * Сохраняет юзера в базе
     *
     * @param userFromJson
     * @return
     */
    private long saveUser(User userFromJson) {

        //Заполнение связей между юзером, адресом и телефонами
        AddressDataSet addressDataSet = userFromJson.getAddress();
        addressDataSet.setUser(userFromJson);

        List<PhoneDataSet> phoneDataSets = userFromJson.getPhoneDataSets();
        phoneDataSets.forEach(phoneDataSet -> phoneDataSet.setUser(userFromJson));

        return dbServiceUser.saveUser(userFromJson);

    }


}
