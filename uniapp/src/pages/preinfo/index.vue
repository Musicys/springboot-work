<template>
   <scroll-view
      class="preinfo-page"
      scroll-y
      @scroll="handleScroll"
      @scrolltolower="setData"
      v-if="lookUser">
      <!-- #ifdef H5 -->
      <view class="hader" :class="{ 'black-bg': showNavbg }">
         <view class="nav-left" @click="handleBack">
            <wd-icon name="arrow-left" size="32rpx"></wd-icon>
            {{ lookUser.username }}
         </view>
         <view class="nav-center"> </view>
         <view class="nav-right"> </view>
      </view>
      <!-- #endif -->
      <!-- #ifdef APP -->
      <view
         class="hader"
         :class="{ 'black-bg': showNavbg }"
         :style="showNavbg ? 'background: #fff' : 'background: transparent'">
         <view class="nav-center"> </view>
         <view class="nav-right"> </view>
      </view>
      <!-- #endif -->

      <view class="bgimg">
         <image src="/src/static/mssgaebg.png"></image>
      </view>
      <!-- 顶部用户信息展示区 -->
      <view class="user-info-section">
         <view class="header-bg"></view>
         <view class="avatar-container">
            <image
               :src="
                  lookUser.avatarUrl
                     ? lookUser.avatarUrl.trim()
                     : 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'
               "
               mode="aspectFill"
               class="avatar"></image>
            <view
               v-if="lookUser.login === 1"
               class="status-dot"
               :class="{ online: lookUser.login === 1 }"></view>
         </view>
         <view class="user-profile">
            <!-- 用户头像 -->

            <!-- 用户基本信息 -->
            <view class="user-basic-info">
               <view class="username-section">
                  <view class="box">
                     <tn-icon
                        :name="
                           lookUser.gender === 1 ? 'sex-male' : 'sex-female'
                        "
                        size="38"
                        :color="lookUser.gender === 1 ? '#4caf50' : '#f44336'"
                        bold />

                     <text class="username">{{
                        lookUser.username || lookUser.userAccount
                     }}</text>
                  </view>
               </view>
               <view class="user-id"
                  >用户编号：{{ lookUser.id || '000000' }}</view
               >
               <view class="user-meta">
                  <text class="age">{{ lookUser.age || '--' }}岁</text>
                  <text class="divider">|</text>
                  <text class="location"
                     >{{ lookUser.province || '--' }}
                     {{ lookUser.district || '' }}</text
                  >
               </view>
            </view>
         </view>

         <!-- 个人简介 -->
         <view class="intro-section">
            <text class="intro-content">{{
               lookUser.introductory || '暂无简介'
            }}</text>
         </view>

         <!-- 用户标签 -->
         <view class="tags-container">
            <view
               v-for="(tag, index) in lookUser.tags
                  ? JSON.parse(lookUser.tags)
                  : []"
               :key="index"
               class="tag">
               {{ tag }}
            </view>
         </view>
         <view class="feature-section">
            <tn-photo-album
               :data="JSON.parse(lookUser.imagsarr || '[]')"
               :column="5"
               max="9" />
         </view>
      </view>

      <!-- 中间功能模块区域 -->

      <!-- 下方动态内容区 -->
      <view class="dynamic-section">
         <wd-tabs
            v-model="selectTab"
            @change="handleChange"
            auto-line-width
            swipeable
            sticky>
            <block v-for="item in Tab" :key="item.key">
               <wd-tab :title="`${item.value}`">
                  <view class="dynamic-list">
                     <view class="srcoll" v-if="selectTab == 0">
                        <DynamicCart
                           v-for="value in dynamicList"
                           :key="value.id"
                           :dynamic="value"></DynamicCart>
                        <NoData v-if="dynamicList.length === 0"></NoData>
                        <!-- 加载状态 -->
                        <view
                           class="loading-container"
                           v-if="isDataLoading && dynamicList.length > 0">
                           <wd-loading
                              size="large"
                              color="#9c27b0"></wd-loading>
                        </view>

                        <!-- 加载完毕提示 -->
                        <view
                           class="load-complete"
                           v-if="!isDataLoading && dynamicList.length > 0">
                           <text class="complete-text">已加载完毕</text>
                        </view>
                     </view>
                     <view class="srcoll" v-if="selectTab == 1">
                        <!-- 加载完毕提示 -->
                        <NoData></NoData>
                     </view>
                  </view>
               </wd-tab>
            </block>
         </wd-tabs>
      </view>
      <!-- 底部操作按钮 -->
      <view class="bottom-actions">
         <view class="action-btn chat-btn" @click="addFriend">
            <text class="btn-label">聊天</text>
         </view>
         <view class="action-btn follow-btn" @click="handleFollow">
            <text class="btn-label">{{ isFollowing ? '已关注' : '关注' }}</text>
         </view>
      </view>
   </scroll-view>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'uni-mini-router';
import { ref, onMounted, reactive } from 'vue';
import { getUsersDetailViews } from '@/api/detail';
import { User } from '@/api/user/model/type';
import { isFocus, FocusAdd, FocusDel } from '@/api/focus';
import { getDynamicById } from '@/api/dynamic';
import DynamicCart from '@/components/dynamic-cart/index.vue';
import { throttle, debounce } from '@/util';
import { sockeStore } from '@/store/socke';
import { useStore } from '@/store/user';
import { getTEXT } from '@/util/mssageType';
const store = useStore();
const route = useRoute();
const router = useRouter();
const { send, IsFriend } = sockeStore();
// 用户信息
const lookUser = ref<User>();
const Tab = reactive([
   {
      key: 2,
      value: '动态'
   },
   {
      key: 3,
      value: '收藏'
   }
]);
// 是否已关注
const showNavbg = ref(false);
const isFollowing = ref(false);
const selectTab = ref(0);
const isDataLoading = ref(true);
const dynamicList = ref<[]>([]);
//相册
const albumList = computed(() => {
   return JSON.parse(lookUser.value?.imagsarr || '[]');
});
onMounted(() => {
   if (route.query.userId == store.userInfo.id) {
      router.replace({
         name: 'usermessage'
      });
   }
   getUsersDetailViews(Number(route.query.userId)).then(res => {
      if (res.code === 0) {
         lookUser.value = res.data;
      }
   });
   isFocus(Number(route.query.userId)).then(res => {
      if (res.code === 0) {
         isFollowing.value = res.data;
      }
   });

   setData();
});

const from = reactive({
   page: 1,
   pageSize: 10
});
const setData = () => {
   if (!isDataLoading.value && selectTab.value === 0) {
      return;
   }

   getDynamicById({
      ...from,
      id: Number(route.query.userId)
   }).then(res => {
      if (res.code === 0) {
         dynamicList.value = [...dynamicList.value, ...res.data.records];
         if (res.data.records.length == from.pageSize) {
            from.page = from.page + 1;
         } else {
            isDataLoading.value = false;
         }
      }
   });
};
const handleScroll = debounce(e => {
   if (e.detail.scrollTop > 50) {
      showNavbg.value = true;
   } else {
      showNavbg.value = false;
   }
}, 10);
const handleBack = () => {
   uni.navigateBack();
};
// 获取用户详情
// 跳转到聊天页面
//私聊
const addFriend = () => {
   const isFriend = IsFriend(lookUser.value.userId);
   if (isFriend) {
      router.push({
         name: 'chat',
         params: { sendid: lookUser.value.userId }
      });
   } else {
      send(
         JSON.stringify({
            id: store.userInfo.id,
            type: 4,
            sendid: lookUser.value?.id,
            sendteam: null,
            context: getTEXT('我们开始聊天吧'),
            sendTime: new Date()
         })
      );
      setTimeout(() => {
         router.push({
            name: 'chat',
            params: { sendid: lookUser.value?.id }
         });
      }, 500);
   }
};
// 关注/取消关注
const handleFollow = () => {
   //判断是否关注，调用关注接口和取消关注接口
   if (isFollowing.value) {
      // 取消关注

      FocusDel({
         focusUserId: Number(route.query.userId)
      }).then(res => {
         if (res.code === 0) {
            uni.showToast({
               title: '取消关注成功',
               icon: 'none'
            });
            isFollowing.value = !isFollowing.value;
         }
      });
   } else {
      // 关注
      FocusAdd({
         focusUserId: Number(route.query.userId)
      }).then(res => {
         if (res.code === 0) {
            uni.showToast({
               title: '关注成功',
               icon: 'none'
            });
            isFollowing.value = !isFollowing.value;
         }
      });
   }

   // 这里可以添加关注/取消关注的API调用
};
</script>

<style lang="scss" scoped>
// 主色调定义 - 紫色系
$primary-purple: #3e2642;
$light-purple: #e1bee7;
$dark-purple: #6a1b9a;
$accent-purple: #ea80fc;

.preinfo-page {
   background-color: #f5f5f5;
   height: 100vh;
   padding-bottom: 120rpx; // 为底部按钮留出空间
}

// 顶部用户信息展示区
.user-info-section {
   position: relative;
   background: black;
   border-radius: 30rpx 30rpx 0 0;

   padding-top: 100rpx;
   padding-bottom: 30rpx;
   color: white;
   box-shadow: 0 4rpx 20rpx rgba(156, 39, 176, 0.3);
   margin-top: 15vh;
}

.header-bg {
   position: absolute;
   top: 0;
   left: 0;
   right: 0;
   height: 200rpx;
   background: radial-gradient(
      circle at 50% 50%,
      rgba(255, 255, 255, 0.15),
      transparent 70%
   );
}

.user-profile {
   display: flex;

   padding: 0 30rpx;
   flex-direction: column;
   justify-content: start;
   margin-top: 20rpx;
   margin-bottom: 20rpx;
}

.avatar-container {
   // position: relative;
   // --h: 50rpx;
   position: absolute;
   top: -50rpx;
   left: 50rpx;

   margin-right: 25rpx;
}

.avatar {
   width: 160rpx;
   height: 160rpx;
   border-radius: 50%;
   border: 4rpx solid rgba(255, 255, 255, 0.8);
}

.status-dot {
   position: absolute;
   bottom: 15rpx;
   right: -15rpx;
   width: 30rpx;
   height: 30rpx;
   border-radius: 50%;
   background-color: rgba(255, 255, 255, 0.5);
   border: 3rpx solid white;
}

.status-dot.online {
   background-color: #07c160;
}

.user-basic-info {
   flex: 1;
}

.username-section {
   display: flex;
   align-items: center;
   margin-bottom: 10rpx;
}

.username {
   font-size: 42rpx;
   font-weight: 700;
   margin-right: 15rpx;
}

.user-id {
   font-size: 26rpx;
   opacity: 0.8;
   margin: 10rpx 0;
}

.user-meta {
   display: flex;
   align-items: center;
   font-size: 26rpx;
   opacity: 0.8;
}

.divider {
   margin: 0 15rpx;
}

.intro-section {
   padding: 0 30rpx;
   margin-bottom: 25rpx;
}

.intro-content {
   font-size: 28rpx;
   line-height: 1.6;
   opacity: 0.9;
}

.tags-container {
   display: flex;
   flex-wrap: wrap;
   gap: 12rpx;
   padding: 0 30rpx;
}

.tag {
   padding: 8rpx 20rpx;
   background-color: rgba(255, 255, 255, 0.2);
   color: white;
   font-size: 26rpx;
   border-radius: 20rpx;
}

// 中间功能模块区域
.feature-section {
   padding: 30rpx 10rpx;
}

.feature-grid {
   display: grid;
   grid-template-columns: repeat(4, 1fr);
   gap: 20rpx;
}

.feature-card {
   display: flex;
   flex-direction: column;
   align-items: center;
   padding: 25rpx 15rpx;
   background: white;
   border-radius: 20rpx;
   box-shadow: 0 2rpx 15rpx rgba(0, 0, 0, 0.05);
   transition: all 0.3s ease;
}

.feature-card:active {
   transform: scale(0.98);
   box-shadow: 0 1rpx 8rpx rgba(0, 0, 0, 0.1);
}

.feature-icon {
   width: 60rpx;
   height: 60rpx;
   border-radius: 50%;
   margin-bottom: 15rpx;
}

.planet-icon {
   background: linear-gradient(135deg, #ff9800, #f57c00);
}

.wealth-icon {
   background: linear-gradient(135deg, #4caf50, #388e3c);
}

.charm-icon {
   background: linear-gradient(135deg, $accent-purple, $primary-purple);
}

.likes-icon {
   background: linear-gradient(135deg, #f44336, #d32f2f);
}

.feature-title {
   font-size: 26rpx;
   font-weight: 600;
   color: #333;
   margin-bottom: 5rpx;
}

.feature-subtitle {
   font-size: 24rpx;
   color: #999;
}

// 底部操作按钮
.bottom-actions {
   position: fixed;
   bottom: 0;
   left: 0;
   right: 0;
   display: flex;
   padding: 20rpx 30rpx;
   background: white;
   box-shadow: 0 -2rpx 15rpx rgba(0, 0, 0, 0.05);
   gap: 20rpx;
}

.action-btn {
   flex: 1;
   height: 90rpx;
   border-radius: 45rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   transition: all 0.3s ease;
}

.action-btn:active {
   transform: scale(0.98);
}

.chat-btn {
   background-color: #f0f0f0;
}

.chat-btn .btn-label {
   color: #333;
   font-size: 32rpx;
   font-weight: 600;
}

.follow-btn {
   background: linear-gradient(135deg, $primary-purple, $dark-purple);
}

.follow-btn .btn-label {
   color: white;
   font-size: 32rpx;
   font-weight: 600;
}
// 下方动态内容区
.dynamic-section {
   width: 100%;
   overflow: hidden;
   margin: auto;
   display: flex;
   justify-content: center;
   align-items: center;
   flex-direction: column;
}

.dynamic-list {
   background: white;
   border-radius: 20rpx;
   overflow: hidden;
   width: 100%;
   margin-bottom: 15rpx;
   min-height: 82vh;
   box-shadow: 0 2rpx 15rpx rgba(0, 0, 0, 0.05);
   position: relative;
}
.srcoll {
   position: sticky;
   top: 10rpx;
}
.black-bg {
   background: black;
   color: white;
}

/* 加载状态容器 */
.loading-container {
   display: flex;
   justify-content: center;
   align-items: center;
   padding: 40rpx 0;
   height: 100rpx;
}

/* 加载完毕提示 */
.load-complete {
   display: flex;
   justify-content: center;
   align-items: center;
   padding: 20rpx 0;
   height: 80rpx;
}
.hader {
   position: fixed;
   height: 80rpx;

   display: flex;
   justify-content: space-between;
   align-items: center;
   color: white;
   top: 0;
   left: 0;
   z-index: 999;
   transition: all 0.3s ease;
   right: 0;
}
.bgimg {
   height: 30vh;
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   & > image {
      width: 100%;
      height: 100%;
   }
}
</style>
