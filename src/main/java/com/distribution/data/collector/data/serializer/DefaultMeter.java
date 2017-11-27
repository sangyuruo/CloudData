package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(DefaultMeter.class);
	public static final String[] strKeys = new String[]{"U"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int i = 0;
    		int length = numberOfReg/2;
	    	for (int j=0; j < length; j++) {
				Float f = null;
				try {
					f = com.distribution.data.collector.type.NumberUtils.readFloat(data, i);
					if(f != null && !f.equals(Float.NaN)){
						batchArgs.put(new Integer(j*2 + start).toString(), f);
					}
				} catch (Exception e) {
					logger.error("数据解析错误: {}", e.getLocalizedMessage());
					batchArgs.put(new Integer(j*2 + start).toString(), 0f);
				}
				i += 4;
			}
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
        if(start < 0 || start % 2 > 0 || numberOfReg % 2 > 0){
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
	public Integer getKey() {
		return 0;
	}

	@Override
	public String getName() {
		return "缺省智能电表";
	}

}
