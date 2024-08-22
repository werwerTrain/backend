package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.service.IMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private IMessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMessage_WithMessages() {
        // 模拟存在消息的情况
        String userID = "610113200403260040";
        List<Map<String, Object>> mockMessages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("orderType", "Hotel");
        message.put("orderId", "2024052815245812638562");
        message.put("haveRead", false);
        message.put("title", "Test Title");
        message.put("messageTime", LocalDateTime.of(2023, 8, 21, 12, 30, 0));
        message.put("content", "Test Content");
        message.put("mid", "1");
        mockMessages.add(message);

        when(messageService.getMessage(userID)).thenReturn(mockMessages);

        Map<String, Object> result = messageController.getMessage(userID);

        List<Object> expectedResult = new ArrayList<>();
        Map<String, Object> expectedMessage = new HashMap<>();
        expectedMessage.put("orderType", "Hotel");
        expectedMessage.put("orderId", "2024052815245812638562");
        expectedMessage.put("haveRead", false);
        expectedMessage.put("title", "Test Title");
        expectedMessage.put("messageTime", "2023-08-21 12:30:00");
        expectedMessage.put("content", "Test Content");
        expectedMessage.put("mid", "1");
        expectedResult.add(expectedMessage);

        assertEquals(expectedResult, result.get("result"));
        verify(messageService, times(1)).getMessage(userID);
    }

    @Test
    void testGetMessage_NoMessages() {
        // 模拟没有消息的情况
        String userID = "610113200403260040";
        when(messageService.getMessage(userID)).thenReturn(new ArrayList<>());

        Map<String, Object> result = messageController.getMessage(userID);

        assertEquals(new ArrayList<>(), result.get("result"));
        verify(messageService, times(1)).getMessage(userID);
    }

    @Test
    void testSetHaveread() {
        // 测试标记已读功能
        String mid = "1";

        doNothing().when(messageService).setHaveread(mid);

        messageController.setHaveread(mid);

        verify(messageService, times(1)).setHaveread(mid);
    }

    @Test
    void testSetAllRead_WithMessages() {
        // 测试存在消息时的标记全部已读功能
        String userId = "610113200403260040";
        List<Map<String, Object>> mockMessages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("mid", "1");
        mockMessages.add(message);

        when(messageService.getMessage(userId)).thenReturn(mockMessages);
        doNothing().when(messageService).setHaveread("1");

        messageController.setAllRead(userId);

        verify(messageService, times(1)).getMessage(userId);
        verify(messageService, times(1)).setHaveread("1");
    }

    @Test
    void testSetAllRead_NoMessages() {
        // 测试不存在消息时的标记全部已读功能
        String userId = "12345";
        when(messageService.getMessage(userId)).thenReturn(new ArrayList<>());

        messageController.setAllRead(userId);

        verify(messageService, times(1)).getMessage(userId);
        verify(messageService, times(0)).setHaveread(anyString());
    }
}


