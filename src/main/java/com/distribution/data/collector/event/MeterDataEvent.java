package com.distribution.data.collector.event;

import com.distribution.data.domain.SmartMeterData;
import org.springframework.context.ApplicationEvent;

import java.util.List;


public class MeterDataEvent extends ApplicationEvent {

    private List<SmartMeterData> rsdata;

	public MeterDataEvent(List<SmartMeterData> source) {
		super(source);
		this.rsdata = source;
	}

	public List<SmartMeterData> getRsdata(){
	    return this.rsdata;
    }
}
