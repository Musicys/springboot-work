<template>
   <view class="empty-list">
      <button class="custom-shadow" @click="emit('go')">
         {{ props.buttonText }}
      </button>
      <text class="empty-text">{{ props.text }}</text>
      <text class="empty-subtext">{{ props.subtext }}</text>
   </view>
</template>

<script setup lang="ts">
const props = defineProps({
   text: {
      type: String,
      default: '暂无好友'
   },
   subtext: {
      type: String,
      default: '快去添加新好友吧~'
   },
   buttonText: {
      type: String,
      default: '去寻找好友'
   }
});
const emit = defineEmits(['go']);
</script>

<style lang="scss" scoped>
.empty-list {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: center;
   height: 500rpx;
   color: #999;
}

.empty-icon {
   font-size: 120rpx;
   margin-bottom: 20rpx;
}

.empty-text {
   font-size: 32rpx;
   margin-bottom: 10rpx;
}

.empty-subtext {
   font-size: 26rpx;
   color: #bbb;
}
.custom-shadow {
   background-color: #a6b0b9;
   color: #fff;
   padding: 5rpx 40rpx;
   border-radius: 20rpx;
   font-size: 28rpx;
   font-weight: 600;
   margin-bottom: 15rpx;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}
</style>
