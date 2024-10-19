<template>
  <div class="content">
    <!-- 第一层：视频分类和视频标签 -->
    <div class="top-section">
      <!-- 视频分类 -->
      <div class="left-search-item">
        <h4>视频分类</h4>
        <div class="category-view">
          <span class="category-item"
                :class="{ 'category-select': contentData.selectedKeys.includes(item.key) }"
                v-for="item in contentData.cData"
                :key="item.key"
                @click="onSelect([item.key])">
            {{ item.title }}
          </span>
        </div>
      </div>

      <!-- 视频标签 -->
      <div class="left-search-item">
        <h4>热门标签</h4>
        <div class="tag-view">
          <span class="tag"
                :class="{ 'tag-select': contentData.selectTagId === item.id }"
                v-for="item in contentData.tagData"
                :key="item.id"
                @click="clickTag(item.id)">
            {{ item.title }}
          </span>
        </div>
      </div>
    </div>

    <!-- 第二层：视频内容展示 -->
    <div class="content-right">
      <!-- 视频列表上方的标签 -->
      <div class="top-select-view">
        <div class="order-view">
          <span v-for="(item, index) in contentData.tabData"
                :key="index"
                class="tab"
                :class="{ 'tab-select': contentData.selectTabIndex === index }"
                @click="selectTab(index)">
            {{ item }}
          </span>
        </div>
      </div>

      <!-- 视频列表 -->
      <a-spin :spinning="contentData.loading">
        <div class="pc-video-list">
          <div v-for="item in contentData.pageData" :key="item.id" @click="handleDetail(item)" class="video-item">
            <div class="img-view">
              <img :src="item.cover" />
            </div>
            <div class="info-view">
              <h3 class="video-name">{{ item.title.substring(0, 12) }}</h3>
              <span style="font-size: 12px">{{ getFormatTime(item.createTime, true) }}</span>
              <br />
              <span style="font-size: 12px">{{ item.pv }}次观看</span>
            </div>
          </div>
          <div v-if="contentData.pageData.length <= 0 && !contentData.loading" class="no-data">暂无数据</div>
        </div>
      </a-spin>

      <!-- 加载更多按钮 -->
      <div v-if="contentData.hasMore && !contentData.loading" class="load-more">
        <a-button type="primary" @click="getVideoList({ page: contentData.page, pageSize: contentData.pageSize, isAdmin: 1 })">加载更多</a-button>
      </div>
      <div v-else-if="!contentData.hasMore" class="no-more-data">没有更多数据了</div>
    </div>
  </div>
</template>

<script setup>
import {listApi as listClassificationList} from '/@/api/classification'
import {listApi as listTagList} from '/@/api/tag'
import {listApi as listVideoList} from '/@/api/video'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";
import PlayIcon from '/@/assets/images/Play.png'
import {getFormatTime} from '/@/utils/'

const userStore = useUserStore()
const router = useRouter()

const contentData = reactive({
  selectX: 0,
  selectTagId: -1,
  cData: [],
  selectedKeys: [],
  tagData: [],
  loading: false,
  tabData: ['最新', '最热', '推荐'],
  selectTabIndex: 0,
  tabUnderLeft: 12,
  videoData: [],
  pageData: [],
  page: 1,
  total: 0,
  pageSize: 12,
  hasMore: true,   // 是否还有更多数据
})

onMounted(() => {
  initSider()
  getVideoList({ isAdmin: 1,page: 1,pageSize: 12 })
})

const initSider = async () => {
  contentData.cData.push({ key: '-1', title: '全部' })
  try {
    const classificationRes = await listClassificationList()
    classificationRes.data.list.forEach(item => {
      item.key = item.id
      contentData.cData.push(item)
    })
    const tagRes = await listTagList()
    contentData.tagData = tagRes.data.list
  } catch (err) {
    console.log('初始化侧边栏数据失败', err)
  }
}

const getSelectedKey = () => {
  return contentData.selectedKeys.length > 0 ? contentData.selectedKeys[0] : -1
}

const onSelect = (selectedKeys) => {
  contentData.selectedKeys = selectedKeys
  if (selectedKeys.length > 0) {
    contentData.page = 1; // 重置页数为1
    contentData.pageData = []; // 清空已有的视频数据
    contentData.hasMore = true;
    getVideoList({ c: getSelectedKey(), isAdmin: 1,page: 1,pageSize: 12 })
  }
  contentData.selectTagId = -1
}

const clickTag = (index) => {
  contentData.selectedKeys = []
  contentData.selectTagId = index
  contentData.page = 1; // 重置页数为1
  contentData.pageData = []; // 清空已有的视频数据
  contentData.hasMore = true;
  getVideoList({ tag: contentData.selectTagId, isAdmin: 1,page: 1,pageSize: 12 })
}

const selectTab = (index) => {
  contentData.selectTabIndex = index
  contentData.tabUnderLeft = 12 + 50 * index
  contentData.page = 1; // 重置页数为1
  contentData.pageSize = 12;
  contentData.pageData = []; // 清空已有的视频数据
  contentData.hasMore = true;
  const sort = index === 0 ? 'recent' : index === 1 ? 'hot' : 'recommend'
  const data = { sort, isAdmin: 1,page:1,pageSize:12 }

  if (contentData.selectTagId !== -1) {
    data.tag = contentData.selectTagId
  } else {
    data.c = getSelectedKey()
  }

  getVideoList(data)
}

const handleDetail = (item) => {
  const { href } = router.resolve({ name: 'detail', query: { id: item.id } })
  window.open(href, '_blank')
}

const getVideoList = async (data) => {
  contentData.loading = true
  try {
    // 发送请求，加载当前页的视频数据
    const res = await listVideoList(data);
    res.data.list.forEach(item => {
      if (item.cover) {
        item.cover = `${BASE_URL}/api/staticfiles/image/${item.cover}`
      }
    })
    // 将新数据追加到当前显示的视频列表中
    contentData.pageData = [...contentData.pageData, ...res.data.list];
    contentData.total = res.data.total;

    // 检查是否加载完所有数据
    if (contentData.pageData.length >= contentData.total) {
      contentData.hasMore = false;  // 没有更多数据了
    }
    contentData.page += 1;  // 增加页码，准备加载下一页
  } catch (err) {
    console.log('获取数据失败', err)
  } finally {
    contentData.loading = false
  }
}

</script>

<style scoped lang="less">
/* 页面主布局，增加网页的整体边距 */
.content {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0 20px; /* 页面与边框的间距 */
  margin: 80px auto;
  box-sizing: border-box; /* 确保 padding 不影响布局 */
}

/* 第一层布局：视频分类和视频标签 */
.top-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.left-search-item {
  flex: 1;
  margin-right: 32px;
}

h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 10px;
}

/* 视频分类横向排列 */
.category-view {
  display: flex;
  flex-wrap: wrap;
}

.category-item {
  background: #fff;
  border: 1px solid #a1adc6;
  border-radius: 16px;
  padding: 0 8px;
  margin: 8px 8px 0 0;
  cursor: pointer;
  font-size: 12px;
}

.category-select {
  background: #4684e3;
  color: #fff;
}

/* 标签样式 */
.tag-view {
  display: flex;
  flex-wrap: wrap;
}

.tag {
  background: #fff;
  border: 1px solid #a1adc6;
  border-radius: 16px;
  padding: 0 8px;
  margin: 8px 8px 0 0;
  cursor: pointer;
  font-size: 12px;
}

.tag-select {
  background: #4684e3;
  color: #fff;
}

/* 第二层布局：视频内容展示 */
.content-right {
  padding-top: 12px;
}

.top-select-view {
  margin-bottom: 16px;
}

.order-view {
  display: flex;
}

.tab {
  margin-right: 20px;
  cursor: pointer;
  color: #999;
}

.tab-select {
  color: #152844;
  font-weight: 600;
}

/* 视频列表 */
.pc-video-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

.video-item {
  width: calc((100% - 64px) / 4); /* 每行展示4个视频卡片 */
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding-bottom: 16px;
  border-radius: 8px; /* 增加圆角 */
  background: #fff; /* 增加背景颜色 */
  box-sizing: border-box;
}

.img-view {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.img-view img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-top-left-radius: 8px; /* 配合卡片的圆角 */
  border-top-right-radius: 8px;
}

.info-view {
  padding: 10px;
}

.video-name {
  font-weight: bold;
  font-size: 15px;
  margin-bottom: 8px;
}

.no-data {
  text-align: center;
  padding: 20px;
  color: #999;
}

/* 分页样式 */
.page-view {
  text-align: center;
  margin-top: 20px;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}
.no-more-data {
  text-align: center;
  margin-top: 20px;
  color: #999;
}
</style>


