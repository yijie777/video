<template>
  <div style=" height: 61px;line-height: 60px;display: flex;width: 100%;min-width: 1000px">
    <div style="margin: 10px ">
      <div class="block">

        <el-dropdown>
          <router-link :to="imgPath">
            <el-avatar :size="40" :src="user.imgUrl">登录</el-avatar>
          </router-link>
          <template #dropdown >
            <el-dropdown-menu >
              <el-dropdown-item v-if="user.username!==undefined" @click="$router.push('/modifyUserInformation')">个人信息</el-dropdown-item>
              <el-dropdown-item v-if="user.username!==undefined" @click="loginOut">退出系统</el-dropdown-item>
              <el-dropdown-item v-if="user.username===undefined" @click="toLogin">点击登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>


      </div>
    </div>
    <h3>{{ user.nickName || user.username }}</h3>
    <el-menu :default-active="path"
             class="el-menu-demo"
             mode="horizontal"
             router>
      <el-menu-item index="/home">首页</el-menu-item>
      <el-menu-item index="/videoStore">视频中心</el-menu-item>
      <el-menu-item index="/blog">热门博客</el-menu-item>
      <el-menu-item index="/userInformation">个人中心</el-menu-item>
      <el-menu-item index="/videoUpload">上传发布</el-menu-item>
      <el-menu-item index="/adminManager" :disabled="!(user.roleId === 1||user.roleId===4)">管理员后台</el-menu-item>
    </el-menu>
    <!--      搜索区域-->
    <div style="flex: 1"></div>
    <div>
      <el-input v-model="search" :placeholder="placeholderShow" style="width: 70%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="searchVideo">搜索</el-button>
    </div>
  </div>
</template>

<script>

export default {
  name: "Header",
  data() {
    return {
      path: this.$route.path,
      activeIndex: '1',
      circleUrl: "",
      search: '',
      imgPath: 'login',
      user: '',
      searchPath: 'search',
      placeholderShow: '请输入关键字',
    };
  },
  methods: {
    toLogin() {
      this.$router.push("/login")
    },
    loginOut() {
      sessionStorage.removeItem("user")
      this.$router.push("/login")
      this.$router.go(0)
    },

    searchVideo() {
      if (this.$route.path.indexOf('/blog') !== -1) {
        this.$router.push({
          path: `/blog`,
          query: {search: this.search}
        })
      } else {
        if (this.search === '') {
          this.$message({
            type: "warning",
            message: "请输入关键字"
          })
          return
        }
        this.$router.push({path: `/search/${this.search}`, query: {search: this.search}})
      }


    }
  },
  created() {
    //权限
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
    this.user.imgUrl = "http://" + window.server.filesUploadUrl +window.server.port + this.user.imgUrl
    if (this.user.username === undefined) {
      const filterArr = ['/login', '/register', '/', '/home', '/videoStore', '/blog'];
      const filter = filterArr.indexOf(this.path) === -1 && this.path.indexOf("video-list-album") === -1 && this.path.indexOf("search") === -1;
      if (filter) {
        this.$message({
          type: "warning",
          message: "请先登录"
        })
        this.$router.push("/login")
      }
    }
    const qx14 = ['/adminVideo', '/adminManager', '/adminUser'];
    const managerArr = [1, 4]
    if (qx14.indexOf(this.path) !== -1) {
      if (managerArr.indexOf(this.user.roleId) === -1) {
        this.$router.push('/')
        this.$message({
          type: "error",
          message: "无权限访问此页面"
        })
      }
    }


    if (this.user.username === undefined) {
      this.imgPath = 'login'
    } else {
      this.imgPath = ''
    }
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        if (val.path === '/blog') {
          this.placeholderShow = '博客搜索！请输入关键字'
        } else {
          this.placeholderShow = '请输入关键字'
        }
        // 请求服务端，确认当前登录用户的 合法信息
        if (val.path === '/home' && oldVal.path === '/login') {
          this.$router.go(0)
        }
        //是否登录的权限
        if (this.user.username === undefined) {
          const filterArr = ['/login', '/register', '/', '/home', '/videoStore', '/blog'];
          console.log(val.path)
          const filter = filterArr.indexOf(val.path) === -1 && val.path.indexOf("video-list-album") === -1 && val.path.indexOf("search") === -1;
          if (filter) {
            this.$message({
              type: "warning",
              message: "请先登录"
            })
            this.$router.push("/login")
          }
        }


      },
      // 深度观察监听
      deep: true
    }
  },


}
</script>

<style scoped>

</style>