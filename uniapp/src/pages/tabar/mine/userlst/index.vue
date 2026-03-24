<template>
   <view class="page">
      <view class="nav-bar">
         <view class="back-btn" @click="navigateBack">
            <wd-icon name="thin-arrow-left"></wd-icon>
         </view>
         <view class="nav-title">切换账号</view>
         <view class="empty"></view>
      </view>

      <view class="content">
         <view class="title">轻触头像以切换账号</view>

         <view class="user-list">
            <view
               v-for="user in userList"
               :key="user.id"
               class="user-item"
               @click="switchUser(user)">
               <view class="avatar-container">
                  <image
                     :src="
                        user.avatarUrl
                           ? user.avatarUrl.trim()
                           : 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'
                     "
                     mode="aspectFill"
                     class="avatar"></image>
                  <view v-if="isCurrentUser(user)" class="current-indicator">
                     <wd-icon name="check" size="16" color="#fff"></wd-icon>
                  </view>
               </view>
               <view class="username">{{
                  user.username || user.userAccount
               }}</view>
               <view v-if="isCurrentUser(user)" class="current-tag"
                  >当前使用</view
               >
            </view>

            <!-- 添加账号按钮 -->
            <view class="add-account-btn" @click="addAccount">
               <view class="avatar-container add-avatar">
                  <wd-icon name="add1" size="44px"></wd-icon>
               </view>
               <view class="add-text">添加账号</view>
            </view>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { useStore } from '@/store/user';
import { UserLogin, UserRegister, UserLogout } from '@/api/user';
import { useRouter } from 'uni-mini-router';
import { sockeStore } from '@/store/socke';

const webstore = sockeStore();
const store = useStore();
const router = useRouter();
// 获取用户列表
const userList = store.Tokens || [];
// 检查是否为当前使用的用户
const isCurrentUser = (user: any) => {
   // 优先使用is_default字段判断，确保只有一个用户显示为当前使用
   return user.is_default === 1;
};
// 切换用户
const switchUser = async (user: any) => {
   // 如果点击的是当前用户，不执行操作
   if (isCurrentUser(user)) {
      return;
   }
   await UserLogout();

   // 调用登录接口切换用户
   UserLogin(user.tokens)
      .then(res => {
         if (res.code === 0) {
            // 更新用户信息和令牌

            store.setUserInfo(res.data);
            store.setLocation();
            store.setTokens({
               ...res.data,
               tokens: user.tokens,
               is_default: 1
            });
            if (webstore.$state.SocketTask) {
               webstore.$state.SocketTask.close(); //关闭连接
            }

            setTimeout(() => {
               webstore.websocke(res.data.id);
            }, 1000);
            // 显示登录成功提示
            uni.showToast({
               title: '切换成功',
               icon: 'none',
               duration: 500,
               success: () => {
                  // 切换成功后返回上一页
                  setTimeout(() => {
                     router.pushTab({ name: 'tabar' });
                  }, 1000);
               }
            });
         } else {
            // 显示切换失败提示
            uni.showToast({
               title: '切换失败，请重试',
               icon: 'none'
            });
         }
      })
      .catch(() => {});
};

// 返回上一页
const navigateBack = () => {
   router.back();
};

// 添加账号
const addAccount = () => {
   router.replace({
      path: '/pages/login/index'
   });
};
</script>

<style lang="scss" scoped>
.page {
   height: 100vh;
   background-color: #f8f8f8;
   padding-top: var(--status-bar-height);
}

.nav-bar {
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: 44px;
   background-color: #fff;
   padding: 0 20rpx;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.back-btn {
   width: 40rpx;
   height: 40rpx;
   display: flex;
   align-items: center;

   justify-content: center;
}

.nav-title {
   font-size: 36rpx;
   font-weight: 600;
   color: #333;
}

.empty {
   width: 40rpx;
}

.content {
   padding: 40rpx 30rpx;
}

.title {
   font-size: 32rpx;
   color: #666;
   text-align: center;
   margin-bottom: 40rpx;
}

.user-list {
   display: grid;
   grid-template-columns: repeat(2, 1fr);
   gap: 30rpx;
}

.user-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   padding: 20rpx 10rpx;
   transition: all 0.3s ease;
}

.user-item:active {
   transform: scale(0.95);
}

.avatar-container {
   position: relative;
   width: 160rpx;
   height: 160rpx;
   margin-bottom: 15rpx;
   border-radius: 50%;
   overflow: hidden;
   // 头像 hover 效果
   display: flex;
   justify-content: center;
   align-items: center;
   color: #0bdaee;
   border: 3rpx solid transparent;
   transition: border-color 0.3s ease;
}

.user-item:active .avatar-container {
   border-color: #0bdaee;
}

.avatar {
   width: 100%;
   height: 100%;
   border-radius: 50%;
}

.current-indicator {
   position: absolute;
   bottom: 0;
   right: 0;
   width: 40rpx;
   height: 40rpx;
   background-color: #0bdaee;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   border: 2rpx solid #fff;
}

.username {
   font-size: 28rpx;
   color: #333;
   text-align: center;
   margin-bottom: 10rpx;
   max-width: 140rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.current-tag {
   font-size: 24rpx;
   color: #0bdaee;
   background-color: rgba(11, 218, 238, 0.1);
   padding: 4rpx 16rpx;
   border-radius: 20rpx;
}

.add-account-btn {
   display: flex;
   flex-direction: column;
   align-items: center;
   padding: 20rpx 10rpx;
   transition: all 0.3s ease;
}

.add-account-btn:active {
   transform: scale(0.95);
}

.add-avatar {
   background-color: #f0f0f0;
   border: 2rpx dashed #ddd;
}

.add-text {
   font-size: 28rpx;
   color: #666;
   text-align: center;
   margin-top: 15rpx;
}
</style>
