<template>
   <view class="page">
      <!-- 顶部用户信息卡片 -->
      <view class="page-header">
         <view class="user-info-container">
            <!-- 左侧头像和用户信息 -->
            <view class="user-main-info">
               <view class="avatar-container" @click="showPopup = true">
                  <!-- <image
                     :src="
                       
                    
                     mode="aspectFill" /> -->
                  <view class="user-avatar">
                     <wd-img
                        :src="
                           userInfo?.avatarUrl ||
                           'https://ts1.tc.mm.bing.net/th/id/OIP-C.-r8TdWtF72EheUNjt_uKvwAAAA?rs=1&pid=ImgDetMain&o=7&rm=3'
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

                  <view class="camera-icon">
                     <tn-icon name="camera-fill"></tn-icon>
                  </view>
               </view>
               <view class="user-text-info">
                  <view class="username">
                     <tn-icon
                        :name="
                           userInfo?.gender === 1 ? 'sex-male' : 'sex-female'
                        "
                        size="26"
                        :color="userInfo?.gender === 1 ? '#4caf50' : '#f44336'"
                        bold /><text style="margin-left: 10rpx">
                        {{ userInfo?.username || '用户名称' }}
                     </text></view
                  >
                  <view class="user-email">
                     账号：{{ userInfo?.email || 'user@example.com' }}</view
                  >
               </view>
            </view>

            <!-- 右侧查看按钮 -->
            <view
               class="view-button"
               @click.stop="router.push({ name: 'usermessage' })">
               <text>查看</text>
               <wd-icon name="arrow-right" size="14" class="arrow-icon" />
            </view>
         </view>

         <!-- 用户数据统计 -->
         <view class="stats-container">
            <view
               class="stat-item"
               @click.stop="router.push({ name: 'focuse' })">
               <view class="stat-number">{{
                  statusnum.myFocus == 0 ? '0' : statusnum.myFocus
               }}</view>
               <view class="stat-label">我关注的</view>
            </view>
            <view
               class="stat-item"
               @click.stop="router.push({ name: 'reply' })">
               <view class="stat-number">{{
                  statusnum.myReply == 0 ? '0' : statusnum.myReply
               }}</view>
               <view class="stat-label">回复我的</view>
            </view>
            <view
               class="stat-item"
               @click.stop="router.push({ name: 'acceptable' })">
               <view class="stat-number">{{
                  statusnum.myLike == 0 ? '0' : statusnum.myLike
               }}</view>
               <view class="stat-label">认同我的</view>
            </view>
         </view>
      </view>

      <!-- 系统设置和客服帮助 -->
      <view class="section-container">
         <view class="card-section">
            <view class="cell-border">
               <wd-cell
                  title="用户信息"
                  icon="user"
                  :show-arrow="true"
                  is-link
                  class="cell-item" />
            </view>

            <view class="cell-border">
               <wd-cell
                  title="客服帮助"
                  icon="help-circle"
                  :show-arrow="true"
                  class="cell-item"
                  is-link />
            </view>

            <view class="cell-border">
               <wd-cell
                  title="关于我们"
                  icon="info-circle"
                  is-link
                  :show-arrow="true"
                  class="cell-item" />
            </view>
            <view class="cell-border" @click="router.push({ name: 'userlst' })">
               <wd-cell
                  title="切换用户"
                  icon="history"
                  is-link
                  :show-arrow="true"
                  class="cell-item" />
            </view>

            <view class="cell-border" @click="handleLogout">
               <wd-cell
                  title="退出登录"
                  icon="logout"
                  :show-arrow="true"
                  is-link
                  class="cell-item" />
            </view>

            <wd-cell
               title="隐私政策"
               icon="laptop"
               :show-arrow="true"
               class="cell-item"
               is-link />
         </view>
      </view>
      <tn-update-user-info-popup
         confirm-bg-color="#ffe088"
         confirm-text-color="#000"
         v-model:show="showPopup"
         v-model:nickname="UserEiditFrom.username"
         v-model:avatar="UserEiditFrom.avatarUrl"
         @choose-avatar="avatarChooseHandle"
         @confirm="exiditUserInfo" />
   </view>
</template>

<script setup lang="ts">
import { useRouter } from 'uni-mini-router';
import { useStore } from '@/store/user';
import { sockeStore } from '@/store/socke';
import { UserEdit, UserIsLogin, UserLogout, UserGetInfo } from '@/api/user';
import { YoUViewStatistics } from '@/api/system';
import { onShow } from '@dcloudio/uni-app';
import TnUpdateUserInfoPopup from 'tnuiv3p-tn-update-user-info-popup/index.vue';
import { reactive, watch } from 'vue';
import { updateOssFile } from '@/api/file';
const router = useRouter();
const userStore = useStore();
const { SocketTask } = sockeStore();
const { getEiditUser, setUserInfo, setTokens } = userStore;
//抽屉
const showPopup = ref<boolean>(false);
const userInfo = ref({ ...useStore.userInfo });
const UserEiditFrom = reactive({
   avatarUrl: '',
   username: ''
});
const statusnum = ref({
   myFocus: 0,
   myReply: 0,
   myLike: 0
});

watch(
   () => userStore.userInfo,
   (newVal, oldVal) => {
      userInfo.value = newVal;
      UserEiditFrom.avatarUrl = newVal.avatarUrl || '';
      UserEiditFrom.username = newVal.username || '';
   },
   {
      immediate: true
   }
);

// 头像选择事件
const avatarChooseHandle = async (url: string) => {
   // 换成自己的上传接口
   const res = await updateOssFile(url);

   const { code, data } = JSON.parse(res.data);
   if (code == 0) {
      UserEiditFrom.avatarUrl = data;
   }
};

const exiditUserInfo = async () => {
   let res = await UserEdit(getEiditUser(UserEiditFrom));
   if (res.code == 0) {
      // 重新获取用户信息存储
      let res = await UserGetInfo();
      if (res.code == 0) {
         setUserInfo(res.data);
         setTokens(res.data);
      }
   }
};

// 退出登录处理
const handleLogout = () => {
   // 显示确认弹窗
   uni.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: res => {
         if (res.confirm) {
            //端口 websoke

            UserLogout().then(res => {
               if (res.code == 0) {
                  userStore.setUserInfo({});
                  userStore.ColseTokenDefale();
                  SocketTask.close();
                  uni.showToast({
                     title: '退出成功',
                     icon: 'none'
                  });
                  router.replaceAll({ name: 'login' });
               }
            });
            return;
         }
      }
   });
};

onShow(async () => {
   let res = await YoUViewStatistics();
   if (res.code === 0) {
      statusnum.value = res.data;
   }
});
</script>

<style lang="scss" scoped>
// 页面基础样式
.page {
   overflow: hidden;
   padding-top: var(--status-bar-height); // 状态栏
   padding-bottom: env(safe-area-inset-bottom); // 底部安全区
}

// 页面头部样式
.page-header {
   background: var(--quyou-nav-bg-color);
   padding: 28rpx;
   position: relative;
   padding-bottom: 100rpx;
   border-radius: 0 0 30rpx 30rpx;
}

// 用户信息区域
.user-info-container {
   display: flex;

   justify-content: space-between;
   position: relative;
   z-index: 10;
}

.user-main-info {
   display: flex;
   align-items: center;
}

.avatar-container {
   position: relative;
}

.user-avatar {
   width: 150rpx;
   height: 150rpx;
   border: 16rpx solid rgba(255, 255, 255, 0.3);
   border-radius: 50%;
   object-fit: cover;
   position: relative;
}

.camera-icon {
   position: absolute;
   bottom: 10rpx;
   right: 10rpx;
   font-size: 18rpx;
   width: 30rpx;
   height: 30rpx;
   border: 5rpx solid white;
   background: rgba(0, 0, 0, 0.5);
   display: flex;
   justify-content: center;
   border-radius: 50%;
   align-items: center;
   color: white;
}

.user-text-info {
   margin-left: 25rpx;
   display: flex;
   flex-direction: column;

   font-size: 22rpx;
   justify-content: center;
   font-weight: bold;
   height: 100%;
}

.username {
   font-size: 24rpx;
   margin-bottom: 15rpx;
   color: #303030;
}

.user-email {
   font-size: 22rpx;
   margin-top: 4rpx;
   color: #6f6c65;
}

// 查看按钮
.view-button {
   display: flex;
   align-items: center;
   color: #b8b1b0;
}

.arrow-icon {
   margin-left: 4rpx;
}

// 统计数据区域
.stats-container {
   position: absolute;
   bottom: -50rpx;
   left: 50%;
   width: 90%;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
   transform: translateX(-50%);
   display: flex;
   justify-content: space-between;
   margin-top: 20rpx;
   background: #fff;
   color: black;
   padding: 30rpx 80rpx;

   border-radius: 15rpx;
}

.stat-item {
   text-align: center;
}

.stat-number {
   font-size: 28rpx;
   font-weight: bold;
}

.stat-label {
   font-size: 22rpx;
   margin-top: 4rpx;
}

// 内容区块容器
.section-container {
   margin-top: 100rpx;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
   padding: 0 30rpx;
}

.card-section {
   background: white;
   border-radius: 16rpx;
   margin-top: 8rpx;
   overflow: hidden;
   box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
   transition: all 0.3s ease;
}

.cell-border {
   border-bottom: 4rpx solid #f5f5f5;
}

// 单元格样式
.cell-item::after {
   border: none !important;
}

.cell-item .wd-cell__title {
   font-size: 15px;
}

.cell-item .wd-cell__icon {
   color: #666;
}

// 角标样式
.badge {
   font-size: 12rpx;
   background-color: #fef2f2;
   color: #ef4444;
   padding: 2rpx 6rpx;
   border-radius: 10rpx;
}

.activity-badge {
   font-size: 12rpx;
   display: flex;
   align-items: center;
}

.activity-dot {
   color: #ef4444;
}

.activity-arrow {
   margin-left: 8rpx;
}
:deep() {
   .tn-popup.is-visible .tn-popup__content {
      bottom: 100rpx;
   }
   .tn-update-user-info-popup__avatar--image {
      width: 100% !important;
      height: 100% !important;
   }
}
</style>
