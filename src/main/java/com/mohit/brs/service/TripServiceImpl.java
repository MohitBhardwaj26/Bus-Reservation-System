package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.TripDTO;
import com.mohit.brs.repository.AgencyRepository;
import com.mohit.brs.repository.BusRepository;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    StopRepository stopRepository;

    @Override
    public void addTrip(TripDTO tripDTO) {

        Trip trip = new Trip();

        String agencyCode = tripDTO.getAgencyCode();
        String busCode = tripDTO.getBusCode();
        String sourceName = tripDTO.getSourceStopName();
        String destinationName = tripDTO.getDestinationStopName();

        Agency agency = agencyRepository.findByCode(agencyCode);
        Bus bus = busRepository.findByCode(busCode);
        Stop source = stopRepository.findByName(sourceName);
        Stop destination = stopRepository.findByName(destinationName);

        trip.setAgency(agency);
        trip.setBus(bus);
        trip.setSourceStop(source);
        trip.setDestStop(destination);
        trip.setFare(tripDTO.getFare());
        trip.setJourneyTime(tripDTO.getJourneyTime());

        tripRepository.saveAndFlush(trip);

    }
}
