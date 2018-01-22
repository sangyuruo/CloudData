package com.distribution.data.provider;


import com.distribution.data.collector.event.ext.MessageEvent;
import com.distribution.data.domain.Company;
import com.distribution.data.messaging.ProducerChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.io.Serializable;

/**
 * Created by baling.fang 于 2017年08月12日.
 */
@Component
public class Producer {

    Logger logger = LoggerFactory.getLogger( Producer.class );
    private MessageChannel channel;

    public Producer(ProducerChannel channel) {
        this.channel = channel.messageChannel();
    }

    public void send(Serializable msg) {
        channel.send(
            MessageBuilder.withPayload(msg).build()
        );
    }

    public void send(MessageEvent messageEvent) {
        logger.info( "send message type: {}", messageEvent.getType() );
        channel.send(
            MessageBuilder.withPayload(messageEvent).build()
        );
    }
//    @Autowired
//    private JmsMessagingTemplate jmsMessagingTemplate;
//
////    @Autowired
//    private Queue queue;

//    @Autowired
//    private Topic topic;

//    @Scheduled(fixedDelay=10000)//每3s执行1次
//    public void send() {
//        this.jmsMessagingTemplate.convertAndSend(this.queue, "hi,activeMQ");
//        this.jmsMessagingTemplate.convertAndSend(this.topic, "hi,activeMQ(topic)");
//        this.jmsMessagingTemplate.convertAndSend();
//    }
}
