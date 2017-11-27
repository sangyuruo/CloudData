package com.distribution.data.collector.data;

import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.modbus.protocol.ip.IpParameters;
import com.distribution.modbus.protocol.msg.ModbusRequest;

import java.io.Serializable;


public class TcpModbusRequest implements Serializable{

	private static final long serialVersionUID = -3429099502488293294L;

    private ModbusRequest request;

	private Meter meter;
	private String name;
	private IpParameters ipParams;

//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

	public Meter getMeter() {
		return meter;
	}
	public void setMeter(Meter meter) {
		this.meter = meter;
	}
	public IpParameters getIpParams() {
		return ipParams;
	}
	public void setIpParams(IpParameters ipParams) {
		this.ipParams = ipParams;
	}

    public ModbusRequest getRequest() {
        return request;
    }

    public void setRequest(ModbusRequest request) {
        this.request = request;
    }
}
