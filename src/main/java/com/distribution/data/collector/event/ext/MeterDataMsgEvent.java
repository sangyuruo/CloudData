package com.distribution.data.collector.event.ext;

import com.distribution.data.domain.SmartMeterData;

import java.util.List;

public class MeterDataMsgEvent extends AbstractMessageEvent<List<SmartMeterData>> implements MessageEvent<List<SmartMeterData>> {

    public MeterDataMsgEvent(String type, String action, List<SmartMeterData> messages) {
        super(type, action, messages);
    }
}
