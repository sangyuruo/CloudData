package com.distribution.data.service;
import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.service.client.MeterInfoDTO;
import com.distribution.data.service.client.MeterServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

public class MeterInfoService {
    private MeterServiceClient meterServiceClient;
    private  Meter meter;
    public MeterInfoService(MeterServiceClient meterServiceClient) {
        this.meterServiceClient = meterServiceClient;
    }

    public List<Meter> findMetersByServerId(String comPointCode){
        List<MeterInfoDTO> meterInfoDTOList =meterServiceClient.getAllMeterInfosByComPointCode(comPointCode);
        List<Meter> meterList =new ArrayList<>();
        for (MeterInfoDTO m1: meterInfoDTOList){
            meter.setId(m1.getMeterCode());
            meter.setServerId(m1.getComPointCode());
            meter.setCompanyId(m1.getCompanyCode());
           /* meter.setServer();
            meter.setStatus();*/
            meter.setCode(m1.getRegisterCode());
            meter.setLongcode(m1.getLongcode());
            meter.setFunc(3);
            meter.setAllowDuplicate(m1.getAllowDuplicate());
            meter.setName(m1.getMeterName());
            meter.category(m1.getMeterTypeCode());
            meter.setEnable(m1.getEnable());
            meter.setBigEndian(m1.getBigEndian());
            meter.setStartOffset(m1.getStartOffset());
            meter.setNumberOfRegisters(m1.getNumberOfRegisters());
            meter.setControlAddress(m1.getControlAddress());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = m1.getControlCommands();
            Map map= null;
            try {
                map = mapper.readValue(jsonString,Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            meter.setDataTypes(map);
            meterList.add(meter);
        }
        return meterList;
    }

    public Meter findOneMeter(String meterCode, UUID comPointCode, Integer registerCode){
        MeterInfoDTO m =meterServiceClient.findOneMeter(meterCode,comPointCode,registerCode);
        Meter meter=new Meter();
        meter.setId(m.getMeterCode());
        meter.setServerId(m.getComPointCode());
        meter.setCompanyId(m.getCompanyCode());
           /* meter.setServer();
            meter.setStatus();*/
        meter.setCode(m.getRegisterCode());
        meter.setLongcode(m.getLongcode());
        meter.setFunc(3);
        meter.setAllowDuplicate(m.getAllowDuplicate());
        meter.setName(m.getMeterName());
        meter.category(m.getMeterTypeCode());
        meter.setEnable(m.getEnable());
        meter.setBigEndian(m.getBigEndian());
        meter.setStartOffset(m.getStartOffset());
        meter.setNumberOfRegisters(m.getNumberOfRegisters());
        meter.setControlAddress(m.getControlAddress());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = m.getControlCommands();
        Map map= null;
        try {
            map = mapper.readValue(jsonString,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        meter.setDataTypes(map);
        return  meter;
    }

}
