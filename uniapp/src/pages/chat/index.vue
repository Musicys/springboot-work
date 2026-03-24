<template>
   <view class="chat-page">
      <!-- 导航栏区域 -->
      <wd-navbar left-arrow @click-left="handleClickLeft">
         <template #title>
            <view class="centext">
               <text class="title">
                  <tn-icon
                     :name="friendInfo.gender === 1 ? 'sex-male' : 'sex-female'"
                     :color="friendInfo.gender === 1 ? '#4caf50' : '#f44336'"
                     bold />
                  {{ friendInfo.username || '聊天' }}
               </text>
            </view>
         </template>
         <template #right>
            <wd-button
               class="follow-btn"
               type="success"
               size="small"
               @click="toggleFollow"
               >{{ followText }}</wd-button
            >
         </template>
      </wd-navbar>

      <view class="chat-nav">
         <!-- 人物卡片区域 -->
         <view class="user-card">
            <view class="avatar-container">
               <view class="avatar">
                  <wd-img :src="friendInfo.avatarUrl" radius="50">
                     <template #error>
                        <wd-skeleton theme="avatar" />
                     </template>
                     <template #loading>
                        <wd-skeleton theme="avatar" />
                     </template>
                  </wd-img>
               </view>
               <view
                  v-if="friendInfo.login === 1"
                  :class="[
                     'online-status',
                     { online: friendInfo.login === 1 }
                  ]"></view>
            </view>
            <view class="user-info">
               <view class="box">
                  <view class="tag"> Ta的标签： </view>
                  <view class="tags">
                     <text
                        v-for="(tag, index) in JSON.parse(
                           friendInfo.tags || '[]'
                        )"
                        :key="index"
                        class="tag-item"
                        >{{ tag }}</text
                     >
                  </view>
               </view>
               <view class="box">
                  <view class="tag"> Ta的简介： </view>
                  <view class="intro">{{
                     friendInfo.introductory || '还没有介绍哦！'
                  }}</view>
               </view>
            </view>
         </view>
      </view>

      <!-- 聊天区域 -->
      <scroll-view
         class="chat-area"
         scroll-y
         :scroll-top="bottomsize"
         :scroll-with-animation="true"
         :show-scrollbar="false">
         <view class="bottom-placeholder">
            <view v-if="hasMore" class="see-more" @click="loadMoreMessages">
               <text class="see-more-text">查看更多</text>
            </view>
            <view
               v-for="(msg, index) in messages"
               :key="index"
               :class="['msg-item', msg.isMine ? 'mine' : 'other']">
               <!-- 自己的消息 -->
               <view v-if="msg.showTime" class="msg-time">{{ msg.time }}</view>
               <template v-if="msg.isMine">
                  <view class="!flex flex-row-reverse mb-0.5">
                     <view class="avatar">
                        <wd-img :src="userInfo.avatarUrl" radius="50">
                           <template #error>
                              <wd-skeleton theme="avatar" />
                           </template>
                           <template #loading>
                              <wd-skeleton theme="avatar" />
                           </template>
                        </wd-img>
                     </view>
                     <view class="msg-content">
                        <view
                           v-if="msg.type === 'text'"
                           :class="{
                              'text-msg': isValidJSON(msg.content)
                                 ? JSON.parse(msg.content).type ==
                                   MESSAGE_TYPE.TEXT
                                 : true
                           }">
                           <view>
                              <RelolverChat :msg="msg.content"></RelolverChat>
                           </view>
                        </view>
                     </view>
                  </view>
               </template>
               <!-- 对方的消息 -->
               <template v-else>
                  <view class="!flex mb-0.5">
                     <view class="avatar">
                        <wd-img
                           @click="
                              router.push({
                                 name: 'preinfo',
                                 params: { userId: friendInfo.id }
                              })
                           "
                           :src="friendInfo.avatarUrl"
                           radius="50">
                           <template #error>
                              <wd-skeleton theme="avatar" />
                           </template>
                           <template #loading>
                              <wd-skeleton theme="avatar" />
                           </template>
                        </wd-img>
                     </view>
                     <view class="msg-content">
                        <view
                           v-if="msg.type === 'text'"
                           :class="{
                              'text-msg': isValidJSON(msg.content)
                                 ? JSON.parse(msg.content).type ==
                                   MESSAGE_TYPE.TEXT
                                 : true
                           }">
                           <RelolverChat :msg="msg.content"></RelolverChat>
                        </view>
                        <view
                           v-else-if="msg.type === 'image'"
                           class="image-msg">
                           <image :src="msg.content" mode="aspectFill"></image>
                        </view>
                        <view
                           v-else-if="msg.type === 'voice'"
                           class="voice-msg">
                           <wot-icon
                              name="voice"
                              color="#666"
                              size="20"></wot-icon>
                           <text class="voice-duration"
                              >{{ msg.duration }}''</text
                           >
                        </view>
                     </view>
                  </view>
               </template>
            </view>
         </view>
      </scroll-view>

      <InputSend @sumbitValue="sendMsg"></InputSend>
   </view>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'uni-mini-router';
import { nextTick, onMounted, ref, watch } from 'vue';
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import { UpdateSendsye } from '@/api/wbscoke';
import { getSendMsgList } from '@/api/chat';
import { formatMessageTime } from '@/util';
import InputSend from '@/components/input-send/index.vue';
import RelolverChat from '@/components/relolver-chat/index.vue';
import { getTEXT, MESSAGE_TYPE } from '@/util/mssageType';
import { shouldShowTime } from '@/util/chat';
const { userInfo } = useStore();
const soke = sockeStore();
const { send, UserList, setyellow } = soke;
const route = useRoute();
const router = useRouter();
const messages = ref([]);
const bottomsize = ref<number>(0);
const isFollowed = ref(false);
const followText = ref(isFollowed.value ? '已关注' : '关注');
const friendInfo = ref({
   avatarUrl: '',
   username: '',
   age: 0,
   login: 0,
   sendList: [],
   id: ''
});
const form = ref({
   page: 1,
   pageSize: 20,
   sendId: ''
});
const hasMore = ref(true);
onMounted(() => {
   const sendid = route.query.sendid || '';
   form.value.sendId = sendid;
   try {
      // 尝试从store获取用户信息
      const index = UserList.findIndex(item => item.id == sendid);
      if (sendid !== '' && index !== -1) {
         UpdateSendsye(sendid).then(res => {
            if (res.code == 0) {
               setyellow(userInfo.id, sendid);
            }
         });
         friendInfo.value = UserList[index];
      }
      updateMessages(() => {
         if (messages.value.length < form.value.page * form.value.pageSize) {
            hasMore.value = false;
         } else {
            form.value.page += 1;
         }
      });
   } catch (error) {
      console.error('处理用户数据失败:', error);
   }
});
// 监听sendList变化
watch(
   () => UserList,
   () => {
      updateMessages();
   },
   { deep: true }
);
function isValidJSON(str) {
   try {
      JSON.parse(str); // 尝试解析字符串
      return true; // 如果没有抛出错误，返回 true
   } catch (e) {
      return false; // 捕获错误，返回 false
   }
}
// 加载更多消息
const loadMoreMessages = () => {
   uni.showLoading({
      title: '加载中',
      mask: true
   });

   // 在实际应用中，这里应该调用API加载更多历史消息
   // 这里仅做模拟示例

   // 调用API获取更多消息（示例）
   getSendMsgList(form.value).then(res => {
      if (res.code === 0) {
         const arr = res.data || [];
         arr.reverse();

         const arrsend = arr.map((msg, idx) => {
            let bool;
            if (idx === 0) {
               bool = true;
            } else {
               bool = shouldShowTime(
                  msg.createtime,
                  friendInfo.value.sendList[idx - 1].createtime
               );
            }
            return {
               content: msg.context,
               type: 'text',
               time: formatMessageTime(msg.createtime),
               isMine: msg.userid == userInfo.id, // 假设当前用户ID是26758
               showTime: bool
            };
         });
         // 将新消息添加到现有消息前面，实现历史消息加载
         messages.value = [...arrsend, ...messages.value];

         if (arr.length != form.value.pageSize) {
            hasMore.value = false;
         } else {
            form.value.page += 1;
         }
         uni.hideLoading();
      }
   });
};
const scrollToBottom = () => {
   nextTick(() => {
      let query = uni.createSelectorQuery().in(this);
      query
         .select('.bottom-placeholder')
         .boundingClientRect(res => {
            console.log('Node Info:', res.height);
            setTimeout(() => {
               bottomsize.value = res.height;
            }, 100);
         })
         .exec();
      query = null;
   });
};
const updateMessages = (fun = () => {}) => {
   if (friendInfo.value.sendList && friendInfo.value.sendList.length > 0) {
      messages.value = friendInfo.value.sendList.map((msg, idx) => {
         let bool;
         if (idx === 0) {
            bool = true;
         } else {
            bool = shouldShowTime(
               msg.createtime,
               friendInfo.value.sendList[idx - 1].createtime
            );
         }
         return {
            content: msg.context,
            type: 'text',
            time: formatMessageTime(msg.createtime),
            isMine: msg.userid == userInfo.id, // 假设当前用户ID是26758
            showTime: bool
         };
      });
      fun();
      console.log(messages.value);
   } else {
      // 如果没有消息，设置默认消息
      messages.value = [
         {
            content: getTEXT('你好啊，我们开始聊天把-.-'),
            type: 'text',
            time: '2025-08-20 14:09:22',
            isMine: false,
            showTime: true
         }
      ];
   }
   scrollToBottom();
};
// 返回
const goBack = () => {
   uni.navigateBack();
};
const toggleFollow = () => {
   isFollowed.value = !isFollowed.value;
   followText.value = isFollowed.value ? '已关注' : '关注';
};
// 发送消息
const sendMsg = (str: string) => {
   soke.send(
      JSON.stringify({
         id: userInfo.id,
         type: 3,
         sendid: friendInfo.value.id,
         sendteam: null,
         context: str,
         senTime: new Date()
      })
   );
};
const handleClickLeft = () => {
   uni.navigateBack();
};
</script>

<style lang="scss" scoped>
$theme-color: #ff6b6b;
$light-gray: #f5f5f5;
$border-color: #eee;
$online-green: #52c41a;

.chat-page {
   display: flex;
   padding-top: var(--status-bar-height); /* 状态栏 */
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
   flex-direction: column;
   width: 100vw;
   height: 100vh;
   background-color: $light-gray;
   background: var(--quyou-bg-centext-color);
}

.chat-nav {
   box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-card {
   padding: 10rpx 15rpx;

   .avatar-container {
      --h: 90rpx;
      position: relative;
      width: var(--h);
      height: var(--h);
      .avatar {
         width: 100%;
         height: 100%;
         border-radius: 50%;
         overflow: hidden;
         border: 2px solid #f0f0f0;
         image {
            width: 100%;
            height: 100%;
         }
      }
      .online-status {
         position: absolute;
         bottom: 0;
         right: 0;
         width: 25rpx;
         height: 25rpx;
         border-radius: 50%;
         background-color: #ccc;
         border: 2px solid #fff;
         &.online {
            background-color: $online-green;
         }
      }
   }
   .user-info {
      text-align: center;
      margin-bottom: 10px;
      .box {
         display: flex;
         flex-wrap: wrap;
         font-size: 25rpx;
         justify-content: start;
         align-items: center;
      }
      .tag {
         font-size: 22rpx;
      }
      .intro {
         text-align: center;
         color: #666;
         font-size: 22rpx;
         line-height: 1.4;
         padding: 0 10px;
      }
   }
   .tags {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      margin-bottom: 5px;
      .tag-item {
         padding: 3px 8px;

         border-radius: 10px;
         margin: 3px;
         font-size: 22rpx;
         color: #666;
      }
   }
}

.msg-item {
   display: flex;
   flex-direction: column;
}
.chat-area {
   flex: 1;
   padding: 10px;
   overflow-y: auto; /* 添加滚动条 */
   .msg-item {
      margin-bottom: 15px;
      display: flex;

      &.mine {
         .msg-content {
            .text-msg {
               background-color: #a2e39c;
               color: #333;
            }
            .image-msg image {
               border: 1px solid #a2e39c;
            }
            .voice-msg {
               background-color: #a2e39c;
            }
         }
      }
      &.other {
         .msg-content {
            .text-msg {
               background-color: #fff;
               width: auto;
               color: #333;
            }
            .image-msg image {
               border: 1px solid #fff;
            }
            .voice-msg {
               background-color: #fff;
            }
         }
      }
      .avatar {
         width: 40px;
         height: 40px;
         border-radius: 50%;
         overflow: hidden;
         margin: 0 10px;
         image {
            width: 100%;
            height: 100%;
         }
      }
      .msg-content {
         max-width: 70%;
         .text-msg {
            display: inline-block;
            padding: 8px 12px;
            border-radius: 8px;
            margin-bottom: 3px;
            word-break: break-all;
         }
         .image-msg image {
            width: 200px;
            height: auto;
            border-radius: 8px;
            margin-bottom: 3px;
         }
         .voice-msg {
            display: flex;
            align-items: center;
            padding: 8px 12px;
            border-radius: 8px;
            margin-bottom: 3px;
            .voice-duration {
               margin-left: 5px;
               font-size: 12px;
            }
         }
         .msg-time {
            font-size: 10px;
            display: block;
            margin-bottom: 1.5em;
         }
      }
   }
}
.msg-time {
   text-align: center;
   font-size: 22rpx;
   color: #999;
   margin: 50rpx 0;
}

.input-area {
   display: flex;
   align-items: center;
   padding: 10px;
   border-top: 1px solid $border-color;

   .input-left {
      flex: 1;
      display: flex;
      align-items: center;
      .text-input {
         flex: 1;
         height: 36px;
         line-height: 36px;
         padding: 0 8px;
         border: 1px solid #ddd;
         border-radius: 18px;
         font-size: 24rpx;
         margin: 0 8px;
      }
      .voice-holder {
         flex: 1;
         height: 36px;
         line-height: 36px;
         text-align: center;
         background-color: $light-gray;
         border-radius: 18px;
         margin: 0 8px;
      }
   }
   .input-right {
      .send-btn {
         width: 70px;
         height: 36px;
         line-height: 36px;
         background-color: $theme-color;
         color: #fff;
         border-radius: 18px;
         border: none;
      }
   }
}

.bottom-func {
   display: flex;
   justify-content: space-around;
   padding: 10px 0;
   border-top: 1px solid $border-color;
   .func-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 18px;
      text {
         font-size: 12px;
         color: #666;
         margin-top: 5px;
      }
   }
}

.emoji-panel {
   position: fixed;
   bottom: 120px;
   left: 0;
   right: 0;

   padding: 10px;
   display: flex;
   flex-wrap: wrap;
   .emoji-item {
      width: 30px;
      height: 30px;
      text-align: center;
      line-height: 30px;
      font-size: 20px;
      margin: 5px;
   }
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
.centext {
   font-size: 26rpx;
   display: flex;
   flex-direction: column;
   .title {
      font-size: 23rpx;
   }
   .status {
      color: #666;
      font-size: 24rpx;
   }
}
.follow-btn {
   color: black !important;
}
:deep() {
   .wd-navbar {
      background: none !important  ;
   }
}
.btn {
   background: #fff;
}
</style>
