package com.mohit.brs.service;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.request.StopDTO;
import com.mohit.brs.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StopServiceImpl implements StopService {

    @Autowired
    StopRepository stopRepository;

    @Override
    public void addStop(StopDTO stopDTO) {

        Stop stop = new Stop();

        stop.setCode(stopDTO.getCode());
        stop.setDetail(stopDTO.getDetail());
        stop.setName(stopDTO.getName());

        stopRepository.saveAndFlush(stop);

    }

    @Override
    public List<Stop> getAllStops() {
        List<Stop> allStops = stopRepository.findAll();
        return allStops;
    }
}
