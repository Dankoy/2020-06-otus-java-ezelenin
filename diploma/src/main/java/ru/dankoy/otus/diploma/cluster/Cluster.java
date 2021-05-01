package ru.dankoy.otus.diploma.cluster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.List;

public interface Cluster extends Cloneable {
    double getLatitude();

    double getLongitude();

    List<Crash> getPoints();

    void clearPoints();

    void renewCoordinates(double latitude, double longitude);

    @JsonIgnore
    double getSumLatitude();

    @JsonIgnore
    double getSumLongitude();

    long getAmountOfPoints();

    Object clone() throws CloneNotSupportedException;
}
