package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A SmartMeterStatus.
 */

@Table(name = "smartMeterStatus")
public class SmartMeterStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;


    @ClusteringColumn
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

//    @Transient
    private Float volume;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public SmartMeterStatus meterId(UUID meterId) {
        this.meterId = meterId;
        return this;
    }

    public UUID getMeterId() {
		return meterId;
	}

	public void setMeterId(UUID meterId) {
		this.meterId = meterId;
	}

	public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public SmartMeterStatus lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public SmartMeterStatus createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getLastState() {
        return lastState;
    }

    public SmartMeterStatus lastState(Integer lastState) {
        this.lastState = lastState;
        return this;
    }

    public void setLastState(Integer lastState) {
        this.lastState = lastState;
    }

    public ZonedDateTime getLastRetryDate() {
        return lastRetryDate;
    }

    public SmartMeterStatus lastRetryDate(ZonedDateTime lastRetryDate) {
        this.lastRetryDate = lastRetryDate;
        return this;
    }

    public void setLastRetryDate(ZonedDateTime lastRetryDate) {
        this.lastRetryDate = lastRetryDate;
    }

    public Integer getLastRetryLevel() {
        return lastRetryLevel;
    }

    public SmartMeterStatus lastRetryLevel(Integer lastRetryLevel) {
        this.lastRetryLevel = lastRetryLevel;
        return this;
    }

    public void setLastRetryLevel(Integer lastRetryLevel) {
        this.lastRetryLevel = lastRetryLevel;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public SmartMeterStatus retryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getSuccess() {
        return success;
    }

    public SmartMeterStatus success(Integer success) {
        this.success = success;
        return this;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getTotal() {
        return total;
    }

    public SmartMeterStatus total(Integer total) {
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
        SmartMeterStatus smartMeterStatus = (SmartMeterStatus) o;
        if(smartMeterStatus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, smartMeterStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void initialize(){
    	this.lastUpdate(ZonedDateTime.now())
    	.lastState(-1)
        .createDate(null)
    	.lastRetryDate(null)
    	.lastRetryLevel(0)
    	.retryCount(0)
    	.success(0)
        .switchStatus(-1)
    	.total(0)
    	.setId(null);
    }

    @Override
    public String toString() {
        return "SmartMeterStatus{" +
            "id=" + id +
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

    public SmartMeterStatus switchStatus(Integer switchStatus){
        this.switchStatus = switchStatus;
        return this;
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

    public SmartMeterStatus volume(Float volume) {
        this.volume = volume;
        return this;
    }
}
