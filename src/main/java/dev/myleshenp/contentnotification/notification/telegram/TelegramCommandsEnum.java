package dev.myleshenp.contentnotification.notification.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TelegramCommandsEnum {
    START("/start"),
    STOP("/stop"),
    RESTART("/restart"),
    CONTENT("/content"),
    ADD_CONTENT("/add_content"),
    DEMO_ADD_CONTENT("/get_template_add_content");

    private final String command;

    public static TelegramCommandsEnum getEnumFromCommand(String command) {
        return switch (command) {
            case "/start" -> START;
            case "/stop" -> STOP;
            case "/restart" -> RESTART;
            case "/add_content" -> ADD_CONTENT;
            case "/get_template_add_content" -> DEMO_ADD_CONTENT;
            default -> CONTENT;
        };
    }
}
