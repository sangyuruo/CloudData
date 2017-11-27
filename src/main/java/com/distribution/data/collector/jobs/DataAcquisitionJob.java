package com.distribution.data.collector.jobs;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class DataAcquisitionJob {
	Logger logger = LoggerFactory.getLogger( DataAcquisitionJob.class );
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
		for (Server ss : config.getMServer()) {
			logger.debug( "the server{} job start" , ss.getHostname() );
			taskExecutor.executeAsyncTask(ss,  config);
		}
	}

}
