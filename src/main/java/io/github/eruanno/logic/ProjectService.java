package io.github.eruanno.logic;

import io.github.eruanno.TaskConfigurationProperties;
import io.github.eruanno.model.*;
import io.github.eruanno.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties taskConfigurationProperties;

    ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project create(Project source) {
        return projectRepository.save(source);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!taskConfigurationProperties.getTemplate()
                .isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        TaskGroup result = projectRepository.findById(projectId).map(project -> {
            var targetGroup = new TaskGroup();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(project.getSteps().stream().map(projectStep ->
                    new Task(deadline.plusDays(projectStep.getDaysToDeadline()), projectStep.getDescription())
            ).collect(Collectors.toSet()));
            targetGroup.setProject(project);
            return taskGroupRepository.save(targetGroup);
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
