<template>
   <NavTop v-if="showpadding"></NavTop>
   <scroll-view
      class="srcollview"
      scroll-y="true"
      refresher-enabled="true"
      refresher-default-style="none"
      :refresher-triggered="triggered"
      :refresher-threshold="50"
      refresher-background="#F4ECD7"
      @refresherpulling="onPulling"
      @refresherrefresh="onRefresh"
      @scrolltolower="scrbotm">
      <view class="cart">
         <view class="refresh-text" v-if="triggered">
            <tn-loadmore status="loading" :text="customLoadMoreText" />
         </view>

         <slot></slot>

         <view class="load-text" v-if="bom"> 数据加载中.... </view>

         <view class="text" v-if="Isbut">
            <tn-loadmore
               status="nomore"
               color="#666;"
               :text="customLoadMoreText" />
         </view>
         <NavBottom v-if="showpadding"></NavBottom>
      </view>
   </scroll-view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const triggered = ref(false);
import NavTop from '@/components/nav-top/index.vue';
import NavBottom from '@/components/nav-buttom/index.vue';

//默认值
const props = defineProps({
   showpadding: {
      type: Boolean,
      default: true
   }
});

//组件名称 ScrollView
defineOptions({
   name: 'ScrollView'
});
const customLoadMoreText: LoadmoreText = {
   loadmore: '还有内容呢',
   loading: '等等，数据正在赶来',
   nomore: '哎呀，没有啦',
   empty: '空空的呀'
};
const bom = ref(false);
const emit = defineEmits(['srctop', 'srcbut']);
//是否加载完毕
const Isbut = ref(false);
// 取消拉取
const onRefresh = () => {
   // 加载数据
   triggered.value = true; // 开始刷新
   // 等待父组件的异步操作完成
   setTimeout(() => {
      emit('srctop', () => {
         triggered.value = false;
         Isbut.value = false;
      });
   }, 1000);
};
// 拉取
const onPulling = () => {
   console.log('拉取');
};
const scrbotm = () => {
   if (Isbut.value) {
      console.log('加载数据了完毕了...');
      return;
   }
   bom.value = true;
   emit('srcbut', bool => {
      bom.value = false;
      Isbut.value = bool || false;
   });
};
</script>

<style lang="scss" scoped>
.srcollview {
   width: 100vw;
   height: 90vh;
   background: linear-gradient(160deg, #f2e9d2, #fff);
}

.refresh-text,
.load-text {
   text-align: center;
   font-size: 14px;
   height: 50rpx;

   color: #666;
   animation: fade 1.5s infinite; // 添加淡入淡出动画
}

.text {
   text-align: center;
   font-size: 14px;
   color: #666;
}

@keyframes fade {
   0% {
      opacity: 1;
   }

   50% {
      opacity: 0.3;
   }

   100% {
      opacity: 1;
   }
}
</style>
