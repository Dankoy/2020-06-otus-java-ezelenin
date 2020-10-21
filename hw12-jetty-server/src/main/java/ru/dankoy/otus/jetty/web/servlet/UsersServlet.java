package ru.dankoy.otus.jetty.web.servlet;

import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.jetty.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;
    private final SessionManagerHibernate sessionManagerHibernate;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao,
            SessionManagerHibernate sessionManagerHibernate) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
        this.sessionManagerHibernate = sessionManagerHibernate;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        sessionManagerHibernate.beginSession();

        List<User> users = userDao.getAllUsers();

        User randomUser = users.stream().findAny().get();

        paramsMap.put("id", randomUser.getId());
        paramsMap.put("name", randomUser.getName());
        paramsMap.put("age", randomUser.getAge());
        paramsMap.put("phones", randomUser.getPhoneDataSets());
        paramsMap.put("address", randomUser.getAddress());

        userMap.put(TEMPLATE_ATTR_RANDOM_USER, paramsMap);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, userMap));

        sessionManagerHibernate.commitSession();

    }

}
