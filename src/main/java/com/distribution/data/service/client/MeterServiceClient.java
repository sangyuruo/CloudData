package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@AuthorizedFeignClient(name = "EmCloudMi")
public interface MeterServiceClient {

    @GetMapping(value = "/api/meter-infos/{meterCode}/{comPointCode}/{registerCode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    MeterInfoDTO getOneMeterInfo(@PathVariable(value = "meterCode")  String meterCode,@PathVariable(value = "comPointCode")  String comPointCode, @PathVariable(value = "registerCode") Integer registerCode);

    @GetMapping(value = "/api/meter-infos/by-cpc/{comPointCode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<MeterInfoDTO> getAllMeterInfosByComPointCode(@PathVariable(value = "comPointCode") String comPointCode) ;

    @GetMapping(value = "/api/meter-infos/by-mc/{meterCode}" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    MeterInfoDTO getMeterInfoByMeterCode(@PathVariable(value = "meterCode") String meterCode);

    @GetMapping(value = "/api/meter-infos" , consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<MeterInfoDTO> getAllMeterInfos();
}
