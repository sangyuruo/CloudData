package com.distribution.data.collector.listener;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.data.collector.data.ModbusServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MeterRegister implements BeanPostProcessor {
	private final Logger log = LoggerFactory.getLogger(MeterRegister.class);
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof IMeterParser) {
			IMeterParser meter = (IMeterParser) bean;
			ModbusServerManager.meterParser.put(meter.getKey(), meter);
			log.info("成功注册{}数据解析程序", meter.getName());
		}
		return bean;
	}

}
