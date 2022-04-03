# ByteDance Android Homework - Network

[TOC]

## 软件功能实现


## 抓包流程与结果


### 用 WireShark 筛选并解析包

使用如下语句作为过滤器：
```
ipv6.addr eq 2403:d400:1400:2fd:3dbc:3718:97df:749 and ipv6.addr eq 2408:8706:0:5e05:123:123:219:80 and tls
```
