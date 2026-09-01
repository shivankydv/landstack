package com.landstack.backend.repository;

import com.landstack.backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByParcelUlpin(String ulpin);
}