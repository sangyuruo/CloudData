package com.distribution.data.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;


@SuppressWarnings("unused")
@Configuration
@EnableJms
@AutoConfigureAfter(value = { MetricsConfiguration.class })
public class ActiveMQConfiguration {

    private final Logger log = LoggerFactory.getLogger(ActiveMQConfiguration.class);

    @PreDestroy
    public void destroy() {
        log.info("Closing Active MQ Manager");
    }

    @Bean
    public Queue logQueue() {
        return new ActiveMQQueue("LOG_QUEUE");
    }

    @Bean
    public Topic meterStatusTopic() {
        return new ActiveMQTopic("meter.status.topic");
    }

    @Bean
    public Topic serverStatusTopic() {
        return new ActiveMQTopic("server.status.topic");
    }

    @Bean
    public Topic meterDataTopic() {
        return new ActiveMQTopic("meter.data.topic");
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory connectionFactory,
                                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory connectionFactory,
                                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
