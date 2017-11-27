package com.distribution.data.provider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * Created by baling.fang 于 2017年08月12日.
 */
@Component
public class Producer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

//    @Autowired
    private Queue queue;

//    @Autowired
//    private Topic topic;

//    @Scheduled(fixedDelay=10000)//每3s执行1次
    public void send() {
        this.jmsMessagingTemplate.convertAndSend(this.queue, "hi,activeMQ");
//        this.jmsMessagingTemplate.convertAndSend(this.topic, "hi,activeMQ(topic)");
//        this.jmsMessagingTemplate.convertAndSend();
    }

}
