<template>
  <div class="my-jobs-container">
    <!-- 消息提示弹窗 -->
    <div v-if="showMessage" class="message-overlay" @click="showMessage = false">
      <div class="message-box" :class="messageType" @click.stop>
        <div class="message-content">
          {{ messageText }}
        </div>
        <button class="close-btn" @click="showMessage = false">&times;</button>
      </div>
    </div>
    
    <!-- 顶部导航栏 -->
    <header class="my-jobs-header">
      <button class="back-btn" @click="goBack">
        <svg class="icon" viewBox="0 0 1024 1024" width="24" height="24">
          <path d="M724 218.3V141c0-6.7-7.7-10.4-12.9-6.3L260.3 486.8c-16.4 12.8-16.4 37.5 0 50.3l450.8 352.1c5.3 4.1 12.9 0.4 12.9-6.3v-77.3c0-4.9-2.3-9.6-6.1-12.6l-360-281 360-281.1c3.8-3 6.1-7.7 6.1-12.6z" />
        </svg>
      </button>
      <h1 class="page-title">我的业务</h1>
      <button class="add-btn" @click="showAddJobForm">
        <svg class="icon" viewBox="0 0 1024 1024" width="24" height="24">
          <path d="M482 152h60q8 0 8 8v704q0 8-8 8h-60q-8 0-8-8V160q0-8 8-8z" />
          <path d="M176 474h672q8 0 8 8v60q0 8-8 8H176q-8 0-8-8v-60q0-8 8-8z" />
        </svg>
      </button>
    </header>

    <!-- 业务列表 -->
    <div class="jobs-list">
      <div v-if="jobs.length === 0" class="empty-state">
        <svg class="empty-icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M20 6h-4V4c0-1.11-.89-2-2-2h-4c-1.11 0-1.99.89-1.99 2v2H4c-1.11 0-1.99.89-1.99 2L2 19c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2zm-6 0h-4V4h4v2z"/>
        </svg>
        <p>暂无业务信息</p>
        <button class="add-first-btn" @click="showAddJobForm">添加业务</button>
      </div>
      
      <div v-else class="jobs-grid">
        <div 
          v-for="job in jobs" 
          :key="job.id"
          class="job-card"
        >
          <div class="job-header">
            <h3 class="job-title">{{ job.title }}</h3>
            <div class="job-actions">
              <button class="edit-btn" @click="editJob(job)">
                <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
                </svg>
              </button>
              <button class="delete-btn" @click="deleteJob(job.id)">
                <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                </svg>
              </button>
            </div>
          </div>
          <div class="job-info">
            <div class="job-tags" v-if="job.skills && job.skills.length > 0">
              <span v-for="skill in parseJsonArray(job.skills)" :key="skill" class="job-tag">{{ skill }}</span>
            </div>
            <p class="job-experience" v-if="job.experience">从业时间: {{ job.experience }}年</p>
            <p class="job-price" v-if="job.price">单价: ¥{{ job.price }}/次</p>
            <div v-if="job.location && job.location.length > 0" class="job-location">
              <span v-for="(city, index) in parseLocation(job.location)" :key="index" class="location-tag">
                {{ city }}
              </span>
            </div>
            <p class="job-description">{{ job.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑业务表单 -->
    <div v-if="showForm" class="form-overlay" @click="closeForm">
      <div class="form-container" @click.stop>
        <h2 class="form-title">{{ editingJob !== null ? '编辑业务' : '添加业务' }}</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label class="form-label">业务名称</label>
            <input
              type="text" 
              v-model="formData.title" 
              class="form-input" 
              placeholder="请输入业务名称" 
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">业务类型</label>
            <select v-model="formData.jobType" class="form-input">
              <option disabled value="">请选择业务类型</option>
              <option
                v-for="type in jobTypes"
                :key="type.id"
                :value="type.id"
              >
                {{ type.name }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label class="form-label">擅长标签 (最多3个，用逗号分隔)</label>
            <input 
              type="text" 
              v-model="formData.skillsInput" 
              class="form-input" 
              placeholder="请输入擅长标签，用逗号分隔"
              @keydown.enter.prevent="handleSkillsInput"
              @blur="handleSkillsInput"
            />
            <span v-if="formData.skills.length >= 3" class="skill-limit-hint">已达到最大标签数量(3个)</span>
            <div class="skill-tags" v-if="formData.skills && formData.skills.length > 0">
              <span v-for="(skill, index) in formData.skills" :key="index" class="skill-tag">
                {{ skill }}
                <button type="button" @click="removeSkill(index)" class="remove-tag">×</button>
              </span>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-label">单价 (¥/次)</label>
            <input 
              type="number" 
              v-model="formData.price" 
              class="form-input" 
              placeholder="请输入单价" 
              min="0" 
              step="0.01"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">从业时间 (年)</label>
            <input 
              type="number" 
              v-model="formData.experience" 
              class="form-input" 
              placeholder="请输入从业时间" 
              min="0" 
              step="1"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">主要工作城市 (最多3个)</label>
            <div v-for="(city, index) in formData.cities" :key="index" class="city-selector-item">
              <el-cascader
                size="large"
                :options="options"
                v-model="formData.cities[index]"
                @change="handleCityChange"
                placeholder="请选择城市">
              </el-cascader>
              <button type="button" @click="removeCity(index)" class="remove-city">×</button>
            </div>
            <button 
              v-if="formData.cities.length < 3" 
              type="button" 
              @click="addCityField" 
              class="add-city-btn">
              + 添加城市
            </button>
            <div class="city-tags-preview" v-if="formData.cities && formData.cities.length > 0">
              <span v-for="(city, index) in formData.cities" :key="index" class="city-tag">
                {{ getCityLabel(city) }}
              </span>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-label">业务描述</label>
            <textarea 
              v-model="formData.description" 
              class="form-textarea" 
              placeholder="请输入业务描述" 
              rows="4"
              required
            ></textarea>
          </div>
          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="closeForm">取消</button>
            <button type="submit" class="submit-btn" :disabled="isSubmitting">
              {{ isSubmitting ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { getJobs, createJob, updateJob, deleteJob, getJobTypes } from '../api/job.js'
import { provinceAndCityData, codeToText } from 'element-china-area-data'

export default {
  name: 'MyJobs',
  emits: ['navigate'],
  data() {
    return {
      currentUser: null,
      jobs: [],
      showForm: false,
      editingJob: null,
      isSubmitting: false,
      // 错误提示相关
      showMessage: false,
      messageText: '',
      messageType: 'error', // 'success' 或 'error'
      messageTimer: null,
      options: provinceAndCityData,
      jobTypes: [],
      formData: {
        id: '',
        title: '',
        skills: [],
        skillsInput: '',
        price: '',
        experience: '',
        cities: [],
        citiesInput: '',
        description: '',
        jobType: ''
      }
    }
  },
  mounted() {
    // 确保用户信息已加载
    this.loadJobTypes()
    this.checkUserInfo()
  },
  methods: {
    // 检查用户信息
    checkUserInfo() {
      const userInfo = localStorage.getItem('userInfo')
      if (!userInfo) {
        // 如果没有用户信息，尝试从后端获取当前用户
        this.loadCurrentUser()
      } else {
        // 有用户信息，直接加载业务列表
        this.loadJobs()
      }
    },

    // 加载职位类型
    async loadJobTypes() {
      try {
        const response = await getJobTypes()
        if (response && response.data) {
          this.jobTypes = response.data
        }
      } catch (error) {
        console.error('获取职位类型失败:', error)
      }
    },
    
    // 加载当前用户信息
    async loadCurrentUser() {
      try {
        const { getCurrentUser } = await import('../api/user.js')
        const response = await getCurrentUser()
        if (response.data) {
          localStorage.setItem('userInfo', JSON.stringify(response.data))
        }
        // 无论是否获取到用户信息，都尝试加载业务列表
        this.loadJobs()
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 即使获取用户信息失败，也尝试加载业务列表
        this.loadJobs()
      }
    },
    
    // 解析JSON数组字符串
    parseJsonArray(value) {
      // 如果已经是数组，直接返回
      if (Array.isArray(value)) {
        return value;
      }
      
      // 如果是字符串，尝试解析为数组
      if (typeof value === 'string') {
        // 如果是空字符串，返回空数组
        if (!value.trim()) {
          return [];
        }
        
        try {
          // 尝试解析JSON
          const parsed = JSON.parse(value);
          // 确保解析结果是数组
          if (Array.isArray(parsed)) {
            return parsed;
          } else {
            // 如果不是数组，将其包装成数组
            return [parsed];
          }
        } catch (e) {
          // 如果JSON解析失败，按逗号分割字符串
          return value.split(',').map(item => item.trim()).filter(item => item);
        }
      }
      
      // 其他情况返回空数组
      return [];
    },
    
    // 解析位置信息
    parseLocation(value) {
      // 如果是数组，直接返回
      if (Array.isArray(value)) {
        // 如果数组元素是字符串，尝试解析每个元素
        return value.map(item => {
          if (typeof item === 'string' && item.startsWith('[')) {
            try {
              return JSON.parse(item).join('/');
            } catch (e) {
              return item;
            }
          }
          return item;
        });
      }
      
      // 如果是字符串，尝试解析
      if (typeof value === 'string') {
        // 如果是空字符串，返回空数组
        if (!value.trim()) {
          return [];
        }
        
        try {
          // 尝试解析JSON
          const parsed = JSON.parse(value);
          // 确保解析结果是数组
          if (Array.isArray(parsed)) {
            return parsed.map(item => {
              if (typeof item === 'string' && item.startsWith('[')) {
                try {
                  return JSON.parse(item).join('/');
                } catch (e) {
                  return item;
                }
              }
              return item;
            });
          } else {
            // 如果不是数组，将其包装成数组
            return [parsed];
          }
        } catch (e) {
          // 如果JSON解析失败，按逗号分割字符串
          return value.split(',').map(item => item.trim()).filter(item => item);
        }
      }
      
      // 其他情况返回空数组
      return [];
    },
    
    // 加载业务信息
    async loadJobs() {
      try {
        const response = await getJobs()
        // 转换后端数据字段为前端显示格式
        this.jobs = (response.data || []).map(job => {
          let skills = []
          try {
            // 尝试解析JSON格式的skill字段
            skills = typeof job.skill === 'string' && job.skill ? JSON.parse(job.skill) : []
            // 兼容旧的skills字段
            if (!Array.isArray(skills) && typeof job.skills === 'string') {
              skills = JSON.parse(job.skills)
            }
            // 确保skills是数组
            if (!Array.isArray(skills)) {
              skills = []
            }
          } catch (e) {
            // 如果解析失败，尝试拆分字符串
            if (typeof job.skill === 'string') {
              skills = job.skill.split(',').map(s => s.trim()).filter(Boolean)
            } else if (typeof job.skills === 'string') {
              skills = job.skills.split(',').map(s => s.trim()).filter(Boolean)
            }
          }
          
          // 处理城市数据（现在是workCity字段）
          let location = []
          try {
            if (typeof job.workCity === 'string') {
              if (job.workCity.startsWith('[')) {
                // 如果已经是JSON数组格式，解析为数组
                location = JSON.parse(job.workCity)
                // 确保是数组
                if (!Array.isArray(location)) {
                  location = []
                }
              } else if (job.workCity.trim()) {
                // 如果是普通字符串，转换为包含单个元素的数组
                location = [job.workCity.trim()]
              }
            } else if (Array.isArray(job.workCity)) {
              // 如果已经是数组，直接使用
              location = job.workCity
            }
          } catch (e) {
            console.error('解析城市数据失败:', e)
            // 出错时使用空数组
            location = []
          }
          
          return {
            id: job.id,
            title: job.job || job.jobName, // 后端现在是job字段，兼容旧的jobName
            skills: skills,
            description: job.introduce || job.description || '',  // 兼容description字段
            location: location,
            price: job.price,
            experience: job.workDuration !== undefined && job.workDuration !== null ? job.workDuration : (job.experience || ''), // 优先使用workDuration字段，兼容旧的experience，确保不为undefined
            jobType: job.jobType
          }
        })
      } catch (error) {
          console.error('加载业务信息失败:', error)
          // 加载失败时使用模拟数据
          this.jobs = this.getMockJobs()
      }
    },
    
    // 获取模拟业务数据
    getMockJobs() {
      return [
        {
          id: 1,
          title: '前端开发服务',
          skills: ['Vue.js', 'React', 'JavaScript'],
          location: '北京',
          price: '200',
          experience: '5',
          description: '提供专业的Web前端开发服务，包括网站建设、单页应用开发、前端性能优化等。'
        },
        {
          id: 2,
          title: 'UI设计服务',
          skills: ['UI设计', '交互设计', 'Figma'],
          location: '上海',
          price: '300',
          experience: '3',
          description: '提供高质量的用户界面设计和用户体验设计服务，帮助提升产品价值。'
        }
      ]
    },
    
    // 显示添加业务表单
    showAddJobForm() {
      this.editingJob = null
      this.formData = {
        id: '',
        title: '',
        skills: [],
        skillsInput: '',
        price: '',
        experience: '',
        cities: [], // 初始化为空数组
        citiesInput: '',
        description: '',
        jobType: ''
      }
      this.showForm = true
    },
    
    // 编辑业务
    editJob(job) {
      this.editingJob = job
      
      // 处理技能数据
      let skills = []
      try {
        // 处理技能数据
        if (job.skill) {
          if (Array.isArray(job.skill)) {
            // 如果已经是数组，直接使用
            skills = job.skill
          } else if (typeof job.skill === 'string') {
            // 如果是字符串，尝试解析为数组
            if (job.skill.startsWith('[')) {
              skills = JSON.parse(job.skill)
            } else {
              // 如果不是JSON数组格式，作为单个技能处理
              skills = [job.skill.trim()]
            }
            // 确保是数组
            if (!Array.isArray(skills)) {
              skills = []
            }
          }
        }
        // 兼容旧的skills字段
        else if (job.skills) {
          if (Array.isArray(job.skills)) {
            skills = job.skills
          } else if (typeof job.skills === 'string') {
            if (job.skills.startsWith('[')) {
              skills = JSON.parse(job.skills)
            } else {
              skills = [job.skills.trim()]
            }
            if (!Array.isArray(skills)) {
              skills = []
            }
          }
        }
      } catch (e) {
        console.error('解析技能数据失败:', e)
        // 出错时使用空数组
        skills = []
      }
      
      // 处理城市数据
      let cities = []
      try {
        // 从job.workCity字段获取城市数据（后端实际字段是workCity）
        if (job.workCity) {
          if (Array.isArray(job.workCity)) {
            // 如果已经是数组，直接使用
            cities = job.workCity
          } else if (typeof job.workCity === 'string') {
            // 如果是字符串，尝试解析为数组
            if (job.workCity.startsWith('[')) {
              cities = JSON.parse(job.workCity)
            } else {
              // 如果不是JSON数组格式，作为单个城市处理
              cities = [job.workCity.trim()]
            }
            // 确保是数组
            if (!Array.isArray(cities)) {
              cities = []
            }
          }
        }
      } catch (e) {
        console.error('解析城市数据失败:', e)
        // 出错时使用空数组
        cities = []
      }
      
      // 转换城市数据为级联选择器所需的格式
      const formattedCities = cities.map(cityStr => {
        // 如果城市数据已经是数组格式，直接返回
        if (Array.isArray(cityStr)) {
          // 如果是完整的省市数组，直接返回
          return cityStr;
        }
        
        // 如果是字符串格式，尝试匹配到级联选择器的数据
        // 这里简化处理，实际应用中可能需要更复杂的匹配逻辑
        // 为了确保编辑时能显示已有数据，我们至少返回一个空数组
        return [];
      })
      
      // 添加调试信息
      console.log('编辑业务数据:', job)
      console.log('提取的技能数据:', skills)
      console.log('提取的城市数据:', cities)
      console.log('格式化后的城市数据:', formattedCities)
      
      // 确保从业时间是字符串类型
      const experienceValue = job.workDuration !== undefined && job.workDuration !== null ? String(job.workDuration) : 
                            (job.experience !== undefined ? String(job.experience) : '')
      
      this.formData = {
        id: job.id,
        title: job.job || job.title || '', // 兼容job字段
        skills: skills,
        skillsInput: '',
        price: job.price !== undefined ? String(job.price) : '',
        experience: experienceValue,
        cities: formattedCities,
        citiesInput: '',
        description: job.introduce || job.description || '',
        jobType: job.jobType !== undefined && job.jobType !== null ? String(job.jobType) : ''
      }
      this.showForm = true
    },
    
    // 删除业务
    async deleteJob(jobId) {
        if (confirm('确定要删除这个业务信息吗？')) {
        try {
          await deleteJob(jobId)
          // 刷新列表
          this.loadJobs()
        } catch (error) {
          console.error('删除失败:', error)
          alert('删除失败，请重试')
        }
      }
    },
    
    // 处理技能输入
    handleSkillsInput() {
      // 如果已有3个标签，不再添加
      if (this.formData.skills.length >= 3) {
        this.formData.skillsInput = ''
        return
      }
      this.updateSkills()
    },
    
    // 添加技能标签
    updateSkills() {
      if (this.formData.skillsInput) {
        const newSkills = this.formData.skillsInput.split(',').map(s => s.trim()).filter(Boolean)
        // 合并并去重
        const combinedSkills = [...new Set([...this.formData.skills, ...newSkills])]
        // 限制最多3个标签
        this.formData.skills = combinedSkills.slice(0, 3)
        // 清空输入框
        this.formData.skillsInput = ''
      }
    },
    
    // 移除技能标签
    removeSkill(index) {
      this.formData.skills.splice(index, 1)
    },
    
    // 提交表单
    async submitForm() {
      // 更新技能标签
      this.updateSkills()

      if (!this.formData.jobType) {
        this.showErrorMessage('请选择业务类型')
        return
      }

      this.isSubmitting = true
      try {
        // 格式化城市数据以发送到后端
        const formDataToSend = {
          ...this.formData,
          cities: this.formData.cities.map(cityCodes => 
            cityCodes.map(code => codeToText[code]).join('/')
          )
        }
        
        if (!this.editingJob) {
          // 添加新业务
          await createJob(formDataToSend)
        } else {
          // 更新现有业务
          await updateJob(formDataToSend)
        }
        
        // 刷新列表
        this.loadJobs()
        // 自动关闭表单
        this.closeForm()
        
        // 显示成功提示
        this.showSuccessMessage('保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        console.log('错误详情:', JSON.stringify(error, null, 2))
        // 显示更友好的错误信息，确保能正确处理后端返回的错误信息
        let errorMessage = '保存失败，请重试'
        
        // 检查不同的错误响应格式
        try {
          if (error.response && error.response.data) {
            const data = error.response.data
            console.log('响应数据:', data)
            
            if (data.message) {
              errorMessage = data.message
            } else if (data.errorMsg) {
              errorMessage = data.errorMsg
            } else if (data.success === false && data.errorMsg) {
              // 特别处理后端返回的成功状态为false的情况
              errorMessage = data.errorMsg
            } else if (data.error) {
              errorMessage = typeof data.error === 'string' ? data.error : data.error.message || '未知错误'
            } else if (typeof data === 'string') {
              errorMessage = data
            } else if (typeof data === 'object' && !Array.isArray(data)) {
              // 处理对象类型的错误响应，尝试提取有用信息
              const errorKeys = Object.keys(data)
              if (errorKeys.length > 0) {
                // 尝试获取第一个错误信息
                const firstValue = data[errorKeys[0]]
                errorMessage = typeof firstValue === 'string' ? firstValue : JSON.stringify(data)
              }
            }
          } else if (error.message) {
            errorMessage = error.message
          } else if (error.toString) {
            errorMessage = error.toString()
          }
        } catch (e) {
          console.error('解析错误信息失败:', e)
        }
        
        console.log('最终错误消息:', errorMessage)
        // 使用自定义弹窗替代alert
        this.showErrorMessage(errorMessage)
      } finally {
        this.isSubmitting = false
      }
    },
    
    // 处理城市输入
    handleCitiesInput() {
      // 如果已有3个城市，不再添加
      if (this.formData.cities.length >= 3) {
        this.formData.citiesInput = ''
        return
      }
      this.updateCities()
    },
    
    // 添加城市标签
    updateCities() {
      if (this.formData.citiesInput) {
        const newCities = this.formData.citiesInput.split(',').map(c => c.trim()).filter(Boolean)
        // 合并并去重
        const combinedCities = [...new Set([...this.formData.cities, ...newCities])]
        // 限制最多3个城市
        this.formData.cities = combinedCities.slice(0, 3)
        // 清空输入框
        this.formData.citiesInput = ''
      }
    },
    
    // 移除城市标签
    removeCity(index) {
      this.formData.cities.splice(index, 1)
    },
    
    // 处理城市变更
    handleCityChange(value) {
      // 城市变更处理已在 v-model 中自动完成
      console.log('城市变更:', value)
    },
    
    // 获取城市标签
    getCityLabel(cityCodes) {
      if (!cityCodes || cityCodes.length === 0) return ''
      
      // 根据 element-china-area-data 的规则来获取城市名称
      return cityCodes.map(code => codeToText[code]).join('/')
    },
    
    // 添加城市字段
    addCityField() {
      this.formData.cities.push([])
    },
    
    // 显示错误消息
    showErrorMessage(message) {
      this.messageText = message
      this.messageType = 'error'
      this.showMessage = true
      
      // 3秒后自动关闭
      if (this.messageTimer) {
        clearTimeout(this.messageTimer)
      }
      this.messageTimer = setTimeout(() => {
        this.showMessage = false
      }, 3000)
    },
    
    // 显示成功消息
    showSuccessMessage(message) {
      this.messageText = message
      this.messageType = 'success'
      this.showMessage = true
      
      // 2秒后自动关闭
      if (this.messageTimer) {
        clearTimeout(this.messageTimer)
      }
      this.messageTimer = setTimeout(() => {
        this.showMessage = false
      }, 2000)
    },
    
    // 关闭表单
    closeForm() {
      this.showForm = false
      this.editingJob = null
      this.formData = {
        id: '',
        title: '',
        skills: [],
        skillsInput: '',
        price: '',
        experience: '',
        cities: [], // 初始化为空数组
        citiesInput: '',
        description: '',
        jobType: ''
      }
    },
    
    // 返回上一页
    goBack() {
      this.$emit('navigate', 'profile')
    }
  }
}
</script>

<style scoped>
.my-jobs-container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding-bottom: 20px;
}

/* 城市选择器样式 */
.city-selector-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.city-selector-item .el-cascader {
  flex: 1;
}

.remove-city {
  background: none;
  border: none;
  color: #ff4d4f;
  font-size: 20px;
  cursor: pointer;
  margin-left: 10px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-city-btn {
  background-color: #f0f8ff;
  color: #1989fa;
  border: 1px dashed #1989fa;
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 14px;
  cursor: pointer;
  margin-bottom: 10px;
}

.city-tags-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.city-tag {
  background-color: #fff7e6;
  color: #fa8c16;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  display: inline-flex;
  align-items: center;
}

/* 消息提示弹窗样式 */
.message-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.message-box {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  min-width: 280px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  position: relative;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.message-box.error {
  border-left: 4px solid #ff4d4f;
}

.message-box.success {
  border-left: 4px solid #52c41a;
}

.message-content {
  font-size: 16px;
  color: #333;
  margin-right: 20px;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #666;
}

/* 顶部导航栏 */
.my-jobs-header {
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

.back-btn, .add-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 8px 12px;
  color: #666;
}

.add-btn .icon {
  width: 24px;
  height: 24px;
  color: #1989fa;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 业务列表 */
.jobs-list {
  padding: 16px;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-icon {
  width: 64px;
  height: 64px;
  color: #ccc;
  margin-bottom: 16px;
}

.empty-state p {
  color: #999;
  margin-bottom: 20px;
  font-size: 14px;
}

.add-first-btn {
  background-color: #1989fa;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 24px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-first-btn:active {
  background-color: #096dd9;
}

/* 业务卡片网格 */
.jobs-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.job-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.job-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  flex: 1;
}

.job-actions {
  display: flex;
  gap: 8px;
}

.edit-btn, .delete-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
}

.edit-btn:hover {
  background-color: #f0f0f0;
}

.delete-btn:hover {
  background-color: #fff2f0;
}

.edit-btn .icon, .delete-btn .icon {
  width: 20px;
  height: 20px;
}

.edit-btn .icon {
  color: #1989fa;
}

.delete-btn .icon {
  color: #ff4d4f;
}

.job-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.job-location {
  font-size: 13px;
  color: #666;
  margin: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
}

.job-location::before {
  content: '📍';
  margin-right: 4px;
  flex-shrink: 0;
}

.location-tag {
  background-color: #fff7e6;
  color: #fa8c16;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.job-description {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0;
}

/* 标签样式 */
.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 6px;
}

.job-tag {
  background-color: #e6f7ff;
  color: #1890ff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.job-experience {
  font-size: 13px;
  color: #666;
  margin: 4px 0;
}

.job-price {
  font-size: 13px;
  color: #ff4d4f;
  margin: 4px 0;
  font-weight: 500;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.skill-tag {
  background-color: #e6f7ff;
  color: #1890ff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  display: inline-flex;
  align-items: center;
}

.remove-tag {
  background: none;
  border: none;
  color: #1890ff;
  margin-left: 4px;
  cursor: pointer;
  font-size: 16px;
  line-height: 1;
  padding: 0;
}

.skill-limit-hint {
  color: #ff4d4f;
  font-size: 12px;
  display: block;
  margin-top: 4px;
}

.city-limit-hint {
  color: #ff4d4f;
  font-size: 12px;
  display: block;
  margin-top: 4px;
}

.city-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.city-tag {
  background-color: #fff7e6;
  color: #fa8c16;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  display: inline-flex;
  align-items: center;
}

/* 表单样式 */
.form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
}

.form-container {
  background-color: #fff;
  border-radius: 12px;
  padding: 24px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.form-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
  text-align: center;
}

.form-group {
  margin-bottom: 16px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 6px;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:focus, .form-textarea:focus {
  outline: none;
  border-color: #1989fa;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.cancel-btn, .submit-btn {
  flex: 1;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.cancel-btn {
  background-color: #fff;
  border: 1px solid #d9d9d9;
  color: #666;
}

.cancel-btn:active {
  background-color: #f5f5f5;
}

.submit-btn {
  background-color: #1989fa;
  border: 1px solid #1989fa;
  color: #fff;
}

.submit-btn:active {
  background-color: #096dd9;
  border-color: #096dd9;
}

.submit-btn:disabled {
  background-color: #ccc;
  border-color: #ccc;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .my-jobs-header {
    padding: 10px 14px;
  }
  
  .page-title {
    font-size: 17px;
  }
  
  .jobs-list {
    padding: 14px;
  }
  
  .job-card {
    padding: 14px;
  }
  
  .job-title {
    font-size: 15px;
  }
  
  .form-container {
    width: 95%;
    padding: 20px;
    margin: 0 10px;
  }
}
</style>