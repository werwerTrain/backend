package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.Food;
import com.buaa.werwertrip.entity.FoodOrder;
import com.buaa.werwertrip.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IFoodService {
    public List<Food> getAllFood(String trainId, String date, String type);

    public Food findFoodByAllKeys(String trainId, String date, String type, String name);

    public void addFoodOrder(FoodOrder foodOrder);

    public List<FoodOrder> getFoodOrdersByOid(String oid);

    public List<FoodOrder> getFoodOrdersByTrain(String trainId, String trainDate);

    public List<Order> getTrainRelatedFoodOrders(String tid, String date, String uid);

    public void reduceFoodNum(Food food, int num);
}
