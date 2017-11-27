package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JKWMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(JKWMeter.class);
	public static final String[] strKeys = new String[]{"U", "I1", "I2", "P_f_s", "P1", "P2", "Q1", "Q2", "F_f", "T", "HDu1", "N0",
			"HDu2",  "HDu3",  "HDu4",  "HDu5",  "HDu6",  "HDu7",  "HDu8",  "HDu9",  "HDu10",  "HDu11",  "HDu12",  "HDu13",  "HDu14",
			"HDu15",  "HDu16",  "HDu17",  "HDu18",  "HDu19",  "HDu20",  "HDu21", "HDi1",  "N1",  "HDi2",  "HDi3",  "HDi4",  "HDi5",
			"HDi6",  "HDi7",  "HDi8",  "HDi9",  "HDi10",  "HDi11",  "HDi12",  "HDi13",  "HDi14",  "HDi15",  "HDi16",  "HDi17",
			"HDi18",  "HDi19",  "HDi20",  "HDi21", "C_s", "Warn_S", "Version", "Addr", "Pass", "Rate", "CRC", "CT/T", "H_U", "L_U",
			"HDu", "HDi", "Env_T", "T_P_f_s", "Sw_thr", "Sw_delay", "Disc_delay", "C1",  "C2",  "C3",  "C4",  "C5",  "C6",  "C7",  "C8",  "C9",  "C10",  "C11",  "C12"};
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
                    if(key.equals("Sw_thr") || key.equals("T_P_f_s")){
                        //目标功率因数、投切延时需除以100 方为实际值。
                        batchArgs.put(key, new Float(f / 100f));
                    }else if(key.equals("N0") || key.equals("N1") || key.equals("C_s") || key.equals("Warn_s") || key.equals("Version")
                            || key.equals("Addr") || key.equals("Pass") || key.equals("Rate") || key.equals("CRC") || key.equals("CT/T")
                            || "H_U".equals(key) || "L_U".equals(key) || "Disc_delay".equals(key)){
                        //软件版本识别码、检验位，不允许设置。无检验。
                        batchArgs.put(key, (float)f);
                    }else if(key.equals("P_f_s")){// COS 除以1000 为实际值，如果是超前,减去32768 再除以1000
                        batchArgs.put(key, new Float(f / 1000f));
                    }else {
                        //电压、电流、有功功率、无功功率、谐波、频率、温度除10 为实际值。
                        //C1---C12 为电容容量（KVAR）除10 为实际值；
                        //电压谐波、电流谐波保护值、投切门限需除以10 为实际值。
                        batchArgs.put(key, new Float(f / 10f));
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
	    if(start < 100 || numberOfReg > 83){
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
	public Integer getKey() {
		return 2;
	}

	@Override
	public String getName() {
		return "台州顶峰无功表";
	}

}
