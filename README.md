#
## 简介

问世是一款仿抖音的开源项目，目的是熟悉Android MVVM、Kotlin、Jetpack、音视频、即时通讯以及混合Flutter

## Todo List
- [x] 登录、游客模式
- [x] 登陆页ffmpeg视频播放器 附[源码分析](https://github.com/qq326646683/tech-article/blob/master/android/AndroidJNI实战——记录实现视频播放器.md)
- [x] 抖音首页
- [x] 点赞动画(vap) 附[源码分析](https://github.com/qq326646683/tech-article/blob/master/android/Android学习——VAP源码.md)
- [ ] 视频处理
    - [ ] 滤镜
    - [ ] 美颜
- [ ] 发布视频
- [ ] 嵌入cocos游戏
- [ ] 即时通讯(Protobuf)
    - [ ] 添加好友
    - [ ] 文字
    - [ ] 图片
    - [ ] 视频
    - [ ] 语音
    - [ ] 1v1视频聊天(WebRTC)
- [ ] 朋友圈
    - [ ] 发布动态
    - [ ] 朋友圈展示
- [ ] 还没想好(混合Flutter)

## Setup

1. 后端地址: https://github.com/qq326646683/wenshi_api
2. 修改app请求api url
```
Retrofit.kt

private const val BASE_URL = "http://192.168.11.36:3000"
```





