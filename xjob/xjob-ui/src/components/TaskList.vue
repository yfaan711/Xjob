<template>
  <div class="task-list-container">
    <!-- 顶部导航栏 -->
    <header class="task-header">
      <h1 class="task-title">技能广场</h1>
    </header>
    
    <!-- 筛选栏 -->
    <div class="filter-section">
      <div class="filter-scroll">
        <!-- 城市筛选 -->
        <div class="filter-item" @click="toggleCityFilter">
          <span class="filter-label">{{ selectedCity || '城市' }}</span>
          <svg class="filter-icon" width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path d="M6 9L12 15L18 9" stroke="#999" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        
        <!-- 其他筛选项可以在这里添加 -->
      </div>
    </div>
    
    <!-- 城市筛选面板 -->
    <div v-if="showCityFilter" class="filter-panel" :class="{ active: showCityFilter }">
      <div class="filter-panel-header">
        <h3 class="filter-panel-title">选择城市</h3>
        <button class="filter-panel-close" @click="showCityFilter = false">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6L18 18" stroke="#999" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </button>
      </div>
      
      <div class="filter-options">
        <div 
          v-for="city in cities" 
          :key="city"
          class="filter-option"
          @click="selectCity(city)"
        >
          <span class="option-label">{{ city }}</span>
          <svg v-if="selectedCity === city" class="check-icon" width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z" fill="#667eea"/>
          </svg>
        </div>
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
          <h3 class="task-title">{{ task.title }}</h3>
          <p class="task-description">{{ task.description }}</p>
        </div>
        
        <!-- 任务底部信息 -->
        <div class="task-footer">
          <div class="task-price">
            <span class="price-symbol">¥</span>
            <span class="price-amount">{{ task.price }}</span>
            <span class="price-unit">/小时</span>
          </div>
          <div class="task-location">
            {{ task.location }}
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
import { getAllJobs } from '../api/job.js'

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
      selectedCity: '',
      cities: [
        '北京', '上海', '广州', '深圳', '杭州', '南京', '苏州', '天津', 
        '成都', '武汉', '西安', '重庆', '青岛', '大连', '厦门', '福州'
      ],
      defaultAvatar: 'https://picsum.photos/200/200?random=1'
    }
  },
  mounted() {
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
    
    // 加载任务数据
    async loadTasks(reset = false) {
      if (this.loading) return
      
      this.loading = true
      
      try {
        // 如果是重置，重置页码和任务列表
        if (reset) {
          this.currentPage = 1
          this.tasks = []
          this.hasMore = true
        }
        
        // 如果没有更多数据，不再请求
        if (!this.hasMore) {
          this.loading = false
          return
        }
        
        // 调用API获取分页数据
        const response = await getAllJobs(this.currentPage, this.pageSize)
        
        if (response && response.data) {
          // 转换后端数据为前端显示格式
          const newTasks = response.data.map(job => {
            // 解析城市数据
            let location = '未指定地点'
            try {
              if (typeof job.workCity === 'string' && job.workCity) {
                if (job.workCity.startsWith('[')) {
                  const cities = JSON.parse(job.workCity)
                  if (Array.isArray(cities) && cities.length > 0) {
                    location = cities[0] // 取第一个城市
                  }
                } else {
                  location = job.workCity
                }
              }
            } catch (e) {
              console.error('解析城市数据失败:', e)
            }
            
            // 解析技能数据
            let skills = []
            try {
              if (typeof job.skill === 'string' && job.skill) {
                skills = JSON.parse(job.skill)
              }
            } catch (e) {
              console.error('解析技能数据失败:', e)
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
          if (this.selectedCity) {
            filteredTasks = newTasks.filter(task => task.location.includes(this.selectedCity))
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
    
    // 查看任务详情
    viewTaskDetail(taskId) {
      console.log('查看任务详情:', taskId)
      // 这里可以实现导航到任务详情页面的逻辑
    }
  }
}
</script>

<style scoped>
.task-list-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 60px;
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

.task-content .task-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  text-align: left;
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
  font-size: 12px;
  color: #999;
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
