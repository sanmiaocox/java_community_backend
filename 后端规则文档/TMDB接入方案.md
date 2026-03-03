# TMDB接入后端方案

> **创建日期**: 2026-03-02  
> **状态**: 待审查

---

## 📋 目录

1. [现状分析](#现状分析)
2. [接入方案](#接入方案)
3. [需要创建的接口](#需要创建的接口)
4. [技术实现细节](#技术实现细节)
5. [数据流转设计](#数据流转设计)
6. [优缺点分析](#优缺点分析)

---

## 现状分析

### 已有的TMDB相关代码

1. **TmdbService.java** - 已实现
   - ✅ 获取热门电影 `getPopularMovies()`
   - ✅ 获取电影详情 `getMovieDetails()`
   - ✅ 搜索电影 `searchMovies()`
   - ✅ 获取演职人员 `getMovieCredits()`
   - ✅ 获取电影图片 `getMovieImages()`
   - ✅ 获取正在上映 `getNowPlayingMovies()`
   - ✅ 获取即将上映 `getUpcomingMovies()`
   - ✅ 获取高分电影 `getTopRatedMovies()`
   - ✅ 构建图片URL `getImageUrl()`

2. **TmdbController.java** - 已实现
   - ✅ GET `/api/tmdb/popular` - 热门电影
   - ✅ GET `/api/tmdb/movie/{movieId}` - 电影详情
   - ✅ GET `/api/tmdb/search` - 搜索电影
   - ✅ GET `/api/tmdb/movie/{movieId}/credits` - 演职人员
   - ✅ GET `/api/tmdb/now-playing` - 正在上映
   - ✅ GET `/api/tmdb/top-rated` - 高分电影

3. **Movie实体类** - 已存在
   - 本地数据库中的电影表
   - 用于存储用户发布动态时关联的电影

### 项目中的电影相关功能

根据API文档分析，项目中涉及电影的功能：

1. **动态系统**（待实现）
   - 发布动态时可以关联电影
   - 查看某电影的所有动态

2. **收藏系统**（已实现）
   - 收藏电影到收藏夹
   - 查看收藏的电影列表

3. **看过记录**（已实现）
   - 标记电影为看过
   - 查看看过的电影列表

4. **活动系统**（待实现）
   - 创建活动时可以关联电影

---

## 接入方案

### 方案概述

**核心思路**: 前端直接调用后端的TMDB接口获取电影数据，后端作为TMDB的代理层，同时在用户操作时将电影信息保存到本地数据库。

### 数据流转模式

```
前端 → 后端TMDB接口 → TMDB API → 返回电影数据 → 前端展示
                                    ↓
                            （用户操作时）保存到本地数据库
```

### 两种数据源

1. **TMDB数据源**（实时数据）
   - 电影搜索
   - 电影详情查询
   - 热门/高分/正在上映等榜单
   - 演职人员信息
   - 电影图片

2. **本地数据库**（用户数据）
   - 用户收藏的电影
   - 用户看过的电影
   - 用户发布动态关联的电影
   - 活动关联的电影

---

## 需要创建的接口

### 1. 电影搜索与浏览接口（新增）

#### MovieController.java

```java
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    
    private final TmdbService tmdbService;
    private final MovieService movieService;
    
    /**
     * 搜索电影（从TMDB）
     * GET /api/movies/search?keyword=星际穿越&page=1
     */
    @GetMapping("/search")
    public ApiResponse<JsonNode> searchMovies(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page) {
        // 直接返回TMDB数据
        JsonNode result = tmdbService.searchMovies(keyword, page);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("搜索失败");
    }
    
    /**
     * 获取热门电影（从TMDB）
     * GET /api/movies/popular?page=1
     */
    @GetMapping("/popular")
    public ApiResponse<JsonNode> getPopularMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getPopularMovies(page);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取正在上映（从TMDB）
     * GET /api/movies/now-playing?page=1
     */
    @GetMapping("/now-playing")
    public ApiResponse<JsonNode> getNowPlayingMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getNowPlayingMovies(page);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取即将上映（从TMDB）
     * GET /api/movies/upcoming?page=1
     */
    @GetMapping("/upcoming")
    public ApiResponse<JsonNode> getUpcomingMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getUpcomingMovies(page);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取高分电影（从TMDB）
     * GET /api/movies/top-rated?page=1
     */
    @GetMapping("/top-rated")
    public ApiResponse<JsonNode> getTopRatedMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getTopRatedMovies(page);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取电影详情（从TMDB）
     * GET /api/movies/{tmdbId}/details
     */
    @GetMapping("/{tmdbId}/details")
    public ApiResponse<JsonNode> getMovieDetails(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieDetails(tmdbId);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取电影演职人员（从TMDB）
     * GET /api/movies/{tmdbId}/credits
     */
    @GetMapping("/{tmdbId}/credits")
    public ApiResponse<JsonNode> getMovieCredits(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieCredits(tmdbId);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
    
    /**
     * 获取电影图片（从TMDB）
     * GET /api/movies/{tmdbId}/images
     */
    @GetMapping("/{tmdbId}/images")
    public ApiResponse<JsonNode> getMovieImages(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieImages(tmdbId);
        return result != null ? 
            ApiResponse.success(result) : 
            ApiResponse.error("获取失败");
    }
}
```

### 2. 本地电影数据接口（新增）

#### MovieService.java

```java
@Service
@RequiredArgsConstructor
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final TmdbService tmdbService;
    
    /**
     * 保存或更新电影到本地数据库
     * 当用户收藏、标记看过、发布动态时调用
     */
    @Transactional
    public Movie saveOrUpdateMovie(int tmdbId) {
        // 检查本地是否已存在
        Optional<Movie> existing = movieRepository.findByTmdbId(tmdbId);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // 从TMDB获取详情
        JsonNode tmdbData = tmdbService.getMovieDetails(tmdbId);
        if (tmdbData == null) {
            throw new RuntimeException("无法从TMDB获取电影信息");
        }
        
        // 转换并保存到本地
        Movie movie = convertTmdbToMovie(tmdbData);
        return movieRepository.save(movie);
    }
    
    /**
     * 将TMDB数据转换为本地Movie实体
     */
    private Movie convertTmdbToMovie(JsonNode tmdbData) {
        Movie movie = new Movie();
        movie.setTmdbId(tmdbData.get("id").asInt());
        movie.setTitle(tmdbData.get("title").asText());
        movie.setOriginalTitle(tmdbData.get("original_title").asText());
        movie.setPosterUrl(tmdbService.getImageUrl(
            tmdbData.get("poster_path").asText(), "w500"));
        movie.setRating(tmdbData.get("vote_average").asDouble());
        movie.setReleaseDate(tmdbData.get("release_date").asText());
        movie.setSynopsis(tmdbData.get("overview").asText());
        // ... 其他字段映射
        return movie;
    }
    
    /**
     * 获取本地电影列表（用户相关的电影）
     */
    public Page<Movie> getLocalMovies(int page, int size) {
        return movieRepository.findAll(PageRequest.of(page, size));
    }
    
    /**
     * 获取本地电影详情
     */
    public Movie getLocalMovie(Long movieId) {
        return movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("电影不存在"));
    }
}
```

### 3. 修改现有功能以集成TMDB

#### WatchedMovieService.java（修改）

```java
@Transactional
public WatchedMovieResponse markAsWatched(Long userId, MarkWatchedRequest request) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
    
    // 【新增】保存电影到本地数据库
    Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
    
    // 检查是否已标记
    if (watchedMovieRepository.existsByUserIdAndMovieId(userId, movie.getId())) {
        throw new RuntimeException("该电影已标记为看过");
    }
    
    // 创建看过记录
    WatchedMovie watchedMovie = new WatchedMovie();
    watchedMovie.setUser(user);
    watchedMovie.setMovie(movie);
    watchedMovie.setWatchedAt(LocalDateTime.now());
    watchedMovie.setRating(request.getRating());
    watchedMovie.setNote(request.getNote());
    
    WatchedMovie saved = watchedMovieRepository.save(watchedMovie);
    return convertToResponse(saved);
}
```

#### FavoriteService.java（修改）

```java
@Transactional
public FavoriteItemResponse addFavorite(Long userId, AddFavoriteRequest request) {
    // ... 验证逻辑 ...
    
    // 【新增】如果是电影类型，保存到本地数据库
    if (request.getItemType() == ItemType.MOVIE) {
        Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
        request.setItemId(movie.getId()); // 使用本地ID
    }
    
    // 创建收藏项
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setCollection(collection);
    favorite.setItemType(request.getItemType());
    favorite.setItemId(request.getItemId());
    favorite.setNote(request.getNote());
    
    Favorite saved = favoriteRepository.save(favorite);
    
    // 更新收藏夹计数
    collection.setItemCount(collection.getItemCount() + 1);
    collectionRepository.save(collection);
    
    return convertToResponse(saved);
}
```

---

## 技术实现细节

### 1. Movie实体类需要添加的字段

```java
@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 【新增】TMDB电影ID（用于关联TMDB数据）
    @Column(unique = true, nullable = false)
    private Integer tmdbId;
    
    // 现有字段...
    private String title;
    private String originalTitle;
    private String posterUrl;
    private Double rating;
    private String releaseDate;
    private String synopsis;
    // ...
}
```

### 2. MovieRepository需要添加的方法

```java
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    /**
     * 根据TMDB ID查询电影
     */
    Optional<Movie> findByTmdbId(Integer tmdbId);
    
    /**
     * 检查TMDB ID是否存在
     */
    boolean existsByTmdbId(Integer tmdbId);
}
```

### 3. DTO类需要修改

#### MarkWatchedRequest.java

```java
@Data
public class MarkWatchedRequest {
    
    @NotNull(message = "TMDB电影ID不能为空")
    private Integer tmdbId; // 改为tmdbId
    
    @DecimalMin(value = "0.0", message = "评分不能小于0.0")
    @DecimalMax(value = "10.0", message = "评分不能大于10.0")
    private Double rating;
    
    private String note;
}
```

#### AddFavoriteRequest.java

```java
@Data
public class AddFavoriteRequest {
    
    @NotNull(message = "收藏夹ID不能为空")
    private Long collectionId;
    
    @NotNull(message = "收藏项类型不能为空")
    private ItemType itemType;
    
    // 【修改】电影使用tmdbId，活动使用itemId
    private Integer tmdbId; // 电影的TMDB ID
    private Long itemId;    // 活动的本地ID
    
    private String note;
}
```

---

## 数据流转设计

### 场景1: 用户搜索电影

```
1. 前端: 输入关键词"星际穿越"
   ↓
2. 调用: GET /api/movies/search?keyword=星际穿越
   ↓
3. 后端: 调用TmdbService.searchMovies()
   ↓
4. TMDB: 返回搜索结果（包含tmdbId）
   ↓
5. 前端: 展示搜索结果列表
```

### 场景2: 用户查看电影详情

```
1. 前端: 点击电影（tmdbId=157336）
   ↓
2. 调用: GET /api/movies/157336/details
   ↓
3. 后端: 调用TmdbService.getMovieDetails(157336)
   ↓
4. TMDB: 返回电影详情
   ↓
5. 前端: 展示电影详情页
```

### 场景3: 用户标记电影为看过

```
1. 前端: 点击"看过"按钮（tmdbId=157336）
   ↓
2. 调用: POST /api/watched
   Body: { "tmdbId": 157336, "rating": 9.5 }
   ↓
3. 后端: MovieService.saveOrUpdateMovie(157336)
   ├─ 检查本地是否存在
   ├─ 不存在则从TMDB获取详情
   └─ 保存到本地数据库（生成本地ID）
   ↓
4. 后端: 创建watched_movies记录
   ↓
5. 前端: 显示"已标记为看过"
```

### 场景4: 用户查看看过的电影列表

```
1. 前端: 进入"看过"页面
   ↓
2. 调用: GET /api/watched
   ↓
3. 后端: 从watched_movies表查询
   ├─ 关联本地movies表
   └─ 返回电影信息（包含tmdbId和本地数据）
   ↓
4. 前端: 展示看过的电影列表
```

---

## 优缺点分析

### 方案优点 ✅

1. **数据实时性**
   - 电影搜索、详情、榜单等数据实时从TMDB获取
   - 保证数据的准确性和时效性

2. **减少存储压力**
   - 只保存用户操作过的电影到本地
   - 不需要同步整个TMDB数据库

3. **灵活性高**
   - 前端可以直接获取TMDB的完整数据
   - 后端只在必要时保存到本地

4. **易于维护**
   - TMDB数据更新自动生效
   - 不需要定时同步任务

5. **功能完整**
   - 支持搜索、榜单、详情、演职人员等丰富功能
   - 利用TMDB的强大能力

### 方案缺点 ❌

1. **依赖外部服务**
   - TMDB服务不可用时影响功能
   - 需要处理网络超时和错误

2. **API限流风险**
   - TMDB免费账户每秒40次请求
   - 高并发时可能触发限流

3. **响应速度**
   - 需要请求外部API，比本地查询慢
   - 建议添加缓存优化

4. **数据一致性**
   - 本地ID和TMDB ID需要同时维护
   - 需要处理ID映射关系

### 优化建议 💡

1. **添加Redis缓存**
   ```java
   @Cacheable(value = "tmdb:movie", key = "#tmdbId", ttl = 3600)
   public JsonNode getMovieDetails(int tmdbId) {
       // ...
   }
   ```

2. **请求失败重试**
   ```java
   @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
   public JsonNode getMovieDetails(int tmdbId) {
       // ...
   }
   ```

3. **降级策略**
   - TMDB不可用时，返回本地数据
   - 提示用户"数据可能不是最新"

4. **批量预加载**
   - 热门电影定时缓存
   - 减少实时请求

---

## 实施步骤

### 第一阶段：基础接入

1. ✅ TmdbService已实现
2. ✅ TmdbController已实现
3. ⏳ 创建MovieController（电影查询接口）
4. ⏳ 创建MovieService（本地电影管理）
5. ⏳ Movie实体添加tmdbId字段
6. ⏳ 数据库添加tmdbId字段和索引

### 第二阶段：功能集成

7. ⏳ 修改WatchedMovieService（集成TMDB）
8. ⏳ 修改FavoriteService（集成TMDB）
9. ⏳ 修改相关DTO类
10. ⏳ 更新API文档

### 第三阶段：优化增强

11. ⏳ 添加Redis缓存
12. ⏳ 添加请求重试机制
13. ⏳ 添加降级策略
14. ⏳ 性能测试和优化

---

## 需要审查的问题

### 1. 数据ID设计

**问题**: 前端应该使用TMDB ID还是本地ID？

**方案A**: 前端统一使用TMDB ID
- 优点: 前端逻辑简单，不需要区分
- 缺点: 后端需要做ID转换

**方案B**: 前端区分使用
- 优点: 后端逻辑清晰
- 缺点: 前端需要维护两种ID

**推荐**: 方案A（前端统一使用TMDB ID）

### 2. 缓存策略

**问题**: 是否需要立即实现缓存？

**建议**: 
- 第一阶段先不加缓存，验证功能
- 第二阶段根据实际情况添加缓存

### 3. 接口路径

**问题**: 电影接口应该放在 `/api/movies` 还是 `/api/tmdb`？

**建议**: 
- `/api/movies` - 对外统一的电影接口
- `/api/tmdb` - 保留作为测试接口

---

**请审查以上方案，确认后我将开始实施！**

