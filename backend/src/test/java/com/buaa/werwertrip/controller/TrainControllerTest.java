package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.*;
import com.buaa.werwertrip.service.*;
import com.buaa.werwertrip.service.Impl.EmailService;
import com.buaa.werwertrip.controller.TrainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainControllerTest {

    @Mock
    private ITrainService trainService;

    @Mock
    private IOrderService orderService;

    @Mock
    private IMessageService messageService;

    @Mock
    private EmailService emailService;

    @Mock
    private IUserService userService;

    @Mock
    private IFoodService foodService;

    @InjectMocks
    private TrainController trainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTrainQuery() {
        // Arrange
        String startCity = "Beijing";
        String arriveCity = "Shanghai";
        String date = "2024-08-21";
        String userID = "user123";
        List<Boolean> seatTypes = Arrays.asList(true, true, true, true, true, true);
        boolean isHide = false;

        Train mockTrain1 = new Train(), mockTrain2 = new Train();

        mockTrain1.setTrainId("T123");
        mockTrain1.setStartStation("Beijing");
        mockTrain1.setArrivalStation("Shanghai");
        mockTrain1.setStartTime("2024-08-21T08:00");
        mockTrain1.setArrivalTime("2024-08-21T12:00");
        mockTrain1.setDuration(Time.valueOf("4:00:00"));
        mockTrain1.setBusinessSeatSurplus(10);
        mockTrain1.setFirstClassSeatSurplus(20);
        mockTrain1.setSecondClassSeatSurplus(30);
        mockTrain1.setSoftSleeperSurplus(40);
        mockTrain1.setHardSleeperSurplus(50);
        mockTrain1.setHardSeatSurplus(60);
        mockTrain2.setTrainId("T124");
        mockTrain2.setStartStation("Beijing");
        mockTrain2.setArrivalStation("Shanghai");
        mockTrain2.setStartTime("2024-08-21T09:00");
        mockTrain2.setArrivalTime("2024-08-21T13:00");
        mockTrain2.setDuration(Time.valueOf("4:00:00"));
        mockTrain2.setBusinessSeatSurplus(0);
        mockTrain2.setFirstClassSeatSurplus(0);
        mockTrain2.setSecondClassSeatSurplus(0);
        mockTrain2.setSoftSleeperSurplus(0);
        mockTrain2.setHardSleeperSurplus(0);
        mockTrain2.setHardSeatSurplus(0);


        List<Train> mockTrains = Arrays.asList(
                mockTrain1, mockTrain2
        );

        when(trainService.searchTrain(startCity, arriveCity, date, 2, 1, seatTypes)).thenReturn(mockTrains);

        // Act
        Map<String, Object> response = trainController.trainQuery(startCity, arriveCity, date, userID, 2, 1, seatTypes, isHide);

        // Assert
        List<Map<String, Object>> result = (List<Map<String, Object>>) response.get("result");
        assertEquals(1, result.size());  // Only one train with available seats

        Map<String, Object> trainInfo = result.get(0);
        assertEquals("T123", trainInfo.get("tid"));
        assertEquals("Beijing", trainInfo.get("start_station"));
        assertEquals("Shanghai", trainInfo.get("arrive_station"));
        assertEquals("04:00:00", trainInfo.get("time"));
        assertEquals(10, ((Map<String, Object>) trainInfo.get("business")).get("remain"));
    }

    @Test
    void testSubmitTrainOrder_Success() {
        // Arrange
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("userID", "user123");
        orderRequest.put("tid", "T123");
        orderRequest.put("date", "2024-08-21");
        orderRequest.put("sum_price", "100.0");

        List<Map<String, String>> persons = Arrays.asList(
                new HashMap<String, String>() {{
                    put("name", "John Doe");
                    put("identification", "ID123");
                    put("seat_type", "商务座");
                }},
                new HashMap<String, String>() {{
                    put("name", "Jane Doe");
                    put("identification", "ID124");
                    put("seat_type", "一等座");
                }}
        );

        orderRequest.put("person", persons);

        when(trainService.getTrainOrderByTrainAndIdentification("T123", "2024-08-21", "ID123")).thenReturn(Collections.emptyList());
        when(trainService.getTrainOrderByTrainAndIdentification("T123", "2024-08-21", "ID124")).thenReturn(Collections.emptyList());
        doNothing().when(orderService).addOrder(any(Order.class));
        doNothing().when(trainService).updateTrainSeat(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        when(trainService.getTrainByIdAndDate("T123", "2024-08-21")).thenReturn(Map.of(
                "startStation", "Beijing",
                "arrivalStation", "Shanghai",
                "startTime", LocalDateTime.now(),
                "arrivalTime", LocalDateTime.now().plusHours(4),
                "duration", "4h"
        ));
        when(userService.getEmail(any(String.class))).thenReturn("2653941526@qq.com");

        // Act
        Map<String, Object> response = trainController.submitTrainOrder(orderRequest);

        // Assert
        assertEquals("下单成功！", response.get("info"));
        verify(emailService, times(1)).sendSimpleMail(anyString(), anyString(), anyString());
    }

    @Test
    void testCancelTrainOrder_Success() {
        // Arrange
        String userID = "user123";
        String oid = "OID123";

        Order mockOrder = new Order(oid, userID, "2024-08-21 12:00:00", 100.0, Order.OrderStatus.Paid, Order.OrderType.Train);
        Train mockTrain1 = new Train(), mockTrain2 = new Train();
        List<TrainOrder> mockTrainOrders = Arrays.asList(
                new TrainOrder("12345", "T123", "2024-08-21", "John Doe", "ID123", "商务座"),
                new TrainOrder("54321", "T123", "2024-08-21", "Jane Doe", "ID124", "一等座")
        );

        when(orderService.getOrderByOidAndUid(oid, userID)).thenReturn(mockOrder);
        when(trainService.getTrainOrdersByOid(oid)).thenReturn(mockTrainOrders);
        when(trainService.getTrainByIdAndDate("T123", "2024-08-21")).thenReturn(Map.of(
                "startStation", "Beijing",
                "arrivalStation", "Shanghai",
                "startTime", LocalDateTime.now(),
                "arrivalTime", LocalDateTime.now().plusHours(4),
                "duration", "4h"
        ));
        when(userService.getEmail(any(String.class))).thenReturn("2653941526@qq.com");

        // Act
        Map<String, Object> response = trainController.cancelTrainOrder(userID, oid);

        // Assert
        assertEquals("取消成功", response.get("info"));
        assertTrue((Boolean) response.get("result"));
        verify(emailService, times(1)).sendSimpleMail(anyString(), anyString(), anyString());
    }

    @Test
    void testGetOrders() {
        // Arrange
        String userID = "user123";
        String status = "paid";
        List<Order> mockOrders = Arrays.asList(
                new Order("OID123", userID, "2024-08-21 12:00:00", 100.0, Order.OrderStatus.Paid, Order.OrderType.Train)
        );

        when(orderService.getOrdersByUidAndStatus(userID, Order.OrderStatus.Paid, Order.OrderType.Train)).thenReturn(mockOrders);
        when(trainService.getTrainOrdersByOid("OID123")).thenReturn(Collections.singletonList(
                new TrainOrder("12345", "T123", "2024-08-21", "John Doe", "ID123", "商务座")
        ));
        Train mockTrain1 = new Train();

        mockTrain1.setTrainId("T123");
        mockTrain1.setStartStation("Beijing");
        mockTrain1.setArrivalStation("Shanghai");
        mockTrain1.setStartTime("2024-08-21T08:00");
        mockTrain1.setArrivalTime("2024-08-21T12:00");
        mockTrain1.setDuration(Time.valueOf("4:00:00"));
        mockTrain1.setBusinessSeatSurplus(10);
        mockTrain1.setFirstClassSeatSurplus(20);
        mockTrain1.setSecondClassSeatSurplus(30);
        mockTrain1.setSoftSleeperSurplus(40);
        mockTrain1.setHardSleeperSurplus(50);
        mockTrain1.setHardSeatSurplus(60);
        when(trainService.getTrainByTidAndDate("T123", "2024-08-21")).thenReturn(mockTrain1);

        // Act
        Map<String, Object> response = trainController.getOrders(userID, status);

        // Assert
        List<Map<String, Object>> result = (List<Map<String, Object>>) response.get("result");
        assertEquals(1, result.size());
        assertEquals("T123", result.get(0).get("tid"));
    }

    @Test
    void testGetSelfOrder() {
        // Arrange
        String userID = "user123";
        String status = "all";
        List<Order> mockOrders = Arrays.asList(
                new Order("OID123", userID, "2024-08-21 12:00:00", 100.0, Order.OrderStatus.Paid, Order.OrderType.Train)
        );

        when(orderService.getOrderByUid(userID, Order.OrderType.Train)).thenReturn(mockOrders);
        when(trainService.getSelfOrderDetail("OID123", userID)).thenReturn(Map.of(
                "trainId", "T123",
                "trainDate", "2024-08-21",
                "seatType", "商务座"
        ));
        when(trainService.getTrainByIdAndDate("T123", "2024-08-21")).thenReturn(Map.of(
                "startStation", "Beijing",
                "arrivalStation", "Shanghai",
                "startTime", LocalDateTime.now(),
                "arrivalTime", LocalDateTime.now().plusHours(4),
                "duration", "4h"
        ));

        // Act
        Map<String, Object> response = trainController.getSelfOrder(userID, status);

        // Assert
        List<Map<String, Object>> result = (List<Map<String, Object>>) response.get("result");
        assertEquals(1, result.size());
        assertEquals("T123", result.get(0).get("tid"));
    }
}
