package com.distribution.data.collector.data;

import org.springframework.context.ApplicationEvent;

public class ModbusServerManagerEvent extends ApplicationEvent {
    public ModbusServerManagerEvent(String s) {
        super(s);
    }
}
