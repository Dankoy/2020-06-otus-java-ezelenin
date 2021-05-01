package ru.dankoy.otus.diploma.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.otus.diploma.cluster.Cluster;
import ru.dankoy.otus.diploma.clusteralgorithms.kmeans.KMeansImpl;
import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.service.crashservice.DBServiceCrash;

import java.util.List;

@RestController
public class KMeansClusterRestController {

    private final DBServiceCrash dbServiceCrash;
    private final KMeansImpl kMeans;

    public KMeansClusterRestController(DBServiceCrash dbServiceCrash, KMeansImpl kMeans) {
        this.dbServiceCrash = dbServiceCrash;
        this.kMeans = kMeans;
    }

    @GetMapping(value = "/cluster/kmeans/all/{clusterSize}")
    public List<Cluster> getClustersForEverythingByClusterSize(
            @PathVariable(name = "clusterSize") int clusterSize) {
        List<Crash> crashes = dbServiceCrash.getAllCrashes();

        return kMeans.cluster(crashes, clusterSize);
    }

    @GetMapping(value = "/cluster/kmeans/all/{clusterSize}", params = {"north", "south", "west", "east"})
    public List<Cluster> getClustersForEverythingByClusterSizeAndMapBounds(
            @PathVariable(name = "clusterSize") int clusterSize, @RequestParam(name = "north") double north,
            @RequestParam(name = "south") double south,
            @RequestParam(name = "west") double west,
            @RequestParam(name = "east") double east) {

        List<Crash> crashes = dbServiceCrash.getAllCrashesInMapBounds(north, south, west, east);

        return kMeans.cluster(crashes, clusterSize);
    }

    @GetMapping(value = "/cluster/kmeans/nonmotorist/{clusterSize}")
    public List<Cluster> getClustersForNonMotoristsByClusterSize(
            @PathVariable(name = "clusterSize") int clusterSize) {
        List<Crash> crashes = dbServiceCrash.getCrashesWithNonMotorists();

        return kMeans.cluster(crashes, clusterSize);
    }


    @GetMapping(value = "/cluster/kmeans/nonmotorist/{clusterSize}", params = {"north", "south", "west", "east"})
    public List<Cluster> getClustersForNonMotoristsByClusterSizeAndMapBounds(
            @PathVariable(name = "clusterSize") int clusterSize, @RequestParam(name = "north") double north,
            @RequestParam(name = "south") double south,
            @RequestParam(name = "west") double west,
            @RequestParam(name = "east") double east) {

        List<Crash> crashes = dbServiceCrash.getCrashesWithNonMotoristsInMapBounds(north, south, west, east);

        return kMeans.cluster(crashes, clusterSize);
    }

}
