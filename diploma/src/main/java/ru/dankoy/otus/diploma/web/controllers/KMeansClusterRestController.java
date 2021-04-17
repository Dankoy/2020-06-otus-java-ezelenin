package ru.dankoy.otus.diploma.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.otus.diploma.cluster.Cluster;
import ru.dankoy.otus.diploma.clusteralgorithms.kmeans.KMeansImpl;
import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.service.crashservice.DBServiceCrash;

import java.util.List;

@RestController
public class KMeansClusterRestController {

    private final DBServiceCrash dbServiceCrash;

    public KMeansClusterRestController(DBServiceCrash dbServiceCrash) {
        this.dbServiceCrash = dbServiceCrash;
    }

    @GetMapping(value = "/cluster/kmeans/all/{clusterSize}")
    public List<Cluster> getClustersForEverythingByClusterSize(
            @PathVariable(name = "clusterSize") int clusterSize) throws Exception {
        List<Crash> crashes = dbServiceCrash.getAllCrashes();

        KMeansImpl kMeans = new KMeansImpl();

        return kMeans.cluster(crashes, clusterSize);
    }

    @GetMapping(value = "/cluster/kmeans/nonmotorist/{clusterSize}")
    public List<Cluster> getClustersForNonMotoristsByClusterSize(
            @PathVariable(name = "clusterSize") int clusterSize) throws Exception {
        List<Crash> crashes = dbServiceCrash.getCrashesWithNonMotorists();

        KMeansImpl kMeans = new KMeansImpl();

        return kMeans.cluster(crashes, clusterSize);
    }

}
