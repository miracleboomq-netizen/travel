import axios from 'axios'

// API 基础地址：生产环境通过 VITE_API_BASE_URL 环境变量注入，本地开发默认指向 8080
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const http = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000
})

// 请求拦截器：自动携带登录用户信息（后端当前按用户名参数鉴权）
http.interceptors.request.use((config) => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      const user = JSON.parse(userStr)
      if (user?.username) config.headers['X-Username'] = user.username
    } catch {
      /* 忽略本地缓存的非法数据 */
    }
  }
  return config
})

/** 统一处理接口响应，失败时抛出错误并弹出提示 */
export async function request(config) {
  try {
    const res = await http(config)
    return res.data
  } catch (err) {
    return Promise.reject(err)
  }
}

export default http
