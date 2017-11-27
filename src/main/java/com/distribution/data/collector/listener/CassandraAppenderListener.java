package com.distribution.data.collector.listener;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.event.MeterDataEvent;
import com.distribution.data.repository.SmartMeterDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CassandraAppenderListener implements ApplicationListener<MeterDataEvent> {
	private static Logger logger = LoggerFactory.getLogger(CassandraAppenderListener.class);

	@Inject
	private SmartMeterDataRepository smartMeterDataRepository;

    @Timed
    @Override
    public void onApplicationEvent(MeterDataEvent meterDataEvent) {
        if(meterDataEvent.getRsdata() != null)
            smartMeterDataRepository.batchSave(meterDataEvent.getRsdata());
    }
}
