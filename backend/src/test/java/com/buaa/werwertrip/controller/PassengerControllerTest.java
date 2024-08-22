package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.Passenger;
import com.buaa.werwertrip.service.IMessageService;
import com.buaa.werwertrip.service.IPassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerControllerTest {
    @InjectMocks
    private PassengerController passengerController;

    @Mock
    private IPassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPassenger_Success() {
        // Arrange
        String id = "hotel123";
        String name = "John Doe";
        String identification = "123456789012345678";
        String phone = "123456789@qq.com";

        List<Passenger> existingPassengers = Collections.emptyList(); // No existing passenger
        when(passengerService.showPassengerInfo(id)).thenReturn(existingPassengers);
        when(passengerService.addPassenger(name, identification, phone, id)).thenReturn(1); // Success

        // Act
        Map<String, Object> response = passengerController.addPassenger(id, name, identification, phone);

        // Assert
        assertTrue((Boolean) response.get("info"));
    }

    @Test
    void testAddPassenger_Failure_AlreadyExists() {
        // Arrange
        String id = "hotel123";
        String name = "John Doe";
        String identification = "123456789012345678";
        String phone = "1234567890";

        Passenger existingPassenger = new Passenger();
        existingPassenger.setIdentification(identification);
        List<Passenger> existingPassengers = Collections.singletonList(existingPassenger); // Already exists
        when(passengerService.showPassengerInfo(id)).thenReturn(existingPassengers);

        // Act
        Map<String, Object> response = passengerController.addPassenger(id, name, identification, phone);

        // Assert
        assertFalse((Boolean) response.get("info"));
    }


    @Test
    void testUpdatePassenger_Success() {
        // Arrange
        String id = "hotel123";
        String oldIdentification = "123456789012345678";
        String newName = "John Smith";
        String newIdentification = "123456789012345679";
        String newPhone = "0987654321";

        when(passengerService.updatePassenger(id, oldIdentification, newName, newIdentification, newPhone)).thenReturn(1); // Success

        // Act
        Map<String, Boolean> response = passengerController.updatePassenger(id, oldIdentification, newName, newIdentification, newPhone);

        // Assert
        assertTrue(response.get("info"));
    }


    @Test
    void testUpdatePassenger_Failure() {
        // Arrange
        String id = "hotel123";
        String oldIdentification = "123456789012345678";
        String newName = "John Smith";

        when(passengerService.updatePassenger(id, oldIdentification, newName, "", "")).thenReturn(0); // Failure

        // Act
        Map<String, Boolean> response = passengerController.updatePassenger(id, oldIdentification, newName, "", "");

        // Assert
        assertFalse(response.get("info"));
    }


    @Test
    void testShowPassengerInfo_Success() {
        // Arrange
        String id = "hotel123";
        Passenger passenger = new Passenger();
        List<Passenger> passengers = Collections.singletonList(passenger);
        when(passengerService.showPassengerInfo(id)).thenReturn(passengers);

        // Act
        Map<String, Object> response = passengerController.showPassengerInfo(id);

        // Assert
        assertEquals(passengers, response.get("passenger"));
    }


    @Test
    void testShowPassengerInfo_NoData() {
        // Arrange
        String id = "hotel123";
        List<Passenger> passengers = Collections.emptyList(); // No passengers
        when(passengerService.showPassengerInfo(id)).thenReturn(passengers);

        // Act
        Map<String, Object> response = passengerController.showPassengerInfo(id);

        // Assert
        assertTrue(((List<?>) response.get("passenger")).isEmpty());
    }


    @Test
    void testDeletePassenger_Success() {
        // Arrange
        String id = "hotel123";
        String name = "John Doe";
        String identification = "123456789012345678";

        when(passengerService.deletePassenger(id, name, identification)).thenReturn(1); // Success

        // Act
        Map<String, Boolean> response = passengerController.deletePassenger(id, name, identification);

        // Assert
        assertTrue(response.get("info"));
    }


    @Test
    void testDeletePassenger_Failure() {
        // Arrange
        String id = "hotel123";
        String name = "John Doe";
        String identification = "123456789012345678";

        when(passengerService.deletePassenger(id, name, identification)).thenReturn(0); // Failure

        // Act
        Map<String, Boolean> response = passengerController.deletePassenger(id, name, identification);

        // Assert
        assertFalse(response.get("info"));
    }
}
