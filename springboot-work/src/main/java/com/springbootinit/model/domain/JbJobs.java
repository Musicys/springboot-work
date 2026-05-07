package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职岗位表
 * @TableName jb_jobs
 */
@Data
@TableName(value = "jb_jobs")
public class JbJobs implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者 ID
     */
    private Long merchantId;

    /**
     * 关联商家/店铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 岗位标题
     */
    private String title;

    /**
     * 岗位描述
     */
    private String description;

    /**
     * 最低薪资
     */
    private BigDecimal salaryMin;

    /**
     * 最高薪资
     */
    private BigDecimal salaryMax;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 地区代码
     */
    private String regionCode;

    /**
     * 押金金额 (商家担保)
     */
    private BigDecimal depositAmount;

    /**
     * 状态: 1:发布中，2:进行中，3:已完成，4:已关闭，5:强制下架
     */
    private Integer status;

    /**
     * 交易模式: 1:平台担保，2:允许线下
     */
    @TableField("trade_mode")
    private Integer tradeMode;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 结算周期: 1-日结, 2-周结, 3-月结, 4-完工结
     */
    @TableField("settlement_cycle")
    private Integer settlementCycle;

    /**
     * 封面图数组(列表页展示用)
     */
    @TableField("cover_images")
    private String coverImages; // 数据库JSON类型，Java中用String接收

    /**
     * 类型: 1-线上, 2-线下, 3-校园兼职, 4-校外兼职
     */
    @TableField("job_type")
    private Integer jobType;

    /**
     * 地区名称
     */
    @TableField("region_name")
    private String regionName;

    /**
     * 招聘人数
     */
    @TableField("recruit_num")
    private Integer recruitNum;

    /**
     * 简介(列表页展示用)
     */
    @TableField("brief_intro")
    private String briefIntro;

    /**
     * 工作时间类型: 1-周末, 2-周一至周五, 3-自定义时间范围
     */
    @TableField("work_time_type")
    private Integer workTimeType;

    /**
     * 工作时间描述
     */
    @TableField("work_time_desc")
    private String workTimeDesc;

    /**
     * 岗位描述封面图
     */
    @TableField("job_desc_cover")
    private String jobDescCover;

    /**
     * 岗位详细描述
     */
    @TableField("job_detail")
    private String jobDetail;
}
