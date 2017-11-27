package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JDRKPMMeter implements IMeterParser {

    //0103040020002e7be5
	private final Logger logger = LoggerFactory.getLogger(JDRKPMMeter.class);
	public static final String[] strKeys = new String[]{"PM2.5", "PM10"};

	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)){
		    try {
                Float t = (float)Util.readShort(data, 0);
                Float h = (float)Util.readShort(data, 2);
                batchArgs.put(strKeys[0], t);
                batchArgs.put(strKeys[1], h);
            }catch (Exception e){
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
            }
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start != 0 || numberOfReg != 2){
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
		return 43;
	}

	@Override
	public String getName() {
		return "建大仁科PM2.5和PM10";
	}

}
