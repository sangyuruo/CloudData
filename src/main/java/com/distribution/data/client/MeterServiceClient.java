package com.distribution.data.client;

import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@AuthorizedFeignClient(name = "EmCloudMi")
public interface MeterServiceClient {
    @GetMapping(value = "/api/meter-info" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MeterInfo> getAllMeterInfos(@ApiParam Pageable pageable) ;
}
