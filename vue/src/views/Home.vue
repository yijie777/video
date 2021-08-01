<template>
  <div style="background-color:rgb(230,236,240);min-width: 1800px">
    <div style="margin: 20px 200px;background-color: white;border-radius: 5px">

      <div style="margin-left: 30px">
        <ul class="home-menu" v-for="(item, index) in types" :key="index">
          <li>
            {{ item.typeName }}
            {{ videos[item.id] }}
          </li>
          <el-row>
            <el-col :span="6" v-for="i in videos[item.id]">
              <div class="grid-content bg-purple ly1">
                <el-image class="img-link" style="width: 100%; height: 100%" :src="i.thumbnailUrl" :fit="'cover'"
                          @click="goAblum(i)">

                </el-image>
              </div>
              <div class="link" style="margin: 0 30px" @click="goAblum(i)">{{ i.name }}</div>
            </el-col>
          </el-row>
        </ul>
      </div>


    </div>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Home',
  data() {
    return {
      fullscreenLoading: false,
      categoryIndex: -1,
      types: [],
      path: "video-list-album",
      videos: [],
      currentPage: 1,
      search: '',
      pageSize: 8,
      total: 10,
      type: '',
    }
  },
  created() {
    this.findTypes()
  },
  methods: {
    clickCategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
    },

    findTypes() {
      request.get("/type").then(res => {
        let _this = this
        this.types = res.data
        this.types.forEach(function (type) {
          let resVideoList = []
          request.get("/video", {
            params: {
              type: type.typeName
            }
          }).then(res => {
            res.data.records.forEach(function (video) {
              video.thumbnailUrl = "http://" + window.server.filesUploadUrl + ":9090" + video.thumbnailUrl
              resVideoList.push(video)
            });
          })
          _this.videos[type.id] = resVideoList
        })
        console.log(this.videos)
      })

    },
    goAblum(item) {
      this.$router.push({path: `/${this.path}/${item.id}`})
    },
    watch: {
      $route: {
        handler: function (val, oldVal) {
          this.findTypes()
        },
        deep: true
      }
    },
  },

}
</script>
<style scoped>
.ly1 {
  margin: 30px;
}

.link:hover {
  color: cornflowerblue;
  cursor: pointer;
}

.img-link:hover {
  cursor: pointer;
}

.home-menu {
  padding: 20px;
  list-style-type: none
}

.home-menu li {
  line-height: 40px;
}

.home-menu li:hover {
  color: dodgerblue;
  cursor: pointer;
}

.active {
  font-weight: 600;
  color: dodgerblue;
  border-bottom: 3px solid dodgerblue;
}
</style>