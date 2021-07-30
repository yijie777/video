<template>
  <div style="padding: 10px">
    <div style="margin: 10px 0">
      <el-popconfirm title="确定删除吗？" @confirm="delList">
        <template #reference>
          <el-button type="primary" v-if="user.roleId===4">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名">
      </el-table-column>
      <el-table-column
          prop="nickName"
          label="昵称">
      </el-table-column>
      <el-table-column
          prop="age"
          label="年龄">
      </el-table-column>
      <el-table-column
          prop="sex"
          label="性别">
      </el-table-column>
      <el-table-column
          label="头像">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.imgUrl"
              :preview-src-list="[scope.row.imgUrl]">
          </el-image>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.roleId === 1||user.roleId===4">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)" v-if="user.roleId === 1||user.roleId===4">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="提示" v-model="dialogVisible" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" style="width: 80%" show-password :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="头像">
            <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess"
                       :before-upload="beforeAvatarUpload">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="角色">
            <el-select v-model="form.roleId" placeholder="请选择角色">
              <div v-for="(item, index) in roles" :key="index">
                <el-option :label="item.userRole" :value="item.id"></el-option>
              </div>
            </el-select>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'AdminVideo',
  components: {

  },
  data() {
    return {
      selectionList: [],
      roles: [],
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 5,
      total: 0,
      tableData: [],
      filesUploadUrl: "http://" + window.server.filesUploadUrl + ":9090/files/uploadTh"
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)

    this.findRoles()
    this.load()
  },
  methods: {
    //上传图片前的图片验证回调
    beforeAvatarUpload(file) {
      //图片格式
      const isJPG = file.type === 'image/jpg' || file.type === 'image/png'|| file.type === 'image/jpeg';
      //图片大小
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传图片只能为jpg或png格式');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过2MB');
      }

      return isJPG && isLt2M;
    },
    handleSelectionChange(selection) {
      this.selectionList = []
      selection.forEach(element => {
        this.selectionList.push(element.id)
      });
    },
    findRoles() {
      request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    filesUploadSuccess(res) {
      this.form.imgUrl = "http://" + window.server.filesUploadUrl + ":9090"+res.data
    },
    load() {
      this.loading = true
      request.get("/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.total = res.data.total
        this.tableData=[]
        let _this=this
        res.data.records.forEach(function(user) {
          user.imgUrl = "http://" + window.server.filesUploadUrl + ":9090"+ user.imgUrl
          _this.tableData.push(user)
        });

      })


    },
    add() {
      this.dialogVisible = true
      this.form = {}
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save() {
      if (this.form.id) {  // 更新
        console.log(this.form)
        this.form.imgUrl= this.form.imgUrl.substring(this.form.imgUrl.indexOf("\\"))
        request.put("/user", this.form).then(res => {
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }  else {  // 新增
        request.post("/user", this.form).then(res => {
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }

    },
    delList(){
      if(this.selectionList.length===0){
        this.$message({
          type: "warning",
          message: "请选择用户"
        })
        return
      }
      request.post("/user/delList", this.selectionList).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "批量删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })

    },
    handleDelete(id) {
      request.delete("/user/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
