package com.distribution.data.client;

import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@AuthorizedFeignClient(name = "EmCloudCpi")
public interface ComPointServiceClient {
    @GetMapping(value = "/api/meter-info" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CompointDTO> getAllMeterInfos(@ApiParam Pageable pageable) ;
}
