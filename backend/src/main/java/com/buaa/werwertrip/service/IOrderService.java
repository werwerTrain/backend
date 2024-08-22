package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IOrderService {

    public List<Order> getOrdersByUidAndStatus(String uid, Order.OrderStatus status, Order.OrderType type);

    public List<Order> getOrderByUid(String uid, Order.OrderType type);

    public Order getOrderByOidAndUid(String oid, String uid);

    public Order getOrder(String oid);

    public void addOrder(Order order);

    public void cancelOrder(Order order);

    public void deleteOrder(Order order);

    void setCancelTime(String oid, String cancelTime);

    List<Order> getIdByUid(String userID);

    List<Order> getIdByUidAndStatus(String userID, String status);

    List<Order> getAllTrain();

    Boolean getMessageSend(String orderId);

    void setMessageHaveSend(String orderId);

    void finishOrder(String oid);

}
