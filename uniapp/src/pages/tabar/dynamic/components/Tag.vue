<!-- 使用页面滚动示例 -->
<template>
   <!-- 此时使用了页面的滚动，z-paging不需要有确定的高度，use-page-scroll需要设置为true -->
   <ScrollView @srcbut="srcbut" @srctop="srctop">
      <view class="item" v-for="(item, index) in dataList" :key="index">
         <DynamicCart :dynamic="item"></DynamicCart>
      </view>

      <template v-if="dataList.length == 0">
         <NoData></NoData>
      </template>
   </ScrollView>
</template>

<script lang="ts" setup>
import NoData from '@/components/no-data/index.vue';
import { focusdrslst } from '@/api/focus';
import { throttle } from '@/util/index';
import { nextTick, reactive, watch, ref, onMounted } from 'vue';
import ScrollView from '@/components/scroll-view/index.vue';
import DynamicCart from '@/components/dynamic-cart/index.vue';
import { onShow } from '@dcloudio/uni-app';

const dataList = ref([]);
const from = ref({
   page: 1,
   pageSize: 10
});
//是否加载完成
const loding = ref<boolean>(false);
//是否能加载
const IsCanLoad = ref<boolean>(true);

const queryList = fu => {
   focusdrslst({
      page: from.value.page,
      pageSize: from.value.pageSize,
      sach: ''
   })
      .then(res => {
         // 模拟请求成功
         if (res.code === 0) {
            console.log(res.data.length);

            if (res.data.length !== from.value.pageSize) {
               //没有下一页了
               dataList.value = [...dataList.value, ...res.data];
               loding.value = true;
               fu(true);
            } else {
               //有下一页
               dataList.value = [...dataList.value, ...res.data];
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
   focusdrslst({
      page: from.value.page,
      pageSize: from.value.pageSize,
      sach: ''
   })
      .then(res => {
         // 模拟请求成功
         if (res.code === 0) {
            if (res.data.length !== from.value.pageSize) {
               //没有下一页了
               dataList.value = res.data;
               loding.value = true;
            } else {
               //有下一页
               dataList.value = res.data;
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
   queryList();
});

// 其他省略
</script>
