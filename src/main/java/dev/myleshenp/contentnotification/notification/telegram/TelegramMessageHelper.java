package dev.myleshenp.contentnotification.notification.telegram;

import com.pengrad.telegrambot.model.User;
import dev.myleshenp.contentnotification.content.Content;

public class TelegramMessageHelper {

    static String getMessage(Content content, String messageTemplate) {
        var currentMessage = messageTemplate;
        currentMessage = currentMessage.replace("${type}", content.type());
        currentMessage = currentMessage.replace("${quote}", content.text());
        currentMessage = currentMessage.replace("${author}", content.author());
        return currentMessage;
    }

    static String getWelcomeMessage() {
        return """
                Hey Welcome to COTDBot (Content of the day)!!,
                This bot will send you a notification every day at 9am with a quote / something interesting
                Hope you enjoy the Content!!

                Useful Commands
                "/start" -> To get this message
                "/stop"  -> To stop the daily notifications
                "/restart" -> To restart the daily notifications
                "/content" -> Get a random content on demand
                """;
    }

    static String getRestartMessage() {
        return """
                Glad to have you back on board!!.
                """;
    }

    static String getSubscriptionStopMessage() {
        return """
                Sorry to see you go, but you can still get added back in anytime using the command "/restart"
                Have a great day!.
                """;
    }

    static TelegramNotificationEntity createEntity(Long chatId, User user) {
        return TelegramNotificationEntity.builder()
                .id(null)
                .chatId(chatId.toString())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .userName(user.username())
                .build();
    }
}
