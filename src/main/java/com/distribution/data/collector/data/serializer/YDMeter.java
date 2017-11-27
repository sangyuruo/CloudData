package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.data.collector.type.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class YDMeter implements IMeterParser {
	public static final String[] strKeys = new String[]{"St", "U_a", "U_b", "U_c", "U_ab", "U_bc", "U_ca", "I_a", "I_b", "I_c", "P_a", "P_b", "P_c", "P_s", "Q_a", "Q_b", "Q_c", "Q_s", "S_s", "P_f_s", "F_f", "E_p", "N0", "E_q", "N1", "W_p", "N2", "W_q", "N3", "D_O", "D_I" };
	private final Logger logger = LoggerFactory.getLogger(YDMeter.class);
	private String serverCode;
	private String meterCode;
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null &&  validate(start, numberOfReg)){
    		int j= start == 5 ? 1 : (start - 6)/2 + 1;
    		int i = 0;
    		if(start == 5){
    			try {
					Short state =  NumberUtils.readShort(data, 0);
					state = state < 0 ? 0 : state;
					batchArgs.put(strKeys[0], new Float(state));
				} catch (IOException e) {
					logger.error("数据解析错误: {}, {}, {}", this.serverCode, this.meterCode , e.getLocalizedMessage());
					batchArgs.put(strKeys[0], -1f);
				}
    			i++;
    		}
    		int length = start == 5 ? ((numberOfReg - 1) / 2 + 1) : numberOfReg / 2;
	    	for (; i < length; i++) {
	    		String key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
				Float f = null;
				try {
					int k = start == 5 ? i * 4 -2 : i* 4;
					if(i <  28) {
//					    if(key.startsWith("N")){
//                            continue;
//                        }
                        f = NumberUtils.readFloat(data, k);
                        if (f != null && !f.equals(Float.NaN)) {
//						f = f < 0 ? 0 : f;
                            batchArgs.put(key, f);
                        }
                    }else{
                        batchArgs.put(key, new Float(NumberUtils.readShort(data, k)));
                        j++;
                        short x =  NumberUtils.readShort(data, k + 2);
                        key = j < strKeys.length ? strKeys[j] : new Integer(j * 2 + start).toString();
                        batchArgs.put(key, new Float(x));
                        break;
                    }
				} catch (Exception e) {
					logger.error("数据解析错误: {},{}, {}", this.serverCode, this.meterCode ,  e.getLocalizedMessage());
					batchArgs.put(key, 0f);
				}
				j++;
			}
    	}
	}

    @Override
    public boolean validate(int start, int numberOfReg) {
        if(numberOfReg > 58){
            return false;
        }else if((start != 5 && numberOfReg % 2 > 0) || (start == 5 && numberOfReg % 2 == 0)){
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
        return "D_I";
    }

    @Override
    public int getStatusAddress() {
        return 5;
    }

    @Override
	public Integer getKey() {
		return 1;
	}

	@Override
	public String getName() {
		return "雅达智能电表";
	}

}
