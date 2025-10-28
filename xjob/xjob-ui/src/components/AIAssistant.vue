<template>
  <div class="ai-assistant-container">
    <!-- 顶部导航栏 -->
    <header class="ai-header">
      <h1 class="ai-title">AI助手</h1>
      <div class="ai-actions">
        <button class="history-btn" @click="showHistory">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M13 3c-4.97 0-9 4.03-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42C8.27 19.99 10.51 21 13 21c4.97 0 9-4.03 9-9s-4.03-9-9-9zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/>
          </svg>
        </button>
        <button class="settings-btn" @click="showSettings">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"/>
          </svg>
        </button>
      </div>
    </header>
    
    <!-- AI助手介绍卡片 -->
    <div class="ai-intro-card">
      <div class="ai-avatar">
        <svg class="ai-icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm3.5-9c.83 0 1.5-.67 1.5-1.5S16.33 8 15.5 8 14 8.67 14 9.5s.67 1.5 1.5 1.5zm-7 0c.83 0 1.5-.67 1.5-1.5S9.33 8 8.5 8 7 8.67 7 9.5 7.67 11 8.5 11zm3.5 6.5c2.33 0 4.31-1.46 5.11-3.5H6.89c.8 2.04 2.78 3.5 5.11 3.5z"/>
        </svg>
      </div>
      <h2 class="ai-name">Xjob AI 助手</h2>
      <p class="ai-description">您的职业发展顾问，随时为您解答职场问题</p>
    </div>
    
    <!-- 快捷问题 -->
    <div class="quick-questions">
      <h3 class="section-title">热门问题</h3>
      <div class="quick-question-list">
        <button 
          v-for="(question, index) in quickQuestions" 
          :key="index"
          class="quick-question-item"
          @click="askQuestion(question)"
        >
          {{ question }}
        </button>
      </div>
    </div>
    
    <!-- 聊天历史区域 -->
    <div class="chat-history" ref="chatHistory">
      <!-- 系统提示消息 -->
      <div class="chat-message system-message">
        <div class="message-content">
          👋 您好！我是Xjob AI助手，很高兴为您提供职业发展相关的帮助。请问有什么可以帮您的吗？
        </div>
      </div>
      
      <!-- 动态生成的聊天消息将在这里显示 -->
    </div>
    
    <!-- 输入区域 -->
    <div class="input-area">
      <div class="input-container">
        <input 
          type="text" 
          v-model="userInput" 
          class="text-input"
          placeholder="请输入您的问题..."
          @keyup.enter="sendMessage"
        />
        <button 
          class="send-btn" 
          @click="sendMessage"
          :disabled="!userInput.trim()"
        >
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AIAssistant',
  data() {
    return {
      userInput: '',
      messages: [],
      quickQuestions: [
        '如何优化我的简历？',
        '面试中如何回答优缺点问题？',
        '如何提升职场沟通能力？',
        '如何进行有效的职业规划？'
      ],
      aiResponses: {
        '如何优化我的简历？': '优化简历的几个关键点：1. 突出量化的工作成果；2. 使用关键词匹配目标职位要求；3. 保持简洁明了，控制在1-2页；4. 强调与目标岗位相关的技能和经验；5. 注意排版和格式的专业性。',
        '面试中如何回答优缺点问题？': '回答缺点时，可以选择一个与工作关系不大但正在改进的缺点；回答优点时，选择与目标岗位相关且有实例支持的优点。记住要保持真实性，避免过于笼统的回答。',
        '如何提升职场沟通能力？': '提升职场沟通能力的方法：1. 学会倾听，避免打断他人；2. 清晰表达观点，使用简洁明了的语言；3. 根据不同对象调整沟通方式；4. 注重非语言沟通，如肢体语言和表情；5. 学会反馈和确认。',
        '如何进行有效的职业规划？': '有效的职业规划需要：1. 明确自己的职业目标和价值观；2. 评估自身技能和兴趣；3. 了解行业趋势和发展机会；4. 制定短期和长期计划；5. 持续学习和适应变化；6. 定期评估和调整计划。'
      }
    }
  },
  methods: {
    showHistory() {
      console.log('显示历史记录')
    },
    showSettings() {
      console.log('显示设置')
    },
    askQuestion(question) {
      // 设置输入框内容为快捷问题
      this.userInput = question
      // 发送消息
      this.sendMessage()
    },
    sendMessage() {
      if (!this.userInput.trim()) return
      
      const userMessage = this.userInput.trim()
      
      // 添加用户消息到聊天历史
      this.addMessage('user', userMessage)
      
      // 清空输入框
      this.userInput = ''
      
      // 模拟AI回复延迟
      setTimeout(() => {
        // 根据问题内容生成回复
        const aiResponse = this.generateResponse(userMessage)
        this.addMessage('ai', aiResponse)
        
        // 滚动到底部
        this.scrollToBottom()
      }, 800)
      
      // 滚动到底部
      this.scrollToBottom()
    },
    addMessage(sender, content) {
      const chatHistory = this.$refs.chatHistory
      const messageDiv = document.createElement('div')
      messageDiv.className = `chat-message ${sender}-message`
      
      // 添加头像
      if (sender === 'user') {
        messageDiv.innerHTML = `
          <div class="user-avatar"></div>
          <div class="message-content">${content}</div>
        `
      } else {
        messageDiv.innerHTML = `
          <div class="ai-avatar-small"></div>
          <div class="message-content">${content}</div>
        `
      }
      
      chatHistory.appendChild(messageDiv)
    },
    generateResponse(question) {
      // 检查是否有预设的回复
      for (const [key, response] of Object.entries(this.aiResponses)) {
        if (question.includes(key)) {
          return response
        }
      }
      
      // 默认回复
      return `感谢您的提问！关于"${question}"，我建议您考虑以下几点：\n\n1. 明确您的具体需求和目标\n2. 收集相关信息和资料\n3. 制定切实可行的行动计划\n4. 寻求专业人士的建议\n\n如果您需要更详细的指导，请提供更多背景信息，我可以为您提供更有针对性的建议。`
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatHistory = this.$refs.chatHistory
        if (chatHistory) {
          chatHistory.scrollTop = chatHistory.scrollHeight
        }
      })
    }
  }
}
</script>

<style scoped>
.ai-assistant-container {
  background-color: #f5f5f5;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.ai-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.ai-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.ai-actions {
  display: flex;
  gap: 16px;
}

.history-btn,
.settings-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.history-btn .icon,
.settings-btn .icon {
  width: 24px;
  height: 24px;
  color: #666;
}

/* AI介绍卡片 */
.ai-intro-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 16px;
  padding: 20px;
  border-radius: 16px;
  text-align: center;
  color: white;
}

.ai-avatar {
  width: 80px;
  height: 80px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
}

.ai-icon {
  width: 40px;
  height: 40px;
  color: white;
}

.ai-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px;
}

.ai-description {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

/* 快捷问题 */
.quick-questions {
  padding: 0 16px 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px;
}

.quick-question-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.quick-question-item {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 12px;
  font-size: 14px;
  color: #666;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;
}

.quick-question-item:active {
  background-color: #f5f5f5;
  transform: scale(0.98);
}

/* 聊天历史区域 */
.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  -webkit-overflow-scrolling: touch;
}

.chat-message {
  display: flex;
  margin-bottom: 16px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-avatar {
  width: 36px;
  height: 36px;
  background-color: #4a90e2;
  border-radius: 50%;
  margin-right: 10px;
  flex-shrink: 0;
}

.ai-avatar-small {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  margin-right: 10px;
  flex-shrink: 0;
}

.system-message {
  justify-content: center;
}

.message-content {
  background-color: #fff;
  padding: 10px 14px;
  border-radius: 16px;
  max-width: 75%;
  word-wrap: break-word;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  line-height: 1.5;
}

.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  background-color: #4a90e2;
  color: white;
  margin-right: 10px;
  margin-left: 0;
}

.user-message .user-avatar {
  margin-right: 0;
  margin-left: 10px;
}

/* 输入区域 */
.input-area {
  background-color: #fff;
  padding: 12px 16px;
  box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.1);
}

.input-container {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 24px;
  padding: 8px 12px;
}

.text-input {
  flex: 1;
  border: none;
  background: none;
  padding: 8px;
  font-size: 16px;
  color: #333;
  outline: none;
}

.send-btn {
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  flex-shrink: 0;
}

.send-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.send-btn .icon {
  width: 20px;
  height: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-intro-card {
    padding: 16px;
    margin: 14px;
  }
  
  .ai-avatar {
    width: 70px;
    height: 70px;
  }
  
  .quick-question-item {
    padding: 10px;
    font-size: 13px;
  }
  
  .message-content {
    max-width: 80%;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .ai-title {
    font-size: 17px;
  }
  
  .ai-name {
    font-size: 17px;
  }
  
  .ai-description {
    font-size: 13px;
  }
  
  .quick-question-list {
    gap: 8px;
  }
  
  .quick-question-item {
    padding: 9px;
    font-size: 12px;
    border-radius: 10px;
  }
  
  .chat-history {
    padding: 12px;
  }
  
  .message-content {
    max-width: 85%;
    font-size: 12px;
    padding: 9px 12px;
  }
  
  .user-avatar,
  .ai-avatar-small {
    width: 32px;
    height: 32px;
  }
}
</style>