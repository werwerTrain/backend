package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@RestController
public class StationController {

    @Autowired
    private IStationService stationService;

    @GetMapping("/station")
    public Map<String, Object> inquireAllStations() {
//        response.setHeader("Access-Control-Allow-Origin", "http://49.232.244.162");

        List<Object> list = new ArrayList<>();
        stationService.inquireAllStations().forEach(e -> {
            list.add(new HashMap<>() {{
                put("value", e.getStationName());
            }});
        });
        return new HashMap<>() {{
            put("station", list);
        }};
    }
}
