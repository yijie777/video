<template>
<div>
  <el-card style="width: 40%; margin: 10px">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="旧密码">
        <el-input v-model="form.oldpassword"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.confirm"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" @click="update">保存</el-button>
    </div>
  </el-card>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ModifyIcon",
  data() {
    return {
      form: {}
    }
  },
  methods: {
    update() {
      request.put("/user", this.form).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>