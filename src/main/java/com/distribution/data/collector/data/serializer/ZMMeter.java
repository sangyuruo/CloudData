package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ZMMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(ZMMeter.class);
	public static final String[] strKeys = new String[]{"U", "I1", "P_f_s", "P", "Q", "HDu1", "HDi1", "F_f",
            "C1",  "C2",  "C3",  "C4",  "C5",  "C6",  "C7",  "C8",  "C9",  "C10",  "C11",  "C12", "H_U", "L_U",
            "CT/T", "T_P_f_s", "Sw_delay", "Sw_thr", "HDu", "HDi", "C_s"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int length = numberOfReg;
    		int j = (start - 100) / 2;
	    	for (int i = 0; i < length; i++) {
	    		String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
				try {
					Float v = 0.0f;
					short f = Util.readShort(data, i * 2);
					if("U".equals(key) || "I1".equals(key) || "P".equals(key) || "Q".equals(key) || "F_f".equals(key) || "HDu1".equals(key) || "HDi1".equals(key) || "HDu".equals(key) || "HDi".equals(key)){
                        batchArgs.put(key, (float) f / 10);
                    }else if("H_U".equals(key)){
                        float l = f == 241 ? 0 : f * 2;
					    batchArgs.put(key, l);
                    }else if("L_U".equals(key)) {
                        float l = f == 161 ? 0 : f * 2;
                        batchArgs.put(key, l);
                    }else if("P_f_s".equals(key)) {
                        batchArgs.put(key, (float) f / 10000);
                    }else if("T_P_f_s".equals(key)) {
                        batchArgs.put(key, (float) f / 100);
                    }else if("Sw_delay".equals(key)) {
                        float l = f / 10;
                        if (f > 10 && f < 20) {
                            l = (f - 10);
                        } else if (f >= 20) {
                            l = (f - 20) * 10;
                        }
                        batchArgs.put(key, l);
                    }else if("HDu".equals(key)) {
                        float l = f == 101 ? 0 : f * 0.5f;
                        batchArgs.put(key, l);
                    }else if("HDi".equals(key)){
                        float l = f == 201 ? 0 : f * 0.5f;
                        batchArgs.put(key, l);
                    }else{
                        batchArgs.put(key, (float) f);
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
	    if(start < 100 || numberOfReg > 29){
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
		return 3;
	}

	@Override
	public String getName() {
		return "批明无功表";
	}

}
