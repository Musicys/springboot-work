<template>
   <wd-tabs v-model="tab" swipeable sticky>
      <block v-for="item in tabs" :key="item.value">
         <wd-tab :title="`${item.title}`" :name="item.value">
            <view class="content" v-if="item.value == '0'">
               <Dynamic
                  v-if="tab == 0"
                  :showpadding="false"
                  :data="props.data"></Dynamic> </view
            ><view class="content" v-if="item.value == '1'">
               <Alluser
                  v-if="tab == 1"
                  :data="{ sach: props.data }"
                  :showpadding="false"></Alluser>
            </view>
            <view class="content" v-if="item.value == '2'"> </view>
         </wd-tab>
      </block>
   </wd-tabs>
</template>

<script setup lang="ts">
import Alluser from '@/components/home-list/Alluser.vue';
import Dicuser from '@/components/home-list/Dicuser.vue';
import Dynamic from '@/pages/tabar/dynamic/components/Dynamic.vue';
const tabs = ref([
   { title: '综合', value: '0' },
   { title: '用户', value: '1' },
   { title: '趣味群', value: '2' }
]);
const tab = ref(0);
const props = defineProps({
   data: {
      type: String,
      default: () => {
         return '';
      }
   }
});
const sach = ref('');
watch(
   () => props.data,
   newVal => {
      sach.value = newVal;
   }
);
onMounted(() => {
   console.log(props.data);

   sach.value = props.data;
});
</script>

<style lang="scss" scoped></style>
