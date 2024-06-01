package dev.myleshenp.contentnotification.notification.email;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EmailRequestRepository extends ReactiveMongoRepository<EmailRequest, String> {}
