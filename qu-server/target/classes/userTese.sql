-- 先创建存储过程（如果已存在则先删除）
-- 此处用于测试生成用户数据
DROP PROCEDURE IF EXISTS generate_test_users;
DELIMITER //
CREATE PROCEDURE generate_test_users(IN num INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    -- 定义常用的姓氏和名字，用于拼接真实姓名
    DECLARE last_names VARCHAR(1000) DEFAULT '赵,钱,孙,李,周,吴,郑,王,冯,陈,褚,卫,蒋,沈,韩,杨,朱,秦,尤,许';
    DECLARE first_names VARCHAR(2000) DEFAULT '伟,芳,娜,敏,静,丽,强,磊,军,洋,勇,艳,杰,娟,涛,明,超,秀,宇,浩';
    -- 定义职业简介，用于生成个人简介
    DECLARE introductions VARCHAR(4000) DEFAULT '热爱编程的后端开发工程师,专注于前端可视化的开发人员,产品经理擅长需求分析和原型设计,UI设计师热爱简约风格的视觉设计,测试工程师专注于自动化测试和性能测试,大数据开发工程师熟悉Hadoop和Spark,运维工程师负责服务器和容器化部署,机器学习算法工程师专注于计算机视觉,内容运营负责平台内容的创作和推广,电商运营擅长店铺优化和活动策划,人力资源专员负责招聘和员工培训,财务会计负责公司账务处理和报表编制,法务专员负责合同审核和法律咨询,市场推广负责品牌宣传和渠道拓展,客服专员负责用户问题解答和售后处理,数据分析师擅长数据可视化和趋势分析,架构师负责系统架构设计和技术选型';
    DECLARE random_last_name VARCHAR(10);
    DECLARE random_first_name VARCHAR(10);
    DECLARE random_username VARCHAR(20);
    DECLARE random_email VARCHAR(50);
    DECLARE random_phone VARCHAR(20);
    DECLARE random_status INT;
    DECLARE random_intro VARCHAR(200);
    DECLARE random_create_time DATETIME;
    DECLARE random_update_time DATETIME;

    -- 循环生成num条数据
    WHILE i <= num DO
            -- 随机获取姓氏和名字，拼接真实姓名
            SET random_last_name = SUBSTRING_INDEX(SUBSTRING_INDEX(last_names, ',', FLOOR(RAND() * 20 + 1)), ',', -1);
            SET random_first_name = SUBSTRING_INDEX(SUBSTRING_INDEX(first_names, ',', FLOOR(RAND() * 20 + 1)), ',', -1);
            -- 生成用户名（真实姓名拼音+随机数字）
            SET random_username = CONCAT(LOWER(CONVERT(random_last_name USING ascii)), LOWER(CONVERT(random_first_name USING ascii)), FLOOR(RAND() * 1000));
            -- 生成邮箱（用户名+随机域名）
            SET random_email = CONCAT(random_username, '@', ELT(FLOOR(RAND() * 3 + 1), 'example.com', '163.com', 'qq.com'));
            -- 生成手机号（13开头的随机手机号）
            SET random_phone = CONCAT('13', FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10), FLOOR(RAND() * 10));
            -- 随机状态（0:拉黑，概率10%；1:正常，概率90%）
            SET random_status = IF(RAND() < 0.1, 0, 1);
            -- 随机个人简介（如果是拉黑状态，显示拉黑相关简介）
            SET random_intro = IF(random_status = 0, '因违规被拉黑的用户，暂无简介', SUBSTRING_INDEX(SUBSTRING_INDEX(introductions, ',', FLOOR(RAND() * 17 + 1)), ',', -1));
            -- 随机创建时间（近一年以内）
            SET random_create_time = NOW() - INTERVAL FLOOR(RAND() * 365) DAY;
            -- 随机更新时间（创建时间之后，当前时间之前）
            SET random_update_time = random_create_time + INTERVAL FLOOR(RAND() * (DATEDIFF(NOW(), random_create_time))) DAY;

            -- 插入数据（忽略唯一键冲突，比如用户名/邮箱重复）
            INSERT IGNORE INTO user (username, password, real_name, status, email, phone, introduction, create_time, update_time)
            VALUES (random_username, 'e10adc3949ba59abbe56e057f20f883e', CONCAT(random_last_name, random_first_name), random_status, random_email, random_phone, random_intro, random_create_time, random_update_time);

            SET i = i + 1;
END WHILE;
END //
DELIMITER ;

-- 调用存储过程，生成1000条测试数据（可修改数字为需要的数量，比如500、2000）
CALL generate_test_users(1000);

-- 可选：删除存储过程（生成数据后可执行）
-- DROP PROCEDURE IF EXISTS generate_test_users;