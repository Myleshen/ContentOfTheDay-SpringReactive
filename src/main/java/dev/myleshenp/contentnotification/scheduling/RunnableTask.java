package dev.myleshenp.contentnotification.scheduling;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RunnableTask implements Runnable{

    TaskDefinition taskDefinition;

    @Override
    public void run() {
        log.info(taskDefinition.id() + taskDefinition.actionType() + taskDefinition.cronExpression());
    }

}
