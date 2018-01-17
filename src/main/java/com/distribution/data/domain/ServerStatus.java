package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A ServerStatus.
 */

@Table(name = "serverStatus")
public class ServerStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private String serverId;

    @ClusteringColumn
    private UUID id;

    private ZonedDateTime lastUpdate;

    private ZonedDateTime createDate;

    private Integer lastState;

    private Integer success;

    private Integer total;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServerStatus serverId(String serverId) {
    	this.serverId = serverId;
        return this;
    }

    public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public ServerStatus lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public ServerStatus createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getLastState() {
        return lastState;
    }

    public ServerStatus lastState(Integer lastState) {
        this.lastState = lastState;
        return this;
    }

    public void setLastState(Integer lastState) {
        this.lastState = lastState;
    }

    public Integer getSuccess() {
        return success;
    }

    public ServerStatus success(Integer success) {
        this.success = success;
        return this;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getTotal() {
        return total;
    }

    public ServerStatus total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerStatus serverStatus = (ServerStatus) o;
        if(serverStatus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serverStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void initialize(){
    	this.lastUpdate(ZonedDateTime.now())
    	.lastState(-1)
    	.success(0)
    	.total(0)
    	.createDate(ZonedDateTime.now())
    	.setId(null);
    }

    @Override
    public String toString() {
        return "ServerStatus{" +
            "id=" + id +
            ", lastUpdate='" + lastUpdate + "'" +
            ", createDate='" + createDate + "'" +
            ", lastState='" + lastState + "'" +
            ", success='" + success + "'" +
            ", total='" + total + "'" +
            '}';
    }
}
