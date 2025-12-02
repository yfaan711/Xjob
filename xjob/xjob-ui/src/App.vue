<template>
  <div class="app-container">
    <!-- 主应用界面（登录和未登录都显示） -->
    <div>
      <!-- 页面容器 -->
      <div class="pages-container">
        <Plaza v-if="currentPage === 'square'" @show-ai-assistant="showAIAssistant" />
        <TaskList v-else-if="currentPage === 'tasks'" />
        <AIAssistant v-else-if="currentPage === 'ai'" />
        <Messages v-else-if="currentPage === 'message'" />
        <Profile 
          v-else-if="currentPage === 'profile'" 
          :isLoggedIn="isLoggedIn" 
          :key="`profile-${loginStateChange}`"
          @logout="handleLogout" 
          @goToLogin="goToLogin" 
        />
        <EditProfile v-else-if="currentPage === 'edit-profile'" @navigate="navigateTo" />
        <MyJobs v-else-if="currentPage === 'my-jobs'" @navigate="navigateTo" />
        <MyOrders v-else-if="currentPage === 'my-orders'" @navigate="navigateTo" />
      </div>
      
      <!-- 底部导航栏 -->
      <BottomNavigation 
        :current-page="currentPage"
        @page-change="changePage"
        @show-publish="showPublishDialog"
      />
    </div>
    
    <!-- 登录组件（按需显示，全屏覆盖） -->
      <div v-if="showLogin" class="login-overlay">
        <Login @login-success="handleLoginSuccess" @close="handleLoginClose" />
      </div>
    
    <!-- 底部登录提示弹窗 -->
    <div v-if="!isLoggedIn && showLoginPrompt" class="login-prompt">
      <div class="prompt-content">
        <span>登录后享受更多功能</span>
        <div class="prompt-actions">
          <button class="prompt-cancel" @click="closeLoginPrompt">暂不登录</button>
          <button class="prompt-login" @click="goToLogin">立即登录</button>
        </div>
      </div>
      <button class="prompt-close" @click="closeLoginPrompt">×</button>
    </div>
  </div>
</template>

<script>
import Plaza from './components/Plaza.vue'
import TaskList from './components/TaskList.vue'
import Messages from './components/Messages.vue'
import Profile from './components/Profile.vue'
import EditProfile from './components/EditProfile.vue'
import BottomNavigation from './components/BottomNavigation.vue'
import Login from './components/Login.vue'
import AIAssistant from './components/AIAssistant.vue'
import MyJobs from './components/MyJobs.vue'
import MyOrders from './components/MyOrders.vue'

// 导入全局样式文件
import './styles/global.css'

export default {
  name: 'App',
  components: {
    Plaza,
    TaskList,
    Messages,
    Profile,
    EditProfile,
    BottomNavigation,
    Login,
    AIAssistant,
    MyJobs,
    MyOrders
  },
  data() {
    return {
      // 默认显示广场页面
      currentPage: 'square',
      // 登录状态管理，默认未登录
      isLoggedIn: false,
      // 是否显示登录组件
      showLogin: false,
      // 是否显示登录提示弹窗
      showLoginPrompt: false,
      // 登录状态变更标志，用于触发子组件更新
      loginStateChange: 0
    }
  },
  async mounted() {
    // 组件挂载时检查登录状态
    await this.checkLoginStatus()
    
    // 短暂延迟后显示登录提示（仅在未登录时）
    setTimeout(() => {
      if (!this.isLoggedIn) {
        this.showLoginPrompt = true
      }
    }, 1000)
  },
  methods: {
    // 页面切换方法
    changePage(pageName) {
      this.currentPage = pageName
    },
    
    // 检查登录状态
    async checkLoginStatus() {
      try {
        // 检查本地是否有token
        const token = localStorage.getItem('token')
        if (!token) {
          console.log('未找到token，保持未登录状态')
          return
        }
        
        // 导入用户API
        const { getCurrentUser } = await import('./api/user.js')
        
        // 验证token是否有效
        const response = await getCurrentUser()
        if (response && response.success) {
          // token有效，设置为已登录状态
          this.isLoggedIn = true
          console.log('检测到有效token，已设置为登录状态')
        } else {
          // token无效，清除本地存储
          localStorage.removeItem('token')
          this.isLoggedIn = false
          console.log('token无效，已清除并设置为未登录状态')
        }
      } catch (error) {
        console.error('检查登录状态失败:', error)
        // 出错时清除token并设置为未登录状态
        localStorage.removeItem('token')
        this.isLoggedIn = false
      }
    },
    
    // 显示AI助手页面
    showAIAssistant() {
      this.currentPage = 'ai'
    },
    
    // 导航到特定页面（支持从组件内部调用）
    navigateTo(pageName) {
      this.currentPage = pageName
    },
    
    // 显示发布对话框
    showPublishDialog() {
      // 这里可以实现显示发布对话框的逻辑
      console.log('显示发布内容对话框')
      // 例如：可以显示一个模态框或者跳转到发布页面
      alert('发布内容功能待实现')
    },
    
    // 处理登录成功
    handleLoginSuccess() {
      this.isLoggedIn = true
      this.showLogin = false
      this.showLoginPrompt = false
      // 更新登录状态变更标志，触发子组件更新
      this.loginStateChange++
      console.log('用户登录成功，已更新登录状态')
    },
    
    // 处理退出登录
    handleLogout() {
      this.isLoggedIn = false
      this.currentPage = 'square'
      // 清除本地存储的token
      localStorage.removeItem('token')
      console.log('用户退出登录')
    },
    
    // 跳转到登录页面
    goToLogin() {
      console.log('App组件处理goToLogin事件 - 开始')
      // 直接设置showLogin为true，确保登录界面显示
      this.showLogin = true
      this.showLoginPrompt = false
      console.log('App组件处理goToLogin事件 - 完成，showLogin状态:', this.showLogin)
    },
    
    // 处理登录关闭
    handleLoginClose() {
      this.showLogin = false
    },
    
    // 关闭登录提示
    closeLoginPrompt() {
      this.showLoginPrompt = false
    }
  }
}
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  -webkit-tap-highlight-color: transparent;
}

html, body {
  width: 100%;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  font-size: 16px;
  color: #333;
  background-color: #f8f8f8;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow-x: hidden;
}

#app {
  width: 100%;
  height: 100%;
}

.app-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* 登录界面覆盖层样式 */
.login-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  box-sizing: border-box;
}

/* 确保登录组件不会太宽 */
.login-overlay > .login-container {
  max-width: 400px;
  width: 100%;
  margin: 20px auto;
  box-sizing: border-box;
}

/* 确保登录表单内容紧凑 */
.login-overlay .login-form {
  width: 100%;
  box-sizing: border-box;
}

.pages-container {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 4px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 2px;
}

::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* 禁用文本选择，提高移动端体验 */
body {
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}

/* 图片和输入框允许选择 */
img, input, textarea {
  user-select: auto;
  -webkit-user-select: auto;
  -moz-user-select: auto;
  -ms-user-select: auto;
}

/* 按钮通用样式 */
button {
  font-family: inherit;
  font-size: inherit;
  line-height: inherit;
  cursor: pointer;
}

/* 输入框通用样式 */

/* 登录提示弹窗样式 */
.login-prompt {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 1000;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.prompt-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.prompt-actions {
  display: flex;
  gap: 8px;
}

.prompt-cancel {
  background: transparent;
  color: white;
  border: 1px solid white;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
}

.prompt-login {
  background: white;
  color: #333;
  border: none;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  font-weight: bold;
}

.prompt-close {
  background: transparent;
  color: white;
  border: none;
  font-size: 20px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}
input, textarea {
  font-family: inherit;
  font-size: inherit;
  line-height: inherit;
  outline: none;
}
</style>
