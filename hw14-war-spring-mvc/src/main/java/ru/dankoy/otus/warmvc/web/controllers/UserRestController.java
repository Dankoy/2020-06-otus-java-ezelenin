package ru.dankoy.otus.warmvc.web.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.dankoy.otus.warmvc.core.model.AddressDataSet;
import ru.dankoy.otus.warmvc.core.model.PhoneDataSet;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.userservice.DBServiceUser;
import ru.dankoy.otus.warmvc.web.serializator.CustomJsonSerializer;

import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final DBServiceUser dbServiceUser;
    private final CustomJsonSerializer customJsonSerializer;

    public UserRestController(DBServiceUser dbServiceUser, CustomJsonSerializer customJsonSerializatior) {
        this.dbServiceUser = dbServiceUser;
        this.customJsonSerializer = customJsonSerializatior;
    }

    @GetMapping(value = {"/api/user"}, produces = {"application/json"})
    public String getClients() {

        List<User> users = dbServiceUser.getAllUsers();
        Gson gson = customJsonSerializer.getGson();

        return gson.toJson(users);
    }

    @GetMapping(value = {"/api/user/{id}"}, produces = {"application/json"})
    public String getUserById(@PathVariable(name = "id") long id) {
        Optional<User> optionalUser = dbServiceUser.getUser(id);

        Gson gson = customJsonSerializer.getGson();

        return gson.toJson(optionalUser.get());
    }

    @PostMapping(value = {"/api/user"}, produces = {"application/json"})
    @ResponseBody
    public String saveNewUser(@RequestBody User user) {

        long newUserId = saveUser(user);
        return getNewUserFromDataBase(newUserId);
    }

    /**
     * Получает json описание нового созданного юзера
     *
     * @param id
     * @return
     */
    private String getNewUserFromDataBase(long id) {

        Optional<User> foundUser = dbServiceUser.getUser(id);

        Gson gson = customJsonSerializer.getGson();

        return gson.toJson(foundUser.get());

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
