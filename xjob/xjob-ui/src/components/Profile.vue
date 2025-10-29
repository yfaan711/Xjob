<template>
  <div class="profile-container">
    <!-- 顶部导航栏 -->
    <header class="profile-header">
      <h1 class="profile-title">我的</h1>
      <div class="header-actions">
        <button class="settings-btn" @click="showSettings">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"/>
          </svg>
        </button>
        <button class="help-btn" @click="showHelp">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"/>
          </svg>
        </button>
      </div>
    </header>
    
    <!-- 用户信息卡片 -->
    <div class="user-profile-card">
      <div class="avatar-container">
        <img :src="userInfo.avatar" alt="用户头像" class="user-avatar" />
        <div class="avatar-badge">Lv{{ userInfo.level }}</div>
      </div>
      
      <div class="user-info">
        <h2 class="user-name">{{ userInfo.name }}</h2>
        <p class="user-title">{{ userInfo.title }}</p>
        <div class="user-stats">
          <div class="stat-item">
            <span class="stat-number">{{ userInfo.following }}</span>
            <span class="stat-label">关注</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">{{ userInfo.followers }}</span>
            <span class="stat-label">粉丝</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">{{ userInfo.likes }}</span>
            <span class="stat-label">获赞</span>
          </div>
        </div>
      </div>
      
      <button class="edit-profile-btn" @click="editProfile">
        编辑资料
      </button>
    </div>
    
    <!-- 我的作品 -->
    <div class="my-works">
      <div class="section-header">
        <h3 class="section-title">我的作品</h3>
        <button class="view-all-btn" @click="viewAllWorks">
          查看全部
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z"/>
          </svg>
        </button>
      </div>
      
      <div class="works-grid">
        <div 
          v-for="(work, index) in userInfo.works" 
          :key="index"
          class="work-item"
          @click="viewWork(index)"
        >
          <img :src="work.imageUrl" alt="作品图片" class="work-image" />
          <div class="work-stats">
            <span class="work-stat">{{ work.likes }}❤️</span>
            <span class="work-stat">{{ work.comments }}💬</span>
          </div>
        </div>
        <div v-if="userInfo.works.length < 9" class="work-item add-work" @click="createWork">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
          </svg>
          <span>添加作品</span>
        </div>
      </div>
    </div>
    
    <!-- 功能菜单 -->
    <div class="menu-section">
      <div class="menu-group">
        <button 
          v-for="menu in mainMenu" 
          :key="menu.id"
          class="menu-item"
          @click="handleMenu(menu.id)"
        >
          <div class="menu-icon">
            <svg :class="['icon', menu.iconClass]" viewBox="0 0 24 24" fill="currentColor">
              <path :d="menu.iconPath"/>
            </svg>
          </div>
          <span class="menu-text">{{ menu.title }}</span>
          <svg class="icon arrow-icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z"/>
          </svg>
        </button>
      </div>
      
      <div class="menu-group">
        <button 
          v-for="menu in otherMenu" 
          :key="menu.id"
          class="menu-item"
          @click="handleMenu(menu.id)"
        >
          <div class="menu-icon">
            <svg :class="['icon', menu.iconClass]" viewBox="0 0 24 24" fill="currentColor">
              <path :d="menu.iconPath"/>
            </svg>
          </div>
          <span class="menu-text">{{ menu.title }}</span>
          <svg class="icon arrow-icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getCurrentUser, getUserInfo, logout } from '../api/user'

// 用户基础信息
const currentUser = ref(null)
// 用户详细信息
const userInfo = reactive({
  name: '职场新人',
  avatar: 'https://picsum.photos/200/200?random=1',
  title: '产品经理 | 3年经验',
  level: 1,
  following: 0,
  followers: 0,
  likes: 0,
  works: []
})

// 菜单数据
const mainMenu = [
  {
    id: 'collections',
    title: '我的收藏',
    iconPath: 'M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2zm0 15l-5-2.18L7 18V5h10v13z',
    iconClass: 'collection-icon'
  },
  {
    id: 'history',
    title: '浏览历史',
    iconPath: 'M13 3c-4.97 0-9 4.03-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42C8.27 19.99 10.51 21 13 21c4.97 0 9-4.03 9-9s-4.03-9-9-9zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z',
    iconClass: 'history-icon'
  },
  {
    id: 'jobs',
    title: '我的工作',
    iconPath: 'M20 6h-4V4c0-1.11-.89-2-2-2h-4c-1.11 0-1.99.89-1.99 2v2H4c-1.11 0-1.99.89-1.99 2L2 19c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2zm-6 0h-4V4h4v2z',
    iconClass: 'job-icon'
  },
  {
    id: 'wallet',
    title: '我的钱包',
    iconPath: 'M21 18v1c0 1.1-.9 2-2 2H5c-1.11 0-2-.9-2-2V5c0-1.1.89-2 2-2h14c1.1 0 2 .9 2 2v1h-9c-1.11 0-2 .9-2 2v8c0 1.1.89 2 2 2h9zm-9-2h10V8H12v8zm4-2.5c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5z',
    iconClass: 'wallet-icon'
  }
]

const otherMenu = [
  {
    id: 'settings',
    title: '设置',
    iconPath: 'M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z',
    iconClass: 'settings-icon'
  },
  {
    id: 'help',
    title: '帮助与反馈',
    iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z',
    iconClass: 'help-icon'
  },
  {
    id: 'about',
    title: '关于我们',
    iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z',
    iconClass: 'about-icon'
  },
  {
    id: 'logout',
    title: '退出登录',
    iconPath: 'M17 7l-1.41-1.41-5.59 5.59-5.59-5.59L7 7l6 6 6-6zM17 17l1.41 1.41-5.59-5.59 5.59-5.59L17 11l-6 6 6 6z',
    iconClass: 'logout-icon'
  }
]

// 加载用户信息
const loadUserInfo = async () => {
  try {
    // 获取当前登录用户基础信息
    const userData = await getCurrentUser()
    console.log('当前用户信息:', userData)
    
    if (userData.success && userData.data) {
      currentUser.value = userData.data
      
      // 获取用户详细信息
      const userInfoData = await getUserInfo(currentUser.value.id)
      console.log('用户详细信息:', userInfoData)
      
      if (userInfoData.success && userInfoData.data) {
        // 更新用户信息
        Object.assign(userInfo, {
          name: userInfoData.data.nickname || currentUser.value.phone,
          avatar: userInfoData.data.avatar || 'https://picsum.photos/200/200?random=1',
          title: userInfoData.data.title || '用户',
          level: userInfoData.data.level || 1,
          following: userInfoData.data.followingCount || 0,
          followers: userInfoData.data.followersCount || 0,
          likes: userInfoData.data.likesCount || 0
        })
      } else {
        // 如果没有详细信息，使用基础信息
        userInfo.name = currentUser.value.phone || '用户'
      }
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    // 可以在这里处理未登录的情况，比如跳转到登录页
  }
}

// 菜单方法
const showSettings = () => {
  console.log('显示设置')
}

const showHelp = () => {
  console.log('显示帮助')
}

const editProfile = () => {
  console.log('编辑资料')
}

const viewAllWorks = () => {
  console.log('查看全部作品')
}

const viewWork = (index) => {
  console.log('查看作品:', index)
}

const createWork = () => {
  console.log('创建新作品')
}

const handleMenu = async (menuId) => {
  console.log('菜单点击:', menuId)
  
  if (menuId === 'logout') {
    // 从localStorage获取token
    const token = localStorage.getItem('token');
    
    try {
      // 无论是否有token，都执行登出逻辑
      
      // 调用登出接口，传递token
      if (token) {
        const result = await logout(token)
        console.log('退出登录结果:', result)
      }
      
      // 清除本地存储的token
      localStorage.removeItem('token')
      
      // 清除用户信息
      currentUser.value = null
      
      console.log('退出登录成功')
      
      // 刷新页面
      window.location.reload()
    } catch (error) {
      console.error('退出登录失败:', error)
      
      // 即使后端登出失败，也要清除前端状态
      localStorage.removeItem('token')
      currentUser.value = null
      window.location.reload()
    }
  }
}

// 组件挂载时加载用户信息
onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

/* 顶部导航栏 */
.profile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.profile-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.settings-btn,
.help-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.settings-btn .icon,
.help-btn .icon {
  width: 24px;
  height: 24px;
  color: #666;
}

/* 用户信息卡片 */
.user-profile-card {
  background-color: #fff;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.avatar-container {
  position: relative;
  margin-bottom: 12px;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f0f0f0;
}

.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #ff2442;
  color: white;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 10px;
  border: 2px solid #fff;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px;
}

.user-title {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px;
}

.user-stats {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 20px;
  background-color: #e0e0e0;
}

.edit-profile-btn {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-profile-btn:active {
  background-color: #f5f5f5;
}

/* 我的作品 */
.my-works {
  background-color: #fff;
  margin-top: 10px;
  padding: 16px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.view-all-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  color: #999;
  font-size: 14px;
  cursor: pointer;
}

.view-all-btn .icon {
  width: 16px;
  height: 16px;
}

.works-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
}

.work-item {
  position: relative;
  padding-top: 100%; /* 正方形 */
  overflow: hidden;
  border-radius: 6px;
}

.work-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.work-stats {
  position: absolute;
  bottom: 6px;
  left: 6px;
  display: flex;
  gap: 8px;
  font-size: 12px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.add-work {
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 0;
  height: 100%;
  cursor: pointer;
}

.add-work .icon {
  width: 30px;
  height: 30px;
  color: #ccc;
  margin-bottom: 8px;
}

.add-work span {
  font-size: 12px;
  color: #999;
}

/* 功能菜单 */
.menu-section {
  margin-top: 10px;
}

.menu-group {
  background-color: #fff;
  margin-bottom: 10px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  background: none;
  border: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  background-color: #f5f5f5;
}

.menu-icon .icon {
  width: 20px;
  height: 20px;
  color: #666;
}

.collection-icon {
  color: #ff9800 !important;
}

.history-icon {
  color: #2196f3 !important;
}

.job-icon {
  color: #4caf50 !important;
}

.wallet-icon {
  color: #e91e63 !important;
}

.settings-icon {
  color: #9e9e9e !important;
}

.help-icon {
  color: #00bcd4 !important;
}

.about-icon {
  color: #673ab7 !important;
}

.menu-text {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.arrow-icon {
  width: 16px;
  height: 16px;
  color: #ccc;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-profile-card {
    padding: 16px 14px;
  }
  
  .user-avatar {
    width: 70px;
    height: 70px;
  }
  
  .my-works {
    padding: 14px;
  }
  
  .menu-item {
    padding: 12px 14px;
  }
}

@media (max-width: 480px) {
  .profile-title {
    font-size: 17px;
  }
  
  .settings-btn .icon,
  .help-btn .icon {
    width: 22px;
    height: 22px;
  }
  
  .user-name {
    font-size: 17px;
  }
  
  .user-title {
    font-size: 13px;
  }
  
  .user-stats {
    gap: 16px;
  }
  
  .stat-number {
    font-size: 15px;
  }
  
  .works-grid {
    gap: 4px;
  }
  
  .menu-text {
    font-size: 14px;
  }
}
</style>