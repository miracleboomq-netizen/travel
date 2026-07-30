<template>
  <div id="app">
    <header class="main-header">
      <div class="logo-area" @click="goTo('/')">
        <el-icon :size="24" color="#409eff" style="margin-right: 10px;"><Location /></el-icon>
        <h2>智慧旅游系统</h2>
      </div>

      <nav class="nav-menu">
        <div class="nav-item" @click="goTo('/')">首页</div>
        <div class="nav-item" @click="goTo('/dashboard')">数据大屏</div>
        <div class="nav-item" @click="goTo('/user')">个人中心</div>
       <div class="nav-item admin-btn" v-if="isLoggedIn && currentUser.username === 'admin'" @click="goTo('/admin')">后台管理</div>
      </nav>

      <div class="user-area">
        <template v-if="isLoggedIn">
          <span class="welcome-text">欢迎您，{{ currentUser.nickname || currentUser.username }}</span>
          <el-button type="danger" size="small" plain @click="handleLogout">退出</el-button>
        </template>
        <template v-else>
          <el-button type="primary" @click="goTo('/login')">登录 / 注册</el-button>
        </template>
      </div>
    </header>

    <main class="main-content">
      <router-view :key="$route.fullPath"></router-view>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 登录状态变量
const isLoggedIn = ref(false)
const currentUser = ref({})

// 检查本地是否有用户数据
const checkLoginStatus = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    isLoggedIn.value = true
    currentUser.value = JSON.parse(userStr)
  } else {
    isLoggedIn.value = false
    currentUser.value = {}
  }
}

// 监听路由变化，每次切换页面都重新检查一下登录状态
watch(() => route.path, () => {
  checkLoginStatus()
})

// 页面加载时执行一次
onMounted(() => {
  checkLoginStatus()
})

// 页面跳转逻辑
const goTo = (path) => {
  router.push(path)
}

// 退出登录逻辑
const handleLogout = () => {
  localStorage.removeItem('user') // 清除缓存
  isLoggedIn.value = false
  ElMessage.success('已安全退出')
  router.push('/login') // 踢回登录页
}
</script>

<style>
/* 全局样式重置 */
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  background-color: #f5f7fa;
}

/* 顶部导航栏样式 */
.main-header {
  height: 60px;
  background-color: #ffffff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.logo-area {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #303133;
}
.logo-area h2 { margin: 0; font-size: 20px; }

.nav-menu {
  display: flex;
  gap: 30px;
}
.nav-item {
  cursor: pointer;
  font-size: 16px;
  color: #606266;
  font-weight: 500;
  transition: color 0.3s;
}
.nav-item:hover { color: #409eff; }
.admin-btn { color: #e6a23c; } /* 给后台管理按钮加点特殊颜色 */

.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
  min-width: 150px;
  justify-content: flex-end;
}
.welcome-text { font-size: 14px; color: #606266; }

.main-content {
  min-height: calc(100vh - 60px);
}
</style>