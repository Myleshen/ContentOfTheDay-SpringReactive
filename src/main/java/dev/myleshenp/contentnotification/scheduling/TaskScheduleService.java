package dev.myleshenp.contentnotification.scheduling;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskScheduleService {

    final RunnableTask runnableTask;
    final TaskScheduler taskScheduler;
    final TaskScheduleRepository taskScheduleRepository;
    final Map<String, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    Flux<TaskDefinition> getAllSchedules() {
        return taskScheduleRepository.findAll();
    }

    Mono<TaskDefinition> getByJobId(String jobId) {
        return taskScheduleRepository.findById(jobId);
    }

    Mono<TaskDefinition> addTaskToDB(TaskDefinition taskDefinition) {
        return taskScheduleRepository.save(taskDefinition);
    }

    void addNewScheduleToScheduler(TaskDefinition taskDefinition) {
        // In Memory only
        ScheduledFuture<?> task = taskScheduler.schedule(runnableTask,
                new CronTrigger(taskDefinition.cronExpression(), TimeZone.getTimeZone(TimeZone.getDefault().getID()))
        );
        scheduledTasks.put(taskDefinition.id(), task);
    }

    void removeScheduleFromMap(String jobId) {
        scheduledTasks.remove(jobId);
    }

    @PostConstruct
    void loadTasksFromDB() {
        getAllSchedules().doOnNext(this::addNewScheduleToScheduler);
    }

    TaskDefinition switchStatusOfStatus(String jobId) {
        var task = getByJobId(jobId).block();
        if (task != null) {
            TaskDefinition updatedTask;
            if (task.isEnabled()) {
                updatedTask = task.withIsEnabled(false);
                removeScheduleFromMap(jobId);
            } else {
                updatedTask = task.withIsEnabled(true);
                addNewScheduleToScheduler(updatedTask);
            }
            return taskScheduleRepository.save(updatedTask).block();
        }
        return null;
    }
}
