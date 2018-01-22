package com.distribution.data.collector.event.ext;

import com.distribution.data.domain.SmartMeterStatus;
import com.emcloud.message.event.AbstractMessageEvent;
import com.emcloud.message.event.MessageEvent;

import java.util.List;

public class MeterStatusMsgEvent extends AbstractMessageEvent<List<SmartMeterStatus>> implements MessageEvent<List<SmartMeterStatus>> {

    public MeterStatusMsgEvent(String type, String action, List<SmartMeterStatus> messages) {
        super(type, action, messages);
    }

}
