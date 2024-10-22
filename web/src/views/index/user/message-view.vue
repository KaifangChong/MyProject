<template>
  <div class="content-list">
    <div class="list-title">我的消息</div>
    <a-spin :spinning="loading" style="min-height: 200px;">
      <div class="list-content">
      <div class="notification-view">
        <div class="list">
          <div class="notification-item flex-view" v-for="item in msgData">
            <img
              :src="MessageIconImage"
              class="avatar">
            <div class="content-box">
              <div class="header">
                <span class="title-txt">{{item.title}}</span>
                <span class="time">{{ getFormatTime(item.createTime, true) }}</span>
              </div>
              <div class="head-text">
              <span class="name" style="display: none;">
              </span>
              </div>
              <div class="content">
                <p>{{ item.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </a-spin>
  </div>
</template>

<script setup>
import {listApi} from '/@/api/notice'
import MessageIconImage from '/@/assets/images/setting-msg-icon.svg'
import {getFormatTime} from '/@/utils'

const loading = ref(false)
const msgData = ref([])

onMounted(() => {
  getMessageList()
})

const getMessageList = async () => {
  loading.value = true
  try {
    const res = await listApi()
    msgData.value = res.data.list
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}

</script>
<style scoped lang="less">
progress {
  vertical-align: baseline;
}

.flex-view {
  display: flex;
}

input, textarea {
  outline: none;
  border-style: none;
}

button {
  padding: 0;
}

.content-list {
  flex: 1;
}

.notification-item {
  padding-top: 16px;

  .avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    margin-right: 8px;
  }

  .content-box {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    border-bottom: 1px dashed #e9e9e9;
    padding: 4px 0 16px;
  }

  .header {
    margin-bottom: 12px;
  }

  .title-txt {
    color: #315c9e;
    font-weight: 500;
    font-size: 14px;
  }

  .time {
    color: #a1adc5;
    font-size: 14px;
    margin-left: 16px;
  }

  .head-text {
    color: #152844;
    font-weight: 500;
    font-size: 14px;
    line-height: 22px;

    .name {
      margin-right: 8px;
    }
  }

  .content {
    margin-top: 4px;
    color: #484848;
    font-size: 14px;
    line-height: 22px;
  }

}

</style>
