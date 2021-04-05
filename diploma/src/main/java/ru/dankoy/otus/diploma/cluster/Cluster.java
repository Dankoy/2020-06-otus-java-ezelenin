package ru.dankoy.otus.diploma.cluster;

import ru.dankoy.otus.diploma.core.model.Crash;

import java.io.Serializable;
import java.util.List;

public interface Cluster extends Serializable {
    double getLatitude();

    double getLongitude();

    List<Crash> getPoints();

    void clearPoints();

    void renewCoordinates(double latitude, double longitude);

    double getSumLatitude();

    double getSumLongitude();

    long getAmountOfPoints();
}
