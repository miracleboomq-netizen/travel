<template>
  <div class="user-center-wrapper">
    <el-row :gutter="24">
      <el-col :span="6">
        <el-card class="profile-card" shadow="hover">
          <div class="avatar-container">
            <el-upload
              class="avatar-uploader"
              :action="`${API_BASE_URL}/api/upload`"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              name="file"
            >
              <el-avatar 
                :size="100" 
                fit="cover"
                :src="userInfo.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${userInfo.username}`" 
                class="avatar-img"
              />
              <div class="avatar-hover-mask">
                <el-icon><Camera /></el-icon>
              </div>
            </el-upload>
            <div class="role-tag">
              <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'success'" effect="dark" round>
                {{ userInfo.role === 'admin' ? '管理员' : '普通游客' }}
              </el-tag>
            </div>
          </div>
          
          <h2 class="username">{{ userInfo.username }}</h2>
          <div class="user-info-list">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>{{ userInfo.realName || '未实名认证' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span>{{ userInfo.phone || '未绑定手机' }}</span>
            </div>
          </div>

          <el-divider />
          
          <div class="stats-container">
            <div class="stat-item">
              <div class="stat-num">{{ orders.length }}</div>
              <div class="stat-label">预约总数</div>
            </div>
            <div class="stat-item">
              <div class="stat-num" style="color: #67c23a;">{{ completedOrders }}</div>
              <div class="stat-label">已核销</div>
            </div>
          </div>

          <el-button type="danger" plain class="logout-btn" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 安全退出
          </el-button>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card class="main-content-card" shadow="hover">
          <el-tabs v-model="activeTab" class="user-tabs">
            
            <el-tab-pane label="我的预约订单" name="orders">
              <template #label>
                <span class="tab-label"><el-icon><Ticket /></el-icon> 门票订单管理</span>
              </template>
              
              <el-table :data="orders" stripe style="width: 100%" v-loading="loadingOrders">
                <el-table-column prop="id" label="订单号" width="80" align="center" />
                <el-table-column prop="spotName" label="预约景区" min-width="180" />
                <el-table-column prop="orderDate" label="预约时间" min-width="180">
                  <template #default="scope">
                    {{ new Date(scope.row.orderDate).toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100" align="center">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === '已核销' ? 'info' : 'success'">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="核销操作" width="180" align="center">
                  <template #default="scope">
                    <el-button 
                      v-if="scope.row.status === '已预约'"
                      type="success" 
                      size="small" 
                      @click="showQrCode(scope.row.id)"
                    >
                      出示电子票
                    </el-button>
                    <div v-else style="display: flex; align-items: center; justify-content: center; gap: 10px;">
                      <span style="color: #909399; font-size: 13px;">凭证已失效</span>
                      <el-button type="danger" size="small" plain @click="handleDeleteOrder(scope.row.id)">删除</el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="旅客实名资料" name="edit">
              <template #label>
                <span class="tab-label"><el-icon><Postcard /></el-icon> 旅客实名资料</span>
              </template>
              
              <div class="edit-form-container">
                <el-alert title="为保障顺利入园及保险购买，请填写真实旅客信息。" type="info" show-icon style="margin-bottom: 20px;" />
                
                <el-form :model="editForm" label-width="100px" style="max-width: 500px;">
                  <el-form-item label="登录账号">
                    <el-input v-model="editForm.username" disabled />
                  </el-form-item>
                  
                  <el-form-item label="真实姓名">
                    <el-input v-model="editForm.realName" placeholder="如：李明 (用于实名核销)" />
                  </el-form-item>

                  <el-form-item label="身份证号">
                    <el-input v-model="editForm.idCard" placeholder="请输入18位有效身份证号" />
                  </el-form-item>

                  <el-form-item label="性别">
                    <el-radio-group v-model="editForm.gender">
                      <el-radio value="男">男</el-radio>
                      <el-radio value="女">女</el-radio>
                      <el-radio value="保密">保密</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  
                  <el-form-item label="联系手机">
                    <el-input v-model="editForm.phone" placeholder="用于接收预约短信提醒" />
                  </el-form-item>

                  <el-form-item label="重置密码">
                    <el-input v-model="editForm.password" type="password" placeholder="若不修改密码请留空" show-password />
                  </el-form-item>

                  <el-form-item>
                    <el-button type="primary" @click="saveProfile">保存实名资料</el-button>
                    <el-button @click="resetForm">恢复原数据</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>

          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="qrDialogVisible" title="入园电子凭证" width="350px" center>
      <div class="qr-code-wrapper">
        <p class="qr-hint">请向景区工作人员出示此二维码核销</p>
        <div class="qr-box">
          <qrcode-vue :value="currentQrCodeValue" :size="200" level="H" />
        </div>
        <p class="qr-order-num">核销码：{{ currentQrCodeValue }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Phone, Calendar, SwitchButton, Ticket, Edit, Camera, User, Postcard } from '@element-plus/icons-vue'
import QrcodeVue from 'qrcode.vue'
import axios, { API_BASE_URL } from '../api'

const router = useRouter()

const activeTab = ref('orders')
const userInfo = reactive({
  id: '',
  username: '',
  phone: '',
  role: '',
  avatar: '',
  realName: '',
  idCard: '',
  gender: ''
})
const editForm = reactive({
  username: '',
  phone: '',
  password: '',
  realName: '',
  idCard: '',
  gender: ''
})

const orders = ref([])
const loadingOrders = ref(false)
const qrDialogVisible = ref(false)
const currentQrCodeValue = ref('')

const completedOrders = computed(() => {
  return orders.value.filter(order => order.status === '已核销').length
})

const initData = () => {
  const userStr = localStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('登录状态已失效，请重新登录')
    router.push('/login')
    return
  }
  
  const user = JSON.parse(userStr)
  Object.assign(userInfo, user)
  
  editForm.username = user.username
  editForm.phone = user.phone || ''
  editForm.realName = user.realName || ''
  editForm.idCard = user.idCard || ''
  editForm.gender = user.gender || '保密'
  editForm.password = ''

  loadUserOrders(user.id)
}

const loadUserOrders = async (userId) => {
  loadingOrders.value = true
  try {
    const res = await axios.get(`/api/order/user/${userId}`)
    orders.value = res.data.data || []
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loadingOrders.value = false
  }
}

// 🌟 新增：删除订单核心方法
const handleDeleteOrder = (orderId) => {
  ElMessageBox.confirm(
    '确定要清理这条历史订单吗？删除后将不可恢复。',
    '清理订单提示',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const res = await axios.delete(`/api/order/delete/${orderId}`)
      if (res.data.code === 200) {
        ElMessage.success('订单已成功清理！')
        // 删除成功后重新加载当前用户的订单列表
        loadUserOrders(userInfo.id)
      } else {
        ElMessage.error(res.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('网络错误，删除请求失败')
    }
  }).catch(() => {
    // 用户点击取消，静默处理
  })
}

// 保存修改逻辑
const saveProfile = async () => {
  try {
    const payload = {
      id: userInfo.id,
      phone: editForm.phone,
      realName: editForm.realName,
      idCard: editForm.idCard,
      gender: editForm.gender
    }
    if (editForm.password.trim()) {
      payload.password = editForm.password
    }

    const res = await axios.put('/api/user/update', payload)
    
    if (res.data.code === 200) {
      ElMessage.success('实名资料更新成功！')
      
      // 更新本地状态
      Object.assign(userInfo, payload)
      const updatedUser = { ...JSON.parse(localStorage.getItem('user')), ...payload }
      localStorage.setItem('user', JSON.stringify(updatedUser))
      
      if (editForm.password.trim()) {
        ElMessageBox.alert('安全校验：密码已修改，请重新登录', '提示', {
          confirmButtonText: '立即重新登录',
          callback: () => handleLogout()
        })
      }
    } else {
      ElMessage.error(res.data.msg || '修改失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，修改请求未发送，请检查后端是否提供 update 接口')
  }
}

const resetForm = () => {
  editForm.phone = userInfo.phone
  editForm.realName = userInfo.realName
  editForm.idCard = userInfo.idCard
  editForm.gender = userInfo.gender
  editForm.password = ''
}

const showQrCode = (orderId) => {
  currentQrCodeValue.value = String(orderId)
  qrDialogVisible.value = true
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出当前账号吗？', '安全退出', {
    confirmButtonText: '确定退出',
    cancelButtonText: '暂不',
    type: 'warning',
  }).then(() => {
    localStorage.removeItem('user')
    ElMessage.success('已安全退出')
    router.push('/login')
  }).catch(() => {})
}

const beforeAvatarUpload = (file) => {
  const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isJPGOrPNG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
  }
  if (!isLt10M) {
    ElMessage.error('头像图片大小不能超过 10MB!')
  }
  return isJPGOrPNG && isLt10M
}

const handleAvatarSuccess = async (res) => {
  console.log("👉 后端返回的完整数据：", res)
  
  let newAvatarUrl = ''
  if (res.data && typeof res.data === 'string' && res.data.startsWith('http')) {
    newAvatarUrl = res.data
  }
  
  if (newAvatarUrl) {
    userInfo.avatar = newAvatarUrl
    try {
      await axios.put('/api/user/update', {
        id: userInfo.id,
        avatar: newAvatarUrl
      })
      const updatedUser = { ...JSON.parse(localStorage.getItem('user')), avatar: newAvatarUrl }
      localStorage.setItem('user', JSON.stringify(updatedUser))
      ElMessage.success('头像更换成功！')
    } catch (e) {
      ElMessage.warning('图片已替换，但同步到数据库时出现异常')
    }
  } else {
    ElMessage.error(res.msg || '上传成功，但未能获取到图片地址')
  }
}

onMounted(initData)
</script>

<style scoped>
.user-center-wrapper { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
.profile-card { text-align: center; border-radius: 12px; padding-bottom: 20px; }
.avatar-container { position: relative; margin-top: 20px; display: inline-block; cursor: pointer; }
.avatar-uploader { display: block; }
.avatar-img { border: 4px solid #ecf5ff; box-shadow: 0 4px 12px rgba(0,0,0,0.1); background-color: #f0f2f5; transition: all 0.3s; }
.avatar-hover-mask { position: absolute; top: 0; left: 0; width: 100%; height: 100px; border-radius: 50%; background: rgba(0, 0, 0, 0.5); color: white; display: flex; justify-content: center; align-items: center; font-size: 28px; opacity: 0; transition: opacity 0.3s; }
.avatar-container:hover .avatar-hover-mask { opacity: 1; }
.role-tag { position: absolute; bottom: -10px; left: 50%; transform: translateX(-50%); white-space: nowrap; z-index: 10; }
.username { margin: 20px 0 10px; font-size: 1.5rem; color: #303133; }
.user-info-list { margin: 20px 0; color: #606266; font-size: 14px; }
.info-item { display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 12px; }
.stats-container { display: flex; justify-content: space-around; margin: 20px 0 30px; }
.stat-item { text-align: center; }
.stat-num { font-size: 1.5rem; font-weight: bold; color: #409eff; }
.stat-label { font-size: 12px; color: #909399; margin-top: 5px; }
.logout-btn { width: 80%; border-radius: 20px; }
.main-content-card { border-radius: 12px; min-height: 500px; }
.tab-label { display: flex; align-items: center; gap: 5px; font-size: 15px; }
.edit-form-container { padding: 30px; }
.qr-code-wrapper { text-align: center; }
.qr-hint { color: #67c23a; font-weight: bold; margin-bottom: 20px; }
.qr-box { background: #fff; padding: 15px; display: inline-block; border: 1px solid #ebeef5; border-radius: 8px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); }
.qr-order-num { margin-top: 20px; font-size: 18px; color: #303133; letter-spacing: 2px; }
</style>