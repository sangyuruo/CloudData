package com.distribution.data.client;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * 采集点信息表
 * @author Capejor
 */
public class CompointDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 设备编码 外键
     */
    private String comPointCode;

    /**
     * 登记代码
     */
    private Integer registerCode;

    /**
     * 登记名称
     */
    private String registerName;

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
     * ip地址
     */
    private String ip;

    /**
     * 服务器名称
     */
    private String hostName;

    /**
     * 服务器端口
     */
    private Integer hostPort;

    /**
     * 请求超时时间
     */
    private Integer requestTimeout;

    /**
     * 响应超时时间
     */
    private Integer replyTimeout;

    /**
     * 是否有效
     */
    private Boolean enable;

    /**
     * 是否心跳
     */
    private Boolean keepAlive;

    /**
     * 链接模式
     */
    private Integer connectMode;

    /**
     * 创建人员
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Instant createTime;

    /**
     * 更新人员
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Instant updateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComPointCode() {
        return comPointCode;
    }

    public CompointDTO comPointCode(String comPointCode) {
        this.comPointCode = comPointCode;
        return this;
    }

    public void setComPointCode(String comPointCode) {
        this.comPointCode = comPointCode;
    }

    public Integer getRegisterCode() {
        return registerCode;
    }

    public CompointDTO registerCode(Integer registerCode) {
        this.registerCode = registerCode;
        return this;
    }

    public void setRegisterCode(Integer registerCode) {
        this.registerCode = registerCode;
    }

    public String getRegisterName() {
        return registerName;
    }

    public CompointDTO registerName(String registerName) {
        this.registerName = registerName;
        return this;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public CompointDTO addressCode(String addressCode) {
        this.addressCode = addressCode;
        return this;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public CompointDTO organizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
        return this;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public CompointDTO companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getIp() {
        return ip;
    }

    public CompointDTO ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostName() {
        return hostName;
    }

    public CompointDTO hostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public CompointDTO hostPort(Integer hostPort) {
        this.hostPort = hostPort;
        return this;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public CompointDTO requestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getReplyTimeout() {
        return replyTimeout;
    }

    public CompointDTO replyTimeout(Integer replyTimeout) {
        this.replyTimeout = replyTimeout;
        return this;
    }

    public void setReplyTimeout(Integer replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

    public Boolean isEnable() {
        return enable;
    }

    public CompointDTO enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean isKeepAlive() {
        return keepAlive;
    }

    public CompointDTO keepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getConnectMode() {
        return connectMode;
    }

    public CompointDTO connectMode(Integer connectMode) {
        this.connectMode = connectMode;
        return this;
    }

    public void setConnectMode(Integer connectMode) {
        this.connectMode = connectMode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CompointDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public CompointDTO createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public CompointDTO updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public CompointDTO updateTime(Instant updateTime) {
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
        CompointDTO compoint = (CompointDTO) o;
        if (compoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompointDTO{" +
            "id=" + getId() +
            ", comPointCode='" + getComPointCode() + "'" +
            ", registerCode='" + getRegisterCode() + "'" +
            ", registerName='" + getRegisterName() + "'" +
            ", addressCode='" + getAddressCode() + "'" +
            ", organizationCode='" + getOrganizationCode() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", ip='" + getIp() + "'" +
            ", hostName='" + getHostName() + "'" +
            ", hostPort='" + getHostPort() + "'" +
            ", requestTimeout='" + getRequestTimeout() + "'" +
            ", replyTimeout='" + getReplyTimeout() + "'" +
            ", enable='" + isEnable() + "'" +
            ", keepAlive='" + isKeepAlive() + "'" +
            ", connectMode='" + getConnectMode() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
