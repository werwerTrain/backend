package com.buaa.werwertrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buaa.werwertrip.entity.Passenger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPassengerMapper extends BaseMapper<Passenger> {
    Integer addPassenger(String name, String identification, String phone, String userId);

//    boolean updatePassengerName(String id, String oldidentification, String newname);
//
//    boolean updatePassengerIdentification(String id, String oldidentification, String newidentification);
//
//    boolean updatePassengerPhone(String id, String oldidentification, String newphone);

    Integer updatePassenger(String id, String oldidentification, String newname, String newidentification, String newphone);

    List<Passenger> showPassengerInfo(String userId);

    Integer deletePassenger(String userId, String passengerName, String identification);

    Passenger getPassengerByUidAndId(String id, String identification);
}
