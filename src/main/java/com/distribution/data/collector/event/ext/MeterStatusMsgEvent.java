package com.distribution.data.collector.event.ext;

import com.distribution.data.domain.SmartMeterStatus;

import java.util.List;

public class MeterStatusMsgEvent extends AbstractMessageEvent<List<SmartMeterStatus>> implements MessageEvent<List<SmartMeterStatus>> {

    public MeterStatusMsgEvent(String type, String action, List<SmartMeterStatus> messages) {
        super(type, action, messages);
        this.action = action;
        this.messages = messages;
    }

}
