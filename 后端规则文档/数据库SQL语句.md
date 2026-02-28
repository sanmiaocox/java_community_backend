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
    INDEX idx_year (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影表';
```

**执行时间**: 2026-02-27  
**说明**: 存储电影详细信息，只保留豆瓣和TMDB链接

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

## 收藏表（favorites）

### 创建表

```sql
-- 创建收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    movie_id BIGINT NOT NULL COMMENT '电影ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_movie (user_id, movie_id) COMMENT '用户-电影唯一索引',
    INDEX idx_movie_id (movie_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';
```

**执行时间**: 2026-02-27  
**说明**: 存储用户收藏的电影，防止重复收藏

---

## 活动表（events）

### 创建表

```sql
-- 创建活动表
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '活动图片',
    event_date DATETIME NOT NULL COMMENT '活动时间',
    location VARCHAR(200) NOT NULL COMMENT '活动地点',
    participants INT NOT NULL DEFAULT 0 COMMENT '参与人数',
    max_participants INT NOT NULL COMMENT '最大人数',
    type VARCHAR(50) NOT NULL COMMENT '活动类型',
    description TEXT DEFAULT NULL COMMENT '活动描述',
    movie_id BIGINT DEFAULT NULL COMMENT '关联电影ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_event_date (event_date),
    INDEX idx_movie_id (movie_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动表';
```

**执行时间**: 2026-02-27  
**说明**: 存储线下活动信息

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
TRUNCATE TABLE favorites;
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
DESC favorites;
DESC events;
DESC event_participants;
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

