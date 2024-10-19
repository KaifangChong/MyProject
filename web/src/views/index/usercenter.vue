<template>
  <div class="user">

    <Header />

    <div class="user-content">
      <div class="user-content-left">
        <MineInfosView />
      </div>
      <div class="user-content-right">
        <router-view />
      </div>
    </div>

    <Footer />

  </div>
</template>
<script>
import Header from '/@/views/index/components/header.vue'
import Footer from '/@/views/index/components/footer.vue'
import MineInfosView from '/@/views/index/user/mine-infos-view.vue'

export default {
  components: {
    MineInfosView,
    Footer,
    Header
  },
  data () {
    return {
      collapsed: false
    }
  }
}
</script>

<style scoped lang="less">
.user {
  display: block;
}

.user-content {
  display: flex;
  flex-direction: row;
  //background-color: #2a9a44;
  width: 100%;
  margin: 80px 16px;
  .user-content-left {
  }
  .user-content-right{
    flex: 1;
    padding-right: 20px;
  }

}
</style>
