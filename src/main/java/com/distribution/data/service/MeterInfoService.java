package com.distribution.data.service;
import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.service.client.MeterInfoDTO;
import com.distribution.data.service.client.MeterServiceClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

public class MeterInfoService {
    private MeterServiceClient meterServiceClient;
    public MeterInfoService(MeterServiceClient meterServiceClient) {
        this.meterServiceClient = meterServiceClient;
    }

    public List<Meter> findMetersByServerId(String comPointCode){
        List<MeterInfoDTO> meterInfoDTOList =meterServiceClient.getAllMeterInfosByComPointCode(comPointCode);
        List<Meter> meterList =new ArrayList<>();
        for (MeterInfoDTO m1: meterInfoDTOList){

               Meter meter=new Meter();
            meter.setId(UUID.fromString(m1.getMeterCode()));
            meter.setServerId(UUID.fromString(m1.getComPointCode()));
            meter.setCompanyId(UUID.fromString(m1.getCompanyCode()));
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

    public Meter findOneMeter(String meterCode, String comPointCode, Integer registerCode){
        MeterInfoDTO m =meterServiceClient.getOneMeterInfo(meterCode,comPointCode,registerCode);
        Meter meter=new Meter();
        meter.setId(UUID.fromString(m.getMeterCode()));
        meter.setServerId(UUID.fromString(m.getComPointCode()));
        meter.setCompanyId(UUID.fromString(m.getCompanyCode()));
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
    public Meter findOneById(String meterCode) {
        MeterInfoDTO m = meterServiceClient.getMeterInfoByMeterCode(meterCode);
        Meter meter=new Meter();
        meter.setId(UUID.fromString(m.getMeterCode()));
        meter.setServerId(UUID.fromString(m.getComPointCode()));
        meter.setCompanyId(UUID.fromString(m.getCompanyCode()));
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
    public List<SmartMeter> findAllForService() {
        List<MeterInfoDTO> meterInfoDTOList = meterServiceClient.getAllMeterInfos();
        List<SmartMeter> meterList =new ArrayList<>();
        SmartMeter smartMeter = new SmartMeter();
        for (MeterInfoDTO m1: meterInfoDTOList){
            smartMeter.setId(UUID.fromString(m1.getMeterCode()));
            smartMeter.setServerId(UUID.fromString(m1.getComPointCode()));
            smartMeter.setCompanyId(UUID.fromString(m1.getCompanyCode()));
            smartMeter.setCode(m1.getRegisterCode());
            smartMeter.setLongcode(m1.getLongcode());
            smartMeter.setFunc(3);
            smartMeter.setAllowDuplicate(m1.getAllowDuplicate());
            smartMeter.setName(m1.getMeterName());
            smartMeter.category(m1.getMeterTypeCode());
            smartMeter.setEnable(m1.getEnable());
            smartMeter.setBigEndian(m1.getBigEndian());
            smartMeter.setStartOffset(m1.getStartOffset());
            smartMeter.setNumberOfRegisters(m1.getNumberOfRegisters());
            smartMeter.setControlAddress(m1.getControlAddress());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = m1.getControlCommands();
            Map map= null;
            try {
                map = mapper.readValue(jsonString,Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            smartMeter.setDataTypes(map);
            meterList.add(smartMeter);
        }
        return meterList;
    }

    public List<SmartMeter> findByServerIdForService(String id) {
        List<MeterInfoDTO> meterInfoDTOList = meterServiceClient.getAllMeterInfosByComPointCode(id);
        List<SmartMeter> meterList =new ArrayList<>();
        SmartMeter smartMeter = new SmartMeter();
        for (MeterInfoDTO m1: meterInfoDTOList){
            smartMeter.setId(UUID.fromString(m1.getMeterCode()));
            smartMeter.setServerId(UUID.fromString(m1.getComPointCode()));
            smartMeter.setCompanyId(UUID.fromString(m1.getCompanyCode()));
            smartMeter.setCode(m1.getRegisterCode());
            smartMeter.setLongcode(m1.getLongcode());
            smartMeter.setFunc(3);
            smartMeter.setAllowDuplicate(m1.getAllowDuplicate());
            smartMeter.setName(m1.getMeterName());
            smartMeter.category(m1.getMeterTypeCode());
            smartMeter.setEnable(m1.getEnable());
            smartMeter.setBigEndian(m1.getBigEndian());
            smartMeter.setStartOffset(m1.getStartOffset());
            smartMeter.setNumberOfRegisters(m1.getNumberOfRegisters());
            smartMeter.setControlAddress(m1.getControlAddress());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = m1.getControlCommands();
            Map map= null;
            try {
                map = mapper.readValue(jsonString,Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            smartMeter.setDataTypes(map);
            meterList.add(smartMeter);
        }
        return meterList;
    }
}
