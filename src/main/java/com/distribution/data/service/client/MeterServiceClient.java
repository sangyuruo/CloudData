package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@AuthorizedFeignClient(name = "EmCloudMi")
public interface MeterServiceClient {
    @GetMapping(value = "/api/meter-info/by-com-point-code" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    //List<MeterInfoDTO>findMetersByServerId();
    MeterInfoDTO findOneMeter(String meterCode, String comPointCode, Integer registerCode);
    List<MeterInfoDTO> getAllMeterInfosByComPointCode(String comPointCode) ;
}
