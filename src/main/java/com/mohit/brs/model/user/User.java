package com.mohit.brs.model.user;

import com.mohit.brs.model.bus.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_role",
//            joinColumns = {@JoinColumn(name = "email")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id")})
//    private Set<Role> role;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, phoneNumber, password, role);
    }

}
