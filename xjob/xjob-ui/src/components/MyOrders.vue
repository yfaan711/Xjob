<template>
  <div class="my-orders-container">
    <!-- 顶部导航栏 -->
    <header class="my-orders-header">
      <button class="back-btn" @click="goBack">返回</button>
      <h1 class="page-title">我的订单</h1>
      <div class="header-actions"></div>
    </header>

    <!-- 订单类型切换标签 -->
    <div class="order-tabs">
      <button 
        :class="['tab-btn', { active: activeTab === 'booking' }]"
        @click="activeTab = 'booking'"
      >
        我预约的
      </button>
      <button 
        :class="['tab-btn', { active: activeTab === 'appointment' }]"
        @click="activeTab = 'appointment'"
      >
        预约我的
      </button>
    </div>

    <!-- 订单列表 -->
    <div class="orders-list">
      <!-- 我预约的订单 -->
      <div v-if="activeTab === 'booking'">
        <div v-if="bookingOrders.length === 0" class="empty-state">
          <svg class="empty-icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm-2 14l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"/>
          </svg>
          <p>暂无预约订单</p>
          <button class="browse-btn" @click="browseServices">浏览服务</button>
        </div>
        
        <div v-else class="orders-grid">
          <div 
            v-for="(order, index) in bookingOrders" 
            :key="index"
            class="order-card"
          >
            <div class="order-header">
              <h3 class="service-title">{{ order.serviceTitle }}</h3>
              <span :class="['order-status', order.status]">{{ order.statusText }}</span>
            </div>
            <div class="order-info">
              <div class="info-item">
                <span class="info-label">服务提供者：</span>
                <span class="info-value">{{ order.providerName }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">预约时间：</span>
                <span class="info-value">{{ order.bookingTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">订单金额：</span>
                <span class="info-value price">¥{{ order.price }}</span>
              </div>
            </div>
            <div class="order-actions">
              <button 
                v-if="order.status === 'pending'" 
                class="action-btn cancel-btn"
                @click="cancelOrder('booking', index)"
              >
                取消订单
              </button>
              <button 
                v-if="order.status === 'confirmed'" 
                class="action-btn contact-btn"
                @click="contactService(order)"
              >
                联系对方
              </button>
              <button 
                v-if="order.status === 'completed'" 
                class="action-btn review-btn"
                @click="reviewOrder(order)"
              >
                评价服务
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 预约我的订单 -->
      <div v-else-if="activeTab === 'appointment'">
        <div v-if="appointmentOrders.length === 0" class="empty-state">
          <svg class="empty-icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm-2 14l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"/>
          </svg>
          <p>暂无预约我的订单</p>
          <button class="publish-btn" @click="publishService">发布服务</button>
        </div>
        
        <div v-else class="orders-grid">
          <div 
            v-for="(order, index) in appointmentOrders" 
            :key="index"
            class="order-card"
          >
            <div class="order-header">
              <h3 class="service-title">{{ order.serviceTitle }}</h3>
              <span :class="['order-status', order.status]">{{ order.statusText }}</span>
            </div>
            <div class="order-info">
              <div class="info-item">
                <span class="info-label">预约用户：</span>
                <span class="info-value">{{ order.customerName }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">预约时间：</span>
                <span class="info-value">{{ order.bookingTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">订单金额：</span>
                <span class="info-value price">¥{{ order.price }}</span>
              </div>
            </div>
            <div class="order-actions">
              <button 
                v-if="order.status === 'pending'" 
                class="action-btn decline-btn"
                @click="declineOrder(index)"
              >
                拒绝
              </button>
              <button 
                v-if="order.status === 'pending'" 
                class="action-btn accept-btn"
                @click="acceptOrder(index)"
              >
                接受
              </button>
              <button 
                v-if="order.status === 'confirmed'" 
                class="action-btn contact-btn"
                @click="contactService(order)"
              >
                联系对方
              </button>
              <button 
                v-if="order.status === 'confirmed'" 
                class="action-btn complete-btn"
                @click="completeOrder(index)"
              >
                标记完成
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyOrders',
  emits: ['navigate'],
  data() {
    return {
      activeTab: 'booking',
      bookingOrders: [],
      appointmentOrders: []
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    // 加载订单信息
    loadOrders() {
      // 从localStorage加载订单信息
      this.loadBookingOrders()
      this.loadAppointmentOrders()
    },
    
    // 加载我预约的订单
    loadBookingOrders() {
      const saved = localStorage.getItem('bookingOrders')
      if (saved) {
        try {
          this.bookingOrders = JSON.parse(saved)
        } catch (error) {
          console.error('解析预约订单数据失败:', error)
          this.bookingOrders = this.getMockBookingOrders()
          this.saveBookingOrders()
        }
      } else {
        this.bookingOrders = this.getMockBookingOrders()
        this.saveBookingOrders()
      }
    },
    
    // 加载预约我的订单
    loadAppointmentOrders() {
      const saved = localStorage.getItem('appointmentOrders')
      if (saved) {
        try {
          this.appointmentOrders = JSON.parse(saved)
        } catch (error) {
          console.error('解析被预约订单数据失败:', error)
          this.appointmentOrders = this.getMockAppointmentOrders()
          this.saveAppointmentOrders()
        }
      } else {
        this.appointmentOrders = this.getMockAppointmentOrders()
        this.saveAppointmentOrders()
      }
    },
    
    // 保存我预约的订单
    saveBookingOrders() {
      localStorage.setItem('bookingOrders', JSON.stringify(this.bookingOrders))
    },
    
    // 保存预约我的订单
    saveAppointmentOrders() {
      localStorage.setItem('appointmentOrders', JSON.stringify(this.appointmentOrders))
    },
    
    // 获取模拟的我预约的订单数据
    getMockBookingOrders() {
      return [
        {
          serviceTitle: '前端开发技术指导',
          providerName: '张工',
          bookingTime: '2024-01-25 14:00-16:00',
          price: 200,
          status: 'confirmed',
          statusText: '已确认'
        },
        {
          serviceTitle: 'UI设计评审',
          providerName: '李设计师',
          bookingTime: '2024-01-26 10:00-11:30',
          price: 150,
          status: 'pending',
          statusText: '待确认'
        },
        {
          serviceTitle: '产品经理经验分享',
          providerName: '王经理',
          bookingTime: '2024-01-20 16:00-18:00',
          price: 250,
          status: 'completed',
          statusText: '已完成'
        }
      ]
    },
    
    // 获取模拟的预约我的订单数据
    getMockAppointmentOrders() {
      return [
        {
          serviceTitle: 'Java后端技术支持',
          customerName: '小明',
          bookingTime: '2024-01-25 09:00-11:00',
          price: 300,
          status: 'confirmed',
          statusText: '已确认'
        },
        {
          serviceTitle: '项目管理咨询',
          customerName: '小红',
          bookingTime: '2024-01-27 13:30-15:30',
          price: 280,
          status: 'pending',
          statusText: '待确认'
        }
      ]
    },
    
    // 取消预约订单
    cancelOrder(type, index) {
      if (confirm('确定要取消这个订单吗？')) {
        if (type === 'booking') {
          this.bookingOrders.splice(index, 1)
          this.saveBookingOrders()
        }
        alert('订单已取消')
      }
    },
    
    // 接受预约订单
    acceptOrder(index) {
      if (confirm('确定要接受这个预约吗？')) {
        this.appointmentOrders[index].status = 'confirmed'
        this.appointmentOrders[index].statusText = '已确认'
        this.saveAppointmentOrders()
        alert('已接受预约')
      }
    },
    
    // 拒绝预约订单
    declineOrder(index) {
      if (confirm('确定要拒绝这个预约吗？')) {
        this.appointmentOrders.splice(index, 1)
        this.saveAppointmentOrders()
        alert('已拒绝预约')
      }
    },
    
    // 标记订单完成
    completeOrder(index) {
      if (confirm('确定要标记此订单为已完成吗？')) {
        this.appointmentOrders[index].status = 'completed'
        this.appointmentOrders[index].statusText = '已完成'
        this.saveAppointmentOrders()
        alert('订单已标记为完成')
      }
    },
    
    // 联系服务提供者/预约用户
    contactService(order) {
      alert('联系功能待实现')
    },
    
    // 评价服务
    reviewOrder(order) {
      alert('评价功能待实现')
    },
    
    // 浏览服务
    browseServices() {
      this.$emit('navigate', 'square')
    },
    
    // 发布服务
    publishService() {
      alert('发布服务功能待实现')
    },
    
    // 返回上一页
    goBack() {
      this.$emit('navigate', 'profile')
    }
  }
}
</script>

<style scoped>
.my-orders-container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding-bottom: 20px;
}

/* 顶部导航栏 */
.my-orders-header {
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

.back-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 8px 12px;
  color: #666;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-actions {
  width: 60px;
}

/* 订单类型切换标签 */
.order-tabs {
  display: flex;
  background-color: #fff;
  margin-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.tab-btn {
  flex: 1;
  padding: 14px 0;
  background: none;
  border: none;
  font-size: 15px;
  color: #666;
  position: relative;
  cursor: pointer;
  transition: color 0.2s;
}

.tab-btn.active {
  color: #1989fa;
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
  background-color: #1989fa;
  border-radius: 1.5px;
}

/* 订单列表 */
.orders-list {
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
  background-color: #fff;
  border-radius: 12px;
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

.browse-btn, .publish-btn {
  background-color: #1989fa;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 24px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.browse-btn:active, .publish-btn:active {
  background-color: #096dd9;
}

/* 订单卡片网格 */
.orders-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.service-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  flex: 1;
  margin-right: 12px;
}

.order-status {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.order-status.pending {
  background-color: #fff7e6;
  color: #fa8c16;
}

.order-status.confirmed {
  background-color: #e6f7ff;
  color: #1890ff;
}

.order-status.completed {
  background-color: #f6ffed;
  color: #52c41a;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.info-item {
  display: flex;
  font-size: 14px;
}

.info-label {
  color: #999;
  width: 80px;
  flex-shrink: 0;
}

.info-value {
  color: #333;
  flex: 1;
}

.info-value.price {
  color: #ff4d4f;
  font-weight: 500;
}

.order-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.action-btn {
  padding: 6px 16px;
  border-radius: 16px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid;
}

.cancel-btn, .decline-btn {
  background-color: #fff;
  border-color: #d9d9d9;
  color: #666;
}

.cancel-btn:active, .decline-btn:active {
  background-color: #f5f5f5;
}

.accept-btn, .contact-btn, .review-btn, .complete-btn {
  background-color: #1989fa;
  border-color: #1989fa;
  color: #fff;
}

.accept-btn:active, .contact-btn:active, .review-btn:active, .complete-btn:active {
  background-color: #096dd9;
  border-color: #096dd9;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .my-orders-header {
    padding: 10px 14px;
  }
  
  .page-title {
    font-size: 17px;
  }
  
  .orders-list {
    padding: 14px;
  }
  
  .order-card {
    padding: 14px;
  }
  
  .service-title {
    font-size: 15px;
  }
  
  .info-label {
    width: 70px;
    font-size: 13px;
  }
  
  .info-value {
    font-size: 13px;
  }
  
  .order-actions {
    gap: 8px;
  }
  
  .action-btn {
    padding: 5px 12px;
    font-size: 12px;
  }
}
</style>