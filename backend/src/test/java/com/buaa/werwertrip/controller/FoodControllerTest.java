package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.*;
import com.buaa.werwertrip.service.*;
import com.buaa.werwertrip.service.Impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FoodControllerTest {

    @Mock
    private IFoodService foodService;

    @Mock
    private IOrderService orderService;

    @Mock
    private ITrainService trainService;

    @Mock
    private IMessageService messageService;

    @Mock
    private EmailService emailService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private FoodController foodController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 正面测试用例：用户有车票并且在用餐时间段内
    @Test
    void testGetAllFood_WhenUserHasTicket_ShouldReturnFoodList() {
        // Arrange
        String tid = "T1001";
        String userID = "user123";
        String date = "2024-08-20";
        String time = "lunch";

        TrainOrder mockTrainOrder = new TrainOrder();
        mockTrainOrder.setOid("O12345");

        when(trainService.getTrainOrderByTrainAndIdentification(tid, date, userID))
                .thenReturn(Collections.singletonList(mockTrainOrder));

        Order mockOrder = new Order();
        mockOrder.setOrderStatus(Order.OrderStatus.Paid);

        when(orderService.getOrder(mockTrainOrder.getOid())).thenReturn(mockOrder);

        Train mockTrain = new Train();
        mockTrain.setStartTime("2024-08-20 12:30:00");
        mockTrain.setArrivalTime("2024-08-20 13:30:00");

        when(trainService.getTrainByTidAndDate(tid, date)).thenReturn(mockTrain);

        Food mockFood = new Food();
        mockFood.setName("Pizza");
        mockFood.setPrice(10.0);
        mockFood.setPhoto("pizza.jpg");
        mockFood.setNum(5);

        when(foodService.getAllFood(tid, date, time)).thenReturn(Collections.singletonList(mockFood));

        // Act
        Map<String, Object> response = foodController.getAllFood(tid, userID, date, time);

        // Assert
        assertTrue((Boolean) response.get("haveTicket"));
        assertEquals("购买成功", response.get("info"));

        List<Object> result = (List<Object>) response.get("result");
        assertEquals(1, result.size());

        Map<String, Object> foodItem = (Map<String, Object>) result.get(0);
        assertEquals("Pizza", foodItem.get("name"));
        assertEquals("10.0", foodItem.get("price"));
        assertEquals("pizza.jpg", foodItem.get("photo"));
        assertEquals(5, foodItem.get("number"));
    }

    // 反面测试用例：用户没有车票
    @Test
    void testGetAllFood_WhenUserDoesNotHaveTicket_ShouldReturnNoFood() {
        // Arrange
        String tid = "T1001";
        String userID = "user123";
        String date = "2024-08-20";
        String time = "lunch";

        Train mockTrain = new Train();
        mockTrain.setStartTime("2024-08-20 12:30:00");
        mockTrain.setArrivalTime("2024-08-20 13:30:00");

        when(trainService.getTrainByTidAndDate(tid, date)).thenReturn(mockTrain);

        Food mockFood = new Food();
        mockFood.setName("Pizza");
        mockFood.setPrice(10.0);
        mockFood.setPhoto("pizza.jpg");
        mockFood.setNum(5);

        when(foodService.getAllFood(tid, date, time)).thenReturn(Collections.singletonList(mockFood));

        // Act
        Map<String, Object> response = foodController.getAllFood(tid, userID, date, time);

        // Assert
        assertFalse((Boolean) response.get("haveTicket"));
        assertEquals("没有购买当日该车次车票", response.get("info"));

        List<Object> result = (List<Object>) response.get("result");
        assertTrue(result.isEmpty());
    }

    // 反面测试用例：用户没有在用餐时间段内
    @Test
    void testGetAllFood_WhenUserNotInMealTime_ShouldReturnNoFood() {
        // Arrange
        String tid = "T1001";
        String userID = "user123";
        String date = "2024-08-20";
        String time = "lunch";

        TrainOrder mockTrainOrder = new TrainOrder();
        mockTrainOrder.setOid("O12345");

        when(trainService.getTrainOrderByTrainAndIdentification(tid, date, userID))
                .thenReturn(Collections.singletonList(mockTrainOrder));

        Order mockOrder = new Order();
        mockOrder.setOrderStatus(Order.OrderStatus.Paid);

        when(orderService.getOrder(mockTrainOrder.getOid())).thenReturn(mockOrder);

        Train mockTrain = new Train();
        mockTrain.setStartTime("2024-08-20 15:30:00"); // 不在午餐时间段内
        mockTrain.setArrivalTime("2024-08-20 16:30:00");

        when(trainService.getTrainByTidAndDate(tid, date)).thenReturn(mockTrain);

        // Act
        Map<String, Object> response = foodController.getAllFood(tid, userID, date, time);

        // Assert
        assertFalse((Boolean) response.get("haveTicket"));
        assertEquals("午餐点您不在车上哦", response.get("info"));

        List<Object> result = (List<Object>) response.get("result");
        assertTrue(result.isEmpty());
    }

    // 正面测试用例：订单提交成功
    @Test
    void testSubmitFoodOrder_ShouldReturnSuccessMessage() {
        // Arrange
        String userId = "user123";
        String trainId = "T1001";
        String mealDate = "2024-08-20";
        String mealTime = "lunch";
        double sumPrice = 50.0;

        Map<String, Object> request = new HashMap<>();
        request.put("userID", userId);
        request.put("tid", trainId);
        request.put("date", mealDate);
        request.put("time", mealTime);
        request.put("sum_price", sumPrice);

        List<Map<String, Object>> foods = new ArrayList<>();
        Map<String, Object> food1 = new HashMap<>();
        food1.put("name", "Pizza");
        food1.put("number", 2);
        foods.add(food1);
        request.put("foods", foods);

        Food mockFood = new Food();
        mockFood.setName("Pizza");
        mockFood.setPrice(10.0);
        mockFood.setPhoto("pizza.jpg");
        mockFood.setNum(5);

        when(foodService.findFoodByAllKeys(trainId, mealDate, mealTime, "Pizza")).thenReturn(mockFood);
        when(userService.getEmail(userId)).thenReturn("2653941526@qq.com");

        // Act
        Map<String, Object> response = foodController.submitFoodOrder(request);

        // Assert
        assertTrue((Boolean) response.get("result"));
        assertEquals("下单成功！", response.get("info"));

        verify(orderService, times(1)).addOrder(any(Order.class));
        verify(foodService, times(1)).addFoodOrder(any(FoodOrder.class));
        verify(messageService, times(1)).addMessage(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyString());
        verify(emailService, times(1)).sendSimpleMail(anyString(), anyString(), anyString());
    }

    // 反面测试用例：订单提交失败（找不到指定的食品）
    @Test
    void testSubmitFoodOrder_WhenFoodNotFound_ShouldReturnFailureMessage() {
        // Arrange
        String userId = "user123";
        String trainId = "T1001";
        String mealDate = "2024-08-20";
        String mealTime = "lunch";
        double sumPrice = 50.0;

        Map<String, Object> request = new HashMap<>();
        request.put("userID", userId);
        request.put("tid", trainId);
        request.put("date", mealDate);
        request.put("time", mealTime);
        request.put("sum_price", sumPrice);

        List<Map<String, Object>> foods = new ArrayList<>();
        Map<String, Object> food1 = new HashMap<>();
        food1.put("name", "Pizza");
        food1.put("number", 2);
        foods.add(food1);
        request.put("foods", foods);

        when(foodService.findFoodByAllKeys(trainId, mealDate, mealTime, "Pizza")).thenReturn(null); // 食品找不到

        // Act
        Map<String, Object> response = foodController.submitFoodOrder(request);

        // Assert
        assertNull(response); // 返回值为null表示失败
    }

    // 正面测试用例：获取用户订单
    @Test
    void testGetOrders_ShouldReturnOrderList() {
        // Arrange
        String userID = "user123";
        String status = "paid";

        Order mockOrder = new Order();
        mockOrder.setOid("O12345");
        mockOrder.setBillTime("2024-08-20 12:00:00");
        mockOrder.setTotal(50.0);
        mockOrder.setOrderStatus(Order.OrderStatus.Paid);

        when(orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Paid, Order.OrderType.Food))
                .thenReturn(Collections.singletonList(mockOrder));

        FoodOrder mockFoodOrder = new FoodOrder();
        mockFoodOrder.setTrainId("T1001");
        mockFoodOrder.setMealDate("2024-08-20");
        mockFoodOrder.setMealTime("lunch");
        mockFoodOrder.setFoodName("Pizza");
        mockFoodOrder.setCount(2);
        mockFoodOrder.setPhoto("pizza.jpg");

        when(foodService.getFoodOrdersByOid(mockOrder.getOid())).thenReturn(Collections.singletonList(mockFoodOrder));

        // Act
        Map<String, Object> response = foodController.getOrders(userID, status);

        // Assert
        List<Object> result = (List<Object>) response.get("result");
        assertEquals(1, result.size());

        Map<String, Object> orderMap = (Map<String, Object>) result.get(0);
        assertEquals("O12345", orderMap.get("oid"));
        assertEquals("T1001", orderMap.get("tid"));
        assertEquals("2024-08-20 12:00:00", orderMap.get("order_time"));
        assertEquals("午餐", orderMap.get("time"));
        assertEquals("2024-08-20", orderMap.get("date"));
        assertEquals(50.0, orderMap.get("sum_price"));
        assertEquals("已支付", orderMap.get("status"));

        List<Object> foods = (List<Object>) orderMap.get("foods");
        assertEquals(1, foods.size());

        Map<String, Object> foodMap = (Map<String, Object>) foods.get(0);
        assertEquals("Pizza", foodMap.get("food_name"));
        assertEquals(2, foodMap.get("count"));
        assertEquals("pizza.jpg", foodMap.get("photo"));
    }

    // 反面测试用例：用户没有符合条件的订单
    @Test
    void testGetOrders_WhenNoOrdersFound_ShouldReturnEmptyList() {
        // Arrange
        String userID = "user123";
        String status = "paid";

        when(orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Paid, Order.OrderType.Food))
                .thenReturn(Collections.emptyList());

        // Act
        Map<String, Object> response = foodController.getOrders(userID, status);

        // Assert
        List<Object> result = (List<Object>) response.get("result");
        assertTrue(result.isEmpty());
    }

    // 正面测试用例：成功取消订单
    @Test
    void testCancelOrder_ShouldReturnSuccessMessage() {
        // Arrange
        String userID = "user123";
        String oid = "O12345";

        Order mockOrder = new Order();
        mockOrder.setOid(oid);
        mockOrder.setOrderStatus(Order.OrderStatus.Paid);

        when(orderService.getOrderByOidAndUid(oid, userID)).thenReturn(mockOrder);

        FoodOrder mockFoodOrder = new FoodOrder();
        mockFoodOrder.setTrainId("T1001");
        mockFoodOrder.setMealDate("2024-08-20");
        mockFoodOrder.setMealTime("dinner");

        when(foodService.getFoodOrdersByOid(oid)).thenReturn(Collections.singletonList(mockFoodOrder));
        when(userService.getEmail(userID)).thenReturn("2653941526@qq.com");

        // Act
        Map<String, Object> response = foodController.cancelOrder(userID, oid);

        // Assert
        assertTrue((Boolean) response.get("result"));
        assertEquals("取消订单成功", response.get("info"));

        verify(orderService, times(1)).cancelOrder(mockOrder);
        verify(messageService, times(1)).addMessage(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyString());
        verify(emailService, times(1)).sendSimpleMail(anyString(), anyString(), anyString());
    }

    // 反面测试用例：取消订单失败（找不到订单）
    @Test
    void testCancelOrder_WhenOrderNotFound_ShouldReturnFailureMessage() {
        // Arrange
        String userID = "user123";
        String oid = "O12345";

        when(orderService.getOrderByOidAndUid(oid, userID)).thenReturn(null); // 订单不存在

        // Act
        Map<String, Object> response = foodController.cancelOrder(userID, oid);

        // Assert
        assertFalse((Boolean) response.get("result"));
        assertEquals("取消订单失败", response.get("info"));
    }

    // 正面测试用例：成功删除订单
    @Test
    void testDeleteOrder_ShouldReturnSuccessMessage() {
        // Arrange
        String userID = "user123";
        String oid = "O12345";

        Order mockOrder = new Order();
        mockOrder.setOid(oid);

        when(orderService.getOrderByOidAndUid(oid, userID)).thenReturn(mockOrder);

        // Act
        Map<String, Object> response = foodController.deleteOrder(userID, oid);

        // Assert
        assertTrue((Boolean) response.get("result"));
        assertEquals("删除订单成功", response.get("info"));

        verify(orderService, times(1)).deleteOrder(mockOrder);
    }

    // 反面测试用例：删除订单失败（找不到订单）
    @Test
    void testDeleteOrder_WhenOrderNotFound_ShouldReturnFailureMessage() {
        // Arrange
        String userID = "user123";
        String oid = "O12345";

        when(orderService.getOrderByOidAndUid(oid, userID)).thenReturn(null); // 订单不存在

        // Act
        Map<String, Object> response = foodController.deleteOrder(userID, oid);

        // Assert
        assertFalse((Boolean) response.get("result"));
        assertEquals("删除订单失败", response.get("info"));
    }
}
