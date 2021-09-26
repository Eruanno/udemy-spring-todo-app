package io.github.eruanno.logic;

import io.github.eruanno.TaskConfigurationProperties;
import io.github.eruanno.model.ProjectRepository;
import io.github.eruanno.model.TaskGroupRepository;
import io.github.eruanno.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties, final TaskGroupService taskGroupService) {
        return new ProjectService(projectRepository, taskGroupRepository, taskConfigurationProperties, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository taskGroupRepository, final TaskRepository taskRepository) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
