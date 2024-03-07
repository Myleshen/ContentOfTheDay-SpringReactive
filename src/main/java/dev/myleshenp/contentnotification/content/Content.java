package dev.myleshenp.contentnotification.content;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record Content(@Id String id, @NotNull  String type, @NotNull String text, @NotNull String author, String reference) {}
