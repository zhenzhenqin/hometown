-- AI智能问答功能相关表

-- 1. 知识库表：存储景点/特产/文化的介绍，用于AI回答的上下文
CREATE TABLE `ai_knowledge_base` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category` varchar(50) NOT NULL COMMENT '分类：attraction-景点, specialty-特产, culture-文化',
    `source_id` int(11) NOT NULL COMMENT '关联来源ID（景点/特产/文化ID）',
    `title` varchar(200) NOT NULL COMMENT '标题',
    `content` text NOT NULL COMMENT '详细内容（用于AI理解）',
    `tags` varchar(255) DEFAULT NULL COMMENT '标签，用逗号分隔',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_category` (`category`),
    INDEX `idx_source_id` (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库表';

-- 2. AI会话表
CREATE TABLE `ai_conversation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `user_id` int(11) DEFAULT NULL COMMENT '用户ID（游客为null）',
    `session_id` varchar(64) NOT NULL COMMENT '会话唯一标识（UUID）',
    `title` varchar(200) DEFAULT NULL COMMENT '会话标题（第一条消息前20字）',
    `message_count` int(11) DEFAULT 0 COMMENT '消息数量',
    `last_message` text DEFAULT NULL COMMENT '最后一条消息内容',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_id` (`session_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI会话表';

-- 3. AI消息表
CREATE TABLE `ai_message` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `conversation_id` bigint(20) NOT NULL COMMENT '会话ID',
    `role` varchar(20) NOT NULL COMMENT '角色：user-用户, assistant-AI助手',
    `content` text NOT NULL COMMENT '消息内容',
    `tokens` int(11) DEFAULT 0 COMMENT '消耗token数',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_conversation_id` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI消息表';

-- 初始化知识库数据（示例）
INSERT INTO `ai_knowledge_base` (`category`, `source_id`, `title`, `content`, `tags`) VALUES
('attraction', 1, '天脊龙门', '天脊龙门景区位于浙江省衢州市龙游县，是国家4A级旅游景区。景区内飞瀑绝壁、植被繁茂，负氧离子含量极高，是避暑纳凉的绝佳去处。主要景点包括龙凤楼、索桥、玻璃观景台等。门票价格约60元，适合全家出游。', '景点,龙游,避暑,峡谷'),
('attraction', 2, '江郎山', '江郎山位于浙江省衢州市江山市，是世界自然遗产地、国家级风景名胜区、国家5A级旅游景区。三片丹霞岩石拔地冲天，被誉为"中国丹霞第一奇峰"。主峰海拔819米，郎峰、亚峰、灵峰三峰对峙，蔚为壮观。', '景点,江山,5A景区,世界遗产,丹霞'),
('specialty', 1, '衢州烤饼', '衢州烤饼是浙江省衢州市的传统小吃，已有千年历史。选用上等面粉，配以鲜肉、葱花和特制调料，在炭火炉中烤制而成。饼皮酥脆、内馅鲜美，是衢州人最爱的早餐之一。价格实惠，深受当地人和游客喜爱。', '美食,小吃,传统,早餐'),
('specialty', 2, '龙游发糕', '龙游发糕是衢州市龙游县的传统糕点，起源于明代。选用上等糯米和粳米，按一定比例混合浸泡，磨成米浆后加入酒酿发酵，蒸制而成。口感软糯、香甜可口，是馈赠亲友的佳品。', '美食,糕点,传统,伴手礼'),
('culture', 1, '南孔文化', '衢州是孔氏南宗所在地，南宋建炎二年（1128年），孔子第48代孙孔端友随宋高宗南渡，赐家于衢州。从此，衢州成为南孔圣地。衢州孔庙是全国仅有的两座孔氏家庙之一，具有极高的历史和文化价值。', '文化,儒家,非遗,历史');
