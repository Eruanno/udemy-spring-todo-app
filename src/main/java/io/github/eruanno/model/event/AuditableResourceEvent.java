package io.github.eruanno.model.event;

import io.github.eruanno.model.AuditableResource;

import java.time.Clock;
import java.time.Instant;

public abstract class AuditableResourceEvent {
    private int resourceId;
    private Instant occurrence;
    private String classname;

    AuditableResourceEvent(final int taskId, String classname, Clock clock) {
        this.resourceId = taskId;
        this.classname = classname;
        this.occurrence = Instant.now(clock);
    }

    public int getResourceId() {
        return resourceId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    public String getClassname() {
        return classname;
    }

    public static AuditableResourceEvent changed(AuditableResource source) {
        return source.isDone() ? new AuditableResourceDone(source) : new AuditableResourceUndone(source);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "classname=" + classname +
                ", resourceId=" + resourceId +
                ", occurrence=" + occurrence +
                '}';
    }
}
