package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Bus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface BusRepository extends JpaRepository<Bus, Long> {

    Bus findByCode(String busId);
}
