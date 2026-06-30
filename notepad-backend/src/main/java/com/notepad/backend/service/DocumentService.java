package com.notepad.backend.service;

import com.notepad.backend.dto.DocumentRequest;
import com.notepad.backend.dto.DocumentResponse;
import com.notepad.backend.entity.Document;
import com.notepad.backend.entity.User;
import com.notepad.backend.repository.DocumentRepository;
import com.notepad.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public List<DocumentResponse> listDocuments(String username) {
        User owner = findUser(username);
        return documentRepository.findByOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getDocument(Long id, String username) {
        User owner = findUser(username);
        Document document = documentRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return mapToResponse(document);
    }

    public DocumentResponse createDocument(DocumentRequest request, String username) {
        User owner = findUser(username);
        Document document = Document.builder()
                .name(request.getName())
                .content(request.getContent())
                .owner(owner)
                .build();
        Document saved = documentRepository.save(document);
        return mapToResponse(saved);
    }

    public DocumentResponse updateDocument(Long id, DocumentRequest request, String username) {
        User owner = findUser(username);
        Document document = documentRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setName(request.getName());
        document.setContent(request.getContent());
        Document saved = documentRepository.save(document);
        return mapToResponse(saved);
    }

    public void deleteDocument(Long id, String username) {
        User owner = findUser(username);
        Document document = documentRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        documentRepository.delete(document);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private DocumentResponse mapToResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .content(document.getContent())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
