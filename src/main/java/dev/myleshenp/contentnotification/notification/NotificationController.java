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

    @GetMapping("/emails")
    Flux<EmailRequest> getAllEmailSubscriptions() {
        // TODO
        return null;
    }

    @GetMapping("/emails/{id}")
    Mono<EmailRequest> getEmailSubscriptionById(@PathVariable String id) {
        // TODO
        return null;
    }

    @PostMapping("/emails")
    Mono<Boolean> addNewEmailSubscription(@RequestBody EmailRequest emailRequest) {
        // TODO
        return null;
    }

    @GetMapping("/emails/send")
    Mono<EmailRequest> sendEmailNotification() {
        // TODO
        return null;
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
