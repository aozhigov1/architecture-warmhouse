package com.warmhouse.temperatureapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class TemperatureController {

    private static final Map<String, String> SENSOR_ID_TO_LOCATION = Map.of(
            "1", "Living Room",
            "2", "Bedroom",
            "3", "Kitchen"
    );

    private static final Map<String, String> LOCATION_TO_SENSOR_ID = Map.of(
            "Living Room", "1",
            "Bedroom", "2",
            "Kitchen", "3"
    );

    private final Random random = new Random();

    @GetMapping("/temperature")
    public Map<String, Object> getTemperature(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String sensorId) {

        if (location == null || location.isEmpty()) {
            if (sensorId == null || sensorId.isEmpty()) {
                sensorId = "1";
                location = "Living Room";
            } else {
                location = SENSOR_ID_TO_LOCATION.getOrDefault(sensorId, "Unknown");
            }
        }

        if (sensorId == null || sensorId.isEmpty()) {
            sensorId = LOCATION_TO_SENSOR_ID.getOrDefault(location, "0");
        }

        double temperature = 15 + random.nextDouble() * 15;
        temperature = Math.round(temperature * 10) / 10.0;

        Map<String, Object> response = new HashMap<>();
        response.put("sensorId", sensorId);
        response.put("location", location);
        response.put("temperature", temperature);
        response.put("unit", "C");

        return response;
    }
}
