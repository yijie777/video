<template>
  <div style="padding: 10px">
    <div style="margin: 10px 0">
      <el-popconfirm title="确定删除吗？" @confirm="delList">
        <template #reference>
          <el-button type="primary" v-if="user.roleId===4">批量删除</el-button>
        </template>
      </el-popconfirm>
      <el-button type="primary" v-if="user.roleId===1||user.roleId===4" :onclick="add">新增</el-button>
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
        style="width: 90vw"
        @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          fixed="left"
          prop="id"
          label="ID"
          sortable
      width="80px">
      </el-table-column>
      <el-table-column
          fixed="left"
          width="130px"
          prop="articleId"
          label="博客ID">
      </el-table-column>
      <el-table-column
          width="350px"
          prop="articleTitle"
          label="标题">
      </el-table-column>
      <el-table-column
          width="150px"
          prop="author"
          label="作者">
      </el-table-column>
      <el-table-column
          width="450px"
          prop="articleUrl"
          label="地址">
      </el-table-column>
      <el-table-column
          width="200px"
          prop="publishDate"
          label="创建时间">
      </el-table-column>
      <el-table-column
          width="350px"
          prop="articleTags"
          label="标签">
      </el-table-column>
      <el-table-column
          width="120px"
          prop="articleType"
          label="类型">
      </el-table-column>
      <el-table-column
          width="120px"
          prop="readCount"
          label="浏览次数"
          sortable>
      </el-table-column>
      <el-table-column
          width="120px"
          prop="collectCount"
          label="收藏次数"
          sortable>
      </el-table-column>
      <el-table-column
          width="120px"
          prop="commentCount"
          label="评论次数"
          sortable>
      </el-table-column>
      <el-table-column label="操作"  width="150px" fixed="right">

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
          <el-form-item label="博客ID">
            <el-input v-model="form.articleId" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="标题">
            <el-input v-model="form.articleTitle" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.articleUrl" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-input v-model="form.publishDate" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="标签">
            <el-input v-model="form.articleTags" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="form.articleType" placeholder="请选择视频类型">
              <div v-for="(item, index) in types" :key="index">
                <el-option :label="item.typeName" :value="item.typeName"></el-option>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="浏览次数">
            <el-input v-model="form.readCount" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="收藏次数">
            <el-input v-model="form.collectCount" style="width: 80%" :disabled="user.roleId!==4"></el-input>
          </el-form-item>
          <el-form-item label="评论次数">
            <el-input v-model="form.commentCount" style="width: 80%" :disabled="user.roleId!==4"></el-input>
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
  name: 'AdminType',
  components: {},
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
      pageSize: 10,
      total: 0,
      tableData: [],
      types:[],
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

    handleSelectionChange(selection) {
      this.selectionList = []
      selection.forEach(element => {
        this.selectionList.push(element.id)
      });
    },

    load() {
      this.loading = true
      request.get("/blog", {
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
    findTypes() {
      request.get("/articletype").then(res => {
        this.types = res.data
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/blog", this.form).then(res => {
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
        request.post("/blog", this.form).then(res => {
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
    delList() {
      if (this.selectionList.length === 0) {
        this.$message({
          type: "warning",
          message: "请选择类型"
        })
        return
      }
      request.post("/blog/delList", this.selectionList).then(res => {
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

    },
    handleDelete(id) {
      request.delete("/blog/" + id).then(res => {
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
<style>

</style>
