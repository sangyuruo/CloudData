package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SldWaterMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(SldWaterMeter.class);
	public static final String[] strKeys = new String[]{"Instant_flow", "Instant_hot_flow", "Vel", "Measure_vel",
        "Aggr_val", "Aggr_decimal", "Neg_aggr_val", "Neg_aggr_decimal", "Aggr_hot_val", "Aggr_hot_decimal", "Neg_aggr_hot_val",
        "Neg_aggr_hot_decimal", "Aggr_val_cul", "Aggr_val_cul_decimal", "Aggr_hot_val_cul", "Aggr_hot_val_cul_decimal"};

    /**
     * 000000000000000000000000f9f044f1000000000bce3b0100000000000000000000000000000000000000004426b566000000000bce3b010000000044263566
     * 1f703fa900000000a6f43de9c00c449d03d000006bc73f15ff56ffff0f1abf4c00000000000000000000000000000000032500005c803f490000000000000000
     * @param batchArgs
     * @param data
     * @param start
     * @param numberOfReg
     */
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int length = numberOfReg/2;
    		int j = (start) / 2;
	    	for (int i = 0; i < length; i++) {
	    		String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
				try {
					Float v = 0.0f;
					int k = i == 0 ? 0 : i * 4;
                    byte[] d = new byte[4];
                    d[0] = data[k + 2];
                    d[1] = data[k + 3];
                    d[2] = data[k];
                    d[3] = data[k + 1];
					if(key.equals("Aggr_val") || key.equals("Neg_aggr_val") || key.equals("Aggr_hot_val") ||
                        key.equals("Neg_aggr_hot_val") || key.equals("Aggr_val_cul") || key.equals("Aggr_hot_val_cul")) {
                        v = (float) com.distribution.data.collector.type.NumberUtils.readInt(d, 0);
                    }else{
                        v = (float) com.distribution.data.collector.type.NumberUtils.readFloat(d, 0);
                    }
                    batchArgs.put(key, v);
                } catch (Exception e) {
                    logger.error("数据解析错误: {}", e.getLocalizedMessage());
                    batchArgs.put(key, 0f);
                }
                j++;
			}
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start < 0 || start > 30|| numberOfReg > 32){
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
    public String getVolumeKey() {
        return "Aggr_val";
    }

    @Override
    public int getStatusAddress() {
        return -1;
    }

    @Override
	public Integer getKey(){
		return 21;
	}

	@Override
	public String getName() {
		return "顺来达水表";
	}

}
