package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import io.swagger.annotations.ApiModel;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * not an ignored comment
 *
 */
@ApiModel(description = "not an ignored comment")
@Table(name = "smartMeter")
@Document(indexName = "smartmeter")
public class SmartMeter implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
      private String id;


    @ClusteringColumn(0)
    private String serverId;

    private String companyId;

    @Transient
    private ModbusServer server;
    @Transient
    private SmartMeterStatus status;

    @ClusteringColumn(1)
    private Integer code;

    private Long longcode;

    private int func=3;

    private Boolean allowDuplicate = false;

    @NotNull
    @Size(max = 128)
    private String name;

    private Integer category;

    private Double longitude;

    private Double latitude;

    private Boolean enable;

    private Boolean bigEndian;

    private Integer startOffset;

    private Integer numberOfRegisters;

    private Map<String, String> dataTypes;

    private Integer controlAddress;

    public ModbusServer getServer() {
		return server;
	}

	public void setServer(ModbusServer server) {
		this.server = server;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerId() {
		return serverId;
	}

    public SmartMeter serverId(String serverId) {
    	this.serverId = serverId;
        return this;
    }

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public SmartMeter serverId(SmartMeterStatus status) {
		this.status = status;
		return this;
	}


	public SmartMeterStatus getStatus() {
		return status;
	}

	public void setStatus(SmartMeterStatus status) {
		this.status = status;
	}

	public Integer getCode() {
        return code;
    }

    public SmartMeter code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public SmartMeter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public SmartMeter category(Integer category) {
        this.category = category;
        return this;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Double getLongitude() {
        return longitude;
    }

    public SmartMeter longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public SmartMeter latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean isEnable() {
        return enable;
    }

    public SmartMeter enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean isBigEndian() {
        return bigEndian;
    }

    public SmartMeter bigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
        return this;
    }

    public void setBigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public SmartMeter startOffset(Integer startOffset) {
        this.startOffset = startOffset;
        return this;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public SmartMeter numberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
        return this;
    }

    public void setNumberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }

    public Map<String, String> getDataTypes() {
        return dataTypes;
    }

    public SmartMeter dataTypes(Map<String, String> dataTypes) {
        this.dataTypes = dataTypes;
        return this;
    }

    public void setDataTypes(Map<String, String> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public Integer getControlAddress() {
        return controlAddress;
    }

    public SmartMeter controlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
        return this;
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
        SmartMeter smartMeter = (SmartMeter) o;
        if(smartMeter.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, smartMeter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SmartMeter{" +
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

    public SmartMeter companyId(String companyId){
        this.companyId = companyId;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Long getLongcode() {
        return longcode;
    }

    public void setLongcode(Long longcode) {
        this.longcode = longcode;
    }

    public SmartMeter longcode(Long longcode){
        this.longcode = longcode;
        return this;
    }

    public int getFunc(){
        return this.func <= 0 ? 3 : this.func;
    }

    public void setFunc(int function){
        this.func = function;
    }

    public SmartMeter function(int function){
        this.func = function;
        return this;
    }

    public SmartMeter allowDuplicate(Boolean allowDuplicate){
       this.allowDuplicate = allowDuplicate;
       return this;
    }

    public Boolean getAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(Boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }
}
