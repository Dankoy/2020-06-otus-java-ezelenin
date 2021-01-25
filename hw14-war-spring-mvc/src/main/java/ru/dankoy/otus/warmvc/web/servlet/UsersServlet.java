package ru.dankoy.otus.warmvc.web.servlet;

//import ru.dankoy.otus.jetty.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
        import java.util.Map;

public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_USER_DATA_NAME = "users";
    private static final String CONTENT_TYPE = "text/html";

//    private final DBServiceUser dbServiceUser;
//    private final TemplateProcessor templateProcessor;

//    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
//        this.templateProcessor = templateProcessor;
//        this.dbServiceUser = dbServiceUser;
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        HttpSession session = req.getSession();
//        session.setMaxInactiveInterval(WebServerBasicAuth.MAX_INACTIVE_INTERVAL);

        Map<String, Object> userMap = new HashMap<>();
//        List<User> users = dbServiceUser.getAllUsers();

//        userMap.put(TEMPLATE_USER_DATA_NAME, users);

        response.setContentType(CONTENT_TYPE);
//        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, userMap));

    }

}
