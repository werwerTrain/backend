package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@TableName("Orders")
public class Order {
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String oid;

    public Order() {

    }

    public static String generateOrderId() {
        // 获取当前时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());

        // 生成一个五位数的随机数
        Random random = new Random();
        int randomNum = 10000 + random.nextInt(90000);

        // 组合时间戳和随机数生成订单ID
        return timestamp + randomNum;
    }

    private String uid;
    private String billTime;

    private String cancelTime;

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    private Double total;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


    public enum OrderStatus {
        Paid, Canceled, Done
    }

    private OrderStatus orderStatus;

    public enum OrderType {
        Food, Hotel, Train
    }

    private OrderType orderType;

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(String oid, String uid, String billTime, Double total, OrderStatus orderStatus, OrderType orderType) {
        this.oid = oid;
        this.uid = uid;
        this.billTime = billTime;
        this.total = total;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
    }
}
