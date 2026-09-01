package com.landstack.backend.repository;

import com.landstack.backend.entity.LandParcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LandParcelRepository
        extends JpaRepository<LandParcel, Long>,
        JpaSpecificationExecutor<LandParcel> {

    Optional<LandParcel> findByUlpin(String ulpin);

    boolean existsByUlpin(String ulpin);
}