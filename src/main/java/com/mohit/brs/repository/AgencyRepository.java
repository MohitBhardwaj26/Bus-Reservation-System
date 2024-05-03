package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Agency;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Agency findByAgencyId(Long agencyId);
    Agency findByCode(String code);
}
