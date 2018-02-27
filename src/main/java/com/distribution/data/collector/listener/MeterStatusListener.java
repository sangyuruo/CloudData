package com.distribution.data.collector.listener;

import com.codahale.metrics.annotation.Timed;
import com.datastax.driver.core.utils.UUIDs;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.collector.data.TcpModbusResult;
import com.distribution.data.collector.event.MeterStatusEvent;
import com.distribution.data.collector.event.TcpModbusEvent;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.domain.SmartMeterStatus;
import com.distribution.data.provider.Producer;
import com.emcloud.domain.SmartMeterStatusMsg;
import com.emcloud.message.event.MeterStatusMsgEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class MeterStatusListener implements GenericApplicationListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private Producer producer;

    @Autowired
//    @Qualifier(value="meterStatusTopic")
//    private Topic meterStatusTopic;
//    @Autowired
//    private JmsTemplate jmsTemplate;

    public static synchronized String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }

    @Override
    @Timed
    public void onApplicationEvent(ApplicationEvent event) {
        @SuppressWarnings("unchecked")
        List<TcpModbusResult> result = (List<TcpModbusResult>) event.getSource();
        LocalDateTime dt = null;
        if (result.size() > 0) {
            dt = result.get(0).getRequest().getMeter().getStatus().getLastUpdate().toLocalDateTime();
        } else {
            dt = LocalDateTime.now();
        }
        UUID id = UUIDs.endOf(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<SmartMeterStatus> list = new ArrayList<SmartMeterStatus>();
        for (TcpModbusResult ms : result) {
            TcpModbusRequest request = ms.getRequest();
            if (request.getMeter().getStatus().getId() == null) {
                request.getMeter().getStatus().setId(id);
            }
            list.add(request.getMeter().getStatus());
        }
        if (list.size() > 0) {
            this.eventPublisher.publishEvent(new MeterStatusEvent(list));

            List<SmartMeterStatusMsg> msgs = new ArrayList<>();
            for (SmartMeterStatus status : list) {
                SmartMeterStatusMsg newMsg = new SmartMeterStatusMsg();
                BeanUtils.copyProperties(status, newMsg);
                newMsg.setId(status.getId().toString());
                newMsg.setMeterId(status.getMeterId().toString());
//                newMsg.setLastUpdate(status.getLastUpdate().toLocalDate());
//                newMsg.setCreateDate(status.getCreateDate().toLocalDate());
//                newMsg.setLastRetryDate(status.getLastRetryDate().toLocalDate());
                msgs.add(newMsg);
            }
            producer.send(new MeterStatusMsgEvent("create", msgs));
//            this.jmsTemplate.send(this.meterStatusTopic, new MessageCreator() {
//                @Override
//                public Message createMessage(Session session) throws JMSException {
//                    return session.createObjectMessage(new MeterStatusEvent(list));
//                }
//            });
        }
    }

    @Override
    public int getOrder() {
        return 100;
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
        this.eventPublisher = applicationEventPublisher;
    }
}
