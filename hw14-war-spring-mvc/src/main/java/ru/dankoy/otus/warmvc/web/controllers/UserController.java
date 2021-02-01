package ru.dankoy.otus.warmvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.service.userservice.DBServiceUser;

import java.util.List;

@Controller
public class UserController {

    private final DBServiceUser dbServiceUser;

    public UserController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping(value = "/")
    public String startView() {
        return "index.html";
    }

    @GetMapping({"/user/list"})
    public String clientsListView(Model model) {
        List<User> users = dbServiceUser.getAllUsers();
        model.addAttribute("users", users);
        return "users.html";
    }

//    @GetMapping("/client/create")
//    public String clientCreateView(Model model) {
//        model.addAttribute("client", new Client());
//        return "clientCreate.html";
//    }
//
//    @PostMapping("/client/save")
//    public RedirectView clientSave(@ModelAttribute Client client) {
//        clientService.save(client);
//        return new RedirectView("/", true);
//    }

}
