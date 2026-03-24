<template>
   <view
      class="find-container"
      @touchstart="touchStart"
      @touchmove="touchMove"
      @touchend="touchEnd">
      <!-- 顶部标签栏和操作图标 -->
      <view
         class="header-section !bg-[var(--quyou-nav-bg-color)]"
         :class="{ 'fixed-header': isHeaderFixed }">
         <view class="tab-bar">
            <view
               :class="{ 'tab-item': true, active: tabIndex === 0 }"
               @click="tabIndex = 0">
               <text class="tab-text">通讯录</text>
            </view>
            <view
               :class="{ 'tab-item': true, active: tabIndex === 1 }"
               @click="tabIndex = 1">
               <text class="tab-text">群消息</text>
            </view>
         </view>
         <view class="header-actions">
            <view class="action-icon search-icon">🔍</view>
            <view class="action-icon add-icon">➕</view>
            <view class="action-icon filter-icon">⚙️</view>
         </view>
      </view>

      <!-- 功能模块区域 -->
      <view class="feature-modules" :class="{ '!mt-[140rpx]': isHeaderFixed }">
         <!-- 最新关注 -->
         <view class="feature-item" @click="router.push({ name: 'befocused' })">
            <view class="icon-circle">
               <image src="@/static/imgs/focues.png" mode="widthFix"></image>
            </view>

            <text class="feature-text">最新关注</text>
            <text class="feature-count">{{ statusnum.focus }}</text>
         </view>

         <!-- 谁看过我 -->
         <view class="feature-item" @click="router.push({ name: 'viewed' })">
            <view class="icon-circle">
               <image src="@/static/imgs/look.png" mode="widthFix"></image>
            </view>

            <text class="feature-text">谁看过我</text>
            <text class="feature-count">{{ statusnum.look }}</text>
         </view>

         <!-- 谁喜欢我 -->
         <view class="feature-item" @click="router.push({ name: 'look' })">
            <view class="icon-circle">
               <image src="@/static/imgs/love.png" mode="widthFix"></image>
            </view>

            <text class="feature-text">谁喜欢我</text>
            <text class="feature-count">{{ statusnum.like }}</text>
         </view>
      </view>

      <template v-if="tabIndex === 0">
         <Find></Find>
      </template>
      <template v-else>
         <Group></Group>
      </template>
   </view>
</template>

<script setup lang="ts">
import Find from './components/Find.vue';
import Group from './components/Group.vue';
import { ref } from 'vue';
import { onPageScroll, onShow } from '@dcloudio/uni-app';
import { useRouter } from 'uni-mini-router';
import { MyViewStatistics } from '@/api/system';

const router = useRouter();
const tabIndex = ref<number>(0);
const isHeaderFixed = ref<boolean>(false);
const scrollTop = ref<number>(0);
const statusnum = ref({
   focus: 0,
   like: 0,
   look: 0
});

let startX = 0; // 触摸起始点横坐标（不需要响应式，用普通变量即可）
let deltaX = 0;

// 触摸开始
function touchStart(event: any) {
   startX = event.touches[0].clientX;
}

// 触摸移动（可选，用于实时计算）
function touchMove(event: any) {
   const currentX = event.touches[0].clientX;
   deltaX = currentX - startX;
}

// 触摸结束：判断方向并更新 tabIndex
function touchEnd() {
   // 设置最小滑动阈值（例如 50px），避免误触
   const minSwipeDistance = 50;

   if (deltaX > minSwipeDistance) {
      // 向右滑动 → tabIndex = 1
      tabIndex.value = 0;
      console.log('向右滑动：切换到 tab 1');
   } else if (deltaX < -minSwipeDistance) {
      // 向左滑动 → tabIndex = 0
      tabIndex.value = 1;
      console.log('向左滑动：切换到 tab 0');
   }

   // 重置 deltaX（可选，避免下次滑动受残留值影响）
   deltaX = 0;
}
onPageScroll(event => {
   scrollTop.value = event.scrollTop;
   // 当滚动超过100px时，固定头部0
   isHeaderFixed.value = scrollTop.value > 40;
});
onShow(async () => {
   let res = await MyViewStatistics();
   if (res.code === 0) {
      statusnum.value = res.data;
   }
});
</script>

<style lang="scss" scoped>
// 全局容器样式
.find-container {
   width: 100%;
   background-color: #f5f7fa;
   background: var(--quyou-bg-centext-color);
   padding-top: var(--status-bar-height); // 状态栏
   padding-bottom: env(safe-area-inset-bottom); // 底部安全区
}

// 顶部快捷入口样式
.top-shortcuts {
   display: flex;
   justify-content: space-around;
   padding: 20rpx 0;
   background-color: #fff;
   border-bottom: 1rpx solid #f0f0f0;
   margin-bottom: 10rpx;
}

.shortcut-item {
   padding: 10rpx 20rpx;
   border-radius: 20rpx;
   transition: all 0.2s;
   &.active {
      background-color: #e6f7ff;
      .shortcut-text {
         color: #1890ff;
         font-weight: 600;
      }
   }
   &:active {
      opacity: 0.8;
   }
}

.shortcut-text {
   font-size: 28rpx;
   color: #666;
}

// 顶部标题栏区域样式
.header-section {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 20rpx;
   padding-top: var(--status-bar-height); // 状态栏
   margin-bottom: 20rpx;
   z-index: 10;
   transition: all 0.3s ease;
}

// 固定头部样式
.fixed-header {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   margin-bottom: 0;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
   animation: slideDown 0.3s ease;
}

@keyframes slideDown {
   from {
      transform: translateY(-100%);
   }
   to {
      transform: translateY(0);
   }
}

// 为内容区域添加padding-top，避免被固定头部遮挡
.find-container {
   padding-top: 0;
   transition: padding-top 0.3s ease;
}

// 中间标签栏样式
.tab-bar {
   display: flex;

   border-radius: 12rpx;
   overflow: hidden;
}

.tab-item {
   flex: 1;
   text-align: center;
   padding: 16rpx 30rpx;
   position: relative;
   transition: all 0.2s;
   &.active {
      .tab-text {
         color: #333;
         font-weight: bold;
         text-wrap: nowrap;
      }
      &::after {
         content: '';
         position: absolute;
         bottom: 0;
         left: 25%;
         width: 50%;
         height: 4rpx;
         background-color: #333;
         border-radius: 2rpx;
      }
   }
   &:active {
   }
}

.tab-text {
   font-size: 32rpx;
   color: #999;
   transition: all 0.2s;
}

// 右侧操作图标样式
.header-actions {
   display: flex;
   align-items: center;
   gap: 30rpx;
}

.action-icon {
   width: 50rpx;
   height: 50rpx;
   display: flex;
   justify-content: center;
   align-items: center;
   font-size: 32rpx;
   border-radius: 50%;
   background-color: #f5f5f5;
   transition: all 0.2s;
   &:active {
      transform: scale(0.95);
      background-color: #eaeaea;
   }
}

// 功能模块区域样式
.feature-modules {
   display: grid;
   grid-template-columns: repeat(3, 1fr);
   gap: 40rpx;
   padding: 0 20rpx 20rpx;
}

.feature-item {
   display: flex;
   position: relative;
   align-items: center;
   justify-content: center;
   font-size: 24rpx;
   padding: 10rpx 10rpx;
   border-radius: 16rpx;
   transition: all 0.2s;
   &:active {
      transform: scale(0.95);
   }
}

.icon-circle {
   --h: 50rpx;
   width: var(--h);
   height: var(--h);
   border-radius: 50%;
   display: flex;
   justify-content: center;
   align-items: center;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
   & > image {
      width: 100%;
      height: 100%;
      border-radius: 50%;
   }
}

// 彩色卡片式设计 - 最新关注（蓝色）
.feature-item:nth-child(1) {
   background: #4096ff55;
}

.follow-icon {
   .icon-text {
      color: #fff;
      font-size: 40rpx;
      font-weight: bold;
   }
}

// 彩色卡片式设计 - 谁看过我（紫色）
.feature-item:nth-child(2) {
   background: #a253d455;
}

.view-icon {
   .icon-text {
      color: #fff;
      font-size: 36rpx;
   }
}

// 彩色卡片式设计 - 谁喜欢我（粉色）
.feature-item:nth-child(3) {
   background: #ffe20d85;
}

.like-icon {
   background-color: #ff69b4;
   .icon-text {
      color: #fff;
      font-size: 36rpx;
   }
}

// 推荐好友保持原有样式
.feature-item:nth-child(4) {
   background-color: #fff;
   border: 1rpx solid #f0f0f0;
}

.recommend-icon {
   .icon-text {
      color: #52c41a;
      font-size: 36rpx;
   }
}

.icon-text {
   font-size: 36rpx;
}

.feature-text {
   font-size: 24rpx;
   color: #272626;
   margin-left: 5rpx;
   font-weight: 500;
}

// 彩色数字角标
.feature-count {
   position: absolute;
   top: 0rpx;
   right: -12rpx;
   background: red;
   border-radius: 50%;
   display: flex;
   justify-content: center;
   align-items: center;
   font-size: 18rpx;
   color: white;
   width: 25rpx;
   height: 25rpx;
}

// 区域标题样式
.section-header {
   padding: 20rpx;
   margin-bottom: 10rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: 600;
   color: #333;
}

// 好友列表样式
.friend-list {
   padding: 0 20rpx;
}

.friend-item {
   display: flex;
   align-items: center;
   padding: 24rpx;
   border-radius: 12rpx;
   margin-bottom: 15rpx;
   transition: all 0.2s;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

   &:active {
      transform: scale(0.98);
      box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.1);
   }
}

.avatar-container {
   position: relative;
   margin-right: 20rpx;
}

.avatar {
   width: 110rpx;
   height: 110rpx;
   border-radius: 50%;
   background-color: #f0f0f0;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.online-indicator {
   position: absolute;
   bottom: 6rpx;
   right: 6rpx;
   width: 32rpx;
   height: 32rpx;
   border-radius: 50%;
   border: 4rpx solid #fff;
   background-color: #e0e0e0;
   box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);

   &.online {
      background-color: #52c41a;
      animation: pulse 2s infinite;
   }
}

@keyframes pulse {
   0% {
      box-shadow: 0 0 0 0 rgba(82, 196, 26, 0.4);
   }
   70% {
      box-shadow: 0 0 0 15rpx rgba(82, 196, 26, 0);
   }
   100% {
      box-shadow: 0 0 0 0 rgba(82, 196, 26, 0);
   }
}

.friend-info {
   flex: 1;
   min-width: 0;
}

.friend-info-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 10rpx;
}

.friend-name-container {
   display: flex;
   align-items: center;
}

.friend-name {
   font-size: 34rpx;
   font-weight: 600;
   color: #333;
   margin-right: 12rpx;
}

.online-status {
   font-size: 24rpx;
   color: #52c41a;
   background-color: rgba(82, 196, 26, 0.1);
   padding: 4rpx 12rpx;
   border-radius: 12rpx;
}

.message-time {
   font-size: 24rpx;
   color: #999;
}

.friend-info-footer {
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.latest-message {
   font-size: 28rpx;
   color: #666;
   flex: 1;
   min-width: 0;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.unread-count {
   min-width: 40rpx;
   height: 40rpx;
   line-height: 40rpx;
   text-align: center;
   font-size: 24rpx;
   color: #fff;
   background-color: #ff4d4f;
   border-radius: 20rpx;
   padding: 0 14rpx;
   margin-left: 12rpx;
   box-shadow: 0 2rpx 8rpx rgba(255, 77, 79, 0.3);
}

// 空列表样式
.empty-list {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: center;
   height: 500rpx;
   color: #999;
}

.empty-icon {
   font-size: 120rpx;
   margin-bottom: 20rpx;
}

.empty-text {
   font-size: 32rpx;
   margin-bottom: 10rpx;
}

.empty-subtext {
   font-size: 26rpx;
   color: #bbb;
}
</style>
