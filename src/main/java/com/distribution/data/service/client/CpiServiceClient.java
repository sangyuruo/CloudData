package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@AuthorizedFeignClient(name = "EmCloudCpi")
public interface CpiServiceClient {
    @GetMapping(value = "/api/compoints" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CompointDTO> getAllCompoints();


    @GetMapping(value = "/api/compoints/{compointCode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO getCompoint(String compointCode , String  companyCode);


    @GetMapping(value = "/api/compoints/{compointCode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO findOneById(String  compointCode);

    @GetMapping(value = "/api/compoints/bycompanycode" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CompointDTO> getAllByCompanyCode(String companyCode);
}

