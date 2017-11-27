package com.distribution.data.service.mapper;

import com.distribution.data.domain.ServerStatus;
import com.distribution.data.service.dto.ServerStatusDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ServerStatus and its DTO ServerStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServerStatusMapper {

    ServerStatusDTO serverStatusToServerStatusDTO(ServerStatus serverStatus);

    List<ServerStatusDTO> serverStatusesToServerStatusDTOs(List<ServerStatus> serverStatuses);

    ServerStatus serverStatusDTOToServerStatus(ServerStatusDTO serverStatusDTO);

    List<ServerStatus> serverStatusDTOsToServerStatuses(List<ServerStatusDTO> serverStatusDTOs);
}
