<template>
  <div class="home-container">
    <div class="hero-section">
      <el-carousel height="450px" motion-blur>
        <el-carousel-item v-for="(img, index) in banners" :key="index">
          <div class="banner-box" :style="{ backgroundImage: `url(${img.url})` }">
            <div class="banner-content">
              <h1>{{ img.title }}</h1>
              <p>{{ img.sub }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <div class="search-floating">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索目的地、景区名称..." 
          class="custom-search"
          clearable
          @clear="fetchSpots"
        >
          <template #append>
            <el-button @click="fetchSpots" icon="Search">立即搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>

    <div class="main-layout">
      <el-row :gutter="30">
        <el-col :span="18">
          <div class="section-header">
            <h2 class="title-with-line">精选景区推荐</h2>
            
            <el-radio-group v-model="category" size="small" @change="fetchSpots">
              <el-radio-button value="全部">全部</el-radio-button>
              <el-radio-button value="自然风光">自然风光</el-radio-button>
              <el-radio-button value="人文古迹">人文古迹</el-radio-button>
              <el-radio-button value="休闲娱乐">休闲娱乐</el-radio-button>
            </el-radio-group>
          </div>

          <el-row :gutter="20" v-loading="loading">
            <el-col :span="8" v-for="spot in spots" :key="spot.id" style="margin-bottom: 25px;">
              <el-card :body-style="{ padding: '0px' }" shadow="hover" class="spot-card" @click="goToDetail(spot.id)">
                <div class="image-wrapper">
                  <img :src="spot.imageUrl" class="spot-img" />
                  <div class="spot-tag">热门</div>
                </div>
                <div class="spot-info">
                  <h3 class="spot-name">{{ spot.name }}</h3>
                  <p class="spot-desc">{{ spot.description }}</p>
                  <div class="spot-footer">
                    <el-button type="danger" size="small" round @click.stop="handleQuickReserve(spot)">
                      快捷预约
                    </el-button>
                    <el-button type="primary" link icon="ArrowRight">查看详情</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <div class="pagination">
            <el-pagination 
              background 
              layout="prev, pager, next" 
              :total="total" 
              :page-size="pageSize"
              v-model:current-page="pageNum"
              @current-change="fetchSpots"
            />
          </div>
        </el-col>

        <el-col :span="6">
          <el-card class="side-card weather-card">
            <div class="weather-info">
              <div class="city">实时天气：荆州市</div>
              <div class="temp">22°C</div>
              <div class="desc">多云转晴 · 适宜旅游</div>
            </div>
          </el-card>

          <el-card class="side-card notice-card">
            <template #header><div class="side-title">📢 景区动态</div></template>
            <div class="notice-item" v-for="(n, i) in notices" :key="i">
              <span class="dot"></span>
              <p>{{ n }}</p>
            </div>
          </el-card>

          <div class="ad-banner">
            <img src="https://picsum.photos/300/150?random=88" />
            <div class="ad-mask">
              <span>数字化导览系统 · 已全面上线</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <footer class="footer">
      <p>© 2026 智慧旅游系统 | 数字化景区毕业设计演示版</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api'
import { Search, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const spots = ref([])
const loading = ref(false)
const searchQuery = ref('')
const category = ref('全部')
const pageNum = ref(1)
const pageSize = ref(6)
const total = ref(0)

const banners = [
  { url: 'https://picsum.photos/1200/450?random=11', title: '数字博物馆', sub: '感受科技与历史的碰撞' },
  { url: 'https://picsum.photos/1200/450?random=12', title: '荆楚古城', sub: '领略千年古都的魅力' },
  { url: 'https://picsum.photos/1200/450?random=13', title: '智慧湿地', sub: '人与自然和谐共生' }
]

const notices = [
  '关于荆楚古城景区的限流公告',
  '智慧导览系统离线地图下载指南',
  '荆州博物馆“数字展厅”开放时间调整',
  '文明旅游，从我做起：游客倡议书'
]

const fetchSpots = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/spot/list', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: searchQuery.value === '全部' ? '' : searchQuery.value
      }
    })
    spots.value = res.data.data.records
    total.value = res.data.data.total
  } catch (e) {
    console.error("加载列表失败", e)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => {
  router.push(`/spot/${id}`)
}

// 🌟 核心：首页一键快捷预约逻辑
const handleQuickReserve = (spot) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录账户后预约')
    return router.push('/login')
  }
  
  ElMessageBox.confirm(`确认直接预约【${spot.name}】的门票吗？`, '快捷预约确认', {
    confirmButtonText: '确定预约',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      await axios.post('/api/order/add', {
        userId: user.id,
        spotId: spot.id,
        spotName: spot.name
      })
      ElMessage.success('预约成功！请前往“个人中心”查看入园二维码')
    } catch (e) {
      ElMessage.error('系统繁忙，预约失败')
    }
  }).catch(() => {
    // 点击取消不做任何操作
  })
}

onMounted(fetchSpots)
</script>

<style scoped>
.home-container { background-color: #f8fafc; min-height: 100vh; }
.hero-section { position: relative; }
.banner-box { height: 100%; background-size: cover; background-position: center; display: flex; align-items: center; justify-content: center; color: white; text-align: center; }
.banner-content h1 { font-size: 3rem; margin-bottom: 10px; text-shadow: 2px 2px 10px rgba(0,0,0,0.5); }
.banner-content p { font-size: 1.2rem; letter-spacing: 2px; }

.search-floating { position: absolute; bottom: -30px; left: 50%; transform: translateX(-50%); width: 600px; z-index: 10; }
.custom-search :deep(.el-input__wrapper) { border-radius: 30px 0 0 30px; height: 60px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
.custom-search :deep(.el-input-group__append) { border-radius: 0 30px 30px 0; background-color: #409eff; color: white; border: none; padding: 0 30px; }

.main-layout { padding: 80px 10% 40px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.title-with-line { border-left: 5px solid #409eff; padding-left: 15px; font-size: 1.5rem; }

.spot-card { cursor: pointer; transition: transform 0.3s, box-shadow 0.3s; border-radius: 12px; overflow: hidden; }
.spot-card:hover { transform: translateY(-10px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
.image-wrapper { position: relative; height: 180px; }
.spot-img { width: 100%; height: 100%; object-fit: cover; }
.spot-tag { position: absolute; top: 10px; right: 10px; background: #f56c6c; color: white; padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.spot-info { padding: 15px; }
.spot-name { margin: 0 0 10px; font-size: 1.2rem; font-weight: bold; }
.spot-desc { color: #909399; font-size: 0.9rem; height: 40px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; text-overflow: ellipsis; }
.spot-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 15px; border-top: 1px solid #f0f0f0; padding-top: 10px; }

.side-card { margin-bottom: 20px; border-radius: 12px; }
.weather-card { background: linear-gradient(135deg, #409eff, #79bbff); color: white; text-align: center; border: none; }
.city { font-size: 1.1rem; }
.temp { font-size: 2.5rem; font-weight: bold; }
.desc { font-size: 0.9rem; margin-top: 5px; }

.notice-item { display: flex; align-items: flex-start; gap: 8px; margin-bottom: 12px; cursor: pointer; }
.notice-item p { margin: 0; font-size: 0.85rem; color: #606266; line-height: 1.5; }
.dot { width: 6px; height: 6px; background: #409eff; border-radius: 50%; margin-top: 6px; flex-shrink: 0; }

.ad-banner { position: relative; border-radius: 12px; overflow: hidden; cursor: pointer; height: 150px; margin-top: 20px; }
.ad-banner img { width: 100%; height: 100%; object-fit: cover; display: block; }
.ad-mask { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; }
.ad-mask span { color: white; font-weight: bold; text-align: center; padding: 0 20px; font-size: 1rem; }

.pagination { margin-top: 30px; display: flex; justify-content: center; }
.footer { text-align: center; padding: 40px; color: #909399; font-size: 0.9rem; background: #fff; margin-top: 40px; border-top: 1px solid #eee; }
</style>