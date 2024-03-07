package dev.myleshenp.contentnotification.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class TaskScheduleController {

    final TaskScheduleService taskScheduleService;

    @GetMapping
    Flux<TaskDefinition> getAllSchedules() {
        return taskScheduleService.getAllSchedules();
    }

    @GetMapping("/{jobId}")
    Mono<TaskDefinition> getAllSchedules(@PathVariable String jobId) {
        return taskScheduleService.getByJobId(jobId);
    }

    @PostMapping
    TaskDefinition addNewSchedule(@RequestBody TaskDefinition taskDefinition) {
        var task = taskScheduleService.addTaskToDB(taskDefinition).block();
        if (task.isEnabled()) {
            taskScheduleService.addNewScheduleToScheduler(task);
        }
        return task;
    }

    @PutMapping("/{jobId}")
    TaskDefinition switchEnabledStatusOfTask(@PathVariable String jobId) {
        return taskScheduleService.switchStatusOfStatus(jobId);
    }
}
