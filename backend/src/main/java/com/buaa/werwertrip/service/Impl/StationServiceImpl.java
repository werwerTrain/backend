package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.entity.Station;
import com.buaa.werwertrip.mapper.IStationMapper;
import com.buaa.werwertrip.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stationService")
public class StationServiceImpl implements IStationService {
    @Autowired
    private IStationMapper stationMapper;

    @Override
    public List<Station> inquireAllStations() {
        return stationMapper.inquireAllStations();
    }
}
