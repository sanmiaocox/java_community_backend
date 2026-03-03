# TMDB接口500错误排查指南

> **创建时间**: 2026-03-04  
> **问题**: 前端访问后端TMDB接口返回500错误

---

## 📋 问题分析

### 已确认的信息

✅ **TMDB API本身正常** - 直接访问TMDB API可以正常返回数据：
```bash
curl --request GET \
     --url 'https://api.themoviedb.org/3/movie/popular?language=zh-CN&page=1' \
     --header 'Authorization: Bearer eyJh...' \
     --header 'accept: application/json'
```

✅ **TMDB接口都是官方标准接口** - 项目中使用的所有接口都存在且正确

❌ **后端访问TMDB时出现问题** - 500错误说明后端代码执行时抛出异常

---

## 🔍 可能的原因

### 1. 网络连接问题（最可能）

**症状**: 后端服务器无法访问 `https://api.themoviedb.org`

**原因**:
- Java应用的网络环境与你本地curl命令的网络环境不同
- 如果你使用了代理（如Clash、V2Ray等），curl可能走了系统代理，但Java应用没有配置代理
- 防火墙阻止了Java应用的外网访问

**解决方案**:

#### 方案A: 配置Java应用使用代理

如果你的环境需要代理才能访问国外网站，修改 `RestTemplateConfig.java`：

```java
@Bean
public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(10000);
    factory.setReadTimeout(30000);
    
    // 配置代理（根据你的代理软件端口修改）
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
    factory.setProxy(proxy);
    
    return new RestTemplate(factory);
}
```

常见代理端口：
- Clash: 7890
- V2Ray: 10809
- Shadowsocks: 1080

#### 方案B: 启动时配置JVM代理参数

在启动Spring Boot应用时添加参数：
```bash
java -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7890 \
     -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890 \
     -jar your-app.jar
```

或在IDE中配置VM Options：
```
-Dhttp.proxyHost=127.0.0.1
-Dhttp.proxyPort=7890
-Dhttps.proxyHost=127.0.0.1
-Dhttps.proxyPort=7890
```

---

### 2. RestTemplate Bean冲突

**症状**: 应用启动失败或注入失败

**原因**: 
- 之前的代码中 `TmdbService` 直接 `new RestTemplate()`
- 现在改为注入，但可能存在Bean冲突

**解决方案**: 已通过创建 `RestTemplateConfig` 解决

---

### 3. 依赖缺失

**症状**: 编译错误或运行时ClassNotFoundException

**检查**: 确保 `pom.xml` 中有以下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## 🛠️ 已实施的修复

### 1. 创建RestTemplateConfig配置类

**文件**: `src/main/java/com/community/java_community_backend/config/RestTemplateConfig.java`

**作用**:
- 统一管理RestTemplate Bean
- 配置超时时间（连接超时10秒，读取超时30秒）
- 预留代理配置接口

### 2. 重构TmdbService

**改进**:
- 使用依赖注入的RestTemplate（而非直接new）
- 添加详细的错误日志分类：
  - `HttpClientErrorException`: 4xx客户端错误
  - `HttpServerErrorException`: 5xx服务器错误
  - `ResourceAccessException`: 网络连接错误
  - 其他异常
- 统一请求处理逻辑到 `executeRequest()` 方法
- 每次请求都会记录详细日志

### 3. 增强日志输出

现在每次请求都会输出：
```
INFO  - 开始获取热门电影，请求URL: https://api.themoviedb.org/3/movie/popular?language=zh-CN&page=1
INFO  - 获取热门电影成功，状态码: 200 OK
```

或者错误时：
```
ERROR - 获取热门电影失败 - 网络连接错误: 无法访问TMDB服务器，请检查网络连接或代理设置
```

---

## 📝 排查步骤

### 第1步: 重新编译并启动后端

```bash
cd "e:/final project/code/java_community_backend"
mvn clean package
mvn spring-boot:run
```

或在IDE中重新运行应用。

### 第2步: 查看启动日志

确认应用正常启动，没有Bean注入错误。

### 第3步: 测试后端接口

使用Postman或curl测试：
```bash
curl http://localhost:7070/api/tmdb/popular?page=1
```

### 第4步: 查看详细错误日志

在控制台中查找以下关键信息：
- `开始获取热门电影，请求URL: ...` - 确认请求发起
- `获取热门电影成功` - 确认请求成功
- `网络连接错误` - 说明无法访问TMDB
- `客户端错误` - 说明请求参数或认证有问题
- `TMDB服务器错误` - 说明TMDB服务端问题

### 第5步: 根据错误类型处理

#### 如果看到 "网络连接错误"
→ 需要配置代理（见上面的方案A或方案B）

#### 如果看到 "客户端错误: 状态码=401"
→ Token失效，需要重新生成

#### 如果看到 "客户端错误: 状态码=404"
→ 请求URL错误（但这不太可能，因为我们的URL都是正确的）

#### 如果没有任何日志
→ 请求根本没到达Service层，检查Controller和路由配置

---

## 🧪 测试代理配置是否生效

### 方法1: 在代码中添加测试日志

在 `RestTemplateConfig.java` 中：
```java
@Bean
public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(10000);
    factory.setReadTimeout(30000);
    
    // 测试：打印代理配置
    System.out.println("=== RestTemplate配置 ===");
    System.out.println("连接超时: 10秒");
    System.out.println("读取超时: 30秒");
    
    // 如果配置了代理
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
    factory.setProxy(proxy);
    System.out.println("代理: 127.0.0.1:7890");
    
    return new RestTemplate(factory);
}
```

### 方法2: 检查系统代理设置

在Windows PowerShell中：
```powershell
# 查看系统代理设置
netsh winhttp show proxy
```

---

## 📊 后端接口列表

| 接口路径 | 说明 | TMDB API |
|---------|------|----------|
| `GET /api/tmdb/popular?page=1` | 热门电影 | `/movie/popular` |
| `GET /api/tmdb/now-playing?page=1` | 正在上映 | `/movie/now_playing` |
| `GET /api/tmdb/upcoming?page=1` | 即将上映 | `/movie/upcoming` |
| `GET /api/tmdb/top-rated?page=1` | 高分电影 | `/movie/top_rated` |
| `GET /api/tmdb/movie/{id}` | 电影详情 | `/movie/{id}` |
| `GET /api/tmdb/movie/{id}/credits` | 演职人员 | `/movie/{id}/credits` |
| `GET /api/tmdb/movie/{id}/images` | 电影图片 | `/movie/{id}/images` |
| `GET /api/tmdb/search?keyword=xxx&page=1` | 搜索电影 | `/search/movie` |

---

## ✅ 预期结果

修复后，访问 `http://localhost:7070/api/tmdb/popular?page=1` 应该返回：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "page": 1,
    "results": [
      {
        "id": 1290821,
        "title": "庇护之地",
        "overview": "...",
        "poster_path": "/buWK1jAS0lrpzGQuqDWl3GHVJdt.jpg",
        ...
      }
    ],
    "total_pages": 55461,
    "total_results": 1109217
  }
}
```

---

## 🔧 快速修复建议

**最可能的问题是代理配置**，建议：

1. 确认你的代理软件（Clash/V2Ray等）正在运行
2. 查看代理软件的HTTP端口（通常是7890或10809）
3. 修改 `RestTemplateConfig.java`，取消代理配置的注释
4. 重启后端应用
5. 测试接口

如果还是不行，请提供完整的错误日志，我会进一步分析。

---

## 📞 需要提供的调试信息

如果问题仍未解决，请提供：

1. **完整的后端启动日志**（从启动到出错的全部日志）
2. **访问接口时的错误日志**（包括堆栈跟踪）
3. **你的网络环境**：
   - 是否使用代理？
   - 代理软件是什么？
   - 代理端口是多少？
4. **测试结果**：
   - curl直接访问TMDB: ✅ 成功
   - 后端访问TMDB: ❌ 失败（500错误）
   - 错误日志中的具体异常类型

---

**总结**: 99%的可能性是网络/代理配置问题，配置好代理后应该就能解决。

