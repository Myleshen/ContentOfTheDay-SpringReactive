package dev.myleshenp.contentnotification.notification;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum NotificationTypesEnum {
    EMAIL("email"),
    TELEGRAM("telegram");

    String actionType;
}
