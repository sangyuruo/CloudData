package com.distribution.data.collector.data;



public class TcpModbusResult {
    private TcpModbusRequest request;
    private TcpModbusResponse response;

	public TcpModbusRequest getRequest() {
		return request;
	}
	public void setRequest(TcpModbusRequest request) {
		this.request = request;
	}
	public TcpModbusResponse getResponse() {
		return response;
	}
	public void setResponse(TcpModbusResponse response) {
		this.response = response;
	}

}
