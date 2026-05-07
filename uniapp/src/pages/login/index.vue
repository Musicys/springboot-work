<template>
   <view class="page">
      <!-- 顶部品牌 -->
      <view class="brand-header">
         <view class="brand-content">
            <view class="brand-icon">
               <text class="icon-text">💼</text>
            </view>
            <text class="brand-title">兼职系统</text>
         </view>
         <text class="brand-desc">轻松找兼职，安全有保障</text>
      </view>

      <!-- 登录表单 -->
      <view class="form-container">
         <!-- 手机号 -->
         <view class="form-group">
            <view class="form-label">
               <text class="label-icon">👤</text>
               <text class="label-text">手机号</text>
            </view>
            <input
               type="tel"
               v-model="loginForm.username"
               class="form-input"
               placeholder="请输入手机号"
               maxlength="11" />
         </view>

         <!-- 密码 -->
         <view class="form-group">
            <view class="form-label-row">
               <view class="form-label">
                  <text class="label-icon">🔒</text>
                  <text class="label-text">密码</text>
               </view>
               <text class="forgot-password" @click="handleForgotPassword"
                  >忘记密码？</text
               >
            </view>
            <input
               type="password"
               v-model="loginForm.password"
               class="form-input"
               placeholder="请输入密码" />
         </view>

         <!-- 协议勾选 -->
         <view class="agree-section">
            <view class="checkbox-wrap" @click="agreed = !agreed">
               <view class="checkbox" :class="{ checked: agreed }">
                  <text v-if="agreed" class="check-icon">✓</text>
               </view>
            </view>
            <text class="agree-text">
               登录即表示同意
               <text class="link-text">《用户服务协议》</text>
               和
               <text class="link-text">《隐私政策》</text>
            </text>
         </view>

         <!-- 登录按钮 -->
         <button class="login-btn" @click="handleLogin">
            <text class="btn-icon">📲</text>
            <text class="btn-text">登录</text>
         </button>

         <!-- 其他登录方式 -->
         <view class="other-login">
            <view class="divider">
               <view class="line"></view>
               <text class="divider-text">其他登录方式</text>
               <view class="line"></view>
            </view>
            <view class="social-buttons">
               <view class="social-btn weixin" @click="handleWeixinLogin">
                  <text class="social-icon">💬</text>
               </view>
               <view class="social-btn qq" @click="handleQQLogin">
                  <text class="social-icon">🐧</text>
               </view>
            </view>
         </view>

         <!-- 注册跳转 -->
         <view class="register-link">
            <text class="register-text">还没有账号？</text>
            <text class="register-btn" @click="goToRegister">立即注册</text>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import * as api from '@/api/user/index';
import { useUserStore } from '@/store/user';
import { useRoute } from 'uni-mini-router';

const userStore = useUserStore();
const route = useRoute();

onMounted(() => {
   loginForm.username = route.params.username || loginForm.username;
});

const loginForm = reactive({
   username: '13800000001',
   password: '12345678'
});

// 是否同意协议
const agreed = ref(true);

// 处理登录
const handleLogin = async () => {
   if (!loginForm.username) {
      uni.showToast({
         title: '请输入正确手机号',
         icon: 'none'
      });
      return;
   }

   if (!loginForm.password || loginForm.password.length < 6) {
      uni.showToast({
         title: '请输入至少6位密码',
         icon: 'none'
      });
      return;
   }

   if (!agreed.value) {
      uni.showToast({
         title: '请同意用户服务协议',
         icon: 'none'
      });
      return;
   }

   // 模拟登录成功
   uni.showLoading({
      title: '登录中...',
      mask: true
   });

   try {
      const res: any = await api.login(loginForm);

      if (res.code === 0) {
         userStore.setUserInfo({
            userId: res.data.userId,
            username: res.data.username,
            userType: res.data.userType,
            avatarUrl: res.data.avatarUrl,
            studentId: res.data.studentId || '',
            realName: res.data.realName || '',
            age: res.data.age || 0,
            gender: res.data.gender || 0,
            phone: res.data.phone || '',
            profession: res.data.profession || '',
            introduction: res.data.introduction || '',
            tagName: res.data.tagName || ''
         });

         setTimeout(() => {
            uni.hideLoading();
            uni.showToast({
               title: '登录成功',
               icon: 'success',
               duration: 1500
            });
            setTimeout(() => {
               uni.switchTab({
                  url: '/pages/tabar/index'
               });
            }, 1500);
         }, 100);
      }
   } catch (err) {
      uni.hideLoading();
      console.error('登录失败', err);
   }
};

// 忘记密码
const handleForgotPassword = () => {
   uni.showToast({
      title: '忘记密码功能开发中',
      icon: 'none'
   });
};

// 微信登录
const handleWeixinLogin = () => {
   uni.showToast({
      title: '微信登录功能开发中',
      icon: 'none'
   });
};

// QQ登录
const handleQQLogin = () => {
   uni.showToast({
      title: 'QQ登录功能开发中',
      icon: 'none'
   });
};

// 跳转注册
const goToRegister = () => {
   uni.navigateTo({
      url: '/pages/register/index'
   });
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
}

.brand-header {
   background-color: #3b82f6;
   padding: 80rpx 40rpx 60rpx;
   text-align: center;
}

.brand-content {
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 16rpx;
}

.brand-icon {
   font-size: 48rpx;
}

.icon-text {
   font-size: 48rpx;
}

.brand-title {
   font-size: 48rpx;
   font-weight: bold;
   color: #fff;
}

.brand-desc {
   display: block;
   font-size: 26rpx;
   color: rgba(255, 255, 255, 0.8);
   margin-top: 16rpx;
}

.form-container {
   padding: 48rpx 40rpx;
}

.form-group {
   margin-bottom: 40rpx;
}

.form-label {
   display: flex;
   align-items: center;
   gap: 12rpx;
   margin-bottom: 16rpx;
}

.label-icon {
   font-size: 28rpx;
}

.label-text {
   font-size: 28rpx;
   font-weight: 500;
   color: #1e293b;
}

.form-label-row {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 16rpx;
}

.forgot-password {
   font-size: 24rpx;
   color: #3b82f6;
}

.form-input {
   width: 100%;
   height: 88rpx;
   padding: 0 24rpx;
   background-color: #fff;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;

   &:focus {
      border-color: #3b82f6;
      box-shadow: 0 0 0 4rpx rgba(59, 130, 246, 0.15);
   }
}

.agree-section {
   display: flex;
   align-items: flex-start;
   gap: 12rpx;
   margin-bottom: 48rpx;
}

.checkbox-wrap {
   padding-top: 6rpx;
}

.checkbox {
   width: 36rpx;
   height: 36rpx;
   border: 2rpx solid #cbd5e1;
   border-radius: 6rpx;
   display: flex;
   align-items: center;
   justify-content: center;

   &.checked {
      background-color: #3b82f6;
      border-color: #3b82f6;
   }
}

.check-icon {
   color: #fff;
   font-size: 24rpx;
   font-weight: bold;
}

.agree-text {
   font-size: 24rpx;
   color: #64748b;
   flex: 1;
   line-height: 1.5;
}

.link-text {
   color: #3b82f6;
}

.login-btn {
   width: 100%;
   height: 96rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 12rpx;
   margin-bottom: 60rpx;

   &:active {
      opacity: 0.9;
      transform: scale(0.98);
   }
}

.btn-icon {
   font-size: 32rpx;
}

.btn-text {
   font-size: 32rpx;
   font-weight: 600;
   color: #fff;
}

.other-login {
   margin-bottom: 40rpx;
}

.divider {
   display: flex;
   align-items: center;
   gap: 20rpx;
   margin-bottom: 32rpx;
}

.line {
   flex: 1;
   height: 1rpx;
   background-color: #e2e8f0;
}

.divider-text {
   font-size: 24rpx;
   color: #94a3b8;
}

.social-buttons {
   display: flex;
   justify-content: center;
   gap: 100rpx;
}

.social-btn {
   width: 88rpx;
   height: 88rpx;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;

   &.weixin {
      background-color: #e0f2fe;
   }

   &.qq {
      background-color: #eff6ff;
   }

   &:active {
      opacity: 0.8;
      transform: scale(0.95);
   }
}

.social-icon {
   font-size: 40rpx;
}

.register-link {
   display: flex;
   justify-content: center;
   align-items: center;
   gap: 8rpx;
   margin-top: 20rpx;
}

.register-text {
   font-size: 26rpx;
   color: #64748b;
}

.register-btn {
   font-size: 26rpx;
   color: #3b82f6;
   font-weight: 500;
}
</style>
