<template>
  <div style="background-color:rgb(230,236,240);">
    <div style="margin: 20px 100px;border-radius: 5px">

      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item v-for="i in img" :key="i">
          <div class="block">
            <el-image :src="i" style="height: 400px;width: 860px" fit="cover"></el-image>
          </div>
        </el-carousel-item>
      </el-carousel>


    </div>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Home',
  data() {
    return {
      fullscreenLoading: false,
      categoryIndex: -1,
      types: [],
      path: "video-list-album",
      videos: [],
      currentPage: 1,
      search: '',
      pageSize: 8,
      total: 10,
      type: '',
      img: [
        "https://tse3-mm.cn.bing.net/th/id/OIP-C.sX4vpG_5ugWGVZ84EXOPLgHaEK?w=333&h=187&c=7&r=0&o=5&dpr=2&pid=1.7",
        "https://tse4-mm.cn.bing.net/th/id/OIP-C.2F3tsUNi3DolHGyFcF7ElQHaEK?w=319&h=180&c=7&r=0&o=5&dpr=2&pid=1.7",
        "https://tse1-mm.cn.bing.net/th/id/OIP-C.gd2thIjxqkjX5kubpCVNnQHaDZ?w=348&h=160&c=7&r=0&o=5&dpr=2&pid=1.7",
        "https://tse3-mm.cn.bing.net/th/id/OIP-C.IGG5kf3aa6sdhoP5rDjCzwHaEo?w=257&h=180&c=7&r=0&o=5&dpr=2&pid=1.7",
        "https://tse1-mm.cn.bing.net/th/id/OIP-C.3z9Wq7GFD3Lm9EzVuSinewHaFN?w=281&h=198&c=7&r=0&o=5&dpr=2&pid=1.7",
      ]
    }
  },
  created() {
  },
  methods: {
    clickCategory(index) { // 这里我们传入一个当前值
      this.categoryIndex = index
    },

    goAblum(item) {
      this.$router.push({path: `/${this.path}/${item.id}`})
    },
    watch: {
      $route: {
        handler: function (val, oldVal) {
        },
        deep: true
      }
    },
  },

}
</script>
<style scoped>

.home-menu li {
  line-height: 40px;
}

.home-menu li:hover {
  color: dodgerblue;
  cursor: pointer;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>