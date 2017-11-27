package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class JLEnergyMeter implements IMeterParser {
	private final Logger logger = LoggerFactory.getLogger(JLEnergyMeter.class);
	public static final String[] strKeys = new String[]{"W_p", "U_a", "I_a", "P_a", "P_f_s", "F_f"};
	@Override
	public void parser(Map<String, Float> batchArgs, byte[] data, int start, int numberOfReg, Map<String, String> calculates, Map<String, String> auxiliary) {
		if(data != null && validate(start, numberOfReg)) {
            String key = "";
            try {
                key = strKeys[0];
                Float v = (float) com.distribution.data.collector.type.NumberUtils.readFloat(data, 0);
                batchArgs.put(key, v);
            } catch (Exception e) {
                logger.error("数据解析错误: {}", e.getLocalizedMessage());
                batchArgs.put(key, 0f);
            }
        }
	}

	@Override
    public Float getVolume(Map<String, Float> value, Float oldValue){
        Float v = value.get(getVolumeKey());
        if(v != null && (oldValue == null || v >= oldValue)){
            value.put(strKeys[1], getVoltage());
            if(oldValue != null && oldValue > 0 && v > 0){
                Float load = getLoad(v - oldValue);
                value.put(strKeys[3], load);
                Float i = load / value.get(strKeys[1]);
                value.put(strKeys[2], i);
                value.put(strKeys[4], getFactor());
            }else{
                value.put(strKeys[2], 0f);
                value.put(strKeys[3], 0f);
                value.put(strKeys[4], 1f);
            }
            value.put(strKeys[5], getFrequency());
            return v;
        }
        return null;
    }

    private Float getLoad(float en){
        Float v = en * 120;

        return v;
    }

    private Float getFactor(){
        Float v = (float)Math.random();
        v = v < 0.5 ? 0.5f : v;
        v =  v > 1 ? 1f : v;
        return v;
    }

    private Float getVoltage(){
        Float v = 220f;
        int i = (rad.nextInt()%10) - 10;
        v += i;
        return v;
    }


    public static Random rad = new Random() ;

    private Float getFrequency(){
        Float v = 50.0f;
        Float i = Float.valueOf((rad.nextInt()%10 - 5) / 100);
        v += i;
        return v;
    }

    @Override
    public boolean validate(int start, int numberOfReg) {
	    if(start < 256 || numberOfReg > 4){
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
		return 4;
	}

	@Override
	public String getName() {
		return "金来直通电表";
	}

}
