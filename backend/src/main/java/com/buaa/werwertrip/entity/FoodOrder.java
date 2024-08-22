package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("foodOrders")
public class FoodOrder {
    private String oid;

    private String foodName;

    private Integer count;

    private String trainId;

    private String trainDate;

    private String mealDate;

    private String mealTime;

    private String photo;

    public FoodOrder() {

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }


    public FoodOrder(String oid, String foodName, Integer count, String trainId, String trainDate, String mealDate, String mealTime, String photo) {
        this.oid = oid;
        this.foodName = foodName;
        this.count = count;
        this.trainId = trainId;
        this.trainDate = trainDate;
        this.mealDate = mealDate;
        this.mealTime = mealTime;
        this.photo = photo;
    }
}
