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

public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(WebServerBasicAuth.MAX_INACTIVE_INTERVAL);

        String requestURI = request.getRequestURI();

        if (requestURI.endsWith("user")) {

            doGetAllUsers(response);

        } else {

            doGetUserById(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(WebServerBasicAuth.MAX_INACTIVE_INTERVAL);

        var userFromJson = getUserJsonFromRequestBody(request);
        var id = saveUser(userFromJson);

        returnSavedUser(id, response);

    }

    /**
     * Получает id юзера из URI запроса
     *
     * @param request
     * @return
     */
    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

    /**
     * Получает всех юзеров из бд и возвращает в ответе как JSON строку
     *
     * @param response
     * @throws IOException
     */
    private void doGetAllUsers(HttpServletResponse response) throws IOException {

        List<User> user = dbServiceUser.getAllUsers();

        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));

    }

    /**
     * Получает юзера по ID из бд и возвращает в виде JSON
     *
     * @param response
     * @throws IOException
     */
    private void doGetUserById(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = dbServiceUser.getUser(extractIdFromRequest(request)).orElse(null);
        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    /**
     * Формирует объект User из json строки переданной в теле зарпоса
     *
     * @param request
     * @return
     * @throws IOException
     */
    private User getUserJsonFromRequestBody(HttpServletRequest request) throws IOException {

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        return gson.fromJson(body, User.class);

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

    /**
     * Формирует ответ в виде json строки созданного юзера в бд.
     *
     * @param id
     * @param response
     * @throws IOException
     */
    private void returnSavedUser(long id, HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        Optional<User> foundUser = dbServiceUser.getUser(id);
        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(foundUser.orElse(null)));

    }

}
