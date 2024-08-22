package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.Order;
import com.buaa.werwertrip.service.*;
import com.buaa.werwertrip.service.Impl.EmailService;
import com.buaa.werwertrip.entity.Message;
import com.buaa.werwertrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@Component
public class Reminder {
    @Autowired
    private ITrainService trainService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IFoodService foodService;
    @Autowired
    private IHotelService hotelService;

    @Scheduled(fixedRate = 60000) // 每隔1分钟执行一次
    public void remindUsers() {
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime threeHoursLater = now.plusHours(3);
        List<Order> orderMap = orderService.getAllTrain();
        for (Order order : orderMap) {
            if (order.getOrderType() == Order.OrderType.Train) {
                String userId = order.getUid();
                String orderId = order.getOid();
                String email = userService.getEmail(userId);
                List<Map<String, Object>> trainMapList = trainService.getTrainIdAndDate(orderId);
                //System.out.println(trainMapList);
                if (!trainMapList.isEmpty()) {
                    Map<String, Object> trainMap = trainMapList.get(0);
//                    System.out.println(trainMap);

                    String trainId = trainMap.get("trainId").toString();
                    String trainDate = trainMap.get("trainDate").toString();
//                    System.out.println(trainId);
//                    System.out.println(trainDate);

                    //LocalDateTime startTime = (LocalDateTime) trainMap.get("startTime");
                    // String formattedStartTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime startTime =(LocalDateTime) trainService.getStartTime(trainId, trainDate).get("startTime");
                    LocalDateTime arrivalTime =(LocalDateTime) trainService.getStartTime(trainId, trainDate).get("arrivalTime");

                    String formattedStartTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String formattedArrivaleTime = arrivalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

//                    System.out.println(trainService.getTrainState(trainId, trainDate));
                    if (!trainService.getTrainState(trainId, trainDate)) // 车次存在
                    {
//                        System.out.println(startTime);
                        LocalDateTime start = LocalDateTime.parse(formattedStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        LocalDateTime arrive = LocalDateTime.parse(formattedArrivaleTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                        System.out.println(now);
                        Duration duration = Duration.between(now, start);
                        Duration finish = Duration.between(now, arrive);

                        if (duration.isPositive() && duration.toHours() < 3 && !orderService.getMessageSend(orderId)) {
                            // 距发车时间小于3h且未发送过消息
                            String content = "【WerwerTrip】距离您预定的" + trainDate + " " + trainId + "车次的" + "列车发车时间已不足3小时，请您合理安排出行时间，以免错过列车。";
//                            System.out.println(content);
                            String Mcontent = "距离您预定的" + trainDate + " " + trainId + "车次的" + "列车发车时间已不足3小时，请您合理安排出行时间，以免错过列车。";
                            messageService.addMessage(userId, Message.generateMessageId(), orderId, "行程提醒", now.toString(), Mcontent, false, "2");
                            emailService.sendSimpleMail(email, "行程提醒", content);
                            orderService.setMessageHaveSend(orderId);
                        }
                        if (finish.isNegative()) {
                            orderService.finishOrder(order.getOid());
                            // 完成该trainOrder对应的foodOrder
                            foodService.getTrainRelatedFoodOrders(trainId, trainDate, userId).forEach(e-> {
                                orderService.finishOrder(orderService.getOrder(e.getOid()).getOid());
                                //orderService.cancelOrder(e);
                            });
//                            foodService.getFoodOrdersByTrain(trainId, trainDate).forEach(foodOrder -> {
//                                orderService.finishOrder(orderService.getOrder(foodOrder.getOid()).getOid());
//                            });
                        }
                    }
                    // 车次取消
                    else {
//                        System.out.println("车次已取消");
                        orderService.cancelOrder(order);
                        // 取消该trainOrder对应的foodOrder
                        foodService.getTrainRelatedFoodOrders(trainId, trainDate, userId).forEach(e-> {
                            orderService.cancelOrder(e);
                        });
                        // 距发车时间小于3h且未发送过消息
                        String content = "【WerwerTrip】非常抱歉，因铁路部门安排调整，您购买的" + trainDate + " " + trainId + "车次的" + "列车已取消，订单将自动取消并原路退回钱款";
                        //System.out.println(content);
                        String Mcontent = "非常抱歉，因铁路部门安排调整，您购买的" + trainDate + " " + trainId + "车次的" + "列车已取消，订单将自动取消并原路退回钱款";
                        messageService.addMessage(userId, Message.generateMessageId(), orderId, "车次取消", now.toString(), Mcontent, false, "2");
                        emailService.sendSimpleMail(email, "车次取消", content);
                        orderService.setMessageHaveSend(orderId);
                    }
                }
                // 有order无trainorders
                else {
                    System.out.println(orderId);
                    System.out.println("获取订单详细信息失败");
                }
            } else if (order.getOrderType() == Order.OrderType.Hotel) {
                List<Map<String, Object>> hotelOrderDetail = hotelService.getHotelOrderDetail(order.getOid());
                if (hotelOrderDetail.isEmpty()) {
                    System.out.println("获取酒店订单详情失败");
                } else {
                    String finishTime = hotelOrderDetail.get(0).get("checkoutTime").toString();
                    LocalDate finishDate =  LocalDate.parse(finishTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    //System.out.println(now);
                    LocalDateTime finish = finishDate.atStartOfDay(); // 将日期转换为当天的开始时间
                    Duration duration = Duration.between(now, finish);
                    if (duration.isNegative()) {
                        orderService.finishOrder(order.getOid());
                    }
                }

            }


        }

    }

}



