<template>
  <div class="task-list-container">
    <!-- 顶部导航栏 -->
    <header class="task-header">
      <h1 class="task-title">业务广场</h1>
    </header>
    
    <!-- 职位类型 -->
    <div class="job-type-section" v-if="jobTypes && jobTypes.length">
      <div class="job-type-grid">
        <button
          v-for="type in jobTypes"
          :key="type.id"
          class="job-type-btn"
          :class="{ active: selectedJobType === type.id }"
          @click="selectJobType(type.id)"
        >
          {{ type.name }}
        </button>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-section">
      <div class="filter-scroll">
        <!-- 城市筛选 -->
        <div class="filter-item" @click="toggleCityFilter">
          <span class="filter-label">{{ selectedCityLabel || '城市' }}</span>
          <svg class="filter-icon" width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path d="M6 9L12 15L18 9" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        
        <!-- 搜索框 -->
        <div class="search-container">
          <input 
            v-model="searchKeyword" 
            @input="handleSearchInput"
            @keyup.enter="performSearch"
            class="search-input" 
            placeholder="搜索你想要查询的业务..."
            type="text"
          />
          <button @click="performSearch" class="search-button">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <circle cx="11" cy="11" r="8" stroke="#999" stroke-width="2"/>
              <path d="M21 21L16.65 16.65" stroke="#999" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </button>
        </div>
      </div>
    </div>
    
    <!-- 城市筛选面板 -->
    <div v-if="showCityFilter" class="city-filter-dropdown">
      <div class="filter-panel-header">
        <h3 class="filter-panel-title">选择城市</h3>
        <button class="filter-panel-close" @click="showCityFilter = false">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6L18 18" stroke="#999" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </button>
      </div>
      
      <!-- 使用级联选择器 -->
      <div class="cascader-container">
        <el-cascader
          v-model="selectedCity"
          :options="cityOptions"
          @change="handleCityChange"
          placeholder="请选择城市"
          clearable
          filterable
          size="large"
          style="width: 100%; padding: 16px 20px;">
        </el-cascader>
      </div>
      
      <div class="filter-actions">
        <button class="reset-btn" @click="resetCityFilter">重置</button>
        <button class="confirm-btn" @click="confirmCityFilter">确定</button>
      </div>
    </div>
    
    <!-- 任务列表 -->
    <div class="task-list" v-if="tasks.length > 0">
      <div 
        v-for="task in tasks" 
        :key="task.id" 
        class="task-card"
        @click="viewTaskDetail(task.id)"
      >
        <!-- 服务提供者信息 -->
        <div class="provider-info">
          <img :src="task.provider.icon || defaultAvatar" alt="用户头像" class="provider-avatar" @error="handleImageError" />
          <div class="provider-details">
            <div class="provider-name-row">
              <h3 class="provider-name">{{ task.provider.nickname || '匿名用户' }}</h3>
            </div>
            <div class="provider-meta">
              <span class="provider-experience">{{ task.provider.experience || '经验未知' }}</span>
            </div>
          </div>
        </div>
        
        <!-- 任务内容 -->
        <div class="task-content">
          <div class="task-header-row">
            <h3 class="task-title">{{ task.title }}</h3>
            <div class="task-skills-inline" v-if="task.skills && task.skills.length > 0">
              <span v-for="(skill, index) in task.skills" :key="index" class="skill-tag-inline">{{ skill }}</span>
            </div>
          </div>
          <p class="task-description">{{ task.description }}</p>
        </div>
        
        <!-- 任务底部信息 -->
        <div class="task-footer">
          <div class="task-price">
            <span class="price-symbol">¥</span>
            <span class="price-amount">{{ task.price }}</span>
            <span class="price-unit">/小时</span>
          </div>
          <div class="task-location" v-if="task.location && task.location.length">
            <span v-for="(city, index) in task.location" :key="index" class="location-tag">{{ city }}</span>
          </div>
          <div class="task-location empty" v-else>
            未指定地点
          </div>
        </div>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <span class="loading-text">加载中...</span>
    </div>
    
    <!-- 没有更多数据提示 -->
    <div v-if="!loading && !hasMore && tasks.length > 0" class="no-more-data">
      <span class="no-more-text">没有更多数据了</span>
    </div>
    
    <!-- 空状态 -->
    <div v-if="!loading && tasks.length === 0 && !loading" class="empty-state">
      <svg class="empty-icon" width="64" height="64" viewBox="0 0 24 24" fill="none">
        <path d="M8 6H6C4.89543 6 4 6.89543 4 8V18C4 19.1046 4.89543 20 6 20H18C19.1046 20 20 19.1046 20 18V10C20 8.89543 19.1046 8 18 8H16" stroke="#CCCCCC" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M16 8V6C16 4.89543 15.1046 4 14 4H8C6.89543 4 6 4.89543 6 6V8" stroke="#CCCCCC" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M9 13L11 13" stroke="#CCCCCC" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M9 17L13 17" stroke="#CCCCCC" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
      <p class="empty-text">暂无任务信息</p>
    </div>
  </div>
</template>

<script>
import { getAllJobs, getJobTypes } from '../api/job.js'
import { provinceAndCityData, codeToText } from 'element-china-area-data'

export default {
  name: 'TaskList',
  data() {
    return {
      tasks: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      hasMore: true,
      showCityFilter: false,
      selectedCity: [], // 级联选择器使用数组
      selectedCityLabel: '', // 显示在筛选按钮上的城市标签
      cityOptions: provinceAndCityData, // 使用 element-china-area-data 提供的数据
      defaultAvatar: 'https://picsum.photos/200/200?random=1',
      searchKeyword: '', // 搜索关键词
      searchTimer: null, // 搜索防抖定时器
      jobTypes: [],
      selectedJobType: null
    }
  },
  mounted() {
    this.loadJobTypes()
    this.loadTasks(true)
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    // 处理图片加载错误，显示默认头像
    handleImageError(event) {
      event.target.src = this.defaultAvatar
    },
    
    // 处理城市选择变更
    handleCityChange(val) {
      console.log('城市选择变更:', val)
      // 城市选择变更时更新标签
      if (val && val.length > 0) {
        // 可以根据需要处理城市选择变更
      }
    },
    
    // 重置城市筛选
    resetCityFilter() {
      this.selectedCity = []
      this.selectedCityLabel = ''
      this.showCityFilter = false
      // 重新加载数据
      this.loadTasks(true)
    },
    
    // 确认城市筛选
    confirmCityFilter() {
      // 获取选择的城市标签
      if (this.selectedCity && this.selectedCity.length > 0) {
        // 使用 codeToText 来获取城市名称
        this.selectedCityLabel = this.selectedCity.map(code => codeToText[code]).join(' / ')
      } else {
        this.selectedCityLabel = ''
      }
      this.showCityFilter = false
      // 重新加载数据
      this.loadTasks(true)
    },

    // 加载职位类型
    async loadJobTypes() {
      try {
        const response = await getJobTypes()
        if (response && response.data) {
          this.jobTypes = response.data
        }
      } catch (error) {
        console.error('加载职位类型失败:', error)
      }
    },
    
    // 加载任务数据
    async loadTasks(reset = false) {
      if (this.loading) return

      if (reset) {
        this.currentPage = 1
        this.hasMore = true
      }

      this.loading = true

      try {
        // 调用API获取分页数据
        const response = await getAllJobs(this.currentPage, this.pageSize, this.selectedJobType)
        
        if (response && response.data) {
          // 转换后端数据为前端显示格式
          const newTasks = response.data.map(job => {
            // 解析城市数据
            let location = []
            try {
              if (typeof job.workCity === 'string' && job.workCity) {
                if (job.workCity.startsWith('[')) {
                  const cities = JSON.parse(job.workCity)
                  if (Array.isArray(cities) && cities.length > 0) {
                    // 修改：显示所有城市，而不是只显示第一个
                    location = cities
                  }
                } else if (job.workCity.includes(',')) {
                  location = job.workCity
                    .split(',')
                    .map(city => city.trim())
                    .filter(Boolean)
                } else {
                  location = [job.workCity.trim()]
                }
              } else if (Array.isArray(job.workCity) && job.workCity.length > 0) {
                location = job.workCity
              }
            } catch (e) {
              console.error('解析城市数据失败:', e)
            }
            
            // 解析业务数据（同时兼容 skill 与 skills 字段，以及中英文分隔符）
            const normalizeSkills = value => {
              if (Array.isArray(value)) {
                return value.filter(skill => typeof skill === 'string').map(skill => skill.trim()).filter(Boolean)
              }
              if (typeof value === 'string' && value.trim()) {
                if (value.trim().startsWith('[')) {
                  const parsed = JSON.parse(value)
                  return Array.isArray(parsed)
                    ? parsed.filter(skill => typeof skill === 'string').map(skill => skill.trim()).filter(Boolean)
                    : []
                }
                return value
                  .split(/[，,、]/)
                  .map(skill => skill.trim())
                  .filter(Boolean)
              }
              return []
            }

            let skills = []
            try {
              skills = normalizeSkills(job.skill)
              if (skills.length === 0) {
                skills = normalizeSkills(job.skills)
              }
            } catch (e) {
              console.error('解析业务数据失败:', e)
            }
            
            return {
              id: job.id,
              title: job.job || '未命名任务',
              description: job.introduce || '暂无描述',
              skills: skills,
              price: job.price || 0,
              location: location,
              provider: {
                id: job.userId,
                nickname: job.nickName || '匿名用户',
                icon: job.icon,
                experience: job.workDuration ? `${job.workDuration}年经验` : '经验未知'
              }
            }
          })
          
          // 如果有城市筛选条件，进行筛选
          let filteredTasks = newTasks
          if (this.selectedCity && this.selectedCity.length > 0) {
            // 获取选择的城市名称
            const selectedCityName = codeToText[this.selectedCity[this.selectedCity.length - 1]]
            // 筛选包含该城市的数据
            filteredTasks = newTasks.filter(task => {
              if (Array.isArray(task.location)) {
                return task.location.some(city => city.includes(selectedCityName))
              }
              return task.location.includes(selectedCityName)
            })
          }
          
          // 如果有搜索关键词，进行搜索过滤
          if (this.searchKeyword) {
            const keyword = this.searchKeyword.toLowerCase()
            filteredTasks = filteredTasks.filter(task => {
              // 检查标题、描述、技能和城市是否包含关键词
              const titleMatch = task.title && task.title.toLowerCase().includes(keyword)
              const descriptionMatch = task.description && task.description.toLowerCase().includes(keyword)
              const skillsMatch = task.skills && task.skills.some(skill => 
                skill && skill.toLowerCase().includes(keyword)
              )
              const locationMatch = task.location && (
                Array.isArray(task.location) 
                  ? task.location.some(loc => loc && loc.toLowerCase().includes(keyword))
                  : task.location.toLowerCase().includes(keyword)
              )
              
              return titleMatch || descriptionMatch || skillsMatch || locationMatch
            })
          }
          
          // 将任务添加到任务列表
          if (reset) {
            this.tasks = filteredTasks
          } else {
            this.tasks = [...this.tasks, ...filteredTasks]
          }
          
          // 更新分页信息
          if (response.total) {
            const totalPages = Math.ceil(response.total / this.pageSize)
            this.hasMore = this.currentPage < totalPages
          } else {
            // 如果没有返回总数，根据返回的数据量判断是否还有更多
            this.hasMore = newTasks.length === this.pageSize
          }
          
          // 如果有更多数据，增加页码
          if (this.hasMore) {
            this.currentPage++
          }
        }
      } catch (error) {
        console.error('加载任务数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 处理滚动事件，实现下拉加载更多
    handleScroll() {
      // 如果正在加载或没有更多数据，不处理
      if (this.loading || !this.hasMore) return
      
      // 获取滚动位置
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop
      const windowHeight = window.innerHeight
      const documentHeight = document.documentElement.scrollHeight
      
      // 当滚动到距离底部100px以内时，加载更多数据
      if (scrollTop + windowHeight >= documentHeight - 100) {
        this.loadTasks(false)
      }
    },
    
    // 切换城市筛选面板显示
    toggleCityFilter() {
      this.showCityFilter = !this.showCityFilter
    },
    
    // 选择城市
    selectCity(city) {
      this.selectedCity = this.selectedCity === city ? '' : city
      this.showCityFilter = false
      // 重新加载数据
      this.loadTasks(true)
    },

    // 选择职位类型
    selectJobType(typeId) {
      this.selectedJobType = this.selectedJobType === typeId ? null : typeId
      this.currentPage = 1
      this.hasMore = true
      this.loadTasks(true)
    },
    
    // 查看任务详情
    viewTaskDetail(taskId) {
      console.log('查看任务详情:', taskId)
      // 这里可以实现导航到任务详情页面的逻辑
    },
    
    // 处理搜索输入（防抖）
    handleSearchInput() {
      // 清除之前的定时器
      if (this.searchTimer) {
        clearTimeout(this.searchTimer)
      }
      
      // 设置新的定时器
      this.searchTimer = setTimeout(() => {
        this.performSearch()
      }, 300) // 300ms防抖
    },
    
    // 执行搜索
    performSearch() {
      // 重置分页并重新加载数据
      this.currentPage = 1
      this.loadTasks(true)
    }
  }
}
</script>

<style scoped>
.task-list-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 60px;
  position: relative;
}

/* 职位类型 */
.job-type-section {
  background-color: #fff;
  padding: 12px 16px 8px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.job-type-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px 10px;
}

.job-type-btn {
  padding: 8px 10px;
  background-color: #f5f5f5;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  color: #555;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.job-type-btn.active {
  background-color: #ffecec;
  color: #ff4d4f;
  font-weight: 600;
}

.job-type-btn:active {
  transform: scale(0.98);
}

/* 顶部导航栏 */
.task-header {
  padding: 16px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.task-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
  text-align: center;
}

/* 筛选栏 */
.filter-section {
  background-color: #fff;
  padding: 12px 0;
  margin-bottom: 12px;
  position: sticky;
  top: 56px;
  z-index: 99;
}

.filter-scroll {
  display: flex;
  overflow-x: auto;
  padding: 0 16px;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
}

.filter-scroll::-webkit-scrollbar {
  display: none;
}

.filter-item {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  background-color: #f5f5f5;
  border-radius: 20px;
  margin-right: 12px;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-item:active {
  background-color: #e8e8e8;
}

.filter-label {
  font-size: 14px;
  color: #666;
  margin-right: 6px;
}

.filter-icon {
  flex-shrink: 0;
}

/* 搜索框样式 */
.search-container {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 20px;
  padding: 4px 12px;
  flex: 1;
  max-width: 300px;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 8px 0;
  font-size: 14px;
  outline: none;
  color: #333;
}

.search-input::placeholder {
  color: #999;
}

.search-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 筛选面板 */
.filter-panel {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-radius: 20px 20px 0 0;
  max-height: 60vh;
  z-index: 1000;
  transform: translateY(100%);
  transition: transform 0.3s ease;
  box-shadow: 0 -5px 20px rgba(0, 0, 0, 0.1);
}

.filter-panel.active {
  transform: translateY(0);
}

.filter-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.filter-panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-panel-close {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.filter-options {
  padding: 16px 20px;
  max-height: calc(60vh - 60px);
  overflow-y: auto;
}

.filter-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.filter-option:last-child {
  border-bottom: none;
}

.option-label {
  font-size: 15px;
  color: #333;
}

.check-icon {
  flex-shrink: 0;
}

/* 任务列表 */
.task-list {
  padding: 12px 16px;
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #999;
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 8px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 14px;
  color: #999;
}

/* 没有更多数据提示 */
.no-more-data {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.no-more-text {
  font-size: 14px;
  color: #999;
}

.task-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.task-card:active {
  transform: scale(0.98);
}

/* 服务提供者信息 */
.provider-info {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.provider-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.provider-details {
  flex: 1;
}

.provider-name-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 4px;
}

.provider-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.provider-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.provider-experience {
  font-size: 12px;
  color: #999;
}

/* 任务内容 */
.task-content {
  margin-bottom: 12px;
}

.task-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.task-content .task-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0;
  text-align: left;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.task-skills-inline {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: flex-end;
  margin-left: 10px;
}

.skill-tag-inline {
  background-color: #e6f7ff;
  color: #1890ff;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  white-space: nowrap;
  flex-shrink: 0;
}

.task-description {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0 0 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 任务底部信息 */
.task-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f5f5f5;
}

.task-price {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 14px;
  color: #ff6b6b;
  font-weight: 600;
  margin-right: 2px;
}

.price-amount {
  font-size: 20px;
  color: #ff6b6b;
  font-weight: 700;
}

.price-unit {
  font-size: 12px;
  color: #999;
  margin-left: 4px;
}

.task-location {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  font-size: 12px;
  color: #999;
}

.task-location.empty {
  color: #c0c4cc;
}

.location-tag {
  background: #f3f4f6;
  color: #606266;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  line-height: 1.4;
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
  margin-bottom: 16px;
}

.empty-text {
  font-size: 15px;
  color: #999;
  margin-bottom: 24px;
}

/* 城市筛选下拉面板 */
.city-filter-dropdown {
  position: absolute;
  top: 100px; /* 位于筛选栏下方 */
  left: 16px;
  right: 16px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
}

/* 筛选面板头部 */
.filter-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.filter-panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-panel-close {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 级联选择器容器 */
.cascader-container {
  border-bottom: 1px solid #f0f0f0;
}

/* 筛选操作按钮 */
.filter-actions {
  display: flex;
  padding: 16px 20px;
  gap: 12px;
}

.reset-btn,
.confirm-btn {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  border: none;
}

.reset-btn {
  background-color: #f5f5f5;
  color: #666;
}

.confirm-btn {
  background-color: #1989fa;
  color: white;
}

/* 技能标签样式 */
.task-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 8px;
}

.skill-tag {
  background-color: #e6f7ff;
  color: #1890ff;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  white-space: nowrap;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .task-header {
    padding: 14px 16px;
  }
  
  .task-title {
    font-size: 17px;
  }
  
  .filter-item {
    padding: 7px 14px;
    margin-right: 10px;
  }
  
  .filter-label {
    font-size: 13px;
  }
  
  .task-card {
    padding: 14px;
    margin-bottom: 14px;
  }
  
  .provider-avatar {
    width: 44px;
    height: 44px;
    margin-right: 10px;
  }
  
  .provider-name {
    font-size: 15px;
  }
  
  .task-content .task-title {
    font-size: 14px;
  }
  
  .task-description {
    font-size: 13px;
  }
  
  .price-amount {
    font-size: 18px;
  }
}
</style>
