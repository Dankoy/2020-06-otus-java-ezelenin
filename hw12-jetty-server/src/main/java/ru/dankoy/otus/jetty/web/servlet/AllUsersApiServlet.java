package ru.dankoy.otus.jetty.web.servlet;

import com.google.gson.Gson;
import ru.dankoy.otus.jetty.WebServerBasicAuth;
import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.model.PhoneDataSet;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.service.userservice.DBServiceUser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllUsersApiServlet extends HttpServlet {

    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public AllUsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(WebServerBasicAuth.MAX_INACTIVE_INTERVAL);

        List<User> user = dbServiceUser.getAllUsers();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(WebServerBasicAuth.MAX_INACTIVE_INTERVAL);

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        User user = gson.fromJson(body, User.class);

        //Заполнение связей между юзером, адресом и телефонами
        AddressDataSet addressDataSet = user.getAddress();
        addressDataSet.setUser(user);

        List<PhoneDataSet> phoneDataSets = user.getPhoneDataSets();
        phoneDataSets.forEach(phoneDataSet -> phoneDataSet.setUser(user));

        var id = dbServiceUser.saveUser(user);
        response.setStatus(HttpServletResponse.SC_OK);

        Optional<User> foundUser = dbServiceUser.getUser(id);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(foundUser.orElse(null)));

    }

}
