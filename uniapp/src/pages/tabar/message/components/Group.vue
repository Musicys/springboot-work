<template>
   <view class="group-message-container">
      <!-- 群消息列表 -->

      <view class="group-list" v-if="data.length > 0">
         <!-- 群消息卡片 -->
         <view
            class="group-item"
            v-for="group in data"
            :key="group.id"
            @click="goToChat(group.id)">
            <!-- 左侧组合头像区域 -->
            <view class="avatar-section">
               <view class="combined-avatar">
                  <!-- 显示最多4个头像组合 -->
                  <image :src="group.url" class="avatar-image"></image>
                  <!-- 未读消息角标 -->
               </view>
            </view>

            <!-- 右侧信息区域 -->
            <view class="message-content">
               <!-- 顶部：群名和时间 -->
               <view class="message-header">
                  <view class="group-info">
                     <text class="group-name">{{ group.chatName }}</text>
                     <text class="group-count"> {{ group.count }}人</text>
                  </view>
                  <text class="message-time">{{
                     formatMessageTime(
                        getLastMessage(group.sendList)?.createtime
                     )
                  }}</text>
               </view>

               <!-- 底部：最新消息 -->
               <view
                  class="box"
                  style="
                     display: flex;
                     align-items: center;
                     justify-content: space-between;
                     width: 100%;
                  ">
                  <view class="message-footer">
                     <text class="sender-name"
                        >{{ getLastMessage(group.sendList)?.username }}:</text
                     >
                     <text class="message-text">{{
                        getLastMessage(group.sendList)?.context
                     }}</text>
                  </view>
                  <view v-if="group.lookCount > 0" class="unread-badge">
                     {{ group.lookCount > 99 ? '99+' : group.lookCount }}
                  </view>
               </view>
            </view>
         </view>
      </view>
      <no-list
         v-else
         text="暂无群聊"
         subtext="快去添加新群聊吧~"
         buttonText="去寻找群聊"
         @go="router.push({ name: 'home' })"></no-list>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import { useRouter } from 'uni-mini-router';
import { formatMessageTime } from '@/util';
import { GetMessageContent } from '@/util/mssageType';
import NoList from './NoList.vue';
const router = useRouter();
const user = useStore();
const soke = sockeStore();

const data = ref([...soke.ChatList]);
watch(
   () => soke.ChatList,
   () => {
      data.value = [...soke.ChatList];
   },
   {
      deep: true
   }
);

// 获取最后一条消息
const getLastMessage = (sendList: any[]) => {
   if (!sendList || sendList.length === 0) return null;

   let send = { ...sendList[sendList.length - 1] };
   send.context = GetMessageContent(send.context);

   return send;
};

// 获取前4个成员用于组合头像
const getFirstFourMembers = (sendList: any[]) => {
   if (!sendList || sendList.length === 0) return [];
   // 去重获取前4个不同的用户头像
   const uniqueUsers = Array.from(
      new Map(sendList.map(item => [item.userid, item])).values()
   );
   return uniqueUsers.slice(0, 4);
};

// 跳转到聊天页面
const goToChat = (groupId: number) => {
   router.push({
      name: 'groupchat',
      params: {
         groupId
      }
   });
};

onMounted(() => {});
</script>

<style lang="scss" scoped>
.group-message-container {
   width: 100%;
   min-height: 100vh;

   padding-bottom: 20rpx;
}

.group-list {
   padding: 0 20rpx;
}

.group-item {
   display: flex;
   padding: 12px 5rpx;
   margin-bottom: 10rpx;
   border-radius: 12rpx;
   &:active {
      background-color: #f5f7fa;
   }
}

// 头像区域样式
.avatar-section {
   margin-right: 20rpx;
}

.combined-avatar {
   position: relative;
   border-radius: 16rpx;
   background-color: #f0f0f0;
}

.avatar-grid {
   display: grid;
   grid-template-columns: repeat(2, 1fr);
   grid-template-rows: repeat(2, 1fr);
   width: 100%;
   height: 100%;
   gap: 2rpx;
   background-color: #fff;
}

.small-avatar {
   background-size: cover;
   background-position: center;
   background-repeat: no-repeat;
}

.unread-badge {
   width: 30rpx;
   height: 30rpx;
   background-color: #ff4757;
   color: #fff;
   font-size: 24rpx;
   font-weight: 600;
   padding: 15rpx 25rpx;
   border-radius: 20rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   border: 2rpx solid #fff;
}

// 消息内容区域样式
.message-content {
   flex: 1;
   display: flex;
   flex-direction: column;
   justify-content: center;
   margin-right: 20rpx;
   overflow: hidden;
}

.message-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 10rpx;
}

.group-info {
   display: flex;
   align-items: center;
   flex: 1;
}

.group-name {
   font-size: 26rpx;
   font-weight: 600;
   color: #333;
   margin-right: 8rpx;
   max-width: 200rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.group-count {
   font-size: 22rpx;
   color: #999;
   margin-right: 12rpx;
}

.group-tag {
   background-color: #f0f7ff;
   padding: 2rpx 12rpx;
   border-radius: 12rpx;
}

.tag-text {
   font-size: 22rpx;
   color: #1890ff;
}

.message-time {
   font-size: 22rpx;
   color: #999;
}

.message-footer {
   font-size: 24rpx;
   display: flex;
   align-items: center;
   width: 90%;
}

.sender-name {
   color: #666;
   margin-right: 8rpx;
}

.message-text {
   color: #999;
   flex: 1;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}
.avatar-image {
   width: 95rpx;
   height: 95rpx;
   border-radius: 50%;
   background-color: #f0f0f0;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}
</style>
