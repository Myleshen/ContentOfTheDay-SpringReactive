package dev.myleshenp.contentnotification.notification.telegram;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface TelegramRequestRepository
        extends ReactiveMongoRepository<TelegramNotificationEntity, String> {

    Mono<Long> countByChatId(String chatId);

    Mono<TelegramNotificationEntity> deleteByChatId(String chatId);
}
