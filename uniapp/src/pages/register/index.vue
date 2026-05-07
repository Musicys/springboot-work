<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <text class="back-icon">←</text>
         </view>
         <text class="header-title">用户注册</text>
         <view class="placeholder"></view>
      </view>

      <!-- 注册表单 -->
      <scroll-view scroll-y class="form-scroll">
         <view class="form-container">
            <!-- 头像上传 -->
            <view class="avatar-section">
               <view class="avatar-wrap">
                  <image
                     :src="avatarUrl"
                     class="avatar-img"
                     mode="aspectFill"></image>
                  <view class="avatar-upload" @click="handleAvatarUpload">
                     <text class="upload-icon">📷</text>
                  </view>
               </view>
               <text class="avatar-tip">点击更换头像</text>
            </view>

            <!-- 必要信息 -->
            <view class="section">
               <view class="section-header">
                  <text class="section-icon">ℹ️</text>
                  <text class="section-title">必要信息</text>
               </view>

               <!-- 学号 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">🆔</text>
                     <text class="label-text">学号</text>
                  </view>
                  <input
                     type="text"
                     v-model="registerForm.studentId"
                     class="form-input"
                     placeholder="请输入学号" />
               </view>

               <!-- 手机号 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">👤</text>
                     <text class="label-text">账号（手机号）</text>
                  </view>
                  <input
                     type="tel"
                     v-model="registerForm.mobile"
                     class="form-input"
                     placeholder="请输入手机号"
                     maxlength="11" />
               </view>

               <!-- 密码 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">🔒</text>
                     <text class="label-text">密码</text>
                  </view>
                  <input
                     type="password"
                     v-model="registerForm.password"
                     class="form-input"
                     placeholder="请输入密码（至少6位）" />
               </view>

               <!-- 确认密码 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">🔒</text>
                     <text class="label-text">确认密码</text>
                  </view>
                  <input
                     type="password"
                     v-model="registerForm.confirmPassword"
                     class="form-input"
                     placeholder="请再次输入密码" />
               </view>

               <!-- 年龄 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">🎂</text>
                     <text class="label-text">年龄</text>
                  </view>
                  <input
                     type="number"
                     v-model="registerForm.age"
                     class="form-input"
                     placeholder="请输入年龄" />
               </view>

               <!-- 性别 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">⚧️</text>
                     <text class="label-text">性别</text>
                  </view>
                  <picker
                     mode="selector"
                     :range="genderOptions"
                     range-key="label"
                     @change="onGenderChange">
                     <view class="picker-wrap">
                        <text class="picker-text">{{
                           currentGenderLabel
                        }}</text>
                        <text class="picker-arrow">▼</text>
                     </view>
                  </picker>
               </view>
            </view>

            <!-- 非必要信息 -->
            <view class="section">
               <view class="section-header">
                  <text class="section-icon">✏️</text>
                  <text class="section-title">非必要信息</text>
               </view>

               <!-- 真实姓名 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">👤</text>
                     <text class="label-text">真实姓名</text>
                  </view>
                  <input
                     type="text"
                     v-model="registerForm.realName"
                     class="form-input"
                     placeholder="请输入真实姓名" />
               </view>

               <!-- 电话 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">📞</text>
                     <text class="label-text">电话</text>
                  </view>
                  <input
                     type="tel"
                     v-model="registerForm.phone"
                     class="form-input"
                     placeholder="请输入电话号码" />
               </view>

               <!-- 职业 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">💼</text>
                     <text class="label-text">职业</text>
                  </view>
                  <input
                     type="text"
                     v-model="registerForm.occupation"
                     class="form-input"
                     placeholder="请输入职业" />
               </view>

               <!-- 个人简介 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">📝</text>
                     <text class="label-text">个人简介</text>
                  </view>
                  <textarea
                     v-model="registerForm.bio"
                     class="form-textarea"
                     placeholder="请输入个人简介" />
               </view>

               <!-- 简历上传 -->
               <view class="form-item">
                  <view class="form-label">
                     <text class="label-icon">📄</text>
                     <text class="label-text">简历</text>
                  </view>
                  <view class="upload-area" @click="handleResumeUpload">
                     <text class="upload-icon-large">📁</text>
                     <text class="upload-text">{{
                        resumeName || '点击上传简历'
                     }}</text>
                  </view>
                  <text class="upload-tip"
                     >支持 PDF、Word 格式，大小不超过 5MB</text
                  >
               </view>
            </view>

            <!-- 协议勾选 -->
            <view class="agree-section">
               <view class="checkbox-wrap" @click="agreed = !agreed">
                  <view class="checkbox" :class="{ checked: agreed }">
                     <text v-if="agreed" class="check-icon">✓</text>
                  </view>
               </view>
               <text class="agree-text">
                  注册即表示同意
                  <text class="link-text">《用户服务协议》</text>
                  和
                  <text class="link-text">《隐私政策》</text>
               </text>
            </view>

            <!-- 注册按钮 -->
            <button class="register-btn" @click="handleRegister">
               <text class="btn-icon">➕</text>
               <text class="btn-text">注册</text>
            </button>
         </view>
      </scroll-view>

      <!-- 注册成功弹窗 -->
      <view v-if="!showSuccessModal" class="modal-overlay" @click="closeModal">
         <view class="modal-content" @click.stop>
            <view class="success-icon">✓</view>
            <text class="modal-title">注册成功</text>
            <text class="modal-desc">账号已创建成功，即将跳转到登录页面</text>
            <button class="modal-btn" @click="goToLogin">我知道了</button>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { getRandomNickname, getRandomAvatar } from './index';
import { register } from '@/api/user';
import router from '@/router';
// 注册表单
const registerForm = reactive({
   studentId: '2023010101',
   mobile: '13800000001',
   password: '12345678',
   confirmPassword: '12345678',
   age: '18',
   gender: 1,
   realName: '张三',
   phone: '13800000000',
   occupation: '学生',
   bio: '一个学生'
});

// 性别选项
const genderOptions = [
   { label: '男', value: 1 },
   { label: '女', value: 0 }
];

// 当前性别标签
const currentGenderLabel = computed(() => {
   const selected = genderOptions.find(g => g.value === registerForm.gender);
   return selected ? selected.label : '男';
});

// 性别选择变化
const onGenderChange = (e: any) => {
   const index = e.detail.value;
   registerForm.gender = genderOptions[index].value;
};

// 头像URL
const avatarUrl = ref('');

// 简历文件名
const resumeName = ref('');

// 是否同意协议
const agreed = ref(true);

// 是否显示成功弹窗
const showSuccessModal = ref(true);

// 初始化随机头像
onMounted(async () => {
   try {
      const avatar = await getRandomAvatar();
      if (avatar && avatar.data) {
         avatarUrl.value = avatar.data;
      } else {
         avatarUrl.value = 'https://picsum.photos/seed/avatar/200/200';
      }
   } catch {
      avatarUrl.value = 'https://picsum.photos/seed/avatar/200/200';
   }
});

// 返回
const goBack = () => {
   uni.navigateBack();
};

// 头像上传
const handleAvatarUpload = async () => {
   try {
      const avatar = await getRandomAvatar();
      if (avatar && avatar.data) {
         avatarUrl.value = avatar.data;
         uni.showToast({
            title: '头像已更换',
            icon: 'success'
         });
      }
   } catch {
      uni.showToast({
         title: '更换头像失败',
         icon: 'none'
      });
   }
};

// 简历上传
const handleResumeUpload = () => {
   uni.showToast({
      title: '简历上传功能开发中',
      icon: 'none'
   });
};

// 处理注册
const handleRegister = async () => {
   // 表单验证
   if (!registerForm.mobile || registerForm.mobile.length !== 11) {
      uni.showToast({
         title: '请输入正确的手机号',
         icon: 'none'
      });
      return;
   }

   if (!registerForm.password || registerForm.password.length < 6) {
      uni.showToast({
         title: '请输入至少6位密码',
         icon: 'none'
      });
      return;
   }

   if (registerForm.password !== registerForm.confirmPassword) {
      uni.showToast({
         title: '两次输入的密码不一致',
         icon: 'none'
      });
      return;
   }

   if (!registerForm.studentId) {
      uni.showToast({
         title: '请输入学号',
         icon: 'none'
      });
      return;
   }

   const age = registerForm.age ? parseInt(registerForm.age) : null;
   if (registerForm.age && (age! < 16 || age! > 60)) {
      uni.showToast({
         title: '请输入有效的年龄（16-60岁）',
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

   // 调用注册接口
   uni.showLoading({
      title: '注册中...',
      mask: true
   });

   try {
      const res: any = await register({
         username: registerForm.mobile,
         password: registerForm.password,
         studentId: registerForm.studentId,
         realName: registerForm.realName,
         age: age,
         gender: registerForm.gender,
         phone: registerForm.phone,
         profession: registerForm.occupation,
         introduction: registerForm.bio,
         avatarUrl: avatarUrl.value
      });

      uni.hideLoading();

      if (res.code === 0) {
         showSuccessModal.value = false;
      } else {
         uni.showToast({
            title: res.message || '注册失败',
            icon: 'none'
         });
      }
   } catch (e: any) {
      uni.hideLoading();
      uni.showToast({
         title: e.message || '注册失败，请稍后重试',
         icon: 'none'
      });
   }
};

// 关闭弹窗
const closeModal = () => {
   showSuccessModal.value = false;
   goToLogin();
};

// 跳转到登录页
const goToLogin = () => {
   router.push({
      name: 'login',
      params: { username: registerForm.mobile }
   });
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
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

.back-btn {
   width: 72rpx;
   height: 72rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.back-icon {
   font-size: 40rpx;
   color: #333;
}

.header-title {
   font-size: 34rpx;
   font-weight: 600;
   color: #1e293b;
}

.placeholder {
   width: 72rpx;
}

.form-scroll {
   height: calc(100vh - 120rpx);
}

.form-container {
   padding: 32rpx;
}

.avatar-section {
   display: flex;
   flex-direction: column;
   align-items: center;
   margin-bottom: 40rpx;
}

.avatar-wrap {
   position: relative;
   width: 192rpx;
   height: 192rpx;
   margin-bottom: 16rpx;
}

.avatar-img {
   width: 100%;
   height: 100%;
   border-radius: 50%;
   border: 4rpx solid #e2e8f0;
}

.avatar-upload {
   position: absolute;
   right: 0;
   bottom: 0;
   width: 64rpx;
   height: 64rpx;
   background-color: #3b82f6;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   border: 4rpx solid #fff;

   &:active {
      opacity: 0.8;
   }
}

.upload-icon {
   font-size: 28rpx;
}

.avatar-tip {
   font-size: 26rpx;
   color: #64748b;
}

.section {
   margin-bottom: 32rpx;
}

.section-header {
   display: flex;
   align-items: center;
   gap: 12rpx;
   margin-bottom: 24rpx;
}

.section-icon {
   font-size: 32rpx;
}

.section-title {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
}

.form-item {
   margin-bottom: 28rpx;
}

.form-label {
   display: flex;
   align-items: center;
   gap: 12rpx;
   margin-bottom: 12rpx;
}

.label-icon {
   font-size: 28rpx;
}

.label-text {
   font-size: 28rpx;
   font-weight: 500;
   color: #1e293b;
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
      box-shadow: 0 0 0 4rpx rgba(59, 130, 246, 0.1);
   }
}

.gender-options {
   display: flex;
   gap: 40rpx;
}

.gender-option {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 8rpx;
   padding: 20rpx 40rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;

   &.active {
      border-color: #3b82f6;
      background-color: rgba(59, 130, 246, 0.05);
   }
}

.gender-icon {
   font-size: 36rpx;
}

.gender-text {
   font-size: 26rpx;
   color: #1e293b;
}

.picker-wrap {
   display: flex;
   justify-content: space-between;
   align-items: center;
   width: 100%;
   height: 88rpx;
   padding: 0 24rpx;
   background-color: #fff;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   box-sizing: border-box;
}

.picker-text {
   font-size: 28rpx;
   color: #1e293b;
}

.picker-arrow {
   font-size: 22rpx;
   color: #94a3b8;
}

.form-textarea {
   width: 100%;
   min-height: 200rpx;
   padding: 24rpx;
   background-color: #fff;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;

   &:focus {
      border-color: #3b82f6;
      box-shadow: 0 0 0 4rpx rgba(59, 130, 246, 0.1);
   }
}

.upload-area {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   gap: 12rpx;
   height: 160rpx;
   border: 2rpx dashed #cbd5e1;
   border-radius: 12rpx;
   background-color: #f8fafc;

   &:active {
      background-color: #f1f5f9;
   }
}

.upload-icon-large {
   font-size: 48rpx;
}

.upload-text {
   font-size: 26rpx;
   color: #64748b;
}

.upload-tip {
   display: block;
   font-size: 22rpx;
   color: #94a3b8;
   margin-top: 12rpx;
}

.agree-section {
   display: flex;
   align-items: flex-start;
   gap: 12rpx;
   margin-bottom: 32rpx;
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
}

.link-text {
   color: #3b82f6;
}

.register-btn {
   width: 100%;
   height: 96rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 12rpx;

   &:active {
      opacity: 0.9;
      transform: scale(0.98);
   }
}

.btn-icon {
   font-size: 28rpx;
}

.btn-text {
   font-size: 32rpx;
   font-weight: 600;
   color: #fff;
}

.modal-overlay {
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

.modal-content {
   width: 560rpx;
   background-color: #fff;
   border-radius: 24rpx;
   padding: 48rpx 32rpx;
   text-align: center;
}

.success-icon {
   width: 128rpx;
   height: 128rpx;
   background-color: #d1fae5;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   margin: 0 auto 24rpx;
   font-size: 64rpx;
   color: #10b981;
}

.modal-title {
   display: block;
   font-size: 36rpx;
   font-weight: 600;
   color: #1e293b;
   margin-bottom: 16rpx;
}

.modal-desc {
   display: block;
   font-size: 26rpx;
   color: #64748b;
   margin-bottom: 40rpx;
}

.modal-btn {
   width: 100%;
   height: 88rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   font-size: 30rpx;
   color: #fff;
   font-weight: 500;
}
</style>
