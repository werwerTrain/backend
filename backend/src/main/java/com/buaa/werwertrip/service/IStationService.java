package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IStationService {
    public List<Station> inquireAllStations();

}
