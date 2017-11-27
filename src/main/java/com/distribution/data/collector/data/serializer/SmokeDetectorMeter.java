package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmokeDetectorMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(SmokeDetectorMeter.class);
	public static final String[] strKeys = new String[]{"SmokeDetector"};

	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)){
		    String key = strKeys[0];
		    try {
                Float v = (float)Util.readShort(data, 0);
                batchArgs.put(key, v);
            }catch (Exception e){
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
                batchArgs.put(key, 0f);
            }
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start != 3 || numberOfReg != 1){
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
		return 40;
	}

	@Override
	public String getName() {
		return "建大仁科光电感烟";
	}

}
