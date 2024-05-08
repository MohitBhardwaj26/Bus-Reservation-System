package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Component
@Table(name = "bus")
public class Bus {


    @Id
    @Column(name = "bus_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private int capacity;

    private String make;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agency_id")
    @JsonIgnore
    Agency agency;
}
