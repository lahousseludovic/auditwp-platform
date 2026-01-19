package com.auditwp.platform.repository;

import com.auditwp.platform.entity.AuditJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuditJobRepository
        extends JpaRepository<AuditJobEntity, UUID> {
}
