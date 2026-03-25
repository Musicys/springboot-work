-- 测试数据脚本

-- 1. 用户表 (ur_users)
INSERT INTO ur_users (user_type, username, password_hash, avatar_url, region_code, status) VALUES
(1, 'user1', '123456', 'https://example.com/avatar1.jpg', '310100', 1),
(1, 'user2', '123456', 'https://example.com/avatar2.jpg', '310100', 1),
(2, 'merchant1', '123456', 'https://example.com/avatar3.jpg', '310100', 1),
(2, 'merchant2', '123456', 'https://example.com/avatar4.jpg', '310100', 1),
(3, 'admin', '123456', 'https://example.com/avatar5.jpg', '310100', 1);

-- 2. 商家档案表 (ur_merchant_profiles)
INSERT INTO ur_merchant_profiles (user_id, company_name, license_url, parent_merchant_id, is_sub_account, contact_phone) VALUES
(3, '测试公司1', 'https://example.com/license1.jpg', NULL, 0, '13800138001'),
(4, '测试公司2', 'https://example.com/license2.jpg', NULL, 0, '13800138002');

-- 3. 简历文件表 (ur_resumes)
INSERT INTO ur_resumes (user_id, file_name, file_url, version_num, is_deleted) VALUES
(1, '简历1.pdf', 'https://example.com/resume1.pdf', 1, 0),
(2, '简历2.pdf', 'https://example.com/resume2.pdf', 1, 0);

-- 4. 求职意向表 (ur_intentions)
INSERT INTO ur_intentions (user_id, tag_name) VALUES
(1, '周末可做'),
(1, '接受夜班'),
(2, '短期兼职'),
(2, '日结');

-- 5. 分类表 (jb_categories)
INSERT INTO jb_categories (name, parent_id, sort_order, is_visible) VALUES
('兼职', 0, 1, 1),
('全职', 0, 2, 1),
('线上兼职', 1, 1, 1),
('线下兼职', 1, 2, 1),
('家教', 3, 1, 1),
('客服', 3, 2, 1),
('促销', 4, 1, 1),
('餐饮', 4, 2, 1);

-- 6. 兼职岗位表 (jb_jobs)
INSERT INTO jb_jobs (merchant_id, title, description, salary_min, salary_max, category_id, region_code, deposit_amount, status, trade_mode, publish_time, expire_time) VALUES
(3, '线上客服', '负责客户咨询，解答问题', 100.00, 150.00, 6, '310100', 200.00, 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
(3, '线下促销', '在商场进行产品促销', 120.00, 180.00, 7, '310100', 300.00, 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
(4, '家教老师', '辅导小学生数学', 200.00, 250.00, 5, '310100', 500.00, 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
(4, '餐饮服务员', '在餐厅提供服务', 100.00, 120.00, 8, '310100', 200.00, 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));

-- 7. 投递记录表 (od_applications)
INSERT INTO od_applications (job_id, user_id, status, resume_snapshot) VALUES
(1, 1, 1, '{"name": "用户1", "experience": "无经验"}'),
(1, 2, 2, '{"name": "用户2", "experience": "有客服经验"}'),
(2, 1, 3, '{"name": "用户1", "experience": "有促销经验"}'),
(3, 2, 1, '{"name": "用户2", "experience": "有家教经验"}');

-- 8. 订单主表 (od_orders)
INSERT INTO od_orders (application_id, job_id, user_id, merchant_id, trade_mode, order_status, deposit_frozen_id, penalty_amount, start_time, end_time, completed_at) VALUES
(1, 1, 1, 3, 1, 1, NULL, 0.00, NULL, NULL, NULL),
(2, 1, 2, 3, 1, 2, NULL, 0.00, NOW(), NULL, NULL),
(3, 2, 1, 3, 1, 3, NULL, 0.00, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 9. 订单证据表 (od_evidence)
INSERT INTO od_evidence (order_id, uploader_type, uploader_id, evidence_type, file_url, description) VALUES
(2, 1, 2, 1, 'https://example.com/evidence1.jpg', '工作照片'),
(3, 1, 1, 1, 'https://example.com/evidence2.jpg', '完成证明'),
(3, 2, 3, 1, 'https://example.com/evidence3.jpg', '确认照片');

-- 10. 仲裁案件表 (ds_arbitration_cases)
INSERT INTO ds_arbitration_cases (order_id, initiator_id, user_evidence_summary, merchant_evidence_summary, admin_comment, ruling_result, final_settlement_amount, handled_by_admin_id, status, created_at, closed_at) VALUES
(1, 1, '商家未按约定支付工资', '已按时支付', '经核实，商家已支付工资', 1, 100.00, 5, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 11. 投诉主表 (ds_complaints)
INSERT INTO ds_complaints (complainant_id, defendant_id, target_type, related_order_id, reason_code, description, evidence_urls, status, result_action, handled_by_admin_id, created_at, closed_at) VALUES
(1, 3, 2, 1, 'PAYMENT_DELAY', '商家延迟支付工资', '["https://example.com/complaint1.jpg"]', 3, '警告商家并督促支付', 5, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 3, 2, 2, 'WORK_CONDITION', '工作条件恶劣', '["https://example.com/complaint2.jpg"]', 2, NULL, 5, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL);

-- 12. 信誉分主表 (ct_credit_scores)
INSERT INTO ct_credit_scores (user_id, current_score, min_threshold, last_updated_at, version) VALUES
(1, 85, 60, NOW(), 0),
(2, 90, 60, NOW(), 0),
(3, 80, 60, NOW(), 0),
(4, 82, 60, NOW(), 0);

-- 13. 信誉分流水表 (ct_credit_logs)
INSERT INTO ct_credit_logs (user_id, change_amount, reason_type, related_order_id, operator_id, description, balance_after, created_at) VALUES
(1, 5, 'COMPLETE_JOB', 3, NULL, '完成订单加5分', 85, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, -5, 'NO_SHOW', 1, 5, '未按约定提供工作条件扣5分', 80, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 14. 钱包账户表 (wl_wallets)
INSERT INTO wl_wallets (user_id, balance, frozen_balance, total_income, version, updated_at) VALUES
(1, 1000.00, 0.00, 1000.00, 0, NOW()),
(2, 1500.00, 0.00, 1500.00, 0, NOW()),
(3, 5000.00, 200.00, 10000.00, 0, NOW()),
(4, 8000.00, 300.00, 15000.00, 0, NOW());

-- 15. 资金冻结记录表 (wl_freeze_records)
INSERT INTO wl_freeze_records (user_id, order_id, amount, status, release_reason, created_at, resolved_at) VALUES
(3, 1, 200.00, 1, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),
(3, 2, 200.00, 1, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(4, 3, 500.00, 2, '订单完成', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 16. 交易流水表 (wl_transaction_logs)
INSERT INTO wl_transaction_logs (user_id, type, amount, balance_before, balance_after, related_order_id, related_freeze_id, description, created_at) VALUES
(1, 'SALARY_PAY', 120.00, 880.00, 1000.00, 3, NULL, '工资收入', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 'DEPOSIT_IN', 500.00, 4500.00, 5000.00, NULL, NULL, '充值押金', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(4, 'PENALTY_OUT', 100.00, 8100.00, 8000.00, 1, NULL, '违约金扣除', DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 17. 会话表 (ms_conversations)
INSERT INTO ms_conversations (session_id, participant_ids, job_id, last_msg_content, last_msg_time, updated_at) VALUES
('session1', '[1,3]', 1, '您好，我对这个岗位感兴趣', NOW(), NOW()),
('session2', '[2,4]', 3, '请问具体工作时间是什么时候？', NOW(), NOW());

-- 18. 消息内容表 (ms_messages)
INSERT INTO ms_messages (session_id, sender_id, receiver_id, msg_type, content, is_read, created_at) VALUES
('session1', 1, 3, 1, '您好，我对这个岗位感兴趣', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('session1', 3, 1, 1, '您好，请问您有相关经验吗？', 0, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('session2', 2, 4, 1, '请问具体工作时间是什么时候？', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('session2', 4, 2, 1, '工作日晚上6点到9点', 0, DATE_SUB(NOW(), INTERVAL 1 HOUR));

-- 19. 系统通知表 (ms_notifications)
INSERT INTO ms_notifications (user_id, title, content, notify_type, is_read, created_at) VALUES
(1, '订单状态更新', '您的订单已完成', 'ORDER_STATUS_CHANGE', 0, NOW()),
(2, '面试邀请', '您投递的岗位已邀请面试', 'INTERVIEW_INVITE', 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, '投诉处理', '您收到一条投诉需要处理', 'COMPLAINT_RECEIVED', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, '岗位审核', '您发布的岗位已审核通过', 'JOB_APPROVED', 1, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 20. CMS 模块配置表 (cm_modules)
INSERT INTO cm_modules (module_key, module_name, is_enabled, config_data, priority, updated_by, updated_at) VALUES
('home_banner', '首页 banner', 1, '{"images": [{"url": "https://example.com/banner1.jpg", "link": "/jobs"}, {"url": "https://example.com/banner2.jpg", "link": "/merchants"}]}', 1, 5, NOW()),
('salary_ranking', '薪资排行', 1, '{"limit": 10, "region": "310100"}', 2, 5, NOW());

-- 21. 首页装修配置表 (jb_home_configs)
INSERT INTO jb_home_configs (config_key, config_value, is_active, target_region, updated_by, updated_at) VALUES
('banner_switch', '{"enabled": true, "interval": 5000}', 1, 'ALL', 5, NOW()),
('ranking_module', '{"enabled": true, "type": "salary"}', 1, 'ALL', 5, NOW()),
('recommended_jobs', '{"limit": 6, "algorithm": "hot"}', 1, '310100', 5, NOW());
