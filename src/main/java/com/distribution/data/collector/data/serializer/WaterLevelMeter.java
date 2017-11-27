package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WaterLevelMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(WaterLevelMeter.class);
	public static final String[] strKeys = new String[]{"Ullage", "aireTemp", "OilTemp", "Level"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
        float h = -1;
        if(calculates != null ) {
            try {
                if(calculates.get("H") != null) {
                    h = Float.parseFloat(calculates.get("H"));
                }
            }catch (Exception e){
                logger.error("解析液位计安装高度有误:{}", calculates.get("H"));
            }
        }
		if(data != null && validate(start, numberOfReg)){
            int length = numberOfReg;
            int j = (start - 2);
            for (int i = 0; i < length; i++) {
                String key = strKeys[j];
                try {
                    Float v = 0.0f;
                    short f = Util.readShort(data, i * 2);
                    if(key.equals("OilTemp")){
                        batchArgs.put(key, (float) f / 10);
                    }else if(key.equals("Ullage")){
                        float u = (float) f / 1000;
                        batchArgs.put(key, u);
                        float l = h - u > 0 ? h - u : 0;
                        batchArgs.put("Level", l);
                    }else {
                        batchArgs.put(key, (float) f / 1000);
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
	    if(start < 2 || numberOfReg > 3){
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
		return 22;
	}

	@Override
	public String getName() {
		return "GL-100液位计";
	}

}
