<template>
   <view class="start-page-container">
      <view class="start-page-top-section">
         <image
            class="start-page-logo-image"
            src="https://ts1.tc.mm.bing.net/th/id/OIP-C.nzfFhotcNECcnN1Z4bxBWAAAAA?rs=1&pid=ImgDetMain&o=7&rm=3"
            mode="aspectFit"></image>
         <h1 class="start-page-title">南昌交通学院</h1>
         <text class="start-page-subtitle">培养交通人才，服务社会发展</text>
      </view>

      <view class="start-page-bottom-section">
         <wd-button
            type="success"
            class="start-page-email-register-button"
            @click="goLoginform('/pages/login/index')">
            <wd-icon
               name="mail"
               class="start-page-button-icon"
               size="16px"></wd-icon
            ><text class="start-page-button-text">邮箱注册</text>
         </wd-button>

         <wd-button
            type="warning"
            class="start-page-account-login-button"
            @click="goLoginform(2)">
            <wd-icon
               name="mail"
               class="start-page-button-icon"
               size="16px"></wd-icon
            ><text class="start-page-button-text">账号登录</text>
         </wd-button>

         <view class="start-page-agreement-container">
            <label class="start-page-agreement-label">
               <checkbox
                  class="start-page-agreement-checkbox"
                  :checked="Isyuedu"
                  @click="change" />
               <text class="start-page-agreement-text">
                  我已经阅读南昌交通学院的
                  <text class="start-page-agreement-link">《用户协议》</text>
                  和
                  <text class="start-page-agreement-link">《隐私策略》</text>
               </text>
            </label>
         </view>
      </view>
   </view>
   <wd-action-sheet v-model="show" title="提示" @close="close">
      <view class="start-page-modal-container">
         <h1 class="start-page-modal-title">请阅读以下条款</h1>
         <text class="start-page-modal-content">
            我已经阅读南昌交通学院的

            <text class="start-page-modal-link">《用户协议》</text>
            和
            <text class="start-page-modal-link">《隐私策略》</text>
         </text>

         <view class="start-page-modal-confirm-button" @click="gohede">
            同意并继续
         </view>
      </view>
   </wd-action-sheet>
</template>

<script lang="ts" setup>
import { useRouter } from 'uni-mini-router';
const router = useRouter();
const IslDing = ref(false);
const Isyuedu = ref(false);
const show = ref(false);
let selectpath = '';
onMounted(() => {});
const change = () => {
   Isyuedu.value = !Isyuedu.value;
};
const goLoginform = e => {
   if (Isyuedu.value) {
      router.push({ path: e });
   } else {
      show.value = true;
      selectpath = e;
   }
};

const gohede = () => {
   router.push({ path: selectpath });
};
</script>

<style lang="scss" scoped>
.start-page-container {
   display: flex;
   flex-direction: column;
   padding-top: var(--status-bar-height);
   padding-bottom: env(safe-area-inset-bottom);
   width: 100vw;
   height: 100vh;
   justify-content: space-between;
   background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.start-page-top-section {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: center;
   padding-top: 120rpx;
}

.start-page-logo-image {
   width: 200rpx;
   height: 200rpx;
   border-radius: 24rpx;
   box-shadow: 0 8rpx 24rpx rgba(8, 51, 216, 0.15);
   margin-bottom: 40rpx;
}

.start-page-title {
   font-size: 56rpx;
   font-weight: 700;
   color: #0833d8;
   margin-bottom: 16rpx;
   text-align: center;
   letter-spacing: 2rpx;
}

.start-page-subtitle {
   font-size: 28rpx;
   color: #666;
   text-align: center;
   line-height: 1.6;
   padding: 0 40rpx;
}

.start-page-bottom-section {
   display: flex;
   flex-direction: column;
   align-items: center;
   padding-bottom: 60rpx;
}

.start-page-email-register-button {
   background: #0833d8;
   border: none;
   width: 600rpx;
   margin-bottom: 24rpx;
   border-radius: 48rpx;
   height: 96rpx;
   color: white;
   font-weight: 600;
   font-size: 32rpx;
   box-shadow: 0 8rpx 24rpx rgba(8, 51, 216, 0.3);
   display: flex;
   justify-content: center;
   align-items: center;
   transition: all 0.3s ease;

   &:active {
      transform: scale(0.98);
      box-shadow: 0 4rpx 12rpx rgba(8, 51, 216, 0.2);
   }
}

.start-page-account-login-button {
   border: 2rpx solid #0833d8;
   background: white;
   width: 600rpx;
   margin-bottom: 32rpx;
   border-radius: 48rpx;
   height: 96rpx;
   color: #0833d8;
   font-weight: 600;
   font-size: 32rpx;
   display: flex;
   justify-content: center;
   align-items: center;
   transition: all 0.3s ease;

   &:active {
      transform: scale(0.98);
      background: #f5f8ff;
   }
}

.start-page-button-icon {
   margin-right: 12rpx;
}

.start-page-button-text {
   font-size: 32rpx;
}

.start-page-agreement-container {
   display: flex;
   justify-content: center;
   align-items: center;
   width: 600rpx;
}

.start-page-agreement-label {
   display: flex;
   align-items: flex-start;
   font-size: 24rpx;
   line-height: 1.8;
}

.start-page-agreement-checkbox {
   transform: scale(0.8);
   margin-top: 4rpx;
   margin-right: 8rpx;
}

.start-page-agreement-text {
   color: #999;
   font-size: 24rpx;
}

.start-page-agreement-link {
   color: #0833d8;
   font-weight: 500;
}

.start-page-modal-container {
   padding: 40rpx;
   width: 100%;
   display: flex;
   flex-direction: column;
   justify-content: space-between;
   padding-bottom: env(safe-area-inset-bottom);
   margin-bottom: 20rpx;
}

.start-page-modal-title {
   font-size: 40rpx;
   font-weight: 700;
   color: #333;
   margin-bottom: 32rpx;
   text-align: center;
}

.start-page-modal-content {
   font-size: 28rpx;
   color: #666;
   line-height: 1.8;
   text-align: center;
   margin-bottom: 40rpx;
}

.start-page-modal-link {
   color: #0833d8;
   font-weight: 500;
}

.start-page-modal-confirm-button {
   width: 100%;
   height: 88rpx;
   border-radius: 44rpx;
   font-size: 32rpx;
   font-weight: 600;
   display: flex;
   justify-content: center;
   align-items: center;
   background: #0833d8;
   border: none;
   color: white;
   box-shadow: 0 8rpx 24rpx rgba(8, 51, 216, 0.3);
   transition: all 0.3s ease;

   &:active {
      transform: scale(0.98);
      box-shadow: 0 4rpx 12rpx rgba(8, 51, 216, 0.2);
   }
}
</style>
