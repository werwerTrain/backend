package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.*;
import com.buaa.werwertrip.service.*;
import com.buaa.werwertrip.service.Impl.EmailService;
import com.buaa.werwertrip.entity.*;
import com.buaa.werwertrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@RestController
public class FoodController {
    @Autowired
    private IFoodService foodService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ITrainService trainService;
    @Autowired
    private Food food;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserService userService;


    @GetMapping("/food/{userID}/{tid}/{date}/{time}")
    Map<String, Object> getAllFood(@PathVariable String tid,
                                   @PathVariable String userID,
                                   @PathVariable String date,
                                   @PathVariable String time) {
        List<TrainOrder> trainOrders = trainService.getTrainOrderByTrainAndIdentification(tid, date, userID);
        boolean[] haveTicket = {false};
        String info = "没有购买当日该车次车票";
        for (TrainOrder trainOrder : trainOrders) {
            if (orderService.getOrder(trainOrder.getOid()).getOrderStatus() == Order.OrderStatus.Paid) {
                Train train = trainService.getTrainByTidAndDate(tid, date);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                try {
                    LocalTime startTime = LocalTime.parse(train.getStartTime(), formatter);
                    LocalTime arriveTime = LocalTime.parse(train.getArrivalTime(), formatter);

                    LocalTime lunchStartTime = LocalTime.of(12, 0, 0);
                    LocalTime lunchEndTime = LocalTime.of(14, 0, 0);
                    LocalTime dinnerStartTime = LocalTime.of(17, 0, 0);
                    LocalTime dinnerEndTime = LocalTime.of(19, 0, 0);

                    if (time.equals("lunch")) {
                        // [startTime, arriveTime] 与 [lunchStartTime, lunchEndTime]有交集
                        if (startTime.isBefore(lunchEndTime) && arriveTime.isAfter(lunchStartTime)) {
                            haveTicket[0] = true;
                            info = "购买成功";
                        } else {
                            info = "午餐点您不在车上哦";
                        }
                    } else {
                        if (arriveTime.isAfter(dinnerStartTime) && startTime.isBefore(dinnerEndTime)) {
                            haveTicket[0] = true;
                            info = "购买成功";
                        } else {
                            info = "晚餐点您不在车上哦";
                        }
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("时间格式解析错误: " + e.getMessage());
                }
            }
        }

        List<Object> result = new ArrayList<>();
        if (haveTicket[0]) {
            foodService.getAllFood(tid, date, time).forEach(e -> {
                result.add(new HashMap<>() {{
                    put("name", e.getName());
                    put("price", e.getPrice().toString());
                    put("photo", e.getPhoto());
                    put("number", e.getNum());
                }});
            });
        }
        String finalInfo = info;
        return new HashMap<>() {{
            put("result", result);
            put("haveTicket", haveTicket[0]);
            put("info", finalInfo);
        }};
    }

    @PostMapping("/food/bill")
    Map<String, Object> submitFoodOrder(@RequestBody Map<String, Object> map) {
        String userId = (String) map.get("userID");
        String trainId = (String) map.get("tid");
        String mealDate = (String) map.get("date");
        String mealTime = (String) map.get("time");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bill = new Date();
        String billTime = formatter.format(bill);
        Number sumPrice = (Number) (map.get("sum_price"));
        Double total = sumPrice.doubleValue();

        String oid = Order.generateOrderId();

        orderService.addOrder(new Order(oid, userId, billTime, total, Order.OrderStatus.Paid, Order.OrderType.Food));

        try {
            ((List<HashMap<String, Object>>) (map.get("foods"))).forEach(e -> {
                Food f = foodService.findFoodByAllKeys(trainId, mealDate, mealTime, (String) e.get("name"));
                int num = (int) (e.get("number"));
                foodService.addFoodOrder(new FoodOrder(oid, (String) (e.get("name")),
                        num, trainId, mealDate, mealDate, mealTime, f.getPhoto()));
            });
        } catch (Exception e) {
            return null;
        }

        Date date = new Date();
        String formattedDate = formatter.format(date);
        String time;
        if (mealTime.equals("lunch"))
            time = "午餐";
        else
            time = "晚餐";

        //Map<String, Object> trainMap = trainService.getTrainByIdAndDate(trainId,trainDate);
        String content = "【WerwerTrip】您成功预订了" + mealDate + " " + trainId + "车次的" + time + "。感谢您的购买，祝您用餐愉快！";
        String Mcontent = "您成功预订了" + mealDate + " " + trainId + "车次的" + time + "。感谢您的购买，祝您用餐愉快！";

        messageService.addMessage(userId, Message.generateMessageId(), oid, "餐饮订单支付成功", formattedDate, Mcontent, false, "5");

        emailService.sendSimpleMail(userService.getEmail(userId), "餐饮订单支付成功", content);
        return new HashMap<>() {{
            put("info", "下单成功！");
            put("result", true);
        }};
    }

    @GetMapping("/food/orders/{userID}/{status}")
    Map<String, Object> getOrders(@PathVariable String userID,
                                  @PathVariable String status) {
        List<Order> orders = switch (status) {
            case "paid" -> orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Paid, Order.OrderType.Food);
            case "cancel" ->
                    orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Canceled, Order.OrderType.Food);
            case "done" -> orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Done, Order.OrderType.Food);
            default -> orderService.getOrderByUid(userID, Order.OrderType.Food);
        };

        List<Object> result = new ArrayList<>();
        orders.forEach(order -> {
            HashMap<String, Object> map = new HashMap<>();

            List<FoodOrder> foodOrders = foodService.getFoodOrdersByOid(order.getOid());
            if (foodOrders.isEmpty())
                System.out.println(order.getOid());
            map.put("tid", foodOrders.get(0).getTrainId());
            map.put("oid", order.getOid());
            map.put("order_time", order.getBillTime());

            if (order.getOrderStatus() == Order.OrderStatus.Done) {
                map.put("status", "已完成");
            } else if (order.getOrderStatus() == Order.OrderStatus.Paid) {
                map.put("status", "已支付");
            } else {
                map.put("status", "已取消");
            }
            map.put("sum_price", order.getTotal());

            List<Object> foods = new ArrayList<>();
            foodOrders.forEach(foodOrder -> {
                if (!map.containsKey("date")) {
                    map.put("date", foodOrder.getMealDate());
                }
                if (!map.containsKey("time")) {
                    //map.put("time", foodOrder.getMealTime());
                    String s = foodOrder.getMealTime();
                    if (s.equals("lunch"))
                        map.put("time", "午餐");
                    else
                        map.put("time", "晚餐");
                }

                foods.add(new HashMap<>() {{
                    put("food_name", foodOrder.getFoodName());
                    put("count", foodOrder.getCount());
                    put("photo", foodOrder.getPhoto());
                }});
            });
            map.put("foods", foods);
            result.add(map);
        });

        return new HashMap<>() {{
            put("result", result);
        }};
    }

    @PostMapping("/food/cancel/{userID}/{oid}")
    Map<String, Object> cancelOrder(@PathVariable String userID,
                                    @PathVariable String oid) {
        Order order = orderService.getOrderByOidAndUid(oid, userID);
        if (order == null) {
            return new HashMap<>() {{
                put("info", "取消订单失败");
                put("result", false);
            }};
        } else {
            orderService.cancelOrder(order);

            FoodOrder food = foodService.getFoodOrdersByOid(oid).get(0);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);

            String s = food.getMealTime();
            if (s.equals("lunch"))
                s = "午餐";
            else
                s = "晚餐";

            String content = "【WerwerTrip】您已成功取消" + food.getMealDate() + " " + food.getTrainId() + "车次的" + s;
            String Mcontent = "您已成功取消" + food.getMealDate() + " " + food.getTrainId() + "车次的" + s;
            messageService.addMessage(userID, Message.generateMessageId(), oid, "餐饮订单取消成功", formattedDate, Mcontent, false, "5");

            emailService.sendSimpleMail(userService.getEmail(userID), "餐饮订单取消成功", content);
            return new HashMap<>() {{
                put("info", "取消订单成功");
                put("result", true);
            }};
        }
    }

    @PostMapping("/food/delete/{userID}/{oid}")
    Map<String, Object> deleteOrder(@PathVariable String userID,
                                    @PathVariable String oid) {
        Order order = orderService.getOrderByOidAndUid(oid, userID);
        if (order == null) {
            return new HashMap<>() {{
                put("info", "删除订单失败");
                put("result", false);
            }};
        } else {
            orderService.deleteOrder(order);
            return new HashMap<>() {{
                put("info", "删除订单成功");
                put("result", true);
            }};
        }
    }
}
