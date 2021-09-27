package io.github.eruanno.model.projection;

import io.github.eruanno.model.Task;

import java.time.LocalDateTime;

public class GroupTaskReadModel {
    private String description;
    private boolean done;
    private LocalDateTime deadline;

    GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
        deadline = source.getDeadline();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
