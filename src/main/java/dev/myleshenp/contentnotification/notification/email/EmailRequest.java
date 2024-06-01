package dev.myleshenp.contentnotification.notification.email;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record EmailRequest(@Id String id, String to, String subject, String text) {}
