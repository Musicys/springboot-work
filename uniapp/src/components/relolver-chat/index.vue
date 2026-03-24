<template>
   <view class="title" v-if="typeTitle.type === MESSAGE_TYPE.TEXT">
      <rich-text :nodes="typeTitle.content"></rich-text>
   </view>

   <view class="title" v-else-if="typeTitle.type === MESSAGE_TYPE.IMAGE">
      <wd-img
         :src="typeTitle.content.url"
         radius="15"
         :width="typeTitle.content.width || 150"
         :height="typeTitle.content.height || 150"
         :enable-preview="true"></wd-img>
   </view>
   <!-- 表情包 -->
   <!-- 视频 -->
   <view
      class="title"
      v-else-if="typeTitle.type === MESSAGE_TYPE.VIDEO"
      :style="
         'width:' +
         typeTitle.content.width +
         'px;height: ' +
         typeTitle.content.height +
         'px'
      ">
      <!-- #ifdef H5 -->
      <video
         id="myVideo"
         class="myVideo"
         :src="typeTitle.content.url"
         :poster="typeTitle.content.cover"></video>

      <!-- #endif -->
      <!-- #ifdef APP-PLUS -->
      <ChatVdieo
         :videoUrl="typeTitle.content.url"
         :poster="typeTitle.content.cover"></ChatVdieo>

      <!-- #endif -->
   </view>
   <!-- 音频 -->
   <view class="title" v-else-if="typeTitle.type === MESSAGE_TYPE.AUDIO">
   </view>
   <!-- 文件 -->
   <view class="title" v-else-if="typeTitle.type === MESSAGE_TYPE.FILE">
      <view class="title-file">
         <!-- 文件下载 -->
         <view class="title-file-download">
            <text>文件下载</text>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { MESSAGE_TYPE } from '@/util/mssageType';
// #ifdef APP-PLUS
import ChatVdieo from '@/webview/ChatVdieo.vue';
// #endif
const props = defineProps({
   msg: {
      type: String,
      default: ''
   }
});

//计算属性
const typeTitle = computed(() => {
   if (props.msg) {
      try {
         const res = JSON.parse(props.msg);
         return res;
      } catch (error) {
         return {
            type: MESSAGE_TYPE.TEXT,
            content: props.msg
         };
      }
   }
});
const videoContext = ref(null);
onMounted(() => {
   if (typeTitle.value.type === MESSAGE_TYPE.VOICE) {
      videoContext.value = uni.createVideoContext('myVideo');
   }
});
</script>

<style lang="scss" scoped>
.myVideo {
   width: 100% !important;
   height: 100% !important;
   object-fit: cover;
   position: relative !important; /* 确保视频能够随页面滚动 */
}
.title {
   position: relative;
   border-radius: 15rpx;
   overflow: hidden;
}
</style>
