package dev.myleshenp.contentnotification.notification.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.myleshenp.contentnotification.content.Content;
import dev.myleshenp.contentnotification.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

import static dev.myleshenp.contentnotification.constants.ApplicationConstants.CONTENT_SIZE_FOR_NOTIFICATIONS;
import static dev.myleshenp.contentnotification.constants.ApplicationConstants.MESSAGE_TEMPLATE;
import static dev.myleshenp.contentnotification.notification.telegram.TelegramMessageHelper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    final TelegramRequestRepository repository;
    final ContentService contentService;
    final ObjectMapper objectMapper;
    String messageTemplate = MESSAGE_TEMPLATE;

    @Async
    @Scheduled(cron = "0 0 9 * * *", zone = "IST")
    public void runAsScheduled() {
        log.info("Scheduled task executed at {}", System.currentTimeMillis());
        repository
                .findAll()
                .subscribe(notificationEntity -> sendNotification(notificationEntity.chatId()));
    }

    public void messageListener(Update update) {
        if (update.message() != null && update.message().text() != null) {
            var chatId = update.message().chat().id();
            var user = update.message().from();
            var bot = TelegramConfig.getTelegramBot();
            var pattern = Pattern.compile("^[/a-z_]*");
            var matcher = pattern.matcher(update.message().text());
            matcher.find();
            var command = matcher.group();
            switch (TelegramCommandsEnum.getEnumFromCommand(command)) {
                case START -> {
                    bot.execute(new SendMessage(chatId, getWelcomeMessage()));
                    saveSubscription(createEntity(chatId, user)).subscribe();
                }
                case STOP -> {
                    bot.execute(new SendMessage(chatId, getSubscriptionStopMessage()));
                    deleteSubscription(chatId.toString()).subscribe();
                }
                case RESTART -> {
                    bot.execute(new SendMessage(chatId, getRestartMessage()));
                    saveSubscription(createEntity(chatId, user)).subscribe();
                }
                case CONTENT -> {
                    contentService
                            .getRandomContent(CONTENT_SIZE_FOR_NOTIFICATIONS)
                            .subscribe(
                                    content -> {
                                        bot.execute(
                                                new SendMessage(
                                                        chatId,
                                                        getMessage(content, messageTemplate)));
                                    });
                }
                case ADD_CONTENT -> {
                    var jsonContent =
                            update.message().text().substring(update.message().text().indexOf("{"));
                    var content = parseMessageToContent(jsonContent);
                    if (content == null) {
                        bot.execute(new SendMessage(chatId, getAddNewContentErrorMessage()));
                    }
                    contentService.addContent(content).subscribe(x ->
                            bot.execute(new SendMessage(chatId, getAddNewContentMessage(x)))
                    );
                }
                case DEMO_ADD_CONTENT -> {
                    bot.execute(new SendMessage(chatId, getTemplateAddNewContentMessage()));
                }
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
        log.info("Successfully sent message to {}", chatId);
    }

    public Flux<TelegramNotificationEntity> getAllSubscriptions() {
        return repository.findAll();
    }

    public Mono<TelegramNotificationEntity> saveSubscription(
            TelegramNotificationEntity telegramNotificationEntity) {
        log.info("Saving subscription: {}", telegramNotificationEntity.toString());
        return repository.save(telegramNotificationEntity);
    }

    public Mono<TelegramNotificationEntity> deleteSubscription(String chatId) {
        log.info("Deleting subscription for chatId: {}", chatId);
        return repository.deleteByChatId(chatId);
    }

    private Content parseMessageToContent(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, Content.class);
        } catch (JsonProcessingException e) {
            log.error("Cannot parse message to content", e);
            return null;
        }
    }
}
