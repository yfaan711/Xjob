<template>
  <div class="plaza-container">
    <!-- 顶部导航栏 -->
    <header class="plaza-header">
      <!-- 城市选择器 -->
      <button class="city-selector" @click="showCitySelector">
        <span class="current-city">{{ currentCity }}</span>
        <svg class="icon dropdown-icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M7 10l5 5 5-5z"/>
        </svg>
      </button>
      
      <!-- 搜索按钮 -->
      <button class="search-btn" @click="showSearchPage">
        <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
        </svg>
      </button>
    </header>
    
    <!-- 推荐标签 -->
    <div class="tags-section">
      <div class="tag-scroll-container">
        <button 
          v-for="tag in recommendTags" 
          :key="tag.id"
          class="tag-item"
          :class="{ active: selectedTag === tag.name }"
          @click="selectTag(tag.name)"
        >
          {{ tag.name }}
        </button>
      </div>
    </div>
    
    <!-- 博客列表 - 两列布局 -->
    <div class="blog-list">
      <div class="blog-grid">
        <BlogCard 
          v-for="blog in blogList" 
          :key="blog.id"
          :image-url="blog.imageUrl"
          :title="blog.title"
          :user-name="blog.userName"
          :user-avatar="blog.userAvatar"
          :location="blog.location"
          :tags="blog.tags"
          :like-count="blog.likeCount"
          :comment-count="blog.commentCount"
          :collect-count="blog.collectCount"
        />
      </div>
    </div>
    
    <!-- 浮动返回顶部按钮 -->
    <button 
      v-if="showBackToTop" 
      class="back-to-top-btn"
      @click="backToTop"
    >
      <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M7.41 15.41L12 10.83l4.59 4.58L18 14l-6-6-6 6z"/>
      </svg>
    </button>
    

  </div>
</template>

<script>
import BlogCard from './BlogCard.vue'

export default {
  name: 'Plaza',
  components: {
    BlogCard
  },
  data() {
    return {
      currentCity: '北京',
      selectedTag: '推荐',
      showBackToTop: false,
      recommendTags: [
        { id: 1, name: '推荐' },
        { id: 2, name: '职场' },
        { id: 3, name: '技能提升' },
        { id: 4, name: '求职' },
        { id: 5, name: '创业' },
        { id: 6, name: '副业' },
        { id: 7, name: '远程工作' },
        { id: 8, name: '行业资讯' }
      ],
      blogList: [
        {
          id: 1,
          imageUrl: 'https://picsum.photos/400/600?random=1',
          title: '分享我的面试经验，如何在激烈的竞争中脱颖而出，成功拿到心仪公司的offer',
          userName: '职场达人',
          userAvatar: 'https://picsum.photos/100/100?random=1',
          location: '北京',
          tags: ['求职', '经验分享'],
          likeCount: 1258,
          commentCount: 156,
          collectCount: 234
        },
        {
          id: 2,
          imageUrl: 'https://picsum.photos/400/600?random=2',
          title: '程序员必学的10个技能，让你的薪资翻倍',
          userName: '技术大咖',
          userAvatar: 'https://picsum.photos/100/100?random=2',
          location: '上海',
          tags: ['技能提升', '编程'],
          likeCount: 2345,
          commentCount: 432,
          collectCount: 567
        },
        {
          id: 3,
          imageUrl: 'https://picsum.photos/400/600?random=3',
          title: '作为自由职业者，我如何实现月入五位数，分享我的工作方式和客户来源',
          userName: '自由职业指南',
          userAvatar: 'https://picsum.photos/100/100?random=3',
          location: '广州',
          tags: ['远程工作', '自由职业'],
          likeCount: 987,
          commentCount: 234,
          collectCount: 345
        },
        {
          id: 4,
          imageUrl: 'https://picsum.photos/400/600?random=4',
          title: '创业三年，从0到100万的经验分享，这些坑千万不要踩',
          userName: '创业者说',
          userAvatar: 'https://picsum.photos/100/100?random=4',
          location: '深圳',
          tags: ['创业', '经验分享'],
          likeCount: 3456,
          commentCount: 567,
          collectCount: 678
        },
        {
          id: 5,
          imageUrl: 'https://picsum.photos/400/600?random=5',
          title: '利用下班后的2小时，我开发了一个副业，现在月收入超过主业',
          userName: '副业达人',
          userAvatar: 'https://picsum.photos/100/100?random=5',
          location: '杭州',
          tags: ['副业', '斜杠青年'],
          likeCount: 1890,
          commentCount: 345,
          collectCount: 456
        }
      ]
    }
  },
  mounted() {
    // 监听滚动事件，控制返回顶部按钮的显示
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    // 移除滚动事件监听
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    showCitySelector() {
      console.log('显示城市选择器')
    },
    showSearchPage() {
      console.log('显示搜索页面')
    },
    showPublishPage() {
      console.log('显示发布页面')
    },
    selectTag(tag) {
      this.selectedTag = tag
      console.log('选择标签:', tag)
    },
    handleScroll() {
      // 当滚动距离超过300px时显示返回顶部按钮
      this.showBackToTop = window.scrollY > 300
    },
    backToTop() {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
  }
}
</script>

<style scoped>
.plaza-container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

/* 顶部导航栏 */
.plaza-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.city-selector {
  display: flex;
  align-items: center;
  background: none;
  border: none;
  font-size: 16px;
  color: #333;
  cursor: pointer;
}

.dropdown-icon {
  width: 14px;
  height: 14px;
  margin-left: 4px;
  color: #666;
}

.search-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn .icon {
  width: 24px;
  height: 24px;
  color: #666;
}



/* 标签区域 */
.tags-section {
  background-color: #fff;
  padding: 12px 0;
  margin-bottom: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.tag-scroll-container {
  display: flex;
  overflow-x: auto;
  padding: 0 16px;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none; /* Firefox */
}

.tag-scroll-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Edge */
}

.tag-item {
  flex-shrink: 0;
  background-color: #f5f5f5;
  border: none;
  border-radius: 16px;
  padding: 6px 14px;
  margin-right: 10px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
}

.tag-item.active {
  background-color: #ffebee;
  color: #ff2442;
}

/* 博客列表 - 两列布局 */
.blog-list {
  padding: 0 8px;
  margin-top: 12px;
}

.blog-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

/* 返回顶部按钮 */
.back-to-top-btn {
  position: fixed;
  bottom: 80px;
  right: 20px;
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  z-index: 99;
  transition: all 0.3s ease;
}

.back-to-top-btn .icon {
  width: 20px;
  height: 20px;
}

.back-to-top-btn:active {
  transform: scale(0.9);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .plaza-header {
    padding: 10px 14px;
  }
  
  .city-selector {
    font-size: 15px;
  }
  
  .search-btn .icon {
    width: 22px;
    height: 22px;
  }
  
  .tag-item {
    padding: 5px 12px;
    font-size: 13px;
  }
  
  .blog-list {
    padding: 0 8px;
  }
  
  .blog-grid {
    gap: 10px;
  }
  

}

@media (max-width: 480px) {
  .plaza-header {
    padding: 8px 12px;
  }
  
  .search-btn .icon {
    width: 20px;
    height: 20px;
  }
  
  .tag-item {
    font-size: 12px;
    padding: 4px 10px;
    margin-right: 8px;
  }
  
  .blog-grid {
    gap: 8px;
  }
  
  .back-to-top-btn {
    bottom: 70px;
    right: 15px;
    width: 36px;
    height: 36px;
  }
  

}
</style>