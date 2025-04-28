# 网络音乐仓库

本项目在tomcat上搭建了一个音乐服务。

可以：
- 上传音乐。
- 查询已上传音乐。
- 获取已上传音乐的 url 和时长。

在开发者预想中，本服务是用于给 [NetMusic](https://github.com/TartaricAcid/NetMusic) 这个 Minecraft Mod 提供资源的。

由于该 mod 只貌似支持 .mp3 文件，所以开发者就没有提供其他文件格式的上传。

未来开发者可能会写一个 NetMusic 的附属 mod，直接接入该服务。~~// 在新建文件夹了（逃~~

## 下载

v1.0.0
- [Github](https://github.com/whitefood0201/web-music-repository/releases/download/v1.0.0/web-music-repository_release-v1.0.0.zip)
- [百度网盘](https://pan.baidu.com/s/1-5wVRxZb1iqcRtXyYh2GcQ) 提取码：mups
    - 注：可以直接通过浏览器下载

## 配置

在 `/WEB-INF/web.xml` 中。

### mp3 存放位置

mp3 文件的存放位置，相对于项目所在文件夹

```xml
<context-param>
    <param-name>staticPath</param-name>
    <param-value>/static</param-value>
</context-param>
```

### 索引方式

有两个选择：
1. txt
   - 使用 `dbFile` 中配置的文件存放数据。**默认为这个。**
   - 优点：方便，不需要使用额外应用。适合在 PC 上临时使用。
2. db
   - 使用数据库，需要一个数据库（开发时使用的是mysql，不清楚能否在其他数据库运行）。
   - 请见下面的[数据库配置](#数据库配置)

```xml
<context-param>
    <param-name>dao</param-name>
    <param-value>txt</param-value>
</context-param>
<context-param>
    <param-name>dbFile</param-name>
    <param-value>/db/musics.txt</param-value>
</context-param>
```

### 数据库配置

经典 jdbc 配置。不多言说了，网上到处多是。

```xml
<context-param>
    <param-name>url</param-name>
    <param-value>jdbc:mysql://localhost:3306/web?useUnicode=true&amp;characterEncoding=utf8</param-value>
</context-param>
<context-param>
    <param-name>driver</param-name>
    <param-value>com.mysql.cj.jdbc.Driver</param-value>
</context-param>
<context-param>
    <param-name>user</param-name>
    <param-value></param-value>
</context-param>
<context-param>
    <param-name>password</param-name>
    <param-value></param-value>
</context-param>
```

另附 sql 脚本，用于建表：

```sql
drop table if exists t_mups;

create table t_mups(
        mid int primary key auto_increment,
        mname varchar(255),
        duration int
);

select * from t_mups;
```

### 通过 web 删除文件及数据

`canDelete` 控制是否启用

访问 `<域名>/mup/action/delete?username=11&pwd=45&mid=14` 可以根据 mid 进行删除。

~~懒得写 html 了，感觉不用太上。主要是会开放给外部，存在安全问题。~~

**默认关闭。**

不建议开启，删什么直接上数据库或 txt 文件中删除，并手动删除文件。

`username`和`pwd`顾名思义，简单的验证，防止非本人操作。

**注意：数据全是明文的。**

```xml
<context-param>
    <param-name>canDelete</param-name>
    <param-value>false</param-value>
</context-param>
<context-param>
    <param-name>username</param-name>
    <param-value>admin</param-value>
</context-param>
<context-param>
    <param-name>pwd</param-name>
    <param-value>123admin</param-value>
</context-param>
```

## 第三方库

本项目使用了以下第三方库：

- [jaudiotagger](https://github.com/marcoc1712/jaudiotagger) - Licensed under the LGPL License. 
- [MySQL Connector](https://github.com/mysql/mysql-connector-j) - Licensed under the MIT License. 

其许可证文本已包含在本仓库中。

## 开源许可

本项目使用 GPL-3.0 开源许可证。