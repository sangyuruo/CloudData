package com.distribution.data.collector.event.ext;
import com.emcloud.domain.SmartMeterStatusMsg;

import java.util.List;

public class MeterStatusMsgEvent extends AbstractMessageEvent<List<SmartMeterStatusMsg>> implements MessageEvent<List<SmartMeterStatusMsg>> {

    public static String METER_STATUS_TYPE="meter_status";
    public MeterStatusMsgEvent( String action, List<SmartMeterStatusMsg> messages) {
        super( METER_STATUS_TYPE , action, messages);
    }

}
