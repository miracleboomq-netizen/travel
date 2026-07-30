<template>
  <div class="admin-layout">
    <el-menu :default-active="activeMenu" class="sidebar" @select="m => activeMenu = m" background-color="#304156" text-color="#fff">
      <div class="brand">智旅管理中心</div>
      <el-menu-item index="spot"><el-icon><Location /></el-icon> 景区内容管理</el-menu-item>
      <el-menu-item index="user"><el-icon><User /></el-icon> 用户账号管理</el-menu-item>
      <el-menu-item index="comment"><el-icon><ChatDotRound /></el-icon> 游客评论审核</el-menu-item>
      <el-menu-item index="order"><el-icon><Ticket /></el-icon> 订单核销管理</el-menu-item>
    </el-menu>

    <div class="main-content">
      
      <el-card v-if="activeMenu === 'spot'" class="box-card">
        <template #header>
          <div class="card-header">
            <span class="bold">景区资源列表</span>
            <el-button type="primary" size="small" @click="openAdd">新增景区</el-button>
          </div>
        </template>
        <el-table :data="spots" stripe v-loading="loading">
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column prop="name" label="景区名称" width="150" />
          <el-table-column label="预览图" width="120">
            <template #default="s">
              <el-image :src="s.row.imageUrl" style="width:80px;height:45px;border-radius:4px" fit="cover" />
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" show-overflow-tooltip />
          <el-table-column label="操作" width="180">
            <template #default="s">
              <el-button type="warning" size="small" @click="openEdit(s.row)">编辑</el-button>
              <el-popconfirm title="确定删除吗？" @confirm="handleDeleteSpot(s.row.id)">
                <template #reference><el-button type="danger" size="small">删除</el-button></template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card v-if="activeMenu === 'user'" class="box-card">
        <template #header><span class="bold">系统注册用户管理</span></template>
        <el-table :data="users" stripe v-loading="loading">
          <el-table-column prop="id" label="UID" width="80" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="phone" label="手机号" />
          <el-table-column label="角色">
            <template #default="s">
              <el-tag :type="s.row.username === 'admin' ? 'danger' : 'success'">
                {{ s.row.username === 'admin' ? '管理员' : '普通用户' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="s">
              <el-button type="danger" size="small" :disabled="s.row.username === 'admin'" @click="handleDeleteUser(s.row.id)">封禁账号</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card v-if="activeMenu === 'comment'" class="box-card">
        <template #header><span class="bold">游客互动评论审核</span></template>
        <el-table :data="comments" stripe v-loading="loading">
          <el-table-column prop="username" label="评价人" width="120" />
          <el-table-column prop="content" label="评论内容" show-overflow-tooltip />
          <el-table-column prop="createTime" label="发布时间" width="180">
             <template #default="s">{{ new Date(s.row.createTime).toLocaleString() }}</template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="s">
              <el-button type="danger" size="small" @click="handleDeleteComment(s.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="comments.length === 0" description="暂无待审核评论" />
      </el-card>

      <el-card v-if="activeMenu === 'order'" class="box-card">
        <template #header><span class="bold">景区验票核销台</span></template>
        <div style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center; background: #f0f7ff; padding: 20px; border-radius: 8px;">
          <el-input 
            v-model="verifyOrderId" 
            placeholder="请使用扫码枪扫码，或手动输入订单号" 
            size="large" 
            style="width: 300px;" 
            clearable
          />
          <el-button type="success" size="large" @click="handleVerify(verifyOrderId)">立即核销</el-button>
        </div>
        <el-table :data="orders" stripe v-loading="loading">
          <el-table-column prop="id" label="订单号" width="100" />
          <el-table-column prop="spotName" label="预约景区" />
          <el-table-column prop="orderDate" label="预约日期" />
          <el-table-column label="状态">
            <template #default="s">
              <el-tag :type="s.row.status === '已核销' ? 'info' : 'success'">
                {{ s.row.status || '已预约' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="s">
              <el-button type="primary" size="small" :disabled="s.row.status === '已核销'" @click="handleVerify(s.row.id)">
                {{ s.row.status === '已核销' ? '已验票' : '手动验票' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="orders.length === 0" description="暂无门票订单" />
      </el-card>

    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑景区信息' : '新增景区资源'" width="800px" top="5vh">
      <el-form :model="form" label-width="100px">
        <el-form-item label="景区名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="form.imageUrl" />
        </el-form-item>
        <el-form-item label="景区介绍">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        
        <el-form-item label="官方推荐攻略">
          <div style="border: 1px solid #dcdfe6; border-radius: 4px; z-index: 100; width: 100%;">
            <Toolbar
              style="border-bottom: 1px solid #dcdfe6"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 350px; overflow-y: hidden;"
              v-model="form.strategy"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// 🌟 引入 WangEditor 核心样式和 Vue 组件
import '@wangeditor/editor/dist/css/style.css'
import { ref, shallowRef, onMounted, watch, onBeforeUnmount } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import { Location, User, ChatDotRound, Ticket } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// --- 基础数据 ---
const activeMenu = ref('spot')
const loading = ref(false)
const spots = ref([])
const users = ref([])
const comments = ref([])
const orders = ref([])
const verifyOrderId = ref('')
const dialogVisible = ref(false)
const form = ref({ id: null, name: '', imageUrl: '', description: '', strategy: '' })

// --- 富文本编辑器配置 ---
const editorRef = shallowRef() // 必须使用 shallowRef，防止 Vue 响应式引发的性能问题
const mode = 'default'
const toolbarConfig = { excludeKeys: ['fullScreen'] } // 可以排除不需要的工具栏按钮
const editorConfig = { placeholder: '请在此输入图文并茂的专属游玩攻略...' }

// 记录 editor 实例
const handleCreated = (editor) => {
  editorRef.value = editor
}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

// --- 业务逻辑 ---
const loadData = async () => {
  loading.value = true
  try {
    if (activeMenu.value === 'spot') {
      const res = await axios.get('http://localhost:8080/api/admin/spots')
      spots.value = res.data.data
    } else if (activeMenu.value === 'user') {
      const res = await axios.get('http://localhost:8080/api/admin/users')
      users.value = res.data.data
    } else if (activeMenu.value === 'comment') {
      const res = await axios.get('http://localhost:8080/api/admin/comments')
      comments.value = res.data.data
    } else if (activeMenu.value === 'order') {
      const res = await axios.get('http://localhost:8080/api/admin/orders')
      orders.value = res.data.data || []
    }
  } catch (e) {
    ElMessage.error("数据加载失败")
  } finally {
    loading.value = false
  }
}

watch(activeMenu, () => { loadData() })

const handleVerify = async (id) => {
  if (!id) return ElMessage.warning('请输入有效的订单号')
  try {
    const res = await axios.put(`http://localhost:8080/api/admin/order/verify/${id}`)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      verifyOrderId.value = '' 
      loadData() 
    } else {
      ElMessage.error(res.data.msg) 
    }
  } catch (error) {
    ElMessage.error('核销失败，请检查网络或后端接口')
  }
}

const openAdd = () => { 
  form.value = { id: null, name: '', imageUrl: '', description: '', strategy: '' }
  dialogVisible.value = true 
}

const openEdit = (row) => { 
  form.value = { ...row }
  dialogVisible.value = true 
}

const submitForm = async () => {
  const isEdit = !!form.value.id
  const url = isEdit ? 'http://localhost:8080/api/admin/spot/update' : 'http://localhost:8080/api/admin/spot/add'
  await axios[isEdit ? 'put' : 'post'](url, form.value)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  loadData()
}

const handleDeleteSpot = async (id) => {
  await axios.delete(`http://localhost:8080/api/admin/spot/delete/${id}`)
  loadData()
}

const handleDeleteUser = async (id) => {
  await axios.delete(`http://localhost:8080/api/admin/user/delete/${id}`)
  loadData()
}

const handleDeleteComment = async (id) => {
  await axios.delete(`http://localhost:8080/api/admin/comment/delete/${id}`)
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.admin-layout { display: flex; height: 100vh; background: #f0f2f5; }
.sidebar { width: 220px; }
.brand { color: white; text-align: center; padding: 25px; font-weight: bold; border-bottom: 1px solid #1f2d3d; }
.main-content { flex: 1; padding: 20px; overflow-y: auto; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.bold { font-weight: bold; }
/* 修正一下全屏时的样式层级问题 */
:deep(.w-e-full-screen-container) { z-index: 9999; }
</style>