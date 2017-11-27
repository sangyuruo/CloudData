package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ZY112路，主要读取12路状态数据
 * @author sangjun
 *
 */
@Component
public class ZY112Meter implements IMeterParser {
	public static final String[] strKeys = new String[]{"St_1", "St_2", "St_3", "St_4", "St_5", "St_6", "St_7", "St_8", "St_9", "St_A", "St_B", "St_C"};
	private final Logger logger = LoggerFactory.getLogger(ZY112Meter.class);
	private String serverCode;
	private String meterCode;
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int i = 0;
    		int length = numberOfReg;
	    	for (int j=0; j < length; j++) {
	    		String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
				Short f = null;
				try {
					f = com.distribution.data.collector.type.NumberUtils.readShort(data, i);
					if(f != null && !f.equals(Float.NaN)){
						batchArgs.put(key, new Float(f));
					}
				} catch (Exception e) {
					logger.error("数据解析错误: {},{}, {}", this.serverCode, this.meterCode ,  e.getLocalizedMessage());
					batchArgs.put(key, 0f);
				}
				i += 2;
			}
    	}
		logger.info("parse success");
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
        if(numberOfReg > 24){
            return false;
        }else if(start < 0 || start % 2 > 0 || numberOfReg % 2 > 0){
            return false;
        }
        return true;
    }

    @Override
    public void setCode(String serverCode, String meterCode){
        this.serverCode = serverCode;
        this.meterCode = meterCode;
    }
    @Override
    public String getStatusKey() {
        return "St_1";
    }

    @Override
    public int getStatusAddress() {
        return 5;
    }

    @Override
	public Integer getKey() {
		return 70;
	}

	@Override
	public String getName() {
		return "ZY112多路开关";
	}

}
