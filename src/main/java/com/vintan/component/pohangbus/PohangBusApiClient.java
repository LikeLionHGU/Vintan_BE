package com.vintan.component.pohangbus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.domain.BusStop;
import com.vintan.repository.BusStopRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PohangBusApiClient {

    private final BusStopRepository busStopRepository;  // Repository for saving bus stop data
    private final RestTemplate restTemplate;            // Used for making API calls
    private final ObjectMapper objectMapper = new ObjectMapper(); // For parsing JSON response

    @Value("${pohang.api.key}")
    private String pohangApiKey; // Decoded Pohang Bus API key

    /**
     * Runs after the application starts.
     * If there is no bus stop data in the DB, it fetches data from the API and saves it.
     */
    @PostConstruct
    public void initBusStopData() {
        if (busStopRepository.count() == 0) {
            System.out.println("No bus stop data found. Fetching from API...");
            fetchAndSaveAllBusStops();
        } else {
            System.out.println("Bus stop data already exists in the DB.");
        }
    }

    /**
     * Fetches all bus stop data from the API and saves it to the database.
     */
    public void fetchAndSaveAllBusStops() {
        boolean success = fetchBusStops(pohangApiKey);
        if (!success) {
            System.err.println("### Final API call failure ### API key is valid but request failed.");
        }
    }

    /**
     * Makes actual API calls and saves bus stop data to the DB.
     * Handles pagination and merges route names for the same bus stop.
     */
    private boolean fetchBusStops(String apiKey) {
        try {
            int pageNo = 1;
            final int numOfRows = 100; // Number of rows per page
            int totalCount = -1;       // Total number of records
            List<BusStop> allBusStops = new ArrayList<>();
            String baseUrl = "https://apis.data.go.kr/5020000/busStopAcctoUserTrnsitnStepTwo/getBusStopAcctoUserTrnsitnStepTwo?serviceKey=";

            // Handle pagination
            do {
                // Build the API request URL
                String url = baseUrl + apiKey
                        + "&pageNo=" + pageNo
                        + "&numOfRows=" + numOfRows
                        + "&resultType=json";

                System.out.println(pageNo + " page API call URL: " + url);

                // Make the API call
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                String responseBody = response.getBody();

                if (responseBody == null || responseBody.isBlank()) {
                    System.err.println("API response is empty.");
                    break;
                }

                // If the response starts with "<", it's an XML error response
                if (responseBody.trim().startsWith("<")) {
                    System.err.println("API returned an error response (XML): " + responseBody);
                    break;
                }

                // Parse JSON response
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode body = root.path("response").path("body");

                // Get total count from the first page
                if (pageNo == 1) {
                    totalCount = body.path("totalCount").asInt();
                    System.out.println("Total records: " + totalCount);
                    if (totalCount == 0) {
                        System.out.println("API returned no data.");
                        break;
                    }
                }

                // Get bus stop data (items array)
                JsonNode items = body.path("items").path("item");
                if (items.isArray()) {
                    for (JsonNode item : items) {
                        allBusStops.add(BusStop.builder()
                                .stationName(item.path("bus_sttn").asText()) // Bus stop name
                                .latitude(item.path("la").asDouble())        // Latitude
                                .longitude(item.path("lo").asDouble())       // Longitude
                                .routeNames(item.path("route_nm").asText())  // Bus route name
                                .build());
                    }
                }

                pageNo++; // Go to next page

            } while ((pageNo - 1) * numOfRows < totalCount);

            // Merge bus stops with the same name (combine route names)
            Map<String, List<BusStop>> groupedByName = allBusStops.stream()
                    .filter(s -> s.getStationName() != null && !s.getStationName().isEmpty())
                    .collect(Collectors.groupingBy(BusStop::getStationName));

            List<BusStop> mergedBusStops = groupedByName.values().stream().map(stops -> {
                BusStop firstStop = stops.get(0);
                String mergedRoutes = stops.stream()
                        .map(BusStop::getRouteNames)
                        .distinct()
                        .collect(Collectors.joining(", "));
                return BusStop.builder()
                        .stationName(firstStop.getStationName())
                        .latitude(firstStop.getLatitude())
                        .longitude(firstStop.getLongitude())
                        .routeNames(mergedRoutes)
                        .build();
            }).collect(Collectors.toList());

            // Save merged data to the DB
            busStopRepository.saveAll(mergedBusStops);
            System.out.println("Saved " + mergedBusStops.size() + " bus stops to the DB.");
            return true;

        } catch (Exception e) {
            System.err.println("Exception during API call: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a list of bus routes within a 1 km radius of the given location.
     */
    public List<String> getBusRoutesNear(double latitude, double longitude) {
        List<BusStop> allBusStops = busStopRepository.findAll();
        return allBusStops.stream()
                .filter(stop -> calculateDistance(latitude, longitude, stop.getLatitude(), stop.getLongitude()) <= 1.0)
                .map(BusStop::getRouteNames)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the distance between two coordinates using the Haversine formula.
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
