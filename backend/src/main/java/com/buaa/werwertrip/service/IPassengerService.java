package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.Passenger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IPassengerService {
    public Integer addPassenger(String name, String identification, String phone, String userId);

//     boolean updatePassengerName(String id, String oldidentification, String newname);
//
//    boolean updatePassengerIdentification(String id, String oldidentification, String newidentification);
//
//    boolean updatePassengerPhone(String id, String oldidentification, String newphone);

   Integer updatePassenger(String id, String oldidentification, String newname, String newidentification, String newphone);

    public List<Passenger> showPassengerInfo(String userId);

    public Integer deletePassenger(String id, String PassengerName, String identification);

    Passenger getPassengerByUidAndId(String id, String identification);
}
