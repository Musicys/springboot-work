<template>
   <view class="group-chat-container">
      <!-- 顶部导航栏 -->
      <view class="chat-header">
         <view class="header-left" @click="goBack">
            <tn-icon name="left" bold size="36"></tn-icon>
         </view>

         <view class="header-center">
            <text class="group-name"
               >{{ groupChat?.chatName || '' }}({{
                  groupChat?.count || 0
               }})</text
            >
            <text class="group-tag" v-if="groupChat && groupChat.tagName">
               <tn-tag
                  bg-color="#F5F5F5"
                  text-color="#606060"
                  shape="round"
                  font-size="22rpx"
                  type="primary"
                  bold>
                  {{
                     groupChat.tagName
                        ? JSON.parse(groupChat.tagName)[0] || 'Soulmates'
                        : ''
                  }}</tn-tag
               >
            </text>
         </view>

         <view class="header-right" @click="showMoreOptions">
            <text class="more-icon">⋮</text>
         </view>
      </view>

      <!-- 消息展示区域 -->
      <scroll-view
         class="chat-area"
         scroll-y
         :scroll-top="bottomsize"
         :scroll-with-animation="true"
         :show-scrollbar="false">
         <view class="message-container">
            <!-- 查看更多 -->
            <view v-if="hasMore" class="see-more" @click="loadMoreMessages">
               <text class="see-more-text">查看更多</text>
            </view>
            <!-- 时间分割线 -->
            <view class="time-divider">
               <text class="time-text">{{
                  sendList && sendList.length > 0
                     ? formatDate(getFirstMessage()?.createtime)
                     : ''
               }}</text>
            </view>

            <!-- 消息列表 -->
            <view
               v-for="message in sendList || []"
               :key="message.id"
               class="message-item"
               :class="{ 'my-message': isCurrentUser(message.userid) }">
               <template v-if="message.type != 1">
                  <view class="cart">
                     <view class="message-time" v-if="message.showTime">{{
                        formatMessageTime(message.createtime)
                     }}</view>

                     <view
                        class="cart-content"
                        :class="{
                           'my-cart': isCurrentUser(message.userid)
                        }">
                        <view class="avatar">
                           <image
                              @click="goToUser(message.userid)"
                              :src="message.avatarUrl?.replace(/`/g, '') || ''"
                              mode="aspectFill"
                              class="avatar-img"></image>
                        </view>

                        <!-- 消息内容 -->
                        <view
                           class="message-content"
                           :class="{
                              my: isCurrentUser(message.userid),
                              you: !isCurrentUser(message.userid)
                           }">
                           <!-- 用户名和群主标识 -->
                           <view class="sender-info">
                              <text class="username">{{
                                 message.username
                              }}</text>
                              <text
                                 class="admin-badge"
                                 v-if="isAdmin(message.userid)"
                                 >群主</text
                              >
                           </view>

                           <!-- 消息气泡 -->
                           <view
                              :class="{
                                 'message-bubble': true
                              }">
                              <RelolverChat
                                 :msg="message.context"></RelolverChat>
                           </view>

                           <!-- 时间戳 -->
                        </view>
                     </view>
                  </view>
               </template>
               <template v-else>
                  <!-- 用户加入聊群 -->
                  <view class="message-content join-message">
                     <text
                        class="message-text join-message-text"
                        style="color: #999999"
                        >{{ message.context + '加入群聊' }}</text
                     >
                  </view>
               </template>
            </view>

            <!-- 占位元素，确保滚动到底部时最新消息完全显示 -->
            <view class="bottom-space"></view>
         </view>
      </scroll-view>
      <!-- 底部操作区域 -->
      <InputSend @sumbitValue="sendMsg"></InputSend>
   </view>
</template>

<script setup lang="ts">
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import { useRoute, useRouter } from 'uni-mini-router';
import { onMounted, ref, nextTick, watch } from 'vue';
import { getChatMsgList } from '@/api/chat';
import { onHide } from '@dcloudio/uni-app';
import { formatMessageTime } from '@/util';
import { MESSAGE_TYPE } from '@/util/mssageType';
import InputSend from '@/components/input-send/index.vue';
import RelolverChat from '@/components/relolver-chat/index.vue';
import { shouldShowTime } from '@/util/chat';
const route = useRoute();
const router = useRouter();
const { userInfo } = useStore();
const { ChatList, send } = sockeStore();
const groupChat = ref({
   id: 1,
   count: 14,
   lookCount: 0,
   chatName: 'JAVA聊群',
   tagName: '["tag1", "tag2"]',
   url: 'https://ts2.tc.mm.bing.net/th/id/OIP-C.WXtLvr1iXhwS6T1mtv9TDgHaHa?rs=1&pid=ImgDetMain&o=7&rm=3'
});
const sendList = ref([]);

const inputValue = ref('');
const inputFocused = ref(false);
const messageContainer = ref<any>(null);
const bottomsize = ref(0);
const form = ref({
   page: 1,
   pageSize: 20,
   chatId: ''
});
//是否还有更多消息
const hasMore = ref(true);
watch(
   () => ChatList,
   () => {
      // 检查ChatList中是否有当前群聊的数据更新
      if (ChatList && Array.isArray(ChatList)) {
         const updatedChat = ChatList.find(
            item => item.id == route.params.groupId
         );
         if (updatedChat) {
            // 更新群聊基本信息
            groupChat.value = updatedChat;
            // 更新消息列表
            sendList.value =
               updatedChat.sendList.map((itm, idx) => {
                  let bol = false;
                  if (idx == 0) {
                     bol = true;
                  } else {
                     bol = shouldShowTime(
                        itm.createtime,
                        updatedChat.sendList[idx - 1].createtime
                     );
                  }
                  return {
                     ...itm,
                     showTime: bol
                  };
               }) || [];

            if (sendList.value.length < form.value.page * form.value.pageSize) {
               hasMore.value = false;
            } else {
               form.value.page += 1;
            }
         }
      }

      // 滚动到底部
      nextTick(() => {
         scrollToBottom();
      });
   },
   { deep: true }
);
const goToUser = id => {
   if (id == userInfo.id) {
      return;
   }
   router.push({
      name: 'preinfo',
      params: {
         userId: id
      }
   });
};

// 获取第一条消息
const getFirstMessage = () => {
   if (sendList.value && Array.isArray(sendList.value)) {
      return sendList.value[0] || null;
   }
   return null;
};
// 格式化日期
const formatDate = (timeStr: string) => {
   if (!timeStr) return '';
   try {
      const date = new Date(timeStr);
      const now = new Date();
      const diff = now.getTime() - date.getTime();
      const oneDay = 24 * 60 * 60 * 1000;

      if (diff < oneDay) {
         return '今天';
      } else if (diff < 2 * oneDay) {
         return '昨天';
      } else {
         return `${date.getMonth() + 1}月${date.getDate()}日`;
      }
   } catch (error) {
      return timeStr;
   }
};
// 判断是否为当前用户
const isCurrentUser = (userId: number) => {
   return userInfo?.id === userId; // 假设当前用户ID为26758
};
// 判断是否为管理员（可以根据实际需求修改判断逻辑）
const isAdmin = (userId: number) => {
   // 假设用户ID为26768的是群主
   return userId === 1;
};
// 返回上一页
const goBack = () => {
   router.back();
};
// 显示更多选项
const showMoreOptions = () => {
   uni.showActionSheet({
      itemList: ['群设置', '查看群成员', '退出群聊'],
      success: res => {
         console.log('选中了', res.tapIndex);
         // 根据选中的索引执行相应操作
      }
   });
};
function isValidJSON(str) {
   try {
      console.log('解析JSON字符串:', str);

      JSON.parse(str); // 尝试解析字符串
      return true; // 如果没有抛出错误，返回 true
   } catch (e) {
      return false; // 捕获错误，返回 false
   }
}
// 发送消息
const sendMsg = (message: string) => {
   send(
      JSON.stringify({
         id: userInfo.id,
         type: 5,
         sendid: route.params.groupId,
         sendteam: null,
         context: message,
         senTime: new Date()
      })
   );
};

// 加载更多消息
const loadMoreMessages = () => {
   uni.showLoading({
      title: '加载中',
      mask: true
   });

   getChatMsgList(form.value).then(res => {
      if (res.code === 0) {
         console.log(res.data);
         let arr = res.data || [];
         arr.reverse();

         // 将新消息添加到现有消息前面，实现历史消息加载
         const newSendList = [...arr, ...sendList.value];
         sendList.value =
            newSendList.map((itm, idx) => {
               let bol = false;
               if (idx == 0) {
                  bol = true;
               } else {
                  bol = shouldShowTime(
                     itm.createtime,
                     newSendList[idx - 1].createtime
                  );
               }
               return {
                  ...itm,
                  showTime: bol
               };
            }) || [];

         if (arr.length != form.value.pageSize) {
            hasMore.value = false;
         } else {
            form.value.page += 1;
         }
         uni.hideLoading();
      }
   });
};

// 滚动到底部函数
const scrollToBottom = () => {
   nextTick(() => {
      let query = uni.createSelectorQuery().in(this);
      query
         .select('.message-container')
         .boundingClientRect(res => {
            setTimeout(() => {
               bottomsize.value = res.height;
            }, 100);
         })
         .exec();
      query = null;
   });
};
onMounted(() => {
   form.value.chatId = Number(route.params.groupId) || '';
   // 尝试从ChatList中获取群聊数据
   if (ChatList && Array.isArray(ChatList)) {
      ChatList.forEach((item, index) => {
         if (item.id == route.params.groupId) {
            groupChat.value = item;
            ChatList[index].lookCount = 0;
            // 更新sendList
            sendList.value =
               item.sendList.map((itm, idx) => {
                  let bol = false;
                  if (idx == 0) {
                     bol = true;
                  } else {
                     bol = shouldShowTime(
                        itm.createtime,
                        item.sendList[idx - 1].createtime
                     );
                  }
                  return {
                     ...itm,
                     showTime: bol
                  };
               }) || [];

            if (sendList.value.length < form.value.page * form.value.pageSize) {
               hasMore.value = false;
            } else {
               form.value.page += 1;
            }
         }
      });
   }

   // 滚动到底部
   nextTick(() => {
      scrollToBottom();
   });
});

onUnload(() => {
   ChatList.forEach((item, index) => {
      if (item.id == route.params.groupId) {
         ChatList[index].lookCount = 0;
         // 更新sendList
      }
   });
});
</script>

<style lang="scss" scoped>
.group-chat-container {
   width: 100%;
   height: 100vh;
   background: var(--quyou-bg-centext-color);
   padding-top: var(--status-bar-height); // 状态栏
   display: flex;
   flex-direction: column;
   position: relative;
}

/* 顶部导航栏样式 */
.chat-header {
   display: flex;
   align-items: center;
   justify-content: space-between;
   padding: 20rpx;
   border-bottom: 1rpx solid rgba(0, 0, 0, 0.1);

   z-index: 10;
   height: 120rpx;
}

.header-left {
   position: relative;
   padding-left: 10rpx;
}

.back-icon {
   font-size: 40rpx;
   color: #333;
}

.unread-badge {
   position: absolute;
   top: -10rpx;
   left: 30rpx;
   min-width: 40rpx;
   height: 40rpx;
   padding: 0 12rpx;
   background-color: #ff4757;
   color: #fff;
   font-size: 24rpx;
   font-weight: 600;
   border-radius: 20rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.header-center {
   flex: 1;
   text-align: center;
   padding: 0 20rpx;
}

.group-name {
   font-size: 24rpx;
   font-weight: 600;
   color: #333;
   display: block;
}

.group-tag {
   font-size: 24rpx;
   color: #999;
   display: block;
   margin-top: 4rpx;
}

.header-right {
   padding-right: 10rpx;
}

.more-icon {
   font-size: 40rpx;
   color: #333;
}

/* 查看更多样式 */
.see-more {
   text-align: center;
   margin-bottom: 20rpx;
   border-radius: 20rpx;
   cursor: pointer;
   transition: background-color 0.2s;
}

.see-more-text {
   font-size: 26rpx;
   color: #666;
   font-weight: 500;
}

/* 消息展示区域样式 */
.message-container {
   flex: 1;
   overflow-y: auto;
   padding: 20rpx;
}

.time-divider {
   text-align: center;
   margin: 20rpx 0;
}

.time-text {
   font-size: 24rpx;
   color: #999;
   background-color: rgba(0, 0, 0, 0.05);
   padding: 4rpx 20rpx;
   border-radius: 16rpx;
}

.message-item {
   display: flex;
   margin-bottom: 30rpx;
   align-items: flex-start;
}

.message-item.my-message {
   flex-direction: row-reverse;
}

.avatar {
   width: 80rpx;
   height: 80rpx;
   border-radius: 50%;
   overflow: hidden;
   margin-right: 20rpx;
   flex-shrink: 0;
}

.message-item.my-message .avatar {
   margin-right: 0;
   margin-left: 20rpx;
}

.avatar-img {
   width: 100%;
   height: 100%;
}

.message-content {
   flex: 1;
   max-width: 70%;
}
.my {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: flex-end;
}
.you {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: start;
}
/* 加入群聊消息样式 - 居中显示 */
.message-content.join-message {
   display: flex;
   justify-content: center;
   align-items: center;
   max-width: 100%;
   margin: 20rpx 0;
}

.join-message-text {
   background-color: rgba(0, 0, 0, 0.05);
   padding: 8rpx 30rpx;
   border-radius: 20rpx;
   font-size: 26rpx;
   color: #666;
   text-align: center;
}

.sender-info {
   display: flex;
   align-items: center;
   margin-bottom: 8rpx;
}

.message-item.my-message .sender-info {
   justify-content: flex-end;
}

.username {
   font-size: 26rpx;
   color: #666;
   margin-right: 10rpx;
}

.admin-badge {
   font-size: 22rpx;
   color: #fff;
   background-color: #ff6b6b;
   padding: 2rpx 10rpx;
   border-radius: 10rpx;
}

.message-bubble {
   background-color: white;
   padding: 16rpx 20rpx;
   border-radius: 20rpx;
   border-top-left-radius: 4rpx;
   display: inline-block;
}

.message-item.my-message .message-bubble {
   background-color: #a2e39c;
   border-top-left-radius: 20rpx;
   border-top-right-radius: 4rpx;
}

.message-text {
   font-size: 28rpx;
   color: #333;
   line-height: 1.5;
   word-break: break-all;
}
.message-time {
   font-size: 24rpx;
   color: #999;
   text-align: center;
   margin: 2em 0;
   display: block;
}

.bottom-space {
   height: 40rpx;
}

/* 底部操作区域样式 */
.chat-footer {
   background-color: #fff;
   border-top: 1rpx solid #f0f0f0;
   padding: 20rpx;
}

.input-area {
   display: flex;
   align-items: center;
}

.message-input {
   flex: 1;
   height: 80rpx;
   border: 1rpx solid #e0e0e0;
   border-radius: 40rpx;
   padding: 0 30rpx;
   background-color: #f5f5f5;
   font-size: 28rpx;
}

.input-actions {
   display: flex;
   align-items: center;
   margin-left: 20rpx;
}

.input-actions .action-icon {
   width: 60rpx;
   height: 60rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 36rpx;
   margin: 0 10rpx;
}

.send-button {
   background-color: #1890ff;
   padding: 16rpx 30rpx;
   border-radius: 24rpx;
   margin-left: 10rpx;
}

.send-button.disabled {
   background-color: #ccc;
}

.send-text {
   color: #fff;
   font-size: 28rpx;
   font-weight: 500;
}

.send-button.disabled .send-text {
   color: #fff;
}
.chat-area {
   flex: 1;
   padding: 10px;
   overflow-y: auto; /* 添加滚动条 */
}
.cart {
   display: flex;
   width: 100%;
   flex-direction: column;
}
.cart-content {
   display: flex;
}
.my-cart {
   flex-direction: row-reverse;
}
</style>
