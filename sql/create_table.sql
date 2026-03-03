-- ==========================================
-- 数据库初始化脚本
-- 项目名称：基于SpringBoot的校园兼职管理系统
-- 作者：黎旺
-- 日期：2026-03-02
-- ==========================================

-- 1. 创建数据库 (如果不存在)
CREATE
DATABASE IF NOT EXISTS `campus_parttime` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
`campus_parttime`;

-- 关闭外键检查 (防止建表顺序问题)
SET
FOREIGN_KEY_CHECKS=0;

-- ==========================================
-- 2. 用户与权限模块
-- ==========================================

-- 2.1 用户基础表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`     varchar(50)  NOT NULL COMMENT '登录账号',
    `password`     varchar(100) NOT NULL COMMENT '加密密码 (BCrypt)',
    `nickname`     varchar(50)           DEFAULT NULL COMMENT '昵称/真实姓名',
    `role_id`      bigint(20) NOT NULL DEFAULT '3' COMMENT '角色ID: 1-超管, 2-管理员, 3-普通用户, 4-工作室',
    `status`       tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-封禁, 1-正常',
    `credit_score` int(11) NOT NULL DEFAULT '100' COMMENT '信誉分 (初始100，低于60限制接单)',
    `avatar`       varchar(255)          DEFAULT NULL COMMENT '头像URL',
    `phone`        varchar(20)           DEFAULT NULL COMMENT '手机号',
    `school_id`    bigint(20) DEFAULT NULL COMMENT '所属学校/学院ID',
    `deleted`      tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0-未删, 1-已删',
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户基础表';

-- 2.2 角色表 (RBAC)
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `role_name`   varchar(50) NOT NULL COMMENT '角色名',
    `role_code`   varchar(50) NOT NULL COMMENT '角色编码',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- 2.3 用户角色关联表
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';

-- 2.4 学生实名信息表
DROP TABLE IF EXISTS `t_student_profile`;
CREATE TABLE `t_student_profile`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20) NOT NULL COMMENT '关联用户ID',
    `student_no`    varchar(50) NOT NULL COMMENT '学号',
    `college`       varchar(100) DEFAULT NULL COMMENT '学院',
    `major`         varchar(100) DEFAULT NULL COMMENT '专业',
    `grade`         varchar(20)  DEFAULT NULL COMMENT '年级',
    `id_card_front` varchar(255) DEFAULT NULL COMMENT '身份证正面照URL',
    `id_card_back`  varchar(255) DEFAULT NULL COMMENT '身份证反面照URL',
    `auth_status`   tinyint(4) NOT NULL DEFAULT '0' COMMENT '认证状态: 0-待审核, 1-通过, 2-拒绝',
    `reject_reason` varchar(255) DEFAULT NULL COMMENT '拒绝原因',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生实名信息表';

-- ==========================================
-- 3. 工作室模块
-- ==========================================

-- 3.1 工作室信息表
DROP TABLE IF EXISTS `t_studio`;
CREATE TABLE `t_studio`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `name`         varchar(100) NOT NULL COMMENT '工作室名称',
    `owner_id`     bigint(20) NOT NULL COMMENT '负责人用户ID',
    `description`  text COMMENT '工作室简介',
    `license_img`  varchar(255)          DEFAULT NULL COMMENT '营业执照/组织证明图片',
    `status`       tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0-审核中, 1-运营中, 2-已关闭',
    `credit_score` int(11) DEFAULT '100' COMMENT '工作室整体信誉分',
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工作室信息表';

-- 3.2 工作室成员表
DROP TABLE IF EXISTS `t_studio_member`;
CREATE TABLE `t_studio_member`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `studio_id`   bigint(20) NOT NULL COMMENT '工作室ID',
    `user_id`     bigint(20) NOT NULL COMMENT '用户ID',
    `role`        tinyint(4) NOT NULL DEFAULT '2' COMMENT '成员角色: 1-负责人, 2-普通成员',
    `join_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '加入状态: 0-申请中, 1-已通过, 2-已拒绝/退出',
    `join_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_studio_user` (`studio_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工作室成员表';

-- ==========================================
-- 4. 兼职任务模块
-- ==========================================

-- 4.1 兼职信息表
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `publisher_id`   bigint(20) NOT NULL COMMENT '发布者ID',
    `publisher_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '发布类型: 1-个人, 2-工作室',
    `title`          varchar(100)   NOT NULL COMMENT '兼职标题',
    `category`       varchar(50)             DEFAULT NULL COMMENT '分类',
    `description`    text           NOT NULL COMMENT '详细描述',
    `requirements`   text COMMENT '岗位要求',
    `salary_min`     decimal(10, 2) NOT NULL COMMENT '最低薪资',
    `salary_max`     decimal(10, 2)          DEFAULT NULL COMMENT '最高薪资',
    `salary_unit`    varchar(20)             DEFAULT '元/天' COMMENT '薪资单位',
    `location`       varchar(255)            DEFAULT NULL COMMENT '工作地点',
    `start_time`     datetime                DEFAULT NULL COMMENT '开始时间',
    `end_time`       datetime                DEFAULT NULL COMMENT '结束时间',
    `quota`          int(11) NOT NULL DEFAULT '1' COMMENT '招聘人数',
    `hired_count`    int(11) DEFAULT '0' COMMENT '已录用人数',
    `deposit_amount` decimal(10, 2)          DEFAULT '0.00' COMMENT '发布需扣除的虚拟押金',
    `status`         tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0-待审核, 1-上架中, 2-已满员, 3-已下架, 4-审核拒绝',
    `reject_reason`  varchar(255)            DEFAULT NULL,
    `view_count`     int(11) DEFAULT '0' COMMENT '浏览量',
    `deleted`        tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `create_time`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY              `idx_category` (`category`) USING BTREE,
    KEY              `idx_status` (`status`) USING BTREE,
    KEY              `idx_publisher` (`publisher_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='兼职任务表';

-- ==========================================
-- 5. 订单与交易模块 (核心)
-- ==========================================

-- 5.1 用户虚拟钱包表
DROP TABLE IF EXISTS `t_wallet`;
CREATE TABLE `t_wallet`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`        bigint(20) NOT NULL COMMENT '用户ID',
    `balance`        decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
    `frozen_balance` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '冻结余额 (押金/交易中)',
    `total_recharge` decimal(10, 2)          DEFAULT '0.00' COMMENT '累计充值',
    `total_income`   decimal(10, 2)          DEFAULT '0.00' COMMENT '累计收入',
    `update_time`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户虚拟钱包表';

-- 5.2 虚拟交易流水表
DROP TABLE IF EXISTS `t_transaction_log`;
CREATE TABLE `t_transaction_log`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20) NOT NULL,
    `order_id`      bigint(20) DEFAULT NULL COMMENT '关联订单ID',
    `type`          tinyint(4) NOT NULL COMMENT '类型: 1-充值, 2-支付押金, 3-退还押金, 4-结算收入, 5-退款',
    `amount`        decimal(10, 2) NOT NULL COMMENT '变动金额',
    `balance_after` decimal(10, 2) NOT NULL COMMENT '变动后余额',
    `description`   varchar(255)            DEFAULT NULL,
    `create_time`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY             `idx_user_id` (`user_id`) USING BTREE,
    KEY             `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='虚拟交易流水表';

-- 5.3 兼职订单表
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `order_no`       varchar(64)    NOT NULL COMMENT '订单号',
    `job_id`         bigint(20) NOT NULL COMMENT '兼职任务ID',
    `publisher_id`   bigint(20) NOT NULL COMMENT '雇主ID',
    `acceptor_id`    bigint(20) NOT NULL COMMENT '接单人ID',
    `studio_id`      bigint(20) DEFAULT NULL COMMENT '接单工作室ID',
    `status`         tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0-待确认, 1-进行中, 2-待验收, 3-已完成, 4-已取消, 5-纠纷中',
    `agree_time`     datetime                DEFAULT NULL COMMENT '接单时间',
    `finish_time`    datetime                DEFAULT NULL COMMENT '完成提交时间',
    `confirm_time`   datetime                DEFAULT NULL COMMENT '雇主确认时间',
    `salary_amount`  decimal(10, 2) NOT NULL COMMENT '约定薪资',
    `deposit_status` tinyint(4) DEFAULT '0' COMMENT '押金状态: 0-未扣, 1-已冻结, 2-已退还, 3-已没收',
    `rating`         tinyint(4) DEFAULT NULL COMMENT '评分 (1-5)',
    `comment`        varchar(500)            DEFAULT NULL COMMENT '评价内容',
    `cancel_reason`  varchar(255)            DEFAULT NULL,
    `deleted`        tinyint(4) NOT NULL DEFAULT '0',
    `create_time`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
    KEY              `idx_job_id` (`job_id`) USING BTREE,
    KEY              `idx_acceptor` (`acceptor_id`) USING BTREE,
    KEY              `idx_publisher` (`publisher_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='兼职订单表';

-- ==========================================
-- 6. 系统与风控模块
-- ==========================================

-- 6.1 举报投诉表
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `reporter_id`   bigint(20) NOT NULL COMMENT '举报人ID',
    `target_id`     bigint(20) NOT NULL COMMENT '被举报对象ID',
    `target_type`   tinyint(4) NOT NULL COMMENT '类型: 1-用户, 2-订单, 3-兼职',
    `reason`        varchar(500) NOT NULL COMMENT '举报原因',
    `evidence_imgs` varchar(1000)         DEFAULT NULL COMMENT '证据图片JSON数组',
    `status`        tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理状态: 0-待处理, 1-处理中, 2-已解决, 3-无效举报',
    `handler_id`    bigint(20) DEFAULT NULL COMMENT '处理管理员ID',
    `result_desc`   varchar(500)          DEFAULT NULL COMMENT '处理结果',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY             `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='举报投诉表';

-- 6.2 系统字典表
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `dict_type`  varchar(50)  NOT NULL COMMENT '字典类型',
    `dict_label` varchar(100) NOT NULL COMMENT '显示标签',
    `dict_value` varchar(50)  NOT NULL COMMENT '存储值',
    `sort`       int(4) DEFAULT '0' COMMENT '排序',
    `remark`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY          `idx_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统字典表';

-- 恢复外键检查
SET
FOREIGN_KEY_CHECKS=1;

-- ==========================================
-- 7. 初始化基础数据 (可选)
-- ==========================================

-- 插入默认超级管理员 (密码需由后端BCrypt加密，此处仅为示例占位，实际请通过注册接口或后端代码生成)
-- INSERT INTO `t_user` (`username`, `password`, `nickname`, `role_id`) VALUES ('admin', '$2a$10$...', '系统管理员', 1);

-- 插入基础字典数据
INSERT INTO `t_sys_dict` (`dict_type`, `dict_label`, `dict_value`, `sort`)
VALUES ('job_category', '家教辅导', 'tutor', 1),
       ('job_category', '促销导购', 'promotion', 2),
       ('job_category', '软件开发', 'dev', 3),
       ('job_category', '设计美工', 'design', 4),
       ('job_category', '文案写作', 'writing', 5),
       ('order_status', '待确认', '0', 1),
       ('order_status', '进行中', '1', 2),
       ('order_status', '待验收', '2', 3),
       ('order_status', '已完成', '3', 4),
       ('order_status', '已取消', '4', 5),
       ('order_status', '纠纷中', '5', 6);

-- 脚本执行完毕
