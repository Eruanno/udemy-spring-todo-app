package io.github.eruanno.logic;

import io.github.eruanno.model.Project;
import io.github.eruanno.model.TaskGroup;
import io.github.eruanno.model.TaskGroupRepository;
import io.github.eruanno.model.TaskRepository;
import io.github.eruanno.model.projection.GroupReadModel;
import io.github.eruanno.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository taskGroupRepository, final TaskRepository taskRepository) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first.");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found."));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}
