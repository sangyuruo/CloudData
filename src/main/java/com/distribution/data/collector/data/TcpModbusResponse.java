package com.distribution.data.collector.data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baling.fang 于 2017年04月17日.
 */
public class TcpModbusResponse {
    public static final String MAIN_KEY = "main";
    private String serverId;
    private LocalDateTime now;
    private long longcode;
    private int code;
    private Map<String, byte[]> response;

    public long getLongcode() {
        return longcode;
    }

    public void setLongcode(long longcode) {
        this.longcode = longcode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, byte[]> getResponse() {
        if(response == null){
            response = new HashMap<>();
        }
        return response;
    }

    public void setResponse(Map<String, byte[]> response) {
        this.response = response;
    }

    public byte[] getMainData(){
        if(this.response != null){
            return this.response.get(MAIN_KEY);
        }
        return null;
    }

    public  void setMainData(byte[] data){
        this.getResponse().put(MAIN_KEY, data);
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }
}
