package ru.dankoy.otus.jetty.web.servlet;

import com.google.gson.Gson;
import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.model.PhoneDataSet;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.hibernate.sessionmanager.SessionManagerHibernate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllUsersApiServlet extends HttpServlet {

    private final UserDao userDao;
    private final Gson gson;
    private final SessionManagerHibernate sessionManagerHibernate;

    public AllUsersApiServlet(UserDao userDao, Gson gson, SessionManagerHibernate sessionManagerHibernate) {
        this.userDao = userDao;
        this.gson = gson;
        this.sessionManagerHibernate = sessionManagerHibernate;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        sessionManagerHibernate.beginSession();

        List<User> user = userDao.getAllUsers();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));

        sessionManagerHibernate.commitSession();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        sessionManagerHibernate.beginSession();

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        User user = gson.fromJson(body, User.class);

        //Заполнение связей между юзером, адресом и телефонами
        AddressDataSet addressDataSet = user.getAddress();
        addressDataSet.setUser(user);

        List<PhoneDataSet> phoneDataSets = user.getPhoneDataSets();
        phoneDataSets.forEach(phoneDataSet -> phoneDataSet.setUser(user));

        var id = userDao.insertUser(user);
        response.setStatus(HttpServletResponse.SC_OK);

        Optional<User> foundUser = userDao.findById(id);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(foundUser.orElse(null)));

        sessionManagerHibernate.commitSession();

    }

}
