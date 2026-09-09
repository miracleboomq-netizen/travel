<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h1 class="title">智慧旅游大数据实时监控平台</h1>
      <p class="subtitle">实时数据更新中：{{ currentTime }}</p>
    </div>

    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
          <div class="stat-label">累计预约总量</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-value color-today">{{ statistics.todayCount || 0 }}</div>
          <div class="stat-label">今日新增预约</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-value color-checkin">{{ statistics.checkInCount || 0 }}</div>
          <div class="stat-label">入园游客总数</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <template #header><div class="chart-title"><el-icon><BarChart /></el-icon> 景区预约热度排名</div></template>
          <div id="barChart" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <template #header><div class="chart-title"><el-icon><PieChart /></el-icon> 预约分布比例</div></template>
          <div id="pieChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from '../api'
import * as echarts from 'echarts'
import { PieChart as PieIcon, Histogram as BarChart } from '@element-plus/icons-vue'

const currentTime = ref(new Date().toLocaleString())
const statistics = ref({})
let barChart = null
let pieChart = null

// 1. 获取后端统计数据
const loadData = async () => {
  try {
    const res = await axios.get('/api/order/statistics')
    if (res.data.code === 200) {
      statistics.value = res.data.data
      // 数据获取成功后，初始化/更新图表
      initBarChart(res.data.data.ranking)
      initPieChart(res.data.data.ranking)
    }
  } catch (error) {
    console.error("大屏数据加载失败", error)
  }
}

// 2. 初始化柱状图
const initBarChart = (data) => {
  const chartDom = document.getElementById('barChart')
  if (!chartDom) return
  if (!barChart) barChart = echarts.init(chartDom, 'dark')
  
  const option = {
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis' },
    xAxis: { 
      type: 'category', 
      data: data.map(item => item.name),
      axisLabel: { interval: 0, rotate: 30 }
    },
    yAxis: { type: 'value' },
    series: [{
      data: data.map(item => item.value),
      type: 'bar',
      itemStyle: { color: '#409eff' },
      barWidth: '40%'
    }]
  }
  barChart.setOption(option)
}

// 3. 初始化饼图
const initPieChart = (data) => {
  const chartDom = document.getElementById('pieChart')
  if (!chartDom) return
  if (!pieChart) pieChart = echarts.init(chartDom, 'dark')

  const option = {
    backgroundColor: 'transparent',
    tooltip: { trigger: 'item' },
    legend: { bottom: '0', textStyle: { color: '#fff' } },
    series: [{
      name: '预约量',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#1a1a2e', borderWidth: 2 },
      label: { show: false },
      data: data
    }]
  }
  pieChart.setOption(option)
}

// 自动更新时间
let timer = null
onMounted(() => {
  loadData()
  timer = setInterval(() => {
    currentTime.value = new Date().toLocaleString()
  }, 1000)
  
  // 窗口缩放时自适应图表
  window.addEventListener('resize', () => {
    barChart && barChart.resize()
    pieChart && pieChart.resize()
  })
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  background-color: #0b1222;
  color: white;
  padding: 20px 40px;
}
.dashboard-header {
  text-align: center;
  margin-bottom: 40px;
}
.title {
  font-size: 32px;
  letter-spacing: 4px;
  color: #409eff;
  margin-bottom: 10px;
}
.subtitle {
  color: #8c939d;
  font-size: 14px;
}

/* 核心指标卡片 */
.stat-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(64, 158, 255, 0.2);
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s;
}
.stat-card:hover {
  background: rgba(64, 158, 255, 0.1);
  transform: translateY(-5px);
}
.stat-value {
  font-size: 48px;
  font-weight: bold;
  font-family: 'Arial';
  margin-bottom: 10px;
}
.color-today { color: #67c23a; }
.color-checkin { color: #e6a23c; }
.stat-label {
  color: #909399;
  font-size: 16px;
}

/* 图表区域 */
.chart-row { margin-top: 30px; }
.chart-card {
  background: rgba(255, 255, 255, 0.02) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
}
.chart-title {
  color: #409eff;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}
.chart-box {
  width: 100%;
  height: 350px;
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
</style>