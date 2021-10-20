package io.github.eruanno.reports;

import io.github.eruanno.model.event.AuditableResourceDone;
import io.github.eruanno.model.event.AuditableResourceEvent;
import io.github.eruanno.model.event.AuditableResourceUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
class ChangedAuditableResourceEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedAuditableResourceEventListener.class);
    private final PersistedAuditableResourceEventRepository repository;

    ChangedAuditableResourceEventListener(final PersistedAuditableResourceEventRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(AuditableResourceDone event) {
        onChanged(event);
    }

    @Async
    @EventListener
    public void on(AuditableResourceUndone event) {
        onChanged(event);
    }

    private void onChanged(final AuditableResourceEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedAuditableResourceEvent(event));
    }
}
