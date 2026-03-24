<template>
   <view :style="`margin-right: 10rpx; margin-left: 20rpx; height: ${heght}px`">
      <swiper
         :current="swiperIndex"
         style="height: 100%"
         @change="swiperChange"
         :indicator-dots="true">
         <swiper-item v-for="(items, index) in emojData" :key="index">
            <view class="emoj-container">
               <view class="emoj-row">
                  <view
                     class="emoj-parent"
                     v-for="(item, i) in items"
                     :key="i"
                     hover-class="checkActive"
                     hover-stay-time="100"
                     @click="tuchEmoj(item)">
                     <text class="emoj-conn">{{ item }}</text>
                  </view>
               </view>
            </view>
         </swiper-item>
      </swiper>
   </view>
</template>

<script setup>
import { h, onMounted, ref } from 'vue';
const prop = defineProps({
   height: {
      type: Number,
      default: 270
   }
});
const heght = ref(prop.height);

// #ifdef APP-PLUS

onMounted(() => {});
// #endif

// 定义emoji数据
const emojData = [
   [
      '😁',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😆'
   ],
   [
      '😁',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😆'
   ],
   [
      '😁',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😆'
   ],
   [
      '😁',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😆'
   ],
   [
      '😁',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😀',
      '😃',
      '😄',
      '😁',
      '😆',
      '😆'
   ]
];

// 定义响应式数据
const swiperIndex = ref(0); // 初始 swiper 索引
const selectedEmoji = ref(null); // 用于保存用户选择的表情

// 定义事件
const tuchEmoj = item => {
   selectedEmoji.value = item;
   emit('swiperChange', item);
};

const swiperChange = e => {
   swiperIndex.value = e.detail.current;
};

// 定义emits
const emit = defineEmits(['swiperChange']);
</script>

<style>
.emoj-container {
   display: flex;
   flex-direction: column;
}

.emoj-row {
   display: flex;
   flex-direction: row;
   flex-wrap: wrap;
}

.emoj-parent {
   margin-left: 8rpx;
   margin-bottom: 5rpx;
   align-items: center;
   margin-top: 8rpx;
   padding-top: 5rpx;
   padding-bottom: 5rpx;
}

.checkActive {
   background-color: #e8e8e8 !important;
}

.emoj-conn {
   width: 80rpx;
   font-size: 56rpx;
   text-align: center;
}
</style>
