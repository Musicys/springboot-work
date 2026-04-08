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
   overflow-y: auto;
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
