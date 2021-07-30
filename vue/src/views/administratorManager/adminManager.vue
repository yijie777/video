<template>
  <div style="display: flex">

    <el-menu
        style="width: 200px;min-height: calc(100vh - 50px)"
        :default-active="path"
        router>
      <el-menu-item index="adminManager/adminVideo">视频管理</el-menu-item>
      <el-menu-item index="adminManager/adminUser">用户管理</el-menu-item>
      <el-menu-item index="adminManager/adminType">视频类型管理</el-menu-item>
      <el-menu-item index="adminManager/adminArticleType">博客类型管理</el-menu-item>
    </el-menu>
    <router-view style="flex:1"/>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";

export default {
  name: 'AdminManager',
  data() {
    return {
      path:'adminManager'+this.$route.path,
    };
  },

}
</script>
