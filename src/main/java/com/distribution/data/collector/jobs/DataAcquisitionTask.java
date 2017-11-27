package com.distribution.data.collector.jobs;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.collector.data.TcpModbusResponse;
import com.distribution.data.collector.data.TcpModbusResult;
import com.distribution.data.collector.error.RetryLevel;
import com.distribution.data.collector.event.TcpModbusEvent;
import com.distribution.data.collector.type.Utils;
import com.distribution.modbus.protocol.ModbusMaster;
import com.distribution.modbus.protocol.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class DataAcquisitionTask  implements ApplicationEventPublisherAware {

	private static Logger logger = LoggerFactory.getLogger(DataAcquisitionTask.class);
    public static final Map<String, WriteRegistersRequest> CMDS = new HashMap<>();
	private ApplicationEventPublisher eventPublisher;

	public void send(ModbusMaster master, Server ss, LocalDateTime dt, boolean check){
		logger.debug("send data to server[{}]" , ss.getHostname());
		List<TcpModbusResult> result = new ArrayList<TcpModbusResult>();
		try{
			Iterator<TcpModbusRequest> iter = ss.getTcpRequests().iterator();
            synchronized (iter) {
                while (iter.hasNext()) {
//                    interrupt(master, ss);
                    TcpModbusRequest request = iter.next();
                    if (check || request.getMeter().getStatus() == null) {
                        Utils.checkInit(request, true);
                    }
                    //Utils.updateMeterStatus(request, ModbusServerManager.STATUS_PENDING, check);

                    if (request.getMeter().getStatus().getLastState() == ModbusServerManager.STATUS_BAD && ss.getModel() != Server.MODBUS_SERVER_MODEL_SOCKET) {
                        continue;
                    }
                    if (!request.getMeter().isEnable()) {
//	        		request.getMeter().getStatus().setLastState(ModbusServerManager.STATUS_PENDING);
                        Utils.updateMeterStatus(request, ModbusServerManager.STATUS_PENDING, check);
                    } else if (master != null) {
                        sendRequest(master, dt, request, result, check);
                    } else {
                        TcpModbusResult re = new TcpModbusResult();
                        re.setRequest(request);
                        if (ss.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET) {
                            //fixed 会和 PollingServerRequestHandler冲突，多产生一次记数。
                            //Utils.updateMeterStatus(request, ModbusServerManager.STATUS_BAD, check);
                            Utils.updateMeterStatusForTotal(request, check);
//                        request.getMeter().getStatus().setCreateDate(dt.atZone(ZoneId.systemDefault()));
                        } else {
                            Utils.updateMeterStatus(request, ModbusServerManager.STATUS_PENDING, check);
                        }
//                    request.getMeter().getStatus().setLastState(ModbusServerManager.STATUS_PENDING);
                        result.add(re);
                    }
                }
                if(ss.getModel() != Server.MODBUS_SERVER_MODEL_SOCKET && master != null) {
                    retry(master, ss, dt, result, check);
                }
            }
        }finally{
			this.eventPublisher.publishEvent(new TcpModbusEvent(result));
		}
	}

    private void interrupt(ModbusMaster master, Server ss) {
//	    synchronized (CMDS){
//	        WriteRegistersRequest req = CMDS.get(ss.getCode());
//	        if(req != null) {
//                try {
//                    master.send(req);
//                    Thread.currentThread().sleep(500);
//                    WriteRegistersRequest resetReq = new WriteRegistersRequest(req.getSlaveId(), req.getStartOffset(), new short[]{(short) 0});
//                    master.send(req);
//                } catch (Exception e) {
//                    logger.error("命令执行失败！");
//                }
//                if (req != null) {
//                    CMDS.remove(ss.getCode());
//                }
//            }
//        }
    }

    @Timed
    protected void sendRequest(ModbusMaster master, LocalDateTime dt, TcpModbusRequest request, List<TcpModbusResult> result, boolean check) {
		TcpModbusResult re = new TcpModbusResult();
		re.setRequest(request);
		try{
			logger.debug("请求：" + request.getRequest().getSlaveId() + " 数据。");
            TcpModbusResponse res = new TcpModbusResponse();
            ModbusResponse res1 = master.send(request.getRequest());
            if(res1 instanceof ReadHoldingRegistersResponse){
                ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)res1;
                res.setCode(response.getSlaveId());
                res.setMainData(response.getData());
                res.setNow(dt);

            }else if(res1 instanceof ReadInputRegistersResponse){
                ReadInputRegistersResponse response = (ReadInputRegistersResponse)res1;
                res.setCode(response.getSlaveId());
                res.setMainData(response.getData());
                res.setNow(dt);
            }
			re.setResponse(res);
            request.getMeter().getStatus().setCreateDate(null);
//			request.getMeter().getStatus().setLastState(ModbusServerManager.STATUS_OKEY);
            Utils.updateMeterStatus(request, ModbusServerManager.STATUS_OKEY, check);
            logger.debug("串口服务器：" + request.getIpParams().getHost() + ", 表: " + request.getRequest().getSlaveId() + " 数据返回成功！");
		}catch(Exception e){
			logger.error("串口服务器：" + request.getIpParams().getHost() + ", 地址：" + request.getRequest().getSlaveId() + " 获取数据异常，异常信息：" + e.getMessage());
            Utils.updateMeterStatus(request, ModbusServerManager.STATUS_BAD, check);
//			request.getMeter().getStatus().setLastState(ModbusServerManager.STATUS_BAD);
        }finally{
            if(request.getMeter().getStatus().getLastState() == ModbusServerManager.STATUS_BAD && request.getMeter().getStatus().getCreateDate() == null) {
                request.getMeter().getStatus().setCreateDate(dt.atZone(ZoneId.systemDefault()));
            }
            request.getMeter().getStatus().setLastUpdate(dt.atZone(ZoneId.systemDefault()));
			if(request.getMeter().getStatus().getLastState() != ModbusServerManager.STATUS_OKEY){
                request.getMeter().getStatus().setLastRetryDate(dt.atZone(ZoneId.systemDefault()));
            }
			result.add(re);
		}
	}

	private void retry(ModbusMaster master, Server ss, LocalDateTime dt, List<TcpModbusResult> result, boolean check) {
		Iterator<TcpModbusRequest> iter = ss.getTcpRequests().iterator();
		long second = dt.getSecond();
        while(iter.hasNext()){
        	TcpModbusRequest request = iter.next();
        	if(request.getMeter().getStatus().getLastState() != ModbusServerManager.STATUS_BAD || master == null){
        		continue;
        	}
        	request.getMeter().getStatus().setRetryCount(request.getMeter().getStatus().getRetryCount() + 1);
        	long t = dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - request.getMeter().getStatus().getCreateDate().toInstant().toEpochMilli();
        	if(t >= RetryLevel.DELAY_ONE_MINUTE && t < RetryLevel.DELAY_FIVE_MINUTES && (second >= 55 || second < 5)){
        		request.getMeter().getStatus().setLastRetryLevel(RetryLevel.DELAY_ONE_MINUTE);
        		sendRequest(master, dt, request, result, check);
        	}else if(t >= RetryLevel.DELAY_FIVE_MINUTES && t < RetryLevel.DELAY_THIRTY_MINUTES &&  (second >= 55 || second < 5)){
        		request.getMeter().getStatus().setLastRetryLevel(RetryLevel.DELAY_FIVE_MINUTES);
        		sendRequest(master, dt, request, result, check);
        	}else if(t >= RetryLevel.DELAY_THIRTY_MINUTES && t < RetryLevel.DELAY_ONE_HOUR && (second >= 55 || second < 5)){
        		request.getMeter().getStatus().setLastRetryLevel(RetryLevel.DELAY_THIRTY_MINUTES);
        		sendRequest(master, dt, request, result, check);
        	}else if(t >= RetryLevel.DELAY_ONE_HOUR && t < RetryLevel.DELAY_FIVE_HOURS && (second >= 55 || second < 5)){
        		request.getMeter().getStatus().setLastRetryLevel(RetryLevel.DELAY_ONE_HOUR);
        		sendRequest(master, dt, request, result, check);
        	}else if(t >= RetryLevel.DELAY_FIVE_HOURS && t < RetryLevel.DELAY_ONE_DAY && (second >= 55 || second < 5)){
        		request.getMeter().getStatus().setLastRetryLevel(RetryLevel.DELAY_FIVE_HOURS);
        		sendRequest(master, dt, request, result, check);
        	}
        }
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher =  applicationEventPublisher;
	}

}
