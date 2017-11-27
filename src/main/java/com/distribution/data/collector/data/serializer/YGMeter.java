package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.data.collector.type.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class YGMeter implements IMeterParser {
	public static final String[] strKeys = new String[]{"U_a", "U_b", "U_c", "U_ab", "U_bc", "U_ca", "I_a", "I_b", "I_c", "P_a", "P_b", "P_c", "P_s", "Q_a", "Q_b", "Q_c", "Q_s", "S_s", "P_f_s", "F_f", "W_p", "W_Rp", "W_q", "W_Rq"};
	private final Logger logger = LoggerFactory.getLogger(YGMeter.class);
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int j = start - 10;
    		int i = 0;
    		int length = numberOfReg / 2;
	    	for (; i < length; i++) {
	    		String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 ).toString();
				Float f = null;
				try {
					int k = i * 4;
                    batchArgs.put(key, NumberUtils.readFloat(data, k));
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
        if(start < 10 || numberOfReg > 48){
            return false;
        }else if(numberOfReg % 2 > 0){
            return false;
        }
        return true;
    }

    @Override
    public String getStatusKey() {
        return "D_I";
    }

    @Override
    public int getStatusAddress() {
        return -1;
    }

    @Override
	public Integer getKey() {
		return 5;
	}

	@Override
	public String getName() {
		return "仪歌多功能直通电表";
	}

}
