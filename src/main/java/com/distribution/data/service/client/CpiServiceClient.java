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
    @GetMapping(value = "/api/compoint" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CompointDTO> getCompoints();


    @GetMapping(value = "/api/compoint/{compointcode}/{companycode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO getCompoint(@PathVariable(value = "compointcode")  String compointCode ,@PathVariable(value = "companycode") String  companyCode);


    @GetMapping(value = "/api/compoint/{compointcode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    CompointDTO findOneById(@PathVariable(value = "compointcode") String  compointCode);

    @GetMapping(value = "/api/compoints/{companycode}" ,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CompointDTO> getAllByCompanyCode(@PathVariable(value = "companycode") String companyCode);
}

