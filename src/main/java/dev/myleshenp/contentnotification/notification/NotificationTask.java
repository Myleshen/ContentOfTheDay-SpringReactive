package dev.myleshenp.contentnotification.notification;

import jakarta.validation.constraints.NotNull;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@With
public record NotificationTask(
        @Id String id,
        @NotNull NotificationTypesEnum notificationType,
        @NotNull Boolean isEnabled,
        @NotNull Record request) {}
