<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <tn-icon name="arrow-left" size="36" color="#333"></tn-icon>
         </view>
         <text class="header-title">投诉与反馈</text>
         <view class="placeholder"></view>
      </view>

      <!-- 内容区域 -->
      <scroll-view scroll-y class="content">
         <!-- 反馈类型 -->
         <view class="section">
            <text class="section-title">反馈类型</text>
            <view class="type-options">
               <view
                  v-for="(type, index) in feedbackTypes"
                  :key="index"
                  class="type-option"
                  :class="{ active: selectedType === type.value }"
                  @click="selectedType = type.value">
                  <tn-icon
                     :name="type.icon"
                     size="32"
                     :color="
                        selectedType === type.value ? '#3B82F6' : '#94A3B8'
                     "></tn-icon>
                  <text class="type-text">{{ type.label }}</text>
               </view>
            </view>
         </view>

         <!-- 反馈内容 -->
         <view class="section">
            <text class="section-title">反馈内容</text>
            <textarea
               v-model="feedbackContent"
               class="content-textarea"
               placeholder="请详细描述您的问题或建议..."
               :maxlength="500"></textarea>
            <text class="word-count">{{ feedbackContent.length }}/500</text>
         </view>

         <!-- 联系方式 -->
         <view class="section">
            <text class="section-title">联系方式</text>
            <input
               v-model="contactInfo"
               class="contact-input"
               placeholder="请输入您的联系方式（手机号/邮箱）" />
         </view>

         <!-- 上传图片 -->
         <view class="section">
            <text class="section-title">上传图片（可选）</text>
            <view class="image-upload">
               <view
                  v-for="(img, index) in uploadedImages"
                  :key="index"
                  class="image-item">
                  <image
                     :src="img"
                     mode="aspectFill"
                     class="uploaded-image"></image>
                  <view class="delete-img" @click="deleteImage(index)">
                     <tn-icon name="close" size="24" color="#fff"></tn-icon>
                  </view>
               </view>
               <view
                  class="add-image"
                  @click="addImage"
                  v-if="uploadedImages.length < 9">
                  <tn-icon name="plus" size="40" color="#94A3B8"></tn-icon>
               </view>
            </view>
            <text class="upload-tip">最多上传9张图片</text>
         </view>

         <!-- 提交按钮 -->
         <button class="submit-btn" @click="submitFeedback">
            <tn-icon name="send" size="28" color="#fff"></tn-icon>
            <text class="btn-text">提交反馈</text>
         </button>
      </scroll-view>

      <!-- 成功弹窗 -->
      <view
         v-if="showSuccessModal"
         class="modal-overlay"
         @click="closeSuccessModal">
         <view class="modal-content" @click.stop>
            <view class="success-icon">
               <tn-icon name="check" size="64" color="#10B981"></tn-icon>
            </view>
            <text class="modal-title">提交成功</text>
            <text class="modal-desc">感谢您的反馈，我们会尽快处理</text>
            <button class="modal-btn" @click="goBack">我知道了</button>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// 反馈类型选项
const feedbackTypes = [
   { label: '功能问题', value: 'function', icon: 'bug' },
   { label: '内容投诉', value: 'content', icon: 'flag-o' },
   { label: '意见建议', value: 'suggestion', icon: 'lightbulb-o' },
   { label: '其他', value: 'other', icon: 'more-horizontal' }
];

// 选中的类型
const selectedType = ref('function');

// 反馈内容
const feedbackContent = ref('');

// 联系方式
const contactInfo = ref('');

// 上传的图片
const uploadedImages = ref<string[]>([]);

// 是否显示成功弹窗
const showSuccessModal = ref(false);

// 返回
const goBack = () => {
   uni.navigateBack();
};

// 添加图片
const addImage = () => {
   uni.chooseImage({
      count: 9 - uploadedImages.value.length,
      success: res => {
         uploadedImages.value = [...uploadedImages.value, ...res.tempFilePaths];
      }
   });
};

// 删除图片
const deleteImage = (index: number) => {
   uploadedImages.value.splice(index, 1);
};

// 提交反馈
const submitFeedback = () => {
   if (!feedbackContent.value.trim()) {
      uni.showToast({
         title: '请输入反馈内容',
         icon: 'none'
      });
      return;
   }

   uni.showLoading({
      title: '提交中...',
      mask: true
   });

   setTimeout(() => {
      uni.hideLoading();
      showSuccessModal.value = true;
   }, 1000);
};

// 关闭成功弹窗
const closeSuccessModal = () => {
   showSuccessModal.value = false;
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

.placeholder {
   width: 72rpx;
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

.section-title {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
   margin-bottom: 24rpx;
}

.type-options {
   display: flex;
   flex-wrap: wrap;
   gap: 20rpx;
}

.type-option {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 12rpx;
   padding: 24rpx 32rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   min-width: 140rpx;

   &.active {
      border-color: #3b82f6;
      background-color: rgba(59, 130, 246, 0.05);
   }
}

.type-text {
   font-size: 26rpx;
   color: #1e293b;
}

.content-textarea {
   width: 100%;
   min-height: 280rpx;
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

.contact-input {
   width: 100%;
   height: 88rpx;
   padding: 0 24rpx;
   background-color: #f8fafc;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;
}

.image-upload {
   display: flex;
   flex-wrap: wrap;
   gap: 16rpx;
}

.image-item {
   position: relative;
   width: 180rpx;
   height: 180rpx;
   border-radius: 12rpx;
   overflow: hidden;
}

.uploaded-image {
   width: 100%;
   height: 100%;
}

.delete-img {
   position: absolute;
   top: 8rpx;
   right: 8rpx;
   width: 48rpx;
   height: 48rpx;
   background-color: rgba(0, 0, 0, 0.6);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
}

.add-image {
   width: 180rpx;
   height: 180rpx;
   border: 2rpx dashed #cbd5e1;
   border-radius: 12rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   background-color: #f8fafc;

   &:active {
      background-color: #f1f5f9;
   }
}

.upload-tip {
   display: block;
   font-size: 24rpx;
   color: #94a3b8;
   margin-top: 16rpx;
}

.submit-btn {
   width: 100%;
   height: 96rpx;
   background: linear-gradient(135deg, #3b82f6 0%, #60a5fa 100%);
   border: none;
   border-radius: 16rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 12rpx;
   margin-top: 16rpx;

   &:active {
      opacity: 0.9;
      transform: scale(0.98);
   }
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
