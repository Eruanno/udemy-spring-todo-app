package io.github.eruanno.model;

import io.github.eruanno.model.event.AuditableResourceEvent;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AuditableResource extends Resource {
    @Embedded
    private Audit audit = new Audit();
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public AuditableResourceEvent toggle() {
        this.done = !done;
        return AuditableResourceEvent.changed(this);
    }
}
