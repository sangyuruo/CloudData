package com.distribution.data.service.dto;

import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.domain.ModbusServer;
import com.distribution.data.domain.SmartMeterStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the SmartMeter entity.
 */
public class SmartMeterDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(1)
    private Integer code;

    private Long longcode;

    @NotNull
    private UUID serverId;
    private UUID companyId;
    private ModbusServer server;

    @NotNull
    @Size(max = 128)
    private String name;
    @NotNull
    private Integer category;

    private Double longitude;

    private Double latitude;

    private Boolean enable;

    private Boolean bigEndian;
    @Min(0)
    private Integer startOffset;
    @Max(256)
    private Integer numberOfRegisters;

    private Integer func;

    private Map<String, String> dataTypes;

    private Boolean allowDuplicate;

    private Integer controlAddress;

    private SmartMeterStatus status;

    public SmartMeterStatus getStatus() {
		return status;
	}

	public void setStatus(SmartMeterStatus status) {
		this.status = status;
	}

	public Map<Integer, String> getCategoryName(){
    	Map<Integer, String> map = new HashMap<Integer, String>();
        for (Integer key : ModbusServerManager.meterParser.keySet()) {
            map.put(key, ModbusServerManager.meterParser.get(key).getName());
        }
    	return map;
    }

    public UUID getServerId() {
		return serverId;
	}

	public void setServerId(UUID serverId) {
		this.serverId = serverId;
	}

	public ModbusServer getServer() {
		return server;
	}

	public void setServer(ModbusServer server) {
		this.server = server;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    public Boolean getBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
    }
    public Integer getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }
    public Integer getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public void setNumberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }
    public Map<String, String> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(Map<String, String> dataTypes) {
        this.dataTypes = dataTypes;
    }
    public Integer getControlAddress() {
        return controlAddress;
    }

    public void setControlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmartMeterDTO smartMeterDTO = (SmartMeterDTO) o;

        if ( ! Objects.equals(id, smartMeterDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SmartMeterDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", category='" + category + "'" +
            ", longitude='" + longitude + "'" +
            ", latitude='" + latitude + "'" +
            ", enable='" + enable + "'" +
            ", bigEndian='" + bigEndian + "'" +
            ", startOffset='" + startOffset + "'" +
            ", numberOfRegisters='" + numberOfRegisters + "'" +
            ", dataTypes='" + dataTypes + "'" +
            ", controlAddress='" + controlAddress + "'" +
            '}';
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public Long getLongcode() {
        return longcode;
    }

    public void setLongcode(Long longcode) {
        this.longcode = longcode;
    }

    public String getCalculates() {
	    StringBuffer s = new StringBuffer();
	    if(dataTypes != null){
	        int i = 1;
            for (String s1 : dataTypes.keySet()) {
                s.append(s1).append(":").append(dataTypes.get(s1));
                if(i < dataTypes.size()){
                    s.append(";");
                }
                i++;
            }
        }
        return s.toString();
    }

    public void setCalculates(String calculates) {
	    if(calculates != null && calculates.length() > 0) {
            String[] sb = calculates.split(";");
            Map<String, String> dt = new HashMap<>();
            for (String s : sb) {
                String[] data = s.split(":");
                dt.put(data[0], data[1]);
            }
            this.dataTypes = dt;
        }
    }

    public int getFunc(){
	    return this.func;
    }
    public void setFunc(int func){
        this.func = func;
    }

    public Boolean getAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(Boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }
}
