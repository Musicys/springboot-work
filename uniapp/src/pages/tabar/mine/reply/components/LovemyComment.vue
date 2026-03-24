<template>
   <view class="love-my-comment-container" @click="handleViewDynamic">
      <!-- 左侧：用户信息和互动区域 -->
      <view class="left-section">
         <!-- 用户头像 -->
         <image
            :src="localData.avatarUrl || ''"
            mode="aspectFill"
            class="user-avatar"
            @click.stop="handleViewUser" />
         <!-- 互动信息 -->
         <view class="interaction-info">
            <view class="top-row">
               <!-- 用户名 -->
               <text class="username" @click.stop="handleViewUser">{{
                  localData.username || '用户'
               }}</text>
            </view>
            <!-- 评论内容 -->
            <text class="comment-text">{{
               localData.commentsContext || ''
            }}</text>
            <!-- 时间 -->
            <text class="time">{{ formatTime }}</text>
         </view>
      </view>

      <!-- 右侧：动态图片区域 -->
      <view class="right-section" @click.stop="handleViewDynamic">
         <!-- 动态第一张图片 -->
         <image
            v-if="dynamicImage"
            :src="dynamicImage"
            mode="aspectFill"
            class="dynamic-image"
            @click.stop="handleImageClick" />
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { DynamicCommentVo } from '@/api/love/model/type';
import { useRouter } from 'uni-mini-router';
const router = useRouter();
const props = defineProps<{
   data: DynamicCommentVo;
}>();

// 创建本地响应式数据副本，符合单向数据流
const localData = ref<DynamicCommentVo>({ ...props.data });

// 监听props变化，更新本地数据
watch(
   () => props.data,
   newData => {
      localData.value = { ...newData };
   },
   { deep: true }
);

// 计算属性：格式化时间
const formatTime = computed(() => {
   if (!localData.value.createTime) return '';
   const date = new Date(localData.value.createTime);
   const month = date.getMonth() + 1;
   const day = date.getDate();
   return `${month}月${day}日`;
});

// 计算属性：获取动态第一张图片
const dynamicImage = computed(() => {
   if (
      !localData.value.dynamicImgarr ||
      localData.value.dynamicImgarr.trim() === ''
   ) {
      return null;
   }

   try {
      // 尝试解析JSON字符串
      const images = JSON.parse(localData.value.dynamicImgarr!);
      return Array.isArray(images) && images.length > 0 ? images[0] : null;
   } catch (e) {
      // 如果解析失败，尝试按逗号分割并取第一个
      const images = localData.value
         .dynamicImgarr!.split(',')
         .map(img => img.trim())
         .filter(img => img);
      return images.length > 0 ? images[0] : null;
   }
});

// 查看用户资料
const handleViewUser = () => {
   if (localData.value.userId) {
      uni.navigateTo({
         url: `/pages/user/profile?id=${localData.value.userId}`
      });
   }
};

// 查看动态详情
const handleViewDynamic = () => {
   router.push({
      name: 'datails',
      params: {
         dynamicId: localData.value.dynamicId
      }
   });
};

// 查看图片
const handleImageClick = () => {
   if (dynamicImage.value) {
      // 图片预览功能
      uni.previewImage({
         current: dynamicImage.value,
         urls: [dynamicImage.value]
      });
   }
};
</script>

<style lang="scss" scoped>
.love-my-comment-container {
   display: flex;
   align-items: center;
   padding: 20rpx;
   background-color: #ffffff;
   border-bottom: 1rpx solid #f0f0f0;
   transition: background-color 0.2s;

   &:active {
      background-color: #f8f8f8;
   }
}

/* 左侧区域 */
.left-section {
   display: flex;
   align-items: flex-start;
   flex: 1;
   margin-right: 20rpx;
}

/* 用户头像 */
.user-avatar {
   width: 80rpx;
   height: 80rpx;
   border-radius: 40rpx;
   margin-right: 16rpx;
   background-color: #f5f5f5;
   flex-shrink: 0;
}

/* 互动信息 */
.interaction-info {
   flex: 1;
   min-width: 0;
}

.top-row {
   margin-bottom: 8rpx;
}

.username {
   font-size: 28rpx;
   font-weight: 500;
   color: #333333;
}

.comment-text {
   font-size: 26rpx;
   color: #666666;
   line-height: 38rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
   margin-bottom: 8rpx;
}

.time {
   font-size: 24rpx;
   color: #999999;
}

/* 右侧区域 */
.right-section {
   flex-shrink: 0;
}

/* 动态图片样式 */
.dynamic-image {
   width: 120rpx;
   height: 120rpx;
   border-radius: 8rpx;
   background-color: #f0f0f0;
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .love-my-comment-container {
      padding: 16rpx;
   }

   .user-avatar {
      width: 70rpx;
      height: 70rpx;
   }

   .dynamic-image {
      width: 100rpx;
      height: 100rpx;
   }

   .comment-text {
      font-size: 24rpx;
      line-height: 34rpx;
   }
}
</style>
