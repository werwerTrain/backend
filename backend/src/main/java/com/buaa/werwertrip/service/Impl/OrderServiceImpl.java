package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.entity.Order;
import com.buaa.werwertrip.mapper.IOrderMapper;
import com.buaa.werwertrip.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    IOrderMapper orderMapper;

    @Override
    public List<Order> getOrdersByUidAndStatus(String uid, Order.OrderStatus status, Order.OrderType type) {
        return orderMapper.getOrdersByUidAndStatus(uid, status, type);
    }

    @Override
    public List<Order> getOrderByUid(String uid, Order.OrderType type) {
        return orderMapper.getOrderByUid(uid, type);
    }

    @Override
    public Order getOrderByOidAndUid(String oid, String uid) {
        return orderMapper.getOrderByOidAndUid(oid, uid);
    }

    @Override
    public Order getOrder(String oid) {
        return orderMapper.getOrder(oid);
    }

    @Override
    public void addOrder(Order order) {
        orderMapper.addOrder(order);
    }

    @Override
    public void cancelOrder(Order order) {
        orderMapper.cancelOrder(order);
    }

    @Override
    public void deleteOrder(Order order) {
        orderMapper.deleteOrder(order);
    }

    @Override
    public void setCancelTime(String oid, String cancelTime) {
        orderMapper.setCancelTime(oid, cancelTime);
    }

    @Override
    public List<Order> getIdByUidAndStatus(String userID, String status) {
        return orderMapper.getIdByUidAndStatus(userID, status);
    }

    @Override
    public List<Order> getIdByUid(String userID) {
        return orderMapper.getIdByUid(userID);
    }

    @Override
    public List<Order> getAllTrain() {
        return orderMapper.getAllTrainOrder();
    }

    @Override
    public Boolean getMessageSend(String orderId) {
        return orderMapper.getMessageSend(orderId);
    }

    @Override
    public void setMessageHaveSend(String orderId) {
        orderMapper.setMessageHaveSend(orderId);
    }

    @Override
    public void finishOrder(String oid) {
        orderMapper.finishOrder(oid);
    }

}
