<template>
  <div class="box" style="min-width: 1400px;">
    <div style="margin: 10px">
      <h2>{{ video.name }}</h2>
      <p>简介：{{ video.info }}</p>
    </div>

    <el-row>
      <el-col :span="16">
        <video-player class="vjs-custom-skin"
                      ref="videoPlayer"
                      :playsinline="true"
                      :options="playerOptions">
        </video-player>
      </el-col>
      <el-col :span="8">
        <div class="scroll-module">
          <h3>视频选集</h3>
          <el-button type="primary" size="small" :disabled="video.enclosureUrl==null" @click="downloadEnclosure">下载附件<i
              class="el-icon-download el-icon--right"></i></el-button>
          <div v-for="(item, index) in phases" :key="index" class="video-list" style="width: 100%">
            <p @click="changePhase(item.videoUrl,index)" :class="{active:categoryIndex===index}"
               style="overflow: hidden;white-space: nowrap; text-overflow:ellipsis; ">
              {{ item.name }}
            </p>
          </div>
        </div>

      </el-col>

    </el-row>


  </div>

</template>

<script>
import request from "@/utils/request";
import {mapGetters} from 'vuex'

export default {
  data() {
    return {
      playerOptions: {
        //播放速度
        playbackRates: [0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 3.0],
        //如果true,浏览器准备好时开始回放。
        autoplay: false,
        // 默认情况下将会消除任何音频。
        muted: false,
        // 导致视频一结束就重新开始。
        loop: false,
        // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        preload: 'auto',
        language: 'zh-CN',
        // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        aspectRatio: '16:9',
        // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        fluid: true,
        sources: [{
          //类型
          type: "video/mp4",
          //url地址
          src: ''
        }],
        //你的封面地址
        poster: '',
        //允许覆盖Video.js无法播放媒体源时显示的默认信息。
        notSupportedMessage: '此视频暂无法播放，请稍后再试',
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          //全屏按钮
          fullscreenToggle: true
        }
      },
      phases: [],
      categoryIndex: 0,
      video: {},

    }
  },
  methods: {
    downloadEnclosure() {
      window.open("http://" + window.server.filesUploadUrl + ":9090/files/download/" + this.video.id)
    },
    clickCategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
    },
    changePhase(url, index) {
      this.playerOptions.sources[0].src = url
      this.clickCategory(index)
    },
    getAllPhase() {
      request.get("/video/getAllPhase/" + this.$route.params.id).then(res => {
        this.phases = res.data
        this.playerOptions.sources[0].src = this.phases[0].videoUrl
      })
    },
    getVideo() {
      request.get("/video/getOneVideo/" + this.$route.params.id).then(res => {
        this.video = res.data
        console.log(this.video.enclosureUrl)
      })
    },


  },

  created() {
    this.getAllPhase()
    this.getVideo()
  }
}
</script>

<style scoped>


.video-list:hover {
  color: dodgerblue;
  cursor: pointer;
}

.active {
  color: dodgerblue;
  border-bottom: 0;
}

.scroll-module {
  padding:10px;
  margin-left: 30px;
  margin-right: 30px;
  background-color: rgb(244, 244, 244);
  height: 100%;
  line-height: 30px;
  border-radius: 3px
}


.box {
  /*flex: 0 1 auto;*/
  height: 800px;
  width: 1400px;
  /*margin: 20px 200px;*/
  margin: 0 auto;
  padding:0 10px 10px 10px;
  border: 1px solid #bdbdbd;
}
</style>
