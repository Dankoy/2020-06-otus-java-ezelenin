package ru.dankoy.otus.diploma.core.hibernate.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.diploma.core.dao.CrashDao;
import ru.dankoy.otus.diploma.core.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.diploma.core.hibernate.utils.HibernateUtils;
import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.service.crashservice.DBServiceCrash;
import ru.dankoy.otus.diploma.core.service.crashservice.DBServiceCrashImpl;
import ru.dankoy.otus.diploma.flyway.MigrationsExecutor;
import ru.dankoy.otus.diploma.flyway.MigrationsExecutorFlyway;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CrashDaoHibernateTest {

    private static final Logger logger = LoggerFactory.getLogger(CrashDaoHibernateTest.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate-test.cfg.xml";
    private SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManagerHibernate;
    private CrashDao crashDao;
    private DBServiceCrash dbServiceCrash;

    @BeforeEach
    public void setUpMigration() {
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.cleanDb();
        migrationsExecutor.executeMigrations();
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, Crash.class);
    }

    @BeforeEach
    public void setUp() {

        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, Crash.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        crashDao = new CrashDaoHibernateImpl(sessionManagerHibernate);
        dbServiceCrash = new DBServiceCrashImpl(crashDao);
    }

    @Test
    @DisplayName("Проверка что вернулись все записи из бд. Проверяются только интересующие поля")
    void getAllCrashesTest() {

        List<Crash> crashes = dbServiceCrash.getAllCrashes();

        logger.info("All Crashes: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isEqualTo(2);
        assertThat(crashes).extracting(Crash::getRelatedNonMotorist).containsAnyOf(null, "PEDESTRIAN");
        assertThat(crashes).extracting(Crash::getLatitude).containsAnyOf(39.0270D, 39.0267D);
        assertThat(crashes).extracting(Crash::getLongitude).containsAnyOf(-76.136785D, -77.136785D);

    }

    @Test
    @DisplayName("Проверка, что вернулись только записи с авариями с пешеходами. Проверяются только интересующие поля")
    void getOnlyCrushesWithNonMotoristsTest() {

        List<Crash> crashes = dbServiceCrash.getCrashesWithNonMotorists();

        logger.info("Crashes with nonmotorists: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isEqualTo(1);
        assertThat(crashes).extracting(Crash::getRelatedNonMotorist).containsAnyOf("PEDESTRIAN");
        assertThat(crashes).extracting(Crash::getLatitude).containsAnyOf(39.0267D);
        assertThat(crashes).extracting(Crash::getLongitude).containsAnyOf(-77.136785D);

    }

    /**
     * Возвращается только одна авария с пешеходом.
     */
    @Test
    @DisplayName("Проверка, что вернулись только записи с авариями между указанными диапазонами latitude и longitude."
            + "Проверяются только интересующие поля")
    void getAllCrashesInMapBoundsTest() {

        List<Crash> crashes = dbServiceCrash.getAllCrashesInMapBounds(39.348624061555235, 39.0, -77.59848089307916,
                -76.65502996534478);

        logger.info("All Crashes in map bounds: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isEqualTo(1);
        assertThat(crashes).extracting(Crash::getRelatedNonMotorist).containsAnyOf("PEDESTRIAN");
        assertThat(crashes).extracting(Crash::getLatitude).containsAnyOf(39.0267D);
        assertThat(crashes).extracting(Crash::getLongitude).containsAnyOf(-77.136785D);

    }

    /**
     * Возвращается только одна авария не с пешеходом.
     */
    @Test
    @DisplayName("Проверка, что вернулись только записи с авариями между указанными диапазонами latitude и longitude."
            + "Проверяются только интересующие поля.")
    void getAllCrashesInMapBoundsTest2() {

        List<Crash> crashes = dbServiceCrash.getAllCrashesInMapBounds(39.348624061555235, 39.0, -77.0,
                -76.0);

        logger.info("All Crashes in map bounds: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isEqualTo(1);
        assertThat(crashes).extracting(Crash::getLatitude).containsAnyOf(39.0270D);
        assertThat(crashes).extracting(Crash::getLongitude).containsAnyOf(-76.136785D);

    }

    @Test
    @DisplayName("Проверка, что вернулись только записи с авариями с пешеходами между указанными диапазонами latitude" +
            " и longitude. Возвращает пустой список.")
    void getAllCrashesInMapBoundsExpectEmptyListTest() {

        List<Crash> crashes = dbServiceCrash.getCrashesWithNonMotoristsInMapBounds(39.348624061555235, 39.0, -77.0,
                -76.0);

        logger.info("Crashes with pedestrian in map bounds: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isZero();

    }

    @Test
    @DisplayName("Проверка, что вернулись только записи с авариями с пешеходами между указанными диапазонами latitude" +
            " и longitude.")
    void getAllCrashesInMapBoundsExpectNonMotoristTest() {

        List<Crash> crashes = dbServiceCrash
                .getCrashesWithNonMotoristsInMapBounds(39.348624061555235, 39.0, -77.59848089307916,
                        -76.65502996534478);

        logger.info("Crashes with pedestrians in map bounds: {}", crashes);

        assertThat(crashes).isNotNull();
        assertThat(crashes.size()).isEqualTo(1);
        assertThat(crashes).extracting(Crash::getRelatedNonMotorist).containsAnyOf("PEDESTRIAN");
        assertThat(crashes).extracting(Crash::getLatitude).containsAnyOf(39.0267D);
        assertThat(crashes).extracting(Crash::getLongitude).containsAnyOf(-77.136785D);

    }


}
