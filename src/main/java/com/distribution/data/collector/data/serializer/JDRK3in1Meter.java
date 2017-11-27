package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JDRK3in1Meter implements IMeterParser {

    //01030801fc011f0000007fbde7
	private final Logger logger = LoggerFactory.getLogger(JDRK3in1Meter.class);
	public static final String[] strKeys = new String[]{"Humidity", "Temperature", "Luminosity"};

	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)){
		    try {
                Float t = (float)Util.readShort(data, 0);
                Float h = (float)Util.readShort(data, 2);
                Float l = (float) com.distribution.data.collector.type.NumberUtils.readInt(data, 4);
                batchArgs.put(strKeys[0], t/10);
                batchArgs.put(strKeys[1], h/10);
                batchArgs.put(strKeys[2], l);
            }catch (Exception e){
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
            }
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start != 0 || numberOfReg != 4){
	        return false;
        }
        return true;
    }

    @Override
    public String getStatusKey() {
        return "";
    }

    @Override
    public int getStatusAddress() {
        return -1;
    }

    @Override
	public Integer getKey(){
		return 41;
	}

	@Override
	public String getName() {
		return "建大仁科温湿亮三合一";
	}

}
