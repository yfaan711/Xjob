<template>
  <div class="blog-card">
    <!-- 图片区域 -->
    <div class="image-container">
      <img 
        :src="imageUrl" 
        :alt="title" 
        class="blog-image"
        @click="onImageClick"
      />
      
      <!-- 图片右下角的标签 -->
      <div v-if="tags && tags.length > 0" class="tags">
        <span v-for="(tag, index) in displayTags" :key="index" class="tag">
          {{ tag }}
        </span>
      </div>
    </div>
    
    <!-- 内容区域 -->
    <div class="content">
      <!-- 用户信息 -->
      <div class="user-info">
        <img :src="userAvatar" alt="用户头像" class="user-avatar" />
        <div class="user-details">
          <span class="user-name">{{ userName }}</span>
          <span v-if="location" class="user-location">📍 {{ location }}</span>
        </div>
        <button class="follow-btn" @click="onFollow">
          {{ isFollowing ? '已关注' : '关注' }}
        </button>
      </div>
      
      <!-- 博客标题 -->
      <h3 class="blog-title">{{ title }}</h3>
      
      <!-- 互动区域 -->
      <div class="interaction-bar">
        <button class="interaction-btn" @click="onLike">
          <svg v-if="!isLiked" class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
          </svg>
          <svg v-else class="icon liked" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
          </svg>
          <span>{{ likeCount }}</span>
        </button>
        <button class="interaction-btn" @click="onComment">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M21 6h-2v9H6v2c0 .55.45 1 1 1h11l4 4V7c0-.55-.45-1-1-1zm-4 6V3c0-.55-.45-1-1-1H3c-.55 0-1 .45-1 1v14l4-4h10c.55 0 1-.45 1-1z"/>
          </svg>
          <span>{{ commentCount }}</span>
        </button>
        <button class="interaction-btn" @click="onCollect">
          <svg v-if="!isCollected" class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2zm0 15l-5-2.18L7 18V5h10v13z"/>
          </svg>
          <svg v-else class="icon collected" viewBox="0 0 24 24" fill="currentColor">
            <path d="M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2zm0 15l-5-2.18L7 18V5h10v13z"/>
          </svg>
          <span>{{ collectCount }}</span>
        </button>
        <button class="interaction-btn share-btn" @click="onShare">
          <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92 1.61 0 2.92-1.31 2.92-2.92s-1.31-2.92-2.92-2.92z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BlogCard',
  props: {
    imageUrl: {
      type: String,
      default: 'https://picsum.photos/400/600?random=1'
    },
    title: {
      type: String,
      required: true
    },
    userName: {
      type: String,
      default: '用户昵称'
    },
    userAvatar: {
      type: String,
      default: 'https://picsum.photos/100/100?random=1'
    },
    location: {
      type: String,
      default: ''
    },
    tags: {
      type: Array,
      default: () => []
    },
    likeCount: {
      type: Number,
      default: 0
    },
    commentCount: {
      type: Number,
      default: 0
    },
    collectCount: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      isLiked: false,
      isCollected: false,
      isFollowing: false
    }
  },
  computed: {
    // 只显示前两个标签
    displayTags() {
      return this.tags.slice(0, 2)
    }
  },
  methods: {
    onImageClick() {
      // 图片点击事件
      console.log('图片点击:', this.title)
    },
    onLike() {
      // 点赞/取消点赞
      this.isLiked = !this.isLiked
    },
    onComment() {
      // 评论
      console.log('评论:', this.title)
    },
    onCollect() {
      // 收藏/取消收藏
      this.isCollected = !this.isCollected
    },
    onShare() {
      // 分享
      console.log('分享:', this.title)
    },
    onFollow() {
      // 关注/取消关注
      this.isFollowing = !this.isFollowing
    }
  }
}
</script>

<style scoped>
.blog-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.blog-card:active {
  transform: scale(0.98);
}

.image-container {
  position: relative;
  padding-top: 150%; /* 2:3 比例 */
  overflow: hidden;
}

.blog-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.blog-card:hover .blog-image {
  transform: scale(1.05);
}

.tags {
  position: absolute;
  bottom: 12px;
  right: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.tag {
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 10px;
  white-space: nowrap;
}

.content {
  padding: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 8px;
  object-fit: cover;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.user-location {
  font-size: 12px;
  color: #999;
  margin-left: 4px;
}

.follow-btn {
  background: #ff2442;
  color: white;
  border: none;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  cursor: pointer;
}

.follow-btn:active {
  opacity: 0.8;
}

.blog-title {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.interaction-bar {
  display: flex;
  align-items: center;
  gap: 16px;
}

.interaction-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  color: #666;
  font-size: 12px;
  cursor: pointer;
  padding: 4px 0;
}

.share-btn {
  margin-left: auto;
}

.icon {
  width: 18px;
  height: 18px;
}

.icon.liked,
.icon.collected {
  color: #ff2442;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .blog-card {
    margin-bottom: 12px;
    border-radius: 8px;
  }
  
  .content {
    padding: 10px;
  }
  
  .interaction-bar {
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .tags {
    bottom: 8px;
    right: 8px;
  }
  
  .tag {
    font-size: 9px;
    padding: 3px 6px;
  }
  
  .user-name {
    font-size: 13px;
  }
  
  .user-location {
    font-size: 11px;
  }
  
  .follow-btn {
    font-size: 11px;
    padding: 3px 8px;
  }
  
  .blog-title {
    font-size: 13px;
  }
  
  .interaction-btn {
    font-size: 11px;
  }
  
  .icon {
    width: 16px;
    height: 16px;
  }
}
</style>