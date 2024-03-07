package dev.myleshenp.contentnotification.scheduling;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskScheduleRepository extends ReactiveMongoRepository<TaskDefinition, String> {
}
