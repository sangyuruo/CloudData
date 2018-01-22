package com.distribution.data.collector.listener;

import com.codahale.metrics.annotation.Timed;
import com.datastax.driver.core.utils.UUIDs;
import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.collector.data.*;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.collector.event.MeterDataEvent;
import com.distribution.data.collector.event.TcpModbusEvent;
import com.distribution.data.collector.event.ext.MeterDataMsgEvent;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.TcpModbusResponse;
import com.distribution.data.collector.data.TcpModbusResult;
import com.distribution.data.domain.SmartMeterData;
import com.distribution.data.provider.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class MeterDataParserListener implements GenericApplicationListener, ApplicationEventPublisherAware {
	private static Logger logger = LoggerFactory.getLogger(MeterDataParserListener.class);
    @Autowired
	private Producer producer;
    private ApplicationEventPublisher eventPublisher;

	@Override
	@Timed
    @SuppressWarnings(value = "unchecked")
	public void onApplicationEvent(ApplicationEvent event) {
		List<TcpModbusResult> result= (List<TcpModbusResult>)event.getSource();
		LocalDateTime dt = null;
		if(result.size() > 0){
			dt = result.get(0).getRequest().getMeter().getStatus().getLastUpdate().toLocalDateTime();
		}else{
			dt = LocalDateTime.now();
		}
    	String ymd = DateUtils.formatDate(dt, "yyyyMMdd");
    	String hour = DateUtils.formatDate(dt, "HH");
    	String minute = DateUtils.formatDate(dt, "mm");
    	int sec = (int)Math.floor((Integer.parseInt(DateUtils.formatDate(dt, "ss"))/10)) * 10;
    	UUID id = UUIDs.startOf(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    	List<SmartMeterData> list = new ArrayList<SmartMeterData>();
    	for (TcpModbusResult head : result) {
    		Meter meter = head.getRequest().getMeter();
			if( head.getRequest().getMeter().getStatus().getLastState() == ModbusServerManager.STATUS_OKEY && (head.getResponse() != null)){
				SmartMeterData  md = createMeterData(head, id);
				md.setYmd(Integer.parseInt(ymd));
				md.setHour(Integer.parseInt(hour));
				md.setMinute(Integer.parseInt(minute));
				md.setSec(sec);
				list.add(md);
			}
		}
    	if(list.size() > 0) {
            eventPublisher.publishEvent(new MeterDataEvent(list));
            producer.send(new MeterDataMsgEvent( "create",list));
        }
	}

	private SmartMeterData createMeterData(TcpModbusResult head, UUID id){
		SmartMeterData md = new SmartMeterData();

		TcpModbusRequest req = head.getRequest();
		md.setId(id);
		md.setCategory(req.getMeter().getCategory());
		md.setHostname(req.getMeter().getServer().getHostname());
		md.setServerId(req.getMeter().getServer().getId());
		md.setName(req.getMeter().getName());
	    md.setCompanyId(req.getMeter().getServer().getCompanyId());
	    md.setServerCode(req.getMeter().getServer().getCode());
		md.setIp(req.getIpParams().getHost());
		md.setCode(req.getRequest().getSlaveId());
		md.setMeterId(req.getMeter().getId());
        Map<String, Float> dataMap = new HashMap<String, Float>();
        IMeterParser parser = ModbusServerManager.meterParser.get(md.getCategory());
        parser.setCode(req.getMeter().getServer().getCode(), req.getMeter().getCode().toString());
        md.setData(dataMap);
        Map<String, String> auxMap = new HashMap<String, String>();
        md.setAuxiliary(auxMap);

        if (head.getResponse() != null && head.getResponse().getLongcode() == 0) {
            //TODO 如果有多个命令，需要特殊处理
            byte[] data =  head.getResponse().getMainData();

            parser.parser(dataMap, data, req.getMeter().getStartOffset(), req.getMeter().getNumberOfRegisters(), head.getRequest().getMeter().getDataTypes(), auxMap);
        }else if(head.getResponse() != null && head.getResponse().getLongcode() != 0){
            TcpModbusResponse res = head.getResponse();
            Map<String, byte[]> data = res.getResponse();
            parser.parser645(dataMap, data, "2007", head.getRequest().getMeter().getDataTypes(), auxMap);
        }
        if(head.getRequest().getMeter().getControlAddress() != -1){
            Float f = md.getData().get(parser.getStatusKey());
            if(f != null) {
                head.getRequest().getMeter().getStatus().setSwitchStatus(f.intValue());
            }else{
                head.getRequest().getMeter().getStatus().setSwitchStatus(-1);
            }
        }else{
            head.getRequest().getMeter().getStatus().setSwitchStatus(-1);
        }
        Float v = parser.getVolume(dataMap, req.getMeter().getStatus().getVolume());

        if(v != null){
            req.getMeter().getStatus().setVolume(v);
        }
		return md;
	}


	@Override
	public int getOrder() {
		return 4;
	}

	@Override
	public boolean supportsEventType(ResolvableType eventType) {
		Class<?> type = eventType.getRawClass();
		return TcpModbusEvent.class.isAssignableFrom(type);
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return true;
	}

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher =  applicationEventPublisher;
    }
}
