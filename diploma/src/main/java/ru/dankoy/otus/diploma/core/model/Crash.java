package ru.dankoy.otus.diploma.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * @author ezelenin
 * <p>
 * Описывает модель базы данных для таблицы crashes. Один объект = одно событие ДТП.
 */
@Entity
@Table(name = "crashes")
public class Crash implements Serializable {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @Column(name = "report_number")
    private String reportNumber;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "local_case_number")
    private String localCaseNumber;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "agency_name")
    private String agencyName;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "acrs_report_type")
    private String acrsReportType;

    @Column(name = "crash_date_time")
    private java.sql.Date crashDateTime; // Переделать на DateTime??

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "hit_run")
    private String hitRun;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "route_type")
    private String routeType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "mile_point")
    private Double milePoint;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "mile_point_direction")
    private String milePointDirection;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "lane_direction")
    private String laneDirection;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "lane_number")
    private int laneNumber;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "number_of_lanes")
    private int numberOfLanes;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "lane_type")
    private String laneType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "direction")
    private String direction;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "distance")
    private Double distance;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "distance_unit")
    private String distanceUnit;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "road_grade")
    private String roadGrade;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "nontraffic")
    private String nonTraffic;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "road_name")
    private String roadName;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "cross_street_type")
    private String crossStreetType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "cross_street_name")
    private String crossStreetName;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "off_road_description")
    private String offRoadDescription;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "municipality")
    private String municipality;

    @Column(name = "related_non_motorist")
    private String relatedNonMotorist;

    @Column(name = "at_fault")
    private String atFault;

    @Column(name = "collision_type")
    private String collisionType;

    @Column(name = "weather")
    private String weather;

    @Column(name = "light")
    private String light;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "surface_condition")
    private String surfaceCondition;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "traffic_control")
    private String trafficControl;

    @Column(name = "driver_substance_abuse")
    private String driverSubstanceAbuse;

    @Column(name = "non_motorist_substance_abuse")
    private String nonMotoristSubstanceAbuse;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "first_harmful_event")
    private String firstHarmfulEvent;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "second_harmful_event")
    private String secondHarmfulEvent;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "fixed_object_struck")
    private String fixedObjectStruck;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "junction")
    private String junction;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "intersection_type")
    private String intersectionType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "intersection_area")
    private String intersectionArea;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "road_alignment")
    private String roadAlignment;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "road_condition")
    private String roadCondition;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "road_division")
    private String roadDivision;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "geolocation")
    private String geolocation;

    public Crash() {
    }

    @Override
    public String toString() {
        String o = null;
        try {
            o = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getLocalCaseNumber() {
        return localCaseNumber;
    }

    public void setLocalCaseNumber(String localCaseNumber) {
        this.localCaseNumber = localCaseNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAcrsReportType() {
        return acrsReportType;
    }

    public void setAcrsReportType(String acrsReportType) {
        this.acrsReportType = acrsReportType;
    }

    public Date getCrashDateTime() {
        return crashDateTime;
    }

    public void setCrashDateTime(Date crashDateTime) {
        this.crashDateTime = crashDateTime;
    }

    public String getHitRun() {
        return hitRun;
    }

    public void setHitRun(String hitRun) {
        this.hitRun = hitRun;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public Double getMilePoint() {
        return milePoint;
    }

    public void setMilePoint(double milePoint) {
        this.milePoint = milePoint;
    }

    public String getMilePointDirection() {
        return milePointDirection;
    }

    public void setMilePointDirection(String milePointDirection) {
        this.milePointDirection = milePointDirection;
    }

    public String getLaneDirection() {
        return laneDirection;
    }

    public void setLaneDirection(String laneDirection) {
        this.laneDirection = laneDirection;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public void setNumberOfLanes(int numberOfLanes) {
        this.numberOfLanes = numberOfLanes;
    }

    public String getLaneType() {
        return laneType;
    }

    public void setLaneType(String laneType) {
        this.laneType = laneType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getRoadGrade() {
        return roadGrade;
    }

    public void setRoadGrade(String roadGrade) {
        this.roadGrade = roadGrade;
    }

    public String getNonTraffic() {
        return nonTraffic;
    }

    public void setNonTraffic(String nonTraffic) {
        this.nonTraffic = nonTraffic;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getCrossStreetType() {
        return crossStreetType;
    }

    public void setCrossStreetType(String crossStreetType) {
        this.crossStreetType = crossStreetType;
    }

    public String getCrossStreetName() {
        return crossStreetName;
    }

    public void setCrossStreetName(String crossStreetName) {
        this.crossStreetName = crossStreetName;
    }

    public String getOffRoadDescription() {
        return offRoadDescription;
    }

    public void setOffRoadDescription(String offRoadDescription) {
        this.offRoadDescription = offRoadDescription;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getRelatedNonMotorist() {
        return relatedNonMotorist;
    }

    public void setRelatedNonMotorist(String relatedNonMotorist) {
        this.relatedNonMotorist = relatedNonMotorist;
    }

    public String getAtFault() {
        return atFault;
    }

    public void setAtFault(String atFault) {
        this.atFault = atFault;
    }

    public String getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(String collisionType) {
        this.collisionType = collisionType;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getSurfaceCondition() {
        return surfaceCondition;
    }

    public void setSurfaceCondition(String surfaceCondition) {
        this.surfaceCondition = surfaceCondition;
    }

    public String getTrafficControl() {
        return trafficControl;
    }

    public void setTrafficControl(String trafficControl) {
        this.trafficControl = trafficControl;
    }

    public String getDriverSubstanceAbuse() {
        return driverSubstanceAbuse;
    }

    public void setDriverSubstanceAbuse(String driverSubstanceAbuse) {
        this.driverSubstanceAbuse = driverSubstanceAbuse;
    }

    public String getNonMotoristSubstanceAbuse() {
        return nonMotoristSubstanceAbuse;
    }

    public void setNonMotoristSubstanceAbuse(String nonMotoristSubstanceAbuse) {
        this.nonMotoristSubstanceAbuse = nonMotoristSubstanceAbuse;
    }

    public String getFirstHarmfulEvent() {
        return firstHarmfulEvent;
    }

    public void setFirstHarmfulEvent(String firstHarmfulEvent) {
        this.firstHarmfulEvent = firstHarmfulEvent;
    }

    public String getSecondHarmfulEvent() {
        return secondHarmfulEvent;
    }

    public void setSecondHarmfulEvent(String secondHarmfulEvent) {
        this.secondHarmfulEvent = secondHarmfulEvent;
    }

    public String getFixedObjectStruck() {
        return fixedObjectStruck;
    }

    public void setFixedObjectStruck(String fixedObjectStruck) {
        this.fixedObjectStruck = fixedObjectStruck;
    }

    public String getJunction() {
        return junction;
    }

    public void setJunction(String junction) {
        this.junction = junction;
    }

    public String getIntersectionType() {
        return intersectionType;
    }

    public void setIntersectionType(String intersectionType) {
        this.intersectionType = intersectionType;
    }

    public String getIntersectionArea() {
        return intersectionArea;
    }

    public void setIntersectionArea(String intersectionArea) {
        this.intersectionArea = intersectionArea;
    }

    public String getRoadAlignment() {
        return roadAlignment;
    }

    public void setRoadAlignment(String roadAlignment) {
        this.roadAlignment = roadAlignment;
    }

    public String getRoadCondition() {
        return roadCondition;
    }

    public void setRoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    public String getRoadDivision() {
        return roadDivision;
    }

    public void setRoadDivision(String roadDivision) {
        this.roadDivision = roadDivision;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crash crash = (Crash) o;
        return Double.compare(crash.latitude, latitude) == 0 && Double
                .compare(crash.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
