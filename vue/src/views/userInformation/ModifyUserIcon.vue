<template>
  <div style="min-width: 1800px">
    <!--    <el-upload-->
    <!--        ref="upload"-->
    <!--        :action=filesUploadUrl-->
    <!--        :on-success="filesUploadSuccess">-->
    <!--      <el-button type="primary">点击上传</el-button>-->
    <!--    </el-upload>-->
    <el-card style="width: 40%; margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="当前头像">
          <el-image
              style="width: 178px; height: 178px"
              :src="user.imgUrl"
              :preview-src-list="[user.imgUrl]">
          </el-image>
        </el-form-item>
        <el-form-item label="选择图片">
          <el-upload
              class="avatar-uploader"
              :action=filesUploadUrl
              :show-file-list="false"
              :on-success="filesUploadSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="user.imgUrl2" :src="user.imgUrl2" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <div style="text-align: center">
          <el-button type="primary" @click="update">更新</el-button>
        </div>
      </el-form>
    </el-card>



  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ModifyUserIcon",
  data() {
    return {
      user: {},
      filesUploadUrl: "http://" + window.server.filesUploadUrl + window.server.port+"/files/uploadTh"

    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    this.user.imgUrl = "http://" + window.server.filesUploadUrl + window.server.port + this.user.imgUrl
  },
  methods: {
    //上传图片前的图片验证回调
    beforeAvatarUpload(file) {
      //图片格式
      const isJPG = file.type === 'image/jpg' || file.type === 'image/png'||file.type === 'image/jpeg';
      //图片大小
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传图片只能为jpg或png格式');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过2MB');
      }
      return isJPG && isLt2M
    },
    filesUploadSuccess(res) {
      this.user.imgUrl2 = "http://" + window.server.filesUploadUrl + window.server.port +res.data
      console.log(this.user.imgUrl2)
    },
    update() {
      this.user.imgUrl= this.user.imgUrl2.substring(this.user.imgUrl2.indexOf("/images"))
      console.log("--",this.user.imgUrl2.substring(this.user.imgUrl2.indexOf(":9093")+1))
      request.put("/user", this.user).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.user))
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
      this.user.imgUrl2=''
    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}
.avatar-uploader:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>