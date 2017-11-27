package com.distribution.data.service.mapper;

import com.distribution.data.domain.SmartMeter;
import com.distribution.data.service.dto.SmartMeterDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity SmartMeter and its DTO SmartMeterDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmartMeterMapper {

    SmartMeterDTO smartMeterToSmartMeterDTO(SmartMeter smartMeter);

    List<SmartMeterDTO> smartMetersToSmartMeterDTOs(List<SmartMeter> smartMeters);

    SmartMeter smartMeterDTOToSmartMeter(SmartMeterDTO smartMeterDTO);

    List<SmartMeter> smartMeterDTOsToSmartMeters(List<SmartMeterDTO> smartMeterDTOs);
}
