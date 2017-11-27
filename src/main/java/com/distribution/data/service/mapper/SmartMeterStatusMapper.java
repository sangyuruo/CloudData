package com.distribution.data.service.mapper;

import com.distribution.data.domain.SmartMeterStatus;
import com.distribution.data.service.dto.SmartMeterStatusDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity SmartMeterStatus and its DTO SmartMeterStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmartMeterStatusMapper {

    SmartMeterStatusDTO smartMeterStatusToSmartMeterStatusDTO(SmartMeterStatus smartMeterStatus);

    List<SmartMeterStatusDTO> smartMeterStatusesToSmartMeterStatusDTOs(List<SmartMeterStatus> smartMeterStatuses);

    SmartMeterStatus smartMeterStatusDTOToSmartMeterStatus(SmartMeterStatusDTO smartMeterStatusDTO);

    List<SmartMeterStatus> smartMeterStatusDTOsToSmartMeterStatuses(List<SmartMeterStatusDTO> smartMeterStatusDTOs);
}
