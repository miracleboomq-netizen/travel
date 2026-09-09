import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  // 1. 公开展示页面（所有人均可访问）
  { 
    path: '/', 
    name: 'home', 
    component: () => import('../views/HomeView.vue'),
    meta: { title: '智旅出行-首页' }
  },
  { 
    path: '/login', 
    name: 'login', 
    component: () => import('../views/LoginView.vue'),
    meta: { title: '用户登录' }
  },
  { 
    path: '/spot/:id', 
    name: 'spotDetail', 
    component: () => import('../views/SpotDetail.vue'),
    meta: { title: '景区详情' }
  },
  
  // 2. 个人中心（必须登录后访问）
  { 
    path: '/user', 
    name: 'user', 
    component: () => import('../views/UserCenter.vue'),
    meta: { title: '个人中心' }
  },

  // 3. 后台管理系统（仅限 admin 账号访问）
  { 
    path: '/admin', 
    name: 'admin', 
    component: () => import('../views/AdminView.vue'),
    meta: { title: '管理后台' }
  },
  { 
    path: '/dashboard', 
    name: 'dashboard', 
    component: () => import('../views/DashboardView.vue'),
    meta: { title: '数据监控大屏' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

/**
 * 🌟 核心拦截守卫：智慧旅游系统“门禁”
 */
router.beforeEach((to, from, next) => {
  // 动态设置浏览器标签页标题
  if (to.meta.title) {
    document.title = to.meta.title
  }

  // 获取本地存储的登录用户信息
  const userStr = localStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null

  // 定义白名单：无需登录即可访问的页面名称
  const whiteList = ['home', 'login', 'spotDetail']

  // 逻辑 1：基础身份校验
  // 如果去的不是白名单页面且用户未登录，强制跳转到登录页
  if (!whiteList.includes(to.name) && !user) {
    ElMessage.warning('请先登录账户以解锁更多智慧服务')
    next({ name: 'login' })
    return
  }

  // 逻辑 2：权限分级校验 (RBAC 思想)
  // 如果目标页面是后台管理(admin)或数据大屏(dashboard)
  if (to.name === 'admin' || to.name === 'dashboard') {
    // 如果用户未登录，或者登录了但用户名不是 'admin'
    if (!user || user.username !== 'admin') {
      ElMessage.error('权限不足！该模块仅供系统管理员操作')
      next({ name: 'home' }) // 将普通用户拦截并重定向到首页
      return
    }
  }

  // 逻辑 3：放行
  next()
})

export default router