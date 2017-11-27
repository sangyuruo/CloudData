package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Table(name = "AppAudit")
public class PersistentAuditEvent {

    @ClusteringColumn
    private UUID id;

    @NotNull
    @PartitionKey
    private String principal;

    @Column(name = "event_date")
    private ZonedDateTime auditEventDate;

    @Column(name = "event_type")
    private String auditEventType;

    @Column(name = "event_data")
    private Map<String, String> data = new HashMap<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public ZonedDateTime getAuditEventDate() {
        return auditEventDate;
    }

    public void setAuditEventDate(ZonedDateTime auditEventDate) {
        this.auditEventDate = auditEventDate;
    }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
