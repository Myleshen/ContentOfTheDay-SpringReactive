package dev.myleshenp.contentnotification.notification;

import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import dev.myleshenp.contentnotification.notification.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    final EmailService emailService;

    @PostMapping("/emails")
    Mono<Boolean> sendNotification(@RequestBody EmailRequest emailRequest) {
        return emailService.sendNotification(emailRequest);
    }
}
