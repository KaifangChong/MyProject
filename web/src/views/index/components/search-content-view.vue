<template>
  <div class="content-margin">
    <h1 class="search-name-box">{{ tData.keyword }}</h1>
    <div class="search-tab-nav clearfix">
      <div class="tab-text">
        <span>与</span>
        <span class="strong">{{ tData.keyword }}</span>
        <span>相关的内容</span>
      </div>
    </div>
    <div class="content-list">
      <div class="video-list">
        <a-spin :spinning="tData.loading" style="min-height: 200px;">
          <div class="pc-video-list">
            <div v-for="item in tData.pageData" :key="item.id" @click="handleDetail(item)"
                 class="video-item item-column-3">
              <div class="img-view">
                <img :src="item.cover">
                <div style="position: absolute; left: 10px; bottom: 10px;">
                  <!-- Placeholder for PlayIcon if needed -->
                </div>
              </div>
              <div class="info-view">
                <h3 class="video-name">{{ item.title.substring(0, 12) }}</h3>
                <span style="color: #444; font-size: 12px;">{{ getFormatTime(item.createTime, true) }}</span>
                <br />
                <span style="color: #444; font-size: 12px;">{{ item.pv }}次观看</span>
              </div>
            </div>
            <div v-if="tData.pageData.length <= 0 && !tData.loading" class="no-data">暂无数据</div>
          </div>
        </a-spin>
        <div class="page-view">
          <a-pagination v-model:value="tData.page" size="small" @change="changePage" :hideOnSinglePage="true"
                        :defaultPageSize="tData.pageSize" :total="tData.total"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {listApi as listVideoList} from '/@/api/video'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";
import {getFormatTime} from '/@/utils/'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const tData = reactive({
  loading: false,
  keyword: '',
  videoData: [],
  pageData: [],
  page: 1,
  total: 0,
  pageSize: 20,
})

onMounted(() => {
  search()
})

// 监听 query 参数的变化
watch(
    () => route.query,
    () => {
      search()
    },
    { immediate: false }
)

const search = async () => {
  tData.keyword = route.query.keyword?.trim() || ''
  await getVideoList({ keyword: tData.keyword,page:1,pageSize:1000,isAdmin: 1 })
}

// 分页事件
const changePage = (page) => {
  tData.page = page
  const start = (tData.page - 1) * tData.pageSize
  tData.pageData = tData.videoData.slice(start, start + tData.pageSize)
  console.log(`第 ${tData.page} 页`)
}

const handleDetail = (item) => {
  // 跳转新页面
  const { href } = router.resolve({ name: 'detail', query: { id: item.id } })
  window.open(href, '_blank')
}

const getVideoList = async (data) => {
  tData.loading = true
  try {
    const res = await listVideoList(data)
    res.data.list.forEach((item) => {
      if (item.cover) {
        item.cover = `${BASE_URL}/api/staticfiles/image/${item.cover}`
      }
    })
    tData.videoData = res.data.list
    tData.total = tData.videoData.length
    changePage(1)
  } catch (err) {
    console.log(err)
  } finally {
    tData.loading = false
  }
}

</script>

<style scoped lang="less">
.video-list {
  text-align: left; /* 中心对齐，让卡片居中 */
}

.video-item {
  display: inline-block; /* 使用 inline-block 实现卡片一行显示 */
  width: 22%; /* 每行三个卡片，调整宽度以适应需求 */
  margin: 16px; /* 控制卡片之间的间距 */
  position: relative;
  cursor: pointer;
  box-shadow: 4px 4px 4px rgba(200, 200, 200, 0.3), -4px 4px 4px rgba(200, 200, 200, 0.3);
  border-radius: 12px;
  overflow: hidden;

  .img-view {
    position: relative;
    height: 180px;
    width: 100%;
    border-top-left-radius: 12px;
    border-top-right-radius: 12px;

    img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      border-top-left-radius: 12px;
      border-top-right-radius: 12px;
    }
  }

  .info-view {
    padding: 12px;
    background-color: #fff;
    border-bottom-left-radius: 12px;
    border-bottom-right-radius: 12px;

    .video-name {
      font-weight: bold;
      line-height: 24px;
      color: #0f1111;
      font-size: 15px;
    }
  }
}

@media (max-width: 768px) {
  .video-item {
    width: 45%; /* 中等屏幕每行两个卡片 */
  }
}

@media (max-width: 480px) {
  .video-item {
    width: 100%; /* 小屏幕每行一个卡片 */
  }
}
</style>
