package com.distribution.data.service;

import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.domain.Company;
import com.distribution.data.service.client.CompanyDTO;
import com.distribution.data.service.client.CompointDTO;
import com.distribution.data.service.client.CpiServiceClient;
import com.distribution.data.service.client.OUServiceClient;
import org.springframework.stereotype.Service;
import sun.misc.Contended;

import javax.inject.Inject;
import java.util.*;

@Contended
@Service
public class ComPointService {

    @Inject
    private CpiServiceClient cpiServiceClient;

    @Inject
    private OUServiceClient ouServiceClient;

    public ComPointService(CpiServiceClient cpiServiceClient, OUServiceClient ouServiceClient) {
        this.cpiServiceClient = cpiServiceClient;
        this.ouServiceClient = ouServiceClient;
    }

    /**
     *  Get all the compoints.
     *
     *  @return the list of entities
     */


    public  List<Server> findAllServer() {
        List<CompanyDTO> companyDTOList = ouServiceClient.getAllCompanies();

        Map<String,Company> companyMap =new HashMap<>();
        Company company =new Company();
        for (CompanyDTO companyDTO :companyDTOList) {
            //Id  => companyCode
            //code=> RegisterCode
            company.setId(UUID.fromString(companyDTO.getCompanyCode()));
            company.setName(companyDTO.getCompanyName());
            company.setAddress(companyDTO.getAddressName());
            company.setEmail(companyDTO.getEmail());
            company.setTelephone(companyDTO.getTelephone());
            companyMap.put(companyDTO.getCompanyCode(),company);
        }

        List<CompointDTO> compointDTOList = cpiServiceClient.getCompoints();
        List<Server> serverList = new ArrayList<>();
        for (CompointDTO cp : compointDTOList) {
            Server server = new Server();
            server.setCode(""+cp.getRegisterCode());
            server.setIp(cp.getIp());
            server.setHostname(cp.getHostName());
            server.setEnable(cp.isEnable());
            server.setEncapsulated(cp.isEncapsulated());
            server.setKeepAlive(cp.isKeepAlive());
            server.setModel(cp.getConnectMode());
            server.setPort(cp.getHostPort());
            server.setReplyTimeout(cp.getReplyTimeout());
            server.setRequestTimeout(cp.getRequestTimeout());
            server.setId(UUID.fromString(cp.getComPointCode()));
            //           server.setStatus(null);
            server.setCompanyId(UUID.fromString(cp.getCompanyCode()));
            server.setCompany(companyMap.get(cp.getCompanyCode()));
            serverList.add(server);

        }
        return serverList;
    }

    /**
     *  Get all the companies.
     *
     *  @param id the pagination information
     *  @param companyId the pagination information
     *  @return the list of entities
     */
    public Server findOneServer( UUID id,  UUID companyId) {

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
        server.setId(UUID.fromString(c.getComPointCode()));
        server.setCompanyId(UUID.fromString(c.getCompanyCode()));
        return server;
    }

    public List<Server> findAllByCompanyId(UUID companyId){

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
            server.setId(UUID.fromString(cp.getComPointCode()));
            //           server.setStatus(null);
            //            server.setCompany();
            server.setCompanyId(UUID.fromString(cp.getCompanyCode()));

            serverList.add(server);

        }
        return serverList;
    }

    public Server findOneById(UUID uuid){

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
