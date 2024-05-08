package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohit.brs.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "agency")
public class Agency {

    @Id
    @Column(name = "agency_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agencyId;

    @Column(name = "code")
    String code;

    @Column(name="details")
    String details;

    @Column(name = "name")
    String name;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_user_id")
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Bus> buses;


}
