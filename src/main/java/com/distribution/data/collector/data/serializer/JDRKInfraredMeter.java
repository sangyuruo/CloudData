package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JDRKInfraredMeter implements IMeterParser {

    //01030200017984
	private final Logger logger = LoggerFactory.getLogger(JDRKInfraredMeter.class);
	public static final String[] strKeys = new String[]{"St"};

	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)){
		    try {
                Float t = (float)Util.readShort(data, 0);
                batchArgs.put(strKeys[0], t);
            }catch (Exception e){
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
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
		return 45;
	}

	@Override
	public String getName() {
		return "建大仁科红外探测";
	}

}
