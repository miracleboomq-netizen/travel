<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="left-content">
          <h2>探索视界</h2>
          <h2>发现美好</h2>
          <p>欢迎来到智慧旅游大数据服务平台</p>
        </div>
      </div>

      <div class="login-right">
        <div class="form-header">
          <h2 class="title">{{ isLogin ? '欢迎回来' : '创建新账户' }}</h2>
          <p class="subtitle">{{ isLogin ? '请登录您的账户以继续体验智慧导览' : '注册加入平台，开启您的专属旅程' }}</p>
        </div>

        <el-tabs v-model="activeTab" class="custom-tabs" @tab-change="resetForm">
          <el-tab-pane label="用户登录" name="login"></el-tab-pane>
          <el-tab-pane label="新用户注册" name="register"></el-tab-pane>
        </el-tabs>

        <el-form :model="form" class="login-form">
          <el-form-item>
            <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
          </el-form-item>
          
          <el-form-item>
            <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password size="large" />
          </el-form-item>
          
          <el-form-item v-if="!isLogin">
            <el-input v-model="form.phone" placeholder="请输入手机号 (选填)" prefix-icon="Iphone" size="large" />
          </el-form-item>

          <el-button type="primary" class="submit-btn" size="large" @click="handleSubmit" :loading="loading">
            {{ isLogin ? '立即登录' : '确认注册' }}
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Iphone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const activeTab = ref('login')
const loading = ref(false)

const isLogin = computed(() => activeTab.value === 'login')

const form = ref({
  username: '',
  password: '',
  phone: ''
})

const resetForm = () => {
  form.value = { username: '', password: '', phone: '' }
}

const handleSubmit = async () => {
  if (!form.value.username || !form.value.password) {
    return ElMessage.warning('用户名和密码不能为空！')
  }
  
  loading.value = true
  try {
    const url = isLogin.value ? 'http://localhost:8080/api/user/login' : 'http://localhost:8080/api/user/register'
    const res = await axios.post(url, form.value)

    if (res.data.code === 200) {
      if (isLogin.value) {
        ElMessage.success('登录成功')
        localStorage.setItem('user', JSON.stringify(res.data.data))
        // 延迟跳转，给用户一个反馈时间
        setTimeout(() => {
          router.push('/')
          setTimeout(() => window.location.reload(), 100) // 刷新以更新导航栏状态
        }, 500)
      } else {
        ElMessage.success('注册成功，请直接登录')
        activeTab.value = 'login' // 注册成功后自动切回登录页面
      }
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('服务器异常，请检查后端运行状态')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); /* 清新明亮的渐变背景 */
  padding: 20px;
}

.login-box {
  display: flex;
  width: 900px;
  height: 500px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 左侧品牌区 */
.login-left {
  flex: 1;
  background: linear-gradient(135deg, #409eff 0%, #3a7bd5 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}
.login-left::before {
  content: '';
  position: absolute;
  top: -50%; right: -50%; bottom: -50%; left: -50%;
  background: url('https://picsum.photos/800/800?random=100') center/cover;
  opacity: 0.2; /* 微微透出旅游背景图 */
  z-index: 0;
}
.left-content {
  position: relative;
  z-index: 1;
  text-align: left;
}
.left-content h2 {
  font-size: 2.5rem;
  margin: 0 0 10px;
  letter-spacing: 2px;
}
.left-content p {
  font-size: 1.1rem;
  margin-top: 20px;
  opacity: 0.8;
}

/* 右侧表单区 */
.login-right {
  flex: 1;
  padding: 50px 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #ffffff;
}
.form-header {
  margin-bottom: 25px;
}
.title {
  font-size: 1.8rem;
  color: #303133;
  margin: 0 0 8px;
}
.subtitle {
  font-size: 0.9rem;
  color: #909399;
  margin: 0;
}

.custom-tabs {
  margin-bottom: 20px;
}
:deep(.el-tabs__item) {
  font-size: 1.1rem;
}

.login-form {
  margin-top: 10px;
}
:deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none !important;
  border: 1px solid transparent;
  transition: all 0.3s;
}
:deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  background-color: #ffffff;
}

.submit-btn {
  width: 100%;
  margin-top: 20px;
  height: 48px;
  font-size: 1.1rem;
  border-radius: 8px;
  letter-spacing: 1px;
}
</style>