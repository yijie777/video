import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import 'dayjs/locale/zh-cn'
import locale from 'element-plus/lib/locale/lang/zh-cn'


import VideoPlayer from 'vue-video-player/src'
import 'vue-video-player/src/custom-theme.css'
import 'video.js/dist/video-js.css'

import VueCropper from 'vue-cropper/src'
import 'vue-cropper/dist/index'


import '@/assets/css/global.css'

createApp(App).use(store).use(router).use(ElementPlus, { locale }).use(VideoPlayer).use(VueCropper).mount('#app')
