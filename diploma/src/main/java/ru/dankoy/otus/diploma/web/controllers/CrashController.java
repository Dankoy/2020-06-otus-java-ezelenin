package ru.dankoy.otus.diploma.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrashController {

    @GetMapping(value = "/")
    public String startView() {
        return "map.html";
    }

    @GetMapping(value = "/openlayermap")
    public String openLayerMapView() {
        return "map-openlayers.html";
    }

}
