package ru.dankoy.otus.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.model.PhoneDataSet;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.service.userservice.DbServiceUserImpl;
import ru.dankoy.otus.jetty.h2.DataSourceH2;
import ru.dankoy.otus.jetty.service.FileSystemHelper;
import ru.dankoy.otus.jetty.service.TemplateProcessor;
import ru.dankoy.otus.jetty.web.server.UsersWebServer;
import ru.dankoy.otus.jetty.web.server.UsersWebServerWithBasicAuth;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JettyTest {

    private static final Logger logger = LoggerFactory.getLogger(JettyTest.class);

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String API_USER_URL = "api/user";

    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_NAME = "user1";
    private static final int DEFAULT_USER_AGE = 123455;
    private static User DEFAULT_USER;
    private static final String INCORRECT_USER_LOGIN = "BadUser";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "admin";
    private static String ENCODED_STRING;

    private static Gson gson;
    private static UsersWebServer webServer;
    private static HttpClient http;

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }

    @BeforeAll
    static void setUp() throws Exception {

        http = HttpClient.newHttpClient();
        ENCODED_STRING = Base64.getEncoder().encodeToString(("user1" + ":" + "user1").getBytes());

        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);

        var dbServiceUser = mock(DbServiceUserImpl.class);

        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);

        List<PhoneDataSet> phoneDataSets = new ArrayList<>();
        phoneDataSets.add(new PhoneDataSet("user phone1"));
        phoneDataSets.add(new PhoneDataSet("user phone2"));
        phoneDataSets.add(new PhoneDataSet("user phone3"));
        phoneDataSets.add(new PhoneDataSet("user phone4"));

        AddressDataSet addressDataSet = new AddressDataSet("user nice address");
        DEFAULT_USER = new User(DEFAULT_USER_NAME, DEFAULT_USER_AGE, addressDataSet, phoneDataSets);

        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(DEFAULT_USER, DEFAULT_USER_ID);

        addressDataSet.setUser(DEFAULT_USER);
        phoneDataSets.forEach(phone -> phone.setUser(DEFAULT_USER));

        given(dbServiceUser.getUser(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));
        given(dbServiceUser.getAllUsers()).willReturn(List.of(DEFAULT_USER));

        String hashLoginServiceConfigPath = FileSystemHelper
                .localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        webServer = new UsersWebServerWithBasicAuth(WEB_SERVER_PORT, loginService, dbServiceUser, gson, templateProcessor);
        webServer.start();

    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @Test
    @DisplayName("Проверка того, что запрос к /api/user/1 возвращает нужного юзера")
    void testUserApi() throws IOException, InterruptedException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WEB_SERVER_URL);
        stringBuilder.append(API_USER_URL);
        stringBuilder.append("/" + DEFAULT_USER_ID);

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(stringBuilder.toString()))
                .setHeader(HttpHeader.ACCEPT.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.AUTHORIZATION.asString(), "Basic " + ENCODED_STRING)
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).isEqualTo(gson.toJson(DEFAULT_USER));

    }

    @Test
    @DisplayName("Проверка 401 ответ от неавторизованного пользователя")
    void testAuth() throws IOException, InterruptedException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WEB_SERVER_URL);
        stringBuilder.append(API_USER_URL);
        stringBuilder.append("/" + DEFAULT_USER_ID);

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(stringBuilder.toString()))
                .setHeader(HttpHeader.ACCEPT.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.AUTHORIZATION.asString(), "Basic 123")
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);

    }

    @Test
    @DisplayName("Проверка того, что запрос к /api/user возвращает cписок из одного пользователя")
    void testGetAllUsers() throws IOException, InterruptedException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WEB_SERVER_URL);
        stringBuilder.append(API_USER_URL);

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(stringBuilder.toString()))
                .setHeader(HttpHeader.ACCEPT.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json;charset=UTF-8")
                .setHeader(HttpHeader.AUTHORIZATION.asString(), "Basic " + ENCODED_STRING)
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).isEqualTo(gson.toJson(List.of(DEFAULT_USER)));

    }

}
