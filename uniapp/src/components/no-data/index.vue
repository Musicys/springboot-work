<template>
   <view class="no-data-container">
      <!-- SVG图标 -->
      <view class="icon-container">
         <image
            src="/src/static/data.png"
            mode="widthFix"
            class="no-data-icon" />
         <view class="text-container">
            <text class="no-data-text">暂无数据</text>
            <text v-if="subMessage" class="no-data-subtext">{{
               subMessage
            }}</text>
         </view>
      </view>

      <!-- 文本信息 -->
   </view>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped>
.no-data-container {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   padding: 60rpx 20rpx;
   min-height: 300rpx;
   text-align: center;

   .icon-container {
      margin-bottom: 24rpx;
      width: 200rpx;
      height: 200rpx;

      .no-data-icon {
         width: 100%;
         height: 100%;
      }
   }

   .text-container {
      margin-top: 10rpx;
      .no-data-text {
         display: block;
         font-size: 32rpx;
         color: #666666;
         line-height: 1.5;
      }

      .no-data-subtext {
         display: block;
         font-size: 32rpx;
         color: #666666;
         line-height: 1.5;
      }
   }

   .button-container {
      margin-top: 16rpx;
   }
}

// 适配不同尺寸的屏幕
@media (max-width: 375px) {
   .no-data-container {
      padding: 40rpx 20rpx;

      .icon-container {
         width: 160rpx;
         height: 160rpx;
      }
   }
}
</style>
