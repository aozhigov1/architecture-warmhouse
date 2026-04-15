package com.warmhouse.temperatureapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TemperatureApi {

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

    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/temperature", new TemperatureHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Temperature API started on port 8081");
    }

    static class TemperatureHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String location = null;
            String sensorId = null;

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        String key = pair[0];
                        String value = pair[1];
                        if ("location".equals(key)) {
                            location = value;
                        } else if ("sensorId".equals(key)) {
                            sensorId = value;
                        }
                    }
                }
            }

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

            String json = String.format(
                    "{\"sensorId\":\"%s\",\"location\":\"%s\",\"temperature\":%.1f,\"unit\":\"C\"}",
                    sensorId, location, temperature
            );

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, json.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(json.getBytes());
            }
        }
    }
}
