package io.github.eruanno.reports;

import io.github.eruanno.model.event.AuditableResourceEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "resource_events")
class PersistedAuditableResourceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int resourceId;
    String classname;
    LocalDateTime occurrence;
    String name;

    public PersistedAuditableResourceEvent() {
    }

    PersistedAuditableResourceEvent(AuditableResourceEvent source) {
        resourceId = source.getResourceId();
        classname = source.getClassname();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }
}
