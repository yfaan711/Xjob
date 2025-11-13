<template>
  <div class="bottom-navigation">
    <div 
      class="nav-item" 
      :class="{ active: currentPage === 'square' }" 
      @click="switchPage('square')"
    >
      <div class="nav-icon">
        <svg v-if="currentPage !== 'square'" width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M12 20.5C16.6944 20.5 20.5 16.6944 20.5 12C20.5 7.30558 16.6944 3.5 12 3.5C7.30558 3.5 3.5 7.30558 3.5 12C3.5 16.6944 7.30558 20.5 12 20.5Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M8 12L11 15L16 10" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M12 20.5C16.6944 20.5 20.5 16.6944 20.5 12C20.5 7.30558 16.6944 3.5 12 3.5C7.30558 3.5 3.5 7.30558 3.5 12C3.5 16.6944 7.30558 20.5 12 20.5Z" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M8 12L11 15L16 10" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <span class="nav-text">主页</span>
    </div>
    
    <div 
      class="nav-item" 
      :class="{ active: currentPage === 'tasks' }" 
      @click="switchPage('tasks')"
    >
      <div class="nav-icon">
        <svg v-if="currentPage !== 'tasks'" width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M8 6H6C4.89543 6 4 6.89543 4 8V18C4 19.1046 4.89543 20 6 20H18C19.1046 20 20 19.1046 20 18V10C20 8.89543 19.1046 8 18 8H16" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M16 8V6C16 4.89543 15.1046 4 14 4H8C6.89543 4 6 4.89543 6 6V8" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M9 13L11 13" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M9 17L13 17" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M8 6H6C4.89543 6 4 6.89543 4 8V18C4 19.1046 4.89543 20 6 20H18C19.1046 20 20 19.1046 20 18V10C20 8.89543 19.1046 8 18 8H16" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M16 8V6C16 4.89543 15.1046 4 14 4H8C6.89543 4 6 4.89543 6 6V8" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M9 13L11 13" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M9 17L13 17" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <span class="nav-text">技能广场</span>
    </div>
    
    <!-- 发布按钮 - 改为普通导航项 -->
    <div 
      class="nav-item" 
      @click="showPublishPage"
    >
      <div class="nav-icon">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M12 5V19M5 12H19" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <span class="nav-text">发布</span>
    </div>
    
    <div 
      class="nav-item" 
      :class="{ active: currentPage === 'message' }" 
      @click="switchPage('message')"
    >
      <div class="nav-icon">
        <svg v-if="currentPage !== 'message'" width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M21 15C21 19.9706 16.9706 24 12 24C7.02944 24 3 19.9706 3 15C3 12.6252 3.88555 10.4164 5.35978 8.65978C6.83401 6.90312 8.86272 5.63728 11.1176 5.06763L11.5 5H12.5L12.8824 5.06763C15.1373 5.63728 17.166 6.90312 18.6402 8.65978C20.1144 10.4164 21 12.6252 21 15Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M21 15C21 19.9706 16.9706 24 12 24C7.02944 24 3 19.9706 3 15C3 12.6252 3.88555 10.4164 5.35978 8.65978C6.83401 6.90312 8.86272 5.63728 11.1176 5.06763L11.5 5H12.5L12.8824 5.06763C15.1373 5.63728 17.166 6.90312 18.6402 8.65978C20.1144 10.4164 21 12.6252 21 15Z" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <div class="badge" v-if="unreadCount > 0">{{ unreadCount }}</div>
      </div>
      <span class="nav-text">消息</span>
    </div>
    
    <div 
      class="nav-item" 
      :class="{ active: currentPage === 'profile' }" 
      @click="switchPage('profile')"
    >
      <div class="nav-icon">
        <svg v-if="currentPage !== 'profile'" width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M12 12C14.4853 12 16.5 9.98528 16.5 7.5C16.5 5.01472 14.4853 3 12 3C9.51472 3 7.5 5.01472 7.5 7.5C7.5 9.98528 9.51472 12 12 12Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M19.2 20.7C18.7667 19.7667 18.0667 18.9 17.1 18.1C16.1333 17.3 15.0667 16.6 13.9 16.0C13.4333 15.7333 12.9333 15.5 12.4 15.3C11.8667 15.1 11.3 14.9333 10.7 14.8C10.1 14.6666 9.46667 14.5667 8.8 14.5C8.13333 14.4333 7.43333 14.4 6.7 14.4C6.0 14.4 5.3 14.4333 4.6 14.5C3.9 14.5666 3.23333 14.6667 2.6 14.8C2.0 14.9333 1.43333 15.1 0.9 15.3C0.366666 15.5 0 15.7333 0 16.0V21C0 21.3 0.366666 21.5 0.9 21.7C1.43333 21.9 2.0 22.0 2.6 22.1C3.23333 22.2333 3.9 22.3333 4.6 22.4C5.3 22.4667 6.0 22.5 6.7 22.5C7.43333 22.5 8.13333 22.4667 8.8 22.4C9.46667 22.3333 10.1 22.2333 10.7 22.1C11.3 22.0 11.8667 21.9 12.4 21.7C12.9333 21.5 13.4333 21.3 13.9 21.0C15.0667 20.4 16.1333 19.7 17.1 18.9C18.0667 18.1 18.7667 17.2333 19.2 16.3C19.6333 15.3667 19.9 14.4 19.9 13.5C19.9 12.6 19.6333 11.6333 19.2 10.7C18.7667 9.76667 18.0667 8.9 17.1 8.1C16.1333 7.3 15.0667 6.6 13.9 6.0C13.4333 5.73333 12.9333 5.5 12.4 5.3C11.8667 5.1 11.3 4.93333 10.7 4.8C10.1 4.66667 9.46667 4.56667 8.8 4.5C8.13333 4.43333 7.43333 4.4 6.7 4.4C6.0 4.4 5.3 4.43333 4.6 4.5C3.9 4.56667 3.23333 4.66667 2.6 4.8C2.0 4.93333 1.43333 5.1 0.9 5.3C0.366666 5.5 0 5.73333 0 6.0V7.5C0 7.7 0.366666 7.9 0.9 8.1C1.43333 8.3 2.0 8.4 2.6 8.5C3.23333 8.63333 3.9 8.73333 4.6 8.8C5.3 8.86667 6.0 8.9 6.7 8.9C7.43333 8.9 8.13333 8.86667 8.8 8.8C9.46667 8.73333 10.1 8.63333 10.7 8.5C11.3 8.4 11.8667 8.3 12.4 8.1C12.9333 7.9 13.4333 7.7 13.9 7.4C15.0667 6.8 16.1333 6.1 17.1 5.3C18.0667 4.5 18.7667 3.63333 19.2 2.7C19.6333 1.76667 19.9 0.8 19.9 0C19.9 0 24 0 24 0V13.5C24 13.5 24 18.0 19.2 20.7Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M12 12C14.4853 12 16.5 9.98528 16.5 7.5C16.5 5.01472 14.4853 3 12 3C9.51472 3 7.5 5.01472 7.5 7.5C7.5 9.98528 9.51472 12 12 12Z" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M19.2 20.7C18.7667 19.7667 18.0667 18.9 17.1 18.1C16.1333 17.3 15.0667 16.6 13.9 16.0C13.4333 15.7333 12.9333 15.5 12.4 15.3C11.8667 15.1 11.3 14.9333 10.7 14.8C10.1 14.6666 9.46667 14.5667 8.8 14.5C8.13333 14.4333 7.43333 14.4 6.7 14.4C6.0 14.4 5.3 14.4333 4.6 14.5C3.9 14.5666 3.23333 14.6667 2.6 14.8C2.0 14.9333 1.43333 15.1 0.9 15.3C0.366666 15.5 0 15.7333 0 16.0V21C0 21.3 0.366666 21.5 0.9 21.7C1.43333 21.9 2.0 22.0 2.6 22.1C3.23333 22.2333 3.9 22.3333 4.6 22.4C5.3 22.4667 6.0 22.5 6.7 22.5C7.43333 22.5 8.13333 22.4667 8.8 22.4C9.46667 22.3333 10.1 22.2333 10.7 22.1C11.3 22.0 11.8667 21.9 12.4 21.7C12.9333 21.5 13.4333 21.3 13.9 21.0C15.0667 20.4 16.1333 19.7 17.1 18.9C18.0667 18.1 18.7667 17.2333 19.2 16.3C19.6333 15.3667 19.9 14.4 19.9 13.5C19.9 12.6 19.6333 11.6333 19.2 10.7C18.7667 9.76667 18.0667 8.9 17.1 8.1C16.1333 7.3 15.0667 6.6 13.9 6.0C13.4333 5.73333 12.9333 5.5 12.4 5.3C11.8667 5.1 11.3 4.93333 10.7 4.8C10.1 4.66667 9.46667 4.56667 8.8 4.5C8.13333 4.43333 7.43333 4.4 6.7 4.4C6.0 4.4 5.3 4.43333 4.6 4.5C3.9 4.56667 3.23333 4.66667 2.6 4.8C2.0 4.93333 1.43333 5.1 0.9 5.3C0.366666 5.5 0 5.73333 0 6.0V7.5C0 7.7 0.366666 7.9 0.9 8.1C1.43333 8.3 2.0 8.4 2.6 8.5C3.23333 8.63333 3.9 8.73333 4.6 8.8C5.3 8.86667 6.0 8.9 6.7 8.9C7.43333 8.9 8.13333 8.86667 8.8 8.8C9.46667 8.73333 10.1 8.63333 10.7 8.5C11.3 8.4 11.8667 8.3 12.4 8.1C12.9333 7.9 13.4333 7.7 13.9 7.4C15.0667 6.8 16.1333 6.1 17.1 5.3C18.0667 4.5 18.7667 3.63333 19.2 2.7C19.6333 1.76667 19.9 0.8 19.9 0C19.9 0 24 0 24 0V13.5C24 13.5 24 18.0 19.2 20.7Z" stroke="#667eea" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <span class="nav-text">我的</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 定义组件接收的props
const props = defineProps({
  currentPage: {
    type: String,
    default: 'square'
  }
})

// 定义组件发送的事件
const emit = defineEmits(['page-change', 'show-publish'])

// 未读消息数量
const unreadCount = ref(2)

// 切换页面
const switchPage = (page) => {
  emit('page-change', page)
}

// 显示发布页面
const showPublishPage = () => {
  emit('show-publish')
}
</script>

<style scoped>
.bottom-navigation {
  display: flex;
  justify-content: space-around;
  align-items: center;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  z-index: 1000;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  padding: 0;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.nav-item:active {
  background-color: rgba(102, 126, 234, 0.05);
}

.nav-icon {
  position: relative;
  margin-bottom: 4px;
}

.nav-text {
  font-size: 12px;
  color: #999;
  font-weight: 500;
}

.nav-item.active .nav-text {
  color: #667eea;
}

.nav-item.active .nav-icon svg {
  stroke: #667eea;
}

/* 未读消息角标 */
.badge {
  position: absolute;
  top: -4px;
  right: -8px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  border-radius: 9px;
  font-size: 10px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
}

/* 响应式适配 */
@media (max-width: 768px) {
  .bottom-navigation {
    height: 56px;
  }
  
  .nav-text {
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .nav-text {
    font-size: 10px;
  }
}
</style>