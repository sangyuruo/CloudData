package com.distribution.data.collector.event.ext;

import com.distribution.data.domain.SmartMeterStatus;

import java.util.List;

public class MeterStatusMsgEvent extends AbstractMessageEvent<List<SmartMeterStatus>> implements MessageEvent<List<SmartMeterStatus>> {

    public static String METER_STATUS_TYPE="meter_status";
    public MeterStatusMsgEvent( String action, List<SmartMeterStatus> messages) {
        super( METER_STATUS_TYPE , action, messages);
    }

}
