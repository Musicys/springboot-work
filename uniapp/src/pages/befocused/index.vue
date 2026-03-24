<template>
   <z-paging
      ref="paging"
      :refresher-enabled="false"
      v-model="data"
      :use-safe-area-placeholder="true"
      @query="queryList">
      <template #top>
         <viwe class="sach">
            <view class="search-header">
               <view class="search-bar">
                  <view class="back-btn" @click.stop="router.back()">
                     <wd-icon name="thin-arrow-left" size="22px"></wd-icon>
                  </view>
                  <view class="search-input-container">
                     <uni-icons
                        type="search"
                        size="20"
                        color="#999"
                        class="search-icon"></uni-icons>

                     <input
                        type="search"
                        v-model="sach"
                        placeholder="搜索"
                        @change="handleSearchChange"
                        placeholder-class="search-placeholder"
                        @focus="showHistory = true"
                        class="search-input" />
                  </view>
                  <view class="search-btn" @click.stop="paging?.refresh()">
                     <text class="search-btn-text">搜索</text>
                  </view>
               </view>
            </view>
         </viwe>
      </template>
      <template #empty>
         <!-- <empty-view /> -->
         <NoData></NoData>
      </template>
      <view class="item" v-for="item in data" :key="item.id">
         <BefocusedCart :data="item"></BefocusedCart>
      </view>

      <template #bottom> </template>
   </z-paging>
</template>

<script setup lang="ts">
import { myFansLst } from '@/api/focus';
import { onMounted, reactive } from 'vue';
import NoData from '@/components/no-data/index.vue';
import BefocusedCart from './components/BefocusedCart.vue';
import { FocusUserVo } from '@/api/focus/model/type';
import { useRouter } from 'uni-mini-router';
import NavTop from '@/components/nav-top/index.vue';

const router = useRouter();
const paging = ref(null);
const sach = ref('');
const data = ref<FocusUserVo[]>();
const queryList = (page, pageSize) => {
   myFansLst({
      page,
      pageSize,
      sach: sach.value
   }).then(res => {
      if (res.code === 0) {
         paging.value.complete(res.data);
      }
   });
};
onMounted(() => {
   paging.value.refresh();
});
</script>

<style lang="scss" scoped>
.sach {
   padding-top: var(--status-bar-height); // 状态栏
}
/* 顶部搜索区域 */
.search-header {
   padding: 20rpx 30rpx;
   border-bottom: 1px solid #f0f0f0;
   background-color: #fff;
   position: sticky;
   top: var(--status-bar-height);
   z-index: 10;
}

.search-bar {
   display: flex;
   align-items: center;
   gap: 20rpx;
}

.back-btn {
   width: 50rpx;
   height: 50rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.search-input-container {
   flex: 1;
   height: 70rpx;
   background-color: #f5f5f5;
   border-radius: 35rpx;
   display: flex;
   align-items: center;
   padding: 0 30rpx;
}

.search-icon {
   margin-right: 15rpx;
}

.search-input {
   flex: 1;
   height: 100%;
   font-size: 28rpx;
   color: #333;

   background: transparent;
}

.search-placeholder {
   color: #999;
}

.search-btn {
   padding: 0 20rpx;
}

.search-btn-text {
   font-size: 30rpx;
   color: #ff69b4;
   font-weight: 500;
}
</style>
