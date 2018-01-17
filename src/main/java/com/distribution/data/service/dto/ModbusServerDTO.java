package com.distribution.data.service.dto;

import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.domain.Company;
import com.distribution.data.domain.ServerStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;


/**
 * A DTO for the ModbusSlave entity.
 */
public class ModbusServerDTO implements Serializable {
    private String id;

    @NotNull
    @Size(max = 64)
    private String hostname;
    @NotNull
    private UUID companyId;

    private Company company;

    @NotNull
    @Max(65535)
    @Min(1)
    private String code;

    @NotNull
    private String ip;

    @NotNull
    @Max(65535)
    @Min(1024)
    private Integer port;

    @NotNull
    @Max(value = 5)
    private Integer model;

    private ServerStatus status;

    private Boolean encapsulated;
    private Boolean enable;

    private Boolean keepAlive;

    @Max(200)
    @Min(50)
    private Integer requestTimeout;

    @Min(500)
    @Max(2000)
    private Integer replyTimeout;

	public Company getCompany() {
		return company;
	}

    public ServerStatus getStatus() {
		return status;
	}

	public void setStatus(ServerStatus status) {
		this.status = status;
	}

	public Map<Integer, String> getModelNames(){
    	Map<Integer, String> map = new HashMap<Integer, String>();
    	map.put(Server.MODBUS_SERVER_MODEL_MASTER, "Master");
		map.put(Server.MODBUS_SERVER_MODEL_SLAVE, "Slave");
		map.put(Server.MODBUS_SERVER_MODEL_SOCKET, "Socket");
    	return map;
    }

    public List<Object> getMeterCategory(){
	    List<Object> im = new ArrayList<>();
        im.addAll(ModbusServerManager.meterParser.values());
	    return im;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
	public void setCompany(Company company) {
		this.companyId = company.getId();
		this.company = company;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }
    public Boolean getEncapsulated() {
        return encapsulated;
    }

    public void setEncapsulated(Boolean encapsulated) {
        this.encapsulated = encapsulated;
    }
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
    public Integer getReplyTimeout() {
        return replyTimeout;
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

        ModbusServerDTO modbusServerDTO = (ModbusServerDTO) o;

        if ( ! Objects.equals(id, modbusServerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ModbusServerDTO{" +
            "id=" + id +
            ", hostname='" + hostname + "'" +
            ", companyId='" + companyId + "'" +
            ", ip='" + ip + "'" +
            ", port='" + port + "'" +
            ", model='" + model + "'" +
            ", encapsulated='" + encapsulated + "'" +
            ", keepAlive='" + keepAlive + "'" +
            ", requestTimeout='" + requestTimeout + "'" +
            ", replyTimeout='" + replyTimeout + "'" +
            '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
