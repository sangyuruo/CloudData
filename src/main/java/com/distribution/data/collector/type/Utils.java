package com.distribution.data.collector.type;

import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.domain.ServerStatus;
import com.distribution.data.domain.SmartMeterStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by baling.fang 于 2016年12月21日.
 */
public class Utils {
    public static String getListenerKey(String ip, int port){
        return ip + "@" + ip + ":" + port;
    }

    public static boolean isReinit(LocalDateTime dt){
        String hour = DateUtils.formatDate(dt, "HH");
        String minute = DateUtils.formatDate(dt, "mm");
        int i = (int)Math.floor((Integer.parseInt(DateUtils.formatDate(dt, "ss"))/10)) * 10;
        if(Integer.parseInt(hour) == 0 && Integer.parseInt(minute) == 0 && i ==0){
            return true;
        }
        return false;
    }

    public static void updateStatus(Server ss, int st, boolean check){
        getServerStatus(ss, check);
        ServerStatus status = ss.getStatus();
        status.setLastState(st);
        if(st == ModbusServerManager.STATUS_OKEY)
            status.setSuccess(status.getSuccess() + 1);
        status.setTotal(status.getTotal() + 1);
        status.setLastUpdate(ZonedDateTime.now());
    }

    protected static void getServerStatus(Server ss, boolean check){
        if(check){
            ServerStatus status = new ServerStatus();
            ss.setStatus(status);
            status.initialize();
            status.setServerId(ss.getId());
        }
    }

    public static void updateMeterStatus(TcpModbusRequest tcp, int st, boolean check){
        SmartMeterStatus status = tcp.getMeter().getStatus();
        status.setLastState(st);
        if(st == ModbusServerManager.STATUS_OKEY){
            status.setSuccess(status.getSuccess() + 1);
            tcp.getMeter().getStatus().setLastRetryLevel(-1);
        }
       // status.setSwitchStatus(-1);
        status.setTotal(status.getTotal() + 1);
    }

    public static void updateMeterStatusForStatus(TcpModbusRequest tcp, int st){
        SmartMeterStatus status = tcp.getMeter().getStatus();
        status.setLastState(st);
        if(st == ModbusServerManager.STATUS_OKEY){
            status.setSuccess(status.getSuccess() + 1);
            tcp.getMeter().getStatus().setLastRetryLevel(-1);
        }
    }

    public static void updateMeterStatusForTotal(TcpModbusRequest tcp, boolean check){
        SmartMeterStatus status = tcp.getMeter().getStatus();
        status.setLastState(ModbusServerManager.STATUS_BAD);
        status.setTotal(status.getTotal() + 1);
    }

    public static void checkInit(TcpModbusRequest tcp, boolean check) {
        if(check){
            SmartMeterStatus ss = tcp.getMeter().getStatus();
            SmartMeterStatus sms = new SmartMeterStatus();
            sms.initialize();
            sms.setMeterId(tcp.getMeter().getId());
            tcp.getMeter().setStatus(sms);
            sms.setVolume(ss.getVolume());
        }
    }

    public static void updateStatusForStatus(Server ss, int st){
        ServerStatus status = ss.getStatus();
        status.setLastState(st);
        if(st == ModbusServerManager.STATUS_OKEY)
            status.setSuccess(status.getSuccess() + 1);
        status.setLastUpdate(ZonedDateTime.now());
    }

    public static void updateServerStatusForTotal(Server ss, boolean check) {
        getServerStatus(ss, check);
        ServerStatus status = ss.getStatus();
        status.setLastState(ModbusServerManager.STATUS_PENDING);
        status.setTotal(status.getTotal() + 1);
    }
}
