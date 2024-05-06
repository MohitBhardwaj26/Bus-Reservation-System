package com.mohit.brs.service;

import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.TripDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TripService {

    void addTrip(TripDTO tripDTO);

    List<Trip> getALlTrips();
}
