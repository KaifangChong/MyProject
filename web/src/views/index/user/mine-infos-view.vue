<template>
  <div class="mine-infos-view">
    <div class="order-box">
      <div class="list">
        <div class="mine-item flex-view" @click="clickMenu('collectVideoView')">
          <img :src="MyCollectImg">
          <span>我的收藏</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('wishVideoView')">
          <img :src="MyWishImage">
          <span>我的喜欢</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('commentView')">
          <img :src="CommentIconImg">
          <span>我的评论</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('userInfoEditView')">
          <img :src="MyEditImage" alt="编辑资料">
          <span>编辑资料</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('securityView')">
          <img :src="SafeIconImage" alt="账号安全">
          <span>账号安全</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('messageView')">
          <img :src="MessageIconImage" alt="消息管理">
          <span>我的消息</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import MyCollectImg from '/@/assets/images/recommend-hover.jpg'
import CommentIconImg from '/@/assets/images/order-icon.svg'
import MyWishImage from '/@/assets/images/want-read-hover.png'
import MyEditImage from '/@/assets/images/setting-push-icon.svg'
import SafeIconImage from '/@/assets/images/setting-safe-icon.svg'
import MessageIconImage from '/@/assets/images/setting-msg-icon.svg'

import {userCollectListApi} from '/@/api/videoCollect'
import {userWishListApi} from '/@/api/videoWish'
import {useUserStore} from '/@/store';

const router = useRouter()
const userStore = useUserStore()

const collectCount = ref(0)
const wishCount = ref(0)

onMounted(() => {
  getCollectVideoList()
  getWishVideoList()
})

const clickMenu = (name) => {
  router.push({ name })
}

const getCollectVideoList = async () => {
  const userId = userStore.user_id
  if (!userId) return

  try {
    const res = await userCollectListApi({ userId })
    collectCount.value = res.data.length
  } catch (err) {
    console.log('获取收藏列表失败')
  }
}

const getWishVideoList = async () => {
  const userId = userStore.user_id
  if (!userId) return

  try {
    const res = await userWishListApi({ userId })
    wishCount.value = res.data.length
  } catch (err) {
    console.log('获取愿望清单失败')
  }
}

</script>

<style scoped lang="less">
.flex-view {
  display: flex;
}

.mine-infos-view {
  width: 235px;
  margin-right: 20px;
  border: 1px solid #cedce4;
  height: fit-content;

  .info-box {
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    padding: 16px 16px 0;
    overflow: hidden;
  }

  .avatar-img {
    width: 48px;
    height: 48px;
    margin-right: 16px;
    border-radius: 50%;
  }

  .name-box {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    overflow: hidden;

    .nick {
      color: #152844;
      font-weight: 600;
      font-size: 18px;
      line-height: 24px;
      height: 24px;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin: 0;
      overflow: hidden;
    }

    .age {
      font-size: 12px;
      color: #6f6f6f;
      height: 16px;
      line-height: 16px;
      margin-top: 8px;
    }

    .give-point {
      color: #4684e2;
      cursor: pointer;
      float: right;
    }
  }

  .counts-view {
    border: none;
    padding: 16px;
  }

  .counts {
    margin-top: 12px;
    text-align: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;

    .flex-item {
      -webkit-box-flex: 1;
      -ms-flex: 1;
      flex: 1;
      cursor: pointer;
    }

    .text {
      height: 16px;
      line-height: 16px;
      color: #6f6f6f;
    }

    .num {
      height: 18px;
      line-height: 18px;
      color: #152844;
      font-weight: 600;
      font-size: 14px;
      margin-top: 4px;
    }

    .split-line {
      width: 1px;
      height: 24px;
      background: #dae6f9;
    }
  }

  .intro-box {
    border-top: 1px solid #cedce4;
    padding: 16px;

    .title {
      color: #6f6f6f;
      font-size: 12px;
      line-height: 16px;
    }

    .intro-content {
      color: #152844;
      font-size: 14px;
      line-height: 20px;
      overflow: hidden;
      text-overflow: ellipsis;
      margin: 8px 0;
    }
  }

  .create-box {
    cursor: pointer;
    border-top: 1px solid #cedce4;
    padding: 16px;

    .title {
      position: relative;
      color: #152844;
      font-weight: 600;
      font-size: 14px;
      line-height: 18px;
      height: 18px;
    }

    .counts {
      margin-top: 12px;
      text-align: center;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;

      .flex-item {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        cursor: pointer;
      }

      .split-line {
        width: 1px;
        height: 24px;
        background: #dae6f9;
      }
    }
  }

  .order-box {
    border-top: 1px solid #cedce4;
    padding: 16px;

    .title {
      color: #152844;
      font-weight: 600;
      font-size: 14px;
      line-height: 18px;
      height: 18px;
      margin-bottom: 8px;
    }

    .list {
      padding-left: 16px;

      .mine-item {
        border-top: 1px dashed #cedce4;
        cursor: pointer;
        height: 48px;
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;

        img {
          width: 24px;
          margin-right: 8px;
          height: 24px;
        }

        span {
          color: #152844;
          font-size: 14px;
        }
      }

      .mine-item:first-child {
        border: none;
      }
    }
  }

  .setting-box {
    border-top: 1px solid #cedce4;
    padding: 16px;

    .title {
      color: #152844;
      font-weight: 600;
      font-size: 14px;
      line-height: 18px;
      height: 18px;
      margin-bottom: 8px;
    }

    .list {
      padding-left: 16px;
    }

    .mine-item {
      border-top: 1px dashed #cedce4;
      cursor: pointer;
      height: 48px;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;

      img {
        width: 24px;
        margin-right: 8px;
        height: 24px;
      }

      span {
        color: #152844;
        font-size: 14px;
      }
    }

    .mine-item:first-child {
      border: none;
    }
  }
}
</style>
