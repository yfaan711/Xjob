<template>
  <div class="app-container">
    <!-- 登录状态条件渲染 -->
    <Login v-if="!isLoggedIn" @login-success="handleLoginSuccess" />
    
    <!-- 已登录状态下的主应用界面 -->
    <div v-else>
      <!-- 页面容器 -->
      <div class="pages-container">
        <Plaza v-if="currentPage === 'square'" />
        <AIAssistant v-else-if="currentPage === 'ai'" />
        <Messages v-else-if="currentPage === 'message'" />
        <Profile v-else-if="currentPage === 'profile'" />
      </div>
      
      <!-- 底部导航栏 -->
      <BottomNavigation 
        :current-page="currentPage"
        @page-change="changePage"
        @show-publish="showPublishDialog"
      />
    </div>
  </div>
</template>

<script>
import Plaza from './components/Plaza.vue'
import AIAssistant from './components/AIAssistant.vue'
import Messages from './components/Messages.vue'
import Profile from './components/Profile.vue'
import BottomNavigation from './components/BottomNavigation.vue'
import Login from './components/Login.vue'

// 导入全局样式文件
import './styles/global.css'

export default {
  name: 'App',
  components: {
    Plaza,
    AIAssistant,
    Messages,
    Profile,
    BottomNavigation,
    Login
  },
  data() {
    return {
      // 默认显示广场页面
      currentPage: 'square',
      // 登录状态管理，默认未登录
      isLoggedIn: false
    }
  },
  methods: {
    // 页面切换方法
    changePage(pageName) {
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
      console.log('用户登录成功')
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
input, textarea {
  font-family: inherit;
  font-size: inherit;
  line-height: inherit;
  outline: none;
}
</style>
