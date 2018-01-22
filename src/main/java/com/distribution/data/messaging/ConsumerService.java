package com.distribution.data.messaging;

import com.distribution.data.collector.event.ext.MeterDataMsgEvent;
import com.distribution.data.collector.event.ext.MeterStatusMsgEvent;
import com.distribution.data.collector.event.ext.ObjectMessageEvent;
import com.distribution.data.domain.SmartMeterData;
import com.distribution.data.domain.SmartMeterStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);


//    @StreamListener(ConsumerChannel.CHANNEL)
//    public void consume(Greeting greeting) {
//        log.info("Received message: {}.", greeting.getMessage());
//    }
//
//    @StreamListener(ConsumerChannel.CHANNEL)
//    public void consume(Serializable greeting) {
//        log.info("Received Ser message: {}.", greeting.toString());
//    }

    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMessageEvent messageEvent = null;
        try {
            messageEvent = mapper.readValue(msg, ObjectMessageEvent.class);
            Object message = messageEvent.getMessage();
            String type = messageEvent.getType();
            log.info("Received Ser type:{} action:{} message: {}.", messageEvent.getType(), messageEvent.getAction(), message);
            if (type.equals(MeterDataMsgEvent.METER_DATA_TYPE)) {
                List<Map> results = (List<Map>) message;
                for (Map result : results) {
                    if (result == null)
                        continue;
                    try {
                        SmartMeterData data = (SmartMeterData) mapToObject(result,SmartMeterData.class);
                    } catch (Exception e) {
                        log.error( "parse Exception {} . " , e.getMessage() );
//                        e.printStackTrace();
                    }
                }
                log.info("result size is {} ", results.size());
            } else if (type.equals(MeterStatusMsgEvent.METER_STATUS_TYPE)) {
                List<Map> results = (List<Map>) message;
                for (Map result : results) {
                    if (result == null)
                        continue;
                    try {
                        SmartMeterStatus data = (SmartMeterStatus) mapToObject(result,SmartMeterStatus.class);
                    } catch (Exception e) {
                        log.error( "parse Exception {} . " , e.getMessage() );
//                        e.printStackTrace();
                    }
                }
                log.info("result size is {} ", results.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws InstantiationException, IntrospectionException, IllegalAccessException {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                try {
                    setter.invoke(obj, map.get(property.getName()));
                } catch (IllegalAccessException e) {
                    log.error( "parse {} IllegalAccessException. " , property.getName() );
//                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    log.error( "parse {} InvocationTargetException. " , property.getName() );
//                    e.printStackTrace();
                }catch (Throwable e) {
                    log.error( "parse {} Throwable. " , property.getName() );
//                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

}
