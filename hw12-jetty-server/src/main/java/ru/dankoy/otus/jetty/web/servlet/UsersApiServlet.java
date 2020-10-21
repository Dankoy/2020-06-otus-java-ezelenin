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

public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final UserDao userDao;
    private final Gson gson;
    private final SessionManagerHibernate sessionManagerHibernate;

    public UsersApiServlet(UserDao userDao, Gson gson, SessionManagerHibernate sessionManagerHibernate) {
        this.userDao = userDao;
        this.gson = gson;
        this.sessionManagerHibernate = sessionManagerHibernate;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        sessionManagerHibernate.beginSession();

        User user = userDao.findById(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String json = gson.toJson(user);
        out.print(gson.toJson(user));

        sessionManagerHibernate.commitSession();
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

}
