package com.mohit.brs.service;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.request.StopDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StopService {

    void addStop(StopDTO stopDTO);

    List<Stop> getAllStops();
}
