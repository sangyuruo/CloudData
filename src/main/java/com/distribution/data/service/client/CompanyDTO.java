package com.distribution.data.service.client;

import com.distribution.data.client.AuthorizedFeignClient;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * 公司表
 * @author daiziying
 */
@ApiModel(description = "公司表 @author daiziying")
public class CompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 公司全名
     */
    private String companyLongName;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 父公司名
     */
    private String parentCompanyName;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 地址代码
     */
    private String addressCode;

    /**
     * 地址名
     */
    private String addressName;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 父公司代码
     */
    private String parentCompanyCode;

    /**
     * 级别id
     */
    private Integer levelId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件数
     */
    private Integer attachsNum;

    /**
     * 排序
     */
    private Integer seqNo;

    /**
     * 是否可用
     */
    private Boolean enable;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyLongName() {
        return companyLongName;
    }

    public CompanyDTO companyLongName(String companyLongName) {
        this.companyLongName = companyLongName;
        return this;
    }

    public void setCompanyLongName(String companyLongName) {
        this.companyLongName = companyLongName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public CompanyDTO companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getParentCompanyName() {
        return parentCompanyName;
    }

    public CompanyDTO parentCompanyName(String parentCompanyName) {
        this.parentCompanyName = parentCompanyName;
        return this;
    }

    public void setParentCompanyName(String parentCompanyName) {
        this.parentCompanyName = parentCompanyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public CompanyDTO companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public CompanyDTO countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public CompanyDTO cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public CompanyDTO addressCode(String addressCode) {
        this.addressCode = addressCode;
        return this;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressName() {
        return addressName;
    }

    public CompanyDTO addressName(String addressName) {
        this.addressName = addressName;
        return this;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTelephone() {
        return telephone;
    }

    public CompanyDTO telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public CompanyDTO legalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
        return this;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getParentCompanyCode() {
        return parentCompanyCode;
    }

    public CompanyDTO parentCompanyCode(String parentCompanyCode) {
        this.parentCompanyCode = parentCompanyCode;
        return this;
    }

    public void setParentCompanyCode(String parentCompanyCode) {
        this.parentCompanyCode = parentCompanyCode;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public CompanyDTO levelId(Integer levelId) {
        this.levelId = levelId;
        return this;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getRemark() {
        return remark;
    }

    public CompanyDTO remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAttachsNum() {
        return attachsNum;
    }

    public CompanyDTO attachsNum(Integer attachsNum) {
        this.attachsNum = attachsNum;
        return this;
    }

    public void setAttachsNum(Integer attachsNum) {
        this.attachsNum = attachsNum;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public CompanyDTO seqNo(Integer seqNo) {
        this.seqNo = seqNo;
        return this;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Boolean getEnable() {
        return enable;
    }

    public CompanyDTO enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CompanyDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public CompanyDTO createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public CompanyDTO updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public CompanyDTO updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
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
        CompanyDTO companyDTO = (CompanyDTO) o;
        if (companyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", companyLongName='" + getCompanyLongName() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", parentCompanyName='" + getParentCompanyName() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", cityCode='" + getCityCode() + "'" +
            ", addressCode='" + getAddressCode() + "'" +
            ", addressName='" + getAddressName() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", legalPerson='" + getLegalPerson() + "'" +
            ", parentCompanyCode='" + getParentCompanyCode() + "'" +
            ", levelId='" + getLevelId() + "'" +
            ", remark='" + getRemark() + "'" +
            ", attachsNum='" + getAttachsNum() + "'" +
            ", seqNo='" + getSeqNo() + "'" +
            ", enable='" + getEnable() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }

}
