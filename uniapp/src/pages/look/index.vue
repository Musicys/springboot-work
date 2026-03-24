<template>
   <view class="container">
      <image
         src="https://web-scoke.oss-cn-beijing.aliyuncs.com/2024/f34efb735c2441ceacfc4f0476016a751.png"></image>
      <view
         class="content-area"
         @touchstart="touchStart"
         @touchmove="touchMove"
         @touchend="touchEnd">
         <view
            class="text-block"
            v-for="(chunk, index) in textChunks"
            :key="index"
            :style="{ animationDelay: `${index * 0.2}s` }">
            {{ chunk }}
         </view>
      </view>
      <button class="buttom" @click="router.back()">返回</button>
   </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'uni-mini-router';
const router = useRouter();
// 🔸 精简为3句二次元短语（保留治愈感+画面感）
const words = [
   '樱花飘落的速度',
   '耳机分你一只',
   '此页面为空白',
   '页面还没开发'
];

// 无需分行，直接渲染单句（更简洁）
const textChunks: string[] = words;

const tabIndex = ref<number>(0);
let startX = 0;
let deltaX = 0;

// 保留原滑动逻辑（适配页面切换）
function touchStart(event: any) {
   startX = event.touches[0].clientX;
}

function touchMove(event: any) {
   const currentX = event.touches[0].clientX;
   deltaX = currentX - startX;
}

function touchEnd() {
   const minSwipeDistance = 50;
   if (deltaX > minSwipeDistance) {
      tabIndex.value = 1;
      console.log('→ 右滑：tabIndex = 1');
   } else if (deltaX < -minSwipeDistance) {
      tabIndex.value = 0;
      console.log('← 左滑：tabIndex = 0');
   }
   deltaX = 0;
}
</script>

<style scoped>
.container {
   width: 100vw;
   height: 100vh;
   background-color: rgb(120, 117, 114);
   overflow: hidden; /* 取消滚动，适配单屏展示 */
   box-sizing: border-box;
   display: flex;
   padding-top: 2em;

   height: 80vh;
   align-items: start; /* 垂直居中 */
   & > image {
      width: 100%;
      position: absolute;
      top: 0;
      left: 0;
      height: 100%;
      z-index: 0;
      opacity: 0.4;
   }
}

.content-area {
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   user-select: none;
   gap: 20rpx; /* 句子间距更舒展 */
}

.text-block {
   margin-left: 15rpx;

   font-size: 32rpx; /* 适度放大，提升视觉焦点 */
   color: #fff; /* 单一淡黑色，保持简洁 */
   font-weight: bold;
   line-height: 1.5;
   letter-spacing: 0.6px;
   font-family:
      'PingFang SC', 'HarmonyOS_Regular', 'Microsoft YaHei', sans-serif;
   opacity: 0; /* 初始透明，用于渐入动画 */
   transform: translateY(10rpx); /* 初始向下偏移 */
   /* 渐入+轻微上浮动画 */
   animation: fadeInUp 0.8s ease forwards;
   /* hover悬浮效果（适配小程序触摸反馈） */
   transition:
      transform 0.3s ease,
      opacity 0.3s ease;
}

/* 触摸时悬浮效果 */
.text-block:active {
   transform: translateY(-2rpx);
   opacity: 0.8;
}
.buttom {
   position: absolute;
   bottom: 120rpx;
   left: 50%;
   transform: translateX(-50%);
   width: 80%;
   background: #2e2c2c;
   color: white;
}

/* 渐入动画关键帧 */
@keyframes fadeInUp {
   from {
      opacity: 0;
      transform: translateY(10rpx);
   }
   to {
      opacity: 0.94;
      transform: translateY(0);
   }
}
</style>
