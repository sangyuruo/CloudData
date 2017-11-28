package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.client.CompanyDTO;
import com.distribution.data.client.MeterServiceClient;
import com.distribution.data.client.OUServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestResource {
    @Inject
    MeterServiceClient client;

    @Inject
    OUServiceClient ouServiceClient;
    private static Logger logger = LoggerFactory.getLogger(TestResource.class);
    @GetMapping("/test")
    @Timed
    public void testComBtwService() {

        logger.info("start get data from mi");
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 1;
            }

            @Override
            public int getPageSize() {
                return 100;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<CompanyDTO> companies = ouServiceClient.getAllCompanies(pageable);
//                        List<MeterInfo> meterInfos = client.getAllMeterInfos(pageable);
                        for (CompanyDTO company : companies ) {
                            logger.info( company.getCompanyName() );
                        }
                        logger.info("loop meterinfo: " + companies.size());
                    } catch (Throwable ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
