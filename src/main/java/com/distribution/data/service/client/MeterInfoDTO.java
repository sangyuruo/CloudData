package com.distribution.data.service.client;

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
public class MeterInfoDTO implements Serializable {

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
     * 串口编码
     */
    private String comPointCode;

    /**
     * 设备类型代码
     */
    private Integer meterTypeCode;

    /**
     * 设备类型
     */
    private String meterType;
    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /*private int func=3;*/

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

    /**
     * 大Endian
     */
    private Boolean bigEndian;
    /**
     * 允许重复
     */
    private Boolean allowDuplicate;

    /**
     * 计算
     */
    private Integer calculates;

    private Long longcode;
    /**
     * 是否有效
     */
    private Boolean enable;

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

    public MeterInfoDTO meterCode(String meterCode) {
        this.meterCode = meterCode;
        return this;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getMeterName() {
        return meterName;
    }

    public MeterInfoDTO meterName(String meterName) {
        this.meterName = meterName;
        return this;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public Integer getRegisterCode() {
        return registerCode;
    }

    public MeterInfoDTO registerCode(Integer registerCode) {
        this.registerCode = registerCode;
        return this;
    }

    public void setRegisterCode(Integer registerCode) {
        this.registerCode = registerCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public MeterInfoDTO addressCode(String addressCode) {
        this.addressCode = addressCode;
        return this;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public MeterInfoDTO organizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
        return this;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public MeterInfoDTO companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMeterType() {
        return meterType;
    }

    public MeterInfoDTO meterType(String meterType) {
        this.meterType = meterType;
        return this;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getComPointCode() {
        return comPointCode;
    }
    public MeterInfoDTO comPointCode(String comPointCode) {
        this.comPointCode = comPointCode;
        return this;
    }
    public void setComPointCode(String comPointCode) {
        this.comPointCode = comPointCode;
    }

    public Integer getMeterTypeCode() {
        return meterTypeCode;
    }
    public MeterInfoDTO meterTypeCode(Integer meterTypeCode) {
        this.meterTypeCode = meterTypeCode;
        return this;
    }
    public void setMeterTypeCode(Integer meterTypeCode) {
        this.meterTypeCode = meterTypeCode;
    }

    public Double getLongitude() {
        return longitude;
    }
    public MeterInfoDTO longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    public MeterInfoDTO latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getLongcode() {
        return longcode;
    }
    public MeterInfoDTO longcode(Long longcode) {
        this.longcode = longcode;
        return this;
    }
    public void setLongcode(Long longcode) {
        this.longcode = longcode;
    }

    public Boolean getBigEndian() {
        return bigEndian;
    }
    public MeterInfoDTO bigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
        return this;
    }
    public void setBigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public Boolean getAllowDuplicate() {
        return allowDuplicate;
    }
    public MeterInfoDTO allowDuplicate(Boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
        return this;
    }
    public void setAllowDuplicate(Boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }

    public Integer getCalculates() {
        return calculates;
    }
    public MeterInfoDTO calculates(Integer calculates) {
        this.calculates = calculates;
        return this;
    }
    public void setCalculates(Integer calculates) {
        this.calculates = calculates;
    }

    public Boolean getEnable() {
        return enable;
    }
    public MeterInfoDTO enable(boolean enable) {
        this.enable = enable;
        return this;
    }
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public MeterInfoDTO startOffset(Integer startOffset) {
        this.startOffset = startOffset;
        return this;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public MeterInfoDTO numberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
        return this;
    }

    public void setNumberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }

    public Integer getControlAddress() {
        return controlAddress;
    }

    public MeterInfoDTO controlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
        return this;
    }

    public void setControlAddress(Integer controlAddress) {
        this.controlAddress = controlAddress;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public MeterInfoDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public MeterInfoDTO createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public MeterInfoDTO updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public MeterInfoDTO updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getControlCommands() {
        return controlCommands;
    }

    public MeterInfoDTO controlCommands(String controlCommands) {
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
        MeterInfoDTO meterInfo = (MeterInfoDTO) o;
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
        return "MeterInfoDTO{" +
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
