<template>
   <view class="chat-cart-container">
      <!-- 群聊头像 -->

      <view class="chat-avatar" @click.stop="handleViewGroupChat">
         <wd-img :src="localData.url || ''" radius="50">
            <template #error>
               <wd-skeleton theme="avatar" />
            </template>
            <template #loading>
               <wd-skeleton theme="avatar" />
            </template>
         </wd-img>
      </view>

      <!-- 群聊信息 -->
      <view class="chat-info">
         <!-- 群聊名称和标签 -->
         <view class="top-section">
            <text class="chat-name">{{ localData.chatName || '群聊' }}</text>
         </view>

         <!-- 群聊简介 -->
         <text class="introductory">{{
            localData.introductory || '还没有关于此群的介绍哦'
         }}</text>

         <!-- 群聊人数 -->
         <text class="count-text">群成员: {{ localData.count || 0 }}人</text>
      </view>

      <!-- 操作按钮 -->
      <view class="action-section" @click.stop="handleJoinGroup">
         <image
            v-if="localData.isAdd === 1"
            style="width: 30rpx; height: 30rpx"
            src="/src/static/home/dogyellow.png" />
         <image
            v-else
            style="width: 30rpx; height: 30rpx"
            src="/src/static/home/hqs.png" />

         <text>{{ localData.isAdd === 0 ? '速圈子' : '传送门' }}</text>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import router from '@/router';
const { userInfo } = useStore();
const { send } = sockeStore();
interface GroupChatData {
   id: number;
   userId: number;
   chatName: string;
   url: string;
   tagName: string;
   introductory: string;
   count: number;
   isAdd: number;
}

const props = defineProps<{
   data: GroupChatData;
}>();

// 创建本地响应式数据副本，符合单向数据流
const localData = ref<GroupChatData>({ ...props.data });

// 监听props变化，更新本地数据
watch(
   () => props.data,
   newData => {
      localData.value = { ...newData };
   },
   { deep: true }
);

// 计算属性：解析标签数组
const parsedTags = computed(() => {
   if (!localData.value.tagName || localData.value.tagName.trim() === '') {
      return [];
   }

   try {
      // 尝试解析JSON字符串
      const tags = JSON.parse(localData.value.tagName);
      return Array.isArray(tags) ? tags : [];
   } catch (e) {
      // 如果解析失败，返回空数组
      return [];
   }
});

// 查看群聊详情
const handleViewGroupChat = () => {
   setTimeout(() => {
      router.push({
         name: 'groupchat',
         params: { groupId: localData.value.id }
      });
   }, 1000);
};

// 加入群聊
const handleJoinGroup = () => {
   if (localData.value.IsAdd == 1) {
      router.push({
         name: 'groupchat',
         params: { groupId: localData.value.id }
      });
   } else {
      send(
         JSON.stringify({
            id: userInfo.id,
            type: 6,
            sendid: null,
            sendteam: localData.value.id,
            context: '',
            sendTime: new Date()
         })
      );
      localData.value.IsAdd = 1;
      setTimeout(() => {
         router.push({
            name: 'groupchat',
            params: { groupId: localData.value.id }
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
.chat-cart-container {
   position: relative;
   display: flex;
   align-items: center;
   padding: 24rpx;

   border-bottom: 1rpx solid #f0f0f0;
   transition: background-color 0.2s;

   &:active {
      background-color: #f8f8f8;
   }
}

/* 群聊头像 */
.chat-avatar {
   width: 120rpx;
   height: 120rpx;
   border-radius: 20rpx;
   margin-right: 20rpx;
   background-color: #f5f5f5;
   flex-shrink: 0;
   border-radius: 50%;
}

/* 群聊信息 */
.chat-info {
   flex: 1;
   min-width: 0;
   margin-right: 20rpx;
   display: flex;
   flex-direction: column;
   position: relative;
   justify-content: space-between;
   height: 120rpx;
}

/* 顶部区域 */
.top-section {
   display: flex;
   align-items: center;

   flex-wrap: wrap;
}

/* 群聊名称 */
.chat-name {
   font-size: 20rpx;
   font-weight: 500;
   color: #333333;
   margin-right: 12rpx;
   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
}

/* 标签容器 */
.tags-container {
   display: flex;
   flex-wrap: wrap;
}

/* 群聊简介 */
.introductory {
   font-size: 20rpx;
   color: #666666;
   line-height: 38rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   -webkit-line-clamp: 1;
   -webkit-box-orient: vertical;
}

/* 群聊人数 */
.count-text {
   font-size: 18rpx;
   color: #999999;
}

/* 操作区域 */
.action-section {
   display: flex;
   justify-content: center;
   position: absolute;
   top: 30rpx;
   line-height: 30rpx;
   right: 20rpx;
   z-index: 99;
   padding: 10rpx 16rpx;
   background: pink;
   font-size: 16rpx;
   border-radius: 15rpx;
}

/* 加入按钮 */
.join-button {
   width: 120rpx;
   height: 68rpx;
   line-height: 68rpx;
   text-align: center;
   background-color: #ff6b81;
   color: #ffffff;
   border-radius: 34rpx;
   font-size: 22rpx;
   border: none;
   padding: 0;
   box-shadow: 0 4rpx 12rpx rgba(255, 107, 129, 0.3);
   transition: all 0.2s;

   &:active {
      transform: scale(0.95);
      opacity: 0.9;
   }
}

.join-button-text {
   color: #ffffff;
   font-weight: 500;
}

/* 已加入按钮 */
.joined-button {
   width: 120rpx;
   height: 68rpx;
   line-height: 68rpx;
   text-align: center;
   background-color: #f5f5f5;
   color: #666666;
   border-radius: 34rpx;
   font-size: 22rpx;
   border: none;
   padding: 0;
   transition: all 0.2s;

   &:active {
      transform: scale(0.95);
      background-color: #eeeeee;
   }
}

.joined-button-text {
   color: #666666;
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .chat-cart-container {
      padding: 20rpx;
   }

   .chat-avatar {
      width: 90rpx;
      height: 90rpx;
      border-radius: 18rpx;
   }

   .chat-name {
      font-size: 24rpx;
   }

   .introductory {
      font-size: 16rpx;
      line-height: 36rpx;
   }

   .join-button,
   .joined-button {
      width: 110rpx;
      height: 62rpx;
      line-height: 62rpx;
      font-size: 26rpx;
   }
}
</style>
