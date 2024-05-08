package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "trip")
public class Trip {



    @Id
    @Column(name = "trip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fare;

    @Column(name = "journey_time")
    private int journeyTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "source_stop_id")
    @JsonIgnore
    private Stop sourceStop;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dest_stop_id")
    @JsonIgnore
    private Stop destStop;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bus_id")
    @JsonIgnore
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agency_id")
    @JsonIgnore
    private Agency agency;

}
