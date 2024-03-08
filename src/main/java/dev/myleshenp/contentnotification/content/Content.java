package dev.myleshenp.contentnotification.content;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record Content(
        @Id String id,
        @NotNull String type,
        @NotNull String text,
        @NotNull String author,
        String reference) {}
