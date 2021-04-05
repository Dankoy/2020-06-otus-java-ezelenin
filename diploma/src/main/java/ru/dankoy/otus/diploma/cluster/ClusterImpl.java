package ru.dankoy.otus.diploma.cluster;

import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.ArrayList;
import java.util.List;

public class ClusterImpl implements Cluster {

    private long id;
    private double latitude;
    private double longitude;

    private List<Crash> points = new ArrayList<>();

    public ClusterImpl() {
    }

    public ClusterImpl(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public List<Crash> getPoints() {
        return points;
    }

    @Override
    public void clearPoints() {
        this.points = new ArrayList<>();
    }

    @Override
    public void renewCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double getSumLatitude() {
        double sum = 0;
        for (Crash point : points) {
            sum = sum + point.getLatitude();
        }
        return sum;
    }

    @Override
    public double getSumLongitude() {
        double sum = 0;
        for (Crash point : points) {
            sum = sum + point.getLongitude();
        }
        return sum;
    }

    @Override
    public long getAmountOfPoints() {
        return points.size();
    }
}
