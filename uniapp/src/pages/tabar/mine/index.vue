<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <text class="header-title">南昌交通</text>
         <view class="settings-btn" @click="goToSettings">
            <text class="settings-icon">⚙️</text>
         </view>
      </view>

      <!-- 个人信息头部 -->
      <view class="profile-header">
         <view class="profile-info">
            <image
               :src="
                  userStore.userInfo?.avatarUrl ||
                  'https://picsum.photos/seed/useravatar/100/100'
               "
               class="avatar"
               mode="aspectFill"></image>
            <view class="user-details">
               <text class="username">{{
                  userStore.userInfo?.realName ||
                  userStore.userInfo?.username ||
                  '未登录'
               }}</text>
               <text class="user-id"
                  >ID: {{ userStore.userInfo?.userId || '--' }}</text
               >
               <text class="user-gender">{{ genderText }}</text>
            </view>
         </view>

         <!-- 信誉分模块 -->
         <view class="credit-section">
            <view class="credit-header">
               <view class="credit-label">
                  <text class="credit-icon">⭐</text>
                  <text class="credit-text">信誉分</text>
               </view>
               <view class="credit-score">
                  <text class="score-num">{{
                     userStore.userInfo?.creditScore || 0
                  }}</text>
                  <text class="score-total">/ 100</text>
               </view>
            </view>
            <view class="score-bar">
               <view
                  class="score-progress"
                  :style="{
                     width: (userStore.userInfo?.creditScore || 0) + '%'
                  }"></view>
            </view>
            <text class="credit-tip">信誉良好，可优先应聘高薪兼职</text>
         </view>
      </view>

      <!-- 提现金额模块 -->
      <view class="balance-card">
         <view class="balance-left">
            <text class="balance-label">可提现金额</text>
            <text class="balance-amount"
               >¥{{ (userStore.userInfo?.wlWallets?.balance || 0) / 100 }}</text
            >
         </view>
         <button class="withdraw-btn" @click="handleWithdraw">立即提现</button>
         <view class="balance-links">
            <view class="link-item" @click="goToWithdrawHistory">
               <text class="link-icon">📜</text>
               <text class="link-text">提现记录</text>
            </view>
            <view class="link-item" @click="goToEarnings">
               <text class="link-icon">👛</text>
               <text class="link-text">收益明细</text>
            </view>
         </view>
      </view>

      <!-- 功能菜单 -->
      <scroll-view scroll-y class="menu-scroll">
         <!-- 简历模块 -->
         <view class="menu-card">
            <view class="menu-item" @click="goToResume">
               <view class="menu-icon-wrap blue">
                  <text class="menu-icon">📄</text>
               </view>
               <view class="menu-content">
                  <text class="menu-title">我的简历</text>
                  <text class="menu-desc"
                     >已完善{{ resumeProgress }}%，完善度越高越易应聘</text
                  >
               </view>
               <text class="menu-arrow">›</text>
            </view>

            <!-- 简历快捷操作 -->
            <view class="resume-actions">
               <view class="action-item" @click="goToEditResume">
                  <text class="action-icon">✏️</text>
                  <text class="action-text">编辑简历</text>
               </view>
               <view class="action-item" @click="goToUploadResume">
                  <text class="action-icon">⬆️</text>
                  <text class="action-text">上传附件</text>
               </view>
               <view class="action-item" @click="goToShareResume">
                  <text class="action-icon">🔗</text>
                  <text class="action-text">简历分享</text>
               </view>
            </view>
         </view>

         <!-- 核心功能区 -->
         <view class="menu-card">
            <view class="menu-item" @click="goToJobHistory">
               <view class="menu-icon-wrap orange">
                  <text class="menu-icon">⏰</text>
               </view>
               <text class="menu-title">兼职记录</text>
               <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goToFeedback">
               <view class="menu-icon-wrap red">
                  <text class="menu-icon">🚩</text>
               </view>
               <text class="menu-title">投诉与反馈</text>
               <text class="menu-arrow">›</text>
            </view>
         </view>

         <!-- 系统设置区 -->
         <view class="menu-card">
            <view class="menu-item" @click="goToAccountSecurity">
               <view class="menu-icon-wrap gray">
                  <text class="menu-icon">🛡️</text>
               </view>
               <text class="menu-title">账号安全</text>
               <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goToNotificationSettings">
               <view class="menu-icon-wrap gray">
                  <text class="menu-icon">🔔</text>
               </view>
               <text class="menu-title">消息设置</text>
               <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goToHelpCenter">
               <view class="menu-icon-wrap gray">
                  <text class="menu-icon">❓</text>
               </view>
               <text class="menu-title">帮助中心</text>
               <text class="menu-arrow">›</text>
            </view>
         </view>

         <!-- 退出登录 -->
         <view class="logout-section">
            <button class="logout-btn" @click="handleLogout">
               <text class="logout-icon">🚪</text>
               <text class="logout-text">退出登录</text>
            </button>
         </view>
      </scroll-view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useUserStore } from '@/store/user';
import * as userApi from '@/api/user';

// 创建用户store
const userStore = useUserStore();

// 简历完善度
const resumeProgress = ref(80);

// 加载状态
const loading = ref(false);

// 性别显示文本
const genderText = computed(() => {
   const gender = userStore.userInfo?.gender;
   const genderMap: Record<number, string> = {
      0: '保密',
      1: '男',
      2: '女'
   };
   return genderMap[gender] || '保密';
});

// 获取用户信息
const fetchUserInfo = async () => {
   loading.value = true;
   try {
      const res = await userApi.getLoginStatus();
      if (res.code === 0 && res.data) {
         // 更新pina store
         userStore.setUserInfo({
            userId: Number(res.data.userId) || 0,
            username: res.data.username || '',
            token: res.data.token || null,
            userType: res.data.userType || 1,
            avatarUrl: res.data.avatarUrl || '',
            userRote: res.data.userRote || null,
            studentId: res.data.studentId || '',
            realName: res.data.realName || '',
            age: res.data.age || 0,
            gender: res.data.gender || 0,
            phone: res.data.phone || '',
            profession: res.data.profession || '',
            introduction: res.data.introduction || '',
            creditScore: res.data.creditScore || 0,
            tagName: res.data.tagName || '',
            wlWallets: res.data.wlWallets || null
         });
      }
   } catch (error) {
      console.error('获取用户信息失败:', error);
   } finally {
      loading.value = false;
   }
};

// 页面加载时获取用户信息
onMounted(() => {
   fetchUserInfo();
});

// 跳转设置
const goToSettings = () => {
   uni.showToast({
      title: '设置功能开发中',
      icon: 'none'
   });
};

// 提现
const handleWithdraw = () => {
   uni.showToast({
      title: '提现申请已提交，预计1-3个工作日到账',
      icon: 'none',
      duration: 2000
   });
};

// 提现记录
const goToWithdrawHistory = () => {
   uni.showToast({
      title: '提现记录功能开发中',
      icon: 'none'
   });
};

// 收益明细
const goToEarnings = () => {
   uni.showToast({
      title: '收益明细功能开发中',
      icon: 'none'
   });
};

// 我的简历
const goToResume = () => {
   uni.navigateTo({
      url: '/pages/tabar/mine/uploadresume/index'
   });
};

// 编辑简历
const goToEditResume = () => {
   uni.navigateTo({
      url: '/pages/tabar/mine/uploadresume/index'
   });
};

// 上传附件
const goToUploadResume = () => {
   uni.showToast({
      title: '上传附件功能开发中',
      icon: 'none'
   });
};

// 简历分享
const goToShareResume = () => {
   uni.showToast({
      title: '简历分享功能开发中',
      icon: 'none'
   });
};

// 兼职记录
const goToJobHistory = () => {
   uni.navigateTo({
      url: '/pages/tabar/mine/orderlst/index'
   });
};

// 投诉与反馈
const goToFeedback = () => {
   uni.navigateTo({
      url: '/pages/tabar/mine/feedback/index'
   });
};

// 账号安全
const goToAccountSecurity = () => {
   uni.showToast({
      title: '账号安全功能开发中',
      icon: 'none'
   });
};

// 消息设置
const goToNotificationSettings = () => {
   uni.showToast({
      title: '消息设置功能开发中',
      icon: 'none'
   });
};

// 帮助中心
const goToHelpCenter = () => {
   uni.showToast({
      title: '帮助中心功能开发中',
      icon: 'none'
   });
};

// 退出登录
const handleLogout = () => {
   uni.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: res => {
         if (res.confirm) {
            uni.showLoading({
               title: '退出中...',
               mask: true
            });
            setTimeout(() => {
               uni.hideLoading();
               // 清除用户信息
               userStore.clearUserInfo();
               uni.redirectTo({
                  url: '/pages/login/index'
               });
            }, 1000);
         }
      }
   });
};

// 跳转首页
const goToHome = () => {
   uni.switchTab({
      url: '/pages/tabar/index'
   });
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
   padding-bottom: 180rpx;
}

.header {
   position: sticky;
   top: 0;
   z-index: 100;
   background-color: #fff;
   padding: 24rpx 32rpx;
   display: flex;
   align-items: center;
   justify-content: space-between;
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.header-title {
   font-size: 34rpx;
   font-weight: 600;
   color: #1e293b;
}

.settings-btn {
   width: 72rpx;
   height: 72rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.settings-icon {
   font-size: 36rpx;
}

.profile-header {
   background-color: #3b82f6;
   padding: 48rpx 32rpx;
}

.profile-info {
   display: flex;
   align-items: center;
   gap: 24rpx;
   margin-bottom: 32rpx;
}

.avatar {
   width: 128rpx;
   height: 128rpx;
   border-radius: 50%;
   border: 4rpx solid rgba(255, 255, 255, 0.5);
}

.user-details {
   display: flex;
   flex-direction: column;
   gap: 8rpx;
}

.username {
   font-size: 36rpx;
   font-weight: 600;
   color: #fff;
}

.user-id {
   font-size: 26rpx;
   color: rgba(255, 255, 255, 0.8);
}

.user-gender {
   font-size: 24rpx;
   color: rgba(255, 255, 255, 0.6);
   margin-top: 4rpx;
}

.credit-section {
   background-color: rgba(255, 255, 255, 0.1);
   border-radius: 16rpx;
   padding: 24rpx;
}

.credit-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 16rpx;
}

.credit-label {
   display: flex;
   align-items: center;
   gap: 12rpx;
}

.credit-icon {
   font-size: 28rpx;
}

.credit-text {
   font-size: 28rpx;
   color: rgba(255, 255, 255, 0.9);
}

.credit-score {
   display: flex;
   align-items: baseline;
}

.score-num {
   font-size: 40rpx;
   font-weight: 700;
   color: #fff;
}

.score-total {
   font-size: 24rpx;
   color: rgba(255, 255, 255, 0.7);
   margin-left: 4rpx;
}

.score-bar {
   height: 12rpx;
   background-color: rgba(255, 255, 255, 0.2);
   border-radius: 6rpx;
   overflow: hidden;
   margin-bottom: 12rpx;
}

.score-progress {
   height: 100%;
   background-color: #fbbf24;
   border-radius: 6rpx;
   transition: width 0.5s ease;
}

.credit-tip {
   font-size: 22rpx;
   color: rgba(255, 255, 255, 0.7);
}

.balance-card {
   background-color: #fff;
   margin: -24rpx 24rpx 24rpx;
   border-radius: 20rpx;
   padding: 32rpx;
   box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.1);
}

.balance-left {
   display: flex;
   flex-direction: column;
   gap: 8rpx;
   margin-bottom: 20rpx;
}

.balance-label {
   font-size: 26rpx;
   color: #64748b;
}

.balance-amount {
   font-size: 48rpx;
   font-weight: 700;
   color: #1e293b;
}

.withdraw-btn {
   width: 100%;
   height: 80rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #fff;
   font-weight: 500;
   margin-bottom: 20rpx;

   &:active {
      opacity: 0.9;
   }
}

.balance-links {
   display: flex;
   justify-content: center;
   gap: 64rpx;
}

.link-item {
   display: flex;
   align-items: center;
   gap: 8rpx;
}

.link-icon {
   font-size: 24rpx;
}

.link-text {
   font-size: 26rpx;
   color: #3b82f6;
}

.menu-scroll {
   height: calc(100vh - 520rpx);
}

.menu-card {
   background-color: #fff;
   margin: 0 24rpx 24rpx;
   border-radius: 16rpx;
   overflow: hidden;
}

.menu-item {
   display: flex;
   align-items: center;
   padding: 28rpx 24rpx;
   border-bottom: 1rpx solid #f1f5f9;

   &:last-child {
      border-bottom: none;
   }

   &:active {
      background-color: #f8fafc;
   }
}

.menu-icon-wrap {
   width: 80rpx;
   height: 80rpx;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-right: 20rpx;

   &.blue {
      background-color: #eff6ff;
   }

   &.orange {
      background-color: #fff7ed;
   }

   &.red {
      background-color: #fef2f2;
   }

   &.gray {
      background-color: #f8fafc;
   }
}

.menu-icon {
   font-size: 36rpx;
}

.menu-content {
   flex: 1;
   display: flex;
   flex-direction: column;
   gap: 6rpx;
}

.menu-title {
   font-size: 28rpx;
   font-weight: 500;
   color: #1e293b;
}

.menu-desc {
   font-size: 22rpx;
   color: #94a3b8;
}

.menu-arrow {
   font-size: 32rpx;
   color: #94a3b8;
}

.resume-actions {
   display: flex;
   justify-content: space-around;
   padding: 24rpx;
   border-top: 1rpx solid #f1f5f9;
}

.action-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 8rpx;

   &:active {
      opacity: 0.7;
   }
}

.action-icon {
   font-size: 32rpx;
}

.action-text {
   font-size: 24rpx;
   color: #64748b;
}

.logout-section {
   padding: 32rpx 24rpx;
}

.logout-btn {
   width: 100%;
   height: 88rpx;
   background-color: #fff;
   border: 2rpx solid #ef4444;
   border-radius: 12rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 12rpx;

   &:active {
      background-color: #fef2f2;
   }
}

.logout-icon {
   font-size: 28rpx;
}

.logout-text {
   font-size: 28rpx;
   color: #ef4444;
   font-weight: 500;
}

.tab-bar {
   position: fixed;
   bottom: 0;
   left: 0;
   right: 0;
   background-color: #fff;
   padding: 16rpx 0 40rpx;
   display: flex;
   justify-content: space-around;
   border-top: 1rpx solid #e2e8f0;
   box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.tab-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 8rpx;
   padding: 8rpx 40rpx;

   &.active {
      .tab-text {
         color: #3b82f6;
         font-weight: 500;
      }
   }

   &:active {
      opacity: 0.7;
   }
}

.tab-icon {
   font-size: 40rpx;
}

.tab-text {
   font-size: 22rpx;
   color: #94a3b8;
}
</style>
