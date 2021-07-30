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
          prop="typeName"
          label="类型名称">
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
          <el-form-item label="类型">
            <el-input v-model="form.typeName" style="width: 80%" :disabled="user.roleId!==4"></el-input>
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
  name: 'AdminArticleType',
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
      pageSize: 10,
      total: 0,
      tableData: [],
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
      request.get("/articletype/findPage", {
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
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/articletype", this.form).then(res => {
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
        request.post("/articletype", this.form).then(res => {
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
          message: "请选择类型"
        })
        return
      }
      request.post("/articletype/delList", this.selectionList).then(res => {
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
      request.delete("/articletype/" + id).then(res => {
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
