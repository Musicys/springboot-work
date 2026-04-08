<template>
   <div class="main-container">
      <!-- <vue-tabor /> -->
      <transition name="fade" mode="out-in">
         <router-view />
      </transition>
   </div>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped>
.main-container {
   width: 100%;
   height: 100%;
   overflow: auto; /* 或 scroll，确保允许滚动 */

   /* 针对 Firefox */
   scrollbar-width: none;

   /* 针对 IE 10+ */
   -ms-overflow-style: none;
}

/* 针对 Chrome, Safari, Edge (WebKit/Blink 内核) */
.main-container::-webkit-scrollbar {
   display: none;
}
// 路由过渡动画
.fade-enter-active,
.fade-leave-active {
   transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
   opacity: 0;
}
</style>
