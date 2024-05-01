package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.request.AgencyDTO;
import com.mohit.brs.model.request.BusDTO;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.model.request.ProfileDTO;
import com.mohit.brs.repository.AgencyRepository;
import com.mohit.brs.service.AgencyService;
import com.mohit.brs.service.BusService;
import com.mohit.brs.service.ProfileService;
import com.mohit.brs.service.UserService;
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

    private boolean agencyExists(long agencyId) {

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
