package dev.myleshenp.contentnotification.notification.telegram;

import static dev.myleshenp.contentnotification.constants.ApplicationConstants.CONTENT_SIZE_FOR_NOTIFICATIONS;
import static dev.myleshenp.contentnotification.constants.ApplicationConstants.MESSAGE_TEMPLATE;
import static dev.myleshenp.contentnotification.notification.telegram.TelegramMessageHelper.*;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.myleshenp.contentnotification.content.ContentService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    final TelegramRequestRepository repository;
    final ContentService contentService;
    String messageTemplate = MESSAGE_TEMPLATE;

    @Async
    @Scheduled(cron = "0 0 9 * * *")
    public void runAsScheduled() {
        repository
                .findAll()
                .subscribe(notificationEntity -> sendNotification(notificationEntity.chatId()));
    }

    public void messageListener(Update update) {
        if(update.message() != null && update.message().text() != null) {
            var chatId = update.message().chat().id();
            var user = update.message().from();
            var bot = TelegramConfig.getTelegramBot();
            if (Objects.equals(update.message().text(), "/start")) {
                bot.execute(new SendMessage(chatId, getWelcomeMessage()));
                saveSubscription(createEntity(chatId, user)).subscribe();
            } else if (Objects.equals(update.message().text(), "/stop")) {
                bot.execute(new SendMessage(chatId, getSubscriptionStopMessage()));
                deleteSubscription(chatId.toString()).subscribe();
            } else if (Objects.equals(update.message().text(), "/restart")) {
                bot.execute(new SendMessage(chatId, getRestartMessage()));
                saveSubscription(createEntity(chatId, user)).subscribe();
            } else if (Objects.equals(update.message().text(), "/content")) {
                contentService
                        .getRandomContent(CONTENT_SIZE_FOR_NOTIFICATIONS)
                        .subscribe(
                                content -> {
                                    bot.execute(
                                            new SendMessage(
                                                    chatId, getMessage(content, messageTemplate)));
                                });
            }
        }
    }

    public void sendMessage(String chatId) {
        sendNotification(chatId);
    }

    void sendNotification(String chatId) {
        contentService
                .getRandomContent(CONTENT_SIZE_FOR_NOTIFICATIONS)
                .subscribe(
                        content -> {
                            var bot = TelegramConfig.getTelegramBot();
                            bot.execute(
                                    new SendMessage(chatId, getMessage(content, messageTemplate)));
                        });
    }

    public Flux<TelegramNotificationEntity> getAllSubscriptions() {
        return repository.findAll();
    }

    public Mono<TelegramNotificationEntity> saveSubscription(
            TelegramNotificationEntity telegramNotificationEntity) {
        return repository.save(telegramNotificationEntity);
    }

    public Mono<TelegramNotificationEntity> deleteSubscription(String chatId) {
        return repository.deleteByChatId(chatId);
    }
}
