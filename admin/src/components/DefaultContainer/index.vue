<template>
   <div class="main_container">
      <div class="main_content">
         <slot></slot>
      </div>
      <div class="company_info"></div>
   </div>
</template>

<script setup>
import { ElBreadcrumb, ElBreadcrumbItem } from 'element-plus';
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<style lang="scss" scoped>
.main_container {
   display: flex;
   flex-direction: column;
   row-gap: 16px;
   height: 100%;
   // padding: 20px 20px 0;
   background-color: #f7f8fa;

   .main_content {
      position: relative;
      display: flex;
      flex: 1;
      flex-direction: column;

      // overflow-y: auto;
      padding: 18px;
      border-radius: 10px;
      background-color: #fff;
   }

   // .company_info {
   //   height: 36px;
   //   color: rgb(0 0 0 / 45%);
   //   font-size: 12px;
   //   text-align: center;
   // }
}
</style>
