package model;

public record CloudDocument(
        Long id,
        String name,
        String content,
        String createdAt,
        String updatedAt
) {
}
