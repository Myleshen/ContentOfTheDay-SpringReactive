package dev.myleshenp.contentnotification.content;

import com.mongodb.reactivestreams.client.MongoDatabase;
import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import dev.myleshenp.contentnotification.notification.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
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

  Flux<Content> getAllContent() {
    return contentRepository.findAll();
  }

  Mono<Content> getById(String id) {
    return contentRepository.findById(id);
  }

  Mono<Content> addContent(Content content) {
    return contentRepository.save(content);
  }

  Flux<Content> getRandomContent(int size) {
    SampleOperation sampleOperation = Aggregation.sample(size);
    TypedAggregation<Content> aggregation = TypedAggregation.newAggregation(Content.class, sampleOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Content.class);
  }

  Boolean sendTodayContentAsEmail(String destinationEmail) {
    var content = getRandomContent(1).blockFirst();
    var text = """
            Content Type: %s
            
            "%s"
            
            /t/t/t -- %s
            """.formatted(content.type(), content.text(), content.author());
    EmailRequest emailRequest = EmailRequest.builder()
            .to(destinationEmail)
            .subject("Content of the Day!!")
            .text(text)
            .build();
    return emailService.sendNotification(emailRequest).block();
  }

}
