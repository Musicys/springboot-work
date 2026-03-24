<template>
   <view class="find-container">
      <!-- 好友列表 -->
      <view class="friend-list" v-if="friendList.length > 0">
         <view
            class="friend-item"
            v-for="friend in friendList"
            :key="friend.id"
            @click="handleFriendClick(friend)">
            <!-- 头像部分 -->
            <view class="avatar-container">
               <image
                  :src="
                     friend.avatarUrl ||
                     `https://via.placeholder.com/100?text=${friend.username?.charAt(0)}`
                  "
                  class="avatar"
                  mode="aspectFill" />
               <view
                  v-if="friend.login"
                  class="online-indicator"
                  :class="{ online: true }"></view>
            </view>

            <!-- 信息部分 -->
            <view class="friend-info">
               <!-- 第一行：名称、在线状态和时间 -->
               <view class="friend-info-header">
                  <view class="friend-name-container">
                     <text class="friend-name">{{ friend.username }}</text>
                     <text class="online-status" v-if="friend.login === 1"
                        >在线</text
                     >
                  </view>
                  <text class="message-time">{{
                     formatMessageTime(friend.latestMessage?.createtime)
                  }}</text>
               </view>

               <!-- 第二行：最新消息和未读数量 -->
               <view class="friend-info-footer">
                  <text class="latest-message">{{
                     getLatestMessageContent(friend)
                  }}</text>
                  <view class="unread-count" v-if="getUnreadCount(friend) > 0">
                     {{ getUnreadCount(friend) }}
                  </view>
               </view>
            </view>
         </view>
      </view>
      <no-list
         v-else
         text="暂无好友"
         subtext="快去添加新好友吧~"
         buttonText="去寻找好友"
         @go="router.push({ name: 'home' })"></no-list>
      <!-- 空列表状态 -->
   </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import { User } from '@/api/user/model/type';
import { useRouter } from 'uni-mini-router';
import { formatMessageTime } from '@/util';
import { GetMessageContent } from '@/util/mssageType';
import NoList from './NoList.vue';
const router = useRouter();
const user = useStore();
const socke = sockeStore();

// 定义Friend类型
interface Friend extends User {
   latestMessage?: any;
   sendList?: any[];
}

// 计算好友列表（直接监听socke的UserList）
const friendList = computed<Friend[]>(() => {
   if (!socke.UserList || socke.UserList.length === 0) {
      return [];
   }

   return socke.UserList.map(friend => ({
      ...friend,
      // 计算最新消息
      latestMessage:
         friend.sendList && friend.sendList.length > 0
            ? friend.sendList[friend.sendList.length - 1]
            : undefined
   }));
});

// 获取最新消息内容
const getLatestMessageContent = (friend: Friend): string => {
   if (!friend.sendList || friend.sendList.length === 0) {
      return '暂无消息';
   }

   const latestMessage = friend.sendList[friend.sendList.length - 1];
   return GetMessageContent(latestMessage.context) || '';
   // return '123';
};

// 获取未读消息数量
const getUnreadCount = (friend: Friend): number => {
   if (!friend.sendList || friend.sendList.length === 0) {
      return 0;
   }

   // 假设yeslook为'0'表示未读
   return friend.sendList.filter(
      msg => msg.yeslook === 0 && msg.sendid == user.userInfo.id
   ).length;
};

// 处理好友点击事件
const handleFriendClick = (friend: Friend) => {
   router.push({ name: 'chat', params: { sendid: friend.id } });
};

// 组件挂载时
onShow(() => {
   // 这里可以添加刷新逻辑
});

// 处理快捷入口点击
const handleShortcutClick = (type: string) => {
   console.log(`点击了快捷入口: ${type}`);
   // 这里可以添加对应的导航逻辑
};

// 处理标签切换
const handleTabChange = (tab: string) => {
   console.log(`切换到标签: ${tab}`);
   // 这里可以添加标签切换逻辑
};

// 处理功能模块点击
const handleFeatureClick = (feature: string) => {
   console.log(`点击了功能模块: ${feature}`);
   // 这里可以添加对应的功能逻辑
};
</script>

<style lang="scss" scoped>
// 全局容器样式
.find-container {
   width: 100%;
   min-height: 100vh;

   padding-bottom: 20rpx;
}

// 顶部快捷入口样式
.top-shortcuts {
   display: flex;
   justify-content: space-around;
   padding: 20rpx 0;
   background-color: #fff;
   border-bottom: 1rpx solid #f0f0f0;
   margin-bottom: 10rpx;
}

.shortcut-item {
   padding: 10rpx 20rpx;
   border-radius: 20rpx;
   transition: all 0.2s;
   &.active {
      background-color: #e6f7ff;
      .shortcut-text {
         color: #1890ff;
         font-weight: 600;
      }
   }
   &:active {
      opacity: 0.8;
   }
}

.shortcut-text {
   font-size: 28rpx;
   color: #666;
}

// 中间标签栏样式
.tab-bar {
   display: flex;
   background-color: #fff;
   border-radius: 12rpx;
   margin: 0 20rpx 20rpx;
   overflow: hidden;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.tab-item {
   flex: 1;
   text-align: center;
   padding: 20rpx 0;
   position: relative;
   transition: all 0.2s;
   &.active {
      color: #1890ff;
      .tab-text {
         color: #1890ff;
         font-weight: 600;
      }
      &::after {
         content: '';
         position: absolute;
         bottom: 0;
         left: 25%;
         width: 50%;
         height: 4rpx;
         background-color: #1890ff;
         border-radius: 2rpx;
      }
   }
   &:active {
      background-color: #f5f5f5;
   }
}

.tab-text {
   font-size: 30rpx;
   color: #666;
   transition: all 0.2s;
}

.view-icon {
   background-color: #fff2e8;
}

.like-icon {
   background-color: #fff1f0;
}

.recommend-icon {
   background-color: #f6ffed;
}

.icon-text {
   font-size: 36rpx;
}

.feature-text {
   font-size: 26rpx;
   color: #666;
   margin-bottom: 5rpx;
}

.feature-count {
   font-size: 24rpx;
   color: #1890ff;
   background-color: #e6f7ff;
   padding: 2rpx 10rpx;
   border-radius: 10rpx;
}

// 区域标题样式
.section-header {
   padding: 20rpx;
   margin-bottom: 10rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: 600;
   color: #333;
}

// 好友列表样式
.friend-list {
   padding: 0 20rpx;
}

.friend-item {
   display: flex;
   align-items: center;
   padding: 12px 5rpx;
   transition: all 0.2s;

   &:active {
      transform: scale(0.98);
      box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.1);
   }
}

.avatar-container {
   position: relative;
   margin-right: 10rpx;
}

.avatar {
   width: 95rpx;
   height: 95rpx;
   border-radius: 50%;
   background-color: #f0f0f0;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.online-indicator {
   position: absolute;
   bottom: 6rpx;
   right: 6rpx;
   width: 20rpx;
   height: 20rpx;
   border-radius: 50%;
   border: 4rpx solid #fff;
   background-color: #e0e0e0;
   box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);

   &.online {
      background-color: #52c41a;
      animation: pulse 2s infinite;
   }
}

@keyframes pulse {
   0% {
      box-shadow: 0 0 0 0 rgba(82, 196, 26, 0.4);
   }
   70% {
      box-shadow: 0 0 0 15rpx rgba(82, 196, 26, 0);
   }
   100% {
      box-shadow: 0 0 0 0 rgba(82, 196, 26, 0);
   }
}

.friend-info {
   flex: 1;
   min-width: 0;
   margin-left: 15rpx;
}

.friend-info-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 10rpx;
}

.friend-name-container {
   display: flex;
   align-items: center;
}

.friend-name {
   font-size: 26rpx;
   font-weight: 600;
   color: #333;
   margin-right: 12rpx;
}

.online-status {
   font-size: 20rpx;
   color: #52c41a;
   background-color: rgba(82, 196, 26, 0.1);
   padding: 4rpx 12rpx;
   border-radius: 12rpx;
}

.message-time {
   font-size: 22rpx;
   color: #999;
}

.friend-info-footer {
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.latest-message {
   font-size: 22rpx;
   color: #666;
   flex: 1;
   min-width: 0;
   max-width: 80%;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.unread-count {
   min-width: 40rpx;
   height: 40rpx;
   line-height: 40rpx;
   text-align: center;
   font-size: 24rpx;
   color: #fff;
   background-color: #ff4d4f;
   border-radius: 20rpx;
   padding: 0 14rpx;
   margin-left: 12rpx;
   box-shadow: 0 2rpx 8rpx rgba(255, 77, 79, 0.3);
}

// 空列表样式
</style>
