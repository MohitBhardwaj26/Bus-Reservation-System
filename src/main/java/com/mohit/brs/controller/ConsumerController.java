package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.SearchTripDTO;
import com.mohit.brs.model.request.TripDTO;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.service.StopService;
import com.mohit.brs.service.TripScheduleService;
import com.mohit.brs.service.TripService;
import com.mohit.brs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/consumer")
public class ConsumerController {


    @Autowired
    StopService stopService;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    TripService tripService;

    @Autowired
    UserService userService;

    @Autowired
    TripScheduleService tripScheduleService;

    @GetMapping("/all-stops")
    public ResponseEntity<?> fetchAllStops(Principal principal){

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

        List<Stop> allStops = stopService.getAllStops();

        return new ResponseEntity<>(allStops,HttpStatus.OK);
    }

    @GetMapping("/search-trip")
    public ResponseEntity<?> searchTrip(@RequestBody SearchTripDTO searchTripDTO, Principal principal){

        String source = searchTripDTO.getSource();
        String destination = searchTripDTO.getDestination();

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

        if(sourceExists(source) == null){
            return ResponseEntity.badRequest().body("Source Not Present !!!");
        }

        if(sourceExists(destination) == null){
            return ResponseEntity.badRequest().body("Destination Not Present !!!");
        }

        List<Trip> allTrips = tripService.getALlTrips();

        List<TripDTO> searchTripResult = new ArrayList<>();

        for(Trip element:allTrips){

            Stop sourceName = element.getSourceStop();
            Stop destinationName = element.getDestStop();

            String source_check = sourceName.getName();
            String destination_check = destinationName.getName();

            if(source_check.equals(source) && destination_check.equals(destination)){
                TripDTO tripDTO = new TripDTO();
                tripDTO.setFare(element.getFare());
                tripDTO.setJourneyTime(element.getJourneyTime());
                tripDTO.setAgencyCode(element.getAgency().getCode());
                tripDTO.setBusCode(element.getBus().getCode());
                tripDTO.setSourceStopCode(element.getSourceStop().getCode());
                tripDTO.setSourceStopName(element.getSourceStop().getName());
                tripDTO.setDestinationStopCode(element.getDestStop().getCode());
                tripDTO.setDestinationStopName(element.getDestStop().getName());
                searchTripResult.add(tripDTO);
            }

        }
        return new ResponseEntity<>(searchTripResult,HttpStatus.OK);
    }

    @GetMapping("/searchTripSchedule")
    public ResponseEntity<?> filterTripScheduleBasedOnDate(Principal principal, @RequestParam String tripDate){

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

        List<?> tripScheduleList =  tripScheduleService.getTripScheduleListByDate(tripDate);


        return new ResponseEntity<>(tripScheduleList , HttpStatus.OK);
    }

    private Stop sourceExists(String source) {

        Stop stop = stopRepository.findByName(source);

        return stop;
    }

    private boolean isUser(Principal principal) {

        if (principal == null) {
            return false;
        }

        String username = principal.getName();
        User user = userService.findByEmail(username);
        Role role = user.getRole();


        // Check if user has ADMIN role
        if(role != null && role.getRole().toString().equalsIgnoreCase("ROLE_USER")){
            return true;
        }
        return false;

    }

}
