<!-- 使用页面滚动示例 -->
<template>
   <!-- 此时使用了页面的滚动，z-paging不需要有确定的高度，use-page-scroll需要设置为true -->
   <ScrollView @srcbut="srcbut" @srctop="srctop">
      <view class="item" v-for="(item, index) in dataList" :key="index">
         <HomeUserCart :data="item"></HomeUserCart>
      </view>

      <template v-if="dataList.length == 0">
         <NoData></NoData>
      </template>
   </ScrollView>
</template>

<script lang="ts" setup>
import { UserListLongitude } from '@/api/system';
import NoData from '@/components/no-data/index.vue';
import HomeUserCart from '@/components/home-user-cart/index.vue';
import { useStore } from '@/store/user';
import { throttle } from '@/util/index';
import { nextTick, reactive, watch, ref, onMounted } from 'vue';
import ScrollView from '@/components/scroll-view/index.vue';

const store = useStore();
const dataList = ref([]);
const props = defineProps<{
   data: any;
}>();
const from = ref({
   page: 1,
   pageSize: 10
});
//是否加载完成
const loding = ref<boolean>(false);
//是否能加载
const IsCanLoad = ref<boolean>(true);

const queryList = fu => {
   UserListLongitude({
      page: from.value.page,
      pageSize: from.value.pageSize,
      lat: store.userInfo.lat,
      lng: store.userInfo.lng,
      ...from.value,
      ...props.data
   })
      .then(res => {
         if (res.code == 0) {
            console.log(res.data.userVOS.length);

            if (res.data.userVOS.length !== from.value.pageSize) {
               //没有下一页了
               dataList.value = [...dataList.value, ...res.data.userVOS];
               loding.value = true;
               fu(true);
            } else {
               //有下一页
               dataList.value = [...dataList.value, ...res.data.userVOS];
               from.value.page = from.value.page + 1;
               fu(false);
            }
         } else {
            //加载失败
            IsCanLoad.value = false;
         }
      })
      .finally(() => {});
};
const onLoadList = fu => {
   from.value.page = 1;
   loding.value = false;
   UserListLongitude({
      page: from.value.page,
      pageSize: from.value.pageSize,
      lat: store.userInfo.lat,
      lng: store.userInfo.lng,
      ...from.value
   })
      .then(res => {
         if (res.code == 0) {
            if (res.data.userVOS.length !== from.value.pageSize) {
               //没有下一页了
               dataList.value = res.data.userVOS;
               loding.value = true;
            } else {
               //有下一页
               dataList.value = res.data.userVOS;
               from.value.page = from.value.page + 1;
            }

            fu();
         } else {
            //加载失败
            IsCanLoad.value = false;
         }
      })
      .finally(() => {});
};

watch(
   () => props.data,
   newVal => {
      from.value = { ...from.value, ...newVal };
   },
   { immediate: true }
);
// 类似mixins，如果是页面滚动务必要写这一行，并传入当前ref绑定的paging，注意此处是paging，而非paging.value
const srctop = fn => {
   onLoadList(fn);
};

const srcbut = fn => {
   if (loding.value) {
      return fn(true);
   } else {
      queryList(fn);
   }
};
onMounted(() => {
   queryList(() => {});
});
// 其他省略
</script>
