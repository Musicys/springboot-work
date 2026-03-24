package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * CMS 模块配置表
 * @TableName cm_modules
 */
@TableName(value ="cm_modules")
public class CmModules implements Serializable {
    /**
     * 如：home_banner, salary_ranking
     */
    @TableId
    private String moduleKey;

    /**
     * 
     */
    private String moduleName;

    /**
     * 总开关
     */
    private Integer isEnabled;

    /**
     * 详细配置 (图片列表，排序规则等)
     */
    private Object configData;

    /**
     * 展示优先级
     */
    private Integer priority;

    /**
     * 
     */
    private Long updatedBy;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 如：home_banner, salary_ranking
     */
    public String getModuleKey() {
        return moduleKey;
    }

    /**
     * 如：home_banner, salary_ranking
     */
    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    /**
     * 
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * 
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * 总开关
     */
    public Integer getIsEnabled() {
        return isEnabled;
    }

    /**
     * 总开关
     */
    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 详细配置 (图片列表，排序规则等)
     */
    public Object getConfigData() {
        return configData;
    }

    /**
     * 详细配置 (图片列表，排序规则等)
     */
    public void setConfigData(Object configData) {
        this.configData = configData;
    }

    /**
     * 展示优先级
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 展示优先级
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 
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
        CmModules other = (CmModules) that;
        return (this.getModuleKey() == null ? other.getModuleKey() == null : this.getModuleKey().equals(other.getModuleKey()))
            && (this.getModuleName() == null ? other.getModuleName() == null : this.getModuleName().equals(other.getModuleName()))
            && (this.getIsEnabled() == null ? other.getIsEnabled() == null : this.getIsEnabled().equals(other.getIsEnabled()))
            && (this.getConfigData() == null ? other.getConfigData() == null : this.getConfigData().equals(other.getConfigData()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getModuleKey() == null) ? 0 : getModuleKey().hashCode());
        result = prime * result + ((getModuleName() == null) ? 0 : getModuleName().hashCode());
        result = prime * result + ((getIsEnabled() == null) ? 0 : getIsEnabled().hashCode());
        result = prime * result + ((getConfigData() == null) ? 0 : getConfigData().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
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
        sb.append(", moduleKey=").append(moduleKey);
        sb.append(", moduleName=").append(moduleName);
        sb.append(", isEnabled=").append(isEnabled);
        sb.append(", configData=").append(configData);
        sb.append(", priority=").append(priority);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}