package com.landstack.backend.controller;

import com.landstack.backend.dto.DocumentDto;
import com.landstack.backend.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<DocumentDto> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public DocumentDto getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id);
    }

    @GetMapping("/parcel/{ulpin}")
    public List<DocumentDto> getDocumentsByParcel(
            @PathVariable String ulpin) {

        return documentService.getDocumentsByParcel(ulpin);
    }

    @PostMapping
    public DocumentDto createDocument(
            @Valid @RequestBody DocumentDto dto) {

        return documentService.createDocument(dto);
    }

    @PutMapping("/{id}")
    public DocumentDto updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDto dto) {

        return documentService.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id) {

        documentService.deleteDocument(id);

        return ResponseEntity.noContent().build();
    }
}