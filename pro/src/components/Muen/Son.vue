<template>
   <div class="son-menu">
      <div v-for="(item, index) in children" :key="index" class="menu-item-wrapper">
         <div
            class="son-menu-item"
            :class="{ 'is-active': isActive(item), 'has-children': !!item.meta.tabConfig?.isExpand }"
            @click="handleClick(item)">
            <span
               v-if="!!item.meta.tabConfig?.isExpand"
               class="expand-icon"
               :class="{ rotated: expandedItems[item.path] }">
               <el-icon><ArrowRight /></el-icon>
            </span>
            <span>{{ item?.meta?.tabConfig?.name }}</span>
         </div>

         <!-- 子菜单 -->
         <transition name="slide-down">
            <div v-if="expandedItems[item.path] && item.children" class="sub-menu">
               <div
                  v-for="(child, childIndex) in item.children"
                  :key="childIndex"
                  class="sub-menu-item"
                  :class="{ 'is-active': isActiveChild(item, child) }"
                  @click.stop="goToRoute(`${paths}${item.path}/${child.path}`)">
                  <span>{{ child?.meta?.tabConfig?.name }}</span>
               </div>
            </div>
         </transition>
      </div>
   </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const props = defineProps<{
   children: {
      path: string;
      meta: {
         icon?: string;
         setup?: boolean;
         tabConfig: {
            name: string;
            isExpand?: boolean;
         };
         children?: {
            path: string;
            meta: {
               tabConfig: {
                  name: string;
               };
            };
         }[];
      };
      children?: any[];
      paths: string;
   }[];
   paths: string;
}>();

const routers = useRouter();
const route = useRoute();
const expandedItems = ref<Record<string, boolean>>({});

const handleClick = (item: any) => {
   const bol = !!item.meta.tabConfig?.isExpand;
   if (bol) {
      // 切换展开状态
      expandedItems.value[item.path] = !expandedItems.value[item.path];
   } else {
      // 跳转路由
      goToRoute(`${props.paths}${item.path}`);
   }
};

const goToRoute = (url: string) => {
   routers.push(url);
};

const isActive = (item: any) => {
   console.log(route.path, `/${props.paths}${item.path}`);

   return route.path === `${props.paths}${item.path}`;
};

const isActiveChild = (parent: any, child: any) => {
   return route.path === `${props.paths}${parent.path}/${child.path}`;
};

// 初始化时展开所有带有 isExpand 属性的菜单项
onMounted(() => {
   console.log('chilSondren', props.children);

   // 默认展开所有带有 isExpand 属性的菜单项
   props.children.forEach(item => {
      const bol = !!item.meta.tabConfig?.isExpand;
      if (bol) {
         expandedItems.value[item.path] = true;
      }
   });
});
</script>

<style lang="scss" scoped>
.son-menu {
   background: white;
   border-radius: 8px;
   width: 120px;
   font-size: 12px;
   height: 100%;
   padding: 10px 0;
}

.menu-item-wrapper {
   width: 100%;
   display: flex;
   flex-direction: column;
}

.son-menu-item {
   height: 40px;
   position: relative;
   line-height: 40px;
   margin: 2px 0;
   // padding: 0 15px;
   padding-left: 25px;
   cursor: pointer;
   color: #000000;
   transition: all 0.2s ease;
   display: flex;
   justify-content: space-between;
   align-items: center;
   width: 100%;

   &:hover {
      background: rgba(255, 255, 255, 0.1);
   }

   &.is-active {
      background: #f5f5f5;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1);

      span {
         font-weight: 600;
         letter-spacing: 0.5px;
      }
   }

   &.has-children {
      position: relative;
   }
}

.expand-icon {
   font-size: 14px;
   transition: transform 0.3s ease;
   display: inline-block;
   left: 8px;
   width: 16px;
   height: 16px;
   position: absolute;
   display: flex;
   align-items: center;
   justify-content: center;
}

.expand-icon.rotated {
   transform: rotate(90deg);
}
.sub-menu {
   // margin-left: 20px;
   // padding: 0 15px;
}
.sub-menu-item {
   height: 36px;
   padding-left: 25px;
   line-height: 36px;
   // margin: 1px 0;
   width: 100%;
   cursor: pointer;
   color: #000000;
   transition: all 0.2s ease;

   &:hover {
      background: rgba(255, 255, 255, 0.1);
   }

   &.is-active {
      background: #f5f5f5;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1);

      span {
         font-weight: 600;
         letter-spacing: 0.5px;
      }
   }
}

/* 展开/收起动画 */
.slide-down-enter-active,
.slide-down-leave-active {
   transition: all 0.3s ease;
   overflow: hidden;
}

.slide-down-enter-from {
   opacity: 0;
   transform: translateY(-10px);
   max-height: 0;
}

.slide-down-enter-to {
   opacity: 1;
   transform: translateY(0);
   max-height: 500px;
}

.slide-down-leave-from {
   opacity: 1;
   transform: translateY(0);
   max-height: 500px;
}

.slide-down-leave-to {
   opacity: 0;
   transform: translateY(-10px);
   max-height: 0;
}
</style>
