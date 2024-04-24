package com.mohit.brs.service;


import com.mohit.brs.model.entity.Role;
import com.mohit.brs.model.entity.User;
import com.mohit.brs.model.request.UserRegistrationDto;
import com.mohit.brs.repository.RoleRepository;
import com.mohit.brs.repository.UserRepository;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private final UserRepository userRepository;


    @Autowired
    private final RoleRepository roleRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }


    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {



        Optional<Role> userRole = roleRepository.findById(userRegistrationDto.getRole().getId());

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .phoneNumber(userRegistrationDto.getPhoneNumber())
                .build();

        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        if(userRole.isEmpty()){
            throw new RuntimeException();
        }
        else{
//            user.setRole(Set.of(userRole.get()));
            user.setRole(userRole.get());
        }

        userRepository.saveAndFlush(user);

    }

    @Override
    public void loginUser(String email, String password) {


    }

    @Override
    public boolean isUserExists(String email) {

        return userRepository.existsByEmail(email);

    }

    @Override
    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

//        @may-to-may
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()) );

//        many-to-one
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(mapRoleToAuthority(user.getRole()))
        );


    }

//  @may-to-may
//    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
//
//        return roles.getRole().name();
//    }

//    many-to-one
    private GrantedAuthority mapRoleToAuthority(Role role) {
        return new SimpleGrantedAuthority(role.getRole().name());
    }
}
