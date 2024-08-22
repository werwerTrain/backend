package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.service.IStationService;
import com.buaa.werwertrip.controller.StationController;
import com.buaa.werwertrip.entity.Station; // 假设有 Station 类
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StationControllerTest {

    @Mock
    private IStationService stationService;

    @InjectMocks
    private StationController stationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 正面测试用例：成功获取所有车站
    @Test
    void testInquireAllStations_ShouldReturnStationList() {
        // Arrange
        List<Station> mockStations = new ArrayList<>();
        Station station1 = new Station();
        station1.setStationName("Station1");
        Station station2 = new Station();
        station2.setStationName("Station2");
        mockStations.add(station1);
        mockStations.add(station2);

        when(stationService.inquireAllStations()).thenReturn(mockStations);

        // Act
        Map<String, Object> response = stationController.inquireAllStations();

        // Assert
        List<Map<String, Object>> stations = (List<Map<String, Object>>) response.get("station");
        assertEquals(2, stations.size());

        Map<String, Object> firstStation = stations.get(0);
        assertEquals("Station1", firstStation.get("value"));

        Map<String, Object> secondStation = stations.get(1);
        assertEquals("Station2", secondStation.get("value"));
    }

    // 反面测试用例：车站列表为空
    @Test
    void testInquireAllStations_WhenNoStationsFound_ShouldReturnEmptyList() {
        // Arrange
        when(stationService.inquireAllStations()).thenReturn(new ArrayList<>());

        // Act
        Map<String, Object> response = stationController.inquireAllStations();

        // Assert
        List<Map<String, Object>> stations = (List<Map<String, Object>>) response.get("station");
        assertTrue(stations.isEmpty());
    }
}
