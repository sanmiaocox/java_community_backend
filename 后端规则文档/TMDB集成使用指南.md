# TMDB API 集成使用指南

> **最后更新**: 2026-02-26  
> **TMDB API版本**: v3  
> **官方文档**: https://developers.themoviedb.org/3

---

## 📋 目录

1. [TMDB简介](#tmdb简介)
2. [API配置](#api配置)
3. [认证方式](#认证方式)
4. [常用接口](#常用接口)
5. [图片处理](#图片处理)
6. [使用示例](#使用示例)
7. [注意事项](#注意事项)

---

## TMDB简介

**The Movie Database (TMDB)** 是一个社区驱动的电影和电视节目数据库，提供免费的API接口。

### 主要功能
- 🎬 电影信息查询（标题、简介、评分等）
- 🎭 演职人员信息
- 🖼️ 高质量海报和剧照
- 🔍 强大的搜索功能
- 📊 热门、高分、正在上映等榜单
- 🌍 多语言支持（包括中文）

### 为什么选择TMDB？
- ✅ 免费使用
- ✅ 数据丰富且更新及时
- ✅ API文档完善
- ✅ 支持中文数据
- ✅ 无需信用卡注册

---

## API配置

### 1. 获取API密钥

1. 访问 [TMDB官网](https://www.themoviedb.org/)
2. 注册账号并登录
3. 进入 **Settings** → **API**
4. 申请API密钥（选择Developer类型）
5. 获得 **API Key** 和 **API Read Access Token**

### 2. 配置到项目

在 `application.properties` 中配置：

```properties
# TMDB API配置
tmdb.api.key=你的API_KEY
tmdb.api.read-token=你的READ_ACCESS_TOKEN
tmdb.api.base-url=https://api.themoviedb.org/3
tmdb.image.base-url=https://image.tmdb.org/t/p
```

### 3. 当前配置

项目已配置的密钥（仅供开发测试）：

```properties
tmdb.api.key=e4cec003c65f908828840d591a4e8311
tmdb.api.read-token=eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNGNlYzAwM2M2NWY5MDg4Mjg4NDBkNTkxYTRlODMxMSIsIm5iZiI6MTc3MTc3MDI2MS4zNDQsInN1YiI6IjY5OWIxMTk1NjFmOWIyNjg0ZjgwZWU5NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Nc2XDq2tsuR7L3aA6FllrRnOLyvn_vBaqzIk7eJ-xXk
```

⚠️ **生产环境请更换为自己的密钥！**

---

## 认证方式

TMDB API支持两种认证方式：

### 1. API Key认证（URL参数）

```
https://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY&language=zh-CN
```

### 2. Bearer Token认证（推荐）

在HTTP请求头中添加：

```
Authorization: Bearer YOUR_READ_ACCESS_TOKEN
```

**项目采用Bearer Token认证方式**，更安全且符合RESTful规范。

---

## 常用接口

### 1. 获取热门电影

**接口**: `GET /movie/popular`

**参数**:
- `language`: 语言（zh-CN为中文）
- `page`: 页码（默认1）
- `region`: 地区（可选）

**示例**:
```
GET https://api.themoviedb.org/3/movie/popular?language=zh-CN&page=1
```

**响应字段**:
```json
{
  "page": 1,
  "results": [
    {
      "id": 278,
      "title": "肖申克的救赎",
      "original_title": "The Shawshank Redemption",
      "overview": "电影简介...",
      "poster_path": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
      "backdrop_path": "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
      "vote_average": 8.7,
      "vote_count": 23000,
      "release_date": "1994-09-23",
      "genre_ids": [18, 80]
    }
  ],
  "total_pages": 500,
  "total_results": 10000
}
```

---

### 2. 获取电影详情

**接口**: `GET /movie/{movie_id}`

**参数**:
- `movie_id`: 电影ID（路径参数）
- `language`: 语言

**示例**:
```
GET https://api.themoviedb.org/3/movie/278?language=zh-CN
```

**响应字段**:
```json
{
  "id": 278,
  "title": "肖申克的救赎",
  "original_title": "The Shawshank Redemption",
  "overview": "电影简介...",
  "poster_path": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
  "backdrop_path": "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
  "vote_average": 8.7,
  "vote_count": 23000,
  "release_date": "1994-09-23",
  "runtime": 142,
  "budget": 25000000,
  "revenue": 28341469,
  "genres": [
    {"id": 18, "name": "剧情"},
    {"id": 80, "name": "犯罪"}
  ],
  "production_countries": [
    {"iso_3166_1": "US", "name": "United States of America"}
  ],
  "spoken_languages": [
    {"iso_639_1": "en", "name": "English"}
  ]
}
```

---

### 3. 搜索电影

**接口**: `GET /search/movie`

**参数**:
- `query`: 搜索关键词（必填）
- `language`: 语言
- `page`: 页码

**示例**:
```
GET https://api.themoviedb.org/3/search/movie?query=星际穿越&language=zh-CN&page=1
```

---

### 4. 获取演职人员

**接口**: `GET /movie/{movie_id}/credits`

**参数**:
- `movie_id`: 电影ID
- `language`: 语言

**示例**:
```
GET https://api.themoviedb.org/3/movie/278/credits?language=zh-CN
```

**响应字段**:
```json
{
  "id": 278,
  "cast": [
    {
      "id": 504,
      "name": "蒂姆·罗宾斯",
      "character": "Andy Dufresne",
      "profile_path": "/path.jpg",
      "order": 0
    }
  ],
  "crew": [
    {
      "id": 4027,
      "name": "弗兰克·德拉邦特",
      "job": "Director",
      "department": "Directing"
    }
  ]
}
```

---

### 5. 获取电影图片

**接口**: `GET /movie/{movie_id}/images`

**参数**:
- `movie_id`: 电影ID

**示例**:
```
GET https://api.themoviedb.org/3/movie/278/images
```

**响应字段**:
```json
{
  "id": 278,
  "backdrops": [
    {
      "file_path": "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
      "width": 1920,
      "height": 1080,
      "vote_average": 5.384
    }
  ],
  "posters": [
    {
      "file_path": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
      "width": 2000,
      "height": 3000,
      "vote_average": 5.318
    }
  ]
}
```

---

### 6. 获取正在上映

**接口**: `GET /movie/now_playing`

**参数**:
- `language`: 语言
- `page`: 页码
- `region`: 地区（CN为中国）

**示例**:
```
GET https://api.themoviedb.org/3/movie/now_playing?language=zh-CN&page=1&region=CN
```

---

### 7. 获取即将上映

**接口**: `GET /movie/upcoming`

**参数**:
- `language`: 语言
- `page`: 页码
- `region`: 地区

**示例**:
```
GET https://api.themoviedb.org/3/movie/upcoming?language=zh-CN&page=1&region=CN
```

---

### 8. 获取高分电影

**接口**: `GET /movie/top_rated`

**参数**:
- `language`: 语言
- `page`: 页码

**示例**:
```
GET https://api.themoviedb.org/3/movie/top_rated?language=zh-CN&page=1
```

---

## 图片处理

### 图片URL构建

TMDB返回的图片路径需要拼接完整URL：

```
完整URL = 基础URL + 尺寸 + 图片路径
```

**示例**:
```
https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg
```

### 可用尺寸

#### 海报（poster）
- `w92`: 92px宽
- `w154`: 154px宽
- `w185`: 185px宽
- `w342`: 342px宽
- `w500`: 500px宽（推荐）
- `w780`: 780px宽
- `original`: 原始尺寸

#### 背景图（backdrop）
- `w300`: 300px宽
- `w780`: 780px宽
- `w1280`: 1280px宽（推荐）
- `original`: 原始尺寸

#### 头像（profile）
- `w45`: 45px宽
- `w185`: 185px宽（推荐）
- `h632`: 632px高
- `original`: 原始尺寸

### 代码示例

```java
public String getImageUrl(String posterPath, String size) {
    if (posterPath == null || posterPath.isEmpty()) {
        return null;
    }
    return String.format("%s/%s%s", imageBaseUrl, size, posterPath);
}

// 使用示例
String posterUrl = getImageUrl("/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg", "w500");
// 结果: https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg
```

---

## 使用示例

### 在Service中调用

```java
@Service
@Slf4j
public class TmdbService {
    
    @Value("${tmdb.api.read-token}")
    private String readToken;
    
    @Value("${tmdb.api.base-url}")
    private String baseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 创建请求头
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + readToken);
        headers.set("accept", "application/json");
        return headers;
    }
    
    // 获取热门电影
    public JsonNode getPopularMovies(int page) {
        try {
            String url = String.format("%s/movie/popular?language=zh-CN&page=%d", 
                baseUrl, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取热门电影失败", e);
            return null;
        }
    }
}
```

### 在Controller中使用

```java
@RestController
@RequestMapping("/api/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    
    private final TmdbService tmdbService;
    
    @GetMapping("/popular")
    public ApiResponse<JsonNode> getPopularMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getPopularMovies(page);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取热门电影失败");
    }
}
```

### 前端调用示例（Flutter）

```dart
import 'package:http/http.dart' as http;
import 'dart:convert';

Future<List<Movie>> fetchPopularMovies() async {
  final response = await http.get(
    Uri.parse('http://localhost:7070/api/tmdb/popular?page=1'),
  );
  
  if (response.statusCode == 200) {
    final data = json.decode(response.body);
    final results = data['data']['results'] as List;
    return results.map((json) => Movie.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load movies');
  }
}
```

---

## 注意事项

### 1. API限流

TMDB API有请求频率限制：
- **免费账户**: 每秒40次请求
- **超出限制**: 返回429状态码

**建议**:
- 实现请求缓存
- 避免短时间内大量请求
- 使用Redis缓存热门数据

### 2. 语言设置

使用 `language=zh-CN` 获取中文数据，但注意：
- 部分电影可能没有中文翻译
- 可以同时请求英文数据作为备选

### 3. 图片加载优化

- 根据设备选择合适的图片尺寸
- 使用CDN加速（TMDB图片已在CDN上）
- 实现图片懒加载
- 添加占位图和加载失败处理

### 4. 错误处理

```java
try {
    JsonNode result = tmdbService.getMovieDetails(movieId);
    if (result == null) {
        return ApiResponse.error("电影不存在或网络错误");
    }
    return ApiResponse.success(result);
} catch (Exception e) {
    log.error("获取电影详情失败", e);
    return ApiResponse.error("服务器错误");
}
```

### 5. 数据缓存策略

建议缓存以下数据：
- ✅ 电影详情（缓存24小时）
- ✅ 热门榜单（缓存1小时）
- ✅ 搜索结果（缓存30分钟）
- ❌ 用户相关数据（不缓存）

### 6. 生产环境配置

生产环境部署时：
1. 更换为自己的API密钥
2. 配置环境变量，不要硬编码
3. 启用HTTPS
4. 实现请求重试机制
5. 添加监控和日志

---

## 常见问题

### Q1: 为什么有些电影没有中文信息？

**A**: TMDB是社区驱动的，部分电影可能还没有中文翻译。可以：
- 同时请求英文数据作为备选
- 贡献翻译到TMDB社区

### Q2: 图片加载很慢怎么办？

**A**: 
- 使用较小的图片尺寸（如w342代替w500）
- 实现图片懒加载
- 考虑使用图片CDN加速

### Q3: API请求失败怎么办？

**A**: 
- 检查网络连接
- 确认API密钥是否正确
- 查看是否超出请求限制
- 检查请求URL和参数格式

### Q4: 如何获取电影的中文海报？

**A**: 
- 使用 `/movie/{id}/images` 接口
- 筛选 `iso_639_1` 为 `zh` 的图片
- 如果没有中文海报，使用默认海报

---

## 参考资源

- 📚 [TMDB官方文档](https://developers.themoviedb.org/3)
- 🌐 [TMDB官网](https://www.themoviedb.org/)
- 💬 [TMDB论坛](https://www.themoviedb.org/talk)
- 📖 [API更新日志](https://www.themoviedb.org/talk/category/5047958519c29526b50017d6)

---

**文档维护**: 本文档将随项目开发自动更新

