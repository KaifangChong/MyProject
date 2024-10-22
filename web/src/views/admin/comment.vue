<template>
  <div>
    <!--页面区域-->
    <div class="page-view">
      <div class="table-operations">
        <a-space>
          <a-button @click="handleBatchDelete">批量删除</a-button>
          <span>用户名：</span>
          <a-input v-model:value="data.userNameInput" />
          <span>视频名称：</span>
          <a-input-search v-model:value="data.videoNameInput" enter-button @search="onSearch" />
        </a-space>
      </div>
      <a-table
          size="middle"
          rowKey="id"
          :loading="data.loading"
          :data-source="data.list"
          :scroll="{ x: 'max-content' }"
          :row-selection="rowSelection"
          :pagination="{
            size: 'default',
            current: data.page,
            pageSize: data.pageSize,
            total: data.total,  // 总条目数
            onChange: (current) => {
              data.page = current;
              getList();  // 切换页码时重新获取数据
            },
            showSizeChanger: true,
            onShowSizeChange: (current, size) => {
              data.page = current;
              data.pageSize = size;
              getList();  // 切换每页条目数时重新获取数据
            },
            showTotal: (total) => `共${total}条数据`,
          }"
      >
        <a-table-column title="序号" dataIndex="index" key="index" align="center" />
        <a-table-column title="用户" dataIndex="username" key="username" align="center" />
        <a-table-column title="视频名称" dataIndex="title" key="title" align="center" />
        <a-table-column title="评论内容" dataIndex="content" key="content" align="center" />
        <a-table-column title="评论时间" dataIndex="commentTime" key="commentTime" align="center">
          <template #default="{ text }">
            {{ getFormatTime(text, true) }}
          </template>
        </a-table-column>
        <a-table-column title="操作" key="operation" align="center" fixed="right" width="140">
          <template #default="{ record }">
            <a-divider type="vertical" />
            <a-popconfirm title="确定删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(record)">
              <a href="#">删除</a>
            </a-popconfirm>
          </template>
        </a-table-column>
      </a-table>
    </div>

  </div>
</template>

<script setup lang="ts">
import {FormInstance, message} from 'ant-design-vue';
import {createApi, listApi, deleteApi} from '/@/api/comment';
import {BASE_URL} from "/@/store/constants";
import {getFormatTime} from "/@/utils";

// 页面数据
const data = reactive({
  list: [],
  loading: false,
  currentAdminUserName: '',
  userNameInput: '',
  videoNameInput: '',
  selectedRowKeys: [] as any[],
  pageSize: 10,
  page: 1,
  total: 0
});

// 弹窗数据源
const modal = reactive({
  visile: false,
  editFlag: false,
  title: '',
});

onMounted(() => {
  getList();
});

const getList = () => {
  data.loading = true;
  listApi({
    userName: data.userNameInput,
    videoName: data.videoNameInput,
    page: data.page,       // 当前页
    pageSize: data.pageSize,  // 每页条数
  })
      .then((res) => {
        data.loading = false;
        console.log(res);
        res.data.list.forEach((item: any, index: any) => {
          item.index = index + 1;
          if (item.image) {
            item.image = BASE_URL + item.image
          }
        });
        data.list = res.data.list;
        data.total = res.data.total;
      })
      .catch((err) => {
        data.loading = false;
        console.log(err);
      });
};


const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    data.selectedRowKeys = selectedRowKeys;
  },
});

const confirmDelete = (record: any) => {
  console.log('delete', record);
  deleteApi({ids: record.id})
      .then((res) => {
        getList();
      })
      .catch((err) => {
        message.error(err.msg || '操作失败');
      });
};

const handleBatchDelete = () => {
  console.log(data.selectedRowKeys);
  if (data.selectedRowKeys.length <= 0) {
    console.log('hello');
    message.warn('请勾选删除项');
    return;
  }
  deleteApi({ids: data.selectedRowKeys.join(',')})
      .then((res) => {
        message.success('删除成功');
        data.selectedRowKeys = [];
        getList();
      })
      .catch((err) => {
        message.error(err.msg || '操作失败');
      });
};

const onSearch = () => {
  data.page = 1;
  getList();
};
</script>

<style scoped lang="less">
.page-view {
  min-height: 100%;
  background: #fff;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.table-operations {
  margin-bottom: 16px;
  text-align: right;
}

.table-operations > button {
  margin-right: 8px;
}
</style>
