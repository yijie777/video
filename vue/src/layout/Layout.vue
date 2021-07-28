<template>
<div>
  <Header></Header>
  <!--      主题-->
  <div style="display: flex;flex-direction: column;">

    <!--        内容区域-->
    <router-view style="flex:1"/>

  </div>
</div>
</template>

<script>
import Header from "@/components/Header";
export default {
  name: "Layout",
  components: {
    Header,
  }
}
</script>

<style scoped>

</style>