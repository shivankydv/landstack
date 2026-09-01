package com.landstack.backend.service;

import com.landstack.backend.dto.DocumentDto;
import com.landstack.backend.entity.Document;
import com.landstack.backend.entity.LandParcel;
import com.landstack.backend.exception.ResourceNotFoundException;
import com.landstack.backend.repository.DocumentRepository;
import com.landstack.backend.repository.LandParcelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final LandParcelRepository landParcelRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            LandParcelRepository landParcelRepository) {

        this.documentRepository = documentRepository;
        this.landParcelRepository = landParcelRepository;
    }

    public List<DocumentDto> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DocumentDto getDocumentById(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document not found: " + id
                        )
                );

        return toDto(document);
    }

    public List<DocumentDto> getDocumentsByParcel(String ulpin) {

        if (!landParcelRepository.existsByUlpin(ulpin)) {
            throw new ResourceNotFoundException(
                    "Parcel not found: " + ulpin
            );
        }

        return documentRepository.findByParcelUlpin(ulpin)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DocumentDto createDocument(DocumentDto dto) {

        LandParcel parcel = landParcelRepository
                .findByUlpin(dto.getParcelUlpin())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: " + dto.getParcelUlpin()
                        )
                );

        Document document = new Document();

        document.setParcel(parcel);
        document.setDocumentType(dto.getDocumentType());
        document.setDocumentNumber(dto.getDocumentNumber());
        document.setDocumentDate(dto.getDocumentDate());
        document.setStatus(dto.getStatus());

        if (dto.getVerificationStatus() != null) {
            document.setVerificationStatus(
                    dto.getVerificationStatus()
            );
        }

        Document savedDocument =
                documentRepository.save(document);

        return toDto(savedDocument);
    }

    public DocumentDto updateDocument(
            Long id,
            DocumentDto dto) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document not found: " + id
                        )
                );

        LandParcel parcel = landParcelRepository
                .findByUlpin(dto.getParcelUlpin())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: " + dto.getParcelUlpin()
                        )
                );

        document.setParcel(parcel);
        document.setDocumentType(dto.getDocumentType());
        document.setDocumentNumber(dto.getDocumentNumber());
        document.setDocumentDate(dto.getDocumentDate());
        document.setStatus(dto.getStatus());

        if (dto.getVerificationStatus() != null) {
            document.setVerificationStatus(
                    dto.getVerificationStatus()
            );
        }

        Document updatedDocument =
                documentRepository.save(document);

        return toDto(updatedDocument);
    }

    public void deleteDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document not found: " + id
                        )
                );

        documentRepository.delete(document);
    }

    private DocumentDto toDto(Document document) {

        DocumentDto dto = new DocumentDto();

        dto.setId(document.getId());
        dto.setParcelUlpin(
                document.getParcel().getUlpin()
        );
        dto.setDocumentType(
                document.getDocumentType()
        );
        dto.setDocumentNumber(
                document.getDocumentNumber()
        );
        dto.setDocumentDate(
                document.getDocumentDate()
        );
        dto.setStatus(
                document.getStatus()
        );
        dto.setVerificationStatus(
                document.getVerificationStatus()
        );

        return dto;
    }
}