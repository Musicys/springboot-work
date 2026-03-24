<template>
   <div class="common-layout">
      <div class=""></div>
      <el-container>
         <el-aside><Muen @setRouter="setRouter"></Muen></el-aside>
         <el-container>
            <el-header> <Footer></Footer></el-header>
            <el-main
               ><Son v-if="isShowChildren" :paths="paths" :children="children" :isShowChildren="isShowChildren" />
               <Main></Main
            ></el-main>
         </el-container>
      </el-container>
   </div>
</template>
<script setup lang="ts">
import Footer from './components/Footer.vue';
import Main from './components/Main.vue';
import Muen from './components/Muen.vue';
import Son from '@/components/Muen/Son.vue';

import { onMounted } from 'vue';
import { useTabor } from 'vue3-tabor';
const tabor = useTabor();
const paths = ref();
const isShowChildren = ref(false);
const children = ref();
const setRouter = data => {
   paths.value = data.paths;
   children.value = data.children;
   isShowChildren.value = data.isShowChildren;
};
onMounted(() => {
   tabor.closeOthers();
   console.log('初始化完成');
});
</script>

<style lang="scss" scoped>
.common-layout {
   width: 100vw;
   height: 100vh;
}

:deep(.el-container) {
   height: 100%;
}

:deep() {
   .el-breadcrumb__inner {
      color: #fff !important;
   }
   .el-header {
      padding: 0 !important;
   }
   .el-menu {
      border-right: none !important;
   }
   .el-aside {
      background: #545c64;
      width: 160px !important;
      transition: all 0.3;
   }
   .el-main {
      background: #e4e6e9;
      padding: 0 !important;
      display: flex;
      width: 100%;
      height: 100%;
   }
   .el-menu-item-group__title {
      color: white !important;
      font-size: 13px;
      margin-top: 10px;
      padding: 10px 0px;
   }
}
</style>
