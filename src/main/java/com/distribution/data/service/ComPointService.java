package com.distribution.data.service;

import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.service.client.CompointDTO;
import com.distribution.data.service.client.CpiServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Contended;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Contended
@Service
public class ComPointService {

    @Inject
    private CpiServiceClient cpiServiceClient;


    public ComPointService(CpiServiceClient cpiServiceClient) {
        this.cpiServiceClient = cpiServiceClient;
    }


<<<<<<< HEAD

    /**
     *  Get all the compoints.
     *
     *  @return the list of entities
     */

    @Inject
    public  List<Server> findAllServer() {
=======
    public List<Server> findAllServer() {
>>>>>>> 0a722c49eaf482466060a4b155724cb3cf97d1cd
        List<CompointDTO> compointDTOList = cpiServiceClient.getAllCompoints();
        List<Server> serverList = new ArrayList<>();

        for (CompointDTO cp : compointDTOList) {
            Server server = new Server();
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

<<<<<<< HEAD
    /**
     *  Get all the companies.
     *
     *  @param id the pagination information
     *  @param companyId the pagination information
     *  @return the list of entities
     */

    @Inject
    public Server findOneServer( UUID id,  UUID companyId) {
=======
    public Server findOneServer(UUID id, UUID companyId) {
>>>>>>> 0a722c49eaf482466060a4b155724cb3cf97d1cd

        CompointDTO c = cpiServiceClient.getCompoint(id.toString(), companyId.toString());

        Server server = new Server();
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
        server.setId(c.getId());
        server.setCompanyId(c.getCompanyCode());
        return server;
    }

<<<<<<< HEAD

    @Inject
    public List<Server> findAllByCompanyId(UUID companyId){
=======
    public List<Server> findAllByCompanyId(UUID companyId) {
>>>>>>> 0a722c49eaf482466060a4b155724cb3cf97d1cd

        List<CompointDTO> cplist = cpiServiceClient.getAllByCompanyCode(companyId.toString());
        List<Server> serverList = new ArrayList<>();
        for (CompointDTO cp : cplist) {
            Server server = new Server();
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

<<<<<<< HEAD
    @Inject
    public Server findOneById(UUID uuid){
=======
    public Server findOneById(UUID uuid) {
>>>>>>> 0a722c49eaf482466060a4b155724cb3cf97d1cd

        CompointDTO c = cpiServiceClient.findOneById(uuid.toString());

        Server server = new Server();
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
