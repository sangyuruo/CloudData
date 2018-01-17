package com.distribution.data.collector.cassadra.entity;

import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.domain.ModbusServer;
import com.distribution.modbus.protocol.ip.IpParameters;

import java.util.List;

@Table(name = "modbusServer")
public class Server extends ModbusServer{
	public static final int MODBUS_SERVER_MODEL_MASTER = 1;
	public static final int MODBUS_SERVER_MODEL_SLAVE = 2;
	public static final int MODBUS_SERVER_MODEL_SOCKET = 3;
	private static final long serialVersionUID = 2622677934936388404L;

    @Transient
    private IpParameters ipParameters;
	public IpParameters getIpParameters(){
		if(ipParameters == null){
			ipParameters = new IpParameters();
			ipParameters.setHost(getIp());
			ipParameters.setPort(getPort());
			ipParameters.setEncapsulated(isEncapsulated());
		}
		return ipParameters;
	}
	@Transient
	private List<Meter> smartMeters;
	@Transient
	private List<TcpModbusRequest> tcpRequests;



	public List<Meter> getSmartMeters() {
		return smartMeters;
	}

	public void setSmartMeters(List<Meter> smartMeters) {
		this.smartMeters = smartMeters;
	}

	public List<TcpModbusRequest> getTcpRequests() {
		return tcpRequests;
	}

	public void setTcpRequests(List<TcpModbusRequest> tcpRequests) {
		this.tcpRequests = tcpRequests;
	}

}
