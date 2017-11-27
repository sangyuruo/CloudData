package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeviceTempMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(DeviceTempMeter.class);
	public static final String[] strKeys = new String[]{"Temp_1", "Temp_2", "Temp_3", "Temp_4"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)) {
            int length = numberOfReg;
            int j = (start - 4097);
            for (int i = 0; i < length; i++) {
                String key = strKeys[j];
                try {
                    Float v = 0.0f;
                    short f = Util.readShort(data, i * 2);
                    batchArgs.put(key, (float) f / 10);
                    if(calculates != null){
                        String value = calculates.get(key);
                        if(value != null) {
                            auxiliary.put(key, value);
                        }
                    }
                } catch (Exception e) {
                    logger.error("数据解析错误: {}", e.getLocalizedMessage());
                    batchArgs.put(key, 0f);
                }
                j ++;
            }
        }
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start < 4097 || numberOfReg > 4){
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
		return 30;
	}

	@Override
	public String getName() {
		return "KCMD-XJ4设备温度表";
	}

}
