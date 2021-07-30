# video

### 介绍
大数据课程资源网

### 项目技术栈
#####后端技术栈
1. Spring Boot
2. MyBatis-plus
3. MySQL
4. 



#####前端技术栈
1. Nginx
2. Vue
3. ElementUI
4. axios
5. vue-router
6. Vuex
7. vue-video-player
8. vue-cropper
9. vue-cli


### 快速运行
#####  克隆本项目到本地  
1. git clone https://gitee.com/yijie777/video.git
##### 进入vue目录 安装依赖
2. npm install
##### 在 localhost:9988 启动项目（端口可以在vue.config.js 中设置）
3. npm run serve
##### 运行sql文件
4. mysql中创建springboot_vue_video并执行sql脚本
##### 启动springboot
5. ..

上传功能还需下载ffmpeg 并在springboot util中的ThumbnailThread 和TranscodingThread中修改ffmpeg的路径
由于我在vue项目中已经配置了端口转发，将数据转发到SpringBoot上，因此项目启动之后，在浏览器中输入http://localhost:9988就可以访问我们的前端项目了，所有的请求通过端口转发将数据传到SpringBoot中（注意此时不要关闭SpringBoot项目）。



### 项目部署


#### 前端
##### windows下nginx安装
1. https://www.cnblogs.com/jiangwangxiang/p/8481661.html
##### 进入vue目录 打包静态资源
2. npm run build
##### 复制dist文件夹到nginx根目录
3. ctrl+c/ctrl+v
##### 配置nginx
4. 
##### cmd启动nginx 或直接双击nginx.exe
5. start nginx 
#### 后端
##### 打包springboot项目运行
6. java -jar springboot_vue_video.jar