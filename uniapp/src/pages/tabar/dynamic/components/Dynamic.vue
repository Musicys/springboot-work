<template>
   <!-- 此时使用了页面的滚动，z-paging不需要有确定的高度，use-page-scroll需要设置为true -->
   <ScrollView @srcbut="srcbut" @srctop="srctop" :showpadding="showpadding">
      <view class="item">
         <view class="top">
            <view class="top-left">推荐分区</view>
            <view class="top-right">更多<tn-icon name="right"></tn-icon></view>
         </view>
         <view class="content">
            <view
               class="content-box"
               v-for="(item, index) in datatag"
               :key="index"
               @click="srcbut(item.id)">
               <wd-img
                  :src="item.url"
                  width="100rpx"
                  height="100rpx"
                  radius="50">
                  <template #error>
                     <wd-skeleton theme="avatar" />
                  </template>
                  <template #loading>
                     <wd-skeleton theme="avatar" />
                  </template>
               </wd-img>
               <view class="name">{{ item.categoryname }}</view>
            </view>
         </view>
      </view>
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
import { getDynamicList } from '@/api/dynamic';
import { useStore } from '@/store/user';
import { throttle } from '@/util/index';
import { nextTick, reactive, watch, ref, onMounted } from 'vue';
import ScrollView from '@/components/scroll-view/index.vue';
import DynamicCart from '@/components/dynamic-cart/index.vue';
import { DynamciTagList } from '@/api/dynamictag';
import { TagType } from '@/api/dynamictag/model/type';
const store = useStore();
const dataList = ref([]);
//默认值
const props = defineProps({
   data: {
      type: String,
      default: () => {
         return '';
      }
   },
   showpadding: {
      type: Boolean,
      default: () => {
         return true;
      }
   }
});

const from = ref({
   page: 1,
   pageSize: 10,
   sach: props.data || ''
});
//是否加载完成
const loding = ref<boolean>(false);
//是否能加载
const IsCanLoad = ref<boolean>(true);
const datatag = ref<TagType[]>([]);
const setDataTag = async () => {
   const res = await DynamciTagList({
      page: 1,
      pageSize: 5
   });
   if (res.code === 0) {
      datatag.value = res.data.records;
   }
};

const queryList = fu => {
   getDynamicList({
      page: from.value.page,
      pageSize: from.value.pageSize,
      sach: from.value.sach,
      lat: store.userInfo.lat || 0,
      lng: store.userInfo.lng || 0,
      province: ''
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
   getDynamicList({
      page: from.value.page,
      pageSize: from.value.pageSize,
      lat: store.userInfo.lat || 0,
      lng: store.userInfo.lng || 0,
      province: '',
      sach: from.value.sach || ''
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
   queryList(() => {});
   setDataTag();
});
// 其他省略
</script>
<style scoped lang="scss">
/* 推荐频道容器样式 */
.item {
   padding: 20rpx;

   margin-bottom: 10rpx;
}

/* 顶部标题栏样式 */
.top {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 20rpx;
   padding: 0 10rpx;
}

/* 左侧标题 */
.top-left {
   font-size: 32rpx;
   font-weight: bold;
   color: #333333;
}

/* 右侧更多按钮 */
.top-right {
   display: flex;
   align-items: center;
   font-size: 28rpx;
   color: #666666;
}

/* 箭头图标样式 */
.top-right tn-icon {
   margin-left: 6rpx;
   width: 24rpx;
   height: 24rpx;
}

/* 频道内容区域 - 弹性盒布局 */
.content {
   display: flex;
   justify-content: space-between;
}

/* 频道标题样式 */
.content .name {
   font-size: 26rpx;
   color: #333333;
   text-align: center;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
   width: 100%;
}

/* 频道列表容器 - 横向排列5个 */
.item > view:nth-child(2) {
   display: flex;
   flex-wrap: wrap;
}
</style>
