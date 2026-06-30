package com.notepad.backend.controller;

import com.notepad.backend.dto.DocumentRequest;
import com.notepad.backend.dto.DocumentResponse;
import com.notepad.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> listDocuments(Principal principal) {
        return ResponseEntity.ok(documentService.listDocuments(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(documentService.getDocument(id, principal.getName()));
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @Validated @RequestBody DocumentRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(documentService.createDocument(request, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable Long id,
            @Validated @RequestBody DocumentRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id,
            Principal principal
    ) {
        documentService.deleteDocument(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
