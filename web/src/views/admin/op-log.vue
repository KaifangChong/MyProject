<template>
  <div>
    <!--页面区域-->
    <div class="page-view">
      <a-table
          size="middle"
          rowKey="id"
          :loading="data.loading"
          :data-source="data.dataList"
          :scroll="{ x: 'max-content' }"
          :pagination="{
            size: 'default',
            current: data.page,
            pageSize: data.pageSize,
            total: data.total,  // 总条目数
            onChange: (current) => {
              data.page = current;
              getDataList();  // 切换页码时重新获取数据
            },
            showSizeChanger: true,
            onShowSizeChange: (current, size) => {
              data.page = current;
              data.pageSize = size;
              getDataList();  // 切换每页条目数时重新获取数据
            },
            showTotal: (total) => `共${total}条数据`,
          }"
      >
        <a-table-column title="序号" dataIndex="index" key="index" align="center" />
        <a-table-column title="请求方式" dataIndex="method" key="method" align="center" />
        <a-table-column title="请求URL" dataIndex="url" key="url" align="center" />
        <a-table-column title="操作IP" dataIndex="ip" key="ip" align="center" />
        <a-table-column title="操作时间" dataIndex="time" key="time" align="center" />
        <a-table-column title="耗时" dataIndex="accessTime" key="accessTime" align="center">
          <template #default="{ text }">{{ text }}ms</template>
        </a-table-column>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FormInstance, message } from 'ant-design-vue';
import { listOpLogListApi } from '/@/api/log';

// 页面数据
const data = reactive({
  dataList: [],
  loading: false,
  keyword: '',
  selectedRowKeys: [] as any[],
  pageSize: 10,
  page: 1,
  total: 0
});


onMounted(() => {
  getDataList();
});

const getDataList = () => {
  data.loading = true;
  listOpLogListApi({
    keyword: data.keyword,
    page: data.page,       // 当前页
    pageSize: data.pageSize,  // 每页条数
  })
      .then((res) => {
        data.loading = false;
        console.log(res);
        res.data.list.forEach((item: any, index: any) => {
          item.index = index + 1;
        });
        data.dataList = res.data.list;
        data.total = res.data.total;
      })
      .catch((err) => {
        data.loading = false;
        console.log(err);
      });
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
