package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.request.BusDTO;
import com.mohit.brs.repository.AgencyRepository;
import com.mohit.brs.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusServiceImpl implements BusService  {


    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    BusRepository busRepository;


    @Override
    public void addBus(BusDTO busDTO) {

        Long agencyId = busDTO.getAgencyId();

        Bus bus = new Bus();

        Agency agency = agencyRepository.findByAgencyId(agencyId);

        bus.setAgency(agency);
        bus.setCode(busDTO.getCode());
        bus.setMake(busDTO.getMake());
        bus.setCapacity(busDTO.getCapacity());

        busRepository.saveAndFlush(bus);

    }

}
