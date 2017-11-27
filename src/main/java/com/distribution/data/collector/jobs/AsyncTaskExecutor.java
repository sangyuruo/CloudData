package com.distribution.data.collector.jobs;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.event.ServerStatusEvent;
import com.distribution.data.collector.server.MasterListenerConnectionHandler;
import com.distribution.data.collector.server.TcpModbusServerListener;
import com.distribution.data.collector.type.Utils;
import com.distribution.modbus.protocol.exception.ModbusInitException;
import com.distribution.modbus.protocol.ip.IpParameters;
import com.distribution.modbus.protocol.ip.tcp.TcpMaster;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Scope("singleton")
@Service
public class AsyncTaskExecutor  implements ApplicationEventPublisherAware {
	private static Logger logger = LoggerFactory.getLogger(AsyncTaskExecutor.class);

	private ApplicationEventPublisher eventPublisher;

	@Inject
	private DataAcquisitionTask task;

	public DataAcquisitionTask getTask() {
		return task;
	}

	public TcpMaster getModbusMaster(IpParameters params, boolean keepAlive, int timeout, final ModbusServerManager manager, String id) throws ModbusInitException{
		TcpMaster mm = (TcpMaster)manager.getConnections().get(id);
		if(mm == null){
			mm = new TcpMaster(params, keepAlive);
			mm.setTimeout(timeout);
			mm.setRetries(0);
			if(keepAlive) {
                synchronized (manager.getConnections()) {
                    manager.getConnections().put(id, mm);
                }
            }
		}
		if(!mm.isInitialized() || !mm.isConnected() || mm.getConn() == null || mm.getSocket() == null){
			mm.init();
		}
		return mm;
	}

	public void setTask(DataAcquisitionTask task) {
		this.task = task;
	}

	@Timed
	@Async
    public void executeAsyncTask(final Server ss, final ModbusServerManager manager){
	    TcpModbusServerListener config = manager.getModbusServer().get(ss.getPort());
		StopWatch watch = new StopWatch();
		watch.start();
		LocalDateTime dt = LocalDateTime.now();
		boolean check = !Util.isSameDay(ss.getStatus().getCreateDate(), dt);
		if(!ss.isEnable()){
			Utils.updateStatus(ss, ModbusServerManager.STATUS_PENDING, check);
			eventPublisher.publishEvent(new ServerStatusEvent(ss.getStatus()));
			return;
		}
		if(ss.getModel() != null && ss.getModel() == Server.MODBUS_SERVER_MODEL_SLAVE){ //socket.getInetAddress().getHostName() + "@" + socket.getInetAddress().getHostAddress() + ":" + ipParameters.getPort()
			MasterListenerConnectionHandler handle = config.getMapConnections().get(ss.getCode());

			if(handle == null){
				handle = config.getMapConnections().get(Utils.getListenerKey(ss.getIp(), ss.getPort()));
				logger.debug("key=" + Utils.getListenerKey(ss.getIp(), ss.getPort()) + " MasterListenerConnectionHandler=" + (handle == null));
			}
			if(handle != null && handle.isConnected()){
				Utils.updateStatus(ss, ModbusServerManager.STATUS_OKEY, check);
				task.send(handle, ss, dt, check);
			}else{
				synchronized (config.getMapConnections()) {
					Utils.updateStatus(ss, ModbusServerManager.STATUS_BAD, check);
                    task.send(null, ss, dt, check);
//					config.getMapConnections().remove(ss.getCode());
//					config.getMapConnections().remove(Utils.getListenerKey(ss.getIp(), ss.getPort()));
		        }
			}
		}else if(ss.getModel() != null && ss.getModel() == Server.MODBUS_SERVER_MODEL_MASTER){
			TcpMaster master = null;
			try{
				master = getModbusMaster(ss.getIpParameters(), ss.isKeepAlive(), ss.getReplyTimeout(), manager, ss.getId().toString());
				if(master != null && master.isInitialized()){
					Utils.updateStatus(ss, ModbusServerManager.STATUS_OKEY, check);
					task.send(master, ss, dt, check);
				}else{
                    Utils.updateStatus(ss, ModbusServerManager.STATUS_BAD, check);
                    task.send(null, ss, dt, check);
                    synchronized (manager.getConnections()) {
                         manager.getConnections().remove(ss.getId().toString());
                    }
                }
			} catch (ModbusInitException e) {
				logger.error("连接异常： IP: " + ss.getIpParameters().getHost() + " , 端口： " + ss.getIpParameters().getPort() );
				synchronized (manager.getConnections()) {
					Utils.updateStatus(ss, ModbusServerManager.STATUS_BAD, check);
                    task.send(null, ss, dt, check);
					manager.getConnections().remove(ss.getId().toString());
				}
			}
		}else if(ss.getModel() != null && ss.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET){
		    if(ss.getStatus() != null){
//		        ss.getStatus().getLastUpdate()
//                long t = dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - ss.getStatus().getLastUpdate().toInstant().toEpochMilli();
//                if(t >= 30 * 1000){
                    //fixed 与 PollingServerRequestHandler会产生总次数变多的情况。
//                    check = !Util.isSameDay(ss.getStatus().getCreateDate(), dt);
                    Utils.updateServerStatusForTotal(ss, check);
                    task.send(null, ss, dt, check);
//                }
            }
        }
		eventPublisher.publishEvent(new ServerStatusEvent(ss.getStatus()));
		watch.stop();
	    logger.debug("采集完串口服务器{}，共花{}ms", ss.getHostname(), watch.getTotalTimeMillis());
    }




	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher =  applicationEventPublisher;
	}

}
