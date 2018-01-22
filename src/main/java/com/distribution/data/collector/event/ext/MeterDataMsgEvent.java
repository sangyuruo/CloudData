package com.distribution.data.collector.event.ext;

import com.distribution.data.domain.SmartMeterData;

import java.util.List;

public class MeterDataMsgEvent extends AbstractMessageEvent<List<SmartMeterData>> implements MessageEvent<List<SmartMeterData>> {

    public static String METER_DATA_TYPE="meter_data";
    public MeterDataMsgEvent(String action, List<SmartMeterData> messages) {
        super(METER_DATA_TYPE, action, messages);
    }
}
