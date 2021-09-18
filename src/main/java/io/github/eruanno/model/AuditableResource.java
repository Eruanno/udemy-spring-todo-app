package io.github.eruanno.model;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class AuditableResource extends Resource {
    @Embedded
    private Audit audit = new Audit();
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
