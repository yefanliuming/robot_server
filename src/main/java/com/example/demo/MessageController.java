package com.example.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;




@RestController
@RequestMapping("/api")
public class MessageController {
    private final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    // 用于存储消息响应的Map
    private final Map<String, Boolean> messageResponses = new ConcurrentHashMap<>();

    private String robotUrl = "192.168.1.105";
    private String robotUrl2 = "192.168.1.107";


//    @CrossOrigin(origins = "*")
//    @PostMapping("/receiveMessage")
//    public ResponseEntity<StreamingResponseBody> receiveMessage(@RequestBody MyMessage message) {
//        System.out.println("Received message: " + message);
//        StreamingResponseBody responseBody = output -> {
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(message);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);
//
//                try {
//                    String targetUrl = "http://" + robotUrl + ":5000/receive_message";
////                    String targetUrl = "http://192.168.1.105:5000/receive_message";  // Python服务器IP
//                    restTemplate.postForObject(targetUrl, entity, String.class);
//                } catch (Exception e) {
//                    System.err.println("Failed to connect to Python server: " + e.getMessage());
//                    writer.write("data: 无法连接到Python服务器\n\n");
//                    writer.flush();
//                    return;  // Exit early if Python server is unreachable
//                }
//
//                // Send an initial empty message to start the stream
//                writer.write("data: \n\n");
//                writer.flush();
//
//                long lastMessageTime = System.currentTimeMillis();
////                while (!Thread.currentThread().isInterrupted()) {
////                    String message1 = messageQueue.poll();
////                    if (message1 != null) {
////                        writer.write("data: " + message1 + "\n\n");
////                        writer.flush();
////                        lastMessageTime = System.currentTimeMillis();
////                    }
////
////                    // Check if we've exceeded the timeout period (14 seconds to be safe)
////                    if (System.currentTimeMillis() - lastMessageTime > 14000) {
////                        break;
////                    }
////
////                    Thread.sleep(10);
////                }
//                while (!Thread.currentThread().isInterrupted()) {
//                    String message1 = messageQueue.poll();
//                    if (message1 != null) {
//                        writer.write("data: " + message1 + "\n\n");
//                        writer.flush();
//                        lastMessageTime = System.currentTimeMillis();
//                    }
//
//                    // Check if we've exceeded the timeout period (14 seconds to be safe)
//                    if (System.currentTimeMillis() - lastMessageTime > 14000) {
//                        break;
//                    }
//
//                    Thread.sleep(10);
//                }
//            } catch (Exception e) {
//                System.err.println("Error in stream processing: " + e.getMessage());
//            }
//        };
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_EVENT_STREAM)
//                .body(responseBody);
//    }
@CrossOrigin(origins = "*")
@PostMapping("/receiveMessage")
public ResponseEntity<StreamingResponseBody> receiveMessage(@RequestBody MyMessage message) {
    System.out.println("Received message: " + message);

    System.out.println("TargetRobot" + message.getRobotId());

    if(message.getRobotId().equals("R001")){
        StreamingResponseBody responseBody = output -> {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);

                try {
                    String targetUrl = "http://" + robotUrl + ":5000/receive_message";
                    ResponseEntity<String> pythonResponse = restTemplate.postForEntity(targetUrl, entity, String.class);

                    if (pythonResponse.getStatusCode() == HttpStatus.OK &&
                            pythonResponse.getBody() != null &&
                            pythonResponse.getBody().equals("连接已建立")) {
                        writer.write("data: 连接已建立\n\n");
                    } else {
                        writer.write("data: 无法连接到Python服务器\n\n");
                    }
                    writer.flush();
                    return;
                } catch (Exception e) {
                    System.err.println("Failed to connect to Python server: " + e.getMessage());
                    writer.write("data: 无法连接到Python服务器\n\n");
                    writer.flush();
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error in stream processing: " + e.getMessage());
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseBody);
    }else {
        StreamingResponseBody responseBody = output -> {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);

                try {
                    String targetUrl = "http://" + robotUrl2 + ":5000/receive_message";
                    ResponseEntity<String> pythonResponse = restTemplate.postForEntity(targetUrl, entity, String.class);

                    if (pythonResponse.getStatusCode() == HttpStatus.OK &&
                            pythonResponse.getBody() != null &&
                            pythonResponse.getBody().equals("连接已建立")) {
                        writer.write("data: 连接已建立\n\n");
                    } else {
                        writer.write("data: 无法连接到Python服务器\n\n");
                    }
                    writer.flush();
                    return;
                } catch (Exception e) {
                    System.err.println("Failed to connect to Python server: " + e.getMessage());
                    writer.write("data: 无法连接到Python服务器\n\n");
                    writer.flush();
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error in stream processing: " + e.getMessage());
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseBody);
    }

//    StreamingResponseBody responseBody = output -> {
//        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(message);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);
//
//            try {
//                String targetUrl = "http://" + robotUrl + ":5000/receive_message";
//                ResponseEntity<String> pythonResponse = restTemplate.postForEntity(targetUrl, entity, String.class);
//
//                if (pythonResponse.getStatusCode() == HttpStatus.OK &&
//                        pythonResponse.getBody() != null &&
//                        pythonResponse.getBody().equals("连接已建立")) {
//                    writer.write("data: 连接已建立\n\n");
//                } else {
//                    writer.write("data: 无法连接到Python服务器\n\n");
//                }
//                writer.flush();
//                return;
//            } catch (Exception e) {
//                System.err.println("Failed to connect to Python server: " + e.getMessage());
//                writer.write("data: 无法连接到Python服务器\n\n");
//                writer.flush();
//                return;
//            }
//        } catch (Exception e) {
//            System.err.println("Error in stream processing: " + e.getMessage());
//        }
//    };

//    return ResponseEntity.ok()
//            .contentType(MediaType.TEXT_EVENT_STREAM)
//            .body(responseBody);
}

    @PostMapping("/pythonMessage")
    public ResponseEntity<String> receivePythonMessage(@RequestBody String message) {
        try {
            System.out.println("Received Python message: " + message);

            // Try to parse as JSON
            JsonNode jsonNode = objectMapper.readTree(message);
            String actualMessage = jsonNode.has("message")
                    ? jsonNode.get("message").asText()
                    : message;

            messageQueue.offer(actualMessage);
            System.out.println("Message added to queue: " + actualMessage);

            return ResponseEntity.ok("Message received and queued");
        } catch (JsonProcessingException e) {
            // If not JSON, use the raw message
            System.out.println("Received non-JSON message: " + message);
            messageQueue.offer(message);
            return ResponseEntity.ok("Raw message received and queued");
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing message: " + e.getMessage());
        }
    }

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
//    @CrossOrigin(origins = "*")
//    @PostMapping("/message")
//    public ResponseEntity<?> receiveMessage(@RequestBody Message message) {
//        System.out.println(message);
//
//        // 广播消息给所有订阅的客户端
//        messagingTemplate.convertAndSend("/topic/messages", message);
//
//        return ResponseEntity.ok().build();
//    }



    @CrossOrigin(origins = "*")
    @PostMapping("/receiveMessage2")
    public ResponseEntity<?> receiveMessage(@RequestBody TaskRequest taskRequest) {
        try {
        // 记录接收到的请求
            String jsonMessage = objectMapper.writeValueAsString(taskRequest);
            System.out.println("Received task request: " + jsonMessage);

        // 创建 RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            String targetUrl = "http://"+robotUrl+":5000/receive_message";

        // 转发请求到 Python 服务
            ResponseEntity<String> pythonResponse = restTemplate.postForEntity(
                    targetUrl,
                    taskRequest,
                    String.class
            );

        // 返回成功响应
            System.out.println("Python response: " + pythonResponse.getBody());
            return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Task processed successfully\",\"ok\":\"200\"}");

        } catch (Exception e) {
            // 记录错误并返回错误响应
            System.err.println("Error processing request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing request: " + e.getMessage());
        }
    }

//    @CrossOrigin(origins = "*")
//    @PostMapping("/message-response")
//    public ResponseEntity<?> saveResponse(@RequestBody MessageResponse response) throws JsonProcessingException {
//        String jsonMessage = objectMapper.writeValueAsString(response);
//        System.out.println("Received task response: " + jsonMessage);
//        messageResponses.put(response.getId(), response.getConfirmed());
//        return ResponseEntity.ok().build();
//    }
//
//    @CrossOrigin(origins = "*")
//    @GetMapping("/message-response/{messageId}")
//    public ResponseEntity<?> getResponse(@PathVariable String messageId) {
//        Boolean response = messageResponses.get(messageId);
//        if (response != null) {
//            System.out.println("Response for messageId " + messageId + ": " + response);
//            messageResponses.remove(messageId); // 获取后删除响应
//            return ResponseEntity.ok(new MessageResponse(messageId, response));
//        }
//        return ResponseEntity.notFound().build();
//    }

    @CrossOrigin(origins = "*")
    @PostMapping("/message")
    public ResponseEntity<?> receiveMessage(@RequestBody Message message) {
        System.out.println("Received message: " + message);
        messagingTemplate.convertAndSend("/topic/messages", message);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/message-response")
    public ResponseEntity<?> saveResponse(@RequestBody MessageResponse response) {
        System.out.println("Received response: " + response);
        messageResponses.put(response.getId(), response.getConfirmed());
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/message-response/{messageId}")
    public ResponseEntity<?> getResponse(@PathVariable("messageId") String messageId) {  // 添加参数名称
        Boolean response = messageResponses.get(messageId);
        if (response != null) {
            messageResponses.remove(messageId);
            return ResponseEntity.ok(new MessageResponse(messageId, response));
        }
        return ResponseEntity.notFound().build();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Message {
    private String message;
    private long timestamp;
    private String id;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MessageResponse {
    private String id;
    private Boolean confirmed;
}

