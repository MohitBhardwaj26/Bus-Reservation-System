package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TripRepository extends JpaRepository<Trip, Long> {
}
