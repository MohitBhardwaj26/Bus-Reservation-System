package com.mohit.brs.service;

import com.mohit.brs.model.request.StopDTO;
import org.springframework.stereotype.Service;

@Service
public interface StopService {

    void addStop(StopDTO stopDTO);

}
