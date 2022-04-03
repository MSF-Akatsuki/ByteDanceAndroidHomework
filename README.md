# ByteDance Android Homework - Network

[TOC]

## 软件功能实现

### 文本翻译

在输入框中键入汉语或英语的句子，点击翻译，将对应地翻译为英语或汉语，显示在下方的输出框中。

### 语言检测

若输入框中的文字所属语言并非汉语或英语，点击翻译后，两框之间将显示一行提示文字提示语言不受支持。

## 抓包流程与结果
使用 mitmproxy + wireshark 进行抓包分析。通过 mitmproxy 转发 tls 包，并记录下在其加解密过程的密钥，然后使用 wireshark 对转发的包进行解密

### 安装系统证书


根据 https://docs.mitmproxy.org/stable/howto-install-system-trusted-ca-android/

用以下语句打开虚拟机并获取系统目录读写权限
```
emulator -avd Pixel_5_API_30 -writable-system -no-snapshot
adb root
adb shell avbctl disable-verification
adb reboot
adb root
adb remount
```

将mitmproxy的证书存入用户目录
```
adb push mitmproxy.cer /sdcard
```
然后使用虚拟机中安全设置，从（虚拟）SD卡安装用户证书；

此后，将安装好的证书移动至系统证书所在文件夹，此后系统将其识别为系统证书
```
adb shell mv /data/misc/user/0/cacerts-added/c8750f0d.0 /system/etc/security/cacerts
```

### 连接mitm代理

打开 mitmproxy 并指定端口
```
mitmproxy -p 1837
```
然后操作安卓模拟器的 wlan 设置连接至该代理。

此时若从安卓发送 https 请求，在 mitmproxy 的界面可以查看解密后的 http 包（事实上已经达成了目标，但若为查看整个交互过程的多层次包信息，需使用 wireshark 导出 .pcapng）。

### 用 WireShark 捕获、筛选并解析包

在用户环境变量中设置 SSLKEYLOGFILE 指定 SSL 密钥输出文件。
重启 mitmproxy，此后其发送 TLS 包时将会把密钥存储于这一文件中。

在 WireShark 中将 TLS 协议设置中， (Pre)-Master-Secret log file name 指定为这一输出文件，WireShark将自动解析文件，并尝试用其中密钥解密抓包过程中的 TLS 包。

将使用的网卡设置为捕获目标，在安卓手机端使用本 app 发送一次请求，可以在 WireShark 上捕获到相关包。

使用如下语句作为过滤器：
```
ipv6.addr eq 2403:d400:1400:2fd:3dbc:3718:97df:749 and ipv6.addr eq 2408:8706:0:5e05:123:123:219:80 and tls
```
然后导出对应的项目，可得到对应的包。