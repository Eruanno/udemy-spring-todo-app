package io.github.eruanno;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskGroup;
import io.github.eruanno.model.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    public static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository taskGroupRepository;

    Warmup(final TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setDescription(description);
            taskGroup.setTasks(Set.of(new Task(null, "ContextClosedEvent", taskGroup),
                    new Task(null, "ContextRefreshedEvent", taskGroup),
                    new Task(null, "ContextStoppedEvent", taskGroup),
                    new Task(null, "ContextStartedEvent", taskGroup)));
            taskGroupRepository.save(taskGroup);
        }
    }
}
