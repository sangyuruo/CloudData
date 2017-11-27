package com.distribution.data.collector.event;

import com.distribution.data.collector.data.TcpModbusResult;
import org.springframework.context.ApplicationEvent;

import java.util.List;


public class TcpModbusEvent extends ApplicationEvent {

	private static final long serialVersionUID = -9002441207429957514L;

	public TcpModbusEvent(List<TcpModbusResult> source) {
		super(source);
	}

}
