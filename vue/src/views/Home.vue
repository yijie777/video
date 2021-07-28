<template>
  <div style="background-color:rgb(230,236,240);min-width: 1800px">
    <div style="margin: 20px 200px;background-color: white;border-radius: 5px" v-loading.fullscreen.lock="fullscreenLoading">

      <div style="margin-left: 30px">
        <ul class="home-menu">
          <li @click="searchByType('',-1)" :class="{active:categoryIndex===-1}">
            全部
          </li>
        </ul>
        <ul class="home-menu" v-for="(item, index) in types" :key="index">
          <li @click="searchByType(item.typeName,index)" :class="{active:categoryIndex===index}">
            {{ item.typeName }}
          </li>
        </ul>
      </div>

      <div  style="min-height: 650px">
        <div v-show="videos.length ===0" style="padding: 100px">
          <el-empty description="未找到搜索结果" ></el-empty>
        </div>
        <el-row>
          <el-col :span="6" v-for="(i,index) in videos">
            <div class="grid-content bg-purple ly1">
              <el-image class="img-link" style="width: 100%; height: 100%" :src="i.thumbnailUrl" :fit="'cover'"
                        @click="goAblum(i)">

              </el-image>
            </div>
            <div class="link" style="margin: 0 30px" @click="goAblum(i)">{{ i.name }}</div>
          </el-col>
        </el-row>
      </div>



      <div style="padding:20px 0 20px 40px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[8, 12, 16, 24]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

    </div>
    <div style="text-align: center;margin: 10px 0">
      Copyright © 2021 Zhu-Yijie
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
      videos: {},
      currentPage: 1,
      search: '',
      pageSize: 8,
      total: 10,
    }
  },
  created() {
    this.fullscreenLoading = true;
    setTimeout(() => {
      this.fullscreenLoading = false;
    }, 500);
    this.load()
    this.findTypes()
  },
  methods: {
    clickCategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
    },
    searchByType(typeName, index) {
      this.clickCategory(index)
      this.search = typeName
      this.load()
    },
    findTypes() {
      request.get("/type").then(res => {
        this.types = res.data
      })
    },
    goAblum(item) {
      this.$router.push({path: `/${this.path}/${item.id}`})
    },
    load() {
      request.get("/video", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.videos = res.data.records
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },

  },

}
</script>
<style>
.ly1 {
  margin: 30px;
}

.link:hover {
  color: cornflowerblue;
  cursor: pointer;
}

.img-link:hover {
  color: cornflowerblue;
  cursor: pointer;
  border: #d9d9d9 1px solid;
}

.home-menu {
  display: inline-block;
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