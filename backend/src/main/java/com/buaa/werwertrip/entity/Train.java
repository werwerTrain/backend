package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@TableName("trains")
public class Train {
    private String trainId;

    private Date date;

    private String startStation;

    private String arrivalStation;

    private String startTime;

    private String arrivalTime;

    private Time duration;
    // 0高铁 1火车
    public enum TrainType {
        Gaotie, Huoche
    }

    private TrainType type;
    private Boolean haveCanceled;

    public Boolean getHaveCanceled() {
        return haveCanceled;
    }

    public void setHaveCanceled(Boolean haveCanceled) {
        this.haveCanceled = haveCanceled;
    }

    public TrainType getType() {
        return type;
    }

    public void setType(TrainType type) {
        this.type = type;
    }

    private int businessSeatSurplus;

    private double businessSeatPrice;

    private int firstClassSeatSurplus;

    private double firstClassSeatPrice;

    private int secondClassSeatSurplus;

    private double secondClassSeatPrice;

    private int softSleeperSurplus;

    private double softSleeperPrice;

    private int hardSleeperSurplus;

    private double hardSleeperPrice;
    private int hardSeatSurplus;
    private double hardSeatPrice;

    private List<Station> stopover;


    public int getHardSeatSurplus() {
        return hardSeatSurplus;
    }

    public void setHardSeatSurplus(int hardSeatSurplus) {
        this.hardSeatSurplus = hardSeatSurplus;
    }

    public double getHardSeatPrice() {
        return hardSeatPrice;
    }

    public void setHardSeatPrice(double hardSeatPrice) {
        this.hardSeatPrice = hardSeatPrice;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public int getBusinessSeatSurplus() {
        return businessSeatSurplus;
    }

    public void setBusinessSeatSurplus(int businessSeatSurplus) {
        this.businessSeatSurplus = businessSeatSurplus;
    }

    public double getBusinessSeatPrice() {
        return businessSeatPrice;
    }

    public void setBusinessSeatPrice(double businessSeatPrice) {
        this.businessSeatPrice = businessSeatPrice;
    }

    public int getFirstClassSeatSurplus() {
        return firstClassSeatSurplus;
    }

    public void setFirstClassSeatSurplus(int firstClassSeatSurplus) {
        this.firstClassSeatSurplus = firstClassSeatSurplus;
    }

    public double getFirstClassSeatPrice() {
        return firstClassSeatPrice;
    }

    public void setFirstClassSeatPrice(double firstClassSeatPrice) {
        this.firstClassSeatPrice = firstClassSeatPrice;
    }

    public int getSecondClassSeatSurplus() {
        return secondClassSeatSurplus;
    }

    public void setSecondClassSeatSurplus(int secondClassSeatSurplus) {
        this.secondClassSeatSurplus = secondClassSeatSurplus;
    }

    public double getSecondClassSeatPrice() {
        return secondClassSeatPrice;
    }

    public void setSecondClassSeatPrice(double secondClassSeatPrice) {
        this.secondClassSeatPrice = secondClassSeatPrice;
    }

    public int getSoftSleeperSurplus() {
        return softSleeperSurplus;
    }

    public void setSoftSleeperSurplus(int softSleeperSurplus) {
        this.softSleeperSurplus = softSleeperSurplus;
    }

    public double getSoftSleeperPrice() {
        return softSleeperPrice;
    }

    public void setSoftSleeperPrice(double softSleeperPrice) {
        this.softSleeperPrice = softSleeperPrice;
    }

    public int getHardSleeperSurplus() {
        return hardSleeperSurplus;
    }

    public void setHardSleeperSurplus(int hardSleeperSurplus) {
        this.hardSleeperSurplus = hardSleeperSurplus;
    }

    public double getHardSleeperPrice() {
        return hardSleeperPrice;
    }

    public void setHardSleeperPrice(double hardSleeperPrice) {
        this.hardSleeperPrice = hardSleeperPrice;
    }

    public List<Station> getStopover() {
        return stopover;
    }

    public void setStopover(List<Station> stopover) {
        this.stopover = stopover;
    }
}