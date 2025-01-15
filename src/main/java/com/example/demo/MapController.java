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
    private String robotUrl2 = "192.168.1.107";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        System.out.println("getImage");
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

//    @GetMapping("/getloc")
//    public ResponseEntity<?> getMap() {
//        System.out.println("getloc");
//        RestTemplate restTemplate=new RestTemplate();
//        ResponseEntity<String> pythonResponse = restTemplate.getForEntity(
//                "http://" + robotUrl + ":5000/get_loc",
//                String.class
//        );
//        System.out.println(pythonResponse.getBody());
//        return ResponseEntity.ok().body(pythonResponse.getBody());
//    }

//    @GetMapping("/get_battery")
//    public ResponseEntity<?> getBatteryLevel() {
//        // Replace this with actual battery level reading logic
//        System.out.println("get_battery");
////        int batteryLevel = 23;
////        Map<String, Integer> response = new HashMap<>();
////        response.put("x", batteryLevel);
////        return ResponseEntity.ok(response);
//        RestTemplate restTemplate=new RestTemplate();
//        ResponseEntity<String> pythonResponse = restTemplate.getForEntity(
//                "http://" + robotUrl + ":5000/get_battery",
//                String.class
//        );
//        System.out.println(pythonResponse.getBody());
//        return ResponseEntity.ok().body(pythonResponse.getBody());
//    }

@PostMapping("/getloc")
public ResponseEntity<?> getMap(@RequestBody Map<String, String> robotIdMap) {
    System.out.println("getloc");
    RestTemplate restTemplate=new RestTemplate();
    String robotId = robotIdMap.get("robotId");
    System.out.println("Received getMap request for robot: " + robotId);
    ResponseEntity<String> pythonResponse = null;
    if( robotId.equals("R001")){
        pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl + ":5000/get_loc",
                String.class
        );
    }
    else if( robotId.equals("R002")){
        pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl2 + ":5000/get_loc",
                String.class
        );
    }
    System.out.println(pythonResponse.getBody());
    return ResponseEntity.ok().body(pythonResponse.getBody());
}

@PostMapping("/get_battery")
public ResponseEntity<?> getBatteryLevel(@RequestBody Map<String, String> batteryLevel) {
    // Replace this with actual battery level reading logic
    System.out.println("get_battery");

    String robotId = batteryLevel.get("robotId");
    System.out.println("Received battery level request for robot: " + robotId);

    ResponseEntity<String> pythonResponse = null;

    if( robotId.equals("R001")) {
        RestTemplate restTemplate=new RestTemplate();
        pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl + ":5000/get_battery",
                String.class
        );
        System.out.println(pythonResponse.getBody());
    }
    else if( robotId.equals("R002")) {
        RestTemplate restTemplate=new RestTemplate();
        pythonResponse = restTemplate.getForEntity(
                "http://" + robotUrl2 + ":5000/get_battery",
                String.class
        );
        System.out.println(pythonResponse.getBody());
    }


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

        String speed = directionMap.get("speed");
        System.out.println(speed);

        String robotId = directionMap.get("robotId");
        System.out.println(robotId);
        RestTemplate restTemplate = new RestTemplate();

        // 直接使用接收到的 Map 作为请求体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(directionMap, headers);

        ResponseEntity<String> pythonResponse = null;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok().body(null);
        Map<String, Object> response = new HashMap<>();

        if( robotId.equals("R001")) {
            try {
                pythonResponse = restTemplate.postForEntity(
                        "http://" + robotUrl + ":5000/move_ctrl",
                        requestEntity,
                        String.class
                );
                response.put("status", "success");
                response.put("message", "Move processed successfully");
                response.put("ok", "200");
                responseEntity = ResponseEntity.ok().body(response);
            }catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Error processing request");
                response.put("ok", "500");
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        else if( robotId.equals("R002")){
            try {
                pythonResponse = restTemplate.postForEntity(
                        "http://" + robotUrl2 + ":5000/move_ctrl",
                        requestEntity,
                        String.class
                );
                response.put("status", "success");
                response.put("message", "Move processed successfully");
                response.put("ok", "200");
                responseEntity = ResponseEntity.ok().body(response);
            }catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Error processing request");
                response.put("ok", "500");
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        return responseEntity;

//        try {
//            pythonResponse = restTemplate.postForEntity(
//                    "http://" + robotUrl + ":5000/move_ctrl",
//                    requestEntity,
//                    String.class
//            );
//            return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Move processed successfully\",\"ok\":\"200\"}");
//        } catch (Exception e) {
//            System.err.println("Error processing request: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("{\"status\":\"error\",\"message\":\"Error processing request\",\"ok\":\"500\"}");
//        }
    }
}