package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.dao.MeterDataService;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.cmd.MeterExecuter;
import com.distribution.data.collector.data.ModbusServerManager;
import com.distribution.data.collector.data.TcpModbusRequest;
import com.distribution.data.domain.Company;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.service.MeterInfoService;
import com.distribution.modules.ws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class RsDataResource {
	private static Logger logger = LoggerFactory.getLogger(RsDataResource.class);

    /*@Inject
    private CompanyRepository companyRepository;*/

    @Inject
    private MeterInfoService smartMeteRepository;

	@Inject
	private ModbusServerManager manager;

	@Inject
	private MeterDataService meterDataService;

	@Inject
	private MeterExecuter meterExecuter;

    /**
     * 执行命令，控制开关用。
     * @param serverCode
     * @param code
     * @param status
     * @return
     */
    @GetMapping("/rs/{serverCode}/{code}/{status}")
    @Timed
    public int cmd(@PathVariable String serverCode, @PathVariable int code, @PathVariable String status){
        return meterExecuter.commandExecue(serverCode, code, status);
    }

    /**
     * 数据采集用，按时间查询。
     * @param ymd
     * @param hour
     * @param minute
     * @param sec
     * @return
     */
    @GetMapping("/rsdata/{ymd}/{hour}/{minute}/{sec}")
    @Timed
	public FindRsDataResponse findRsData(@PathVariable String ymd, @PathVariable int hour, @PathVariable int minute, @PathVariable int sec) {
		FindRsDataResponse response = new FindRsDataResponse();
		List<MeterData> list = meterDataService.findMeterDataByIdForService(ymd, hour, minute, sec);
		response.getMeterData().clear();
		response.getMeterData().addAll(list);
		return response;
	}

    /**
     * 设备状态用，公司ID为数据平台的ID，如果为NONE，则查询所有数据。
     * 如果要查某个公司，需要对照。
     * @param companyId
     * @return
     */
	@GetMapping("/rsrd/{companyId}")
    @Timed
	public FindRsDataResponse findRsRealData(@PathVariable String companyId) {
        FindRsDataResponse response = new FindRsDataResponse();
        response.getMeterData().clear();
		LocalDateTime dt = LocalDateTime.now();
        List<MeterData> list = null;
        if(!"NONE".equalsIgnoreCase(companyId)){
            list = meterDataService.findMeterDataByIdAndCompanyIdForService(dt, UUID.fromString(companyId));
        }else{
            list = meterDataService.findMeterDataByIdAndCompanyIdForService(dt, null);
        }
		response.getMeterData().addAll(list);
		return response;
	}

    /**
     * 按串口编码ID查询智能电表。如果为NONE，则查询所有电表。
     * @param id
     * @return
     */
    @GetMapping("/rsmeter/{id}")
    @Timed
    public FindSmartMeterResponse findSmartMeter(@PathVariable String id) {
        FindSmartMeterResponse response = new FindSmartMeterResponse();
        List<SmartMeter> list ;
      /*  List<Meter> list1 = null;*/
        if(!"NONE".equalsIgnoreCase(id)) {
            list = smartMeteRepository.findAllForService();
        }else{
            list = smartMeteRepository.findByServerIdForService(id);
        }
        response.getSmartMeter().clear();
   //     response.getSmartMeter().addAll(list);
        return response;
    }
/*

    */
/**
     * 按公司ID查询公司数据。如果为NONE，则查询所有公司。
     * @param id
     * @return
     *//*

    @GetMapping("/rscom/{id}")
    @Timed
    public FindCompanyResponse findCompany(@PathVariable String id) {
        FindCompanyResponse response = new FindCompanyResponse();
//        response.getCompany().clear();
        if(!"NONE".equalsIgnoreCase(id)) {
            Company com = companyRepository.findOne(UUID.fromString(id));
            com.distribution.modules.ws.Company c = new com.distribution.modules.ws.Company();
            c.setId(com.getId().toString());
            c.setName(com.getName());
            c.setAddress(com.getAddress());
            c.setTelephone(com.getTelephone());
            c.setEmail(com.getEmail());
            response.getCompany().add(c);
        }else{
            List<com.distribution.modules.ws.Company> list = companyRepository.findAllForService();
            response.getCompany().addAll(list);
        }
        return response;
    }
*/

    /**
     * 查询设备状态。
     * @param ymd
     * @return
     */
    @GetMapping("/rsstatus/{ymd}")
    @Timed
	public FindRsStatusResponse findRsStatus(@PathVariable String ymd) {
		FindRsStatusResponse response = new FindRsStatusResponse();
		response.getMeterStatus().clear();
		Iterable<Server> sss = manager.getMServer();
		List<MeterStatus> lms = new ArrayList<MeterStatus>();
		for (Server ss : sss) {
			for (TcpModbusRequest tcp : ss.getTcpRequests()) {
				MeterStatus ms = new MeterStatus();
				try{
					ms.setIp(ss.getIp());
					ms.setCompanyId(ss.getCompanyId().toString());
					ms.setServerCode(ss.getCode());
					ms.setServerId(ss.getId().toString());
					ms.setHostname(ss.getHostname());
					ms.setName(tcp.getMeter().getName());
					ms.setCode(tcp.getRequest().getSlaveId());
					ms.setLastRetryLevel(tcp.getMeter().getStatus().getLastRetryLevel());
					if(tcp.getMeter().getStatus().getLastRetryDate() != null) {
                        ms.setLastRetryTime(tcp.getMeter().getStatus().getLastUpdate().toInstant().toEpochMilli());
                    }
					ms.setSuccess(tcp.getMeter().getStatus().getSuccess());
					ms.setTotal(tcp.getMeter().getStatus().getTotal());
					ms.setYmd(Integer.parseInt(ymd));
					if(tcp.getMeter().getStatus().getCreateDate() != null) {
                        ms.setCreateDate(tcp.getMeter().getStatus().getCreateDate().toInstant().toEpochMilli());
                    }
					ms.setLastState(tcp.getMeter().getStatus().getLastState());
					if(tcp.getMeter().getStatus().getLastUpdate() != null) {
                        ms.setLastUpdate(tcp.getMeter().getStatus().getLastUpdate().toInstant().toEpochMilli());
                    }
                    ms.setStatus(tcp.getMeter().getStatus().getSwitchStatus());
					ms.setVolume(tcp.getMeter().getStatus().getVolume());
					lms.add(ms);
				}catch(Exception e){
					logger.error("状态数据获取异常！", e);
				}
			}
		}
		response.getMeterStatus().addAll(lms);
		return response;
	}

    /**
     * 查询串口服务器的状态。
     * @param ymd
     * @return
     */
    @GetMapping("/serverstatus/{ymd}")
    @Timed
	public FindServerStatusResponse findServerStatus(@PathVariable String ymd) {
		FindServerStatusResponse response = new FindServerStatusResponse();
		response.getServerStatus().clear();
		Iterable<Server> sss = manager.getMServer();
		List<ServerStatus> lms = new ArrayList<ServerStatus>();
		for (Server ss : sss) {
        ServerStatus ms = new ServerStatus();
            try{
                ms.setServerId(ss.getHostname());
                ms.setCompanyId(ss.getCompanyId().toString());
                ms.setHostname(ss.getHostname());
                ms.setServerCode(ss.getCode());
                ms.setSuccess(ss.getStatus().getSuccess());
                ms.setTotal(ss.getStatus().getTotal());
                ms.setYmd(Integer.parseInt(ymd));
                if(ss.getStatus().getCreateDate() != null) {
                    ms.setCreateDate(ss.getStatus().getCreateDate().toInstant().toEpochMilli());
                }
                ms.setLastState(ss.getStatus().getLastState());
                if(ss.getStatus().getLastUpdate() != null) {
                    ms.setLastUpdate(ss.getStatus().getLastUpdate().toInstant().toEpochMilli());
                }
                lms.add(ms);
            }catch(Exception e){
                logger.error("状态数据获取异常！", e);
            }
        }
		response.getServerStatus().addAll(lms);
		return response;
	}
}

