## 运行到docker

1. 首先需要安装docker
2. 构建应用包：ktor-server assemble or build
3. 构建并标记镜像：
   > docker build -t my-application .
4. 启动镜像：
   > docker run -p 8080:8080 my-application


## 当前文件夹,已经有打包好的jar
1. 构建并标记镜像：
   > docker build -t my-application . 
2. 启动镜像：
   > docker run -p 8080:8080 my-application