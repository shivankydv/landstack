package com.landstack.backend.repository;

import com.landstack.backend.entity.LandParcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandParcelRepository extends JpaRepository<LandParcel, Long> {

    Optional<LandParcel> findByUlpin(String ulpin);

}