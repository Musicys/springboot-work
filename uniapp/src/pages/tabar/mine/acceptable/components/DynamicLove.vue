<template>
   <view
      class="dynamic-love-container"
      @click="
         router.push({
            name: 'datails',
            params: {
               dynamicId: localData.id
            }
         })
      ">
      <!-- 左侧：用户信息和互动行为区域 -->
      <view class="left-section">
         <!-- 用户头像 -->
         <image
            :src="localData.avatarUrl || ''"
            mode="aspectFill"
            class="user-avatar" />

         <!-- 互动信息 -->
         <view class="interaction-info">
            <view class="top-row">
               <!-- 用户名 -->
               <text class="username">{{ localData.username || '用户' }}</text>
               <!-- 互动行为 -->
               <text class="action-text">赞了你的动态</text>
            </view>
            <!-- 时间 -->
            <text class="time">{{ formatTime }}</text>
         </view>
      </view>

      <!-- 右侧：动态内容区域 -->
      <view class="right-section" @click.stop="handleViewDynamic">
         <!-- 只显示第一张图片（如果有） -->
         <image
            v-if="firstImage"
            :src="firstImage"
            mode="aspectFill"
            class="main-image"
            @click.stop="handleImageClick" />

         <!-- 没有图片时显示文案 -->
         <view v-else class="content-container">
            <text class="content-text">{{
               localData.content?.substring(0, 5) || ''
            }}</text>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { DynamicLoveVo } from '@/api/love/model/type';
import { useRouter } from 'uni-mini-router';
const router = useRouter();
const props = defineProps<{
   data: DynamicLoveVo;
}>();

// 创建本地响应式数据副本，符合单向数据流
const localData = ref<DynamicLoveVo>({ ...props.data });

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

// 计算属性：获取第一张图片
const firstImage = computed(() => {
   if (!localData.value.imgarr || localData.value.imgarr.trim() === '') {
      return null;
   }

   try {
      // 尝试解析JSON字符串
      const images = JSON.parse(localData.value.imgarr!);
      return Array.isArray(images) && images.length > 0 ? images[0] : null;
   } catch (e) {
      // 如果解析失败，尝试按逗号分割并取第一个
      const images = localData.value
         .imgarr!.split(',')
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
   if (localData.value.dynamicId) {
      uni.navigateTo({
         url: `/pages/dynamic/detail?id=${localData.value.dynamicId}`
      });
   }
};

// 发起聊天
const handleChat = () => {
   if (localData.value.userId) {
      uni.navigateTo({
         url: `/pages/chat/index?userId=${localData.value.userId}`
      });
   }
};

// 查看图片
const handleImageClick = () => {
   if (firstImage.value) {
      // 图片预览功能
      uni.previewImage({
         current: firstImage.value,
         urls: [firstImage.value]
      });
   }
};
</script>

<style lang="scss" scoped>
.dynamic-love-container {
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
   align-items: center;
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
}

/* 互动信息 */
.interaction-info {
   flex: 1;
   min-width: 0;
}

.top-row {
   display: flex;
   align-items: center;
   margin-bottom: 8rpx;
}

.username {
   font-size: 28rpx;
   font-weight: 500;
   color: #333333;
   margin-right: 12rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.action-text {
   font-size: 26rpx;
   color: #666666;
}

.time {
   font-size: 24rpx;
   color: #999999;
}

/* 右侧区域 */
.right-section {
   flex-shrink: 0;
}

/* 文案内容 */
.content-text {
   max-width: 150rpx;
   font-size: 24rpx;
   color: #666666;
   line-height: 36rpx;
   overflow: hidden;
   text-overflow: ellipsis;
}

/* 主图片样式 */
.main-image {
   width: 120rpx;
   height: 120rpx;
   border-radius: 8rpx;
   background-color: #f0f0f0;
}

/* 文案容器 */
.content-container {
   display: flex;
   align-items: center;
   height: 120rpx;
   padding: 0 16rpx;
   background-color: #f7f7f7;
   border-radius: 8rpx;
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .dynamic-love-container {
      padding: 16rpx;
   }

   .user-avatar {
      width: 70rpx;
      height: 70rpx;
   }

   .main-image {
      width: 100rpx;
      height: 100rpx;
   }

   .content-container {
      height: 100rpx;
   }
}
</style>
