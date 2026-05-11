create table cm_modules
(
    module_key  varchar(64)                        not null comment '如：home_banner, salary_ranking'
        primary key,
    module_name varchar(128)                       null,
    is_enabled  tinyint  default 1                 null comment '总开关',
    config_data json                               null comment '详细配置 (图片列表，排序规则等)',
    priority    int      default 0                 null comment '展示优先级',
    updated_by  bigint                             null,
    updated_at  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    comment 'CMS 模块配置表';

create table ct_credit_logs
(
    id               bigint auto_increment
        primary key,
    user_id          bigint                             not null,
    change_amount    int                                not null comment '+5 或 -5',
    reason_type      varchar(50)                        not null comment 'COMPLETE_JOB, NO_SHOW, ESCAPE, MANUAL_ADJUST',
    related_order_id bigint                             null comment '关联订单 ID',
    operator_id      bigint                             null comment '操作人 ID (系统或管理员)',
    description      varchar(255)                       null,
    balance_after    int                                not null comment '变动后的分数',
    created_at       datetime default CURRENT_TIMESTAMP null
)
    comment '信誉分流水表';

create index idx_user_time
    on ct_credit_logs (user_id, created_at);

create table ct_credit_scores
(
    user_id         bigint                             not null comment '关联 ur_users.id'
        primary key,
    current_score   int      default 80                not null comment '当前分数，默认 80',
    min_threshold   int      default 60                null comment '最低阈值，低于此值限制发帖',
    last_updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    version         int      default 0                 null comment '乐观锁版本号'
)
    comment '信誉分主表';

create table ds_arbitration_cases
(
    id                        bigint auto_increment
        primary key,
    order_id                  bigint                             not null,
    initiator_id              bigint                             not null,
    user_evidence_summary     text                               null,
    merchant_evidence_summary text                               null,
    admin_comment             text                               null comment '管理员判词',
    ruling_result             tinyint                            null comment '1:全额结款，2:部分结款，3:不结款，4:退还押金',
    final_settlement_amount   decimal(10, 2)                     null,
    handled_by_admin_id       bigint                             null,
    status                    tinyint  default 1                 null comment '1:审理中，2:已结案',
    created_at                datetime default CURRENT_TIMESTAMP null,
    closed_at                 datetime                           null,
    constraint order_id
        unique (order_id)
)
    comment '仲裁案件表';

create table ds_complaints
(
    id                  bigint auto_increment
        primary key,
    complainant_id      bigint                             not null,
    defendant_id        bigint                             not null,
    target_type         tinyint                            not null comment '1:商家，2:用户',
    related_order_id    bigint                             null,
    reason_code         varchar(50)                        null,
    description         text                               null,
    evidence_urls       json                               null,
    status              tinyint  default 1                 null comment '1:待处理，2:处理中，3:成功(处罚), 4:失败(证据不足)',
    result_action       varchar(255)                       null comment '处罚结果描述',
    handled_by_admin_id bigint                             null,
    created_at          datetime default CURRENT_TIMESTAMP null,
    closed_at           datetime                           null
)
    comment '投诉主表';

create index idx_defendant
    on ds_complaints (defendant_id, status);

create table jb_categories
(
    id         int auto_increment
        primary key,
    name       varchar(64)                        not null,
    parent_id  int      default 0                 null,
    sort_order int      default 0                 null,
    is_visible tinyint  default 1                 null comment '前端是否显示开关',
    created_at datetime default CURRENT_TIMESTAMP null
)
    comment '分类表';

create table jb_home_configs
(
    id            bigint auto_increment
        primary key,
    config_key    varchar(64)                           not null comment '如：banner_switch, ranking_module',
    config_value  json                                  not null comment '具体配置内容',
    is_active     tinyint     default 1                 null comment '全局开关',
    target_region varchar(20) default 'ALL'             null comment '针对特定地区配置',
    updated_by    bigint                                null comment '最后修改的管理员 ID',
    updated_at    datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint config_key
        unique (config_key)
)
    comment '首页装修配置表';


CREATE TABLE jb_jobs
(
    id               bigint AUTO_INCREMENT PRIMARY KEY,
    merchant_id      bigint                           NOT NULL COMMENT '发布者 ID',
    title            varchar(128)                     NOT NULL,
    description      text                             NULL,
    salary_min       decimal(10, 2)                   NULL,
    salary_max       decimal(10, 2)                   NULL,
    -- 新增字段开始
    settlement_cycle tinyint  DEFAULT 1               NOT NULL COMMENT '结算周期: 1-日结, 2-周结',
    -- 新增字段结束
    category_id      int                              NOT NULL,
    region_code      varchar(20)                      NOT NULL,
    deposit_amount   decimal(10, 2)                   NOT NULL COMMENT '押金金额 (商家担保)',
    status           tinyint  DEFAULT 1               NULL COMMENT '1:发布中，2:进行中，3:已完成，4:已关闭，5:强制下架',
    trade_mode       tinyint  DEFAULT 1               NULL COMMENT '1:平台担保，2:允许线下',
    publish_time     datetime                         NULL,
    expire_time      datetime                         NULL,
    created_at       datetime DEFAULT CURRENT_TIMESTAMP NULL
) COMMENT '兼职岗位表';


create index idx_merchant
    on jb_jobs (merchant_id);

create index idx_region_cat
    on jb_jobs (region_code, category_id);

create index idx_status
    on jb_jobs (status);

create table ms_conversations
(
    session_id       varchar(64)                        not null comment '会话唯一 ID'
        primary key,
    participant_ids  json                               not null comment '[user_id, merchant_id]',
    job_id           bigint                             null comment '关联的兼职 ID',
    last_msg_content varchar(255)                       null,
    last_msg_time    datetime                           null,
    updated_at       datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    comment '会话表';

create table ms_messages
(
    id          bigint auto_increment
        primary key,
    session_id  varchar(64)                        not null,
    sender_id   bigint                             not null,
    receiver_id bigint                             not null,
    msg_type    tinyint  default 1                 null comment '1:文本，2:图片，3:简历卡片，4:微信交换',
    content     text                               null,
    is_read     tinyint  default 0                 null,
    created_at  datetime default CURRENT_TIMESTAMP null
)
    comment '消息内容表';

create index idx_session_time
    on ms_messages (session_id, created_at);

create table ms_notifications
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                             not null,
    title       varchar(128)                       null,
    content     text                               null,
    notify_type varchar(50)                        null comment 'JOB_DOWNFORCED, SCORE_CHANGE, BAN_WARNING',
    is_read     tinyint  default 0                 null,
    created_at  datetime default CURRENT_TIMESTAMP null
)
    comment '系统通知表';

create index idx_user_read
    on ms_notifications (user_id, is_read);

create table od_applications
(
    id              bigint auto_increment
        primary key,
    job_id          bigint                             not null,
    user_id         bigint                             not null,
    status          tinyint  default 1                 null comment '1:已投递，2:面试中，3:已录用，4:已拒绝',
    resume_snapshot json                               null comment '投递时的简历快照',
    created_at      datetime default CURRENT_TIMESTAMP null
)
    comment '投递记录表';

create index idx_job_user
    on od_applications (job_id, user_id);

create table od_evidence
(
    id            bigint auto_increment
        primary key,
    order_id      bigint                             not null,
    uploader_type tinyint                            not null comment '1:用户，2:商家',
    uploader_id   bigint                             not null,
    evidence_type tinyint                            null comment '1:图片，2:视频，3:文本',
    file_url      varchar(255)                       null,
    description   varchar(500)                       null,
    created_at    datetime default CURRENT_TIMESTAMP null
)
    comment '订单证据表';

create index idx_order
    on od_evidence (order_id);

create table od_orders
(
    id                bigint auto_increment
        primary key,
    application_id    bigint                                   not null,
    job_id            bigint                                   not null,
    user_id           bigint                                   not null,
    merchant_id       bigint                                   not null,
    trade_mode        tinyint                                  not null comment '1:平台担保，2:线下交易',
    order_status      tinyint        default 1                 not null comment '1:待入职，2:进行中(押金冻结), 3:已完成，4:纠纷中，5:已结款，6:用户爽约，7:异常终止',
    deposit_frozen_id bigint                                   null comment '关联 wl_freeze_records.id',
    penalty_amount    decimal(10, 2) default 0.00              null comment '违约金金额',
    start_time        datetime                                 null,
    end_time          datetime                                 null,
    completed_at      datetime                                 null,
    created_at        datetime       default CURRENT_TIMESTAMP null
)
    comment '订单主表';

create index idx_merchant_status
    on od_orders (merchant_id, order_status);

create index idx_user_status
    on od_orders (user_id, order_status);

CREATE TABLE ur_intentions (
                               id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                               user_id       BIGINT                             NOT NULL COMMENT '用户ID',

    -- 新增的个人信息字段 --
                               student_id    VARCHAR(50)                        NOT NULL COMMENT '学号',
                               real_name     VARCHAR(50)                        NOT NULL COMMENT '真实姓名',
                               age           TINYINT UNSIGNED                   NOT NULL DEFAULT 0 COMMENT '年龄',
                               gender        TINYINT                            NOT NULL DEFAULT 0 COMMENT '性别（如：0-未知，1-男，2-女）',
                               phone         VARCHAR(20)                        NOT NULL COMMENT '电话',
                               profession    VARCHAR(100)                       NOT NULL DEFAULT '' COMMENT '职业',
                               introduction  TEXT                               NULL COMMENT '个人简介',

    -- 原有的意向字段 --
                               tag_name      VARCHAR(50)                        NOT NULL COMMENT '求职意向标签，如：周末可做，接受夜班',

                               created_at    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='求职意向表';

create index idx_user_tag
    on ur_intentions (user_id, tag_name);

create table ur_merchant_profiles
(
    user_id            bigint                             not null comment '关联 ur_users.id'
        primary key,
    company_name       varchar(128)                       not null comment '企业名称',
    license_url        varchar(255)                       null comment '营业执照 URL',
    parent_merchant_id bigint                             null comment '父账号 ID (实现连坐机制，子账号指向主账号)',
    is_sub_account     tinyint  default 0                 null comment '0:主账号, 1:子账号',
    contact_phone      varchar(20)                        null,
    location           varchar(255)                       null comment '商家位置',
    legal_person       varchar(64)                        null comment '法人姓名',
    legal_id_card      varchar(32)                        null comment '法人身份证号',
    company_images     json                               null comment '公司图片数组 JSON',
    registered_capital decimal(18, 4)                     null comment '注册成本',
    company_address    varchar(255)                       null comment '公司地址',
    company_intro      text                               null comment '公司简介',
    created_at         datetime default CURRENT_TIMESTAMP null
)
    comment '商家档案表';

create index idx_parent
    on ur_merchant_profiles (parent_merchant_id);

create table ur_resumes
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                             not null comment '关联 ur_users.id',
    file_name   varchar(128)                       null,
    file_url    varchar(255)                       not null,
    version_num int      default 1                 null,
    is_deleted  tinyint  default 0                 null,
    created_at  datetime default CURRENT_TIMESTAMP null
)
    comment '简历文件表';

create index idx_user
    on ur_resumes (user_id);

create table ur_users
(
    id            bigint auto_increment comment '用户全局唯一 ID'
        primary key,
    user_type     tinyint  default 1                 not null comment '1:求职者(C), 2:商家(B), 3:管理员(Admin)',
    username      varchar(64)                        not null comment '用户名/手机号',
    password_hash varchar(128)                       not null comment '加密密码',
    avatar_url    varchar(255)                       null comment '头像',
    region_code   varchar(20)                        null comment '所在地区代码',
    status        tinyint  default 1                 null comment '1:正常, 0:封禁, -1:注销',
    created_at    datetime default CURRENT_TIMESTAMP null,
    updated_at    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    comment '用户基础表';




create index idx_region
    on ur_users (region_code);

create index idx_status
    on ur_users (status);

create table wl_freeze_records
(
    id             bigint auto_increment
        primary key,
    user_id        bigint                             not null comment '被冻结资金的用户 (通常是商家押金，但也可能是用户押金)',
    order_id       bigint                             not null,
    amount         decimal(18, 4)                     not null,
    status         tinyint  default 1                 not null comment '1:冻结中，2:已解冻(退还), 3:已扣除(赔付)',
    release_reason varchar(255)                       null,
    created_at     datetime default CURRENT_TIMESTAMP null,
    resolved_at    datetime                           null
)
    comment '资金冻结记录表';

create index idx_order
    on wl_freeze_records (order_id);

create table wl_transaction_logs
(
    id                bigint auto_increment
        primary key,
    user_id           bigint                             not null,
    type              varchar(50)                        not null comment 'DEPOSIT_IN, SALARY_PAY, PENALTY_OUT, REFUND',
    amount            decimal(18, 4)                     not null,
    balance_before    decimal(18, 4)                     not null,
    balance_after     decimal(18, 4)                     not null,
    related_order_id  bigint                             null,
    related_freeze_id bigint                             null,
    description       varchar(255)                       null,
    created_at        datetime default CURRENT_TIMESTAMP null
)
    comment '交易流水表';

create index idx_user_time
    on wl_transaction_logs (user_id, created_at);

create table wl_wallets
(
    user_id        bigint                                   not null comment '关联 ur_users.id'
        primary key,
    balance        decimal(18, 4) default 0.0000            null comment '可用余额',
    frozen_balance decimal(18, 4) default 0.0000            null comment '冻结金额 (在押资金)',
    total_income   decimal(18, 4) default 0.0000            null,
    version        int            default 0                 null comment '乐观锁，防止并发扣款',
    updated_at     datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    comment '钱包账户表';

