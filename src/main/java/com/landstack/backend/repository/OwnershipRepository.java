package com.landstack.backend.repository;

import com.landstack.backend.entity.Ownership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {

    List<Ownership> findByOwnerId(Long ownerId);

    List<Ownership> findByParcelUlpin(String ulpin);

    List<Ownership> findByParcelUlpinAndIsCurrentTrue(String ulpin);
}