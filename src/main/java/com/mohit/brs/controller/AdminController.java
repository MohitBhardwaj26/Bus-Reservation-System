package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.request.*;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.AgencyRepository;
import com.mohit.brs.repository.BusRepository;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TripRepository;
import com.mohit.brs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    AgencyService agencyService;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    BusService busService;

    @Autowired
    BusRepository busRepository;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    StopService stopService;

    @Autowired
    TripService tripService;

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDTO profileDTO, Principal principal) {
        String username = principal.getName();

        // Check if user is ADMIN
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        profileService.updateProfile(profileDTO, username);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/add-agency")
    public ResponseEntity<String> addAgency(@RequestBody AgencyDTO agencyDTO, Principal principal){
        String username = principal.getName();
        User user = userService.findByEmail(username);

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        agencyService.addAgency(agencyDTO,user);

        return ResponseEntity.ok("Agency Added successfully");

    }

    @PostMapping("/add-bus")
    public ResponseEntity<String> addBus(@RequestBody BusDTO busDTO, Principal principal){

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }
        Long agencyId = busDTO.getAgencyId();
        if(!agencyExists(agencyId)){
            return ResponseEntity.badRequest().body("Agency Not Present !!!!");
        }
        busService.addBus(busDTO);

        return ResponseEntity.ok("Bus Added Successfully");

    }

    @PostMapping("/add-stop")
    public ResponseEntity<String> addStop(@RequestBody StopDTO stopDTO, Principal principal){

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        stopService.addStop(stopDTO);

        return ResponseEntity.ok("Stop Added Successfully");
    }


    @PostMapping("/add-trip")
    public ResponseEntity<String> addTrip(@RequestBody TripDTO tripDTO, Principal principal){

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        String agencyCode = tripDTO.getAgencyCode();
        if(!agencyExistsByCode(agencyCode)){
            return ResponseEntity.badRequest().body("Agency Not Present !!!!");
        }

        String busId = tripDTO.getBusCode();
        if(!busExists(busId)){
            return ResponseEntity.badRequest().body("Bus Not Present !!!");
        }

        String source = tripDTO.getSourceStopName();
        if(sourceExists(source) == null){
            return ResponseEntity.badRequest().body("Source Not Present !!!");
        }

        String dest = tripDTO.getDestinationStopName();
        if(sourceExists(dest) == null){
            return ResponseEntity.badRequest().body("Destination Not Present !!!");
        }

        tripService.addTrip(tripDTO);

        return ResponseEntity.ok("Trip Added Successfully");
    }

    private Stop sourceExists(String source) {

        Stop stop = stopRepository.findByName(source);

        return stop;
    }


    private boolean agencyExistsByCode(String agencyCode) {

        Agency agency = agencyRepository.findByCode(agencyCode);

        if(agency == null){
            return false;
        }
        return true;
    }

    private boolean busExists(String busId) {

        Bus bus = busRepository.findByCode(busId);

        if(bus == null){
            return false;
        }
        return true;
    }

    private boolean agencyExists(Long agencyId) {

        Agency agency = agencyRepository.findByAgencyId(agencyId);

        if(agency == null){
            return false;
        }
        return true;
    }

    private boolean isAdmin(Principal principal) {

        if (principal == null) {
            return false;
        }

        String username = principal.getName();
        User user = userService.findByEmail(username);
        Role role = user.getRole();


        // Check if user has ADMIN role
        if(role != null && role.getRole().toString().equalsIgnoreCase("ROLE_ADMIN")){
            return true;
        }
        return false;

    }

}
