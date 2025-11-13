<template>
  <div class="profile-container">
    <!-- 顶部导航栏 -->
    <header class="profile-header">
      <h1 class="profile-title">我的</h1>
      <div class="header-actions">
        <button class="settings-btn" @click="showSettings" v-if="isLoggedIn">
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
      <!-- 游客模式 -->
      <div v-if="!isLoggedIn" class="visitor-info">
        <div class="visitor-avatar">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
          </svg>
        </div>
        <div class="visitor-text">
          <h2>游客用户</h2>
          <p>登录后查看个人信息</p>
        </div>
        <button class="login-now-btn" @click="goToLogin">
          立即登录
        </button>
      </div>
      
      <!-- 已登录模式 -->
      <div v-else>
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

<script>
export default {
  name: 'Profile',
  emits: ['logout', 'goToLogin'],
  props: {
    isLoggedIn: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      userInfo: {
        name: '职场新人',
        avatar: 'https://picsum.photos/200/200?random=1',
        title: '产品经理 | 3年经验',
        level: 4,
        following: 128,
        followers: 342,
        likes: 1567,
        works: [
          {
            imageUrl: 'https://picsum.photos/300/300?random=10',
            likes: 245,
            comments: 36
          },
          {
            imageUrl: 'https://picsum.photos/300/300?random=11',
            likes: 189,
            comments: 28
          },
          {
            imageUrl: 'https://picsum.photos/300/300?random=12',
            likes: 356,
            comments: 52
          },
          {
            imageUrl: 'https://picsum.photos/300/300?random=13',
            likes: 178,
            comments: 23
          },
          {
            imageUrl: 'https://picsum.photos/300/300?random=14',
            likes: 213,
            comments: 31
          },
          {
            imageUrl: 'https://picsum.photos/300/300?random=15',
            likes: 421,
            comments: 67
          }
        ]
      },
      mainMenu: [
        {
          id: 'jobs',
          title: '我的工作',
          iconPath: 'M20 6h-4V4c0-1.11-.89-2-2-2h-4c-1.11 0-1.99.89-1.99 2v2H4c-1.11 0-1.99.89-1.99 2L2 19c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2zm-6 0h-4V4h4v2z',
          iconClass: 'job-icon'
        },
        {
          id: 'orders',
          title: '我的订单',
          iconPath: 'M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm-2 14l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z',
          iconClass: 'orders-icon'
        }
      ],
      otherMenu: [
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
          iconPath: 'M17 8l4 4-4 4m-6-4h10v2H5z',
          iconClass: 'logout-icon'
        }
      ]
    }
  },
  methods: {
    showSettings() {
      console.log('显示设置')
    },
    showHelp() {
      console.log('显示帮助')
    },
    editProfile() {
      // 导航到编辑资料页面
      this.$parent.navigateTo('edit-profile')
    },
    viewAllWorks() {
      console.log('查看全部作品')
    },
    viewWork(index) {
      console.log('查看作品:', index)
    },
    createWork() {
      console.log('创建新作品')
    },
    handleMenu(menuId) {
      console.log('菜单点击:', menuId)
      
      // 处理退出登录
      if (menuId === 'logout') {
        this.handleLogout()
      } 
      // 处理我的工作导航
      else if (menuId === 'jobs') {
        // 导航到我的工作页面
        this.$parent.navigateTo('my-jobs')
      }
      // 处理我的订单导航
      else if (menuId === 'orders') {
        // 导航到我的订单页面
        this.$parent.navigateTo('my-orders')
      }
    },
    
    // 跳转到登录页面
    goToLogin() {
      console.log('Profile组件跳转到登录页面')
      // 尝试直接修改根组件的showLogin状态
      if (this.$root && typeof this.$root.$data !== 'undefined') {
        console.log('尝试通过$root直接设置showLogin状态')
        this.$root.$data.showLogin = true
        this.$root.$data.showLoginPrompt = false
        console.log('通过$root设置完成')
      } else {
        console.log('尝试通过事件机制')
        this.$emit('goToLogin')
      }
    },
    
    async handleLogout() {
      try {
        // 导入logout API
        const { logout } = await import('../api/user.js')
        
        // 获取token
        const token = localStorage.getItem('token')
        
        if (token) {
          // 调用退出登录接口
          await logout(token)
          console.log('退出登录成功')
        }
        
        // 清除本地存储的token
        localStorage.removeItem('token')
        
        // 通知父组件更新登录状态
        this.$emit('logout')
        
        console.log('已清除token并更新登录状态')
      } catch (error) {
        console.error('退出登录失败:', error)
        // 即使接口失败，也清除本地token并更新状态
        localStorage.removeItem('token')
        this.$parent.$data.isLoggedIn = false
      }
    },
    async loadUserInfo() {
      // 只有在登录状态下才尝试加载用户信息
      if (!this.isLoggedIn) {
        console.log('未登录状态，不加载用户信息')
        return
      }
      
      try {
        // 导入API函数
        const { getCurrentUser, getUserInfo } = await import('../api/user.js')
        
        // 获取用户基本信息
        const userData = await getCurrentUser()
        console.log('获取到的用户基本信息:', userData)
        
        if (userData && userData.data) {
          // 更新用户信息
          this.userInfo.name = userData.data.nickName || '职场新人'
          this.userInfo.avatar = userData.data.icon || 'https://picsum.photos/200/200?random=1'
          
          // 如果有role信息，更新title
          if (userData.data.role) {
            this.userInfo.title = `${userData.data.role}`
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
        // 加载失败时使用默认的模拟数据
        this.loadMockUserInfo()
      }
    },
    loadMockUserInfo() {
      // 使用与EditProfile.vue相同的模拟数据
      const mockUserData = {
        name: 'user_cnyewcpbnf',
        avatar: 'https://picsum.photos/200/200?random=1',
        title: '用户',
        level: 1
      }
      
      // 确保头像URL有效
      if (!mockUserData.avatar || mockUserData.avatar.trim() === '') {
        mockUserData.avatar = 'https://picsum.photos/200/200?random=default'
      }
      
      // 更新用户信息
      this.userInfo = {
        ...this.userInfo,
        ...mockUserData
      }
    }
  },
  mounted() {
    // 尝试加载真实用户信息，如果失败则使用模拟数据
    this.loadUserInfo()
  },
  watch: {
    // 监听登录状态变化，当登录状态改变时重新加载用户信息
    isLoggedIn: {
      handler(newVal) {
        console.log('登录状态变化:', newVal)
        if (newVal) {
          // 登录成功后，延迟一小段时间再加载用户信息，确保token已被正确保存
          setTimeout(() => {
            this.loadUserInfo()
          }, 300)
        }
      },
      immediate: true
    }
  }
}
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

/* 游客信息样式 */
.visitor-info {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 16px;
}

.visitor-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.visitor-avatar .icon {
  width: 30px;
  height: 30px;
}

.visitor-text {
  flex: 1;
}

.visitor-text h2 {
  font-size: 17px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.visitor-text p {
  font-size: 14px;
  color: #999;
}

.login-now-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-now-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
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