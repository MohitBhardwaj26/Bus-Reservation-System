package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohit.brs.model.request.TicketDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "trip_schedule")
public class TripSchedule {

    @Id
    @Column(name = "trip_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripScheduleId;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "trip_date")
    private String tripDate;

//    @Column(name = "trip_id")
//    private Integer tripId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    @JsonIgnore
    private Trip tripDetail;

    @OneToMany(mappedBy = "tripSchedule", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Ticket> ticketsSold = new HashSet<>();

    @Override
    public String toString() {
        return "TripSchedule{" +
                "tripScheduleId=" + tripScheduleId +
                ", availableSeats=" + availableSeats +
                ", tripDate='" + tripDate + '\'' +
                '}';
    }

}