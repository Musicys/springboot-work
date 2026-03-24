<template>
   <scroll-view
      scroll-y
      @scrolltolower="setData"
      @scroll="handleScroll"
      class="usermessage-page">
      <!-- #ifdef H5 -->
      <view class="hader" :class="{ 'black-bg': showNavbg }">
         <view class="nav-left" @click.stop="handleBack">
            <wd-icon name="arrow-left" size="32rpx"></wd-icon>
            {{ userInfo.username }}
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

      <!-- 顶部用户信息展示区 -->

      <view class="bgimg">
         <image src="/src/static/mssgaebg.png"></image>
      </view>

      <!-- 顶部用户信息展示区 -->
      <view class="user-info-section">
         <view class="header-bg"></view>
         <view class="avatar-container">
            <view class="avatar">
               <wd-img
                  :src="
                     userInfo.avatarUrl
                        ? userInfo.avatarUrl.trim()
                        : 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'
                  "
                  radius="50">
                  <template #error>
                     <wd-skeleton theme="avatar" />
                  </template>
                  <template #loading>
                     <wd-skeleton theme="avatar" />
                  </template>
               </wd-img>
            </view>
            <view
               v-if="userInfo.login === 1"
               class="status-dot"
               :class="{ online: userInfo.login === 1 }"></view>
         </view>
         <view class="user-profile">
            <!-- 用户头像 -->

            <!-- 用户基本信息 -->
            <view class="user-basic-info">
               <view class="username-section">
                  <view class="box">
                     <tn-icon
                        :name="
                           userInfo.gender === 1 ? 'sex-male' : 'sex-female'
                        "
                        size="38"
                        :color="userInfo.gender === 1 ? '#4caf50' : '#f44336'"
                        bold />

                     <text class="username">{{
                        userInfo.username || userInfo.userAccount
                     }}</text>
                  </view>
                  <text class="user-id"
                     >用户编号：{{ userInfo.id || '000000' }}</text
                  >
               </view>
               <view class="user-meta">
                  <text class="age">{{ userInfo.age || '--' }}岁</text>
                  <text class="divider">|</text>
                  <text class="location"
                     >{{ userInfo.province || '--' }}
                     {{ userInfo.district || '' }}</text
                  >
               </view>
            </view>
         </view>

         <!-- 个人简介 -->
         <view class="intro-section">
            <text class="intro-content">{{
               userInfo.introductory || '暂无简介'
            }}</text>
         </view>

         <!-- 用户标签 -->
         <view class="tags-container">
            <view
               v-for="(tag, index) in userInfo.tags
                  ? JSON.parse(userInfo.tags)
                  : []"
               :key="index"
               class="tag">
               {{ tag }}
            </view>
         </view>
         <view class="feature-section">
            <tn-photo-album
               :data="JSON.parse(userInfo.imagsarr || '[]')"
               :column="5"
               max="9" />
         </view>
         <!-- #ifdef APP -->
         <view
            style="
               height: 80rpx;
               width: 100%;
               position: absolute;
               left: 0;
               bottom: -80rpx;
               z-index: 9999;
            "
            :style="showNavbg ? 'background: #fff' : 'background: black'">
         </view>
         <!-- #endif -->
         <!-- 下方动态内容区 -->
      </view>
      <wd-tabs
         v-model="selectTab"
         @change="handleChange"
         auto-line-width
         swipeable
         sticky>
         <block v-for="item in Tab" :key="item.key">
            <wd-tab :title="`${item.value}`">
               <view style="height: var(--status-bar-height); background: #fff">
               </view>
               <view class="dynamic-list">
                  <!-- 动态项 -->
                  <view class="srcoll" v-if="selectTab == 0">
                     <DynamicCart
                        v-for="value in dynamicList"
                        :key="value.id"
                        :dynamic="value"></DynamicCart>
                     <NoData v-if="dynamicList.length === 0"></NoData>
                     <!-- 加载状态 -->
                     <view class="loading-container" v-if="isDataLoading">
                        <wd-loading size="large" color="#9c27b0"></wd-loading>
                     </view>

                     <!-- 加载完毕提示 -->
                     <view
                        class="load-complete"
                        v-if="!isDataLoading && dynamicList.length > 0">
                        <text class="complete-text">已加载完毕</text>
                     </view>
                  </view>
                  <view class="srcoll" v-if="selectTab == 1">
                     <DynamicCart
                        v-for="value in focusdynamicsList"
                        :key="value.id"
                        :dynamic="value"></DynamicCart>
                     <NoData v-if="focusdynamicsList.length === 0"></NoData>
                     <!-- 加载状态 -->
                     <view
                        class="loading-container"
                        v-if="isDataLoadingtwo && focusdynamicsList.length > 0">
                        <wd-loading size="large" color="#9c27b0"></wd-loading>
                     </view>

                     <!-- 加载完毕提示 -->
                     <view
                        class="load-complete"
                        v-if="
                           !isDataLoadingtwo && focusdynamicsList.length > 0
                        ">
                        <text class="complete-text">已加载完毕</text>
                     </view>
                  </view>
                  <view class="srcoll" v-if="selectTab == 2">
                     <DynamicCart
                        v-for="value in lovedynamicsList"
                        :key="value.id"
                        :dynamic="value"></DynamicCart>
                     <NoData v-if="lovedynamicsList.length === 0"></NoData>
                     <!-- 加载状态 -->
                     <view
                        class="loading-container"
                        v-if="
                           isDataLoadingthree && lovedynamicsList.length > 0
                        ">
                        <wd-loading size="large" color="#9c27b0"></wd-loading>
                     </view>

                     <!-- 加载完毕提示 -->
                     <view
                        class="load-complete"
                        v-if="
                           !isDataLoadingthree && lovedynamicsList.length > 0
                        ">
                        <text class="complete-text">已加载完毕</text>
                     </view>
                  </view>
               </view>
            </wd-tab>
         </block>
      </wd-tabs>
      <!-- 底部操作按钮 -->
      <view class="bottom-actions">
         <view class="action-btn edit-btn" @click.stop="handleEdit">
            <text class="btn-label">编辑资料</text>
         </view>
      </view>
   </scroll-view>
</template>

<script setup lang="ts">
import { useStore } from '@/store/user';
import { useRouter, useRoute } from 'uni-mini-router';
import { getDynamicById, deleteDynamic } from '@/api/dynamic';
import { onMounted, reactive, ref, watch } from 'vue';
import MassageDirCart from '@/components/massage-dir-cart/index.vue';
import NoData from '@/components/no-data/index.vue';
import { throttle, debounce } from '@/util';
import { focusdrslst } from '@/api/focus';
import { Lovemygooddrs } from '@/api/love';
import DynamicCart from '@/components/dynamic-cart/index.vue';
const store = useStore();
const router = useRouter();
const route = useRoute();

const userInfo = ref(store.userInfo);

const Tab = reactive([
   {
      key: 1,
      value: '动态'
   },
   {
      key: 2,
      value: '关注'
   },
   {
      key: 3,
      value: '赞过'
   }
]);
const selectTab = ref(0);
const showNavbg = ref(false);
const isDataLoading = ref(true);
const isDataLoadingtwo = ref(true);
const isDataLoadingthree = ref(true);
const fromTabone = reactive({
   page: 1,
   pageSize: 10,
   id: userInfo.value.id
});
const formTabtwo = reactive({
   page: 1,
   pageSize: 10,
   sach: ''
});

const formTabthree = reactive({
   page: 1,
   pageSize: 10,
   sach: ''
});
const dynamicList = ref([]);
const focusdynamicsList = ref([]);
const lovedynamicsList = ref([]);
// 编辑资料
const handleEdit = () => {
   // 这里可以跳转到编辑资料页面
   router.push({
      name: 'exituser'
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
const setData = () => {
   if (!isDataLoading.value && selectTab.value == 0) return;
   if (!isDataLoadingtwo.value && selectTab.value == 1) return;
   if (!isDataLoadingthree.value && selectTab.value == 2) return;

   if (selectTab.value === 0) {
      getDynamicById(fromTabone).then(res => {
         if (res.code == 0) {
            if (fromTabone.page == 1) {
               dynamicList.value = res.data.records;
            } else {
               dynamicList.value = [...dynamicList.value, ...res.data.records];
            }

            if (res.data.records.length == fromTabone.pageSize) {
               fromTabone.page = fromTabone.page + 1;
            } else {
               isDataLoading.value = false;
            }
         }
      });
   } else if (selectTab.value === 1) {
      focusdrslst(formTabtwo).then(res => {
         if (res.code == 0) {
            if (formTabtwo.page == 1) {
               focusdynamicsList.value = res.data;
            } else {
               focusdynamicsList.value = [
                  ...focusdynamicsList.value,
                  ...res.data
               ];
            }
            if (res.data.length == formTabtwo.pageSize) {
               formTabtwo.page = formTabtwo.page + 1;
            } else {
               isDataLoadingtwo.value = false;
            }
         }
      });
   } else {
      Lovemygooddrs(formTabthree).then(res => {
         if (res.code == 0) {
            if (formTabthree.page == 1) {
               lovedynamicsList.value = res.data;
            } else {
               lovedynamicsList.value = [
                  ...lovedynamicsList.value,
                  ...res.data
               ];
            }
            if (res.data.length == formTabthree.pageSize) {
               formTabthree.page = formTabthree.page + 1;
            } else {
               isDataLoadingthree.value = false;
            }
         }
      });
   }
};
watch(selectTab, (newValue, oldValue) => {
   setData();
});

onMounted(() => {
   setData();
});
onShow(() => {
   userInfo.value = store.userInfo;
   setData();
});
</script>

<style lang="scss" scoped>
.list-item {
   position: relative;
   display: flex;
   padding: 10px 15px;
   background: #fff;
   color: #464646;
}

.list-item:after {
   position: absolute;
   display: block;
   content: '';
   height: 1px;
   left: 0;
   width: 100%;
   bottom: 0;
   background: #eee;
   transform: scaleY(0.5);
}
image {
   display: block;
   width: 120px;
   height: 78px;
   margin-right: 15px;
}
.right {
   -webkit-box-flex: 1;
   -ms-flex: 1;
   flex: 1;
}

// 主色调定义 - 紫色系
$primary-purple: #9c27b0;
$light-purple: #e1bee7;
$dark-purple: #6a1b9a;
$accent-purple: #ea80fc;

.usermessage-page {
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
   right: 0rpx;
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
   // align-items: center;
   margin-bottom: 10rpx;
   flex-direction: column;
   justify-content: start;
}

.username {
   font-size: 32rpx;
   font-weight: 700;
   margin: 10rpx;
}
.box {
   display: flex;

   align-items: center;
}

.user-id {
   font-size: 26rpx;
   opacity: 0.8;
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
   padding: 10rpx 30rpx;
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

.edit-btn {
   background: black;
}

.edit-btn .btn-label {
   color: white;
   font-size: 32rpx;
   font-weight: 600;
}
.hader {
   position: fixed;
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

// #ifdef APP
.hader {
   height: 80rpx;
}
// #endif

// #ifdef H5
.hader {
   height: 100rpx;
}
// #endif

.srcoll {
   position: sticky;
   top: 10rpx;
}
.black-bg {
   background: black;
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

.complete-text {
   font-size: 26rpx;
   color: #999;
   text-align: center;
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
// #ifdef APP
:deep() {
   .wd-sticky__container {
      top: var(--status-bar-height) !important;
   }
}
// #endif
</style>
