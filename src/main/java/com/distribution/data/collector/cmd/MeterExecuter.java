package com.distribution.data.collector.cmd;

import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.jobs.DataAcquisitionTask;
import com.distribution.modbus.protocol.exception.ModbusTransportException;
import com.distribution.modbus.protocol.msg.WriteRegistersRequest;
import com.distribution.modbus.protocol.polling.ModbusPollingServer;
import com.distribution.modbus.protocol.polling.entity.ModbusSession;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import com.distribution.modbus.protocol.polling.entity.func.request.WriteMultipleRegistersRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by baling.fang 于 2017年02月17日.
 */
@Component
public class MeterExecuter {
    public static Logger logger = LoggerFactory.getLogger(MeterExecuter.class);
    @Autowired
    private ModbusServerManager config;
    public int commandExecue(String serverCode, long code, String stu){
        int st = -1;
        for (Server ss : config.getMServer()) {
            if(ss.getCode().equals(serverCode)){
                Meter m = null;
                for (Meter meter : ss.getSmartMeters()) {
                    if(meter.getCode() == code){
                        m = meter;
                        break;
                    }
                }
                if(m.getControlAddress() != -1 && stu.length() == 1) {
                    int status = Integer.parseInt(stu);
                    if (ss.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET) {
                        st = executePolling(ss, m, status);
                    } else {
                        st = execute(ss, m, status);
                    }
                }else{
                    if (ss.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET) {
                        st = executePolling(ss, m, stu);
                    } else {
                        //TODO 主站模式与从站模式的命令， 不支持 等功能码
                        //WriteCoilRequest
                    }
                }
                break;
            }
        }
        return st;
    }

    private int execute(Server ss, Meter m, int status) {
        int st = -1;
        try {
            WriteRegistersRequest req = new WriteRegistersRequest(m.getCode(), m.getControlAddress(), new short[]{(short)status});
            synchronized (DataAcquisitionTask.CMDS) {
                DataAcquisitionTask.CMDS.put(ss.getCode(), req);
            }
            st = 1;
        } catch (ModbusTransportException e) {
        }
        return st;
    }

    private int executePolling(Server server, Meter m, String status){
        int st = -1;
        for (ModbusSession ms : ModbusPollingServer.MODBUS_SESSION.values()) {
            if(ms.getServerCode().equals(server.getCode())){
                try {
                    ByteBuf buf = toByteBuf(status);
                    ms.getChannel().writeAndFlush(buf);
                    // buf.release();
//                    Thread.currentThread().sleep(500);
//                    buf = getCmdBuf(m, 0);
//                    ms.getChannel().writeAndFlush(buf);
                    //buf.release();
                    st  = 1;
                }catch (Exception e){
                    logger.error("命令执行异常。");
                }
                break;
            }
        }
        return st;
    }

    private static ByteBuf toByteBuf(String status){
        byte[] by = Util.hexStr2ByteArray(status);
        ByteBuf buf = Unpooled.buffer(by.length);
        for(int i = 0; i < by.length; i++) {
            buf.writeByte(by[i]);
        }
        return buf;
    }

    private int executePolling(Server server, Meter m, int status){
        int st = -1;
        for (ModbusSession ms : ModbusPollingServer.MODBUS_SESSION.values()) {
           if(ms.getServerCode().equals(server.getCode())){
               try {
                   ByteBuf buf = getCmdBuf(m, status);
                   ms.getChannel().writeAndFlush(buf);
                  // buf.release();
                   Thread.currentThread().sleep(500);
                   buf = getCmdBuf(m, 0);
                   ms.getChannel().writeAndFlush(buf);
                   //buf.release();
                   st  = 1;
               }catch (Exception e){
                    logger.error("命令执行异常。");
               }
               break;
           }
        }
        return st;
    }

    private ByteBuf getCmdBuf(Meter m, int status) {
        WriteMultipleRegistersRequest req = new WriteMultipleRegistersRequest(m.getControlAddress(), 1, new int[]{status});
        ByteBuf buf = Unpooled.buffer(req.calculateLength() + 3);
        buf.writeByte(m.getCode());
        buf.writeBytes(req.encode());
        int crc = Util.calculateCRC(buf);
        buf.writeByte((byte) (0xff & (crc >> 8)));
        buf.writeByte((byte) (0xff & crc));
        return buf;
    }


    public static void main(String[] args) {

        WriteMultipleRegistersRequest req = new WriteMultipleRegistersRequest(5, 1, new int[]{2});
        ByteBuf buf = Unpooled.buffer(req.calculateLength() + 3);
        buf.writeByte(47);
        buf.writeBytes(req.encode());
        int crc = Util.calculateCRC(buf);
        buf.writeByte((byte) (0xff & (crc >> 8)));
        buf.writeByte((byte) (0xff & crc));
        System.out.println(Util.bytesToHexString(buf.array()));

        System.out.println(Util.bytesToHexString(toByteBuf("0210000500010200017335").array()));
    }
}
