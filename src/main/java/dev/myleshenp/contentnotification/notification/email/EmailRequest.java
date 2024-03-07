package dev.myleshenp.contentnotification.notification.email;

import lombok.Builder;

@Builder
public record EmailRequest(
        String to,
        String subject,
        String text
) {
}
