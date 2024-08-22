package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.entity.Stopover;
import com.buaa.werwertrip.entity.TrainOrder;
import com.buaa.werwertrip.entity.Train;
import com.buaa.werwertrip.mapper.ITrainMapper;
import com.buaa.werwertrip.service.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("trainService")
public class TrainServiceImpl implements ITrainService {
    @Autowired
    private ITrainMapper trainMapper;

    @Override
    public List<Train> searchTrain(String start_station, String arrive_station, String date, Integer isGD, Integer sort_type, List<Boolean> seat_type) {
        return trainMapper.searchTrain(start_station, arrive_station, date, isGD, sort_type, seat_type);
    }

    @Override
    public Train getTrainByTidAndDate(String tid, String date) {
        return trainMapper.getTrainByTidAndDate(tid, date);
    }

    @Override
    public List<Stopover> searchStopover(String trainId) {
        return trainMapper.searchStopover(trainId);
    }

    @Override
    public List<TrainOrder> getTrainOrdersByOid(String oid) {
        return trainMapper.getTrainOrdersByOid(oid);
    }

    @Override
    public List<TrainOrder> getTrainOrderByTrainAndIdentification(String trainId, String date, String identification) {
        return trainMapper.getTrainOrderByTrainAndIdentification(trainId, date, identification);
    }


    @Override
    public void addTrainOrderDetail(String oid, String trainId, String trainDate, String name, String identification, String seatType) {
        trainMapper.addTrainOrderDetail(oid, trainId, trainDate, name, identification, seatType);
    }

    @Override
    public Map<String, Object> getSelfOrderDetail(String oid, String userID) {
        return trainMapper.getSelfOrderDetail(oid, userID);
    }

    @Override
    public Map<String, Object> getTrainByIdAndDate(String trainId, String trainDate) {
        return trainMapper.getTrainByIdAndDate(trainId, trainDate);
    }

    @Override
    public Map<String,Object> getStartTime(String trainId, String trainDate) {
        return trainMapper.getStartTime(trainId, trainDate);
    }

    @Override
    public List<Map<String, Object>> getTrainIdAndDate(String orderId) {
        return trainMapper.getTrainIdAndDate(orderId);
    }
    @Override
    public void updateTrainSeat(String trainId, String trainDate, Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6){
        trainMapper.updateTrainSeat(trainId,trainDate,num1,num2,num3,num4,num5,num6);
    }

    @Override
    public Boolean getTrainState(String trainId,String trainDate){
        return trainMapper.getTrainState(trainId, trainDate);
    }

}
