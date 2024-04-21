package dev.myleshenp.contentnotification.content;

import static dev.myleshenp.contentnotification.constants.ApplicationConstants.CONTENT_SIZE_FOR_NOTIFICATIONS;
import static dev.myleshenp.contentnotification.constants.ApplicationConstants.EMAIL_TEMPLATE;

import com.mongodb.reactivestreams.client.MongoDatabase;
import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import dev.myleshenp.contentnotification.notification.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {

    final ContentRepository contentRepository;
    final ReactiveMongoTemplate reactiveMongoTemplate;
    final MongoDatabase mongoDatabase;
    final EmailService emailService;
    String emailTemplate = EMAIL_TEMPLATE;

    Flux<Content> getAllContent() {
        return contentRepository.findAll();
    }

    Mono<Content> getById(String id) {
        return contentRepository.findById(id);
    }

    Mono<Content> addContent(Content content) {
        return contentRepository.save(content);
    }

    public Flux<Content> getRandomContent(int size) {
        return getRandomContent(size, null);
    }

    public Flux<Content> getRandomContent(int size, String userName) {
        TypedAggregation<Content> aggregation;
        SampleOperation sampleOperation = Aggregation.sample(size);
        if (userName == null) {
            aggregation = TypedAggregation.newAggregation(Content.class, sampleOperation);
        } else {
            aggregation =
                    TypedAggregation.newAggregation(
                            Content.class,
                            Aggregation.match(Criteria.where("userName").is(userName)),
                            sampleOperation);
        }
        return reactiveMongoTemplate.aggregate(aggregation, Content.class);
    }

    String sendTodayContentAsEmail(String destinationEmail) {
        getRandomContent(CONTENT_SIZE_FOR_NOTIFICATIONS)
                .subscribe(
                        content -> {
                            EmailRequest emailRequest =
                                    EmailRequest.builder()
                                            .to(destinationEmail)
                                            .subject("Content of the Day!!")
                                            .text(getEmail(content))
                                            .build();
                            emailService
                                    .sendNotification(emailRequest)
                                    .doOnError(
                                            throwable ->
                                                    log.error(
                                                            String.format(
                                                                    "Email Could not be sent to the following email address: %s, due"
                                                                            + " to: %s",
                                                                    emailRequest.to(),
                                                                    throwable
                                                                            .getLocalizedMessage())))
                                    .subscribe(
                                            result ->
                                                    log.info(
                                                            String.format(
                                                                    "Email sent successfully to %s",
                                                                    emailRequest.to())));
                        });
        return "Request Accepted";
    }

    String getEmail(Content content) {
        var currentEmail = emailTemplate;
        currentEmail = currentEmail.replace("${type}", content.type());
        currentEmail = currentEmail.replace("${quote}", content.text());
        currentEmail = currentEmail.replace("${author}", content.author());
        return currentEmail;
    }
}
