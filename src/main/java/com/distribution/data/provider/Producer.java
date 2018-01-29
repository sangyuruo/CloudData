package com.distribution.data.provider;


import com.distribution.data.collector.event.ext.MessageEvent;
import com.distribution.data.messaging.ProducerChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

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
    AtomicInteger seq =  new AtomicInteger(1);

    public void send(MessageEvent messageEvent) {
        int curSeq = seq.incrementAndGet();
        messageEvent.setSeq(curSeq);
        logger.info( "send message type: {}", messageEvent.getType() );
        StringWriter msg=new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(msg, messageEvent );
            channel.send(
                MessageBuilder.withPayload(msg.toString()).build()
            );
        } catch (Throwable e) {
            e.printStackTrace();
        }

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
