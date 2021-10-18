package io.github.eruanno.logic;

import io.github.eruanno.TaskConfigurationProperties;
import io.github.eruanno.model.Project;
import io.github.eruanno.model.ProjectRepository;
import io.github.eruanno.model.TaskGroupRepository;
import io.github.eruanno.model.projection.GroupReadModel;
import io.github.eruanno.model.projection.GroupTaskWriteModel;
import io.github.eruanno.model.projection.GroupWriteModel;
import io.github.eruanno.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties taskConfigurationProperties;
    private final TaskGroupService taskGroupService;

    ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties, final TaskGroupService taskGroupService) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!taskConfigurationProperties.getTemplate()
                .isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        return projectRepository.findById(projectId).map(project -> {
            var targetGroup = new GroupWriteModel();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(project.getSteps().stream().map(projectStep -> {
                        var task = new GroupTaskWriteModel();
                        task.setDescription(projectStep.getDescription());
                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                        return task;
                    }
            ).collect(Collectors.toSet()));
            return taskGroupService.createGroup(targetGroup, project);
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
