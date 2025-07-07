-- 学生管理系统初始化数据
-- 插入测试学生数据

INSERT INTO students (name, student_number, age, gender, major, email, phone, enrollment_date, created_time, updated_time) VALUES
('张三', '20210001', 20, '男', '计算机科学与技术', 'zhangsan@example.com', '13800138001', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('李四', '20210002', 19, '女', '软件工程', 'lisi@example.com', '13800138002', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('王五', '20210003', 21, '男', '信息安全', 'wangwu@example.com', '13800138003', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('赵六', '20210004', 20, '女', '数据科学与大数据技术', 'zhaoliu@example.com', '13800138004', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('钱七', '20210005', 22, '男', '人工智能', 'qianqi@example.com', '13800138005', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('孙八', '20210006', 19, '女', '计算机科学与技术', 'sunba@example.com', '13800138006', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('周九', '20210007', 21, '男', '软件工程', 'zhoujiu@example.com', '13800138007', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('吴十', '20210008', 20, '女', '信息安全', 'wushi@example.com', '13800138008', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('郑十一', '20210009', 23, '男', '数据科学与大数据技术', 'zhengshiyi@example.com', '13800138009', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('王十二', '20210010', 19, '女', '人工智能', 'wangshier@example.com', '13800138010', '2021-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('刘明', '20220001', 18, '男', '计算机科学与技术', 'liuming@example.com', '13900139001', '2022-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('陈红', '20220002', 19, '女', '软件工程', 'chenhong@example.com', '13900139002', '2022-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('杨强', '20220003', 20, '男', '信息安全', 'yangqiang@example.com', '13900139003', '2022-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('黄丽', '20220004', 18, '女', '数据科学与大数据技术', 'huangli@example.com', '13900139004', '2022-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('林峰', '20220005', 21, '男', '人工智能', 'linfeng@example.com', '13900139005', '2022-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);