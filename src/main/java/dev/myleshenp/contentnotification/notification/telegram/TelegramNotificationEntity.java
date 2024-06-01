package dev.myleshenp.contentnotification.notification.telegram;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@With
public record TelegramNotificationEntity(
        @Id String id,
        @Indexed(unique = true) String chatId,
        String firstName,
        String lastName,
        String userName) {}
