<template>
  <div style="background-color:rgb(230,236,240)">
    <div style="margin: 20px 200px;background-color: white;border-radius: 5px">
      <div style="min-height: 600px">

        <div  v-show="emptyShow === 1" style="padding: 100px">
          <el-empty description="未找到搜索结果" ></el-empty>
        </div>
        <el-row>
          <el-col :span="6" v-for="(i,index) in videos">
            <div class="grid-content bg-purple ly1">
              <el-image class="img-link" style="width: 100%; height: 100%" :src="i.thumbnailUrl" :fit="'cover'"
                        @click="goAblum(i)"></el-image>
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
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Search',
  data() {
    return {
      emptyShow: -1,
      categoryIndex: -1,
      path: "video-list-album",
      videos: {},
      currentPage: 1,
      search: '',
      pageSize: 8,
      total: 10,
    }
  },
  created() {
    this.search = this.$route.params.key
    this.load()
  },
  methods: {
    clickcategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
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
        if (this.total !== 0) {
          this.emptyShow = -1
        } else {
          this.emptyShow = 1
        }
        console.log(this.total)
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
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.search = this.$route.params.key
        this.load()
      },
      // 深度观察监听
      deep: true
    }
  },


}
</script>
<style>
.ly1 {
  height: 250px;
  background-color: rebeccapurple;
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


.home-menu li {
  line-height: 40px;
}

.home-menu li:hover {
  color: dodgerblue;
  cursor: pointer;
}


</style>