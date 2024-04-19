package dev.myleshenp.contentnotification.content;

import jakarta.validation.constraints.NotNull;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@With
public record Content(
        @Id String id,
        @NotNull String type,
        @NotNull String text,
        @NotNull String author,
        String userName,
        String reference) {
}
