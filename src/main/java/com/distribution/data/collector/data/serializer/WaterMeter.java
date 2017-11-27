package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WaterMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(WaterMeter.class);
	public static final String[] strKeys = new String[]{"Aggr_val", "State", "Rate"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)){
            float a = -1;
            if(calculates != null ) {
                try {
                    if(calculates.get("A") != null) {
                        a = Float.parseFloat(calculates.get("A"));
                    }
                }catch (Exception e){
                    logger.error("解析液位计安装高度有误:{}", calculates.get("A"));
                }
            }
		    String key = "";
		    try {
                Float v = 0.0f;
                if (start == 514) {
                    key = strKeys[0];
                    v = (float) com.distribution.data.collector.type.NumberUtils.readInt(data, 0);
                    if(a > 0){
                        v /= a;
                    }
                }else if(start ==  518){
                    key = strKeys[1];
                    v = (float)Util.readShort(data, 0);
                }else if(start ==  520){
                    key = strKeys[2];
                    v = (float)Util.readShort(data, 0);
                }
                batchArgs.put(key, v);
            }catch (Exception e){
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
                batchArgs.put(key, 0f);
            }
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start < 514 || numberOfReg > 8){
	        return false;
        }else if(start % 2 > 0 || start == 516){
	        return false;
        }
        return true;
    }

    @Override
    public String getVolumeKey() {
        return "Aggr_val";
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
		return 20;
	}

	@Override
	public String getName() {
		return "依泉水表";
	}

}
