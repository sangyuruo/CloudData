package com.distribution.data.service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the SmartMeterData entity.
 */
public class SmartMeterDataDTO implements Serializable {

    private UUID id;
    private UUID meterId;

    private int ymd;
    private int hour;
    private int minute;
    private int sec;

    private Map<String, String> auxiliary;
    private Map<String, Float> data;


    public UUID getMeterId() {
		return meterId;
	}

	public void setMeterId(UUID meterId) {
		this.meterId = meterId;
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

	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Map<String, Float> getData() {
        return data;
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

        SmartMeterDataDTO smartMeterDataDTO = (SmartMeterDataDTO) o;

        if ( ! Objects.equals(id, smartMeterDataDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SmartMeterDataDTO{" +
            "id=" + id +
            ", meterId='" + meterId + "'" +
            ", data='" + data + "'" +
            '}';
    }

    public Map<String, String> getAuxiliary() {
        return auxiliary;
    }

    public void setAuxiliary(Map<String, String> auxiliary) {
        this.auxiliary = auxiliary;
    }
}
