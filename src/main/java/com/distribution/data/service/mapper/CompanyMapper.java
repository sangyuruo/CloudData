package com.distribution.data.service.mapper;

import com.distribution.data.domain.Company;
import com.distribution.data.service.dto.CompanyDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity CompanyDTO and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper {

    CompanyDTO companyToCompanyDTO(Company company);

    List<CompanyDTO> companiesToCompanyDTOs(List<Company> companies);

    Company companyDTOToCompany(CompanyDTO companyDTO);

    List<Company> companyDTOsToCompanies(List<CompanyDTO> companyDTOs);
}
