package io.github.eruanno.model.event;

import io.github.eruanno.model.AuditableResource;

import java.time.Clock;

public class AuditableResourceUndone extends AuditableResourceEvent {
    AuditableResourceUndone(final AuditableResource source) {
        super(source.getId(), source.getClass().getName(), Clock.systemDefaultZone());
    }
}
