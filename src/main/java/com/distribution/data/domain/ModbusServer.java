package com.distribution.data.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A ModbusSlave.
 */

@Table(name = "modbusServer")
@Document(indexName = "modbusserver")
public class ModbusServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    @ClusteringColumn
    private UUID companyId;

    private String code;
    @Transient
    private Company company;

    @Transient
    private ServerStatus status;

    @NotNull
    @Size(max = 64)
    private String hostname;

    @NotNull
    private String ip;
    private Boolean enable;
    @NotNull
    private Integer port;

    @NotNull
    @Max(value = 5)
    private Integer model;

    private Boolean encapsulated;

    private Boolean keepAlive;

    private Integer requestTimeout;

    private Integer replyTimeout;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getHostname() {
        return hostname;
    }

    public ModbusServer hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
    public Boolean isEnable() {
        return enable;
    }

    public ModbusServer enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ModbusServer status(ServerStatus status) {
    	this.status = status;
        return this;
    }
    public ServerStatus getStatus() {
		return status;
	}

	public void setStatus(ServerStatus status) {
		this.status = status;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	public ModbusServer companyName(UUID companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ModbusServer ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public ModbusServer port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getModel() {
        return model;
    }

    public ModbusServer model(Integer model) {
        this.model = model;
        return this;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Boolean isEncapsulated() {
        return encapsulated;
    }

    public ModbusServer encapsulated(Boolean encapsulated) {
        this.encapsulated = encapsulated;
        return this;
    }

    public void setEncapsulated(Boolean encapsulated) {
        this.encapsulated = encapsulated;
    }

    public Boolean isKeepAlive() {
        return keepAlive;
    }

    public ModbusServer keepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public ModbusServer requestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getReplyTimeout() {
        return replyTimeout;
    }

    public ModbusServer replyTimeout(Integer replyTimeout) {
        this.replyTimeout = replyTimeout;
        return this;
    }

    public void setReplyTimeout(Integer replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModbusServer modbusServer = (ModbusServer) o;
        if(modbusServer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, modbusServer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ModbusSlave{" +
            "id=" + id +
            ", hostname='" + hostname + "'" +
            ", ip='" + ip + "'" +
            ", code=" + code +
            ", port='" + port + "'" +
            ", model='" + model + "'" +
            ", encapsulated='" + encapsulated + "'" +
            ", keepAlive='" + keepAlive + "'" +
            ", requestTimeout='" + requestTimeout + "'" +
            ", replyTimeout='" + replyTimeout + "'" +
            '}';
    }

    public ModbusServer code(String code){
       this.code = code;
       return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
