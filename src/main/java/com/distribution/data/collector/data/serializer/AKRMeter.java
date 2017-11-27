package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AKRMeter implements IMeterParser {
	public static final String[] strKeys = new String[]{"U_a", "U_b", "U_c", "I_a", "I_b", "I_c","N2", "N2", "N2", "N2",
        "N2", "N2", "U_ab", "U_bc", "U_ca", "F_f", "P", "N2", "N2", "Q", "N2", "N2", "W_Pa", "N2", "W_Pb", "N2", "W_Pc", "N2", "N2", "N2",
        "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "P_a", "P_b", "P_c", "N2", "N2", "N2", "N2", "N2",
        "N2", "Q_a", "Q_b", "Q_c", "P_As", "P_Bs", "P_Cs",  "P_s", "N2", "N2", "P_A_f_s", "P_B_f_s", "P_C_f_s", "N2","N2",
        "N2","N2", "N2", "N2", "W_Qa", "N2", "W_Qb", "N2", "W_Qc", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2",
        "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2", "N2",  "D_O", "W_p", "N2", "N2", "N2", "N2", "N2",  "W_q", "N2"};
	private final Logger logger = LoggerFactory.getLogger(AKRMeter.class);
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && data.length == numberOfReg * 2 && validate(start, numberOfReg)){
    		int j = start - 17;
    		int i = 0;
    		int length = numberOfReg;
            float irate = 1;
            if(calculates != null ) {
                try {
                    if(calculates.get("I") != null) {
                        irate = Float.parseFloat(calculates.get("I"));
                    }
                }catch (Exception e){
                    logger.error("CT变比设置有误：{}",  calculates.get("I"));
                }
            }
	    	for (; i < length; i++) {
	    		String key = strKeys[j];
				Float f = null;
				try {
					int k = i * 2;
					if(key.equals("N2")){

                    }else if(key.startsWith("W_P") || key.startsWith("W_Q") || key.startsWith("W_p") || key.startsWith("W_q")){
                        int v = com.distribution.data.collector.type.NumberUtils.readInt(data, k);
                        batchArgs.put(key, new Float(v)/100);
                    }else{
                        int v = Util.readShort(data, k);
                        if(key.startsWith("U_")){
                            batchArgs.put(key, new Float(v)/10);
                        }else if(key.equals("F_f") ){
                            batchArgs.put(key, new Float(v)/100);
//                        }else if(key.startsWith("W_") ){
//                            batchArgs.put(key, new Float(v)/100);
                        }else if(key.startsWith("I_")){
                            batchArgs.put(key, (new Float(v)/1000) * irate);
                        }else if(key.equals("Q") || key.equals("P") || key.startsWith("P_") || key.startsWith("Q_")){
                            v = getShort(data, k);
                            batchArgs.put(key, (new Float(v)/1000) * irate);
                        }else if(key.endsWith("_f_s")){
                            v = Util.readShort(data, k);
                            batchArgs.put(key, new Float(v)/1000);
                        }else {
                            batchArgs.put(key, new Float(v));
                        }
                    }
				} catch (Exception e) {
					logger.error("数据解析错误: {}", e.getLocalizedMessage());
					batchArgs.put(key, 0f);
				}
				j++;
			}
    	}
	}

    private  short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    @Override
    public boolean validate(int start, int numberOfReg) {
        if(start < 17 || numberOfReg > 102){
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
		return 7;
	}

	@Override
	public String getName() {
		return "安科瑞多功能电表";
	}

}
