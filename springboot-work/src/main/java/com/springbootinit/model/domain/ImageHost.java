package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 图床图片信息表
 * @TableName image_host
 */
@TableName(value ="image_host")
public class ImageHost implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 服务器本地存储路径
     */
    private String storagePath;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * AI识别出的标签，逗号分隔
     */
    private String aiTags;

    /**
     * 上传时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 原始文件名
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * 原始文件名
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * 服务器本地存储路径
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * 服务器本地存储路径
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * 文件大小(字节)
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * 文件大小(字节)
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * AI识别出的标签，逗号分隔
     */
    public String getAiTags() {
        return aiTags;
    }

    /**
     * AI识别出的标签，逗号分隔
     */
    public void setAiTags(String aiTags) {
        this.aiTags = aiTags;
    }

    /**
     * 上传时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 上传时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        ImageHost other = (ImageHost) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOriginalName() == null ? other.getOriginalName() == null : this.getOriginalName().equals(other.getOriginalName()))
            && (this.getStoragePath() == null ? other.getStoragePath() == null : this.getStoragePath().equals(other.getStoragePath()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
            && (this.getAiTags() == null ? other.getAiTags() == null : this.getAiTags().equals(other.getAiTags()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOriginalName() == null) ? 0 : getOriginalName().hashCode());
        result = prime * result + ((getStoragePath() == null) ? 0 : getStoragePath().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getAiTags() == null) ? 0 : getAiTags().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", originalName=").append(originalName);
        sb.append(", storagePath=").append(storagePath);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", aiTags=").append(aiTags);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}