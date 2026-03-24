<template>
   <view class="dynamic-item">
      <view class="dynamic-header">
         <text class="post-time">{{
            formatTime(messageData?.updatetime || messageData?.createTime)
         }}</text>

         <tn-bubble-box
            @click="handleBubbleClick"
            :options="bubbleOptions"
            position="bottom">
            <view
               class="box"
               style="width: 150rpx; display: flex; justify-content: flex-end">
               <tn-icon name="more-vertical" size="36" />
            </view>
         </tn-bubble-box>
      </view>
      <view class="dynamic-content" @click="handleComment">
         <rich-text :nodes="safeValue(messageData?.context, '')" />
      </view>

      <!-- 动态图片 (如果有) -->
      <view
         class="dynamic-images"
         v-if="getImages(messageData?.imgarr).length > 0">
         <tn-photo-album :data="getImages(messageData?.imgarr)" :column="3" />
      </view>

      <!-- 互动按钮 -->
      <view class="interaction-buttons">
         <view class="interaction-btn" @click="handleLike">
            <view
               :class="{
                  'liked-icon': messageData?.isLove === 1,
                  'btn-icon': true
               }">
               <tn-icon
                  :name="messageData?.isLove === 1 ? 'like-fill' : 'like'"
                  size="36"
                  bold />
            </view>
            <text
               class="btn-text"
               :class="{ 'liked-text': messageData?.isLove === 1 }"
               >{{ safeNumber(messageData?.love, 0) }} 赞</text
            >
         </view>
         <view class="interaction-btn" @click="handleComment">
            <view class="btn-icon comment-icon">
               <tn-icon name="message" size="36" />
            </view>
            <text class="btn-text"
               >{{ safeNumber(messageData?.count, 0) }} 评论</text
            >
         </view>
         <view class="interaction-btn" @click="handleShare">
            <view class="btn-icon share-icon">
               <tn-icon name="share-triangle" size="36" />
            </view>
            <text class="btn-text">分享</text>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import type { BubbleBoxOption } from '@tuniao/tnui-vue3-uniapp';
import { LoveAdd, LoveDel } from '@/api/love';
import { useRouter } from 'uni-mini-router';
const router = useRouter();
// 定义组件属性 - 保持兼容性但不再依赖父组件数据
const props = defineProps({
   item: {
      type: Object,
      default: null
   }
});
// 定义组件内部的响应式数据，基于父组件传递的数据进行初始化
const messageData = ref(
   props.item ? JSON.parse(JSON.stringify(props.item)) : {}
);

// 监听props.item变化，更新内部数据
watch(
   () => props.item,
   newItem => {
      if (newItem) {
         messageData.value = JSON.parse(JSON.stringify(newItem));
      }
   },
   { deep: true }
);

const bubbleOptions: BubbleBoxOption = [
   { text: '删除', icon: 'delete' },
   { text: '分享', icon: 'share-triangle' }
];
const handleBubbleClick = (index: number) => {
   if (index === 0) {
      // 删除操作
      console.log('删除动态');
   } else if (index === 1) {
      // 分享操作
      console.log('分享动态');
   }
};
// 格式化时间显示
const formatTime = (timeStr: string | null | undefined) => {
   if (!timeStr) return '';

   try {
      const date = new Date(timeStr);
      const now = new Date();
      const diff = now.getTime() - date.getTime();

      // 处理无效日期
      if (isNaN(date.getTime())) return '';

      // 小于1分钟
      if (diff < 60 * 1000) {
         return '刚刚';
      }
      // 小于1小时
      else if (diff < 60 * 60 * 1000) {
         return Math.floor(diff / (60 * 1000)) + '分钟前';
      }
      // 小于24小时
      else if (diff < 24 * 60 * 60 * 1000) {
         return Math.floor(diff / (60 * 60 * 1000)) + '小时前';
      }
      // 小于7天
      else if (diff < 7 * 24 * 60 * 60 * 1000) {
         return Math.floor(diff / (24 * 60 * 60 * 1000)) + '天前';
      }
      // 超过7天显示日期
      else {
         const year = date.getFullYear();
         const month = (date.getMonth() + 1).toString().padStart(2, '0');
         const day = date.getDate().toString().padStart(2, '0');

         if (year === now.getFullYear()) {
            return `${month}-${day}`;
         } else {
            return `${year}-${month}-${day}`;
         }
      }
   } catch (error) {
      console.error('Failed to format time:', error);
      return '';
   }
};

// 安全地解析图片数组
const getImages = (imgarr: string | null | undefined): string[] => {
   if (!imgarr) return [];
   try {
      const parsed = JSON.parse(imgarr || '[]');
      return Array.isArray(parsed) ? parsed : [];
   } catch (error) {
      return [];
   }
};

// 安全获取字符串值
const safeValue = (value: any, defaultValue: string = ''): string => {
   if (value === null || value === undefined) return defaultValue;
   return String(value);
};

// 安全获取数字值
const safeNumber = (value: any, defaultValue: number = 0): number => {
   const num = Number(value);
   return isNaN(num) ? defaultValue : num;
};

// 定义事件
const emit = defineEmits(['like', 'comment', 'share']);

// 处理点赞功能
const handleLike = async () => {
   // 直接操作内部数据
   if (messageData.value?.isLove) {
      //取消点赞
      let res = await LoveDel({
         commentsId: '',
         dynamicId: messageData.value?.id
      });
      if (res.code == 0) {
         messageData.value.isLove = 0;
         messageData.value.love = messageData.value.love - 1;
      }
   } else {
      //点赞
      let res = await LoveAdd({
         commentsId: '',
         dynamicId: messageData.value?.id
      });
      if (res.code == 0) {
         messageData.value.isLove = 1;
         messageData.value.love += 1;
      }
   }
};

// 处理评论功能
const handleComment = () => {
   // 模拟添加评论数
   router.push({
      name: 'datails',
      params: {
         dynamicId: messageData.value?.id
      }
   });
};

// 处理分享功能
const handleShare = () => {
   emit('share', messageData.value);
};
</script>

<style lang="scss" scoped>
.dynamic-item {
   padding: 15rpx;
   border-bottom: 1rpx solid rgba(0, 0, 0, 0.1);
   padding-bottom: 20rpx;
   margin-bottom: 30rpx;
}

.dynamic-header {
   margin-bottom: 15rpx;
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.post-time {
   font-size: 24rpx;
   color: #999;
}

.dynamic-content {
   font-size: 28rpx;
   color: #333;
   line-height: 1.6;
   margin-bottom: 20rpx;
}

.dynamic-images {
   margin-bottom: 20rpx;
}

.dynamic-image {
   width: 100%;
   height: 300rpx;
   border-radius: 15rpx;
}

.interaction-buttons {
   display: flex;
   justify-content: space-around;
   padding-top: 20rpx;
   border-top: 1rpx solid #f5f5f5;
}

.interaction-btn {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 5rpx;
}

.btn-icon {
   width: 40rpx;
   height: 40rpx;
   border-radius: 50%;
}

.share-icon {
   background-color: rgba(76, 175, 80, 0.1);
}

.btn-text {
   font-size: 24rpx;
   color: #666;
}

.liked {
   background-color: rgba(244, 67, 54, 0.3) !important;
}

.liked-text {
   color: #f44336;
}
.liked-icon {
   color: #f44336;
}
</style>
