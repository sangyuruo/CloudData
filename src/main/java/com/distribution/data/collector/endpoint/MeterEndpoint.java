package com.distribution.data.collector.endpoint;

import com.distribution.data.collector.cassadra.dao.MeterDataService;
import com.distribution.data.collector.data.ModbusServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Endpoint
//@MessageEndpoint
public class MeterEndpoint {
// implements IMeterEndpoint {

	private static Logger logger = LoggerFactory.getLogger(MeterEndpoint.class);

	@Autowired
	private ModbusServerManager manager;

	@Autowired
	private MeterDataService meterDataService;

//    @ServiceActivator(inputChannel="toSA")
//	public List<Object> findData(List<Object> request) {
//    	List<Object> result = new ArrayList<Object>();
//    	if(request != null &&request.size() > 0){
//    		for (Object object : request) {
//    			if(object instanceof FindRsDataRequest){
//    				FindRsDataRequest req = (FindRsDataRequest)object;
//    				FindRsDataResponse res =  findRsData(req);
//    				result.add(res);
//    			}
//    			if(object instanceof FindRsStatusRequest){
//    				FindRsStatusRequest req = (FindRsStatusRequest)object;
//    				FindRsStatusResponse res =  findRsStatus(req);
//    				result.add(res);
//    			}
//			}
//    	}
//    	return result;
//    }
//
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_METERDATA_REQUEST)
//	@ResponsePayload
//	public FindRsDataResponse findRsData(@RequestPayload FindRsDataRequest request) {
//		FindRsDataResponse response = new FindRsDataResponse();
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("ymd", request.getYmd());
//		param.put("hour", request.getHour());
//		param.put("minute", request.getMinute());
//		param.put("sec", request.getSec());
//		List<MeterData> list = meterDataService.findMeterDataByIdForService(request.getYmd(), request.getHour(), request.getMinute(), request.getSec());
//		response.getMeterData().clear();
//		response.getMeterData().addAll(list);
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_METERSTATUS_REQUEST)
//	@ResponsePayload
//	public FindRsStatusResponse findRsStatus(@RequestPayload FindRsStatusRequest request) {
//		FindRsStatusResponse response = new FindRsStatusResponse();
//		response.getMeterStatus().clear();
//		Iterable<Server> sss = manager.getMServer();
//		List<MeterStatus> lms = new ArrayList<MeterStatus>();
//		for (Server ss : sss) {
//			for (TcpModbusRequest tcp : ss.getTcpRequests()) {
//				MeterStatus ms = new MeterStatus();
//				try{
//					ms.setIp(ss.getIp());
//					ms.setCode(tcp.getSlaveId());
//					ms.setLastRetryLevel(tcp.getMeter().getStatus().getLastRetryLevel());
//					ms.setLastRetryTime(tcp.getMeter().getStatus().getLastUpdate().toInstant().toEpochMilli());
//					ms.setSuccess(tcp.getMeter().getStatus().getSuccess());
//					ms.setTotal(tcp.getMeter().getStatus().getTotal());
//					String ymd = DateUtils.formatDate(tcp.getMeter().getStatus().getLastUpdate().toLocalDateTime(), "yyyyMMdd");
//					ms.setYmd(Integer.parseInt(ymd));
//					lms.add(ms);
//				}catch(Exception e){
//					logger.error("状态数据获取异常！", e);
//				}
//			}
//		}
//		response.getMeterStatus().addAll(lms);
//		return response;
//	}

}
