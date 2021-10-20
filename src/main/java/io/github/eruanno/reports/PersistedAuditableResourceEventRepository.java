package io.github.eruanno.reports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PersistedAuditableResourceEventRepository extends JpaRepository<PersistedAuditableResourceEvent, Integer> {
    List<PersistedAuditableResourceEvent> findByResourceId(int resourceId);
}
