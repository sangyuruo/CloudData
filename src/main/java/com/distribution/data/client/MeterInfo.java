package com.distribution.data.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * 设备信息表
 * @author youhong
 */
@ApiModel(description = "设备信息表 @author youhong")
public class MeterInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 设备编码
     */
    private String meterCode;

    /**
     * 设备名称
     */
    private String meterName;

    /**
     * 登记code
     */
    private Integer registerCode;

    /**
     * 地址编码
     */
    private String addressCode;

    /**
     * 组织编码
     */
    private String organizationCode;

    /**
     * 公司编码
     */
    private String companyCode;

    /**
     * 设备类型
     */
    private String meterType;

    /**
     * 起始偏移
     */
    private Integer startOffset;

    /**
     * 寄存器数量
     */
    private Integer numberOfRegisters;

    /**
     * 控制地址
     */
    private Integer controlAddress;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Instant createTime;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private Instant updateTime;

    /**
     * 下发命令
     */
    private String controlCommands;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public MeterInfo meterCode(String meterCode) {
        this.meterCode = meterCode;
        return this;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getMeterName() {
        return meterName;
    }

    public MeterInfo meterName(String meterName) {
        this.meterName = meterName;
        return this;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public Integer getRegisterCode() {
        return registerCode;
    }

    public MeterInfo registerCode(Integer registerCode) {
        this.registerCode = registerCode;
        return this;
    }

    public void setRegisterCode(Integer registerCode) {
        this.registerCode = registerCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public MeterInfo addressCode(String addressCode) {
        this.addressCode = addressCode;
        return this;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public MeterInfo organizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
        return this;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public MeterInfo companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMeterType() {
        return meterType;
    }

    public MeterInfo meterType(String meterType) {
        this.meterType = meterType;
        return this;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public MeterInfo startOffset(Integer startOffset) {
        this.startOffset = startOffset;
        return this;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public MeterInfo numberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
        return this;
    }

    public void setNumberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }

    public Integer getControlAddress() {
        return controlAddress;
    }

    public MeterInfo controlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
        return this;
    }

    public void setControlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public MeterInfo createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public MeterInfo createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public MeterInfo updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public MeterInfo updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getControlCommands() {
        return controlCommands;
    }

    public MeterInfo controlCommands(String controlCommands) {
        this.controlCommands = controlCommands;
        return this;
    }

    public void setControlCommands(String controlCommands) {
        this.controlCommands = controlCommands;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeterInfo meterInfo = (MeterInfo) o;
        if (meterInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), meterInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MeterInfo{" +
            "id=" + getId() +
            ", meterCode='" + getMeterCode() + "'" +
            ", meterName='" + getMeterName() + "'" +
            ", registerCode='" + getRegisterCode() + "'" +
            ", addressCode='" + getAddressCode() + "'" +
            ", organizationCode='" + getOrganizationCode() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", meterType='" + getMeterType() + "'" +
            ", startOffset='" + getStartOffset() + "'" +
            ", numberOfRegisters='" + getNumberOfRegisters() + "'" +
            ", controlAddress='" + getControlAddress() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", controlCommands='" + getControlCommands() + "'" +
            "}";
    }
}
