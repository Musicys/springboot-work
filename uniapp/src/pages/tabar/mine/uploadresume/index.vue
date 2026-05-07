<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <tn-icon name="arrow-left" size="36" color="#333"></tn-icon>
         </view>
         <text class="header-title">上传简历</text>
         <view class="save-btn" @click="saveResume">保存</view>
      </view>

      <!-- 内容区域 -->
      <scroll-view scroll-y class="content">
         <!-- 基础信息 -->
         <view class="section">
            <view class="section-header">
               <tn-icon name="user" size="28" color="#3B82F6"></tn-icon>
               <text class="section-title">基础信息</text>
            </view>

            <view class="form-item">
               <text class="form-label">姓名</text>
               <input
                  v-model="resumeInfo.name"
                  class="form-input"
                  placeholder="请输入姓名" />
            </view>

            <view class="form-item">
               <text class="form-label">性别</text>
               <view class="gender-options">
                  <view
                     class="gender-option"
                     :class="{ active: resumeInfo.gender === 'male' }"
                     @click="resumeInfo.gender = 'male'">
                     <tn-icon
                        name="male"
                        size="32"
                        :color="
                           resumeInfo.gender === 'male' ? '#3B82F6' : '#94A3B8'
                        "></tn-icon>
                     <text class="gender-text">男</text>
                  </view>
                  <view
                     class="gender-option"
                     :class="{ active: resumeInfo.gender === 'female' }"
                     @click="resumeInfo.gender = 'female'">
                     <tn-icon
                        name="female"
                        size="32"
                        :color="
                           resumeInfo.gender === 'female'
                              ? '#3B82F6'
                              : '#94A3B8'
                        "></tn-icon>
                     <text class="gender-text">女</text>
                  </view>
               </view>
            </view>

            <view class="form-item">
               <text class="form-label">年龄</text>
               <input
                  v-model="resumeInfo.age"
                  class="form-input"
                  placeholder="请输入年龄"
                  type="number" />
            </view>

            <view class="form-item">
               <text class="form-label">学历</text>
               <picker
                  :value="educationIndex"
                  :range="educationOptions"
                  @change="handleEducationChange">
                  <view class="picker-value">
                     <text>{{
                        educationOptions[educationIndex] || '请选择学历'
                     }}</text>
                     <tn-icon
                        name="angle-down"
                        size="28"
                        color="#94A3B8"></tn-icon>
                  </view>
               </picker>
            </view>

            <view class="form-item">
               <text class="form-label">专业</text>
               <input
                  v-model="resumeInfo.major"
                  class="form-input"
                  placeholder="请输入专业" />
            </view>

            <view class="form-item">
               <text class="form-label">毕业院校</text>
               <input
                  v-model="resumeInfo.school"
                  class="form-input"
                  placeholder="请输入毕业院校" />
            </view>
         </view>

         <!-- 联系方式 -->
         <view class="section">
            <view class="section-header">
               <tn-icon name="phone" size="28" color="#3B82F6"></tn-icon>
               <text class="section-title">联系方式</text>
            </view>

            <view class="form-item">
               <text class="form-label">手机号</text>
               <input
                  v-model="resumeInfo.phone"
                  class="form-input"
                  placeholder="请输入手机号"
                  type="tel" />
            </view>

            <view class="form-item">
               <text class="form-label">邮箱</text>
               <input
                  v-model="resumeInfo.email"
                  class="form-input"
                  placeholder="请输入邮箱"
                  type="email" />
            </view>

            <view class="form-item">
               <text class="form-label">所在城市</text>
               <input
                  v-model="resumeInfo.city"
                  class="form-input"
                  placeholder="请输入所在城市" />
            </view>
         </view>

         <!-- 工作经历 -->
         <view class="section">
            <view class="section-header">
               <tn-icon name="briefcase" size="28" color="#3B82F6"></tn-icon>
               <text class="section-title">工作经历</text>
            </view>

            <textarea
               v-model="resumeInfo.experience"
               class="form-textarea"
               placeholder="请描述您的工作经历..."
               :maxlength="500"></textarea>
            <text class="word-count"
               >{{ resumeInfo.experience.length }}/500</text
            >
         </view>

         <!-- 技能特长 -->
         <view class="section">
            <view class="section-header">
               <tn-icon name="star" size="28" color="#3B82F6"></tn-icon>
               <text class="section-title">技能特长</text>
            </view>

            <textarea
               v-model="resumeInfo.skills"
               class="form-textarea"
               placeholder="请描述您的技能特长..."
               :maxlength="300"></textarea>
            <text class="word-count">{{ resumeInfo.skills.length }}/300</text>
         </view>

         <!-- 上传附件 -->
         <view class="section">
            <view class="section-header">
               <tn-icon name="file-pdf-o" size="28" color="#3B82F6"></tn-icon>
               <text class="section-title">上传附件</text>
            </view>

            <view class="upload-area" @click="handleUpload">
               <tn-icon name="upload" size="48" color="#94A3B8"></tn-icon>
               <text class="upload-text">{{
                  resumeFile || '点击上传简历文件'
               }}</text>
            </view>
            <text class="upload-tip">支持 PDF、Word 格式，大小不超过 5MB</text>
         </view>

         <!-- 完善度显示 -->
         <view class="progress-section">
            <view class="progress-header">
               <text class="progress-label">简历完善度</text>
               <text class="progress-percent">{{ completeness }}%</text>
            </view>
            <view class="progress-bar">
               <view
                  class="progress-fill"
                  :style="{ width: completeness + '%' }"></view>
            </view>
            <text class="progress-tip">完善度越高，越容易获得兼职机会</text>
         </view>
      </scroll-view>
   </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';

// 学历选项
const educationOptions = ['高中/中专', '大专', '本科', '硕士', '博士'];
const educationIndex = ref(0);

// 简历信息
const resumeInfo = reactive({
   name: '',
   gender: 'male',
   age: '',
   major: '',
   school: '',
   phone: '',
   email: '',
   city: '',
   experience: '',
   skills: ''
});

// 简历文件名
const resumeFile = ref('');

// 计算完善度
const completeness = computed(() => {
   let filled = 0;
   let total = 10;

   if (resumeInfo.name) filled++;
   if (resumeInfo.gender) filled++;
   if (resumeInfo.age) filled++;
   if (educationIndex.value > 0) filled++;
   if (resumeInfo.major) filled++;
   if (resumeInfo.school) filled++;
   if (resumeInfo.phone) filled++;
   if (resumeInfo.email) filled++;
   if (resumeInfo.experience) filled++;
   if (resumeInfo.skills) filled++;

   return Math.round((filled / total) * 100);
});

// 返回
const goBack = () => {
   uni.navigateBack();
};

// 学历选择
const handleEducationChange = (e: any) => {
   educationIndex.value = e.detail.value;
};

// 上传文件
const handleUpload = () => {
   uni.showToast({
      title: '文件上传功能开发中',
      icon: 'none'
   });
};

// 保存简历
const saveResume = () => {
   if (!resumeInfo.name) {
      uni.showToast({
         title: '请填写姓名',
         icon: 'none'
      });
      return;
   }

   if (!resumeInfo.phone) {
      uni.showToast({
         title: '请填写手机号',
         icon: 'none'
      });
      return;
   }

   uni.showLoading({
      title: '保存中...',
      mask: true
   });

   setTimeout(() => {
      uni.hideLoading();
      uni.showToast({
         title: '保存成功',
         icon: 'success',
         duration: 1500
      });
      setTimeout(() => {
         uni.navigateBack();
      }, 1500);
   }, 1000);
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f1f5f9;
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

.header-title {
   font-size: 34rpx;
   font-weight: 600;
   color: #1e293b;
}

.save-btn {
   font-size: 30rpx;
   color: #3b82f6;
   font-weight: 500;
   padding: 12rpx 24rpx;
}

.content {
   padding: 32rpx;
   height: calc(100vh - 120rpx);
}

.section {
   background-color: #fff;
   border-radius: 16rpx;
   padding: 28rpx;
   margin-bottom: 24rpx;
}

.section-header {
   display: flex;
   align-items: center;
   gap: 12rpx;
   margin-bottom: 24rpx;
}

.section-title {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
}

.form-item {
   margin-bottom: 28rpx;

   &:last-child {
      margin-bottom: 0;
   }
}

.form-label {
   display: block;
   font-size: 26rpx;
   color: #64748b;
   margin-bottom: 12rpx;
}

.form-input {
   width: 100%;
   height: 88rpx;
   padding: 0 24rpx;
   background-color: #f8fafc;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;

   &:focus {
      border-color: #3b82f6;
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
   padding: 20rpx 48rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;

   &.active {
      border-color: #3b82f6;
      background-color: rgba(59, 130, 246, 0.05);
   }
}

.gender-text {
   font-size: 26rpx;
   color: #1e293b;
}

.picker-value {
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: 88rpx;
   padding: 0 24rpx;
   background-color: #f8fafc;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
}

.form-textarea {
   width: 100%;
   min-height: 200rpx;
   padding: 24rpx;
   background-color: #f8fafc;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;
}

.word-count {
   display: block;
   text-align: right;
   font-size: 24rpx;
   color: #94a3b8;
   margin-top: 12rpx;
}

.upload-area {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   gap: 16rpx;
   height: 200rpx;
   border: 2rpx dashed #cbd5e1;
   border-radius: 12rpx;
   background-color: #f8fafc;

   &:active {
      background-color: #f1f5f9;
   }
}

.upload-text {
   font-size: 28rpx;
   color: #64748b;
}

.upload-tip {
   display: block;
   font-size: 24rpx;
   color: #94a3b8;
   margin-top: 16rpx;
}

.progress-section {
   background-color: #fff;
   border-radius: 16rpx;
   padding: 28rpx;
   margin-top: 24rpx;
}

.progress-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 16rpx;
}

.progress-label {
   font-size: 28rpx;
   color: #1e293b;
   font-weight: 500;
}

.progress-percent {
   font-size: 32rpx;
   color: #3b82f6;
   font-weight: 600;
}

.progress-bar {
   height: 12rpx;
   background-color: #e2e8f0;
   border-radius: 6rpx;
   overflow: hidden;
}

.progress-fill {
   height: 100%;
   background: linear-gradient(90deg, #3b82f6 0%, #60a5fa 100%);
   border-radius: 6rpx;
   transition: width 0.5s ease;
}

.progress-tip {
   display: block;
   font-size: 24rpx;
   color: #94a3b8;
   margin-top: 12rpx;
}
</style>
