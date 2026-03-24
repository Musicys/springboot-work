<template>
   <view class="focus-cart">
      <!-- 用户头像区域 -->
      <view class="avatar-section">
         <view class="avatar-container">
            <!-- 卡通风格头像 -->
            <image
               @click.stop="handleViewProfile"
               :src="localData.avatarUrl || ''"
               mode="aspectFill"
               class="user-avatar" />
            <!-- 在线状态指示器 -->
            <view v-if="localData.login === 1" class="online-indicator"></view>
         </view>
      </view>

      <!-- 用户信息区域 -->
      <view class="info-section">
         <view class="top-row">
            <!-- 用户名 -->
            <text class="username">{{ localData.username || '用户' }}</text>
            <!-- 关注提示 -->
            <text class="time" style="font-size: 22rpx">{{
               formatViewTime
            }}</text>
         </view>

         <!-- 关注日期 -->

         <!-- 个人简介 -->
         <text v-if="localData.introductory" class="introductory">{{
            localData.introductory
         }}</text>
      </view>

      <!-- 操作按钮区域 -->
      <view class="action-section">
         <button class="chat-button" @click="addFriend">聊天</button>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FocusUserVo } from '@/api/focus/model/type';
import { useRouter } from 'uni-mini-router';
import { useStore } from '@/store/user';
import { sockeStore } from '@/store/socke';
import { getTEXT } from '@/util/mssageType';
const store = useStore();
const router = useRouter();
const socke = sockeStore();

const props = defineProps<{
   data: FocusUserVo;
}>();
// 创建本地响应式数据副本，符合单向数据流
const localData = ref<FocusUserVo>({ ...props.data });

// 监听props变化，更新本地数据
watch(
   () => props.data,
   newData => {
      localData.value = { ...newData };
   },
   { deep: true }
);
// 发起聊天
const addFriend = () => {
   console.log('ces');

   const isFriend = socke.IsFriend(localData.value.userId);
   if (isFriend) {
      router.push({
         name: 'chat',
         params: { sendid: localData.value.userId }
      });
   } else {
      socke.send(
         JSON.stringify({
            id: store.userInfo.id,
            type: 4,
            sendid: localData.value.userId,
            sendteam: null,
            context: getTEXT('你好啊，我们开始聊天把-.-'),
            sendTime: new Date()
         })
      );
      setTimeout(() => {
         router.push({
            name: 'chat',
            params: { sendid: localData.value.userId }
         });
      }, 500);
   }
};

// 计算属性：格式化关注日期
// 格式化观看时间
const formatViewTime = computed(() => {
   if (!localData.value.createTime) return '未知时间';

   try {
      const viewTime = new Date(localData.value.createTime);
      const now = new Date();
      const diffInSeconds = Math.floor(
         (now.getTime() - viewTime.getTime()) / 1000
      );

      // 计算时间差
      const secondsInMinute = 60;
      const secondsInHour = secondsInMinute * 60;
      const secondsInDay = secondsInHour * 24;
      const secondsInMonth = secondsInDay * 30;

      if (diffInSeconds < secondsInMinute) {
         return '刚刚';
      } else if (diffInSeconds < secondsInHour) {
         const minutes = Math.floor(diffInSeconds / secondsInMinute);
         return `${minutes}分钟前`;
      } else if (diffInSeconds < secondsInDay) {
         const hours = Math.floor(diffInSeconds / secondsInHour);
         return `${hours}小时前`;
      } else if (diffInSeconds < secondsInMonth) {
         const days = Math.floor(diffInSeconds / secondsInDay);
         return `${days}天前`;
      } else {
         // 超过一个月显示具体日期
         return `${viewTime.getFullYear()}-${String(viewTime.getMonth() + 1).padStart(2, '0')}-${String(viewTime.getDate()).padStart(2, '0')}`;
      }
   } catch (error) {
      console.error('时间格式化错误:', error);
      return '未知时间';
   }
});

// 查看用户资料
const handleViewProfile = () => {
   // 导航到用户资料页面
   router.push({
      name: 'preinfo',
      params: { userId: localData.value.userId }
   });
};

// 发起聊天
const handleChat = () => {
   // 导航到聊天页面
   uni.navigateTo({
      url: `/pages/chat/index?userId=${localData.value.userId}`
   });
};
</script>

<style lang="scss" scoped>
.focus-cart {
   display: flex;
   align-items: center;
   padding: 20rpx;
   background-color: #ffffff;
   border-radius: 12rpx;
   margin: 10rpx 0;
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
   transition:
      transform 0.2s,
      box-shadow 0.2s;

   &:active {
      transform: scale(0.98);
      box-shadow: 0 1rpx 6rpx rgba(0, 0, 0, 0.05);
   }
}

/* 头像区域 */
.avatar-section {
   margin-right: 20rpx;
}

.avatar-container {
   position: relative;
   width: 120rpx;
   height: 120rpx;
   border-radius: 60rpx;
   overflow: hidden;
   background-color: #f5f5f5;
   display: flex;
   align-items: center;
   justify-content: center;
   /* 卡通风格背景 */
   background-image: radial-gradient(
      circle at 30% 30%,
      rgba(255, 214, 0, 0.2) 0%,
      rgba(255, 214, 0, 0) 60%
   );
}

.user-avatar {
   width: 90%;
   height: 90%;
   border-radius: 50%;
   z-index: 1;
}

.online-indicator {
   position: absolute;
   bottom: 8rpx;
   right: 8rpx;
   width: 20rpx;
   height: 20rpx;
   border-radius: 10rpx;
   background-color: #52c41a;
   border: 2rpx solid #ffffff;
   z-index: 2;
}

/* 信息区域 */
.info-section {
   flex: 1;
   min-width: 0;
}

.top-row {
   display: flex;
   align-items: center;
   margin-bottom: 8rpx;
}

.username {
   font-size: 32rpx;
   font-weight: 500;
   color: #333333;
   margin-right: 12rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.focus-tip {
   font-size: 24rpx;
   color: #ff6b81;
   background-color: #fff0f2;
   padding: 4rpx 12rpx;
   border-radius: 14rpx;
}

.focus-date {
   font-size: 24rpx;
   color: #999999;
   margin-bottom: 6rpx;
}

.introductory {
   font-size: 24rpx;
   color: #666666;
   line-height: 36rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   -webkit-line-clamp: 1;
   -webkit-box-orient: vertical;
}

/* 操作按钮区域 */
.action-section {
   margin-left: 20rpx;
}

.chat-button {
   font-size: 28rpx;
   color: #ffffff;
   background-color: #ff6b81;
   border: none;
   border-radius: 24rpx;
   padding: 0 32rpx;
   min-width: 120rpx;
   text-align: center;
   box-sizing: border-box;

   &:active {
      background-color: #ff4757;
   }
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .focus-cart {
      padding: 16rpx;
   }

   .avatar-container {
      width: 100rpx;
      height: 100rpx;
   }

   .username {
      font-size: 28rpx;
   }

   .chat-button {
      padding: 10rpx 24rpx;
      min-width: 100rpx;
      font-size: 26rpx;
   }
}
</style>
