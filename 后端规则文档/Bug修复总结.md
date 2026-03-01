# Bug修复总结

> **修复日期**: 2026-03-02  
> **修复人**: AI Assistant

---

## 🐛 发现的Bug及修复

### Bug 1: Collection.java 使用了不存在的枚举值

**问题描述**:
```java
private CollectionType type = CollectionType.PLAYLIST;
```

`CollectionType` 枚举已经修改为只包含 `MOVIE` 和 `EVENT`，但 `Collection.java` 中还在使用旧的 `PLAYLIST` 值。

**错误信息**:
```
找不到符号
  符号:   变量 PLAYLIST
  位置: 类 com.community.java_community_backend.enums.CollectionType
```

**修复方案**:
```java
private CollectionType type = CollectionType.MOVIE;
```

**修复文件**: `Collection.java`

---

### Bug 2: JwtUtil 缺少 extractUserId 方法

**问题描述**:
所有新创建的 Controller（CollectionController、FavoriteController、WatchedMovieController）都调用了 `jwtUtil.extractUserId(token)` 方法，但 `JwtUtil` 类中只有 `getUserIdFromToken(token)` 方法。

**错误信息**:
```
找不到符号
  符号:   方法 extractUserId(java.lang.String)
  位置: 类型为com.community.java_community_backend.util.JwtUtil的变量 jwtUtil
```

**修复方案**:
在 `JwtUtil` 中添加兼容方法：
```java
/**
 * 从Token中提取用户ID（兼容方法）
 */
public Long extractUserId(String token) {
    return getUserIdFromToken(token);
}
```

**修复文件**: `JwtUtil.java`

---

### Bug 3: Repository 删除方法缺少必要注解

**问题描述**:
`FavoriteRepository` 和 `WatchedMovieRepository` 中的自定义删除方法缺少 `@Transactional` 和 `@Modifying` 注解，可能导致运行时错误。

**潜在问题**:
- 删除操作不在事务中执行
- JPA 不知道这是修改操作

**修复方案**:

**FavoriteRepository.java**:
```java
@Transactional
@Modifying
void deleteByCollectionIdAndItemTypeAndItemId(Long collectionId, ItemType itemType, Long itemId);
```

**WatchedMovieRepository.java**:
```java
@Transactional
@Modifying
void deleteByUserIdAndMovieId(Long userId, Long movieId);
```

**修复文件**: 
- `FavoriteRepository.java`
- `WatchedMovieRepository.java`

---

### Bug 4: 循环依赖风险

**问题描述**:
`AuthService` 依赖 `CollectionService`，而 `CollectionService` 依赖 `UserRepository`。虽然目前没有直接循环依赖，但为了避免未来可能的问题，使用 `@Lazy` 注解延迟加载。

**修复方案**:
将 `AuthService` 从使用 `@RequiredArgsConstructor` 改为手动构造函数，并对 `CollectionService` 使用 `@Lazy` 注解：

```java
@Service
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserCodeGeneratorService userCodeGeneratorService;
    private final CollectionService collectionService;
    
    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil,
                      UserCodeGeneratorService userCodeGeneratorService,
                      @Lazy CollectionService collectionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userCodeGeneratorService = userCodeGeneratorService;
        this.collectionService = collectionService;
    }
    
    // ... 其他方法
}
```

**修复文件**: `AuthService.java`

---

### Bug 5: DOUBLE类型字段使用错误 🔴

**问题描述**:
`WatchedMovie` 实体类中的 `rating` 字段使用了 `@Column(precision = 2, scale = 1)`，但MySQL的DOUBLE类型不支持precision和scale属性，导致应用启动失败。

**错误代码**:
```java
@Column(precision = 2, scale = 1)
private Double rating;
```

**错误信息**:
```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory'
Caused by: java.lang.IllegalArgumentException: scale has no meaning for SQL floating point types
```

**根本原因**:
- MySQL的DOUBLE类型是浮点数，不支持precision和scale属性
- precision和scale只适用于DECIMAL类型

**修复方案**:

**方案1（推荐）**: 直接使用DOUBLE类型
```java
@Column
private Double rating;
```

**方案2**: 改用DECIMAL类型（如果需要精确控制精度）
```java
@Column(precision = 3, scale = 1)
private BigDecimal rating;
```

**修复文件**: `WatchedMovie.java`

**重要性**: 🔴 高 - 导致应用无法启动

**预防措施**: 
- 已记录到项目规则文档的"数据库规范"章节
- 后续开发中使用DOUBLE类型时不添加precision和scale属性

---

## ✅ 验证结果

### 编译测试

```bash
cd "e:/final project/code/java_community_backend"
./mvnw clean compile
```

**结果**: ✅ BUILD SUCCESS

```
[INFO] Compiling 62 source files with javac [debug parameters release 17] to target\classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.053 s
```

---

## 📊 修复统计

| Bug类型 | 数量 | 严重程度 |
|---------|------|----------|
| 编译错误 | 2 | 🔴 高 |
| 运行时错误 | 1 | 🔴 高 |
| 运行时风险 | 2 | 🟡 中 |
| **合计** | **5** | - |

---

## 🔍 代码质量检查

### 已检查项目

- ✅ 所有 Java 文件编译通过
- ✅ 枚举值使用正确
- ✅ Repository 方法注解完整
- ✅ Service 层依赖注入正确
- ✅ Controller 层方法调用正确
- ✅ 无循环依赖问题

### 潜在改进建议

1. **异常处理**: 建议在 Service 层添加更详细的异常处理和日志记录
2. **参数校验**: 建议在 Controller 层添加更多的参数校验
3. **单元测试**: 建议为新增的 Service 和 Controller 添加单元测试
4. **集成测试**: 建议添加端到端的集成测试

---

## 📝 修复的文件列表

1. `src/main/java/com/community/java_community_backend/entity/Collection.java`
2. `src/main/java/com/community/java_community_backend/util/JwtUtil.java`
3. `src/main/java/com/community/java_community_backend/repository/FavoriteRepository.java`
4. `src/main/java/com/community/java_community_backend/repository/WatchedMovieRepository.java`
5. `src/main/java/com/community/java_community_backend/service/AuthService.java`
6. `src/main/java/com/community/java_community_backend/entity/WatchedMovie.java`

**总计**: 6个文件

---

## 🚀 下一步建议

1. **运行应用程序**: 启动 Spring Boot 应用，确保没有运行时错误
2. **测试API**: 使用 Postman 测试所有新增的 API 接口
3. **数据库迁移**: 执行数据库迁移脚本，创建新表和修改现有表
4. **功能测试**: 测试完整的用户注册、收藏、看过等功能流程

---

**文档维护**: 本文档记录了所有发现和修复的Bug

