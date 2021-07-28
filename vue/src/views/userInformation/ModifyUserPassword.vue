<template>
  <div style="min-width: 1800px">
    <el-card style="width: 40%; margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="旧密码">
          <el-input v-model="form.oldpassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.newpassword"></el-input>
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
  name: "ModifyUserPassword",
  data() {
    return {
      form: {},
      user: '',
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      let str = sessionStorage.getItem("user") || "{}"
      this.user = JSON.parse(str)
      if (this.form.oldpassword !== this.user.password) {
        this.$message({
          type: "error",
          message: '密码输入错误'
        })
        return
      }
      if (this.form.newpassword !== this.form.confirm) {
        this.$message({
          type: "error",
          message: '密码不一致'
        })
        return
      }
      this.form.password=this.form.newpassword
      request.put("/user", this.form).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
          setTimeout(() => {
            this.$router.go(0)
          }, 500);
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