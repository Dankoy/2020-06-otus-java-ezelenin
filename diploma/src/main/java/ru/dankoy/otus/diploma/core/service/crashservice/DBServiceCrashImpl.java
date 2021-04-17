package ru.dankoy.otus.diploma.core.service.crashservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.dankoy.otus.diploma.core.dao.CrashDao;
import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBServiceCrashImpl implements DBServiceCrash {

    private static final Logger logger = LoggerFactory.getLogger(DBServiceCrashImpl.class);

    private final CrashDao crashDao;

    public DBServiceCrashImpl(CrashDao crashDao) {
        this.crashDao = crashDao;
    }


    @Override
    public List<Crash> getAllCrashes() {

        try (var sessionManager = crashDao.getSessionManager()) {
            sessionManager.beginSession();
            try {

                return crashDao.getAllCrashes();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }

    }

    @Override
    public List<Crash> getCrashesWithNonMotorists() {

        try (var sessionManager = crashDao.getSessionManager()) {
            sessionManager.beginSession();
            try {

                return crashDao.getAllCrashesWithNonMotorists();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }

    }

    @Override
    public List<Crash> getCrashesWithNonMotoristsInMapBounds(double north, double south, double west, double east) {

        try (var sessionManager = crashDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return crashDao.getCrashesWithNonMotoristsInMapBounds(north, south, west, east);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }

    }

    @Override
    public List<Crash> getAllCrashesInMapBounds(double north, double south, double west, double east) {

        try (var sessionManager = crashDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return crashDao.getAllCrashesInMapBounds(north, south, west, east);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }

    }


}
