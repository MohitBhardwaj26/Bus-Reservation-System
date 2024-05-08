package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    public Ticket findBySeatNumber(String seatNumber);
}