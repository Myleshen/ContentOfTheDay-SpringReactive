package dev.myleshenp.contentnotification.notification;

import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import dev.myleshenp.contentnotification.notification.email.EmailService;
import dev.myleshenp.contentnotification.notification.telegram.TelegramNotificationEntity;
import dev.myleshenp.contentnotification.notification.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    final EmailService emailService;
    final TelegramService telegramService;

    @PostMapping("/emails")
    Mono<EmailRequest> sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendNotification(emailRequest);
    }

    @GetMapping("/telegram")
    Flux<TelegramNotificationEntity> getAllTelegramSubscriptions() {
        return telegramService.getAllSubscriptions();
    }

    @GetMapping("/telegram/send/{chatId}")
    Mono<String> sendTelegramNotification(@PathVariable String chatId) {
        telegramService.sendMessage(chatId);
        return Mono.just("Request Accepted");
    }
}
