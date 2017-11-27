package com.distribution.data.collector.event;

import org.springframework.context.ApplicationEvent;

public class ModbusReloadEvent extends ApplicationEvent {

	private static final long serialVersionUID = -879704666529067243L;
	public static final int MODBUS_SERVER = 0;
	public static final int MODBUS_METER = 1;
    public static final int OPERATOR_ADD = 1;
    public static final int OPERATOR_MODIFY = 2;
    public static final int OPERATOR_DELETE = 3;
	private int type;
	private int operator;
	public ModbusReloadEvent(Object source) {
		super(source);
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

}
