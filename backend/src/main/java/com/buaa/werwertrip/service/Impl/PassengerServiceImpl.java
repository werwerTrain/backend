package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.entity.Passenger;
import com.buaa.werwertrip.mapper.IPassengerMapper;
import com.buaa.werwertrip.service.IPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("passengerService")
public class PassengerServiceImpl implements IPassengerService {

    @Autowired
    private IPassengerMapper passengerMapper;

    @Override
    public Integer addPassenger(String name, String identification, String phone, String userId) {
        //ArrayList<Map<String,String>>
        return passengerMapper.addPassenger(name, identification, phone, userId);
    }

//    @Override
//    public boolean updatePassengerName(String id, String oldidentification, String newname) {
//        return passengerMapper.updatePassengerName(id, oldidentification, newname);
//    }
//
//    @Override
//    public boolean updatePassengerIdentification(String id, String oldidentification, String newidentification) {
//        return passengerMapper.updatePassengerIdentification(id, oldidentification, newidentification);
//    }
//
//    @Override
//    public boolean updatePassengerPhone(String id, String oldidentification, String newphone) {
//        return passengerMapper.updatePassengerPhone(id, oldidentification, newphone);
//    }

    @Override
    public Integer updatePassenger(String id, String oldidentification, String newname, String newidentification, String newphone) {
        return passengerMapper.updatePassenger(id, oldidentification, newname, newidentification, newphone);
    }

    @Override
    public List<Passenger> showPassengerInfo(String userId) {
        return passengerMapper.showPassengerInfo(userId);
    }

    @Override
    public Integer deletePassenger(String userId, String PassengerName, String identification) {
        return passengerMapper.deletePassenger(userId, PassengerName, identification);
    }

    @Override
    public Passenger getPassengerByUidAndId(String id, String identification) {
        return passengerMapper.getPassengerByUidAndId(id, identification);
    }
}
