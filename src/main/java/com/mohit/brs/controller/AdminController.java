package com.mohit.brs.controller;

import com.mohit.brs.model.entity.Role;
import com.mohit.brs.model.entity.User;
import com.mohit.brs.model.request.ProfileDTO;
import com.mohit.brs.service.ProfileService;
import com.mohit.brs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

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
