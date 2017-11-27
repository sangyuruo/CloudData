package com.distribution.data.collector.data.serializer;

import com.distribution.data.collector.data.IMeterParser;
import com.distribution.data.collector.server.Dlt645Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Dlt6452007Meter implements IMeterParser {
	public static final String[] strKeys = new String[]{"St", "U_a", "U_b", "U_c", "U_ab", "U_bc", "U_ca", "I_a", "I_b", "I_c", "P_a", "P_b", "P_c", "P_s", "Q_a", "Q_b", "Q_c", "Q_s", "S_s", "P_f_s", "F_f", "E_p", "N0", "E_q", "N1", "W_p", "N2", "W_q", "N3", "D_O", "D_I" };
	private final Logger logger = LoggerFactory.getLogger(Dlt6452007Meter.class);

	@Override
	public void parser645(Map<String, Float> dataMap, Map<String, byte[]> data, String s, Map<String, String> calculates, Map<String, String> auxiliary){
        float urate = 1;
        float irate = 1;
        if(calculates != null ) {
            try {
                if(calculates.get("U") != null && calculates.get("I") != null) {
                    urate = Float.parseFloat(calculates.get("U"));
                    irate = Float.parseFloat(calculates.get("I"));
                }
            }catch (Exception e){
                logger.error("DL/T 645变比设置有误：{}, {}", calculates.get("U"), calculates.get("I"));
            }
        }
        if("2007".equalsIgnoreCase(s)){
            for (String key : data.keySet()) {
                if("0000FF00".equalsIgnoreCase(key)){ //电量  4字节，x 800
                    int f = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 4));
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 4, 4));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 8, 4));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 12, 4));
                    int d = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 16, 4));
                    f = f >= 80000000 ? 80000000 - f : f;
                    a = a >= 80000000 ? 80000000 - a : a;
                    b = b >= 80000000 ? 80000000 - b : b;
                    c = c >= 80000000 ? 80000000 - c : c;
                    d = d >= 80000000 ? 80000000 - d: d;
                    dataMap.put("W_p", (f * urate * irate) / 100);
                    dataMap.put("W_1", (a * urate * irate) / 100);
                    dataMap.put("W_2", (b * urate * irate) / 100);
                    dataMap.put("W_3", (c * urate * irate) / 100);
                    dataMap.put("W_4", (d * urate * irate) / 100);
                }else if("0201FF00".equalsIgnoreCase(key)){ //电压 2字节， x 100
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 2));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 2, 2));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 4, 2));
                    dataMap.put("U_a", (a * urate) / 10);
                    dataMap.put("U_b", (b * urate) / 10);
                    dataMap.put("U_c", (c * urate) / 10);
                }else if("0202FF00".equalsIgnoreCase(key)) {//电流 3字节, x 8
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 3));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 3, 3));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 6, 3));
                    a = a >= 800000 ? 800000 - a : a;
                    b = b >= 800000 ? 800000 - b : b;
                    c = c >= 800000 ? 800000 - c : c;
                    dataMap.put("I_a", (a * irate) / 1000);
                    dataMap.put("I_b", (b * irate) / 1000);
                    dataMap.put("I_c", (c * irate) / 1000);
                }else if("0203FF00".equalsIgnoreCase(key)) {//有功 3字节, x 800
                    int p = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 3));
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 3, 3));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 6, 3));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 9, 3));
                    p = p >= 800000 ? 800000 - p : p;
                    a = a >= 800000 ? 800000 - a : a;
                    b = b >= 800000 ? 800000 - b : b;
                    c = c >= 800000 ? 800000 - c : c;
                    dataMap.put("P", (p * urate * irate) / 1000);
                    dataMap.put("P_a", (a * urate * irate) / 1000);
                    dataMap.put("P_b", (b * urate * irate) / 1000);
                    dataMap.put("P_c", (c * urate * irate) / 1000);
                }else if("0204FF00".equalsIgnoreCase(key)) {//无功 3字节， x 800
                    int q = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 3));
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 3, 3));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 6, 3));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 9, 3));
                    q = q >= 800000 ? 800000 - q : q;
                    a = a >= 800000 ? 800000 - a : a;
                    b = b >= 800000 ? 800000 - b : b;
                    c = c >= 800000 ? 800000 - c : c;
                    dataMap.put("Q", (q * urate * irate) / 1000);
                    dataMap.put("Q_a", (a * urate * irate) / 1000);
                    dataMap.put("Q_b", (b * urate * irate) / 1000);
                    dataMap.put("Q_c", (c * urate * irate) / 1000);
                }else if("0206FF00".equalsIgnoreCase(key)) {//功率因素 2字节
                    int ps = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 2));
                    int a = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 2, 2));
                    int b = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 4, 2));
                    int c = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 6, 2));
                    ps = ps >= 8000 ? 8000 - ps : ps;
                    a = a >= 8000 ? 8000 - a : a;
                    b = b >= 8000 ? 8000 - b : b;
                    c = c >= 8000 ? 8000 -c : c;
                    dataMap.put("P_A_f_s", Float.valueOf(a / 1000));
                    dataMap.put("P_B_f_s", Float.valueOf(b / 1000));
                    dataMap.put("P_C_f_s", Float.valueOf(c / 1000));
                    dataMap.put("P_f_s", Float.valueOf(ps / 1000));
                }else if("02800002".equalsIgnoreCase(key)) {//频率 2字节
                    int ff = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 2));
                    dataMap.put("F_f", Float.valueOf(ff/ 100));
                }else if("040005FF".equalsIgnoreCase(key)) {//状态 2字节 //fefefefe6801000000000068910632383337333aa916
                    //00007
                    int lenth = data.get(key).length;
                    for (int i = 0; i < lenth; i++) {
                        int ff = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), i, 1));
                        dataMap.put("St" + (i + 1), Float.valueOf(ff));
                    }

//                }else if("0290FF00".equalsIgnoreCase(key)) {//剩余电流 2字节  //fefefefe680100000000006891073332c3353756348716
//                    //042301
//                    int phase = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 0, 1));
//                    int current = Integer.parseInt(Dlt645Utils.toReverseHexString(data.get(key), 1, 2));
//                    dataMap.put("Leakage_phase", Float.valueOf(phase));
//                    dataMap.put("Leakage_I", Float.valueOf(current));
                }
            }
        }
    }

    @Override
	public Integer getKey() {
		return 50;
	}

	@Override
	public String getName() {
		return "DL/T 645-2007电表";
	}

}
