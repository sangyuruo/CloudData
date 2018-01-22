package com.distribution.data.collector.listener;

import com.distribution.data.collector.event.MeterStatusEvent;
import com.distribution.data.repository.SmartMeterStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;


//@Component
public class MeterStatusAppenderListener implements ApplicationListener<MeterStatusEvent> {
	@Inject
    private SmartMeterStatusRepository smartMeterStatusRepository;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Override
    public void onApplicationEvent(MeterStatusEvent meterStatusEvent) {
        if(meterStatusEvent.getMeterStatuses() != null)
            this.smartMeterStatusRepository.batchSave(meterStatusEvent.getMeterStatuses());
    }

    @JmsListener(destination = "meter.status.topic", containerFactory = "jmsListenerContainerTopic")
    public void receiveStatus(Message message){
        if (message instanceof ObjectMessage) {
            ObjectMessage om = (ObjectMessage) message;
            try {
                MeterStatusEvent exampleUser = (MeterStatusEvent) om.getObject();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println("from" + message.toString() + " get ObjectMessage");
        }
//        System.out.println(list);
    }
}
