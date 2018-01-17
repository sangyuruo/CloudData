package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A SmartMeterData.
 */

@Table(name = "smartMeterData")
public class SmartMeterData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ClusteringColumn(0)
    private UUID id;

    @PartitionKey
    private Long meterId;
    @ClusteringColumn(1)
    private UUID companyId;
    private String serverId;
    private String hostname;
    private String serverCode;
    private String ip;
    private String name;
    private int code;
    private int category;
    private int ymd;
    private int hour;
    private int minute;
    private int sec;

    private Map<String, String> auxiliary;
    private Map<String, Float> data;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SmartMeterData meterId(Long meterId) {
        this.meterId = meterId;
        return this;
    }

    public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}
    public Map<String, Float> getData() {
        return data;
    }

    public SmartMeterData data(Map<String, Float> data) {
        this.data = data;
        return this;
    }

    public void setData(Map<String, Float> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmartMeterData smartMeterData = (SmartMeterData) o;
        if(smartMeterData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, smartMeterData.id);
    }

    public int getYmd() {
		return ymd;
	}

	public void setYmd(int ymd) {
		this.ymd = ymd;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
    public String toString() {
        return "SmartMeterData{" +
            "id=" + id +
            ", data='" + data + "'" +
            '}';
    }

    public String getServerId() {
        return serverId;
    }

    public SmartMeterData serverId(String serverId){
        this.serverId = serverId;
        return this;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerCode() {
        return serverCode;
    }

    public SmartMeterData serverCode(String serverCode){
        this.serverCode = serverCode;
        return this;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public SmartMeterData companyId(UUID companyId){
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public SmartMeterData name(String name){
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAuxiliary() {
        return auxiliary;
    }

    public void setAuxiliary(Map<String, String> auxiliary) {
        this.auxiliary = auxiliary;
    }

    public SmartMeterData auxiliary(Map<String, String> auxiliary) {
        this.auxiliary = auxiliary;
        return this;
    }
}
