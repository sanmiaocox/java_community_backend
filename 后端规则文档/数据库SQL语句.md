# 数据库SQL语句记录

> **数据库名称**: final  
> **数据库类型**: MySQL 8.0+  
> **字符集**: utf8mb4  
> **排序规则**: utf8mb4_0900_ai_ci  
> **最后更新**: 2026-02-27

---

## 📋 目录

1. [数据库创建](#数据库创建)
2. [用户表（users）](#用户表users)
3. [电影表（movies）](#电影表movies)
4. [动态表（feeds）](#动态表feeds)
5. [评论表（comments）](#评论表comments)
6. [点赞表（likes）](#点赞表likes)
7. [收藏表（favorites）](#收藏表favorites)
8. [活动表（events）](#活动表events)
9. [活动参与表（event_participants）](#活动参与表event_participants)
10. [测试数据](#测试数据)

---

## 数据库创建

### 创建数据库

```sql
-- 创建数据库（如果已存在则跳过）
CREATE DATABASE IF NOT EXISTS final 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_0900_ai_ci;

-- 使用数据库
USE final;
```

**执行时间**: 2026-02-27  
**说明**: 创建项目主数据库

---

## 用户表（users）

### 创建表

```sql
-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    phone VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    avatar VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
```

**执行时间**: 2026-02-27  
**说明**: 存储用户基本信息，密码使用BCrypt加密

### 查看表结构

```sql
-- 查看用户表结构
DESC users;

-- 查看创建语句
SHOW CREATE TABLE users;
```

---

## 电影表（movies）

### 创建表

```sql
-- 创建电影表
CREATE TABLE IF NOT EXISTS movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '电影ID',
    tmdb_id INT UNIQUE COMMENT 'TMDB电影ID',
    title VARCHAR(200) NOT NULL COMMENT '电影标题',
    original_title VARCHAR(200) DEFAULT NULL COMMENT '原始标题',
    aliases VARCHAR(500) DEFAULT NULL COMMENT '别名（JSON数组）',
    poster_url VARCHAR(500) DEFAULT NULL COMMENT '海报URL',
    poster_urls VARCHAR(2000) DEFAULT NULL COMMENT '多张海报（JSON数组）',
    rating DECIMAL(3,1) DEFAULT NULL COMMENT '评分',
    rating_source VARCHAR(50) DEFAULT NULL COMMENT '评分来源',
    release_date VARCHAR(100) DEFAULT NULL COMMENT '上映日期',
    episodes VARCHAR(50) DEFAULT NULL COMMENT '集数',
    genres VARCHAR(200) DEFAULT NULL COMMENT '类型（JSON数组）',
    region VARCHAR(100) DEFAULT NULL COMMENT '地区',
    languages VARCHAR(200) DEFAULT NULL COMMENT '语言（JSON数组）',
    directors VARCHAR(500) DEFAULT NULL COMMENT '导演（JSON数组）',
    actors VARCHAR(1000) DEFAULT NULL COMMENT '演员（JSON数组）',
    synopsis TEXT DEFAULT NULL COMMENT '剧情简介',
    douban_url VARCHAR(500) DEFAULT NULL COMMENT '豆瓣链接',
    tmdb_url VARCHAR(500) DEFAULT NULL COMMENT 'TMDB链接',
    year VARCHAR(50) DEFAULT NULL COMMENT '年份',
    genre VARCHAR(50) DEFAULT NULL COMMENT '主要类型',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_rating (rating),
    INDEX idx_year (year),
    INDEX idx_tmdb_id (tmdb_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影表';
```

**执行时间**: 2026-02-27  
**说明**: 存储电影详细信息，只保留豆瓣和TMDB链接

### 如果表已存在，添加tmdb_id字段

```sql
-- 为现有movies表添加tmdb_id字段
ALTER TABLE movies 
ADD COLUMN tmdb_id INT UNIQUE COMMENT 'TMDB电影ID' 
AFTER id;

-- 添加索引
CREATE INDEX idx_tmdb_id ON movies(tmdb_id);
```

**执行时间**: 2026-03-02  
**说明**: 为现有电影表添加TMDB ID字段，用于关联TMDB数据

---

## 动态表（feeds）

### 创建表

```sql
-- 创建动态表
CREATE TABLE IF NOT EXISTS feeds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    movie_id BIGINT DEFAULT NULL COMMENT '电影ID（可选）',
    event_id BIGINT DEFAULT NULL COMMENT '活动ID（可选）',
    content TEXT NOT NULL COMMENT '动态内容',
    rating DECIMAL(3,1) DEFAULT NULL COMMENT '评分',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    share_count INT NOT NULL DEFAULT 0 COMMENT '分享数',
    comment_count INT NOT NULL DEFAULT 0 COMMENT '评论数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_movie_id (movie_id),
    INDEX idx_event_id (event_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE SET NULL,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态表';
```

**执行时间**: 2026-02-27  
**说明**: 存储用户发布的动态，可以关联电影、活动，或两者都关联，或都不关联

---

## 评论表（comments）

### 创建表

```sql
-- 创建评论表
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    feed_id BIGINT NOT NULL COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_feed_id (feed_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
```

**执行时间**: 2026-02-27  
**说明**: 存储动态的评论

---

## 点赞表（likes）

### 创建表

```sql
-- 创建点赞表
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    feed_id BIGINT NOT NULL COMMENT '动态ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_feed (user_id, feed_id) COMMENT '用户-动态唯一索引',
    INDEX idx_feed_id (feed_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点赞表';
```

**执行时间**: 2026-02-27  
**说明**: 存储用户对动态的点赞记录，防止重复点赞

---

## 收藏夹表（collections）

### 创建表

```sql
-- 创建收藏夹表
CREATE TABLE IF NOT EXISTS collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏夹ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '收藏夹名称',
    description TEXT COMMENT '收藏夹描述',
    type ENUM('MOVIE', 'EVENT') NOT NULL DEFAULT 'MOVIE' COMMENT '类型:电影收藏夹/活动收藏夹',
    is_system BOOLEAN DEFAULT FALSE COMMENT '是否系统收藏夹',
    is_public BOOLEAN DEFAULT TRUE COMMENT '是否公开',
    cover_image VARCHAR(500) COMMENT '封面图片',
    item_count INT DEFAULT 0 COMMENT '收藏项数量',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    CONSTRAINT fk_collections_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏夹表';
```

**执行时间**: 2026-03-01  
**说明**: 存储用户创建的收藏夹，支持两种类型：MOVIE(电影收藏夹)、EVENT(活动收藏夹)。看过功能使用独立的 watched_movies 表

---

## 收藏项表（favorites）

### 创建表（全新安装）

```sql
-- 创建收藏项表（全新安装使用）
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    collection_id BIGINT NOT NULL COMMENT '收藏夹ID',
    item_type ENUM('MOVIE', 'EVENT') NOT NULL DEFAULT 'MOVIE' COMMENT '收藏项类型',
    item_id BIGINT NOT NULL COMMENT '收藏项ID（电影ID或活动ID）',
    movie_id BIGINT DEFAULT NULL COMMENT '电影ID（已废弃，兼容旧数据）',
    note TEXT COMMENT '用户备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_collection_item (collection_id, item_type, item_id) COMMENT '防止重复收藏',
    INDEX idx_user_id (user_id),
    INDEX idx_collection_id (collection_id),
    INDEX idx_item (item_type, item_id),
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_collection FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏项表';
```

**执行时间**: 2026-03-01  
**说明**: 存储收藏夹中的具体内容（电影或活动），支持用户备注

### 改造现有表（已有数据迁移）

```sql
-- ========== 步骤1: 先创建collections表 ==========
-- （使用上面的collections表创建语句）

-- ========== 步骤2: 改造favorites表 ==========

-- 步骤2.1: 添加新字段
ALTER TABLE favorites 
ADD COLUMN collection_id BIGINT DEFAULT NULL COMMENT '收藏夹ID' AFTER user_id,
ADD COLUMN item_type ENUM('MOVIE', 'EVENT') NOT NULL DEFAULT 'MOVIE' COMMENT '收藏项类型' AFTER collection_id,
ADD COLUMN item_id BIGINT NOT NULL DEFAULT 0 COMMENT '收藏项ID' AFTER item_type,
ADD COLUMN note TEXT COMMENT '用户备注' AFTER item_id;

-- 步骤2.2: 为每个用户创建默认收藏夹
INSERT INTO collections (user_id, name, type, is_system, is_public)
SELECT DISTINCT user_id, '默认电影收藏夹', 'MOVIE', TRUE, TRUE
FROM favorites;

INSERT INTO collections (user_id, name, type, is_system, is_public)
SELECT DISTINCT user_id, '默认活动收藏夹', 'EVENT', TRUE, TRUE
FROM users
WHERE id IN (SELECT DISTINCT user_id FROM favorites);

-- 步骤2.3: 将现有收藏数据迁移到默认电影收藏夹
UPDATE favorites f
INNER JOIN collections c ON f.user_id = c.user_id AND c.name = '默认电影收藏夹'
SET f.collection_id = c.id,
    f.item_id = f.movie_id,
    f.item_type = 'MOVIE';

-- 步骤2.4: 更新收藏夹的item_count
UPDATE collections c
SET c.item_count = (
    SELECT COUNT(*) FROM favorites f WHERE f.collection_id = c.id
);

-- 步骤2.5: 添加外键约束
ALTER TABLE favorites
ADD CONSTRAINT fk_favorites_collection FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE;

-- 步骤2.6: 删除旧的唯一约束，添加新的唯一约束
ALTER TABLE favorites DROP INDEX uk_user_movie;
ALTER TABLE favorites ADD UNIQUE KEY uk_collection_item (collection_id, item_type, item_id);

-- 步骤2.7: 添加新索引
CREATE INDEX idx_collection_id ON favorites(collection_id);
CREATE INDEX idx_item ON favorites(item_type, item_id);

-- 步骤2.8: movie_id字段改为可空（因为现在用item_id）
ALTER TABLE favorites MODIFY COLUMN movie_id BIGINT DEFAULT NULL COMMENT '电影ID（已废弃，使用item_id）';

-- 步骤2.9: 为所有现有用户创建默认收藏夹（如果还没有）
INSERT INTO collections (user_id, name, type, is_system, is_public)
SELECT id, '默认电影收藏夹', 'MOVIE', TRUE, TRUE
FROM users
WHERE NOT EXISTS (
    SELECT 1 FROM collections c 
    WHERE c.user_id = users.id AND c.type = 'MOVIE' AND c.is_system = TRUE
);

INSERT INTO collections (user_id, name, type, is_system, is_public)
SELECT id, '默认活动收藏夹', 'EVENT', TRUE, TRUE
FROM users
WHERE NOT EXISTS (
    SELECT 1 FROM collections c 
    WHERE c.user_id = users.id AND c.type = 'EVENT' AND c.is_system = TRUE
);
```

---

## 看过记录表（watched_movies）

### 创建表

```sql
-- 创建看过记录表
CREATE TABLE IF NOT EXISTS watched_movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '看过记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    movie_id BIGINT NOT NULL COMMENT '电影ID',
    watched_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '标记看过时间',
    rating DECIMAL(2,1) DEFAULT NULL COMMENT '用户评分(0.0-10.0)',
    note TEXT COMMENT '观影笔记',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_movie (user_id, movie_id) COMMENT '防止重复标记',
    INDEX idx_user_id (user_id),
    INDEX idx_movie_id (movie_id),
    INDEX idx_watched_at (watched_at),
    CONSTRAINT fk_watched_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_watched_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='看过记录表';
```

**执行时间**: 2026-03-01  
**说明**: 独立存储用户看过的电影记录，支持评分和观影笔记，查询性能更优

---

## 活动表（events）

### 创建表

```sql
-- 创建活动表
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '活动图片',
    event_date DATETIME NOT NULL COMMENT '活动开始时间',
    registration_deadline DATETIME DEFAULT NULL COMMENT '报名截止时间',
    end_time DATETIME DEFAULT NULL COMMENT '活动结束时间',
    location VARCHAR(200) NOT NULL COMMENT '活动地点',
    participants INT NOT NULL DEFAULT 0 COMMENT '参与人数',
    max_participants INT NOT NULL COMMENT '最大人数',
    type VARCHAR(50) NOT NULL COMMENT '活动类型',
    description TEXT DEFAULT NULL COMMENT '活动描述',
    movie_id BIGINT DEFAULT NULL COMMENT '关联电影ID',
    creator_id BIGINT NOT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_event_date (event_date),
    INDEX idx_movie_id (movie_id),
    INDEX idx_creator_id (creator_id),
    INDEX idx_registration_deadline (registration_deadline),
    INDEX idx_end_time (end_time),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE SET NULL,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动表';
```

**执行时间**: 2026-02-27  
**说明**: 存储线下活动信息

### 如果表已存在，添加字段

```sql
-- 添加创建人字段（外键关联users表）
ALTER TABLE events 
ADD COLUMN creator_id BIGINT NOT NULL COMMENT '创建人ID' 
AFTER movie_id;

-- 添加报名截止时间字段
ALTER TABLE events 
ADD COLUMN registration_deadline DATETIME NULL COMMENT '报名截止时间' 
AFTER event_date;

-- 添加活动结束时间字段
ALTER TABLE events 
ADD COLUMN end_time DATETIME NULL COMMENT '活动结束时间' 
AFTER registration_deadline;

-- 添加外键约束
ALTER TABLE events 
ADD CONSTRAINT fk_events_creator 
FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE;

-- 添加索引以提高查询性能
CREATE INDEX idx_creator_id ON events(creator_id);
CREATE INDEX idx_registration_deadline ON events(registration_deadline);
CREATE INDEX idx_end_time ON events(end_time);
```

**执行时间**: 2026-03-05  
**说明**: 
- `creator_id`: 活动创建人，用于区分"我创建的"和"我参与的"活动
- `registration_deadline`: 报名截止时间，过期后不能再参加活动
- `end_time`: 活动结束时间，用于筛选历史活动

**注意事项**:
1. 如果events表中已有数据，需要先为现有数据设置creator_id
2. 可以使用以下语句将现有活动的创建人设置为某个默认用户：
   ```sql
   UPDATE events SET creator_id = 1 WHERE creator_id IS NULL;
   ```
3. 建议在执行前备份数据库

---

## 活动参与表（event_participants）

### 创建表

```sql
-- 创建活动参与表
CREATE TABLE IF NOT EXISTS event_participants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '参与ID',
    event_id BIGINT NOT NULL COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    joined_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '参与时间',
    UNIQUE KEY uk_event_user (event_id, user_id) COMMENT '活动-用户唯一索引',
    INDEX idx_user_id (user_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动参与表';
```

**执行时间**: 2026-02-27  
**说明**: 存储用户参与活动的记录，防止重复参与

---

## 测试数据

### 插入测试用户

```sql
-- 插入测试用户（密码为：123456，已BCrypt加密）
INSERT INTO users (username, phone, password, avatar) VALUES
('测试用户1', '13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'https://images.unsplash.com/photo-1763536529823-953ff472bf35?w=400'),
('测试用户2', '13800138002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'https://images.unsplash.com/photo-1569913486515-b74bf7751574?w=400'),
('测试用户3', '13800138003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'https://images.unsplash.com/photo-1563481911853-c14860cd6947?w=400');
```

**执行时间**: 2026-02-27  
**说明**: 创建3个测试用户，密码统一为 `123456`

### 查询测试

```sql
-- 查询所有用户
SELECT id, username, phone, avatar, created_at FROM users;

-- 查询指定用户
SELECT * FROM users WHERE phone = '13800138001';

-- 统计用户数量
SELECT COUNT(*) as total FROM users;
```

---

## 常用查询语句

### 用户相关

```sql
-- 根据手机号查询用户
SELECT * FROM users WHERE phone = '13800138001';

-- 根据用户名查询用户
SELECT * FROM users WHERE username = '测试用户1';

-- 检查用户名是否存在
SELECT EXISTS(SELECT 1 FROM users WHERE username = '测试用户1') as exists_flag;

-- 检查手机号是否存在
SELECT EXISTS(SELECT 1 FROM users WHERE phone = '13800138001') as exists_flag;
```

### 动态相关

```sql
-- 查询所有动态（按时间倒序）
SELECT f.*, u.username, u.avatar 
FROM feeds f 
LEFT JOIN users u ON f.user_id = u.id 
ORDER BY f.created_at DESC;

-- 查询指定用户的动态
SELECT * FROM feeds WHERE user_id = 1 ORDER BY created_at DESC;

-- 统计用户动态数量
SELECT user_id, COUNT(*) as feed_count 
FROM feeds 
GROUP BY user_id;
```

### 清空表数据

```sql
-- 清空所有表数据（注意：会删除所有数据！）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE event_participants;
TRUNCATE TABLE events;
TRUNCATE TABLE watched_movies;
TRUNCATE TABLE favorites;
TRUNCATE TABLE collections;
TRUNCATE TABLE follows;
TRUNCATE TABLE likes;
TRUNCATE TABLE comments;
TRUNCATE TABLE feeds;
TRUNCATE TABLE movies;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
```

---

## 数据库维护

### 查看所有表

```sql
-- 查看当前数据库的所有表
SHOW TABLES;

-- 查看表的详细信息
SHOW TABLE STATUS;
```

### 查看表结构

```sql
-- 查看表结构
DESC users;
DESC movies;
DESC feeds;
DESC comments;
DESC likes;
DESC collections;
DESC favorites;
DESC watched_movies;
DESC events;
DESC event_participants;
DESC follows;
```

### 备份数据库

```bash
# 在命令行执行（不是在Navicat中）
mysqldump -u root -p final > final_backup_20260227.sql
```

### 恢复数据库

```bash
# 在命令行执行
mysql -u root -p final < final_backup_20260227.sql
```

---

## 更新日志

### 2026-02-27
- ✅ 创建数据库 `final`
- ✅ 创建用户表 `users`
- ✅ 创建电影表 `movies`（删除知乎、IMDB、烂番茄链接）
- ✅ 创建动态表 `feeds`（添加event_id支持关联活动）
- ✅ 创建评论表 `comments`
- ✅ 创建点赞表 `likes`
- ✅ 创建收藏表 `favorites`
- ✅ 创建活动表 `events`
- ✅ 创建活动参与表 `event_participants`
- ✅ 插入测试用户数据
- ✅ 修正表关联关系

---

**文档维护**: 本文档记录所有数据库操作SQL语句，每次数据库变更都会更新

# 数据库SQL语句记录

> **数据库名称**: final  
> **数据库类型**: MySQL 8.0+  
> **字符集**: utf8mb4  
> **排序规则**: utf8mb4_0900_ai_ci  
> **最后更新**: 2026-02-28

---

## 数据库创建

```sql
-- 创建数据库（如果已存在则跳过）
CREATE DATABASE IF NOT EXISTS final 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_0900_ai_ci;

-- 使用数据库
USE final;
```

---

## 用户表（users）

### 创建表

```sql
-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    user_code VARCHAR(4) NOT NULL UNIQUE COMMENT '用户唯一标识（0001-9999）',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    phone VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    avatar VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    bio TEXT DEFAULT NULL COMMENT '个人简介',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_code (user_code),
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
```

### 如果表已存在，添加字段

#### 添加user_code字段

```sql
-- 步骤1：添加字段（允许NULL）
ALTER TABLE users ADD COLUMN user_code VARCHAR(4) DEFAULT NULL COMMENT '用户唯一标识' AFTER id;

-- 步骤2：为现有用户生成编码（按注册时间顺序）
SET @row_number = 0;
UPDATE users 
SET user_code = LPAD((@row_number := @row_number + 1), 4, '0')
ORDER BY created_at ASC;

-- 步骤3：设置为NOT NULL并添加唯一约束
ALTER TABLE users MODIFY COLUMN user_code VARCHAR(4) NOT NULL;
ALTER TABLE users ADD UNIQUE KEY uk_user_code (user_code);
CREATE INDEX idx_user_code ON users(user_code);
```

#### 添加bio字段（个人简介）

```sql
-- 为users表添加个人简介字段
ALTER TABLE users 
ADD COLUMN bio TEXT DEFAULT NULL COMMENT '个人简介' 
AFTER avatar;
```

---

---

## 关注关系表（follows）

### 创建表

```sql
-- 创建关注关系表
CREATE TABLE IF NOT EXISTS follows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关注关系ID',
    follower_id BIGINT NOT NULL COMMENT '关注者ID（谁关注）',
    following_id BIGINT NOT NULL COMMENT '被关注者ID（关注谁）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    UNIQUE KEY uk_follower_following (follower_id, following_id) COMMENT '防止重复关注',
    INDEX idx_follower (follower_id) COMMENT '查询我关注的人',
    INDEX idx_following (following_id) COMMENT '查询关注我的人',
    CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_follows_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注关系表';
```

**执行时间**: 2026-02-28  
**说明**: 存储用户之间的关注关系，支持关注、粉丝、好友功能

### 常用查询

```sql
-- 查询用户1关注的所有人
SELECT u.* 
FROM users u
INNER JOIN follows f ON u.id = f.following_id
WHERE f.follower_id = 1
ORDER BY f.created_at DESC;

-- 查询关注用户1的所有人（粉丝）
SELECT u.* 
FROM users u
INNER JOIN follows f ON u.id = f.follower_id
WHERE f.following_id = 1
ORDER BY f.created_at DESC;

-- 查询用户1的好友（互相关注）
SELECT u.* 
FROM users u
INNER JOIN follows f1 ON u.id = f1.following_id AND f1.follower_id = 1
INNER JOIN follows f2 ON u.id = f2.follower_id AND f2.following_id = 1;

-- 统计用户1的关注数
SELECT COUNT(*) AS following_count FROM follows WHERE follower_id = 1;

-- 统计用户1的粉丝数
SELECT COUNT(*) AS follower_count FROM follows WHERE following_id = 1;

-- 统计用户1的好友数
SELECT COUNT(*) AS friend_count
FROM follows f1
WHERE f1.follower_id = 1
AND EXISTS (
    SELECT 1 FROM follows f2 
    WHERE f2.follower_id = f1.following_id 
    AND f2.following_id = 1
);
```

---

## 更新日志

### 2026-03-05
- ✅ 活动表 `events` 添加创建人字段 `creator_id`（外键关联users表）
- ✅ 活动表 `events` 添加报名截止时间字段 `registration_deadline`
- ✅ 活动表 `events` 添加活动结束时间字段 `end_time`
- ✅ 添加相关索引和外键约束
- ✅ 支持区分"我创建的活动"和"我参与的活动"
- ✅ 支持报名截止时间控制

### 2026-03-01
- ✅ 创建收藏夹表 `collections`（支持片单、混合收藏夹、看过系统收藏夹）
- ✅ 改造收藏项表 `favorites`（支持电影和活动收藏，关联收藏夹）
- ✅ 创建看过记录表 `watched_movies`（独立存储看过记录，支持评分和笔记）
- ✅ 提供现有数据平滑迁移方案

### 2026-02-28
- ✅ 添加user_code字段（用户唯一标识）
- ✅ 添加user_code唯一约束和索引
- ✅ 添加bio字段（个人简介）
- ✅ 创建关注关系表 `follows`
- ✅ 提供现有数据迁移方案

### 2026-02-27
- ✅ 创建数据库 `final`
- ✅ 创建用户表 `users`
- ✅ 创建电影表 `movies`
- ✅ 创建动态表 `feeds`
- ✅ 创建评论表 `comments`
- ✅ 创建点赞表 `likes`
- ✅ 创建收藏表 `favorites`
- ✅ 创建活动表 `events`
- ✅ 创建活动参与表 `event_participants`

---

**文档维护**: 本文档记录所有数据库操作SQL语句

