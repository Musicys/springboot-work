<template>
   <view class="befocused-cart-container">
      <!-- 左侧：用户信息区域 -->
      <view class="user-info-section">
         <!-- 用户头像 -->
         <image
            :src="localData.avatarUrl || ''"
            mode="aspectFill"
            class="user-avatar"
            @click.stop="handleViewProfile" />

         <!-- 用户详细信息 -->
         <view class="user-details">
            <view class="top-info">
               <!-- 用户名和性别标识 -->
               <view class="name-gender">
                  <text class="username">{{
                     localData.username || '用户'
                  }}</text>
                  <text :class="['gender-icon', genderClass]">{{
                     genderIcon
                  }}</text>
                  <!-- 年龄 -->
                  <text class="age">{{ localData.age || '' }}岁</text>
               </view>

               <!-- 创建时间 -->
               <text class="age" style="font-size: 18rpx">{{
                  formatViewTime || ''
               }}</text>
            </view>

            <!-- 个人简介 -->
            <text class="introductory">{{ localData.introductory || '' }}</text>

            <!-- 标签 -->
            <view class="tags-container" v-if="parsedTags.length > 0">
               <text
                  v-for="(tag, index) in parsedTags"
                  :key="index"
                  class="tag"
                  @click.stop="handleTagClick(tag)">
                  #{{ tag }}
               </text>
            </view>

            <!-- 在线状态 -->
            <text class="online-status" v-if="localData.login === 1">在线</text>
         </view>
      </view>

      <!-- 右侧：聊天按钮 -->
      <view class="action-section">
         <button class="chat-button" @click="addFriend">
            <text class="chat-button-text">聊天</text>
         </button>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FocusUserVo } from '@/api/focus/model/type';
import { useRouter } from 'uni-mini-router';
import { useStore } from '@/store/user';
import { sockeStore } from '@/store/socke';
const store = useStore();
const router = useRouter();
const socke = sockeStore();

const props = defineProps({
   data: {
      type: Object as () => FocusUserVo,
      default: () => ({})
   }
});

// 创建本地响应式数据副本，符合单向数据流
const localData = ref<FocusUserVo>({ ...props.data });
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

// 监听props变化，更新本地数据
watch(
   () => props.data,
   newData => {
      localData.value = { ...newData };
   },
   { deep: true }
);

// 计算属性：性别样式类
const genderClass = computed(() => {
   return localData.value.gender === 1
      ? 'male'
      : localData.value.gender === 0
        ? 'female'
        : '';
});

// 计算属性：性别图标
const genderIcon = computed(() => {
   return localData.value.gender === 1
      ? '♂'
      : localData.value.gender === 0
        ? '♀'
        : '';
});

// 计算属性：解析标签数组
const parsedTags = computed(() => {
   if (!localData.value.tags || localData.value.tags.trim() === '') {
      return [];
   }

   try {
      // 尝试解析JSON字符串
      const tags = JSON.parse(localData.value.tags);
      return Array.isArray(tags) ? tags : [];
   } catch (e) {
      // 如果解析失败，返回空数组
      return [];
   }
});

// 查看用户资料
const handleViewProfile = () => {
   router.push({
      name: 'preinfo',
      params: { userId: localData.value.userId }
   });
};

// 发起聊天
const addFriend = () => {
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

// 点击标签
const handleTagClick = (tag: string) => {
   // 可以根据标签进行搜索或其他操作
   console.log('Tag clicked:', tag);
};
</script>

<style lang="scss" scoped>
.befocused-cart-container {
   display: flex;
   align-items: center;
   padding: 24rpx;
   background-color: #ffffff;
   border-bottom: 1rpx solid #f0f0f0;
   transition: background-color 0.2s;

   &:active {
      background-color: #f8f8f8;
   }
}

/* 用户信息区域 */
.user-info-section {
   display: flex;
   align-items: flex-start;
   flex: 1;
}

/* 用户头像 */
.user-avatar {
   width: 100rpx;
   height: 100rpx;
   border-radius: 50rpx;
   margin-right: 20rpx;
   background-color: #f5f5f5;
   flex-shrink: 0;
}

/* 用户详细信息 */
.user-details {
   flex: 1;
   min-width: 0;
}

.top-info {
   display: flex;
   align-items: center;
   justify-content: space-between;
   margin-bottom: 12rpx;
}

.name-gender {
   display: flex;
   align-items: center;
}

.username {
   font-size: 28rpx;
   font-weight: 500;
   color: #333333;
   margin-right: 8rpx;
}

.gender-icon {
   font-size: 20rpx;
   padding: 2rpx 8rpx;
   border-radius: 12rpx;
   font-weight: 500;
}

.gender-icon.male {
   color: #4e8af7;
   background-color: #e6f0ff;
}

.gender-icon.female {
   color: #ff6b81;
   background-color: #fff0f0;
}

.age {
   font-size: 22rpx;
   color: #666666;
}

.introductory {
   font-size: 26rpx;
   color: #666666;
   line-height: 38rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
   margin-bottom: 12rpx;
}

/* 标签容器 */
.tags-container {
   display: flex;
   flex-wrap: wrap;
   margin-bottom: 8rpx;
}

.tag {
   font-size: 22rpx;
   color: #999999;
   background-color: #f5f5f5;
   padding: 4rpx 16rpx;
   border-radius: 16rpx;
   margin-right: 12rpx;
   margin-bottom: 8rpx;
}

.online-status {
   font-size: 22rpx;
   color: #52c41a;
   background-color: #f0f9eb;
   padding: 4rpx 12rpx;
   border-radius: 12rpx;
}

/* 操作区域 */
.action-section {
   flex-shrink: 0;
   margin-left: 20rpx;
}

/* 聊天按钮 */
.chat-button {
   width: 120rpx;
   height: 68rpx;
   line-height: 68rpx;
   text-align: center;
   background-color: #ff6b81;
   color: #ffffff;
   border-radius: 34rpx;
   font-size: 24rpx;
   border: none;
   padding: 0;
   box-shadow: 0 4rpx 12rpx rgba(255, 107, 129, 0.3);
   transition: all 0.2s;

   &:active {
      transform: scale(0.95);
      opacity: 0.9;
   }
}

.chat-button-text {
   color: #ffffff;
   font-weight: 500;
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .befocused-cart-container {
      padding: 20rpx;
   }

   .user-avatar {
      width: 90rpx;
      height: 90rpx;
      border-radius: 45rpx;
   }

   .username {
      font-size: 26rpx;
   }

   .introductory {
      font-size: 20rpx;
      line-height: 36rpx;
   }

   .chat-button {
      width: 110rpx;
      height: 62rpx;
      line-height: 62rpx;
      font-size: 22rpx;
   }
}
</style>
