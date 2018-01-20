package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.service.ComPointService;
import com.distribution.data.service.MeterInfoService;
import com.distribution.data.service.client.MeterInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TestResource {
    @Inject
    private MeterInfoService meterInfoService;

    @Inject
    private ComPointService comPointService;
    private static Logger logger = LoggerFactory.getLogger(TestResource.class);

    @GetMapping("/test/mi")
    @Timed
    public List<Server> testComBtwService() {
        logger.info("start get data from mi");
        List<Server> serverList = comPointService.findAllServer();
        System.out.println(serverList.toString());
        return serverList;
    }


    @GetMapping("/test/EmCloudCpi/compoint/{compointcode}")
    @Timed
    public List<Server> testcpiService(String id) {
        logger.info("start get data from mi");
        List<Server> serverList = comPointService.findAllByCompanyId(UUID.fromString(id));
        System.out.println(serverList.toString());
        System.out.println("阿===================================紫");
        return serverList;
    }

    @GetMapping("/test/EmCloudMi/meter-infos")
    @Timed
    public List<SmartMeter> testGetAllMeterInfos() {
        logger.info("start get data from EmCloudMi");
        List<SmartMeter> list = meterInfoService.findAllForService();
        System.out.println(list.toString());
        return list;
    }
    @GetMapping("/test/EmCloudMi/meter-infos/{meterCode}")
    @Timed
    public SmartMeter testGetMeterInfoByMeterCode(String meterCode) {
        logger.info("start get data from EmCloudMi");
        SmartMeter smartMeter = meterInfoService.findOneById(meterCode);
        System.out.println(smartMeter.toString());
        System.out.println("11111111111");
        return smartMeter;
    }



}
