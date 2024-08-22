package com.buaa.werwertrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buaa.werwertrip.entity.Stopover;
import com.buaa.werwertrip.entity.Train;
import com.buaa.werwertrip.entity.TrainOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ITrainMapper extends BaseMapper<Train> {

    List<Train> searchTrain(String start_station, String arrive_station, String date, Integer is_GD, Integer sort_type, List<Boolean> seat_type);

    @Select("select * from trains where (trainId, date) = (#{tid}, #{date})")
    Train getTrainByTidAndDate(String tid, String date);

    List<Stopover> searchStopover(String trainId);

    @Select("select * from trainorders where oid = #{oid}")
    List<TrainOrder> getTrainOrdersByOid(String oid);

    @Select("select * from trainorders where trainId = #{trainId} and identification = #{identification} and trainDate = #{date}")
    List<TrainOrder> getTrainOrderByTrainAndIdentification(String trainId, String date, String identification);

    void addTrainOrderDetail(String oid, String trainId, String trainDate, String name, String identification, String seatType);

    Map<String, Object> getSelfOrderDetail(String oid, String userID);

    Map<String, Object> getTrainByIdAndDate(String trainId, String trainDate);

    Map<String, Object> getStartTime(String trainId, String trainDate);

    List<Map<String, Object>> getTrainIdAndDate(String orderId);

    void updateTrainSeat(String trainId, String trainDate, Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6);

    Boolean getTrainState(String trainId, String trainDate);
}
