package io.github.eruanno.model.projection;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private String description;
    /**
     * Deadline from the latest task in group.
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;
    private int id;

    public GroupReadModel(TaskGroup source) {
        description = source.getDescription();
        source.getTasks().stream().max(Comparator.comparing(Task::getDeadline))
                .ifPresent(task -> deadline = task.getDeadline());
        tasks = source.getTasks().stream().map(GroupTaskReadModel::new).collect(Collectors.toSet());
        id = source.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
