<template>
  <div class="edit-profile-container">
    <!-- 顶部导航栏 -->
    <header class="edit-profile-header">
      <button class="back-btn" @click="goBack">返回</button>
      <h1 class="page-title">编辑资料</h1>
      <button class="save-btn" @click="saveProfile" :disabled="isSaving">
        {{ isSaving ? '保存中...' : '保存' }}
      </button>
    </header>

    <form class="edit-profile-form">
      <!-- 基本信息（头像和昵称） -->
      <div class="form-section">
        <h2 class="section-title">基本信息</h2>
        
        <!-- 头像和昵称 -->
        <div class="basic-info-container">
          <!-- 头像上传 -->
          <div class="avatar-upload-section">
            <div class="avatar-preview">
              <img :src="formData.avatar" alt="用户头像" class="avatar-img" />
            </div>
            <div class="avatar-upload-btn">
              <input 
                type="file" 
                ref="avatarInput" 
                @change="handleAvatarUpload" 
                accept="image/*" 
                style="display: none;"
              />
              <button
                type="button"
                @click="$refs.avatarInput.click()"
                class="upload-button"
                :disabled="isUploadingAvatar"
              >
                {{ isUploadingAvatar ? '上传中...' : '更换头像' }}
              </button>
            </div>
          </div>
          
          <!-- 昵称 -->
          <div class="form-item nickname-item">
            <label class="form-label">昵称</label>
            <input 
              type="text" 
              v-model="formData.nickname" 
              class="form-input" 
              placeholder="请输入昵称"
              maxlength="15"
            />
            <div class="char-count">{{ formData.nickname.length }}/15</div>
          </div>
        </div>
      </div>

      <!-- 详细信息 -->
      <div class="form-section">
        <h2 class="section-title">详细信息</h2>
        
        <!-- 性别 -->
        <div class="form-item">
          <label class="form-label">性别</label>
          <div class="gender-select">
            <label class="radio-label">
              <input 
                type="radio" 
                v-model="formData.gender" 
                value="male"
                class="custom-radio"
              />
              <span class="radio-circle"></span>
              <span class="radio-text">男</span>
            </label>
            <label class="radio-label">
              <input 
                type="radio" 
                v-model="formData.gender" 
                value="female"
                class="custom-radio"
              />
              <span class="radio-circle"></span>
              <span class="radio-text">女</span>
            </label>
            <label class="radio-label">
              <input 
                type="radio" 
                v-model="formData.gender" 
                value="secret"
                class="custom-radio"
              />
              <span class="radio-circle"></span>
              <span class="radio-text">保密</span>
            </label>
          </div>
        </div>

        <!-- 生日 -->
        <div class="form-item">
          <label class="form-label">生日</label>
          <input 
            type="date" 
            v-model="formData.birthday" 
            class="form-input"
            :max="today"
          />
        </div>

        <!-- 所在城市 -->
        <div class="form-item">
          <label class="form-label">所在城市</label>
          <input 
            type="text" 
            v-model="formData.city" 
            class="form-input" 
            placeholder="请输入所在城市"
            maxlength="50"
          />
        </div>

        <!-- 简介 -->
        <div class="form-item">
          <label class="form-label">个人简介</label>
          <textarea 
            v-model="formData.introduce" 
            class="form-textarea" 
            placeholder="介绍一下自己吧"
            maxlength="200"
            rows="4"
          ></textarea>
          <div class="char-count">{{ formData.introduce.length }}/200</div>
        </div>
        

      </div>
    </form>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { updateUserInfo, updateUserProfile, getCurrentUser, getUserInfo, uploadAvatar } from '../api/user.js'

export default {
  name: 'EditProfile',
  setup(props, { emit }) {
    const isSaving = ref(false)
    const avatarInput = ref(null)
    const isUploadingAvatar = ref(false)
    
    // 表单数据
    const formData = reactive({
      avatar: 'https://picsum.photos/200/200?random=1',
      nickname: '',
      city: '',
      introduce: '',
      gender: 'secret',
      birthday: ''
    })
    
    // 今天的日期，用于限制生日选择
    const today = computed(() => {
      const date = new Date()
      return date.toISOString().split('T')[0]
    })
    
    // 加载用户信息
    const loadUserInfo = async () => {
      try {
        // 获取用户基本信息
        const userData = await getCurrentUser()
        if (userData && userData.data) {
          formData.nickname = userData.data.nickName || ''
          formData.avatar = userData.data.icon || formData.avatar
        }
        
        // 获取用户详细信息
        const userId = userData?.data?.id
        if (userId) {
          const userInfoData = await getUserInfo(userId)
          if (userInfoData && userInfoData.data) {
            formData.city = userInfoData.data.city || ''
            formData.introduce = userInfoData.data.introduce || ''
            
            // 转换gender字段：后端使用boolean类型，前端使用字符串
            if (userInfoData.data.gender === false) {
              formData.gender = 'male' // 0：男
            } else if (userInfoData.data.gender === true) {
              formData.gender = 'female' // 1：女
            } else {
              formData.gender = 'secret'
            }
            
            formData.birthday = userInfoData.data.birthday || ''
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    }
    
    // 处理头像上传
    const handleAvatarUpload = async (event) => {
      const file = event.target.files[0]
      if (!file) return

      const previousAvatar = formData.avatar
      const reader = new FileReader()
      reader.onload = (e) => {
        formData.avatar = e.target.result
      }
      reader.readAsDataURL(file)

      isUploadingAvatar.value = true
      try {
        const response = await uploadAvatar(file)
        if (response?.data) {
          formData.avatar = response.data
          alert('头像上传成功')
        } else {
          formData.avatar = previousAvatar
          alert(response?.errorMsg || '上传头像失败，请稍后重试')
        }
      } catch (error) {
        formData.avatar = previousAvatar
        alert(error.errorMsg || '上传头像失败，请稍后重试')
      } finally {
        isUploadingAvatar.value = false
        event.target.value = ''
      }
    }
    
    // 保存资料
    const saveProfile = async () => {
      // 表单验证
      if (!formData.nickname.trim()) {
        alert('昵称不能为空')
        return
      }
      if (formData.nickname.length > 15) {
        alert('昵称不能超过15个字符')
        return
      }
      if (formData.introduce.length > 200) {
        alert('个人简介不能超过200个字符')
        return
      }
      
      isSaving.value = true
      
      try {
        // 1. 保存用户基本信息（nickName）
        const userUpdateData = {
          nickName: formData.nickname // 头像通过独立接口更新
        }
        await updateUserInfo(userUpdateData)
        
        // 2. 保存用户详细信息（gender, birthday, city, introduce）
        // 转换gender字段：后端使用boolean类型（0：男，1：女），前端使用字符串
        let genderValue = null
        if (formData.gender === 'male') {
          genderValue = false // 0：男
        } else if (formData.gender === 'female') {
          genderValue = true // 1：女
        }
        
        const profileData = {
          city: formData.city || undefined,
          introduce: formData.introduce || undefined,
          gender: genderValue,
          birthday: formData.birthday || undefined
        }
        await updateUserProfile(profileData)
        
        alert('保存成功')
        // 保存成功后导航回个人资料页
        setTimeout(() => {
          // 使用emit通知父组件导航到个人资料页
          emit('navigate', 'profile')
        }, 500)
      } catch (error) {
        console.error('保存资料失败:', error)
        alert('保存失败，请重试')
      } finally {
        isSaving.value = false
      }
    }
    
    // 返回上一页
    const goBack = () => {
      // 使用emit通知父组件导航到个人资料页
      emit('navigate', 'profile')
    }
    
    // 组件挂载时加载用户信息
    onMounted(() => {
      loadUserInfo()
    })
    
    return {
      formData,
      isSaving,
      avatarInput,
      isUploadingAvatar,
      today,
      handleAvatarUpload,
      saveProfile,
      goBack
    }
  }
}
</script>

<style scoped>
.edit-profile-container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding-bottom: 80px; /* 增加底部padding以避免被导航栏遮挡 */
}

/* 顶部导航栏 */
.edit-profile-header {
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

.back-btn, .save-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 8px 12px;
}

.back-btn {
  color: #666;
}

.save-btn {
  color: #1989fa;
  font-weight: 500;
}

.save-btn:disabled {
  color: #ccc;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 表单样式 */
.edit-profile-form {
  background-color: #fff;
  margin-top: 10px;
}

.form-section {
  padding: 16px;
  border-bottom: 1px solid #f5f5f5;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
}

/* 基本信息容器 */
.basic-info-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

/* 头像上传 */
.avatar-upload-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-preview {
  margin-bottom: 12px;
}

.avatar-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f0f0f0;
}

.upload-button {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.upload-button:active {
  background-color: #f5f5f5;
}

/* 昵称样式 */
.nickname-item {
  width: 100%;
  max-width: 400px;
  margin-bottom: 0;
}

/* 表单项 */
.form-item {
  margin-bottom: 16px;
  position: relative;
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.form-input, .form-select, .form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  color: #333;
  background-color: #fff;
  box-sizing: border-box;
}

.form-input:focus, .form-select:focus, .form-textarea:focus {
  outline: none;
  border-color: #1989fa;
}

.form-textarea {
  resize: none;
  line-height: 1.5;
}

.char-count {
  position: absolute;
  bottom: -20px;
  right: 0;
  font-size: 12px;
  color: #999;
}

/* 性别选择 */
.gender-select {
  display: flex;
  gap: 24px;
  padding: 8px 0;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 0;
  transition: color 0.2s;
}

.radio-label:hover {
  color: #1989fa;
}

/* 隐藏原生单选按钮 */
.custom-radio {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

/* 自定义圆形单选按钮 */
.radio-circle {
  width: 20px;
  height: 20px;
  border: 2px solid #ccc;
  border-radius: 50%;
  display: inline-block;
  position: relative;
  transition: all 0.2s;
}

/* 选中状态样式 */
.custom-radio:checked + .radio-circle {
  border-color: #1989fa;
  background-color: #fff;
}

/* 选中后的内圆点 */
.custom-radio:checked + .radio-circle::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #1989fa;
  animation: radio-pulse 0.2s ease-out;
}

@keyframes radio-pulse {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 0;
  }
  70% {
    transform: translate(-50%, -50%) scale(1.2);
  }
  100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 1;
  }
}

.radio-text {
  font-size: 15px;
  color: #333;
  transition: color 0.2s;
}

.radio-label:hover .radio-text {
  color: #1989fa;
}

.custom-radio:checked ~ .radio-text {
  color: #1989fa;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .edit-profile-header {
    padding: 12px 14px;
  }
  
  .form-section {
    padding: 14px;
  }
}
</style>