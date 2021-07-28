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
          sortable>
      </el-table-column>
      <el-table-column
          prop="name"
          label="名称">
      </el-table-column>
      <el-table-column
          prop="info"
          label="介绍">
      </el-table-column>
      <el-table-column
          prop="type"
          label="类型">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间">
      </el-table-column>
      <el-table-column
          label="封面">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.thumbnailUrl"
              :preview-src-list="[scope.row.thumbnailUrl]">
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
          <el-form-item label="名称">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="介绍">
            <el-input v-model="form.info" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="视频类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择视频类型">
              <div v-for="(item, index) in types" :key="index">
                <el-option :label="item.typeName" :value="item.typeName"></el-option>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess"
                       :before-upload="beforeAvatarUpload">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
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
  components: {},
  data() {
    return {
      selectionList: [],
      types: [],
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
    // 请求服务端，确认当前登录用户的 合法信息
    // request.get("/user/" + this.user.id).then(res => {
    //   if (res.code === '0') {
    //     this.user = res.data
    //   }
    // })
    this.findTypes()
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
      const _this = this;
      const isSize = new Promise(function(resolve, reject) {
        const img = new Image();
        const _URL = window.URL || window.webkitURl;
        img.onload = function() {
          file.width = img.width;//图片宽度
          file.height = img.height;//图片高度
          const valid = img.width/16 ===  img.height/9;//上传图片尺寸判定
          valid ? resolve() : reject(new Error('error'));
        };
        img.src = _URL.createObjectURL(file);
      }).then(
          () => {return file;},
          () => {
            _this.$message.error('上传图片尺寸必须为16:9(1280×720、1366×768、1920×1080)');
            return Promise.reject(new Error('error'));
          }
      );
      return isJPG && isLt2M && isSize;
    },
    handleSelectionChange(selection) {
      this.selectionList = []
      selection.forEach(element => {
        this.selectionList.push(element.id)
      });
    },
    findTypes() {
      request.get("/type").then(res => {
        this.types = res.data
      })
    },
    filesUploadSuccess(res) {
      this.form.thumbnailUrl = res.data
    },
    load() {
      this.loading = true
      request.get("/video", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
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
        request.put("/video", this.form).then(res => {
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
      } else {  // 新增
        request.post("/video", this.form).then(res => {
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
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })

    },
    delList(){
      if(this.selectionList.length===0){
        this.$message({
          type: "warning",
          message: "请选择视频"
        })
        return
      }
      request.post("/video/delList", this.selectionList).then(res => {
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
    handleDelete(id) {
      request.delete("/video/" + id).then(res => {
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
