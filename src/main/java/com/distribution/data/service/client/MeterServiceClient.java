package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@AuthorizedFeignClient(name = "EmCloudMi")
public interface MeterServiceClient {

    @GetMapping(value = "/api/meter-info/{metercode}/{compointcode}/{registercode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    MeterInfoDTO getOneMeterInfo(@PathVariable  String meterCode,@PathVariable  String comPointCode, @PathVariable Integer registerCode);

    @GetMapping(value = "/api/meter-info/{comointcode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<MeterInfoDTO> getAllMeterInfosByComPointCode(@PathVariable String comPointCode) ;

    @GetMapping(value = "/api/meter-infos/{comointcode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    MeterInfoDTO getMeterInfoByMeterCode(@PathVariable String meterCode);

    @GetMapping(value = "/api/meter-infos" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<MeterInfoDTO> getAllMeterInfos();
}
