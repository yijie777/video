<template>
  <div style="background-color:rgb(230,236,240);">
    <div style="margin: 0 auto;background-color: white;width: 1000px;margin-top: 20px"
         v-loading.fullscreen.lock="fullscreenLoading">

      <div >
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
        <ul class="home-menu">
          <el-radio-group v-model="sort" @change="load">
            <el-radio :label="0">默认</el-radio>
            <el-radio :label="-1">降序</el-radio>
            <el-radio :label="1">升序</el-radio>
          </el-radio-group>
        </ul>

      </div>


      <el-row style="min-height: 628px">
        <div  v-show="emptyShow" style="padding-top:100px; margin: 0 auto">
          <el-empty description="未找到搜索结果" ></el-empty>
        </div>
        <el-col :span="24" v-for="(blog,index) in blogs">
          <div>
            <div class="blog">
              <div class="content">
                <h3 class="titile" @click="toCSDN(blog)">{{ blog.articleTitle }}</h3>
                <p class="desc" @click="toCSDN(blog)">
                  {{ blog.articleDesc }}
                </p>
                <div class="operation">
                  <div><span>  {{ blog.author }}</span>
                    <span class="time">{{ blog.publish }}</span>
                    <span style="margin-right:10px;">{{ blog.commentCount }} 评论</span>
                    <span style="margin-right:10px;"> {{ blog.readCount }} 浏览</span>
                    <span style="margin-right:10px;"> {{ blog.collectCount }} 收藏</span>
                  </div>
                </div>
              </div>

              <!--              <div class="right-img">-->
              <!--                <img-->
              <!--                    src="https://live-file.csdnimg.cn/release/live/file/1627286516475.png?x-oss-process=image/resize,l_300"-->
              <!--                    style="width: 156px; height: 88px;">-->
              <!--              </div>-->

            </div>
          </div>


          <!--          <div class="link" style="margin: 0 30px" @click="goAblum(i)">{{ i.name }}</div>-->
        </el-col>
      </el-row>


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


    <el-dialog title="详细信息" v-model="dialogVisible" width="60%" >
      <div v-html='context'></div>
     </el-dialog>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Blog',
  data() {
    return {
      context:"",
      dialogVisible: false,
      fullscreenLoading: false,
      categoryIndex: -1,
      types: [],
      path: "video-list-album",
      blogs: [],
      currentPage: 1,
      search: '',
      pageSize: 12,
      total: 10,
      sort: 0,
      type: '',
      emptyShow: false,
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
    toCSDN(blog) {
      // window.open(articleUrl)
      this.dialogVisible = true
      this.context = blog.articleContext
      console.log(blog)
    },
    clickCategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
    },
    searchByType(typeName, index) {
      this.clickCategory(index)
      this.type = typeName
      this.load()
    },
    findTypes() {
      request.get("/articletype").then(res => {
        this.types = res.data
      })
    },
    load() {
      request.get("/blog", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          sort: this.sort,
          type: this.type
        }
      }).then(res => {
        this.blogs = res.data.records
        this.total = res.data.total

        this.total===0?this.emptyShow=true:this.emptyShow=false
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
        if (this.$route.query.search !== undefined) {
          this.search = this.$route.query.search
          this.load()
        }

      },
      deep: true
    }
  },
}
</script>
<style scoped>


.home-menu {
  display: inline;
  padding: 20px;
  list-style-type: none
}

.home-menu li {
  display: inline;
  line-height: 60px;
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

.blog {
  padding: 10px 20px;
}

.desc {
  margin: 10px 0;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.time {
  padding: 0 10px;
}

.operation {
  font-size: 14px;
  color: #999aaa;
}

.titile:hover {
  color: red;
  cursor: pointer;
}

.desc:hover {
  cursor: pointer;
}

</style>
