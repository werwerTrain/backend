package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("trainOrders")
public class TrainOrder{
    private String oid;
    private String trainId;
    private String trainDate;
    private String name;
    private String identification;
    private String seatType;

    public TrainOrder(String oid, String trainId, String trainDate, String name, String identification, String seatType) {
        this.oid = oid;
        this.trainId = trainId;
        this.trainDate = trainDate;
        this.name = name;
        this.identification = identification;
        this.seatType = seatType;
    }

    public TrainOrder() {

    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
}
