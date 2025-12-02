<template>
  <div class="login-container">
    <!-- 关闭按钮 -->
    <button class="close-btn" @click="handleClose">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
        <path d="M18 6L6 18M6 6L18 18" stroke="#999" stroke-width="2" stroke-linecap="round"/>
      </svg>
    </button>
    
    <!-- 顶部品牌区域 -->
    <div class="brand-section">
      <div class="logo-container">
        <div class="logo">
          <span class="logo-text">X</span>
        </div>
      </div>
      <h1 class="app-name">X job</h1>
      <p class="slogan">传递每个人的价值</p>
    </div>
    
    <!-- 登录表单卡片 -->
    <div class="login-form">
      <h2 class="form-title">欢迎回来</h2>
      
      <!-- 手机号输入 -->
      <div class="input-group">
        <input 
          type="tel" 
          v-model="formData.phone" 
          placeholder="请输入手机号" 
          maxlength="11"
          class="input"
          autocomplete="tel"
        />
        <div class="input-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M20 15.5C18.8 15.5 17.5 15.3 16.4 14.9C16.3 14.9 16.2 14.9 16.1 14.9C15.8 14.9 15.6 15 15.4 15.2L13.2 17.4C10.4 15.9 8 13.6 6.6 10.8L8.8 8.6C9.1 8.3 9.2 7.9 9 7.6C8.7 6.6 8.5 5.2 8.5 4C8.5 3.5 8 3 7.5 3H4C3.5 3 3 3.5 3 4C3 13.4 10.6 21 20 21C20.5 21 21 20.5 21 20V16.5C21 16 20.5 15.5 20 15.5Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
      </div>
      
      <!-- 验证码/密码输入区域 -->
      <div class="input-group" v-if="loginType === 'code'">
        <input 
          type="text" 
          v-model="formData.code" 
          placeholder="请输入验证码" 
          maxlength="6"
          class="input"
          autocomplete="one-time-code"
        />
        <div class="input-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M12 15V17M12 7V13M22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12Z" stroke="#999" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </div>
        <button 
          class="code-btn" 
          @click="handleSendCode"
          :disabled="countdown > 0"
        >
          {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
        </button>
      </div>
      
      <div class="input-group" v-else>
        <input 
          :type="showPassword ? 'text' : 'password'" 
          v-model="formData.password" 
          placeholder="请输入密码" 
          class="input"
          autocomplete="current-password"
        />
        <div class="input-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M12 15V16M12 8V10M5.5 19.5V19.5001M18.5 19.5V19.5001M12 19.5C15.5899 19.5 18.5 16.5899 18.5 13C18.5 12.7097 18.4838 12.4232 18.4526 12.1411C19.8919 11.4618 21 10.0339 21 8.5C21 5.46243 18.5376 3 15.5 3H8.5C5.46243 3 3 5.46243 3 8.5C3 10.0339 4.10812 11.4618 5.54739 12.1411C5.51619 12.4232 5.5 12.7097 5.5 13C5.5 16.5899 8.41015 19.5 12 19.5Z" stroke="#999" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </div>
        <button class="eye-btn" @click="togglePassword" type="button">
          <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M1 12C1 12 4 7 12 7C20 7 23 12 23 12C23 12 20 17 12 17C4 17 1 12 1 12Z" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="12" cy="12" r="4" stroke="#999" stroke-width="1.5"/>
          </svg>
          <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M3 9C3 9 5.25483 5.5 12 5.5C18.7452 5.5 21 9 21 9M3 15C3 15 5.25483 18.5 12 18.5C18.7452 18.5 21 15 21 15M21 9L15 15M3 15L9 9" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
      
      <!-- 登录按钮 -->
      <button class="login-btn" @click="handleLogin" type="button">
        登录
      </button>
      
      <!-- 新用户说明 -->
      <div class="new-user-note">
        <span class="note-text">新用户将为您创建账户</span>
      </div>
      
      <!-- 切换登录方式 -->
      <div class="switch-login">
        <span @click="toggleLoginType" class="switch-text">
          {{ loginType === 'code' ? '使用密码登录' : '使用验证码登录' }}
        </span>
      </div>
      
      <!-- 错误提示 -->
      <div class="error-message" v-if="errorMsg">{{ errorMsg }}</div>
    </div>
    
    <!-- 底部版权信息 -->
    <div class="footer">
      <p class="copyright">© 2024 Xjob. All rights reserved.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { sendCode, login } from '../api/user'

// 定义emit事件
const emit = defineEmits(['login-success', 'close'])

// 登录表单数据
const formData = reactive({
  phone: '',
  code: '',
  password: ''
})

// 登录方式：'code' 或 'password'
const loginType = ref('code')

// 显示密码
const showPassword = ref(false)

// 验证码倒计时
const countdown = ref(0)

// 错误提示
const errorMsg = ref('')

// 切换登录方式
const toggleLoginType = () => {
  loginType.value = loginType.value === 'code' ? 'password' : 'code'
  errorMsg.value = ''
}

// 切换密码显示状态
const togglePassword = () => {
  showPassword.value = !showPassword.value
}

// 处理关闭登录
const handleClose = () => {
  // 触发关闭事件，通知父组件
  emit('close')
}

// 验证手机号
const validatePhone = (phone) => {
  const phoneRegex = /^1[3-9]\d{9}$/
  return phoneRegex.test(phone)
}

// 发送验证码
const handleSendCode = async () => {
  // 验证手机号
  if (!formData.phone) {
    errorMsg.value = '请输入手机号'
    return
  }
  
  if (!validatePhone(formData.phone)) {
    errorMsg.value = '请输入正确的手机号'
    return
  }
  
  try {
    console.log('准备发送验证码请求，手机号:', formData.phone)
    const data = await sendCode(formData.phone)
    console.log('响应数据:', data)
    
    if (data.success) {
      // 开始倒计时
      startCountdown()
      errorMsg.value = ''
    } else {
      errorMsg.value = data.errorMsg || '发送验证码失败'
    }
  } catch (error) {
    console.error('发送验证码请求失败:', error)
    // 直接使用error对象中的errorMsg或message
    errorMsg.value = error.errorMsg || error.message || '网络错误，请稍后重试'
  }
}

// 开始倒计时
const startCountdown = () => {
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

// 处理登录
const handleLogin = async () => {
  // 验证手机号
  if (!formData.phone) {
    errorMsg.value = '请输入手机号'
    return
  }
  
  if (!validatePhone(formData.phone)) {
    errorMsg.value = '请输入正确的手机号'
    return
  }
  
  // 验证验证码或密码
  if (loginType.value === 'code') {
    if (!formData.code) {
      errorMsg.value = '请输入验证码'
      return
    }
    
    if (formData.code.length !== 6) {
      errorMsg.value = '验证码格式不正确'
      return
    }
  } else {
    if (!formData.password) {
      errorMsg.value = '请输入密码'
      return
    }
  }
  
  try {
    // 准备登录数据，根据登录类型设置字段
    const loginData = {
      phone: formData.phone
    }
    
    // 根据登录类型添加相应字段
    if (loginType.value === 'code') {
      loginData.code = formData.code
    } else {
      loginData.password = formData.password
    }
    
    // 发送登录请求，使用API服务
    const data = await login(loginData)
    console.log('登录响应数据:', data)
    
    if (data.success) {
      // 登录成功，保存token
      const token = data.data
      if (token) {
        console.log('获取到token:', token)
        // 保存token到localStorage
        localStorage.setItem('token', token)
      }
      
      console.log('登录成功:', data)
      // 触发登录成功事件，通知父组件
      emit('login-success')
      // 这里可以添加登录成功后的逻辑，比如跳转到首页
    } else {
      errorMsg.value = data.errorMsg || '登录失败'
    }
  } catch (error) {
    console.error('登录请求失败:', error)
    errorMsg.value = '网络错误，请稍后重试'
  }
}
</script>

<style scoped>
/* 主容器 - 使用渐变背景 */
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  color: white;
  position: relative;
  max-width: 100%;
  width: 100%;
}

/* 关闭按钮样式 */
.close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

/* 品牌区域 */
.brand-section {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeInUp 0.6s ease-out;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

.logo {
  width: 70px;
  height: 70px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.logo-text {
  font-size: 32px;
  font-weight: 700;
  color: white;
}

.app-name {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px;
  color: white;
}

.slogan {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
  font-weight: 400;
}

/* 登录表单卡片 - 使用毛玻璃效果 */
.login-form {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 30px 24px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideUp 0.5s ease-out;
  color: #333;
}

.form-title {
  font-size: 22px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

/* 输入框组 */
.input-group {
  position: relative;
  margin-bottom: 22px;
}

.input {
  width: 100%;
  height: 52px;
  padding: 0 50px 0 45px;
  border: 1px solid #e0e0e0;
  border-radius: 16px;
  font-size: 16px;
  background: white;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-1px);
}

.input::placeholder {
  color: #999;
}

/* 输入框图标 */
.input-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
}

/* 验证码按钮 */
.code-btn {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  padding: 8px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 100px;
  text-align: center;
}

.code-btn:hover:not(:disabled) {
  transform: translateY(-50%) scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.code-btn:disabled {
  background: #e0e0e0;
  color: #999;
  cursor: not-allowed;
  transform: translateY(-50%) scale(1);
}

/* 眼睛图标按钮 */
.eye-btn {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
  transition: transform 0.2s ease;
}

.eye-btn:active {
  transform: translateY(-50%) scale(0.9);
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 54px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 16px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.login-btn:hover::before {
  left: 100%;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

/* 新用户说明 */
.new-user-note {
  text-align: center;
  margin: 8px 0 12px;
}

.note-text {
  font-size: 12px;
  color: #999;
  opacity: 0.9;
}

/* 切换登录方式 */
.switch-login {
  text-align: center;
  margin-bottom: 8px;
}

.switch-text {
  color: #667eea;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: color 0.3s ease;
}

.switch-text:hover {
  color: #764ba2;
  text-decoration: underline;
}

/* 错误提示 */
.error-message {
  color: #ff4757;
  font-size: 14px;
  text-align: center;
  margin-top: 15px;
  padding: 10px;
  background: rgba(255, 71, 87, 0.1);
  border-radius: 8px;
  border: 1px solid rgba(255, 71, 87, 0.2);
  animation: shake 0.5s;
}

/* 底部版权信息 */
.footer {
  text-align: center;
  padding-top: 20px;
}

.copyright {
  font-size: 12px;
  opacity: 0.8;
  margin: 0;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

/* 手机端适配 */
@media (max-width: 768px) {
  .login-container {
    padding: 16px 12px;
    min-height: auto;
  }
  
  .close-btn {
    top: 16px;
    right: 16px;
    width: 32px;
    height: 32px;
  }
  
  .brand-section {
    margin-bottom: 16px;
  }
  
  .logo-container {
    margin-bottom: 8px;
  }
  
  .logo {
    width: 50px;
    height: 50px;
    border-radius: 14px;
  }
  
  .logo-text {
    font-size: 24px;
  }
  
  .app-name {
    font-size: 20px;
    margin-bottom: 4px;
  }
  
  .slogan {
    font-size: 12px;
  }
  
  .login-form {
    padding: 20px 16px;
    border-radius: 16px;
  }
  
  .form-title {
    font-size: 18px;
    margin-bottom: 16px;
  }
  
  .input-group {
    margin-bottom: 14px;
  }
  
  .input {
    height: 44px;
    font-size: 15px;
    padding: 0 45px 0 40px;
    border-radius: 12px;
  }
  
  .login-btn {
    height: 46px;
    font-size: 15px;
    margin-bottom: 12px;
    border-radius: 12px;
  }
  
  .new-user-note {
    margin: 6px 0 10px;
  }
  
  .note-text {
    font-size: 11px;
  }
  
  .switch-text {
    font-size: 13px;
  }
  
  .copyright {
    font-size: 10px;
  }
}

/* 小屏幕手机优化 */
@media (max-width: 480px) {
  .login-container {
    padding: 12px 10px;
  }
  
  .logo {
    width: 46px;
    height: 46px;
  }
  
  .logo-text {
    font-size: 22px;
  }
  
  .app-name {
    font-size: 18px;
  }
  
  .login-form {
    padding: 16px 14px;
  }
  
  .form-title {
    font-size: 17px;
  }
  
  .input {
    height: 42px;
    font-size: 14px;
  }
  
  .login-btn {
    height: 44px;
    font-size: 14px;
  }
}
</style>