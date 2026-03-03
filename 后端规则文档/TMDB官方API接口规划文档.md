# TMDB官方API接口规划文档

> **文档创建时间**: 2026-03-04  
> **TMDB API版本**: v3  
> **官方文档**: https://developer.themoviedb.org/reference/intro/getting-started  
> **API基础URL**: https://api.themoviedb.org/3

---

## 📌 重要说明

**当前项目使用的所有TMDB接口都是官方标准接口，理论上不应该出现404或500错误。**

如果前端访问后端返回500错误，可能的原因：
1. ❌ **网络问题** - 后端无法访问TMDB服务器
2. ❌ **API Token失效** - 需要重新生成
3. ❌ **请求格式错误** - 参数或请求头问题
4. ❌ **后端异常未捕获** - Java代码抛出异常

---

## ✅ TMDB官方API接口清单

### 1. 电影列表类接口

#### 1.1 获取热门电影
```
GET /movie/popular
```
**官方文档**: https://developer.themoviedb.org/reference/movie-popular-list

**参数**:
- `language` (可选): 语言代码，如 `zh-CN`
- `page` (可选): 页码，默认1
- `region` (可选): 地区代码

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getPopularMovies()
GET /movie/popular?language=zh-CN&page=1
```

**后端接口**: `GET /api/tmdb/popular?page=1`

---

#### 1.2 获取正在上映电影
```
GET /movie/now_playing
```
**官方文档**: https://developer.themoviedb.org/reference/movie-now-playing-list

**参数**:
- `language` (可选): 语言代码
- `page` (可选): 页码
- `region` (可选): 地区代码，如 `CN`

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getNowPlayingMovies()
GET /movie/now_playing?language=zh-CN&page=1&region=CN
```

**后端接口**: `GET /api/tmdb/now-playing?page=1`

---

#### 1.3 获取即将上映电影
```
GET /movie/upcoming
```
**官方文档**: https://developer.themoviedb.org/reference/movie-upcoming-list

**参数**:
- `language` (可选): 语言代码
- `page` (可选): 页码
- `region` (可选): 地区代码

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getUpcomingMovies()
GET /movie/upcoming?language=zh-CN&page=1&region=CN
```

**后端接口**: `GET /api/tmdb/upcoming?page=1`

---

#### 1.4 获取高分电影
```
GET /movie/top_rated
```
**官方文档**: https://developer.themoviedb.org/reference/movie-top-rated-list

**参数**:
- `language` (可选): 语言代码
- `page` (可选): 页码

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getTopRatedMovies()
GET /movie/top_rated?language=zh-CN&page=1
```

**后端接口**: `GET /api/tmdb/top-rated?page=1`

---

### 2. 电影详情类接口

#### 2.1 获取电影详情
```
GET /movie/{movie_id}
```
**官方文档**: https://developer.themoviedb.org/reference/movie-details

**参数**:
- `movie_id` (必填): 电影ID，路径参数
- `language` (可选): 语言代码

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getMovieDetails()
GET /movie/278?language=zh-CN
```

**后端接口**: `GET /api/tmdb/movie/{tmdbId}`

---

#### 2.2 获取电影演职人员
```
GET /movie/{movie_id}/credits
```
**官方文档**: https://developer.themoviedb.org/reference/movie-credits

**参数**:
- `movie_id` (必填): 电影ID
- `language` (可选): 语言代码

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getMovieCredits()
GET /movie/278/credits?language=zh-CN
```

**后端接口**: `GET /api/tmdb/movie/{tmdbId}/credits`

---

#### 2.3 获取电影图片
```
GET /movie/{movie_id}/images
```
**官方文档**: https://developer.themoviedb.org/reference/movie-images

**参数**:
- `movie_id` (必填): 电影ID
- `include_image_language` (可选): 图片语言

**项目中的实现**: ✅ 已实现
```java
// TmdbService.getMovieImages()
GET /movie/278/images
```

**后端接口**: `GET /api/tmdb/movie/{tmdbId}/images`

---

### 3. 搜索类接口

#### 3.1 搜索电影
```
GET /search/movie
```
**官方文档**: https://developer.themoviedb.org/reference/search-movie

**参数**:
- `query` (必填): 搜索关键词
- `language` (可选): 语言代码
- `page` (可选): 页码
- `include_adult` (可选): 是否包含成人内容

**项目中的实现**: ✅ 已实现
```java
// TmdbService.searchMovies()
GET /search/movie?query=星际穿越&language=zh-CN&page=1
```

**后端接口**: `GET /api/tmdb/search?keyword=星际穿越&page=1`

---

## 🔧 项目中未实现但可用的官方接口

### 4. 其他推荐接口

#### 4.1 获取相似电影
```
GET /movie/{movie_id}/similar--后边有时间再实现
```
**用途**: 根据电影ID获取相似电影推荐

**参数**:
- `movie_id` (必填): 电影ID
- `language` (可选): 语言代码
- `page` (可选): 页码

**建议实现**: 可用于"猜你喜欢"功能

---

#### 4.2 获取推荐电影--需要实现
```
GET /movie/{movie_id}/recommendations
```
**用途**: 根据电影ID获取推荐电影

**参数**:
- `movie_id` (必填): 电影ID
- `language` (可选): 语言代码
- `page` (可选): 页码

---

#### 4.3 获取电影视频--不需要
```
GET /movie/{movie_id}/videos
```
**用途**: 获取电影预告片、花絮等视频

**参数**:
- `movie_id` (必填): 电影ID
- `language` (可选): 语言代码

---

#### 4.4 获取电影评论--暂时并不需要
```
GET /movie/{movie_id}/reviews
```
**用途**: 获取TMDB用户对电影的评论

**参数**:
- `movie_id` (必填): 电影ID
- `language` (可选): 语言代码
- `page` (可选): 页码

---

#### 4.5 发现电影（高级筛选）--后边有时间再实现
```
GET /discover/movie
```
**用途**: 根据多种条件筛选电影

**参数**:
- `language` (可选): 语言代码
- `sort_by` (可选): 排序方式，如 `popularity.desc`
- `with_genres` (可选): 类型ID，如 `28,12`（动作+冒险）
- `primary_release_year` (可选): 上映年份
- `vote_average.gte` (可选): 最低评分
- `vote_average.lte` (可选): 最高评分

**建议实现**: 可用于高级筛选功能

---

#### 4.6 获取电影类型列表--后边有时间再实现
```
GET /genre/movie/list
```
**用途**: 获取所有电影类型（动作、喜剧等）

**参数**:
- `language` (可选): 语言代码

**建议实现**: 用于筛选器的类型选择

---

## 🖼️ 图片URL构建

TMDB返回的图片路径需要拼接完整URL：

### 图片基础URL
```
https://image.tmdb.org/t/p/{size}{file_path}
```

### 可用尺寸

**海报 (poster_path)**:
- `w92` - 92px宽
- `w154` - 154px宽
- `w185` - 185px宽
- `w342` - 342px宽
- `w500` - 500px宽 ⭐ 推荐
- `w780` - 780px宽
- `original` - 原始尺寸

**背景图 (backdrop_path)**:
- `w300` - 300px宽
- `w780` - 780px宽
- `w1280` - 1280px宽 ⭐ 推荐
- `original` - 原始尺寸

**示例**:
```
原始路径: /q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg
完整URL: https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg
```

---

## 🔐 认证方式

### Bearer Token认证（项目使用）
```
Authorization: Bearer {READ_ACCESS_TOKEN}
```

### API Key认证（备选）
```
?api_key={API_KEY}
```

**项目配置**:
```properties
tmdb.api.key=e4cec003c65f908828840d591a4e8311
tmdb.api.read-token=eyJhbGciOiJIUzI1NiJ9...
```

---

## ⚠️ 500错误排查指南

### 1. 检查后端日志
查看 `java_community_backend` 的控制台输出，找到具体的异常信息。

### 2. 测试TMDB连接
在后端服务器上测试是否能访问TMDB：
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
  "https://api.themoviedb.org/3/movie/popular?language=zh-CN&page=1"
```

### 3. 验证Token是否有效
访问TMDB官网重新生成Token：
https://www.themoviedb.org/settings/api

### 4. 检查网络代理
如果服务器在中国大陆，可能需要配置代理访问TMDB。

### 5. 查看具体错误
在 `TmdbService.java` 中，所有方法都有异常捕获：
```java
catch (Exception e) {
    log.error("获取热门电影失败", e);
    return null;
}
```

查看日志中的详细错误信息。

---

## 📊 后端接口映射表

| 后端接口 | TMDB接口 | 说明 |
|---------|---------|------|
| `GET /api/tmdb/popular` | `/movie/popular` | 热门电影 |
| `GET /api/tmdb/now-playing` | `/movie/now_playing` | 正在上映 |
| `GET /api/tmdb/upcoming` | `/movie/upcoming` | 即将上映 |
| `GET /api/tmdb/top-rated` | `/movie/top_rated` | 高分电影 |
| `GET /api/tmdb/movie/{id}` | `/movie/{id}` | 电影详情 |
| `GET /api/tmdb/movie/{id}/credits` | `/movie/{id}/credits` | 演职人员 |
| `GET /api/tmdb/movie/{id}/images` | `/movie/{id}/images` | 电影图片 |
| `GET /api/tmdb/search` | `/search/movie` | 搜索电影 |

---

## 🧪 测试建议

### 1. 使用Postman测试后端接口
```
GET http://localhost:7070/api/tmdb/popular?page=1
```

### 2. 查看返回的错误信息
如果返回500，查看响应体中的错误消息。

### 3. 直接测试TMDB接口
使用Postman直接调用TMDB接口，确认Token是否有效。

---

## 📝 总结

**结论**: 项目中使用的所有TMDB接口都是官方标准接口，不存在"接口不存在"的问题。

**500错误的真正原因可能是**:
1. 网络连接问题（无法访问TMDB服务器）
2. Token失效或配置错误
3. 后端代码异常（如JSON解析失败）
4. 防火墙或代理问题

**建议下一步**:
1. 查看后端完整的错误日志
2. 测试后端是否能访问 `https://api.themoviedb.org`
3. 验证Token是否有效
4. 检查前端调用的具体接口路径是否正确

