package com.distribution.data.collector.server;

import com.distribution.modbus.protocol.ModbusMaster;
import com.distribution.modbus.protocol.base.BaseMessageParser;
import com.distribution.modbus.protocol.exception.ModbusInitException;
import com.distribution.modbus.protocol.exception.ModbusTransportException;
import com.distribution.modbus.protocol.ip.IpMessageResponse;
import com.distribution.modbus.protocol.ip.IpParameters;
import com.distribution.modbus.protocol.ip.encap.EncapMessageParser;
import com.distribution.modbus.protocol.ip.encap.EncapMessageRequest;
import com.distribution.modbus.protocol.ip.encap.EncapWaitingRoomKeyFactory;
import com.distribution.modbus.protocol.ip.xa.XaMessageParser;
import com.distribution.modbus.protocol.ip.xa.XaMessageRequest;
import com.distribution.modbus.protocol.ip.xa.XaWaitingRoomKeyFactory;
import com.distribution.modbus.protocol.messaging.*;
import com.distribution.modbus.protocol.msg.ModbusRequest;
import com.distribution.modbus.protocol.msg.ModbusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class MasterListenerConnectionHandler extends ModbusMaster implements Runnable {
	private final Logger LOG = LoggerFactory.getLogger(MasterListenerConnectionHandler.class);
    private Socket socket;
    private Transport transport;
    private MessageControl conn;
    private BaseMessageParser ipMessageParser;
    private WaitingRoomKeyFactory waitingRoomKeyFactory;
    private final IpParameters ipParameters;
    private short retries = 0;
    private int timeout = 800;
    private short nextTransactionId = 0;
    private String id;
    private TcpModbusServerListener serverListener;
    public MasterListenerConnectionHandler(Socket socket, IpParameters ipParameters, int timeout) throws ModbusInitException {
        this.socket = socket;
        this.ipParameters = ipParameters;
    }

    @Override
    public void run() {
        LOG.debug(" ListenerConnectionHandler::run() ");

        if (ipParameters.isEncapsulated()) {
            ipMessageParser = new EncapMessageParser(true);
            waitingRoomKeyFactory = new EncapWaitingRoomKeyFactory();
        }
        else {
            ipMessageParser = new XaMessageParser(true);
            waitingRoomKeyFactory = new XaWaitingRoomKeyFactory();
        }

        try {
            LOG.debug("外部系统接入成功，接入地址: :ipaddr: " + socket.getInetAddress() + ":" + ipParameters.getPort());
            if (getePoll() != null)
                transport = new EpollStreamTransport(socket.getInputStream(), socket.getOutputStream(),
                        getePoll());
            else
                transport = new StreamTransport(socket.getInputStream(), socket.getOutputStream());
        }
        catch (Exception e) {
            LOG.error(
                    "外部系统接入异常：" + ipParameters.getPort() + ", caused by "
                            + e.getLocalizedMessage(), e);
            destroy();
        }
        conn = getMessageControl();
        conn.setExceptionHandler(getExceptionHandler());
        conn.DEBUG = true;
        try {
			conn.start(transport, ipMessageParser, null, waitingRoomKeyFactory);
			if (getePoll() == null)
	            ((StreamTransport) transport).start("Modbus4J TcpMaster");
	        connected = true;
		} catch (IOException e) {
			LOG.error(
                    "外部系统接入异常： " + ipParameters.getPort() + ", caused by "
                            + e.getLocalizedMessage(), e);
			conn.close();
		}

    }



    @Override
    public ModbusResponse sendImpl(ModbusRequest request) throws ModbusTransportException {

        if (!connected) {
            LOG.debug("No connection in Port: " + ipParameters.getPort());
            throw new ModbusTransportException(new Exception("TCP Listener has no active connection!"),
                    request.getSlaveId());
        }

        // Wrap the modbus request in a ip request.
        OutgoingRequestMessage ipRequest;
        if (ipParameters.isEncapsulated()) {
            ipRequest = new EncapMessageRequest(request);
            StringBuilder sb = new StringBuilder();
            for (byte b : Arrays.copyOfRange(ipRequest.getMessageData(), 0, ipRequest.getMessageData().length)) {
                sb.append(String.format("%02X ", b));
            }
            LOG.debug("Encap Request: " + sb.toString());
        }
        else {
            ipRequest = new XaMessageRequest(request, getNextTransactionId());
            StringBuilder sb = new StringBuilder();
            for (byte b : Arrays.copyOfRange(ipRequest.getMessageData(), 0, ipRequest.getMessageData().length)) {
                sb.append(String.format("%02X ", b));
            }
            LOG.debug("Xa Request: " + sb.toString());
        }

        // Send the request to get the response.
        IpMessageResponse ipResponse;
        try {
            // Send data via handler!
            conn.DEBUG = true;
            ipResponse = (IpMessageResponse) conn.send(ipRequest);
            if (ipResponse == null) {
                throw new ModbusTransportException(new Exception("No valid response from slave!"), request.getSlaveId());
            }
            StringBuilder sb = new StringBuilder();
            for (byte b : Arrays.copyOfRange(ipResponse.getMessageData(), 0, ipResponse.getMessageData().length)) {
                sb.append(String.format("%02X ", b));
            }
            LOG.debug("Response: " + sb.toString());
            retries = 0;
            return ipResponse.getModbusResponse();
        }
        catch (Exception e) {
            LOG.debug(e.getLocalizedMessage() + ",  Port: " + ipParameters.getPort() + ", retries: " + retries);
            if (retries < 10 && !e.getLocalizedMessage().contains("Broken")) {
                retries++;
            }else {
                LOG.debug("Restarting Socket,  Port: " + ipParameters.getPort() + ", retries: " + retries);

                // Close the serverSocket first to prevent new messages.
                try {
                    setConnected(false);
                    destroy();
                }
                catch (Exception e2) {
                    LOG.debug("Error closing socket" + e2.getLocalizedMessage(), e);
                    getExceptionHandler().receivedException(e2);
                }
                serverListener.getMapConnections().remove(id);
                retries = 0;
            }
            LOG.warn("Error sending request,  Port: " + ipParameters.getPort() + ", msg: " + e.getMessage());
            // Simple send error!
            throw new ModbusTransportException(e, request.getSlaveId());
        }
    }


    protected short getNextTransactionId() {
        return nextTransactionId++;
    }

	@Override
	public void init() throws ModbusInitException {
		initialized = true;
	}

	@Override
	public void destroy() {
		if (conn != null) {
            LOG.debug("Closing Message Control on port: " + ipParameters.getPort());
            closeMessageControl(conn);
        }

        try {
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
            LOG.debug("Error closing socket on port " + ipParameters.getPort() + ". " + e.getLocalizedMessage());
            getExceptionHandler().receivedException(new ModbusInitException(e));
        }
        connected = false;
        conn = null;
        socket = null;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TcpModbusServerListener getServerListener() {
        return serverListener;
    }

    public void setServerListener(TcpModbusServerListener serverListener) {
        this.serverListener = serverListener;
    }
}
