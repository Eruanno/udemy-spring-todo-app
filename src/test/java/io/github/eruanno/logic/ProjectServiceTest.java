package io.github.eruanno.logic;

import io.github.eruanno.TaskConfigurationProperties;
import io.github.eruanno.model.*;
import io.github.eruanno.model.projection.GroupReadModel;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and undone group exist.")
    void createGroup_noMultipleGroupsConfig_and_undoneGroupExists_throwsIllegalStateException() {
        // Arrange
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        var mockConfig = configurationReturning(false);
        var sut = new ProjectService(null, mockGroupRepository, mockConfig, null);

        // Act
        var exception = catchThrowable(() -> sut.createGroup(LocalDateTime.now(), 0));

        // Assert
        assertThat(exception).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured ok and no projects for a given id.")
    void createGroup_configurationOk_and_noProjects_throwsIllegalArgumentException() {
        // Arrange
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        var mockConfig = configurationReturning(true);
        var sut = new ProjectService(mockProjectRepository, null, mockConfig, null);

        // Act
        var exception = catchThrowable(() -> sut.createGroup(LocalDateTime.now(), 0));

        // Assert
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Project with");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just one group and no group and projects for a given id.")
    void createGroup_noMultipleGroupsConfig_and_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // Arrange
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        var mockConfig = configurationReturning(true);
        var sut = new ProjectService(mockProjectRepository, mockGroupRepository, mockConfig, null);

        // Act
        var exception = catchThrowable(() -> sut.createGroup(LocalDateTime.now(), 0));

        // Assert
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Project with");
    }

    @Test
    @DisplayName("should create a new group from project.")
    void createGroup_configurationOk_existingProject_createAndSaveGroup() {
        // Arrange
        var today = LocalDate.now().atStartOfDay();
        var mockProjectRepository = mock(ProjectRepository.class);
        var project = projectWith("bar", Set.of(-1, -2));
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        var mockConfig = configurationReturning(true);
        var taskGroupRepository = inMemoryGroupRepository();
        var serviceWithInMemRepo = new TaskGroupService(taskGroupRepository, null);
        var sut = new ProjectService(mockProjectRepository, taskGroupRepository, mockConfig, serviceWithInMemRepo);

        // Act
        GroupReadModel result = sut.createGroup(today, 1);

        // Assert
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(taskGroupRepository.findAll()).hasSize(1);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDealine) {
        var steps = daysToDealine.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean b) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(b);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private TaskGroupRepository inMemoryGroupRepository() {
        return new TaskGroupRepository() {
            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return Lists.newArrayList(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(final Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(final TaskGroup entity) {
                if (entity.getId() == 0) {
                    try {
                        var field = TaskGroup.class.getSuperclass().getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
                return map.values().stream().filter(group -> !group.isDone())
                        .anyMatch(g -> g.getProject() != null && g.getProject().getId() == projectId);
            }

            @Override
            public boolean existsByDescription(final String description) {
                return map.values().stream().anyMatch(group -> group.getDescription().equals(description));
            }
        };
    }
}