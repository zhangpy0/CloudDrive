# 基于Springboot mybatis mysql的web网盘

## 项目来源

 - 来源于之前的基于Servlet的网盘项目，由于Servlet的局限性，所以使用Springboot重构了一下
 - 前端代码完全来源于之前的网盘项目

## 项目功能

### 已实现功能
 - 用户注册登录
 - 文件上传下载
 
### 以后再做🥰

 - 文件分享
 - 电子邮件注册

## 使用方法

### 直接运行
- 1.克隆项目到本地 `git clone https://github.com/zhangpy0/CloudDrive.git`
- 2.导入IDEA
- 3.数据库配置
    - 3.1.进入数据库`mysql -u yourusername -p` 
    - 3.2.在mysql里运行data文件夹中的sql文件 `source usertable.sql`
    - 3.3.修改application.yml中的数据库配置
        ```
        spring:
            datasource:
                driver-class-name: com.mysql.cj.jdbc.Driver
                url: jdbc:mysql://localhost:3306/CloudDrive
                username: yourusername
                password: yourpassword
        ```
- 4.运行项目 (项目目录下需要有file文件夹，用于存放上传的文件)
- 5.访问localhost:8080
 
### 打包jar
- 1.克隆项目到本地 `git clone https://github.com/zhangpy0/CloudDrive.git`
- 2.数据库配置
    - 2.1.进入数据库`mysql -u yourusername -p` 
    - 2.2.在mysql里运行data文件夹中的sql文件 `source usertable.sql`
    - 2.3.修改application.yml中的数据库配置
        ```
        spring:
            datasource:
                driver-class-name: com.mysql.cj.jdbc.Driver
                url: jdbc:mysql://localhost:3306/CloudDrive
                username: yourusername
                password: yourpassword
        ```
- 3.maven打包 `mvn clean package`
- 4.运行jar `java -jar ./target/CloudDrive-0.0.1-SNAPSHOT.jar` (jar包同级目录下需要有file文件夹，用于存放上传的文件)

## 感谢支持🥰🥰🥰
