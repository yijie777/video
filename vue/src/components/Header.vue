<template>
  <div style=" height: 61px;line-height: 60px;display: flex ">
    <div style="margin: 10px ">
      <div class="block">

        <el-dropdown>
          <router-link :to="imgPath">
            <el-avatar :size="40" :src="user.imgUrl">登录</el-avatar>
          </router-link>
          <template #dropdown>
            <el-dropdown-menu v-if="user.username!==undefined">
              <el-dropdown-item @click="$router.push('/modifyUserInformation')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="loginOut">退出系统</el-dropdown-item>
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
      <!--      <el-menu-item index="/dynamic">动态</el-menu-item>-->
      <!--      <el-menu-item index="/message">消息</el-menu-item>-->
      <el-menu-item index="/userInformation">个人中心</el-menu-item>
      <!--      <el-menu-item index="/videoStore">视频中心</el-menu-item>-->
      <el-menu-item index="/videoUpload">上传发布</el-menu-item>
      <el-menu-item index="/blog">热门博客</el-menu-item>
      <el-menu-item index="/adminManager" :disabled="!(user.roleId === 1||user.roleId===4)">管理员后台</el-menu-item>
    </el-menu>
    <!--      搜索区域-->
    <div style="flex: 1"></div>
    <div>
      <el-input v-model="search" placeholder="请输入关键字" style="width: 70%" clearable></el-input>
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
    };
  },
  methods: {
    loginOut() {
      sessionStorage.removeItem("user")
      this.$router.push("/login")
      this.$router.go(0)
    },

    searchVideo() {
      if (this.search === '') {
        this.$message({
          type: "warning",
          message: "请输入关键字"
        })
        return
      }
      this.$router.push({path: `/search/${this.search}`})

    }
  },
  created() {
    //权限
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)

    if (this.user.username === undefined) {
      const filterArr = ['/login', '/register', '/', '/home', '/blog'];
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

        if (val.path === '/home' && oldVal.path === '/login') {
          this.$router.go(0)
        }
        //是否登录的权限
        if (this.user.username === undefined) {
          const filterArr = ['/login', '/register', '/', '/home', '/blog'];
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