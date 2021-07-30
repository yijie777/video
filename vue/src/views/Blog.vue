<template>
  <div style="background-color:rgb(230,236,240);min-width: 1800px">
    <div style="margin: 0 auto;background-color: white;width: 1000px;margin-top: 20px"
         v-loading.fullscreen.lock="fullscreenLoading">

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
        <ul class="home-menu">
          <el-radio-group v-model="sort" @click="load">
            <el-radio :label="0">默认</el-radio>
            <el-radio :label="-1">降序</el-radio>
            <el-radio :label="1">升序</el-radio>
          </el-radio-group>
        </ul>

      </div>

      <div  v-show="emptyShow" style="padding: 100px">
        <el-empty description="未找到搜索结果" ></el-empty>
      </div>
      <el-row>
        <el-col :span="24" v-for="(blog,index) in blogs">
          <div>
            <div class="blog">
              <div class="content">
                <h3 class="titile" @click="toCSDN(blog.articleUrl)">{{ blog.articleTitle }}</h3>
                <p class="desc" @click="toCSDN(blog.articleUrl)">
                  {{ blog.articleDesc }}
                  提示：好多小伙伴反映，直接看到答案不太好，那我把答案的颜色设置为透明，答案位置还是在题目后面，需要鼠标选中才能看见（操作如下图），同时为了手机端的小伙伴（手机端也可以长按选中查看），我还会把所有答案放到文章最下面，希望给每天进步一点点的小伙伴更好的体验。每天进步一点点！1、在异常处理中，若try中的代码可能产生多种异常则可以对应多个catch语句，若catch中的参数类型有父类子类关系，此时应该将父类放在后面，子类放在前面。正确答案:
                  A 你的答案: A (正确)正确错误题解：首先所有
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
    <div style="text-align: center;margin: 10px 0">
      Copyright © 2021 Zhu-Yijie
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Blog',
  data() {
    return {
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
    toCSDN(articleUrl) {
      window.open(articleUrl)
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
<style>


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
