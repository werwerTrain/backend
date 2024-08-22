package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.service.IMessageService;
import com.buaa.werwertrip.mapper.IMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// 消息
@Service("messageService")
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private IMessageMapper messageMapper;

    @Override
    public void addMessage(String userId, String mid, String orderId, String title, String messageTime, String content, Boolean haveRead, String orderType) {
        messageMapper.addMessage(userId, mid, orderId, title, messageTime, content, haveRead, orderType);
    }

    @Override
    public List<Map<String, Object>> getMessage(String userId) {
        return messageMapper.getMessage(userId);
    }

    @Override
    public void setHaveread(String mid) {
        messageMapper.setHaveread(mid);
    }
}
