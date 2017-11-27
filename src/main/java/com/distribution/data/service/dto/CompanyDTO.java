package com.distribution.data.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(max = 128)
    private String name;

    @Size(max = 128)
    private String address;

    @Size(max = 32)
    private String telephone;

    @Size(max = 64)
    private String email;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;

        if ( ! Objects.equals(id, companyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", telephone='" + telephone + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
