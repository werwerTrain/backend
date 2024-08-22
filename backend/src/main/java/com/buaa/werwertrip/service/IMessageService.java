package com.buaa.werwertrip.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// 消息
@Component
public interface IMessageService {
    void addMessage(String userId, String mid, String orderId, String title, String messageTime, String content, Boolean haveRead, String orderType);

    List<Map<String, Object>> getMessage(String userId);

    void setHaveread(String mid);

}
