package com.distribution.data.collector.server;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.collector.data.TcpModbusResponse;
import com.distribution.data.collector.data.TcpModbusResult;
import com.distribution.data.collector.event.ServerStatusEvent;
import com.distribution.data.collector.event.TcpModbusEvent;
import com.distribution.data.collector.type.Utils;
import com.distribution.modbus.protocol.msg.*;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import com.distribution.modbus.protocol.polling.entity.func.request.PollingHoldingRegistersRequest;
import com.distribution.modbus.protocol.polling.entity.func.response.PollingHoldingRegistersResponse;
import com.distribution.modbus.protocol.polling.handler.ModbusPollingServerRequestHandler;
import com.distribution.modbus.protocol.queue.ByteQueue;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@ChannelHandler.Sharable
public class PollingServerRequestHandler extends ModbusPollingServerRequestHandler {
    private ApplicationEventPublisher eventPublisher;
    private List<Server> mServer;
    public static final byte[] DLT_645_HEADER = {(byte)0xFE, (byte)0xFE, (byte)0xFE, (byte)0xFE};
    public static Logger logger = LoggerFactory.getLogger(PollingServerRequestHandler.class);

    @Timed
    @Override
    protected PollingHoldingRegistersResponse readHoldingRegistersRequest(PollingHoldingRegistersRequest request) {
        List<byte[]> registers = new ArrayList<>();
        registers.add(Util.hexStr2ByteArray("01"));
        pushData(request);
        return new PollingHoldingRegistersResponse(registers);
    }

    private void pushData(PollingHoldingRegistersRequest req){
    	logger.debug( "start pushData, req id is " + req.getId()) ;
        List<TcpModbusResult> result = new ArrayList<TcpModbusResult>();
        Map<Integer, TcpModbusResponse> map = new HashMap<>();
        Map<Integer, TcpModbusResponse> dlt = new HashMap<>();

        //TODO 处理DLT/645数据
        try {
            for (byte[] bytes : req.getData()) {
                try {
                    byte[] temp = {bytes[0], bytes[1], bytes[2], bytes[3]};
                    if(!Arrays.equals(temp, DLT_645_HEADER)) {
                        TcpModbusResponse res = new TcpModbusResponse();
                        ModbusResponse ms = ModbusResponse.createModbusResponse(new ByteQueue(bytes));
                        if(ms instanceof ReadHoldingRegistersResponse){
                            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)ms;
                            res.setCode(response.getSlaveId());
                            res.setMainData(response.getData());
                            res.setNow(req.getNow());

                        }else if(ms instanceof ReadInputRegistersResponse){
                            ReadInputRegistersResponse response = (ReadInputRegistersResponse)ms;
                            res.setCode(response.getSlaveId());
                            res.setMainData(response.getData());
                            res.setNow(req.getNow());
                        }

                        map.put(res.getCode(), res);
                    }else{
                        int key = Dlt645Utils.parserCode(bytes);
                        TcpModbusResponse res = dlt.get(key);
                        if(res == null){
                            res = new TcpModbusResponse();
                        }
                        res.setCode(key);
                        TcpModbusResponse dr = createDlt645Response(bytes, res);
                        dlt.put(dr.getCode(), dr);
                    }
                } catch (Exception e) {
                    logger.error("上报数据解析异常！串口服务器CODE={}", req.getId());
                }
            }
            Server s = null;
            LocalDateTime now = req.getNow();

            for (Server server : this.mServer) {
                if (server.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET && server.getCode().equals(String.valueOf(req.getId()))) {
                    s = server;
                    updateServerStatus(s);
                    break;
                }
            }
            if(s != null) {
                modbusResponse(result, map, s, now);
                dltResponse(result, dlt, s, now);
            }
        }finally {
            this.eventPublisher.publishEvent(new TcpModbusEvent(result));
        }
    }

    private void dltResponse(List<TcpModbusResult> result, Map<Integer, TcpModbusResponse> dlt, Server s, LocalDateTime now) {
        for (TcpModbusRequest tcp : s.getTcpRequests()) {
            if(tcp.getMeter().getLongcode() == null || tcp.getMeter().getLongcode() <= 0){
                continue;
            }
            TcpModbusResult tr = new TcpModbusResult();
            tr.setRequest(tcp);
            TcpModbusResponse m = dlt.get(tcp.getMeter().getCode());
            if (m != null) {
                logger.debug("串口服务器：{}, 表：{} 数据推送成功！", s.getCode(), tcp.getMeter().getCode());

                tr.setResponse(m);
                tcp.getMeter().getStatus().setLastUpdate(now.atZone(ZoneId.systemDefault()));
                Utils.updateMeterStatusForStatus(tcp, ModbusServerManager.STATUS_OKEY);
            }
            result.add(tr);
        }
    }

    private TcpModbusResponse createDlt645Response(byte[] data, TcpModbusResponse res) {
//        logger.info(Util.bytesToHexString(data));

        Dlt645Utils.minus33h(data);
        Dlt645Utils.parserAddr(data, res);
        Dlt645Utils.parserData(data, res);
        return res;
    }

    private void modbusResponse(List<TcpModbusResult> result, Map<Integer, TcpModbusResponse> map, Server s, LocalDateTime now) {
        //                check =  !Util.isSameDay(s.getStatus().getCreateDate(), now);
        logger.debug("串口服务器:{}, 编码:{} 数据推送成功！", s.getHostname(), s.getCode());
        for (TcpModbusRequest tcp : s.getTcpRequests()) {
            if(tcp.getMeter().getLongcode() != null && tcp.getMeter().getLongcode() > 0){
                continue;
            }
            TcpModbusResult tr = new TcpModbusResult();
            tr.setRequest(tcp);
            TcpModbusResponse m = map.get(tcp.getMeter().getCode());
            tcp.getMeter().getStatus().setLastUpdate(now.atZone(ZoneId.systemDefault()));
            if (m != null) {
                logger.debug("串口服务器：{}, 表：{} 数据推送成功！", s.getCode(), tcp.getMeter().getCode());
                tr.setResponse((m));
                Utils.updateMeterStatusForStatus(tcp, ModbusServerManager.STATUS_OKEY);
            } else {
                if (tcp.getMeter().isEnable() && (tcp.getMeter().getLongcode() == null || tcp.getMeter().getLongcode() <= 0)) {
                    logger.debug("串口服务器：{}, 表：{} 数据未推送数据。", s.getCode(), tcp.getMeter().getCode());
                    Utils.updateMeterStatusForStatus(tcp, ModbusServerManager.STATUS_BAD);
                } else {
                    Utils.updateMeterStatusForStatus(tcp, ModbusServerManager.STATUS_PENDING);
                }
            }
            result.add(tr);
        }
    }


    public void updateServerStatus(Server s){
        Utils.updateStatusForStatus(s, ModbusServerManager.STATUS_OKEY);
        this.eventPublisher.publishEvent(new ServerStatusEvent(s.getStatus()));
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setMServer(List<Server> mServer) {
        this.mServer = mServer;
    }
}
