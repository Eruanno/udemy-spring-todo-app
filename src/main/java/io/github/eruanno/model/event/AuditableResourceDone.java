package io.github.eruanno.model.event;

import io.github.eruanno.model.AuditableResource;

import java.time.Clock;

public class AuditableResourceDone extends AuditableResourceEvent {
    AuditableResourceDone(final AuditableResource source) {
        super(source.getId(), source.getClass().getName(), Clock.systemDefaultZone());
    }
}
