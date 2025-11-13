<template>
  <div class="messages-container">
    <!-- 顶部导航栏 -->
    <header class="messages-header">
      <h1 class="messages-title">消息</h1>
      <div class="header-actions">
        <button class="new-message-btn" @click="createNewMessage">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M21 6h-2v9H6v2c0 .55.45 1 1 1h11l4 4V7c0-.55-.45-1-1-1zm-4 6V3c0-.55-.45-1-1-1H3c-.55 0-1 .45-1 1v14l4-4h10c.55 0 1-.45 1-1z"/>
          </svg>
        </button>
        <button class="settings-btn" @click="showSettings">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"/>
          </svg>
        </button>
      </div>
    </header>
    
    <!-- 消息类型选项卡 -->
    <div class="message-tabs">
      <button 
        class="tab-btn"
        :class="{ active: activeTab === 'chat' }"
        @click="switchTab('chat')"
      >
        私信
      </button>
      <button 
        class="tab-btn"
        :class="{ active: activeTab === 'notification' }"
        @click="switchTab('notification')"
      >
        通知
        <span v-if="unreadNotifications > 0" class="badge">{{ unreadNotifications }}</span>
      </button>
    </div>
    
    <!-- 私信列表 -->
    <div v-if="activeTab === 'chat'" class="message-list">
      <button 
        v-for="chat in chatList" 
        :key="chat.id"
        class="chat-item"
        :class="{ unread: chat.unreadCount > 0 }"
        @click="openChat(chat.id)"
      >
        <!-- 头像和在线状态 -->
        <div class="avatar-container">
          <img :src="chat.avatar" alt="{{ chat.name }}" class="chat-avatar" />
          <span v-if="chat.isOnline" class="online-indicator"></span>
          <!-- 消息角标 -->
          <span v-if="chat.unreadCount > 0" class="unread-badge">{{ chat.unreadCount }}</span>
        </div>
        
        <!-- 聊天内容 -->
        <div class="chat-content">
          <div class="chat-header">
            <span class="chat-name">{{ chat.name }}</span>
            <span class="chat-time">{{ chat.time }}</span>
          </div>
          
          <div class="chat-preview">
            <span v-if="chat.lastMessageSender === 'me'" class="sender">我: </span>
            <span class="message-text">{{ chat.lastMessage }}</span>
          </div>
        </div>
      </button>
    </div>
    
    <!-- 通知列表 -->
    <div v-else-if="activeTab === 'notification'" class="notification-list">
      <div 
        v-for="notification in notificationList" 
        :key="notification.id"
        class="notification-item"
        :class="{ unread: !notification.read }"
        @click="markAsRead(notification.id)"
      >
        <div class="notification-icon">
          <svg v-if="notification.type === 'like'" class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
          </svg>
          <svg v-else-if="notification.type === 'comment'" class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M21 6h-2v9H6v2c0 .55.45 1 1 1h11l4 4V7c0-.55-.45-1-1-1zm-4 6V3c0-.55-.45-1-1-1H3c-.55 0-1 .45-1 1v14l4-4h10c.55 0 1-.45 1-1z"/>
          </svg>
          <svg v-else-if="notification.type === 'follow'" class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M16 1H8C6.34 1 5 2.34 5 4v16c0 1.66 1.34 3 3 3h8c1.66 0 3-1.34 3-3V4c0-1.66-1.34-3-3-3zm-2 20h-4v-1h4v1zm3.25-3H6.75V4h10.5v14z"/>
          </svg>
          <svg v-else class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/>
          </svg>
        </div>
        
        <div class="notification-content">
          <p class="notification-text">{{ notification.content }}</p>
          <span class="notification-time">{{ notification.time }}</span>
        </div>
        
        <div v-if="!notification.read" class="read-indicator"></div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="(activeTab === 'chat' && chatList.length === 0) || (activeTab === 'notification' && notificationList.length === 0)" class="empty-state">
      <svg class="empty-icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
      </svg>
      <p class="empty-text">{{ activeTab === 'chat' ? '暂无私信消息' : '暂无通知消息' }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Messages',
  data() {
    return {
      activeTab: 'chat',
      unreadNotifications: 3,
      chatList: [
        {
          id: 1,
          name: '职场达人',
          avatar: 'https://picsum.photos/100/100?random=1',
          lastMessage: '您的建议很有帮助，谢谢分享！',
          lastMessageSender: 'other',
          time: '10:23',
          unreadCount: 2,
          isOnline: true
        },
        {
          id: 2,
          name: 'HR专员',
          avatar: 'https://picsum.photos/100/100?random=2',
          lastMessage: '您好，请问您对我们公司的岗位还有什么疑问吗？',
          lastMessageSender: 'other',
          time: '昨天',
          unreadCount: 1,
          isOnline: false
        },
        {
          id: 3,
          name: '技术团队',
          avatar: 'https://picsum.photos/100/100?random=3',
          lastMessage: '我: 我会在今天完成这个需求的开发',
          lastMessageSender: 'me',
          time: '昨天',
          unreadCount: 0,
          isOnline: false
        },
        {
          id: 4,
          name: '创业伙伴',
          avatar: 'https://picsum.photos/100/100?random=4',
          lastMessage: '下次会议时间定在下周一上午10点',
          lastMessageSender: 'other',
          time: '2天前',
          unreadCount: 0,
          isOnline: true
        },
        {
          id: 5,
          name: '猎头顾问',
          avatar: 'https://picsum.photos/100/100?random=5',
          lastMessage: '我: 请把岗位详情发给我看看',
          lastMessageSender: 'me',
          time: '3天前',
          unreadCount: 0,
          isOnline: false
        }
      ],
      notificationList: [
        {
          id: 1,
          type: 'like',
          content: '职场达人赞了您的动态《如何有效提升工作效率》',
          time: '10分钟前',
          read: false
        },
        {
          id: 2,
          type: 'comment',
          content: '技术大咖评论了您的文章："非常实用的分享，学到了很多！"',
          time: '30分钟前',
          read: false
        },
        {
          id: 3,
          type: 'follow',
          content: 'HR专员关注了您',
          time: '2小时前',
          read: false
        },
        {
          id: 4,
          type: 'system',
          content: '您的账号已完成实名认证',
          time: '昨天',
          read: true
        },
        {
          id: 5,
          type: 'like',
          content: '多位用户赞了您的动态',
          time: '2天前',
          read: true
        }
      ]
    }
  },
  methods: {
    createNewMessage() {
      console.log('创建新消息')
    },
    showSettings() {
      console.log('显示消息设置')
    },
    switchTab(tab) {
      this.activeTab = tab
      if (tab === 'notification') {
        // 切换到通知tab时可以考虑清除未读计数
        // this.unreadNotifications = 0
      }
    },
    openChat(chatId) {
      console.log('打开聊天:', chatId)
      // 在实际应用中，这里会导航到聊天详情页面
      // 或者在当前页面显示聊天界面
    },
    markAsRead(notificationId) {
      console.log('标记为已读:', notificationId)
      const notification = this.notificationList.find(n => n.id === notificationId)
      if (notification && !notification.read) {
        notification.read = true
        this.unreadNotifications = Math.max(0, this.unreadNotifications - 1)
      }
    }
  }
}
</script>

<style scoped>
.messages-container {
  background-color: #f8f8f8;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.messages-header {
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

.messages-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.new-message-btn,
.settings-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.new-message-btn .icon,
.settings-btn .icon {
  width: 24px;
  height: 24px;
  color: #666;
}

/* 消息类型选项卡 */
.message-tabs {
  display: flex;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 10px;
}

.tab-btn {
  flex: 1;
  padding: 12px 0;
  background: none;
  border: none;
  font-size: 15px;
  color: #666;
  position: relative;
  cursor: pointer;
}

.tab-btn.active {
  color: #ff2442;
  font-weight: 500;
}

.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 3px;
  background-color: #ff2442;
  border-radius: 3px;
}

.badge {
  position: absolute;
  top: 8px;
  right: 40%;
  background-color: #ff2442;
  color: white;
  font-size: 10px;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

/* 消息列表 */
.message-list {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  background-color: #f5f5f5;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #fff;
  transition: all 0.2s ease;
  min-height: 72px;
  box-sizing: border-box;
  width: 100%;
  border-radius: 0;
  margin: 0;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}

.chat-item:active {
  background-color: #f9f9f9;
}

.chat-item.unread {
  background-color: #fff;
}

.avatar-container {
  position: relative;
  margin-right: 12px;
  width: 48px;
  height: 48px;
  flex-shrink: 0;
}

.chat-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: none;
  background: none;
}

.online-indicator {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  background-color: #4caf50;
  border: 2px solid #fff;
  border-radius: 50%;
}

.unread-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 18px;
  height: 18px;
  background-color: #ff2442;
  color: white;
  font-size: 11px;
  font-weight: 500;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  box-shadow: 0 1px 2px rgba(255, 36, 66, 0.3);
}

.chat-content {
  flex: 1;
  min-width: 0;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-bottom: 4px;
}

.chat-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  margin-right: 8px;
}

.chat-time {
  font-size: 12px;
  color: #999;
  flex-shrink: 0;
}

.chat-preview {
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 20px;
}

.sender {
  font-size: 14px;
  color: #999;
  font-weight: normal;
  margin-right: 4px;
  flex-shrink: 0;
}

.message-text {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

/* 消息小红点动画效果 */
@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 36, 66, 0.7);
  }
  70% {
    box-shadow: 0 0 0 5px rgba(255, 36, 66, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 36, 66, 0);
  }
}

@keyframes pulse {
  0% {
    box-shadow: 0 2px 6px rgba(255, 107, 107, 0.3);
    transform: translateY(-50%) scale(1);
  }
  50% {
    box-shadow: 0 3px 12px rgba(255, 107, 107, 0.5);
    transform: translateY(-50%) scale(1.05);
  }
  100% {
    box-shadow: 0 2px 6px rgba(255, 107, 107, 0.3);
    transform: translateY(-50%) scale(1);
  }
}

/* 通知列表 */
.notification-list {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 14px 16px;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;
}

.notification-item:active {
  background-color: #f5f5f5;
}

.notification-item.unread {
  background-color: #fff8f9;
}

.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.notification-icon .icon {
  width: 22px;
  height: 22px;
  color: #ff2442;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-text {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  margin: 0 0 4px;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.read-indicator {
  width: 8px;
  height: 8px;
  background-color: #ff2442;
  border-radius: 50%;
  margin-left: 12px;
  margin-top: 8px;
  flex-shrink: 0;
}

/* 空状态 */
.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #999;
}

.empty-icon {
  width: 60px;
  height: 60px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-text {
  font-size: 15px;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-item {
    padding: 10px 14px;
    height: 76px;
  }
  
  .avatar-container {
    width: 45px;
    height: 45px;
    margin-right: 10px;
  }
  
  .chat-avatar {
    width: 45px;
    height: 45px;
  }
  
  .chat-content {
    max-width: calc(100% - 110px);
    height: 45px;
  }
  
  .unread-badge {
    right: 14px;
  }
  
  .notification-item {
    padding: 12px 14px;
  }
  
  .notification-icon {
    width: 36px;
    height: 36px;
  }
}

@media (max-width: 480px) {
  .messages-header {
    padding: 10px 12px;
  }
  
  .messages-title {
    font-size: 17px;
  }
  
  .new-message-btn .icon,
  .settings-btn .icon {
    width: 22px;
    height: 22px;
  }
  
  .tab-btn {
    padding: 10px 0;
    font-size: 14px;
  }
  
  .chat-name {
    font-size: 14px;
  }
  
  .message-text {
    font-size: 12px;
  }
  
  .notification-text {
    font-size: 13px;
  }
}
</style>