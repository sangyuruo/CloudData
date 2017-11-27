package com.distribution.data.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the ServerStatus entity.
 */
public class ServerStatusDTO implements Serializable {

    private UUID id;

    private UUID serverId;

    private ZonedDateTime lastUpdate;

    private ZonedDateTime createDate;

    private Integer lastState;

    private Integer success;

    private Integer total;


    public UUID getServerId() {
		return serverId;
	}

	public void setServerId(UUID serverId) {
		this.serverId = serverId;
	}

	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }
    public Integer getLastState() {
        return lastState;
    }

    public void setLastState(Integer lastState) {
        this.lastState = lastState;
    }
    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
    public Integer getTotal() {
        return total;
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

        ServerStatusDTO serverStatusDTO = (ServerStatusDTO) o;

        if ( ! Objects.equals(id, serverStatusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServerStatusDTO{" +
            "id=" + id +
            ", lastUpdate='" + lastUpdate + "'" +
            ", createDate='" + createDate + "'" +
            ", lastState='" + lastState + "'" +
            ", success='" + success + "'" +
            ", total='" + total + "'" +
            '}';
    }
}
