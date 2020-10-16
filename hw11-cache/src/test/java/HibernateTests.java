import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.hibernate.core.dao.AddressDataSetDao;
import ru.dankoy.otus.hibernate.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.hibernate.core.dao.UserDao;
import ru.dankoy.otus.hibernate.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.core.model.User;
import ru.dankoy.otus.hibernate.hibernate.cacheddao.CachedUserDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.AddressDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.PhoneDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.hibernate.hibernate.utils.HibernateUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class HibernateTests {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTests.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate-test.cfg.xml";
    private SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManagerHibernate;
    private UserDao userDao;
    private CachedUserDaoHibernate cachedUserDaoHibernate;
    private AddressDataSetDao addressDataSetDao;
    private PhoneDataSetDao phoneDataSetDao;


    @BeforeEach
    public void setUp() {

        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDao = new UserDaoHibernate(sessionManagerHibernate);
        cachedUserDaoHibernate = new CachedUserDaoHibernate(userDao);
        addressDataSetDao = new AddressDataSetDaoHibernate(sessionManagerHibernate);
        phoneDataSetDao = new PhoneDataSetDaoHibernate(sessionManagerHibernate);

    }

    @Test
    @DisplayName("Проверка что юзер добавился в базу и вернулся идентичным из кэша")
    void insertUserWithCache() {

        sessionManagerHibernate.beginSession();

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = cachedUserDaoHibernate.insertUser(newUser);

        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();

        logger.info("Getting user by Id, id:{}", userId);
        // Получение юзера из базы
        Optional<User> foundUserFromDb = cachedUserDaoHibernate.findById(userId);
        foundUserFromDb.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        assertThat(newUser.toString()).isEqualTo(foundUserFromDb.get().toString());

        sessionManagerHibernate.commitSession();

    }

    @Test
    @DisplayName("Проверка что юзер добавился в базу и вернулся идентичным из базы без кэша")
    void insertUserWithoutCache() {

        sessionManagerHibernate.beginSession();

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = userDao.insertUser(newUser);

        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();

        logger.info("Getting user by Id, id:{}", userId);
        // Получение юзера из базы
        Optional<User> foundUserFromDb = userDao.findById(userId);
        foundUserFromDb.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser),
                () -> logger.info("user was not found")
        );

        assertThat(newUser.toString()).isEqualTo(foundUserFromDb.get().toString());

        sessionManagerHibernate.commitSession();


    }

}
