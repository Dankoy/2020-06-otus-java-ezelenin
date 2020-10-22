package ru.dankoy.otus.jetty.web.servlet;

import com.google.gson.Gson;
import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.hibernate.sessionmanager.SessionManagerHibernate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

}
