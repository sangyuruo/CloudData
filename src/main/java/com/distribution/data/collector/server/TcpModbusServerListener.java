package com.distribution.data.collector.server;

import com.distribution.data.collector.type.Utils;
import com.distribution.modbus.protocol.ip.IpParameters;
import com.distribution.modbus.protocol.polling.entity.func.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class TcpModbusServerListener extends Thread {
    // Configuration fields.
    private final Logger LOG = LoggerFactory.getLogger(TcpModbusServerListener.class);
    private final IpParameters ipParameters;
    private int timeout;
    // Runtime fields.
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    final Map<String, MasterListenerConnectionHandler> mapConnections = new HashMap<String, MasterListenerConnectionHandler>();
    public TcpModbusServerListener(IpParameters params, boolean keepAlive, int timeout) {
        LOG.debug("Creating TcpListener in port " + params.getPort());
        this.timeout = timeout == 0 ? 800 : timeout;
        ipParameters = params;
        LOG.debug("TcpListener created! Port: " + ipParameters.getPort());
    }

    @SuppressWarnings(value="deprecation")
    public void destroy() {
        // Close the socket first to prevent new messages.
        try {
            if(serverSocket != null)
                serverSocket.close();
        }
        catch (IOException e) {
        	LOG.debug("Error terminating executorService - " + e.getLocalizedMessage());
        }

        // Close all open connections.
        synchronized (mapConnections) {
            for (String key : mapConnections.keySet()){
            	MasterListenerConnectionHandler tch = mapConnections.get(key);
            	LOG.debug("Close the {} client on the modbus server!", key);
            	try {
                    if (tch != null)
                        tch.destroy();
                }catch (Exception e){
            	    LOG.error("关闭连接时发生错误！");
                }
            }
            mapConnections.clear();
        }

        // Now close the executor service.
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
            LOG.debug("Handler Thread terminated,  Port: " + ipParameters.getPort());
        }
        catch (InterruptedException e) {
        	LOG.debug("Error terminating executorService - " + e.getLocalizedMessage());
        }
    }

	public Map<String, MasterListenerConnectionHandler> getMapConnections() {
		return mapConnections;
	}

	@Override
	public void run() {
		LOG.debug("Init TcpListener Port: " + ipParameters.getPort());
        executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(ipParameters.getPort());

            Socket socket;
            while (true) {
                socket = serverSocket.accept();
                MasterListenerConnectionHandler handler = new MasterListenerConnectionHandler(socket, ipParameters, timeout);
                handler.setServerListener(this);
                String key = readIdFromSocket(socket);
                executorService.execute(handler);
                synchronized (mapConnections) {
                	if(key == null) {
                        key = Utils.getListenerKey(socket.getInetAddress().getHostAddress(), ipParameters.getPort());
                    }
                    handler.setId(key);
                    LOG.info("Registed the modbus server, :ipaddr: ID = " + key);
                    MasterListenerConnectionHandler old = mapConnections.get(key);
                    if(old != null){
                        try {
                            mapConnections.remove(key);
                            old.destroy();
                        }catch (Exception e){
                            LOG.error("连接关闭异常！");
                        }
                    }
                	mapConnections.put(key, handler);
                }
            }
        }
        catch (Exception e) {
        	LOG.error("Error terminating executorService - " + e.getLocalizedMessage());
        }
	}

//    public static short readShort(InputStream stream) throws IOException {
//        int ch1 = stream.read();
//        int ch2 = stream.read();
//        if ((ch1 | ch2) < 0) {
//            throw new IOException();
//        }
//        return (short)((ch1 << 8) + (ch2 << 0));
//    }
//
//    public static int getTrueCode(int x){
//        return ((Short.MAX_VALUE * 2 + 1) - x);
//    }

    private String readIdFromSocket(Socket socket) {
        String  id = null;
        try {
           InputStream in = socket.getInputStream();
           socket.setSoTimeout(1000);
           short t = Util.readShort(in);
           short c = Util.readShort(in);
           id = "" + Util.getTrueCode(t, c);
        } catch (IOException e) {
            LOG.warn("The ID of device is not found!");
        }
        return id;
    }
//
//    public static short readShort(byte[] by, int offset) throws IOException {
//        int ch1 = by[offset];
//        int ch2 = by[offset + 1];
//        return (short)((ch1 << 8) + (ch2 << 0));
//    }

}
