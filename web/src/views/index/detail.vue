<template>
  <div class="detail">
    <Header />

    <div class="detail-content">
      <!-- 第一层：视频播放器 -->
      <div class="video-layer">
        <video
            id="video-v"
            class="video"
            :src="detailData.raw"
            controls
            autoplay
            style="background-color: #000"
        ></video>
      </div>

      <!-- 第二层：视频介绍和按钮 -->
      <div class="info-layer">
        <!-- 视频介绍 -->
        <div class="infos">
          <div class="title">{{ detailData.title }}</div>
          <div class="meta">{{ detailData.pv }}次观看</div>
          <div class="desc">简介：{{ detailData.description }}</div>
        </div>

        <!-- 收藏和喜欢按钮 -->
        <div class="video-counts">
          <div class="count-item flex-view pointer" @click="collect()">
            <div class="count-img">
              <img :src="RecommendIcon" />
            </div>
            <div class="count-box flex-view">
              <div class="count-text-box">
                <span class="count-title">收藏</span>
              </div>
              <div class="count-num-box">
                <span class="num-text">{{ detailData.collectCount }}</span>
              </div>
            </div>
          </div>
          <div class="count-item flex-view pointer" @click="addToWish()">
            <div class="count-img">
              <img :src="WantIcon" />
            </div>
            <div class="count-box flex-view">
              <div class="count-text-box">
                <span class="count-title">喜欢</span>
              </div>
              <div class="count-num-box">
                <span class="num-text">{{ detailData.wishCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 第三层：评论区 -->
      <div class="detail-content-bottom">
        <div class="video-comment">
          <!-- 评论区代码 -->
          <div class="title">发表新的评论</div>
          <div class="publish flex-view">
            <img v-if="userStore.user_avatar" :src="userStore.user_avatar" class="mine-img" />
            <img v-else :src="AvatarIcon" class="mine-img" />
            <input placeholder="说点什么..." class="content-input" ref="commentRef" />
            <button class="send-btn" @click="sendComment()">发送</button>
          </div>
          <div class="tab-view flex-view">
            <div class="count-text">共有{{ commentData.length }}条评论</div>
            <div class="tab-box flex-view" v-if="commentData.length > 0">
              <span :class="sortIndex === 0 ? 'tab-select' : ''" @click="sortCommentList('recent')">最新</span>
              <div class="line"></div>
              <span :class="sortIndex === 1 ? 'tab-select' : ''" @click="sortCommentList('hot')">热门</span>
            </div>
          </div>
          <div class="comments-list">
            <!-- 评论列表 -->
            <div class="comment-item" v-for="item in commentData" :key="item.id">
              <div class="flex-item flex-view">
                <img v-if="item.avatar" :src="BASE_URL + '/api/staticfiles/avatar/' + item.avatar" class="avator" />
                <img v-else :src="AvatarIcon" class="avator" />
                <div class="person">
                  <div class="name">{{ item.username }}</div>
                  <div class="time">{{ item.commentTime }}</div>
                </div>
                <div class="float-right">
                  <span @click="like(item.id)">点赞</span>
                  <span class="num">{{ item.likeCount }}</span>
                </div>
              </div>
              <p class="comment-content">{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import {message} from "ant-design-vue";
import Header from '/@/views/index/components/header.vue'
import Footer from '/@/views/index/components/footer.vue'
import WantIcon from '/@/assets/images/want-read-hover.png';
import RecommendIcon from '/@/assets/images/recommend-hover.jpg';
import AvatarIcon from '/@/assets/images/avatar.jpg';
import {detailApi, listApi as listVideoList,} from '/@/api/video'
import {createApi as createCommentApi, likeApi, listVideoCommentsApi} from '/@/api/comment'
import {wishApi} from '/@/api/videoWish'
import {collectApi} from '/@/api/videoCollect'
import {BASE_URL} from "/@/store/constants";
import {useRoute, useRouter} from "vue-router/dist/vue-router";
import {useUserStore} from "/@/store";
import {getFormatTime} from "/@/utils";

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const videoId = ref('')
const detailData = ref({})

const commentData = ref([])
const sortIndex = ref(0)
const order = ref('recent') // 默认排序最新

const commentRef = ref(null)

onMounted(() => {
  videoId.value = route.query.id.trim()
  getVideoDetail()
  getCommentList()
})

const getVideoDetail = async () => {
  try {
    const res = await detailApi({ id: videoId.value })
    detailData.value = res.data
    detailData.value.cover = `${BASE_URL}/api/staticfiles/image/${detailData.value.cover}`
    detailData.value.raw = `${BASE_URL}/api/staticfiles/raw/${detailData.value.raw}`
  } catch (err) {
    message.error('获取详情失败')
  }
}

const addToWish = async () => {
  const userId = userStore.user_id
  if (userId) {
    try {
      const res = await wishApi({ videoId: videoId.value, userId })
      message.success(res.msg)
      getVideoDetail()
    } catch (err) {
      console.log('操作失败')
    }
  } else {
    message.warn('请先登录')
  }
}

const collect = async () => {
  const userId = userStore.user_id
  if (userId) {
    try {
      const res = await collectApi({ videoId: videoId.value, userId })
      message.success(res.msg)
      getVideoDetail()
    } catch (err) {
      console.log('收藏失败')
    }
  } else {
    message.warn('请先登录')
  }
}

const sendComment = async () => {
  const text = commentRef.value.value.trim()
  if (!text) return

  commentRef.value.value = ''

  const userId = userStore.user_id
  if (userId) {
    try {
      await createCommentApi({ content: text, videoId: videoId.value, userId })
      getCommentList()
    } catch (err) {
      console.log(err)
    }
  } else {
    message.warn('请先登录！')
    router.push({ name: 'login' })
  }
}

const like = async (commentId) => {
  try {
    await likeApi({ id: commentId })
    getCommentList()
  } catch (err) {
    console.log(err)
  }
}

const getCommentList = async () => {
  try {
    const res = await listVideoCommentsApi({ videoId: videoId.value, order: order.value })
    res.data.forEach(item => {
      item.commentTime = getFormatTime(item.commentTime, true)
    })
    commentData.value = res.data
  } catch (err) {
    console.log(err)
  }
}

const sortCommentList = (sortType) => {
  sortIndex.value = sortType === 'recent' ? 0 : 1
  order.value = sortType
  getCommentList()
}

</script>

<style scoped lang="less">
.detail-content {
  width: 100%;
  max-width: 1200px; /* 最大宽度 */
  margin: 4px auto;
}

.flex-view {
  display: flex;
}

.video-layer {
  /* 第一层：视频播放器 */
  margin-top: 16px;

  .video {
    width: 100%;
    height: calc(45vw - 48px);
    min-height: 250px;
    max-height: 600px; /* 限制最大高度 */
  }
}

.info-layer {
  /* 第二层：视频介绍和按钮 */
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
  flex-wrap: wrap; /* 使其在小屏设备下换行 */

  .infos {
    flex: 1;
    min-width: 300px;

    .title {
      font-size: 24px;
      font-weight: 400;
      color: #1e1e1e;
    }

    .meta {
      font-size: 12px;
      color: #1e1e1e;
      margin-top: 8px;
    }

    .desc {
      margin-top: 10px;
      font-size: 14px;
      color: #1e1e1e;
    }
  }

  .video-counts {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-left: 20px;
    flex-direction: row; /* 设置为横向排列 */
  }

  .count-item {
    display: flex;
    align-items: center;
    cursor: pointer;

    .count-img {
      width: 32px;
      margin-right: 12px;

      img {
        width: 100%;
      }
    }

    .count-box {
      display: flex;
      align-items: center;

      .count-title {
        font-size: 16px;
        color: #152844;
        font-weight: 600;
      }

      .num-text {
        font-size: 20px;
        color: #152844;
        font-weight: 600;
        margin-left: 8px;
      }
    }
  }
}

.detail-content-bottom {
  /* 第三层：评论区 */
  margin-top: 24px;

  .video-comment {
    width: 100%;

    .title {
      font-weight: 600;
      font-size: 14px;
      color: #152844;
      margin-bottom: 12px;
    }

    /* 评论区其他样式 */
    .publish {
      display: flex;
      align-items: center;
      margin-bottom: 24px;

      .mine-img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-right: 12px;
      }

      .content-input {
        flex: 1;
        height: 32px;
        padding: 5px 12px;
        border-radius: 4px;
        border: none;
        background: #f6f9fb;
        outline: none;
      }

      .send-btn {
        margin-left: 10px;
        width: 80px;
        height: 32px;
        background: #4684e2;
        color: #fff;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }
    }

    .tab-view {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .count-text {
        color: #484848;
      }

      .tab-box {
        display: flex;
        align-items: center;

        span {
          color: #5f77a6;
          cursor: pointer;
        }

        .tab-select {
          color: #152844;
          font-weight: bold;
        }

        .line {
          width: 1px;
          height: 12px;
          background: #cedce4;
          margin: 0 12px;
        }
      }
    }

    .comments-list {
      .comment-item {
        border-bottom: 1px dashed #cedce4;
        padding-bottom: 16px;
        margin-bottom: 16px;

        .flex-item {
          display: flex;
          align-items: center;

          .avator {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            margin-right: 12px;
          }

          .person {
            flex: 1;

            .name {
              font-weight: 600;
              font-size: 14px;
              color: #152844;
            }

            .time {
              font-size: 12px;
              color: #5f77a6;
              margin-top: 2px;
            }
          }

          .float-right {
            display: flex;
            align-items: center;
            color: #4684e2;

            span {
              margin-left: 12px;
              cursor: pointer;
            }

            .num {
              color: #152844;
              margin-left: 6px;
            }
          }
        }

        .comment-content {
          margin-left: 52px;
          margin-top: 8px;
          color: #484848;
          font-size: 14px;
          line-height: 22px;
        }
      }
    }
  }
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .info-layer {
    flex-direction: column; /* 小屏设备下，视频介绍和按钮垂直排列 */
  }

  .video-counts {
    justify-content: space-around; /* 按钮居中排列 */
  }
}
</style>

