<template>
  <div class="content-list">
    <div class="list-title">我的喜欢</div>
    <div role="tablist" class="list-tabs-view flex-view">
    </div>
    <div class="list-content">
      <div class="collect-video-view">
        <div class="video-list flex-view">
          <div class="video-item item-column-3" v-for="(item,index) in wishData" :key="index">
            <div class="remove" @click="handleRemove(item)">移出</div>
            <div class="img-view" @click="handleClickItem(item)">
              <img :src="item.cover">
            </div>
            <div class="info-view">
              <h3 class="video-name">{{item.title}}</h3>
            </div>
          </div>
          <template v-if="!wishData || wishData.length <= 0">
            <a-empty style="width: 100%;margin-top: 200px;"/>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {message} from 'ant-design-vue';
import {unWishApi, userWishListApi} from '/@/api/videoWish'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const wishData = ref([])

onMounted(() => {
  getWishVideoList()
})

const handleClickItem = (record) => {
  const { href } = router.resolve({ name: 'detail', query: { id: record.video_id } })
  window.open(href, '_blank')
}

const handleRemove = async (record) => {
  try {
    await unWishApi({ id: record.id })
    message.success('移除成功')
    getWishVideoList()
  } catch (err) {
    console.log(err)
  }
}

const getWishVideoList = async () => {
  const userId = userStore.user_id
  if (!userId) return

  try {
    const res = await userWishListApi({ userId })
    res.data.forEach(item => {
      item.cover = `${BASE_URL}/api/staticfiles/image/${item.cover}`
    })
    wishData.value = res.data
  } catch (err) {
    console.log(err?.msg || '获取愿望清单失败')
  }
}

</script>
<style scoped lang="less">
.video-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start; // 卡片靠左对齐

  .video-item {
    flex: 0 0 200px; // 固定卡片宽度为200px
    margin-right: 16px;
    margin-bottom: 16px;
    box-sizing: border-box;
  }
}
.video-item {
  position: relative;
  flex: 1;
  margin-right: 16px; // 缩小卡片之间的间距
  min-width: 200px; // 缩小卡片宽度
  max-width: 200px; // 缩小卡片宽度
  border-radius: 6px; // 圆角可以保持较小
  overflow: hidden;
  margin-top: 12px; // 减少卡片上方的间距
  cursor: pointer;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1); // 适当减小阴影
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-3px); // 减少悬停时上移的幅度
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); // 减少阴影的强度
  }

  .remove {
    position: absolute;
    right: 6px; // 缩小删除按钮的边距
    top: 6px;
    padding: 2px 8px; // 减小删除按钮的尺寸
    color: #fff;
    background: #ff4d4f;
    border-radius: 12px; // 适当减小圆角
    font-size: 10px; // 调小字体
    text-align: center;
    cursor: pointer;
    transition: background 0.3s ease;

    &:hover {
      background: #ff7875;
    }
  }

  .img-view {
    background: #eaf1f5;
    font-size: 0;
    text-align: center;
    height: 120px; // 缩小图片高度
    padding: 4px; // 减小内边距

    img {
      max-width: 100%;
      height: 100%;
      display: block;
      margin: 0 auto;
      border-radius: 4px;
      box-sizing: border-box;
    }
  }

  .info-view {
    background: #f6f9fb;
    text-align: center;
    padding: 8px; // 减少内边距

    h3 {
      color: #1c355a;
      font-weight: 500;
      font-size: 14px; // 缩小字体
      line-height: 18px; // 调整行高
      margin: 8px 0; // 缩小上下间距
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
  }
}
</style>
