package dev.myleshenp.contentnotification.notification.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramConfig {

    @Value("${telegram.bot.token}")
    String botToken;

    @Getter static TelegramBot telegramBot;

    final TelegramService service;

    @PostConstruct
    void init() {
        telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(
                listener -> {
                    listener.forEach(service::messageListener);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                error -> {
                    if (error.response() != null) {
                        log.error(error.response().toString());
                    } else {
                        log.error(error.getLocalizedMessage());
                    }
                });
    }
}
