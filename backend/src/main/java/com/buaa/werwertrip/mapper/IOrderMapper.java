package com.buaa.werwertrip.mapper;

import com.buaa.werwertrip.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IOrderMapper {
    @Select("select * from orders where uid = #{uid} and orderStatus = #{status} and orderType = #{type} order by billTime desc")
    List<Order> getOrdersByUidAndStatus(String uid, Order.OrderStatus status, Order.OrderType type);

    @Select("select * from orders where uid = #{uid} and orderType = #{type} order by billTime desc")
    List<Order> getOrderByUid(String uid, Order.OrderType type);

    @Select("select * from orders where oid = #{oid} and uid = #{uid}")
    Order getOrderByOidAndUid(String oid, String uid);

    @Select("select * from orders where oid = #{oid}")
    Order getOrder(String oid);

    @Insert("insert into orders (oid, uid, billTime, total, orderStatus, orderType)" +
            "        values" +
            "(#{oid}, #{uid}, #{billTime}, #{total}, #{orderStatus}, #{orderType})")
    void addOrder(Order order);

    @Update("update orders set orderStatus = 'Canceled', cancelTime = NOW() where oid = #{oid}")
    void cancelOrder(Order order);

    @Delete("delete from orders where oid = #{oid}")
    void deleteOrder(Order order);

    @Update("update orders set cancelTime = #{cancelTime} where oid=#{oid}")
    void setCancelTime(String oid, String cancelTime);

    @Select("select * from orders where uid=#{userID} and orderType='Train'")
    List<Order> getIdByUid(String userID);

    @Select("select * from orders where uid=#{userID} and orderStatus=#{status} and orderType='Train'")
    List<Order> getIdByUidAndStatus(String userID, String status);

    @Select("select * from orders where orderStatus='Paid'")
    List<Order> getAllTrainOrder();

    @Select("select haveSend from orders where oid=#{orderId}")
    Boolean getMessageSend(String orderId);

    @Update("update orders set haveSend = true where oid = #{orderId}")
    void setMessageHaveSend(String orderId);

    @Update("update orders set orderStatus = 'Done' where oid = #{oid}")
    void finishOrder(String oid);
}
