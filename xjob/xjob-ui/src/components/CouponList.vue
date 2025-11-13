<template>
  <div class="coupon-list-container">
    <!-- 顶部导航栏 -->
    <header class="coupon-header">
      <button class="back-button" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        返回
      </button>
      <h1 class="coupon-title">优惠券购买</h1>
      <div class="header-placeholder"></div>
    </header>

    <!-- 优惠券列表 -->
    <div class="coupon-content">
      <!-- 限时秒杀优惠券 -->
      <div class="coupon-section">
        <div class="section-title">限时秒杀</div>
        <div class="flash-sale-coupon">
          <div class="coupon-left">
            <div class="price-container">
              <span class="currency">¥</span>
              <span class="price">9.9</span>
              <span class="original-price">¥20</span>
            </div>
            <div class="coupon-desc">无门槛代金券</div>
            <div class="countdown">
              <span class="countdown-label">距离结束</span>
              <div class="countdown-time">
                <span class="time-item">01</span>
                <span class="time-separator">:</span>
                <span class="time-item">23</span>
                <span class="time-separator">:</span>
                <span class="time-item">45</span>
              </div>
            </div>
          </div>
          <div class="coupon-right">
            <button class="buy-button flash-sale-btn">立即抢购</button>
          </div>
          <div class="coupon-corner"></div>
          <div class="coupon-dot"></div>
        </div>
      </div>

      <!-- 普通代金券 -->
      <div class="coupon-section">
        <div class="section-title">普通代金券</div>
        <div class="normal-coupon" v-for="coupon in normalCoupons" :key="coupon.id">
          <div class="coupon-left">
            <div class="price-container">
              <span class="currency">¥</span>
              <span class="price">{{ coupon.price }}</span>
            </div>
            <div class="coupon-desc">{{ coupon.description }}</div>
            <div class="coupon-condition">满{{ coupon.minSpend }}元可用</div>
          </div>
          <div class="coupon-right">
            <button class="buy-button">立即购买</button>
          </div>
          <div class="coupon-corner"></div>
          <div class="coupon-dot"></div>
        </div>
      </div>

      <!-- 优惠券使用说明 -->
      <div class="coupon-instruction">
        <h3 class="instruction-title">使用说明</h3>
        <ul class="instruction-list">
          <li>1. 优惠券适用于平台内所有技能服务</li>
          <li>2. 每个订单只能使用一张优惠券</li>
          <li>3. 优惠券自购买之日起30天内有效</li>
          <li>4. 限时秒杀优惠券数量有限，先到先得</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CouponList',
  data() {
    return {
      // 普通代金券数据
      normalCoupons: [
        {
          id: 1,
          price: 50,
          description: '满100减50代金券',
          minSpend: 100
        },
        {
          id: 2,
          price: 100,
          description: '满200减100代金券',
          minSpend: 200
        },
        {
          id: 3,
          price: 200,
          description: '满500减200代金券',
          minSpend: 500
        }
      ]
    }
  },
  methods: {
    // 返回上一页
    goBack() {
      // 在实际项目中，这里应该使用Vue Router的返回功能
      // 由于这里是模拟环境，我们使用简单的实现
      console.log('返回上一页')
      // 可以在这里添加页面切换逻辑
    }
  },
  mounted() {
    // 这里可以添加倒计时逻辑
  }
}
</script>

<style scoped>
.coupon-list-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航栏 */
.coupon-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 16px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  background: none;
  border: none;
  color: #333;
  font-size: 16px;
  cursor: pointer;
}

.coupon-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.header-placeholder {
  width: 80px;
}

/* 优惠券内容区 */
.coupon-content {
  padding: 16px;
}

/* 优惠券区域 */
.coupon-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

/* 限时秒杀优惠券 */
.flash-sale-coupon {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  border-radius: 12px;
  padding: 16px;
  color: #fff;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
  transition: transform 0.3s ease;
}

.flash-sale-coupon:hover {
  transform: translateY(-4px);
}

/* 普通优惠券 */
.normal-coupon {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  padding: 16px;
  color: #fff;
  display: flex;
  align-items: center;
  position: relative;
  margin-bottom: 12px;
  transition: transform 0.3s ease;
}

.normal-coupon:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* 优惠券左右布局 */
.coupon-left {
  flex: 1;
}

.coupon-right {
  flex-shrink: 0;
}

/* 价格样式 */
.price-container {
  display: flex;
  align-items: baseline;
  margin-bottom: 8px;
}

.currency {
  font-size: 16px;
  margin-right: 2px;
}

.price {
  font-size: 32px;
  font-weight: bold;
  margin-right: 8px;
}

.original-price {
  font-size: 14px;
  text-decoration: line-through;
  opacity: 0.8;
}

.coupon-desc {
  font-size: 14px;
  margin-bottom: 8px;
}

.coupon-condition {
  font-size: 12px;
  opacity: 0.8;
}

/* 限时秒杀倒计时 */
.countdown {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.countdown-time {
  display: flex;
  align-items: center;
  gap: 2px;
}

.time-item {
  background-color: rgba(255, 255, 255, 0.3);
  padding: 2px 4px;
  border-radius: 4px;
  font-weight: bold;
}

/* 购买按钮 */
.buy-button {
  background-color: #fff;
  color: #667eea;
  border: none;
  padding: 10px 24px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.buy-button:hover {
  background-color: #f0f0f0;
  transform: scale(1.05);
}

.flash-sale-btn {
  color: #ff6b6b;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 107, 107, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 107, 107, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 107, 107, 0);
  }
}

/* 优惠券装饰元素 */
.coupon-corner {
  position: absolute;
  top: 0;
  right: 0;
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.1);
  clip-path: polygon(100% 0, 0 0, 100% 100%);
}

.coupon-dot {
  position: absolute;
  bottom: 12px;
  left: -20px;
  width: 40px;
  height: 40px;
  background-color: #f5f5f5;
  border-radius: 50%;
  opacity: 0.3;
}

/* 使用说明 */
.coupon-instruction {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
}

.instruction-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.instruction-list {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.instruction-list li {
  margin-bottom: 8px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .price {
    font-size: 24px;
  }
  
  .buy-button {
    padding: 8px 16px;
    font-size: 12px;
  }
  
  .coupon-dot {
    display: none;
  }
}
</style>