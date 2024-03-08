package dev.myleshenp.contentnotification.content;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends ReactiveMongoRepository<Content, String> {}
