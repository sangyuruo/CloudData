package com.distribution.data.service;

import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.service.client.CompointDTO;
import com.distribution.data.service.client.CpiServiceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComPointService {
    private CpiServiceClient cpiServiceClient;


    public ComPointService(CpiServiceClient cpiServiceClient) {
        this.cpiServiceClient = cpiServiceClient;
    }



    public  List<Server> findAllServer() {
        List<CompointDTO> compointDTOList = cpiServiceClient.getAllCompoints();
        List<Server> serverList =new ArrayList<>();

        for(CompointDTO cp : compointDTOList){
            Server server=new Server();
            server.setCode(cp.getComPointCode());
            server.setIp(cp.getIp());
            server.setHostname(cp.getHostName());
            server.setEnable(cp.isEnable());
            server.setEncapsulated(cp.isEncapsulated());
            server.setKeepAlive(cp.isKeepAlive());
            server.setModel(cp.getConnectMode());
            server.setPort(cp.getHostPort());
            server.setReplyTimeout(cp.getReplyTimeout());
            server.setRequestTimeout(cp.getRequestTimeout());
            server.setId(cp.getId());
            //           server.setStatus(null);
            //            server.setCompany();
            server.setCompanyId(cp.getCompanyCode());

            serverList.add(server);

        }
        return serverList;
    }

    public Server findOneServer( UUID id,  UUID companyId) {

        CompointDTO c= cpiServiceClient.getCompoint(id.toString(), companyId.toString());

        Server server=new Server();
        server.setCode(c.getComPointCode());
        server.setIp(c.getIp());
        server.setHostname(c.getHostName());
        server.setEnable(c.isEnable());
        server.setEncapsulated(c.isEncapsulated());
        server.setKeepAlive(c.isKeepAlive());
        server.setModel(c.getConnectMode());
        server.setPort(c.getHostPort());
        server.setReplyTimeout(c.getReplyTimeout());
        server.setRequestTimeout(c.getRequestTimeout());
        return server;
    }

    public List<Server> findAllByCompanyId(UUID companyId){

        List<CompointDTO> cplist = cpiServiceClient.getAllByCompanyCode(companyId.toString());
        List<Server> serverList =new ArrayList<>();
        for(CompointDTO cp : cplist){
            Server server=new Server();
            server.setCode(cp.getComPointCode());
            server.setIp(cp.getIp());
            server.setHostname(cp.getHostName());
            server.setEnable(cp.isEnable());
            server.setEncapsulated(cp.isEncapsulated());
            server.setKeepAlive(cp.isKeepAlive());
            server.setModel(cp.getConnectMode());
            server.setPort(cp.getHostPort());
            server.setReplyTimeout(cp.getReplyTimeout());
            server.setRequestTimeout(cp.getRequestTimeout());
            server.setId(cp.getId());
            //           server.setStatus(null);
            //            server.setCompany();
            server.setCompanyId(cp.getCompanyCode());

            serverList.add(server);

        }
        return serverList;
    }

    public Server findOneById(UUID uuid){

        CompointDTO c= cpiServiceClient.findOneById(uuid.toString());

        Server server=new Server();
        server.setCode(c.getComPointCode());
        server.setIp(c.getIp());
        server.setHostname(c.getHostName());
        server.setEnable(c.isEnable());
        server.setEncapsulated(c.isEncapsulated());
        server.setKeepAlive(c.isKeepAlive());
        server.setModel(c.getConnectMode());
        server.setPort(c.getHostPort());
        server.setReplyTimeout(c.getReplyTimeout());
        server.setRequestTimeout(c.getRequestTimeout());
        return server;
    }
}
