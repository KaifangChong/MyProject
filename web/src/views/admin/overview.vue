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

// 控制加载动画显示的状态，初始为 true 表示加载中
let showSpin = ref(true);

// 获取页面中图表 DOM 的引用
const visitChartDiv = ref();
const barChartDiv = ref();
const pieChartDiv = ref();

// 定义图表实例
let visitChart, barChart, pieChart;

// 响应式数据对象，用于存储从接口获取的数据
let tdata = reactive({
  data: {}
});

// 组件挂载时调用，初始化数据和图表
onMounted(() => {
  list();
  // 监听窗口大小变化，调整图表的自适应尺寸
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
    // 初始化折线图、柱状图和饼图
    initCharts();
    initBarChart();
    initPieChart();
  } catch (err) {
    console.error(err);
  } finally {
    showSpin.value = false;
  }
};

// 初始化最近一周访问量折线图
const initCharts = () => {
  // 提取访问量数据，x 轴为日期，y 轴为访问量
  const xData = tdata.data.visitList.map(item => item.day);
  const uvData = tdata.data.visitList.map(item => item.uv);
  const pvData = tdata.data.visitList.map(item => item.pv);

  // 初始化 ECharts 图表实例
  visitChart = echarts.init(visitChartDiv.value);
  visitChart.setOption({
    tooltip: { trigger: 'axis' },// 提示框触发方式
    legend: { data: ['IP', 'visit'], top: '90%', left: 'center' },// 图例
    xAxis: { type: 'category', data: xData },// x 轴为日期
    yAxis: { type: 'value' },// y 轴为数值
    series: [
      { name: 'IP', type: 'line', data: uvData },// IP 数据曲线
      { name: 'visit', type: 'line', data: pvData }// 访问量 数据曲线
    ]
  });
};

// 初始化热门视频排名的柱状图
const initBarChart = () => {
  // 提取热门视频数据，x 轴为视频标题，y 轴为观看次数
  const xData = tdata.data.popularVideos.map(item => item.title);
  const yData = tdata.data.popularVideos.map(item => item.count);

  // 初始化 ECharts 图表实例
  barChart = echarts.init(barChartDiv.value);
  barChart.setOption({
    title: { text: '热门视频排名', x: 'center' },// 图表标题
    tooltip: {
      trigger: 'axis',// 提示框触发方式
      formatter: function (params) {
        // 格式化提示框内容，显示观看次数
        let content = '';
        params.forEach(item => {
          content += `观看次数: ${item.value}<br/>`;
        });
        return content;
      }
    },
    xAxis: { type: 'category', data: xData, axisLabel: { rotate: 30 } },// x 轴为视频标题
    yAxis: { type: 'value' },// y 轴为观看次数
    series: [{ type: 'bar', data: yData }]// 柱状图数据
  });
};

// 初始化热门分类比例的饼图
const initPieChart = () => {
  // 提取热门分类数据，生成饼图所需的 name 和 value
  const pieData = tdata.data.popularClassifications.map(item => ({
    name: item.title,
    value: item.count
  }));

  // 初始化 ECharts 图表实例
  pieChart = echarts.init(pieChartDiv.value);
  pieChart.setOption({
    title: { text: '热门视频分类', x: 'center' }, // 图表标题
    tooltip: {
      trigger: 'item',// 提示框触发方式
      formatter: '{a} <br/>{b}: {c} ({d}%)'// 格式化提示框，显示百分比
    },
    legend: { top: '90%', left: 'center' },// 图例位置
    series: [
      {
        name: '分类',// 系列名称
        type: 'pie',// 图表类型为饼图
        radius: ['40%', '70%'],// 设置内外半径，形成环形图
        data: pieData// 饼图数据
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
