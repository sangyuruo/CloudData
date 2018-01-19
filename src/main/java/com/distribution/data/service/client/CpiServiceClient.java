package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping(value = "/api/compoint/{compointCode}/{companyCode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO getCompoint(@PathVariable  String compointCode ,@PathVariable String  companyCode);


    @GetMapping(value = "/api/compoint/{compointCode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO findOneById(@PathVariable String  compointCode);

    @GetMapping(value = "/api/compoints/{companyCode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CompointDTO> getAllByCompanyCode(@PathVariable String companyCode);
}

