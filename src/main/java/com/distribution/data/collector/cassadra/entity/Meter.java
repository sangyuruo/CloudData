package com.distribution.data.collector.cassadra.entity;

import com.datastax.driver.mapping.annotations.Table;
import com.distribution.data.domain.SmartMeter;

@Table(name = "smartMeter")
public class Meter extends SmartMeter{
	private static final long serialVersionUID = 2622677934936388404L;

//	private Map<String, String> dataTypes; //数据类型映射：Uab:float, Ubc:flat, Uca:float,...,Eq:float。
//	private Integer controlAddress; //可查询状态，设置开，关。默认1个字节长度。
	// private Map<String, String> comms; //存储以json字符串{status: {}, on: {}, off: {}}
	//命令控制：status:010300050001940B, on:0110000500010101CD96, off:01100005000101000C56 。
	//返回结果：status:010301013188, on: 01100005000111C8, off: 01100005000111C8


}
