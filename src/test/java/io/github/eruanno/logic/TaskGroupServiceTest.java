package io.github.eruanno.logic;

import io.github.eruanno.model.TaskGroup;
import io.github.eruanno.model.TaskGroupRepository;
import io.github.eruanno.model.TaskRepository;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Group has undo tasks.")
    void toggleGroup_groupHasUndoneTasks_throwsIllegalStateException() {
        // Arrange.
        TaskRepository mockTaskRepository = getTaskRepository(true);
        TaskGroupService sut = new TaskGroupService(null, mockTaskRepository);

        // Act.
        Throwable throwable = catchThrowable(() -> sut.toggleGroup(1));

        // Assert.
        assertThat(throwable).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group has undone tasks. Done all the tasks first.");
    }

    @Test
    @DisplayName("Task group not exist.")
    void toggleGroup_taskGroupNotExists_throwsIllegalArgumentException() {
        // Arrange.
        TaskGroupRepository mockTaskGroupRepository = getTaskGroupRepository(Optional.empty());
        TaskRepository mockTaskRepository = getTaskRepository(false);
        TaskGroupService sut = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // Act.
        Throwable throwable = catchThrowable(() -> sut.toggleGroup(1));

        // Assert.
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TaskGroup with given id not found.");
    }

    @Test
    @DisplayName("Toggles group.")
    void toggleGroup_togglesGroup() {
        // Arrange.
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.toggle();
        TaskGroupRepository mockTaskGroupRepository = getTaskGroupRepository(Optional.of(taskGroup));
        TaskRepository mockTaskRepository = getTaskRepository(false);
        TaskGroupService sut = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // Act.
        sut.toggleGroup(1);

        // Assert.
        verify(mockTaskGroupRepository).save(taskGroup);
        assertThat(taskGroup.isDone()).isTrue();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private TaskGroupRepository getTaskGroupRepository(Optional<TaskGroup> maybeTaskGroup) {
        TaskGroupRepository mockTaskGroupRepository = spy(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(maybeTaskGroup);
        return mockTaskGroupRepository;
    }

    private TaskRepository getTaskRepository(final boolean b) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return mockTaskRepository;
    }

    @Test
    void additions() {
        int number = 21;
        Set<String> results = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                for (int k = 1; k <= 9; k++) {
                    for (int l = 1; l <= 9; l++) {
                        for (int m = 1; m <= 9; m++) {
                            if (Sets.newLinkedHashSet(i, j, k, l, m).size() == 5) {
                                if (i + j + k + l + m == number) {
                                    List<Integer> result = Lists.list(i, j, k, l, m);
                                    result.sort(Comparator.naturalOrder());
                                    String key = result.stream().map(String::valueOf)
                                            .collect(Collectors.joining(","));
                                    results.add(key);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(results.size());
        System.out.println(results.stream().map(String::valueOf)
                .collect(Collectors.joining("\n")));
    }
}