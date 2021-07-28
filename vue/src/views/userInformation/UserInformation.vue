<template>
  <div style="display: flex">
    <el-menu
        style="width: 200px;min-height: calc(100vh - 62px)"
        :default-active="path"

        class="el-menu-vertical-demo"
        router>


      <el-menu-item index="userInformation/modifyUserInformation">修改信息</el-menu-item>
      <el-menu-item index="userInformation/modifyUserPassword">修改密码</el-menu-item>
      <el-menu-item index="userInformation/modifyUserIcon">修改头像</el-menu-item>
    </el-menu>
    <router-view style="flex:1"/>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";

export default {
  name: 'UserInformation',
  data() {
    return {
      path:'userInformation'+this.$route.path,
    };
  },
  methods:{

  }
}
</script>
<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>