package com.distribution.data.collector.event;

import com.distribution.data.domain.ServerStatus;
import org.springframework.context.ApplicationEvent;

public class ServerStatusEvent extends ApplicationEvent {

	private static final long serialVersionUID = -9002441207429957514L;

	public ServerStatusEvent(ServerStatus source) {
		super(source);
	}

}
