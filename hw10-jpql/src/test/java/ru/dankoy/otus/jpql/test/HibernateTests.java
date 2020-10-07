package ru.dankoy.otus.jpql.test;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jpql.core.dao.AddressDataSetDao;
import ru.dankoy.otus.jpql.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.jpql.core.dao.UserDao;
import ru.dankoy.otus.jpql.core.model.AddressDataSet;
import ru.dankoy.otus.jpql.core.model.PhoneDataSet;
import ru.dankoy.otus.jpql.core.model.User;
import ru.dankoy.otus.jpql.hibernate.dao.AddressDataSetDaoHibernate;
import ru.dankoy.otus.jpql.hibernate.dao.PhoneDataSetDaoHibernate;
import ru.dankoy.otus.jpql.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.jpql.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.jpql.hibernate.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HibernateTests {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTests.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate-test.cfg.xml";
    private SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManagerHibernate;
    private UserDao userDao;
    private AddressDataSetDao addressDataSetDao;
    private PhoneDataSetDao phoneDataSetDao;


    @BeforeEach
    public void setUp() {

        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDao = new UserDaoHibernate(sessionManagerHibernate);
        addressDataSetDao = new AddressDataSetDaoHibernate(sessionManagerHibernate);
        phoneDataSetDao = new PhoneDataSetDaoHibernate(sessionManagerHibernate);

    }

    @Test
    @DisplayName("Проверка что юзер добавился в базу и вернулся идентичным")
    public void insertUser() {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = userDao.insertUser(newUser);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        sessionManagerHibernate.commitSession();

        assertThat(newUser).isEqualTo(foundUser.get());

    }

    @Test
    @DisplayName("Проверка что имя и возраст юзера обновились")
    public void updateUser() {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = userDao.insertUser(newUser);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        sessionManagerHibernate.commitSession();


        // Обнволение юзера
        sessionManagerHibernate.beginSession();

        User userFromHibernateCache = null;
        if (foundUser.isPresent())
            userFromHibernateCache = foundUser.get();

        userFromHibernateCache.setName("newName");
        userFromHibernateCache.setAge(1000);

        userDao.updateUser(userFromHibernateCache);

        Optional<User> updatedUser = userDao.findById(userId);

        sessionManagerHibernate.commitSession();

        assertThat(updatedUser.get()).isNotNull().hasFieldOrPropertyWithValue("name",
                userFromHibernateCache.getName()).hasFieldOrPropertyWithValue("age", 1000);


    }

    @Test
    @DisplayName("Проверка что к существующему юзеру добавился новый телефон")
    public void addNewPhoneToExistingUser() {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = new ArrayList<>(List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3")));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = userDao.insertUser(newUser);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        PhoneDataSet newPhone = new PhoneDataSet("nice new phone number");
        newPhone.setUser(foundUser.get());
        var phones = foundUser.get().getPhoneDataSets();
        phones.add(newPhone);

        userDao.updateUser(foundUser.get());

        sessionManagerHibernate.commitSession();

        // Проверка, что в базе к юзеру добавился новый телефон
        sessionManagerHibernate.beginSession();

        Optional<User> updatedPhoneUser = userDao.findById(userId);

        List<PhoneDataSet> phoneDataSets1 = updatedPhoneUser.get().getPhoneDataSets();

        assertThat(updatedPhoneUser.get()).isNotNull();
        assertTrue(phoneDataSets1.stream().anyMatch(item -> "nice new phone number".equals(item.getNumber())));

        sessionManagerHibernate.commitSession();

    }

    @Test
    @DisplayName("Проверка что у существующему юзера изменился адрес")
    public void updateAddressToExistingUser() {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = new ArrayList<>(List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3")));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = userDao.insertUser(newUser);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        AddressDataSetDao addressDataSetDao = new AddressDataSetDaoHibernate(sessionManagerHibernate);
        AddressDataSet newAddress = foundUser.get().getAddress();
        newAddress.setStreet("NEW ADDRESS");
        newAddress.setUser(foundUser.get());

        addressDataSetDao.updateAddress(newAddress);

        sessionManagerHibernate.commitSession();

        // Проверка, что в базе у юзера обновился адрес
        sessionManagerHibernate.beginSession();

        Optional<User> updatedAddressUser = userDao.findById(userId);

        logger.info("User: {}", updatedAddressUser);
        var address = updatedAddressUser.get().getAddress();

        assertThat(updatedAddressUser.get()).isNotNull();
        assertThat(address.getStreet()).isEqualTo("NEW ADDRESS");

        sessionManagerHibernate.commitSession();


    }


}
