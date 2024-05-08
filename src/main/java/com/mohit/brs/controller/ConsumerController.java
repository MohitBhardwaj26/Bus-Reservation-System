package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.SearchTripDTO;
import com.mohit.brs.model.request.TicketDto;
import com.mohit.brs.model.request.TripDTO;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TicketRepository;
import com.mohit.brs.repository.TripScheduleRepository;
import com.mohit.brs.service.*;
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

   @Autowired
   TripScheduleRepository tripScheduleRepository;

   @Autowired
   TicketService ticketService;

   @Autowired
   TicketRepository ticketRepository;


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

        if(tripScheduleList == null){
            return ResponseEntity.badRequest().body("No Trip Schedule Present !!!");
        }


        return new ResponseEntity<>(tripScheduleList , HttpStatus.OK);
    }

    @PostMapping("/bookTicket")
    public ResponseEntity<?> bookTicketForTrip(Principal principal, @RequestBody TicketDto ticketDto) {
        try {
            if (!isUser(principal)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
            }

            // Retrieve passenger, trip schedule, and other details from the request
            User passenger = userService.findByEmail(ticketDto.getPassengerEmailId());


            if (passenger == null) {
                return ResponseEntity.badRequest().body("User is Not Present !!!!");
            }

            TripSchedule tripSchedule = tripScheduleRepository.findById(ticketDto.getTripScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("No Schedule for this Trip found !!"));

            if(!ticketDto.getJourneyDate().equals(tripSchedule.getTripDate())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select correct trip date for this trip schedule");

            }

            synchronized (tripSchedule) {
                // Check if there are available seats
                int availableSeats = tripSchedule.getAvailableSeats();
                if (availableSeats <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available seats for this trip schedule");
                }


                Ticket alreadyBooked = ticketRepository.findBySeatNumber(ticketDto.getSeatNumber());

                if (alreadyBooked != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat is already Booked");
                }

                // Book the ticket
                Ticket ticket = ticketService.bookTicket(ticketDto, passenger);

                tripSchedule.getTicketsSold().add(ticket);

                // Update the available seats
                tripSchedule.setAvailableSeats(availableSeats - 1);

                // Save the changes to tripSchedule
                tripScheduleRepository.save(tripSchedule);

                return new ResponseEntity<>(ticketDto, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle any exceptions and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while booking the ticket " + e.getMessage());
        }
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
