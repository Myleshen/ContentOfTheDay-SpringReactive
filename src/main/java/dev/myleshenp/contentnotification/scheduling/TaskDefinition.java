package dev.myleshenp.contentnotification.scheduling;

import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import jakarta.validation.constraints.NotNull;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@With
public record TaskDefinition(
        @Id
        String id,
        @NotNull
        String cronExpression,
        @NotNull
        String actionType,
        @NotNull
        Boolean isEnabled,
        EmailRequest emailRequest
) {}
