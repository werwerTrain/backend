package com.buaa.werwertrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buaa.werwertrip.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

// 消息
@Mapper
public interface IMessageMapper extends BaseMapper<Message> {
    @Insert("insert into messages(uid,mid,orderId,title,messageTime,content,haveRead,orderType)" +
            "values(#{userId},#{mid},#{orderId},#{title},#{messageTime},#{content},#{haveRead},#{orderType})")
    void addMessage(String userId, String mid, String orderId, String title, String messageTime, String content, Boolean haveRead, String orderType);

    @Select("select * from messages where uid=#{userId} order by messageTime desc")
    List<Map<String, Object>> getMessage(String userId);

    @Update(("update messages set haveRead = true where mid=#{mid}"))
    void setHaveread(String mid);



}
