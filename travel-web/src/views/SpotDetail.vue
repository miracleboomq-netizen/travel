<template>
  <div class="detail-wrapper" v-loading="loading">
    <template v-if="spot && spot.id">
      <div class="breadcrumb-area">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>景区详情</el-breadcrumb-item>
          <el-breadcrumb-item>{{ spot.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <el-row :gutter="30" class="main-layout">
        <el-col :span="16">
          <el-card class="content-card" :body-style="{ padding: '0px' }">
            <div class="image-banner">
              <img :src="spot.imageUrl" class="main-img" />
              <div class="image-overlay">
                <h1 class="spot-title">{{ spot.name }}</h1>
                <div class="spot-meta">
                  <el-tag type="danger" effect="dark" size="small">热门</el-tag>
                  <span class="rating-text">推荐指数：⭐⭐⭐⭐⭐</span>
                </div>
              </div>
            </div>
            
            <div class="detail-body">
              <h3 class="section-title"><el-icon><InfoFilled /></el-icon> 景区介绍</h3>
              <p class="description">{{ spot.description }}</p>

              <h3 class="section-title" style="margin-top: 30px;"><el-icon><Guide /></el-icon> 官方推荐攻略</h3>
              <div 
                v-if="spot.strategy" 
                class="strategy-content rich-text-container" 
                v-html="spot.strategy"
              ></div>
              <el-empty v-else description="暂无深度攻略，敬请期待" :image-size="60" />
            </div>
          </el-card>

          <el-card class="comment-card">
            <template #header>
              <div class="card-header">
                <span class="bold">游客互动 ({{ comments.length }})</span>
              </div>
            </template>
            
            <div class="post-comment">
              <div v-if="replyTo" class="reply-tag">
                正在回复 @{{ replyTo.username }} 
                <el-icon class="close-icon" @click="replyTo = null"><Close /></el-icon>
              </div>
              <el-input 
                v-model="newCommentText" 
                type="textarea" 
                :rows="3" 
                placeholder="分享你的游玩心得..." 
              />
              <div class="post-footer">
                <el-button type="primary" @click="postComment">发表评价</el-button>
              </div>
            </div>

            <div class="comment-list">
              <div 
                v-for="item in comments" 
                :key="item.id" 
                :class="['comment-item', item.parentId ? 'reply-item' : '']"
              >
                <el-avatar :size="40" :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${item.username}`" />
                <div class="c-right">
                  <div class="c-user">
                    <span>{{ item.username }}</span>
                    <span v-if="item.parentId" class="reply-info">回复了游客</span>
                  </div>
                  <div class="c-text">{{ item.content }}</div>
                  <div class="c-ops">
                    <span class="c-time">{{ item.createTime ? new Date(item.createTime).toLocaleString() : '刚刚' }}</span>
                    <el-button type="primary" link @click="handleReply(item)">回复TA</el-button>
                  </div>
                </div>
              </div>
              <el-empty v-if="comments.length === 0" description="暂无游客评价，快来抢沙发吧！" />
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <div class="sticky-side">
            <el-card class="booking-card">
              <div class="price-box">
                <span class="label">票价：</span>
                <span class="amount">免费预约</span>
              </div>
              <el-button type="success" size="large" class="booking-btn" @click="handleReserve">
                立即预约入园
              </el-button>
              <p class="hint-text">预约成功后请前往“个人中心”出示二维码</p>
            </el-card>
            
            <el-card class="map-card">
              <template #header>
                <div class="map-header">
                  <span class="bold">GIS 路线规划</span>
                  <el-radio-group v-model="travelMode" size="small" @change="planRoute">
                    <el-radio-button value="driving">驾车</el-radio-button>
                    <el-radio-button value="walking">步行</el-radio-button>
                  </el-radio-group>
                </div>
              </template>
              
              <div id="baiduMap"></div>
              
              <div id="routeResults" class="route-panel">
                <p v-if="!spot || !spot.name" class="map-placeholder">正在加载地图数据...</p>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </template>
    
    <el-empty v-else description="景区信息加载中或已不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { InfoFilled, Guide, Close } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const spot = ref(null)
const comments = ref([])
const newCommentText = ref('')
const replyTo = ref(null)
const travelMode = ref('driving')
const loading = ref(false)

// 🌟 核心改进：把加载评论的代码独立出来，不和地图逻辑绑定
const loadComments = async () => {
  try {
    const spotId = route.params.id
    const res = await axios.get(`http://localhost:8080/api/comment/list/${spotId}`)
    if (res.data.code === 200) {
      comments.value = res.data.data || []
    }
  } catch (error) {
    console.error("加载评论失败", error)
  }
}

// 初始化加载数据
const loadAllData = async () => {
  loading.value = true
  try {
    const spotId = route.params.id
    
    // 1. 获取景区核心详细信息
    const resSpot = await axios.get(`http://localhost:8080/api/spot/${spotId}`)
    spot.value = resSpot.data.data
    
    if (spot.value) planRoute()
    
    // 2. 独立调用加载评论
    await loadComments()
    
  } catch (e) {
    ElMessage.error("部分数据加载失败，请检查后端服务")
  } finally {
    loading.value = false
  }
}

// GIS 路线规划核心业务逻辑
const planRoute = () => {
  if (!spot.value || !spot.value.name) return

  const drawMap = () => {
    const mapContainer = document.getElementById("baiduMap")
    if (!mapContainer) return

    const map = new window.BMap.Map("baiduMap")
    const point = new window.BMap.Point(112.239, 30.335)
    map.centerAndZoom(point, 12)
    map.enableScrollWheelZoom(true)
    
    const routePanel = document.getElementById("routeResults")
    if (routePanel) routePanel.innerHTML = ''

    const options = { 
      renderOptions: { 
        map: map, 
        panel: "routeResults", 
        autoViewport: true 
      },
      onSearchComplete: () => {
        if (service.getStatus() !== window.BMAP_STATUS_SUCCESS) {
          if (routePanel) routePanel.innerHTML = `<div style="padding:20px;color:#909399;text-align:center">百度地图未能匹配到“${spot.value.name}”的精确路线。</div>`
        }
      }
    }

    const service = travelMode.value === 'driving' 
      ? new window.BMap.DrivingRoute(map, options) 
      : new window.BMap.WalkingRoute(map, options)
    
    service.search("荆州站", spot.value.name)
  }

  nextTick(() => {
    if (typeof window.BMap !== 'undefined') {
      drawMap()
      return
    }

    if (document.getElementById('baidu-map-script')) return

    window.initBaiduMapCallback = () => {
      drawMap()
    }
    
    const script = document.createElement('script')
    script.id = 'baidu-map-script'
    script.type = 'text/javascript'
    script.src = 'https://api.map.baidu.com/api?v=3.0&ak=E4805d16520de693a3fe707cdc962045&callback=initBaiduMapCallback'
    script.onerror = () => {
      ElMessage.error('百度地图引擎加载失败，请检查网络设置')
    }
    document.body.appendChild(script)
  })
}

// 门票预约业务处理
const handleReserve = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录系统账户后再进行预约')
    return router.push('/login')
  }

  ElMessageBox.confirm(`确认预约景区【${spot.value.name}】的门票吗？`, '预约入园提示', {
    confirmButtonText: '确定预约',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      const res = await axios.post('http://localhost:8080/api/order/add', { 
        userId: user.id, 
        spotId: spot.value.id, 
        spotName: spot.value.name 
      })
      if (res.data.code === 200) {
        ElMessage.success('预约成功！入园二维码已发送至“个人中心”')
      } else {
        ElMessage.error(res.data.msg || '预约失败，该时段可能已满')
      }
    } catch (e) {
      ElMessage.error('网络连接异常，预约请求未发出')
    }
  }).catch(() => {})
}

// 评论系统交互逻辑
const handleReply = (comment) => {
  replyTo.value = comment
  newCommentText.value = `@${comment.username} `
  document.querySelector('.post-comment').scrollIntoView({ behavior: 'smooth' })
}

const postComment = async () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) return ElMessage.warning('请先登录后再发表游玩评价')
  if (!newCommentText.value.trim()) return ElMessage.warning('请输入有效的评价内容')

  try {
    await axios.post('http://localhost:8080/api/comment/add', {
      content: newCommentText.value,
      userId: user.id,
      username: user.username,
      spotId: spot.value.id,
      parentId: replyTo.value ? replyTo.value.id : null
    })
    ElMessage.success('评价发表成功')
    newCommentText.value = ''
    replyTo.value = null
    
    // 🌟 核心改进：发表完成后，只刷新评论区，不刷新整个页面和地图！
    await loadComments() 
    
  } catch (e) {
    ElMessage.error('评论发表失败，请稍后重试')
  }
}

onMounted(loadAllData)
</script>

<style scoped>
.detail-wrapper { max-width: 1200px; margin: 0 auto; padding: 20px 20px 60px; }
.breadcrumb-area { margin-bottom: 20px; }

/* 详情卡片视觉设计 */
.image-banner { position: relative; height: 400px; overflow: hidden; border-radius: 12px 12px 0 0; }
.main-img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
.image-overlay { position: absolute; bottom: 0; left: 0; right: 0; padding: 30px; background: linear-gradient(transparent, rgba(0,0,0,0.85)); color: white; }
.spot-title { margin: 0 0 10px 0; font-size: 2.2rem; font-weight: bold; }
.detail-body { padding: 40px; }

/* 核心内容排版 */
.section-title { font-size: 1.3rem; margin-bottom: 20px; color: #303133; display: flex; align-items: center; gap: 10px; border-left: 4px solid #409eff; padding-left: 15px; }
.description { line-height: 1.8; color: #606266; font-size: 16px; margin-bottom: 30px; }
.rich-text-container { background: #fcfcfc; padding: 25px; border-radius: 8px; border: 1px solid #ebeef5; line-height: 2; color: #444; }
:deep(.rich-text-container img) { max-width: 100%; height: auto; border-radius: 8px; margin: 15px 0; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
:deep(.rich-text-container p) { margin-bottom: 1.2em; }

/* 评价模块视觉设计 */
.comment-card { margin-top: 30px; border-radius: 12px; }
.post-comment { margin-bottom: 30px; }
.reply-tag { background: #eef5fe; padding: 8px 15px; font-size: 13px; margin-bottom: 10px; border-radius: 4px; color: #409eff; display: flex; align-items: center; justify-content: space-between; width: fit-content; }
.close-icon { cursor: pointer; margin-left: 10px; }
.post-footer { margin-top: 15px; text-align: right; }
.comment-list { border-top: 1px solid #f2f6fc; padding-top: 20px; }
.comment-item { display: flex; gap: 20px; padding: 20px 0; border-bottom: 1px solid #f2f6fc; }
.reply-item { margin-left: 60px; background: #fafafa; padding: 20px; border-radius: 12px; margin-top: 10px; border-bottom: none; }
.c-user { font-weight: bold; color: #409eff; margin-bottom: 8px; }
.reply-info { font-weight: normal; color: #909399; margin-left: 12px; font-size: 12px; }
.c-text { line-height: 1.6; color: #555; }
.c-ops { margin-top: 12px; display: flex; justify-content: space-between; align-items: center; font-size: 12px; }
.c-time { color: #999; }

/* 右侧粘性布局侧边栏 */
.sticky-side { position: sticky; top: 20px; }
.booking-card { margin-bottom: 25px; border-radius: 12px; text-align: center; }
.price-box { margin-bottom: 20px; }
.price-box .amount { font-size: 2rem; color: #f56c6c; font-weight: bold; }
.booking-btn { width: 100%; height: 50px; font-size: 1.1rem; border-radius: 25px; }
.hint-text { font-size: 12px; color: #909399; margin-top: 12px; }

/* GIS 地图渲染样式 */
.map-card { border-radius: 12px; }
.map-header { display: flex; justify-content: space-between; align-items: center; }
#baiduMap { width: 100%; height: 320px; border-radius: 8px; margin-top: 10px; }
.route-panel { max-height: 300px; overflow-y: auto; font-size: 13px; margin-top: 15px; border-top: 1px solid #f2f6fc; padding-top: 10px; }
.map-placeholder { text-align: center; color: #909399; padding-top: 50px; }
</style>