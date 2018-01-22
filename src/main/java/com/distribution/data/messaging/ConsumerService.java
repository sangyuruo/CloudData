package com.distribution.data.messaging;

import com.distribution.data.collector.event.ext.AbstractMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 *
 */
@Service
public class ConsumerService {
    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);


    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(Greeting greeting) {
        log.info("Received message: {}.", greeting.getMessage());
    }

    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(Serializable greeting) {
        log.info("Received Ser message: {}.", greeting.toString());
    }

    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(AbstractMessageEvent messageEvent) {
        log.info("Received Ser type:{} action:{} message: {}.", messageEvent.getType(), messageEvent.getAction(), messageEvent.getMessage().toString());
    }
}
