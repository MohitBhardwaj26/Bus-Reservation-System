package com.mohit.brs.service;

import com.mohit.brs.model.request.TripDTO;
import org.springframework.stereotype.Service;

@Service
public interface TripService {

    void addTrip(TripDTO tripDTO);

}
