# Server

[ktor-cn](https://ktor.kotlincn.net)
[ktor.io](https://ktor.io/docs/welcome.html)

## 开发环境：Windows

- Android Studio Hedgehog | 2023.1.1 Patch 1 (因为我是Android开发，习惯了相关配置)
- JDK : Java 17
- Gradle : 8.0
- Docker :Win DockerDesktop+MySql+Redis+(ktor-application)
-
Test：Postman,[测试案例](https://dev-jun.postman.co/workspace/Dev1~68c25709-3d9e-4260-897b-a9025e77c641/collection/17982394-68dc2b14-2a73-4d5a-88f1-107c928e0e75?action=share&creator=17982394)

[**MySQL安装**](https://www.runoob.com/mysql/mysql-install.html)

[**Redis安装**](https://www.runoob.com/docker/docker-install-redis.html)

## 已实现功能

- **Koin**:依赖注入
- **JWT**:获取token,验证token，刷新token
- **Mysql**:连接数据库，基础功能实现
- **Redis**:连接redis，基础功能实现
- **Websocket**:基础功能实现
- **HTML**:静态资源、网页请求，基础功能实现
- **API**:标准格式统一返回，错误code，Exception都统一返回异常，后续可以加入UA判断，web返回特定html,手机返回json
- **Log**:logback日志记录
- **Docker**:打包到docker ，[Alpine Linux](https://alpinelinux.org/)
- **[rate Limit](https://ktor.io/docs/rate-limit.html)**:一定时间限制内api*限制访问次数

## 主要的库：

- Ktor ：个轻量级、灵活且可扩展的 Kotlin Web 框架，适用于构建异步、非阻塞的 Web 应用程序和 API。
- Koin：依赖注入框架
- exposed+mysql：exposed一个用于 Kotlin 的轻量级 ORM（对象关系映射）框架，用于与关系型数据库进行交互。它是一个基于类型安全的 DSL（领域特定语言），使得数据库操作变得简单而直观。
- jedis:一个用于 Java 的流行的 Redis 客户端库，用于与 Redis 数据库进行交互。
- gson:一个用于在 Java 对象和 JSON 数据之间进行序列化和反序列化的开源库。

```groovy
def ktor_version = "2.3.7"
api platform("io.ktor:ktor-bom:$ktor_version")
def kotlin_version = "1.9.22"
def logback_version = "1.4.11"

implementation("io.ktor:ktor-server-auth-jvm") //权限判断
implementation("io.ktor:ktor-server-core-jvm")
implementation("io.ktor:ktor-server-auth-jwt-jvm") //jwt
implementation("io.ktor:ktor-server-websockets-jvm") //websocket
implementation("io.ktor:ktor-server-html-builder-jvm")//DSL构建html
implementation("io.ktor:ktor-server-content-negotiation-jvm")
implementation("io.ktor:ktor-serialization-gson-jvm") //json序列话
implementation("io.ktor:ktor-server-netty-jvm") //netty
implementation("io.ktor:ktor-server-status-pages") //错误页面
implementation("io.ktor:ktor-server-call-logging")//日志
implementation("io.ktor:ktor-server-swagger-jvm")//swagger
implementation("io.ktor:ktor-server-cors")//跨域
implementation("io.ktor:ktor-client-cio") //客户端，用来请求其他服务
implementation("io.ktor:ktor-server-rate-limit")//限流
implementation("ch.qos.logback:logback-classic:$logback_version")
testImplementation("io.ktor:ktor-server-tests-jvm")
testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

//数据库 https://github.com/JetBrains/Exposed
def exposed_version = "0.46.0"
def mysql_version = "8.0.33"
implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
implementation("mysql:mysql-connector-java:$mysql_version") //mysql jdbc

//依赖倒置
// https://mvnrepository.com/artifact/io.insert-koin/koin-ktor
implementation("io.insert-koin:koin-ktor:3.5.3") //依赖注入

// https://mvnrepository.com/artifact/redis.clients/jedis
implementation("redis.clients:jedis:5.1.0") //redis

```

## 遇到的问题

<aside>
💡 java.sql.SQLNonTransientConnectionException: Could not create connection to database server. Attempted reconnect 3 times. Giving up.
</aside>

导致的原因：我的docker是运行在WSL上的，Mysql和Redis都是，直接用127.0.0.1的时候本地调试没有问题，但是全部放到docker上的时候就有问题了

## 部分实现原理

### 1. 标准格式统一返回，保存错误code，Exception都统一返回异常

添加插件：[status pages](https://ktor.io/docs/status-pages.html)

```groovy
implementation("io.ktor:ktor-server-status-pages") //错误页面
```

代码实现如下

```kotlin
install(StatusPages) {
    HttpStatusCode.allStatusCodes.forEach {
        if (it == HttpStatusCode.OK
            || it == HttpStatusCode.TooManyRequests
            || it == HttpStatusCode.MethodNotAllowed
            || it == HttpStatusCode.NotFound
        ) {
            return@forEach
        }
        if (it.value in 400..599) { //只处理错误的状态码
            status(it) { call, _ ->
                call.jsonError(it.value, "${it.value}: ${it.description}")
            }
            return@forEach
        }
    }
}
```

案例：

```json
{
  "code": 404,
  "msg": "method /userinfoxx not found"
}
```

### 2. [JWT实现](doc/JWT%E5%AE%9E%E7%8E%B0.MD):相关文档[JSON Web Tokens](https://ktor.io/docs/3.0.0-beta-1/jwt.html)

### 3. 接口限制：[rate-limit](https://ktor.io/docs/rate-limit.html)

