<template>
   <view class="viewed-cart" @click.stop="handleViewProfile">
      <!-- 头像区域 -->
      <view class="avatar-container">
         <view class="avatar-wrapper">
            <!-- 黄色圆形背景 - 卡通风格 -->
            <view class="avatar-background"></view>
            <image
               class="avatar"
               :src="localData.avatarUrl || '/static/imgs/default-avatar.png'"
               mode="aspectFill"
               alt="用户头像"></image>
            <!-- 在线状态指示器 -->
            <view v-if="localData.login === 0" class="online-indicator"></view>
            <!-- 封禁标识 -->
            <view v-if="localData.userStatus === 1" class="user-banned"
               >已封禁</view
            >
         </view>
         <!-- 性别标识 -->
      </view>

      <!-- 用户信息区域 -->
      <view class="info-container">
         <view class="top-row">
            <view
               v-if="localData.gender !== undefined"
               class="gender-badge"
               :class="genderClass">
               <tn-icon
                  :name="localData.gender === 1 ? 'sex-male' : 'sex-female'"
                  size="24"
                  :color="localData.gender === 1 ? '#4caf50' : '#f44336'"
                  bold />
            </view>
            <!-- 用户名和年龄 -->
            <text class="username">{{ localData.username || '未知用户' }}</text>
            <text v-if="localData.age" class="age">{{ localData.age }}岁</text>
         </view>

         <!-- 互动信息 -->
         <view class="interaction-info">
            <text class="view-time">{{ formatViewTime }}</text>
            <text class="view-text">看过我的资料</text>
         </view>

         <!-- 观看次数 -->
         <view class="view-count">
            <text class="count-text">{{ localData.count || 0 }}次</text>
         </view>
      </view>

      <!-- 操作按钮区域 -->
      <view class="action-container">
         <wd-button
            size="small"
            :type="localData.isFocus === 1 ? 'default' : 'primary'"
            :plain="localData.isFocus === 1"
            @click.stop="handleFollow">
            {{ localData.isFocus === 1 ? '已关注' : '关注' }}
         </wd-button>
      </view>
   </view>
</template>

<script setup lang="ts">
import { LookUser } from '@/api/look/model/type';
import { computed, ref, watch } from 'vue';
import { FocusAdd, FocusDel } from '@/api/focus';

// 定义组件属性
const props = defineProps<{
   data: LookUser;
}>();

// 定义组件事件
const emit = defineEmits<{
   'view-profile': [userId: number];
}>();

// 创建本地响应式数据副本
const localData = ref<LookUser>({ ...props.data });

// 监听props变化，更新本地数据
watch(
   () => props.data,
   newData => {
      localData.value = { ...newData };
   },
   { deep: true }
);

// 计算性别相关样式
const genderClass = computed(() => {
   return localData.value.gender === 0 ? 'female' : 'male';
});

// 格式化观看时间
const formatViewTime = computed(() => {
   if (!localData.value.createtime) return '未知时间';

   try {
      const viewTime = new Date(localData.value.createtime);
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

// 处理关注/取消关注
const handleFollow = async () => {
   try {
      // 禁用按钮防止重复点击
      const isFocused = localData.value.isFocus === 1;

      // 调用对应的API
      const result = isFocused
         ? await FocusDel({ focusUserId: localData.value.id })
         : await FocusAdd({ focusUserId: localData.value.id });

      if (result.code === 0) {
         // 只更新本地状态，不尝试修改父组件数据
         localData.value = {
            ...localData.value,
            isFocus: isFocused ? 0 : 1
         };

         // 可以选择发送一个自定义事件，但不包含更新的数据
         // emit('focus-change', { userId: localData.value.id, isFocused: !isFocused });
      } else {
         uni.showToast({
            title: result.msg || (isFocused ? '取消关注失败' : '关注失败'),
            icon: 'none'
         });
      }
   } catch (error) {
      console.error('关注操作失败:', error);
      uni.showToast({ title: '网络错误', icon: 'none' });
   }
};

// 处理聊天
const handleChat = () => {
   // 跳转到聊天页面
   uni.navigateTo({
      url: `/pages/chat?userId=${localData.value.id}&username=${encodeURIComponent(localData.value.username || '未知用户')}&avatarUrl=${encodeURIComponent(localData.value.avatarUrl || '')}`
   });
};

// 处理点击查看用户资料
const handleViewProfile = () => {
   emit('view-profile', localData.value.id);
};
</script>

<style lang="scss" scoped>
.viewed-cart {
   display: flex;
   align-items: center;
   padding: 20rpx;
   background-color: #ffffff;
   border-radius: 12rpx;
   margin-bottom: 16rpx;
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
   transition:
      transform 0.2s,
      box-shadow 0.2s;

   &:active {
      transform: scale(0.98);
   }
}

/* 头像区域 */
.avatar-container {
   position: relative;
   margin-right: 20rpx;

   .avatar-wrapper {
      position: relative;
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: center;

      .avatar-background {
         position: absolute;
         width: 100%;
         height: 100%;
         background-color: #fff2cc;
         border: 2rpx solid #ffe699;
      }

      .avatar {
         position: relative;
         width: 90%;
         height: 90%;
         object-fit: cover;
         border-radius: 50%;
         z-index: 1;
      }

      .online-indicator {
         position: absolute;
         z-index: 999;
         bottom: 16rpx;
         right: 6rpx;
         width: 24rpx;
         height: 24rpx;
         background-color: #4caf50;
         border: 3rpx solid #ffffff;
         border-radius: 50%;
         box-shadow: 0 0 0 2rpx rgba(76, 175, 80, 0.3);
      }
   }

   .gender-badge {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 3rpx solid #ffffff;

      &.female {
         background-color: #ff85a2;

         .gender-icon {
            color: #ffffff;
            width: 24rpx;
            height: 24rpx;
         }
      }

      &.male {
         background-color: #85c1ff;

         .gender-icon {
            color: #ffffff;
            width: 24rpx;
            height: 24rpx;
         }
      }
   }
}

/* 信息区域 */
.info-container {
   flex: 1;
   display: flex;
   flex-direction: column;
   justify-content: center;

   .top-row {
      display: flex;
      align-items: center;
      margin-bottom: 8rpx;

      .username {
         font-size: 26rpx;
         font-weight: 600;
         margin: 0 12rpx;
         color: #333333;
      }

      .age {
         font-size: 24rpx;
         color: #666666;
      }
   }

   .interaction-info {
      display: flex;
      align-items: center;
      margin-bottom: 8rpx;

      .view-time {
         font-size: 22rpx;
         color: #999999;
         margin-right: 8rpx;
      }

      .view-text {
         font-size: 22rpx;
         color: #666666;
      }
   }

   .view-count {
      .count-text {
         font-size: 24rpx;
         color: #ff6b6b;
         font-weight: 500;
      }
   }
}

/* 操作按钮区域 */
.action-container {
   display: flex;
   align-items: center;
   gap: 16rpx;

   .chat-button {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background-color: #f0f0f0;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;

      &:active {
         background-color: #e0e0e0;
         transform: scale(0.95);
      }
   }
}

/* 封禁用户样式 */
.viewed-cart {
   &:has(.user-banned) {
      opacity: 0.6;

      .action-container {
         opacity: 0.4;
         pointer-events: none;
      }
   }

   .user-banned {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 20rpx;
      color: #ff6b6b;
      background-color: rgba(255, 255, 255, 0.9);
      padding: 4rpx 12rpx;
      border-radius: 12rpx;
      z-index: 2;
   }
}

/* 响应式调整 */
@media (max-width: 375px) {
   .viewed-cart {
      padding: 16rpx;

      .avatar-wrapper {
         width: 100rpx;
         height: 100rpx;

         .avatar {
            width: 90%;
            height: 90%;
         }
      }

      .username {
         font-size: 26rpx;
      }

      .age,
      .view-time,
      .view-text,
      .count-text {
         font-size: 20rpx;
      }
   }
}
</style>
