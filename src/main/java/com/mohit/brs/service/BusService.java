package com.mohit.brs.service;

import com.mohit.brs.model.request.AgencyDTO;
import com.mohit.brs.model.request.BusDTO;
import com.mohit.brs.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface BusService {

    void addBus(BusDTO busDTO);


}
