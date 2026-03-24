<template>
   <div class="init-loading-page">
      <div class="loading-spinner"></div>
      <p class="loading-text">系统初始化中，请稍候...</p>
   </div>
</template>

<style lang="scss" scoped>
.init-loading-page {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   height: 100vh;
   background-color: var(--el-bg-color-page, #ffffff);
}

.loading-spinner {
   width: 42px;
   height: 42px;
   border: 3px solid var(--el-border-color-light, #e4e7ed);
   border-top-color: var(--el-color-primary, #409eff);
   border-radius: 50%;
   animation: loading-rotate 1s linear infinite;
   margin-bottom: 16px;
}

.loading-text {
   margin: 0;
   font-size: 14px;
   color: var(--el-text-color-secondary, #909399);
   line-height: 1.5;
}

@keyframes loading-rotate {
   to {
      transform: rotate(360deg);
   }
}
</style>
