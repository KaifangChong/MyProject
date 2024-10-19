<template>
  <a-spin :spinning="showSpin">
    <div class="main">
      <a-card title="最近一周访问量">
        <div class="chart-container" ref="visitChartDiv"></div>
      </a-card>

      <a-row :gutter="[20, 20]">
        <a-col :xs="24" :sm="24" :md="24" :lg="12">
          <a-card title="热门视频排名" class="chart-card">
            <div class="chart-container" ref="barChartDiv"></div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="24" :md="24" :lg="12">
          <a-card title="热门分类比例" class="chart-card">
            <div class="chart-container" ref="pieChartDiv"></div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </a-spin>
</template>


<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { listApi } from '/@/api/overview';

let showSpin = ref(true);

const visitChartDiv = ref();
const barChartDiv = ref();
const pieChartDiv = ref();

let visitChart, barChart, pieChart;

let tdata = reactive({
  data: {}
});

onMounted(() => {
  list();
  window.onresize = () => {
    visitChart?.resize();
    barChart?.resize();
    pieChart?.resize();
  };
});

const list = async () => {
  try {
    const res = await listApi({});
    tdata.data = res.data;
    initCharts();
    initBarChart();
    initPieChart();
  } catch (err) {
    console.error(err);
  } finally {
    showSpin.value = false;
  }
};

const initCharts = () => {
  const xData = tdata.data.visitList.map(item => item.day);
  const uvData = tdata.data.visitList.map(item => item.uv);
  const pvData = tdata.data.visitList.map(item => item.pv);

  visitChart = echarts.init(visitChartDiv.value);
  visitChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['IP', 'visit'], top: '90%', left: 'center' },
    xAxis: { type: 'category', data: xData },
    yAxis: { type: 'value' },
    series: [
      { name: 'IP', type: 'line', data: uvData },
      { name: 'visit', type: 'line', data: pvData }
    ]
  });
};

const initBarChart = () => {
  const xData = tdata.data.popularVideos.map(item => item.title);
  const yData = tdata.data.popularVideos.map(item => item.count);

  barChart = echarts.init(barChartDiv.value);
  barChart.setOption({
    title: { text: '热门视频排名', x: 'center' },
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        let content = '';
        params.forEach(item => {
          content += `观看次数: ${item.value}<br/>`;
        });
        return content;
      }
    },
    xAxis: { type: 'category', data: xData, axisLabel: { rotate: 30 } },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: yData }]
  });
};

const initPieChart = () => {
  const pieData = tdata.data.popularClassifications.map(item => ({
    name: item.title,
    value: item.count
  }));

  pieChart = echarts.init(pieChartDiv.value);
  pieChart.setOption({
    title: { text: '热门视频分类', x: 'center' },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: { top: '90%', left: 'center' },
    series: [
      {
        name: '分类',
        type: 'pie',
        radius: ['40%', '70%'],
        data: pieData
      }
    ]
  });
};
</script>


<style lang="less" scoped>
.main {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chart-container {
  height: 300px;
}

.chart-card {
  flex: 1;
}
</style>
