package com.distribution.data.collector.event;

import com.distribution.data.domain.SmartMeterStatus;
import org.springframework.context.ApplicationEvent;

import java.util.List;


public class MeterStatusEvent extends ApplicationEvent {

    private List<SmartMeterStatus> meterStatuses;

	public MeterStatusEvent(List<SmartMeterStatus> source) {
		super(source);
		this.meterStatuses = source;
	}

	public List<SmartMeterStatus> getMeterStatuses(){
	    return this.meterStatuses;
    }
}
