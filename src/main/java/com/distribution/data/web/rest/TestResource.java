package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.service.ComPointService;
import com.distribution.data.service.client.CompanyDTO;
import com.distribution.data.service.client.CompointDTO;
import com.distribution.data.service.client.MeterServiceClient;
import com.distribution.data.service.client.OUServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        System.out.println("阿紫================阿紫===================阿紫");
        return serverList;
    }

}
