package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MapController {

    private String robotUrl = "192.168.1.105";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            Path file = Paths.get("src/main/resources/static/images").resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getloc")
    public ResponseEntity<?> getMap() {
        System.out.println("getloc");
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl + ":5000/get_loc",
                String.class
        );
        System.out.println(pythonResponse.getBody());
        return ResponseEntity.ok().body(pythonResponse.getBody());
    }

    @GetMapping("/get_battery")
    public ResponseEntity<?> getBatteryLevel() {
        // Replace this with actual battery level reading logic
        System.out.println("get_battery");
//        int batteryLevel = 23;
//        Map<String, Integer> response = new HashMap<>();
//        response.put("x", batteryLevel);
//        return ResponseEntity.ok(response);
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl + ":5000/get_battery",
                String.class
        );
        System.out.println(pythonResponse.getBody());
        return ResponseEntity.ok().body(pythonResponse.getBody());
    }

//    @PostMapping("/move")
//    public ResponseEntity<?> getMove(@RequestBody String direction) {
//        System.out.println(direction);
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 创建 HttpHeaders 并设置 Content-Type
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 创建包含方向的 JSON 对象
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("direction", direction);
//
//        // 创建 HttpEntity，包含请求体和头信息
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        try {
//            ResponseEntity<String> pythonResponse = restTemplate.postForEntity(
//                    "http://" + robotUrl + ":5000/move_ctrl",
//                    requestEntity,
//                    String.class
//            );
//            return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Move processed successfully\",\"ok\":\"200\"}");
//        }catch (Exception e) {
//            System.err.println("Error processing request: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("{\"status\":\"error\",\"message\":\"Error processing request\",\"ok\":\"500\"}");
//        }
//
//    }

    @PostMapping("/move")
    public ResponseEntity<?> getMove(@RequestBody Map<String, String> directionMap) {
        String direction = directionMap.get("direction");
        System.out.println(direction);
        RestTemplate restTemplate = new RestTemplate();

        // 直接使用接收到的 Map 作为请求体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(directionMap, headers);

        try {
            ResponseEntity<String> pythonResponse = restTemplate.postForEntity(
                    "http://" + robotUrl + ":5000/move_ctrl",
                    requestEntity,
                    String.class
            );
            return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Move processed successfully\",\"ok\":\"200\"}");
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":\"error\",\"message\":\"Error processing request\",\"ok\":\"500\"}");
        }
    }
}