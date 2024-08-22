package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.Passenger;
import com.buaa.werwertrip.service.IPassengerService;
import com.buaa.werwertrip.service.IUserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@RestController
public class PassengerController {
    @Autowired
    private IPassengerService passengerService;
    @Autowired
    private IUserService userService;

    @PostMapping("/insertPassengers/{id}")
    public Map<String, Object> addPassenger(@PathVariable String id,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "identification") String identification,
                                             @RequestParam(value = "phone") String phone) {
//        if(!identification.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
//            return new HashMap<>(){{
//                put("info", false);
//                put("message","身份证号格式错误");
//            }};
        Integer res =0;
        int flag = 1;
        List<Passenger> passengers= passengerService.showPassengerInfo(id);
        for (Passenger passenger : passengers) {
            if (passenger.getIdentification().equals(identification)) {
                flag = 0;
                break;
            }
        }
        if(flag==1)
            res=passengerService.addPassenger(name, identification, phone, id);
        boolean result;
        result = (res != 0);
        return new HashMap<>() {{
            put("info", result);
        }};
    }

    @PostMapping("/updatePassengers/{id}")
    public Map<String, Boolean> updatePassenger(@PathVariable String id,
                                                @RequestParam(value = "oldidentification") String oldidentification,
                                                @RequestParam(value = "newname", required = false, defaultValue = "") String newname,
                                                @RequestParam(value = "newidentification", required = false, defaultValue = "") String newidentification,
                                                @RequestParam(value = "newphone", required = false, defaultValue = "") String newphone) {

        if (newname.isEmpty()) newname = null;
        if (newidentification.isEmpty()) newidentification = null;
        if (newphone.isEmpty()) newphone = null;
        String finalNewname = newname;
        String finalNewidentification = newidentification;
        String finalNewphone = newphone;

        Integer res = passengerService.updatePassenger(id, oldidentification, finalNewname, finalNewidentification, finalNewphone);
        boolean result;
        result = res != 0;
        return new HashMap<>() {{
            put("info", result);
        }};
    }

    @PostMapping("/passengersInfo/{id}")
    public Map<String, Object> showPassengerInfo(@PathVariable String id) {
        return new HashMap<>() {{
            put("passenger", passengerService.showPassengerInfo(id));
        }};
    }

    @PostMapping("/deletePassengers/{id}")
    public Map<String, Boolean> deletePassenger(@PathVariable String id,
                                                @RequestParam String name,
                                                @RequestParam String identification) {
        String passengerName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        Integer res = passengerService.deletePassenger(id, passengerName, identification);
        boolean result;
        result = res != 0;
        return new HashMap<>() {{
            put("info", result);
        }};
    }
}
