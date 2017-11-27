package com.distribution.data.service.mapper;

import com.distribution.data.domain.ModbusServer;
import com.distribution.data.service.dto.ModbusServerDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ModbusSlave and its DTO ModbusServerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModbusServerMapper {

    ModbusServerDTO modbusServerToModbusServerDTO(ModbusServer modbusServer);

    List<ModbusServerDTO> modbusServersToModbusServerDTOs(List<ModbusServer> modbusServers);

    ModbusServer modbusServerDTOToModbusServer(ModbusServerDTO modbusServerDTO);

    List<ModbusServer> modbusServerDTOsToModbusServers(List<ModbusServerDTO> modbusServerDTOs);
}
