package com.distribution.data.collector.event.ext;

import com.emcloud.domain.SmartMeterDataMsg;

import java.util.List;

public class MeterDataMsgEvent extends AbstractMessageEvent<List<SmartMeterDataMsg>> implements MessageEvent<List<SmartMeterDataMsg>> {

    public static String METER_DATA_TYPE="meter_data";
    public MeterDataMsgEvent(String action, List<SmartMeterDataMsg> messages) {
        super(METER_DATA_TYPE, action, messages);
    }
}
