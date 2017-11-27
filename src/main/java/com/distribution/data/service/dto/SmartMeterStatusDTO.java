package com.distribution.data.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the SmartMeterStatus entity.
 */
public class SmartMeterStatusDTO implements Serializable {

    private UUID id;
    private UUID meterId;


    private ZonedDateTime lastUpdate;

    private ZonedDateTime createDate;

    private Integer lastState;

    private ZonedDateTime lastRetryDate;

    private Integer lastRetryLevel;

    private Integer retryCount;

    private Integer success;

    private Integer total;

    private Integer switchStatus;

    private Float volume;

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
    public ZonedDateTime getLastRetryDate() {
        return lastRetryDate;
    }

    public void setLastRetryDate(ZonedDateTime lastRetryDate) {
        this.lastRetryDate = lastRetryDate;
    }
    public Integer getLastRetryLevel() {
        return lastRetryLevel;
    }

    public void setLastRetryLevel(Integer lastRetryLevel) {
        this.lastRetryLevel = lastRetryLevel;
    }
    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
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

        SmartMeterStatusDTO smartMeterStatusDTO = (SmartMeterStatusDTO) o;

        if ( ! Objects.equals(id, smartMeterStatusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SmartMeterStatusDTO{" +
            "id=" + id +
            ", meterId='" + meterId + "'" +
            ", lastUpdate='" + lastUpdate + "'" +
            ", createDate='" + createDate + "'" +
            ", lastState='" + lastState + "'" +
            ", lastRetryDate='" + lastRetryDate + "'" +
            ", lastRetryLevel='" + lastRetryLevel + "'" +
            ", retryCount='" + retryCount + "'" +
            ", success='" + success + "'" +
            ", total='" + total + "'" +
            '}';
    }

    public Integer getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(Integer switchStatus) {
        this.switchStatus = switchStatus;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }
}
