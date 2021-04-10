package ru.dankoy.otus.diploma.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dankoy.otus.diploma.core.service.crashservice.DBServiceCrash;

@Controller
public class CrashController {

    private final DBServiceCrash dbServiceCrash;

    public CrashController(DBServiceCrash dbServiceCrash) {
        this.dbServiceCrash = dbServiceCrash;
    }

    @GetMapping(value = "/")
    public String startView() {
        return "index.html";
    }

    @GetMapping(value = "/map")
    public String mapView() {
        return "map.html";
    }

    @GetMapping(value = "/openlayermap")
    public String openLayerMapView() {
        return "map-openlayers.html";
    }

}
