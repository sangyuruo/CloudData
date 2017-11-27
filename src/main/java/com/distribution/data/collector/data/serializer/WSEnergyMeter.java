package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WSEnergyMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(WSEnergyMeter.class);
	public static final String[] strKeys = new String[]{"W_A_p", "W_B_p", "W_C_p", "W_p"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)) {
            int length = numberOfReg / 2;
            int j = (start - 8192) / 2;
            for (int i = 0; i < length; i++) {
                String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
                try {
                    Float f = null;
                     f = (com.distribution.data.collector.type.NumberUtils.readInt(data, i * 4) )/10000.0f;
                    if(f != null && !f.equals(Float.NaN)){
                        batchArgs.put(key, f);
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
	    if(start < 8192 || start > 8198 || numberOfReg > 8){
	        return false;
        }else if(start % 2 > 0){
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
		return 8;
	}

	@Override
	public String getName() {
		return "DSSD332/DTSD342-1W电表";
	}

}
