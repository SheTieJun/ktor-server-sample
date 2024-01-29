# Server

[ktor-cn](https://ktor.kotlincn.net/)
[ktor.io](https://ktor.io/docs/welcome.html)


## 已实现
- JWT:获取token,验证token，刷新token
- Mysql:连接数据库，基础功能实现
- Redis:连接redis，基础功能实现
- Websocket:基础功能实现
- HTML:静态资源、网页请求，基础功能实现
- API:标准格式统一返回，保存Exception统一异常处理
- Log:logback日志记录
- Docker:打包到docker
- [rate Limit](https://ktor.io/docs/rate-limit.html):一定时间限制内api限制访问次数


## 获取客户端ip
```kotlin
  val ip = call.request.origin.remoteHost // 获取客户端 IP
```
```kotlin
call.request.uri
//路径参数
get("/user/{login}") {
    if (call.parameters["login"] == "admin") {
        // ...
    }
}
//查询参数/products?price=asc
get("/products") {
    if (call.request.queryParameters["price"] == "asc") {
        // Show products from the lowest price to the highest
    }
}
//表单参数 
post("/signup") {
    val formParameters = call.receiveParameters()
    val username = formParameters["username"].toString()
    call.respondText("The '$username' account is created")
}
//多部分表单数据
post("/upload") {
    val multipartData = call.receiveMultipart()

    multipartData.forEachPart { part ->
        when (part) {
            is PartData.FormItem -> {
                fileDescription = part.value
            }

            is PartData.FileItem -> {
                fileName = part.originalFileName as String
                val fileBytes = part.streamProvider().readBytes()
                File("uploads/$fileName").writeBytes(fileBytes)
            }

            else -> {}
        }
        part.dispose()
    }

    call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
}
```
## 打包以及运行到Docker

```groovy

buildscript {
    //用来打包jar
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath 'com.github.johnrengelman:shadow:8.1.1'
    }
}


```

```groovy
apply plugin: 'com.github.johnrengelman.shadow'
apply plugin: 'application'
application {
    mainClass.set("com.example.ApplicationKt")
}
shadowJar {
    archiveBaseName.set('my-application')
    archiveClassifier.set('')
    archiveVersion.set('')
}
```

Dockerfile
```dockerfile
FROM openjdk:17

RUN mkdir /app
EXPOSE 8080

COPY ./ktor-application.jar /app/ktor-application.jar
WORKDIR /app

CMD ["java", "-jar", "ktor-application.jar"]
```

## 构建并运行 Docker 镜像
1.构建应用包：assemble or build
> ./gradlew build

2.构建并标记镜像：
> docker build -t my-application .

3.启动镜像：
> docker run -m512M --cpus 2 -it -p 8080:8080 --rm my-application
