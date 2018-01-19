package com.distribution.data.collector.jobs;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.ModbusServerManagerEvent;
import com.distribution.data.collector.data.TcpModbusResult;
import com.distribution.data.collector.event.TcpModbusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DataAcquisitionJob implements GenericApplicationListener {
	Logger logger = LoggerFactory.getLogger( DataAcquisitionJob.class );
	boolean ready = false;

	public DataAcquisitionJob(){
	}

	@Autowired
	private AsyncTaskExecutor taskExecutor;

	public AsyncTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Autowired
	private ModbusServerManager config;

	@Autowired
	private DataAcquisitionTask task;

	public DataAcquisitionTask getTask() {
		return task;
	}

	public void setTask(DataAcquisitionTask task) {
		this.task = task;
	}

	@Scheduled(cron = "${jobs.task.cron}")
    @Timed
	public void work(){
		logger.debug( "job start" );
		if( ready ) {
            logger.debug( "job start ready" );
            for (Server ss : config.getMServer()) {
                logger.debug("the server{} job start", ss.getHostname());
                taskExecutor.executeAsyncTask(ss, config);
            }
        }
	}



    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        Class<?> type = eventType.getRawClass();
        return ModbusServerManagerEvent.class.isAssignableFrom(type);
    }


    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        String result= (String) event.getSource();
        this.ready = true;
    }


}
