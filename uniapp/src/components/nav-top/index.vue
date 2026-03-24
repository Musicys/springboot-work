<template>
   <!-- #ifdef MP-WEIXIN -->
   <view
      class="nav-placeholder"
      :style="{ height: `calc(var(--status-bar-height) + 44px)` }"></view>
   <!-- #endif -->

   <!-- #ifdef H5 -->
   <view style="margin-top: var(--quyou-nav-height); background: none"> </view>
   <!-- #endif -->

   <!-- #ifdef APP-PLUS -->
   <view
      class="nav-placeholder"
      style="height: 155rpx; background: var(--quyou-nav-bg-color)"></view>
   <!-- #endif -->
</template>
