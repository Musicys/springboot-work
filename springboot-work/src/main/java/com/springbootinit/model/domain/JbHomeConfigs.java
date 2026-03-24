package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 首页装修配置表
 * @TableName jb_home_configs
 */
@TableName(value ="jb_home_configs")
public class JbHomeConfigs implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 如：banner_switch, ranking_module
     */
    private String configKey;

    /**
     * 具体配置内容
     */
    private Object configValue;

    /**
     * 全局开关
     */
    private Integer isActive;

    /**
     * 针对特定地区配置
     */
    private String targetRegion;

    /**
     * 最后修改的管理员 ID
     */
    private Long updatedBy;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 如：banner_switch, ranking_module
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * 如：banner_switch, ranking_module
     */
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    /**
     * 具体配置内容
     */
    public Object getConfigValue() {
        return configValue;
    }

    /**
     * 具体配置内容
     */
    public void setConfigValue(Object configValue) {
        this.configValue = configValue;
    }

    /**
     * 全局开关
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * 全局开关
     */
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * 针对特定地区配置
     */
    public String getTargetRegion() {
        return targetRegion;
    }

    /**
     * 针对特定地区配置
     */
    public void setTargetRegion(String targetRegion) {
        this.targetRegion = targetRegion;
    }

    /**
     * 最后修改的管理员 ID
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 最后修改的管理员 ID
     */
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        JbHomeConfigs other = (JbHomeConfigs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getConfigKey() == null ? other.getConfigKey() == null : this.getConfigKey().equals(other.getConfigKey()))
            && (this.getConfigValue() == null ? other.getConfigValue() == null : this.getConfigValue().equals(other.getConfigValue()))
            && (this.getIsActive() == null ? other.getIsActive() == null : this.getIsActive().equals(other.getIsActive()))
            && (this.getTargetRegion() == null ? other.getTargetRegion() == null : this.getTargetRegion().equals(other.getTargetRegion()))
            && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConfigKey() == null) ? 0 : getConfigKey().hashCode());
        result = prime * result + ((getConfigValue() == null) ? 0 : getConfigValue().hashCode());
        result = prime * result + ((getIsActive() == null) ? 0 : getIsActive().hashCode());
        result = prime * result + ((getTargetRegion() == null) ? 0 : getTargetRegion().hashCode());
        result = prime * result + ((getUpdatedBy() == null) ? 0 : getUpdatedBy().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configKey=").append(configKey);
        sb.append(", configValue=").append(configValue);
        sb.append(", isActive=").append(isActive);
        sb.append(", targetRegion=").append(targetRegion);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}