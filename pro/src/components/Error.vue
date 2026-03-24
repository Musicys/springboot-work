<!-- src/components/FullScreenLoading.vue -->
<template>
   <div class="full-screen-loading">
      <el-empty description="出错了...">
         <template #image>
            <svg
               width="80"
               height="80"
               viewBox="0 0 1024 1024"
               xmlns="http://www.w3.org/2000/svg"
               style="color: #409eff">
               <path
                  d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"
                  fill="currentColor" />
               <path
                  d="M512 128c205.4 0 372 166.6 372 372 0 37.8-5.6 74.4-16.4 109.2L720.4 462a8 8 0 0 0-11.2-11.2l-160 160a8 8 0 0 0 0 11.2l160 160a8 8 0 0 0 11.2-11.2L867.6 623.6C878.4 658.4 884 695 884 732.8c0 205.4-166.6 372-372 372-205.4 0-372-166.6-372-372S306.6 128 512 128z"
                  fill="currentColor" />
            </svg>
         </template>
      </el-empty>
   </div>
</template>

<script setup lang="ts">
defineProps({
   visible: {
      type: Boolean,
      default: false
   }
});
</script>

<style scoped lang="scss">
.full-screen-loading {
   position: fixed;
   top: 0;
   left: 0;
   width: 100vw;
   height: 100vh;
   background-color: #ffffff;
   z-index: 9999;
   display: flex;
   justify-content: center;
   align-items: center;
}
</style>
