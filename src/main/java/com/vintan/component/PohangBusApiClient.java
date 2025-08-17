package com.vintan.component;

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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PohangBusApiClient {

    private final BusStopRepository busStopRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${pohang.api.key}")
    private String pohangApiKey; // 디코딩된 키가 여기에 주입됩니다.

    @PostConstruct
    public void initBusStopData() {
        if (busStopRepository.count() == 0) {
            System.out.println("버스 정류장 데이터가 없습니다. API를 호출하여 데이터를 저장합니다...");
            fetchAndSaveAllBusStops();
        } else {
            System.out.println("버스 정류장 데이터가 이미 DB에 존재합니다.");
        }
    }

    public void fetchAndSaveAllBusStops() {
        // 단 한 번의 올바른 방법으로 API를 호출합니다.
        boolean success = fetchBusStops(pohangApiKey);
        if (!success) {
            System.err.println("### API 호출 최종 실패 ### API 키는 정상이나 호출 과정에 문제가 있습니다. 코드를 다시 확인해주세요.");
        }
    }

    private boolean fetchBusStops(String apiKey) {
        try {
            int pageNo = 1;
            final int numOfRows = 100; // 페이지 크기
            int totalCount = -1;
            List<BusStop> allBusStops = new ArrayList<>();
            String baseUrl = "https://apis.data.go.kr/5020000/busStopAcctoUserTrnsitnStepTwo/getBusStopAcctoUserTrnsitnStepTwo?serviceKey=";

            do {
                String url = baseUrl + apiKey
                        + "&pageNo=" + pageNo
                        + "&numOfRows=" + numOfRows
                        + "&resultType=json";

                System.out.println(pageNo + " 페이지 API 호출 URL: " + url);

                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                String responseBody = response.getBody();

                if (responseBody == null || responseBody.isBlank()) {
                    System.err.println("API 응답이 비어 있습니다.");
                    break;
                }

                // 혹시 XML 에러 응답이면 중단
                if (responseBody.trim().startsWith("<")) {
                    System.err.println("API 에러 응답 (XML): " + responseBody);
                    break;
                }

                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode body = root.path("response").path("body");

                if (pageNo == 1) {
                    totalCount = body.path("totalCount").asInt();
                    System.out.println("총 데이터 개수: " + totalCount);
                    if (totalCount == 0) {
                        System.out.println("API에서 반환된 데이터가 없습니다.");
                        break;
                    }
                }

                JsonNode items = body.path("items").path("item");
                if (items.isArray()) {
                    for (JsonNode item : items) {
                        allBusStops.add(BusStop.builder()
                                .stationName(item.path("bus_sttn").asText())
                                .latitude(item.path("la").asDouble())
                                .longitude(item.path("lo").asDouble())
                                .routeNames(item.path("route_nm").asText())
                                .build());
                    }
                }

                pageNo++;

            } while ((pageNo - 1) * numOfRows < totalCount);

            // 🚍 정류장 이름 기준으로 병합 (노선 합치기)
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

            busStopRepository.saveAll(mergedBusStops);
            System.out.println("총 " + mergedBusStops.size() + "개의 버스 정류장 정보를 DB에 저장했습니다.");
            return true;

        } catch (Exception e) {
            System.err.println("API 호출 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private BusStop jsonToBusStop(JsonNode item) {
        return BusStop.builder()
                .stationName(item.path("bus_sttn").asText())
                .latitude(item.path("la").asDouble())
                .longitude(item.path("lo").asDouble())
                .routeNames(item.path("route_nm").asText())
                .build();
    }

    public List<String> getBusRoutesNear(double latitude, double longitude) {
        List<BusStop> allBusStops = busStopRepository.findAll();
        return allBusStops.stream()
                .filter(stop -> calculateDistance(latitude, longitude, stop.getLatitude(), stop.getLongitude()) <= 1.0)
                .map(BusStop::getRouteNames)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}