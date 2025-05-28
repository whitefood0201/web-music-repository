# .mp3文件上传

## 简介

将.mp3文件上传至服务器，存于某一静态地址，并提供地址给外部访问。

主要是给 [Net Music](https://github.com/TartaricAcid/NetMusic) 使用

## 实现

### 前端

#### 上传页面

简单写个居中，文件选择。

用 table 实现展示。

#### 查询文件

居中
查询输入框，
单选根据名字，根据mid
table

查询结果直接 table 中展示。

试着实现下点击复制。

#### 数据接收 js

后端获取的地址只是相对static目录的地址，需要再加上当前访问的域名。

其他基本差不多，简单的 table 显示。

### 后端

#### 接收文件

1. 接收文件。
2. 安全判断，文件格式。
3. 入数据库。
4. 写入静态地址。

#### 数据库

两种实现：

##### 数据库实现

数据库脚本：

```sql
drop table if exists t_mups;

create table t_mups(
        mid int primary key auto_increment,
        mname varchar(255),
        duration int,
);

insert into t_mups(mname, duration) values('aaa', 153);
insert into t_mups(mname, duration) values('bbb', 142);

select * from t_mups;
```

##### txt 实现

```txt
id_max=2
mid | mname | duration 
1 | aaa | 123
2 | bbb | 154 
```

启动时加载，关闭时持久化

如何保证序列不重复递增

- 在txt文件中**维护一个最高序号**。


#### 查询

根据索引(mid) 或 名称(name)查询。

返回索引，相对静态目录地址，名称，时长。以json格式传给前端。

格式：
```json
{
  "response": 200,
  "datas": [
    {
      "mid": 1,
      "name": "name",
      "duration": 203
    }
  ]
}
```

#### m3u8

前端select上按钮——播放列表。
监听按钮发送请求至getM3u8?url=?
sevlet监听 getM3u8?url=? 并通过给定url生成m3u8。
后端将 .m3u8 返回为文本
JS 给 .m3u8 提供一个地址供 PotPlayer 播放

Content-Type: application/vnd.apple.mpegurl