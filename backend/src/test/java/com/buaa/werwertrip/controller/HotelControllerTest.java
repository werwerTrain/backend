package com.buaa.werwertrip.controller;
import com.buaa.werwertrip.controller.HotelController;
import com.buaa.werwertrip.entity.Hotel;
import com.buaa.werwertrip.entity.Order;
import com.buaa.werwertrip.service.IHotelService;
import com.buaa.werwertrip.service.IMessageService;
import com.buaa.werwertrip.service.IOrderService;
import com.buaa.werwertrip.service.IUserService;
import com.buaa.werwertrip.service.Impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HotelControllerTest {

    @Mock
    private IHotelService hotelService;

    @Mock
    private IOrderService orderService;

    @Mock
    private IMessageService messageService;

    @Mock
    private IUserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private HotelController hotelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 正例：测试酒店查询成功
    @Test
    void testHotelQuery_Success() {
        List<Map<String, Object>> hotelList = new ArrayList<>();
        Map<String, Object> hotelInfo = new HashMap<>();
        hotelInfo.put("id", "1");
        hotelInfo.put("name", "Test Hotel");
        hotelInfo.put("rank", 4.5);
        hotelInfo.put("miniprice", "100.00");
        hotelInfo.put("MIN(num)", 10);
        hotelInfo.put("MIN(price)", 100.0);
        //when(hotelService.getAvailableRoom(anyString(), anyString(), anyString())).thenReturn(Collections.singletonList(hotelInfo));

        hotelList.add(hotelInfo);

        when(hotelService.searchHotelByCity(anyString())).thenReturn(Collections.singletonList("1"));
        when(hotelService.getAvailableRoom(anyString(), anyString(), anyString())).thenReturn(Collections.singletonList(hotelInfo));
        when(hotelService.getHotelInfo(anyString())).thenReturn(new Hotel());
        when(hotelService.getHotelRank(anyString())).thenReturn(4.5);

        Map<String, Object> response = hotelController.hotelQuery("test_station", "2024-08-20", "2024-08-22", "lowprice");

        assertNotNull(response);
        assertFalse(((List<?>) response.get("result")).isEmpty());
    }

    // 反例：测试酒店查询无结果
    @Test
    void testHotelQuery_NoResult() {
        when(hotelService.searchHotelByCity(anyString())).thenReturn(Collections.emptyList());

        Map<String, Object> response = hotelController.hotelQuery("invalid_station", "2024-08-20", "2024-08-22", "lowprice");

        assertNotNull(response);
        assertTrue(((List<?>) response.get("result")).isEmpty());
    }

    // 正例：测试获取酒店详情成功
    @Test
    void testGetHotelDetail_Success() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");

        when(hotelService.getRoomDetail(anyString(), anyString(), anyString())).thenReturn(Collections.emptyList());
        when(hotelService.getHotelInfo(anyString())).thenReturn(hotel);
        when(hotelService.getCommentNum(anyString())).thenReturn(10);
        when(hotelService.getHotelRank(anyString())).thenReturn(4.5);

        Map<String, Object> response = hotelController.getHotelDetail("1", true, true, true, "2024-08-20", "2024-08-22");

        assertNotNull(response);
        assertEquals("Test Hotel", response.get("name"));
    }

    // 反例：测试获取酒店详情失败
    @Test
    void testGetHotelDetail_Failure() {
        when(hotelService.getHotelInfo(anyString())).thenReturn(null);
        String hotelId = "testHotelId";

        // Mock Hotel info
        Hotel mockHotel = new Hotel();
        mockHotel.setPhone("123456789");
        mockHotel.setSetTime("2024-08-21 14:00:00");
        mockHotel.setDescription("A nice hotel.");
        mockHotel.setBreakfastDescription("Continental breakfast included.");
        mockHotel.setAgeNotion("No age restriction.");
        mockHotel.setCheckinTime("14:00");
        mockHotel.setCheckoutTime("12:00");
        mockHotel.setName("Test Hotel");
        mockHotel.setStars(5);
        mockHotel.setLikes(100);
        mockHotel.setPosition("Downtown");

        when(hotelService.getHotelInfo(hotelId)).thenReturn(mockHotel);

        Map<String, Object> response = hotelController.getHotelDetail("invalid_id", true, true, true, "2024-08-20", "2024-08-22");

        assertNull(response.get("name"));
    }

    // 正例：测试提交酒店订单成功
    @Test
    void testSubmitHotelOrder_Success() {
        doNothing().when(orderService).addOrder(any(Order.class));
        when(hotelService.getHotelName(anyString())).thenReturn(Map.of("name", "Test Hotel"));

        Map<String, Object> request = new HashMap<>();
        request.put("hotel_id", "1");
        request.put("id", "user123");
        request.put("checkin_time", "2024-08-20");
        request.put("checkout_time", "2024-08-22");
        request.put("room_num", 1);
        request.put("room_type", 1);
        request.put("money", 100.00);
        request.put("customers", Collections.singletonList(Map.of("name", "John Doe", "id", "1234567890")));

        Map<String, Object> response = hotelController.submitHotelOrder(request);

        assertTrue((Boolean) response.get("result"));
        assertEquals("下单成功", response.get("message"));
    }

    // 反例：测试提交酒店订单失败
    @Test
    void testSubmitHotelOrder_Failure() {
        doThrow(new RuntimeException("Exception message")).when(orderService).addOrder(any(Order.class));

        Map<String, Object> request = new HashMap<>();
        request.put("hotel_id", "1");
        request.put("id", "user123");
        request.put("checkin_time", "2024-08-20");
        request.put("checkout_time", "2024-08-22");
        request.put("room_num", 1);
        request.put("room_type", 1);
        request.put("money", 100.00);
        request.put("customers", Collections.singletonList(Map.of("name", "John Doe", "id", "1234567890")));

        assertThrows(RuntimeException.class, () -> hotelController.submitHotelOrder(request));
    }

    // 正例：测试获取酒店订单成功
    @Test
    void testGetHotelOrders_Success() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", "user123", "2024-08-20 10:00:00", 100.00, Order.OrderStatus.Paid, Order.OrderType.Hotel));

        when(orderService.getOrdersByUidAndStatus(anyString(), eq(Order.OrderStatus.Paid), eq(Order.OrderType.Hotel)))
                .thenReturn(orders);
        when(hotelService.getHotelOrderDetail(anyString())).thenReturn(Collections.singletonList(Map.of("id", "1", "checkinTime", "2024-08-20", "checkoutTime", "2024-08-22", "roomNum", 1, "roomType", "标准双人间")));
        when(hotelService.getHotelName(anyString())).thenReturn(Map.of("name", "Test Hotel", "position", "Test City"));

        Map<String, Object> response = hotelController.getHotelOrders("user123", "paid");

        assertNotNull(response);
        assertFalse(((List<?>) response.get("result")).isEmpty());
    }

    // 反例：测试获取酒店订单无结果
    @Test
    void testGetHotelOrders_NoResult() {
        when(orderService.getOrdersByUidAndStatus(anyString(), eq(Order.OrderStatus.Paid), eq(Order.OrderType.Hotel)))
                .thenReturn(Collections.emptyList());

        Map<String, Object> response = hotelController.getHotelOrders("user123", "paid");

        assertNotNull(response);
        assertTrue(((List<?>) response.get("result")).isEmpty());
    }

    // 正例：测试取消酒店订单成功
    @Test
    void testCancelHotelOrder_Success() {
        Order order = new Order("1", "user123", "2024-08-20 10:00:00", 100.00, Order.OrderStatus.Paid, Order.OrderType.Hotel);
        when(orderService.getOrderByOidAndUid(anyString(), anyString())).thenReturn(order);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = formatter.format(new Date());

        List<Map<String, Object>> hotelOrderDetail = new ArrayList<>();
        Map<String, Object> mockHotelMap = new HashMap<>();
        mockHotelMap.put("id", "hotelId");
        mockHotelMap.put("checkinTime", "2024-08-21 14:00:00");
        mockHotelMap.put("checkoutTime", "2024-08-22 12:00:00");
        mockHotelMap.put("roomNum", 1);
        hotelOrderDetail.add(mockHotelMap);

        when(hotelService.getHotelOrderDetail(anyString())).thenReturn(hotelOrderDetail);

        Map<String, Object> response = hotelController.cancelHotelOrder("user123", "1");

        assertTrue((Boolean) response.get("result"));
        assertEquals(currentDate, response.get("cancel_time"));
    }

    // 反例：测试取消酒店订单失败
    @Test
    void testCancelHotelOrder_Failure() {
        when(orderService.getOrderByOidAndUid(anyString(), anyString())).thenReturn(null);

        Map<String, Object> response = hotelController.cancelHotelOrder("user123", "invalid_oid");

        assertEquals("订单不存在", response.get("info"));
    }
}
