package com.distribution.data.collector.data;

import com.distribution.data.collector.cassadra.dao.MeterService;
import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.event.ModbusReloadEvent;
import com.distribution.data.collector.server.MasterListenerConnectionHandler;
import com.distribution.data.collector.server.PollingServerRequestHandler;
import com.distribution.data.collector.server.TcpModbusServerListener;
import com.distribution.data.collector.type.Utils;
import com.distribution.data.domain.ModbusServer;
import com.distribution.data.domain.ServerStatus;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.domain.SmartMeterStatus;
import com.distribution.data.repository.ServerStatusRepository;
import com.distribution.data.repository.SmartMeterStatusRepository;
import com.distribution.data.service.ComPointService;
import com.distribution.modbus.protocol.ModbusMaster;
import com.distribution.modbus.protocol.ip.IpParameters;
import com.distribution.modbus.protocol.msg.ReadHoldingRegistersRequest;
import com.distribution.modbus.protocol.msg.ReadInputRegistersRequest;
import com.distribution.modbus.protocol.polling.ModbusPollingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ModbusServerManager implements ApplicationListener<ModbusReloadEvent>, ApplicationEventPublisherAware {
    public static final int STATUS_OKEY = 1;
    public static final int STATUS_BAD = 0;
    public static final int STATUS_PENDING = -1;
    private static Logger logger = LoggerFactory.getLogger(ModbusServerManager.class);
    public static final Map<Integer, IMeterParser> meterParser = new HashMap<Integer, IMeterParser>();
    private Map<String, ModbusMaster> connections = new HashMap<String, ModbusMaster>();
    @Autowired
    private ComPointService serverDao;
    @Autowired
    private ServerStatusRepository serverStatusRepository;
    @Autowired
    private SmartMeterStatusRepository smartMeterStatusRepository;
    @Autowired
    private MeterService meterDao;

    @Autowired
    private ComPointService comPointService;

    private ApplicationEventPublisher eventPublisher;

    private Map<Integer, TcpModbusServerListener> modbusServer = new HashMap<Integer, TcpModbusServerListener>();
    private Map<Integer, ModbusPollingServer> modbusPollingServer = new HashMap<Integer, ModbusPollingServer>();
    private List<Server> mServer;

    @PostConstruct
    public void init() {

        StopWatch watch = new StopWatch();
        watch.start();
        mServer = serverDao.findAllServer();
       synchronized (mServer) {
            for (Server current : mServer) {
                initModbusServer(current);
            }
        }
        watch.stop();
        logger.debug("Initialized the modbus config in {} ms", watch.getTotalTimeMillis());

    }

    protected void initModbusServer(Server current) {
        current.getIpParameters();
        List<Meter> sms = meterDao.findMetersByServerId(current.getId());
        current.setSmartMeters(sms);
        logger.info("注册到系统中的服务器：" + current.getHostname() + ", IP: " + current.getIp() + ", 端口：" + current.getPort() + "。");
        initTcpModbusRequest(current);
        if (current.getModel() != null && current.getModel() == Server.MODBUS_SERVER_MODEL_SLAVE) {
            initModbusSlave(current.getIpParameters(), current.isKeepAlive(), current.getReplyTimeout());
        } else if (current.getModel() != null && current.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET) {
            initModbusSocket(current.getIpParameters());
        }
        LocalDateTime now = LocalDateTime.now();
        Optional<ServerStatus> ss = serverStatusRepository.findOneByName(current.getId(), now, now);
        if (!ss.isPresent()) {
            ss = Optional.ofNullable(new ServerStatus());
            ss.get().initialize();
            ss.get().setServerId(current.getId());
        }
        ss.get().setLastState(-1);
        current.setStatus(ss.get());
    }

    public void initModbusSocket(IpParameters params) {
        ModbusPollingServer mm = modbusPollingServer.get(params.getPort());
        try {
            if (mm == null) {
                mm = new ModbusPollingServer(params.getPort());
                PollingServerRequestHandler handler = new PollingServerRequestHandler();
                handler.setEventPublisher(this.eventPublisher);
                handler.setMServer(this.mServer);
                mm.setup(handler);
                modbusPollingServer.put(params.getPort(), mm);
            }
        } catch (Exception e) {
            logger.error("端口监听异常：  端口： " + params.getPort());
        }
    }


    public void initModbusSlave(IpParameters params, boolean keepAlive, int timeout) {
        TcpModbusServerListener mm = modbusServer.get(params.getPort());
        try {
            if (mm == null) {
                mm = new TcpModbusServerListener(params, keepAlive, timeout);
                modbusServer.put(params.getPort(), mm);
                mm.start();
            }
        } catch (Exception e) {
            logger.error("端口监听异常：  端口： " + params.getPort());
        }
    }

    public void initTcpModbusRequest(Server current) {
        List<TcpModbusRequest> tcpRequest = new ArrayList<TcpModbusRequest>();
        if (current.getSmartMeters() != null) {
            for (Meter ms : current.getSmartMeters()) {
                initSmartMeter(current, tcpRequest, ms);
            }
        }
        current.setTcpRequests(tcpRequest);
    }

    protected void initSmartMeter(Server current, List<TcpModbusRequest> tcpRequest, Meter ms) {
        try {
            ms.setServer(current);

            TcpModbusRequest request = new TcpModbusRequest();
            if (ms.getFunc() == 3) {
                ReadHoldingRegistersRequest req = new ReadHoldingRegistersRequest(ms.getCode(), ms.getStartOffset(), ms.getNumberOfRegisters());
                request.setRequest(req);
            } else if (ms.getFunc() == 4) {
                ReadInputRegistersRequest req = new ReadInputRegistersRequest(ms.getCode(), ms.getStartOffset(), ms.getNumberOfRegisters());
                request.setRequest(req);
            }
            LocalDateTime st = LocalDateTime.now();
            Optional<SmartMeterStatus> sms = smartMeterStatusRepository.findOneByName(ms.getId(), st, st);
            if (!sms.isPresent()) {
                sms = Optional.ofNullable(new SmartMeterStatus());
                sms.get().initialize();
                sms.get().setMeterId(ms.getId());
            }
            request.setIpParams(current.getIpParameters());
            sms.get().setLastState(-1);
            ms.setStatus(sms.get());
            request.setMeter(ms);
            tcpRequest.add(request);
        } catch (Exception e) {
            logger.error("初始化表计异常：串口编号-{}，表计编号-{}。" + e.getMessage(), current.getCode(), ms.getCode());
        }
    }

    public Map<Integer, TcpModbusServerListener> getModbusServer() {
        return modbusServer;
    }

    public Iterable<Server> getMServer() {
        return mServer;
    }

    protected void resetSmartMeter(ModbusReloadEvent event) {
        SmartMeter sm = (SmartMeter) event.getSource();
        synchronized (mServer) {
            Server temp = null;
            for (Server server : mServer) {
                if (server.getId().equals(sm.getServerId())) {
                    temp = server;
                    break;
                }
            }
            if (event.getOperator() != ModbusReloadEvent.OPERATOR_ADD && temp != null) {
                List<Meter> list = (List<Meter>) temp.getSmartMeters();
                for (Meter meter : list) {
                    if (meter.getId().equals(sm.getId())) {
                        list.remove(meter);
                        break;
                    }
                }
                List<TcpModbusRequest> tcpRequest = temp.getTcpRequests();
                for (TcpModbusRequest tcp : tcpRequest) {
                    if (tcp.getMeter().getId().equals(sm.getId())) {
                        tcpRequest.remove(tcp);
                        break;
                    }
                }
            }

            if (temp != null) {
                if (event.getOperator() != ModbusReloadEvent.OPERATOR_DELETE) {
                    Meter me = meterDao.findOneMeter(sm.getId(), sm.getServerId(), sm.getCode());
                    List<Meter> list = (List<Meter>) temp.getSmartMeters();
                    initSmartMeter(temp, temp.getTcpRequests(), me);
                    list.add(me);
                }
            } else {
                //Never
                Server s = serverDao.findOneServer(sm.getServerId(), sm.getCompanyId());
                initModbusServer(s);
                mServer.add(s);
            }
        }
    }

    protected void resetModbusServer(ModbusReloadEvent event) {
//        if(ms.getModel() == Server.MODBUS_SERVER_MODEL_SLAVE){
        // releaseSlave(ms, event.isNew());
        synchronized (mServer) {
            ModbusServer ms = (ModbusServer) event.getSource();
            if (event.getOperator() != ModbusReloadEvent.OPERATOR_ADD) {
                for (Server server : mServer) {
                    if (server.getCode().equals(ms.getCode()) || server.getId().equals(ms.getId())) {
                        try {
                            if (ms.getModel() == Server.MODBUS_SERVER_MODEL_MASTER) {
                                ModbusMaster mods = connections.get(server.getId().toString());
                                if (mods != null) {
                                    mods.destroy();
                                }
                            }
                        } catch (Exception e) {
                            logger.error("关闭串口服务器出错: id={}, exception={}", server.getCode(), e.getLocalizedMessage());
                        } finally {
                            connections.remove(server.getId().toString());
                            mServer.remove(server);
                        }
                        break;
                    }
                }
            }
            if (event.getOperator() != ModbusReloadEvent.OPERATOR_DELETE) {
                Server s = serverDao.findOneServer(ms.getId(), ms.getCompanyId());
                initModbusServer(s);
                mServer.add(s);
            }
        }
//        }
    }

//    /**
//     * 此方法待完善。
//     * @param releaseSlave
//     * @param isNew
//     */
//    protected void releaseSlave(ModbusSlave releaseSlave, boolean isNew) {
//        synchronized (modbusServer) {
//            if(!isNew){
//                for (Integer i : modbusServer.keySet()) {
//                    TcpModbusServerListener ss = modbusServer.get(i);
//                    for (String k : ss.getMapConnections().keySet()) {
//                        MasterListenerConnectionHandler h = ss.getMapConnections().get(k);
//                        try {
//                            //TODO 换code的情况未考虑。很难处理这个问题，因此，提前断开所有外部连接。
//                            if (h.getId().equals(releaseSlave.getCode()) || h.getId().equals(releaseSlave.getIp() + "@" + releaseSlave.getIp() + ":" + releaseSlave.getPort())) {
//                                h.destroy();
//                                ss.getMapConnections().remove(k);
//                                ss.getMapConnections().remove(releaseSlave.getIp() + "@" + releaseSlave.getIp() + ":" + releaseSlave.getPort());
//                                break;
//                            }
//                        } catch (Exception e) {
//                            logger.error("关闭异常：{} ", h);
//                        }
//                    }
//                    if(ss.getMapConnections().isEmpty() && i != releaseSlave.getPort().intValue()){
//                        modbusServer.remove(i);
//                    }
//                }
//            }
//        }
//    }

    @Override
    //@Async
    public void onApplicationEvent(ModbusReloadEvent event) {
        logger.debug("重新加载采集配置：{} 设置有改动。", (event.getType() == ModbusReloadEvent.MODBUS_SERVER ? "串口服务器" : "智能电表"));
        try {
            synchronized (modbusServer) {
                if (event.getType() == ModbusReloadEvent.MODBUS_SERVER && event.getOperator() != ModbusReloadEvent.OPERATOR_ADD) {
                    ModbusServer ms = (ModbusServer) event.getSource();
                    if (ms.getModel() == Server.MODBUS_SERVER_MODEL_SLAVE) {
                        MasterListenerConnectionHandler handler = null;
                        for (TcpModbusServerListener tcpModbusServerListener : modbusServer.values()) {
                            handler = tcpModbusServerListener.getMapConnections().get(ms.getCode());
                            if (handler == null) {
                                tcpModbusServerListener.getMapConnections().get(Utils.getListenerKey(ms.getIp(), ms.getPort()));
                            }
                            if (handler != null) {
                                try {
                                    handler.destroy();
                                } catch (Exception e) {
                                    logger.error("关闭异常：{} ", handler.getId());
                                } finally {
                                    tcpModbusServerListener.getMapConnections().remove(handler.getId());
//                                    tcpModbusServerListener.getMapConnections().remove(Utils.getListenerKey(ms.getIp(), ms.getPort()));
                                }
                                break;
                            }
                        }
//                        if(handler == null){
//                            destroySlave();
//                        }
                    } else if (ms.getModel() == Server.MODBUS_SERVER_MODEL_SOCKET) {
                        //TODO
//                        for (ModbusPollingServer ps : modbusPollingServer.values()) {
//                           ps.getClientChannels().close();
//                        }
                    }
                }
            }
        } finally {
            //init();
            if (event.getType() == ModbusReloadEvent.MODBUS_SERVER) {
                resetModbusServer(event);
            } else if (event.getType() == ModbusReloadEvent.MODBUS_METER) {
                resetSmartMeter(event);
            }
        }
    }

    @PreDestroy
    protected void destroySocket() {
        for (Integer key : modbusPollingServer.keySet()) {
            ModbusPollingServer ss = modbusPollingServer.get(key);
            if (ss != null) {
                ss.close();
            }
        }
    }

    @PreDestroy
    protected void destroySlave() {
        for (Integer server : modbusServer.keySet()) {
            TcpModbusServerListener ss = modbusServer.get(server);
            if (ss != null) {
                for (MasterListenerConnectionHandler h : ss.getMapConnections().values()) {
                    try {
                        h.destroy();
                    } catch (Exception e) {
                        logger.error("关闭异常：{} ", h.getId());
                    }
                }
                ss.getMapConnections().clear();
            }
            ss.destroy();
        }
    }

    public Map<String, ModbusMaster> getConnections() {
        return connections;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public ComPointService getComPointService() {
        return comPointService;
    }

    public void setComPointService(ComPointService comPointService) {
        this.comPointService = comPointService;
    }
}
