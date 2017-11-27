package com.distribution.data.service.mapper;

import com.distribution.data.domain.SmartMeterData;
import com.distribution.data.service.dto.SmartMeterDataDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity SmartMeterData and its DTO SmartMeterDataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmartMeterDataMapper {

    SmartMeterDataDTO smartMeterDataToSmartMeterDataDTO(SmartMeterData smartMeterData);

    List<SmartMeterDataDTO> smartMeterDataToSmartMeterDataDTOs(List<SmartMeterData> smartMeterData);

    SmartMeterData smartMeterDataDTOToSmartMeterData(SmartMeterDataDTO smartMeterDataDTO);

    List<SmartMeterData> smartMeterDataDTOsToSmartMeterData(List<SmartMeterDataDTO> smartMeterDataDTOs);
}
