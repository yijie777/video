<template>
  <div style="margin:100px 0">
    <el-form ref="form" :model="form" status-icon :rules="rules" label-width="100px" style="width:400px;margin: 0 auto">
      <el-form-item label="视频名称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="视频描述" prop="describe">
        <el-input v-model="form.describe"></el-input>
      </el-form-item>
      <el-form-item label="视频类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择视频类型">
          <div v-for="(item, index) in types" :key="index">
            <el-option :label="item.typeName" :value="item.typeName"></el-option>
          </div>
        </el-select>
      </el-form-item>
      <el-upload
          ref="upload1"
          :multiple="true"
          :auto-upload="false"
          :on-change="fileChange1"
          :on-remove="onRemoveFile1"
          style="margin-bottom:10px; padding-left: 100px">
        <el-button size="small" type="primary">选择附件</el-button>
      </el-upload>

      <el-upload
          ref="upload"
          :multiple="true"
          :auto-upload="false"
          :on-change="fileChange"
          :on-remove="onRemoveFile"
          style="margin: 0 auto;padding-left: 100px">
        <template #trigger>
          <el-button size="small" type="primary">选取文件</el-button>
        </template>

        <el-button style="margin-left: 10px;" :disabled='uploadBtn' size="small" type="success" @click="submitUpload">上传到服务器<i
            class="el-icon-upload el-icon--right"></i></el-button>
        <div class="el-upload__tip" style="color: red">只能上传 avi,mp4,wmv 等视频文件，且不超过 100G</div>
        <el-progress :stroke-width="16" :percentage="progressPercent" v-show="proShow === 1"
                     style="margin-top: 10px">
        </el-progress>
      </el-upload>
    </el-form>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";

export default {
  name: 'VideoUpload',
  components: {},
  data() {
    return {
      uploadBtn: false,
      user: '',
      proShow: 0,
      types: [],
      form: {},
      fileList: [],
      enclosureList: [],
      progressPercent: "",
      rules: {
        name: [
          {required: true, message: '请输名称', trigger: 'blur'},
        ],
        describe: [
          {required: true, message: '请输入视频描述', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
    this.findTypes()
  },
  methods: {
    findTypes() {
      request.get("/type").then(res => {
        this.types = res.data
      })
    },

    // 文件上传
    onRemoveFile(file, fileList) {
      this.fileList = []
      for (let x of fileList) {
        if (x.raw) {
          this.fileList.push(x.raw)
        }
      }
    },
    submitUpload() {
      this.uploadBtn=true
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.fileList.length === 0) {
            this.$message({
              type: "error",
              message: "请选择文件"
            })
            return
          }
          this.proShow = 1

          let formData = new FormData()

          this.form.userId = this.user.id
          Object.keys(this.form).forEach((ele) => {
            formData.append(ele, this.form[ele]);
          });
          this.fileList.forEach(file => {
            formData.append('files', file)
          })
          if (this.enclosureList.length !== 0) {
            this.enclosureList.forEach(file => {
              formData.append('enclosureList', file)
            })
          }
          let config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            },
            onUploadProgress: progressEvent => {
              // progressEvent.loaded:已上传文件大小
              // progressEvent.total:被上传文件的总大小
              this.progressPercent = Number((progressEvent.loaded / progressEvent.total * 100).toFixed(2))
            }
          }
          request.post("/files/upload", formData, config).then(res => {
            this.uploadBtn=false
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "成功"

              })
              // setTimeout(() => {
              //   this.$router.go(0)
              // }, 1000);
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })

        }
      });


    },
    fileChange(file) {
      const typeArr = ['video/mp4', 'video/avi', 'video/webm', 'video/ogg', "video/x-ms-wmv"];
      const geShi = typeArr.indexOf(file.raw.type) !== -1;
      const isLt3M = file.size / 1024 / 1024 < 100000;

      if (!geShi) {
        this.$message.error('文件格式错误!');
        this.$refs.upload.clearFiles();
        this.fileList = null;
        return;
      }
      if (!isLt3M) {
        this.$message.error('上传视频大小不能超过 100G!');
        this.$refs.upload.clearFiles();
        this.fileList = null;
        return;
      }
      this.fileList.push(file.raw);
    },
    // 文件上传
    onRemoveFile1(file, enclosureList) {
      this.enclosureList = []
      for (let x of enclosureList) {
        if (x.raw) {
          this.enclosureList.push(x.raw)
        }
      }
    },
    fileChange1(file) {
      const isLt3M = file.size / 1024 / 1024 < 10000;
      if (!isLt3M) {
        this.$message.error('上传附件大小不能超过 10G!');
        this.$refs.upload1.clearFiles();
        this.enclosureList = null;
        return;
      }
      this.enclosureList.push(file.raw);

    },
  }
}
</script>
