package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.Stopover;
import com.buaa.werwertrip.entity.Train;
import com.buaa.werwertrip.entity.TrainOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ITrainService {
    public List<Train> searchTrain(String start_station, String arrive_station, String date, Integer isGD, Integer sort_type, List<Boolean> seat_type);

    public Train getTrainByTidAndDate(String tid, String date);

    public List<Stopover> searchStopover(String trainId);

    public List<TrainOrder> getTrainOrdersByOid(String oid);

    public List<TrainOrder> getTrainOrderByTrainAndIdentification(String trainId, String date, String identification);

    public void addTrainOrderDetail(String oid, String trainId, String trainDate, String name, String identification, String seatType);

    Map<String, Object> getSelfOrderDetail(String oid, String userID);

    Map<String, Object> getTrainByIdAndDate(String trainId, String trainDate);

    Map<String,Object> getStartTime(String trainId, String trainDate);

    List<Map<String, Object>> getTrainIdAndDate(String orderId);

    void updateTrainSeat(String trainId, String trainDate, Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6);

Boolean getTrainState(String trainId, String trainDate);
}
