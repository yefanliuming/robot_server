package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessageController messageController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // 初始化 MockRestTemplate
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn("{\"status\": \"success\", \"message\": \"Message received and forwarded successfully.\"}");
    }

    @Test
    public void testReceiveMessage() throws Exception {
        // 创建 MyMessage 对象
        MyMessage message = new MyMessage();
        message.setActionType("testAction");

        // 发送 POST 请求
        MvcResult result = mockMvc.perform(post("/api/receiveMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andReturn();

        // 验证转发请求
        verify(restTemplate).postForObject(eq("http://192.168.3.229:8000/post_string/"), any(HttpEntity.class), eq(String.class));

        // 获取转发的请求体
        String forwardedRequestBody = result.getResponse().getContentAsString();

        // 解析转发的请求体
        MyMessage forwardedMessage = objectMapper.readValue(forwardedRequestBody, MyMessage.class);

        // 验证转发的消息中只有 actionType 字段被设置
        assertEquals("testAction", forwardedMessage.getActionType());
        assertNull(forwardedMessage.getFloor());
        assertNull(forwardedMessage.getRoom());
        assertNull(forwardedMessage.getOperationType());
        assertNull(forwardedMessage.getButtonAction());
        assertNull(forwardedMessage.getSwitchNumber());
        assertNull(forwardedMessage.getActionDirection());
        assertNull(forwardedMessage.getRobotId());
    }
}
