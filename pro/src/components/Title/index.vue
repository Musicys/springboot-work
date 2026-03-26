<template>
   <div>
      <div class="title">
         <div class="title_left">
            <ElIcon v-if="isBack">
               <Back style="cursor: pointer" @click="onBack(router)" />
            </ElIcon>
            <slot />
         </div>
         <div class="title_right">
            <slot name="right" />
         </div>
      </div>
      <ElDivider v-if="!isHidden" />
   </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const onBack = router => {
   router.back();
};

defineProps({
   isBack: {
      type: Boolean,
      default: false
   },
   isHidden: {
      type: Boolean,
      default: false
   }
});
const router = useRouter();
</script>

<style lang="scss" scoped>
.title {
   display: flex;
   justify-content: space-between;
   align-items: center;
   color: rgb(0 0 0 / 88%);
   font-weight: bold;
   font-size: 20px;

   &_left {
      display: flex;
      gap: 10px;
      align-items: center;
   }

   &_right {
      display: flex;
      gap: 10px;
      align-items: center;
   }
}
</style>
