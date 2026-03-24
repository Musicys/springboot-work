<template>
   <view class="webview-container">
      <web-view :src="url"></web-view>
   </view>
</template>

<script setup lang="ts">
import { ref, onLoad } from 'vue';

const url = ref('');

onLoad((options: any) => {
   if (options.url) {
      url.value = decodeURIComponent(options.url);
   }
});
</script>

<style lang="scss" scoped>
.webview-container {
   width: 750rpx;
   height: 100vh;
}
</style>
