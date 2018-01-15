package com.distribution.data.config;

import com.distribution.data.messaging.ConsumerChannel;
import com.distribution.data.messaging.ProducerChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

/**
 * Configures Spring Cloud Stream support.
 * <p>
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for more information.
 */
@EnableBinding(value = {Source.class, ProducerChannel.class, ConsumerChannel.class})
public class MessagingConfiguration {

}
