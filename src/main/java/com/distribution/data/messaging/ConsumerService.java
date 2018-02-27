package com.distribution.data.messaging;

import com.emcloud.domain.SmartMeterDataMsg;
import com.emcloud.domain.SmartMeterStatusMsg;
import com.emcloud.message.event.MeterDataMsgEvent;
import com.emcloud.message.event.MeterStatusMsgEvent;
import com.emcloud.message.event.ObjectMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
            log.info("Received Ser type:{} action:{} seq: {}.", messageEvent.getType(), messageEvent.getAction(), messageEvent.getSeq());

            this.handleData(msg,type);
//            handleData(messageEvent);
//            if( type.equals( MeterDataMsgEvent.METER_DATA_TYPE )){
//                List<SmartMeterData> datas = (List<SmartMeterData> ) message;
//                log.info("message size is {} " , datas.size() );
//            }else if( type.equals( MeterStatusMsgEvent.METER_STATUS_TYPE  )){
//                List<SmartMeterStatus> statuses = (List<SmartMeterStatus> ) message;
//                log.info("message size is {} " , statuses.size() );
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    @StreamListener(ConsumerChannel.CHANNEL)
//    public void consume(MessageEvent msg) {
//        log.info("Received Ser type:{} action:{} message: {}", msg.getType(), msg.getAction(), msg.getMessage().toString());
//    }
    private void handleData(String message, String type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (type.equals(MeterDataMsgEvent.METER_DATA_TYPE)) {
//            mapper.readValues(message, MeterDataMsgEvent.class);
            MeterDataMsgEvent messageEvent = mapper.readValue(message, MeterDataMsgEvent.class );
            log.info( "size is {}" ,  messageEvent.getMessage().size() );
        } else if (type.equals(MeterStatusMsgEvent.METER_STATUS_TYPE)) {
            MeterStatusMsgEvent messageEvent = mapper.readValue(message, MeterStatusMsgEvent.class);
            log.info( "size is {}" ,  messageEvent.getMessage().size() );
        }

    }

    private void handleData(ObjectMessageEvent messageEvent) {
        Object message = messageEvent.getMessage();
        String type = messageEvent.getType();
        if (type.equals(MeterDataMsgEvent.METER_DATA_TYPE)) {
            List<Map> results = (List<Map>) message;
            for (Map result : results) {
                if (result == null)
                    continue;
                try {
                    SmartMeterDataMsg data = (SmartMeterDataMsg) mapToObject(result, SmartMeterDataMsg.class);
                } catch (Exception e) {
                    log.error("parse Exception {} . ", e.getMessage());
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
                    SmartMeterStatusMsg data = (SmartMeterStatusMsg) mapToObject(result, SmartMeterStatusMsg.class);
                } catch (Exception e) {
                    log.error("parse Exception {} . ", e.getMessage());
//                        e.printStackTrace();
                }
            }
            log.info("result size is {} ", results.size());
        }
    }


    private static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws InstantiationException, IntrospectionException, IllegalAccessException {
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
                    log.error("parse {} IllegalAccessException. ", property.getName());
//                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    log.error("parse {} InvocationTargetException. ", property.getName());
//                    e.printStackTrace();
                } catch (Throwable e) {
                    log.error("parse {} Throwable. ", property.getName());
//                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

}
