# TMDB接入实施总结

> **实施日期**: 2026-03-02  
> **状态**: 已完成代码修改

---

## ✅ 已完成的修改

### 1. 实体类修改

#### Movie.java
- ✅ 添加 `tmdbId` 字段（Integer类型，唯一索引）
- ✅ 用于关联TMDB电影数据

```java
@Column(unique = true)
private Integer tmdbId;
```

---

### 2. Repository层修改

#### MovieRepository.java
- ✅ 添加 `findByTmdbId(Integer tmdbId)` 方法
- ✅ 添加 `existsByTmdbId(Integer tmdbId)` 方法

---

### 3. Service层新增

#### MovieService.java（新建）
- ✅ `saveOrUpdateMovie(Integer tmdbId)` - 保存TMDB电影到本地
- ✅ `convertTmdbToMovie(JsonNode tmdbData)` - TMDB数据转换
- ✅ `getLocalMovieByTmdbId(Integer tmdbId)` - 查询本地电影
- ✅ `getLocalMovies(int page, int size)` - 分页查询
- ✅ `getLocalMovie(Long movieId)` - 根据ID查询
- ✅ `existsByTmdbId(Integer tmdbId)` - 检查是否存在

**功能说明**:
- 从TMDB获取电影详情并保存到本地数据库
- 自动转换TMDB数据格式为本地实体
- 避免重复保存（先检查本地是否存在）

---

### 4. Service层修改

#### TmdbService.java
- ✅ 修复import错误：`tools.jackson` → `com.fasterxml.jackson`
- ✅ 修复 `@Value` 注解错误：添加默认值

```java
@Value("${tmdb.api.base-url:https://api.themoviedb.org/3}")
private String baseUrl;
```

#### WatchedMovieService.java
- ✅ 注入 `MovieService`
- ✅ 修改 `markAsWatched()` 方法，集成TMDB
- ✅ 使用 `movieService.saveOrUpdateMovie(tmdbId)` 保存电影

**修改逻辑**:
```java
// 旧代码：直接使用movieId
Movie movie = movieRepository.findById(request.getMovieId())...

// 新代码：使用tmdbId，自动保存到本地
Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
```

#### FavoriteService.java
- ✅ 注入 `MovieService`
- ✅ 修改 `addFavorite()` 方法，集成TMDB
- ✅ 电影类型使用tmdbId，活动类型使用itemId

**修改逻辑**:
```java
if (request.getItemType() == ItemType.MOVIE) {
    Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
    request.setItemId(movie.getId()); // 使用本地ID
}
```

---

### 5. Controller层修改

#### TmdbController.java
- ✅ 修复import错误：`tools.jackson` → `com.fasterxml.jackson`
- ✅ 整合所有TMDB接口（8个接口）
- ✅ 统一接口路径为 `/api/tmdb`

**接口列表**:
1. `GET /api/tmdb/search` - 搜索电影
2. `GET /api/tmdb/popular` - 热门电影
3. `GET /api/tmdb/now-playing` - 正在上映
4. `GET /api/tmdb/upcoming` - 即将上映
5. `GET /api/tmdb/top-rated` - 高分电影
6. `GET /api/tmdb/movie/{tmdbId}` - 电影详情
7. `GET /api/tmdb/movie/{tmdbId}/credits` - 演职人员
8. `GET /api/tmdb/movie/{tmdbId}/images` - 电影图片

---

### 6. DTO层修改

#### MarkWatchedRequest.java
- ✅ 字段改名：`movieId` → `tmdbId`
- ✅ 类型改为 `Integer`

```java
@NotNull(message = "TMDB电影ID不能为空")
private Integer tmdbId;
```

#### AddFavoriteRequest.java
- ✅ 添加 `tmdbId` 字段（电影使用）
- ✅ 保留 `itemId` 字段（活动使用）
- ✅ 移除 `@NotNull` 注解（由Service层验证）

```java
private Integer tmdbId; // 电影的TMDB ID
private Long itemId;    // 活动的本地ID
```

---

### 7. 数据库SQL更新

#### 数据库SQL语句.md
- ✅ 添加movies表的tmdb_id字段说明
- ✅ 提供现有表的ALTER语句

```sql
-- 为现有movies表添加tmdb_id字段
ALTER TABLE movies 
ADD COLUMN tmdb_id INT UNIQUE COMMENT 'TMDB电影ID' 
AFTER id;

-- 添加索引
CREATE INDEX idx_tmdb_id ON movies(tmdb_id);
```

---

## 📊 数据流转设计

### 场景1: 用户搜索电影

```
前端 → GET /api/tmdb/search?keyword=星际穿越
     ↓
后端 → TmdbService.searchMovies()
     ↓
TMDB API → 返回搜索结果（包含tmdbId）
     ↓
前端 ← 展示搜索结果
```

### 场景2: 用户标记电影为看过

```
前端 → POST /api/watched { tmdbId: 157336 }
     ↓
后端 → MovieService.saveOrUpdateMovie(157336)
     ├─ 检查本地是否存在
     ├─ 不存在则从TMDB获取详情
     └─ 保存到本地数据库
     ↓
后端 → 创建watched_movies记录
     ↓
前端 ← 返回成功
```

### 场景3: 用户收藏电影

```
前端 → POST /api/favorites { collectionId: 1, itemType: "MOVIE", tmdbId: 157336 }
     ↓
后端 → MovieService.saveOrUpdateMovie(157336)
     ↓
后端 → 创建favorites记录（使用本地movie_id）
     ↓
前端 ← 返回成功
```

---

## 🔧 技术要点

### 1. 避免重复保存

```java
// MovieService中的逻辑
Optional<Movie> existing = movieRepository.findByTmdbId(tmdbId);
if (existing.isPresent()) {
    return existing.get(); // 直接返回已存在的记录
}
// 不存在才从TMDB获取并保存
```

### 2. 数据转换

```java
// 将TMDB的JSON数据转换为本地Movie实体
private Movie convertTmdbToMovie(JsonNode tmdbData) {
    Movie movie = new Movie();
    movie.setTmdbId(tmdbData.get("id").asInt());
    movie.setTitle(tmdbData.get("title").asText());
    // 海报URL需要拼接
    movie.setPosterUrl(tmdbService.getImageUrl(posterPath, "w500"));
    // ... 其他字段映射
    return movie;
}
```

### 3. 错误处理

```java
// TMDB API调用失败时抛出异常
if (tmdbData == null) {
    throw new RuntimeException("无法从TMDB获取电影信息: tmdbId=" + tmdbId);
}
```

---

## ⚠️ 需要注意的问题

### 1. 循环依赖风险

**问题**: MovieService依赖TmdbService，如果TmdbService也依赖MovieService会造成循环依赖

**解决**: 当前设计中TmdbService不依赖MovieService，只负责调用TMDB API

### 2. 并发保存问题

**问题**: 多个用户同时收藏同一部电影可能导致重复保存

**解决**: 
- tmdb_id字段设置为UNIQUE约束
- 数据库层面防止重复
- Service层先检查再保存

### 3. TMDB API失败处理

**问题**: TMDB API不可用时，用户无法收藏/标记看过

**当前处理**: 抛出异常，返回错误信息

**建议优化**: 
- 添加重试机制
- 添加降级策略（使用缓存数据）
- 允许用户手动输入电影信息

---

## 📝 待执行的数据库操作

### 如果是新建数据库

使用完整的CREATE TABLE语句（已包含tmdb_id字段）

### 如果是现有数据库

执行以下SQL：

```sql
-- 添加tmdb_id字段
ALTER TABLE movies 
ADD COLUMN tmdb_id INT UNIQUE COMMENT 'TMDB电影ID' 
AFTER id;

-- 添加索引
CREATE INDEX idx_tmdb_id ON movies(tmdb_id);
```

---

## 🐛 已修复的错误

### 1. Jackson包名错误

**错误**: `tools.jackson.databind.JsonNode`

**修复**: `com.fasterxml.jackson.databind.JsonNode`

**原因**: 错误的包名导入

**记录**: 已记录到项目规则文档

### 2. @Value注解错误

**错误**: `@Value("https://api.themoviedb.org/3")`

**修复**: `@Value("${tmdb.api.base-url:https://api.themoviedb.org/3}")`

**原因**: @Value注解应该使用${...}语法读取配置

**记录**: 已记录到项目规则文档

---

## 📚 相关文档更新

- ✅ `TMDB接入方案.md` - 接入方案设计
- ✅ `数据库SQL语句.md` - 添加tmdb_id字段说明
- ✅ `TMDB接入实施总结.md` - 本文档

---

## 🚀 下一步工作

### 1. 测试验证

- [ ] 启动应用，检查是否有编译错误
- [ ] 执行数据库迁移SQL
- [ ] 测试TMDB接口是否正常
- [ ] 测试收藏功能是否正常
- [ ] 测试看过功能是否正常

### 2. 功能优化

- [ ] 添加Redis缓存（热门电影、电影详情）
- [ ] 添加请求重试机制
- [ ] 添加降级策略
- [ ] 性能测试和优化

### 3. 文档完善

- [ ] 更新后端API文档
- [ ] 添加TMDB接口使用示例
- [ ] 前端对接说明

---

**实施完成时间**: 2026-03-02  
**修改文件数量**: 11个文件  
**新增文件数量**: 2个文件（MovieService.java, TMDB接入实施总结.md）

