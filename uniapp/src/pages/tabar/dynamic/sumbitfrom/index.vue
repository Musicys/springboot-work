<template>
   <view class="submit-dynamic-page">
      <!-- 自定义导航栏 -->
      <view class="nav-bar">
         <view class="nav-left" @click="handleBack">
            <wd-icon name="arrow-left" size="36rpx" color="#333"></wd-icon>
         </view>
         <view class="nav-center"> </view>
         <view class="nav-right">
            <button
               :class="{
                  'publish-btn':
                     from.title.trim() || from.context.trim()
                        ? 'publish-btn-active'
                        : 'publish-btn-disabled'
               }"
               @click="handlePublish">
               发布
            </button>
         </view>
      </view>
      <!-- 已选视频展示 -->
      <view v-if="selectedVideo && from.type === 1" class="selected-video">
         <view class="video-item">
            <video
               :src="selectedVideo.tempFilePath"
               class="selected-video-preview"
               controls></video>
            <view class="remove-btn" @click.stop="removeVideo">
               <wd-icon name="close" color="#fff"></wd-icon>
            </view>
         </view>
      </view>

      <!-- 标题输入区域 -->
      <view class="title-area">
         <input
            v-model="from.title"
            placeholder="添加标题（选填）"
            placeholder-class="title-placeholder"
            class="title-input"
            maxlength="50" />
         <view class="title-count">{{ from.title.length }}/50</view>
      </view>

      <!-- 内容类型切换 -->
      <view class="content-type-switch">
         <view
            class="type-item"
            :class="{ active: from.type === 0 }"
            @click="changeContentType(0)">
            <wd-icon
               name="image"
               :color="from.type === 0 ? '#0BDAEE' : '#666'"></wd-icon>
            <text :style="{ color: from.type === 0 ? '#0BDAEE' : '#666' }"
               >图文</text
            >
         </view>
         <view
            class="type-item"
            :class="{ active: from.type === 1 }"
            @click="changeContentType(1)">
            <wd-icon
               name="play-circle-filled"
               :color="from.type === 1 ? '#0BDAEE' : '#666'"></wd-icon>
            <text :style="{ color: from.type === 1 ? '#0BDAEE' : '#666' }"
               >视频文</text
            >
         </view>
      </view>
      <!-- 分区选择 -->
      <view class="partition-select" @click="showSlect = true">
         <template v-if="!from.argId">
            <view class="partition-label">
               <wd-icon name="apps" color="#666"></wd-icon>
               <text>选择分区</text>
            </view>
            <view class="partition-value">
               <text class="placeholder-text">未选择</text>
               <wd-icon name="arrow-right" size="32rpx"></wd-icon>
            </view>
         </template>
         <template v-else>
            <view class="partition-label">
               <view>{{ slectTag.categoryname }}</view>
               <view>{{ slectTag.introductory }}</view>
            </view>
            <view class="partition-value">
               <image :src="slectTag.url"></image>
               <tn-icon name="right" bold></tn-icon>
            </view>
         </template>
      </view>
      <!-- 输入区域 -->
      <view class="input-area">
         <textarea
            v-model="from.context"
            placeholder="分享你的日常，让缘分来得更快点~"
            placeholder-class="placeholder"
            class="content-textarea"
            :auto-height="true"
            :show-confirm-bar="false"></textarea>
      </view>
      <!-- 已选图片展示 -->

      <view
         v-if="imagsarr.length > 0 && from.type === 0"
         class="selected-images">
         <view class="image-item" v-for="(img, index) in imagsarr" :key="index">
            <image :src="img" mode="aspectFill" class="selected-image"></image>
            <view class="remove-btn" @click.stop="removeImage(index)">
               <wd-icon name="close" color="#fff"></wd-icon>
            </view>
         </view>
      </view>

      <!-- 固定在顶部的布局 -->
      <view class="fixed-top-layout">
         <!-- 第一行：定制，添加话题 -->
         <view class="topic-line">
            <view class="location-info">
               <wd-icon name="location" color="#999"></wd-icon>
               <text class="location-text">{{ locationText }}</text>
            </view>
            <view class="add-topic" @click="addTopic">
               <wd-icon name="hash" color="#0BDAEE"></wd-icon>
               <text class="topic-text">添加话题</text>
            </view>
         </view>

         <!-- 第二行：标签和相册icon -->
         <view class="function-line">
            <view class="function-item" @click="selectEmoji">
               <tn-icon name="cute" bold />
            </view>
            <view class="function-item" @click="openAlbum">
               <tn-icon name="folder-add" bold />
            </view>
         </view>
      </view>

      <TagSlect
         v-if="showSlect"
         @tagSelect="handSelct"
         @closeSelect="showSlect = false"></TagSlect>
      <!-- 底部空间，避免内容被遮挡 -->
      <view class="bottom-space"></view>
   </view>
</template>

<script setup lang="ts">
import { addDynamic } from '@/api/dynamic';
import { useStore } from '@/store/user';
import { onMounted, reactive, ref, computed } from 'vue';
import { useRouter } from 'uni-mini-router';
import { updateFileVideo, updateOssFile } from '@/api/file';
import TagSlect from './tag-select.vue';
import { TagType } from '@/api/dynamictag/model/type';
import { debounce } from '@/util';
const router = useRouter();
const store = useStore();
const { userInfo, location } = store;

const showSlect = ref<boolean>(false);
const slectTag = ref<TagType>('');
// 表单数据
const from = reactive({
   title: '',
   context: '',
   imgArr: [],
   lat: location.lat,
   lng: location.lng,
   userId: userInfo.id,
   type: 0,
   video: '',
   partitionId: '', // 分区ID
   argId: null // 标签ID
});

// 已选视频类型定义
interface VideoInfo {
   tempFilePath: string;
   duration: number;
   width: number;
   height: number;
   thumbTempFilePath?: string;
}

// 已选图片数组
const imagsarr = ref<string[]>([]);
// 已选视频
const selectedVideo = ref<VideoInfo | null>(null);
// 内容类型（图文/视频）

// 位置信息
const locationText = computed(() => {
   return userInfo?.province || '定位中...';
});

const handSelct = (tag: TagType) => {
   console.log('select', tag);

   slectTag.value = tag;
   from.argId = tag.id;
};

const handleBack = () => {
   router.back();
};
const IsSumbit = ref<boolean>(false);

// 发布动态
const handlePublish = debounce(async () => {
   if (IsSumbit.value) {
      return;
   }
   if (!from.title.trim() && !from.context.trim()) {
      uni.showToast({
         title: '请输入标题或内容',
         icon: 'none'
      });
      return;
   }

   if (!from.argId) {
      uni.showToast({
         title: '请选择分区',
         icon: 'none'
      });
      return;
   }

   uni.showLoading({
      title: '发布中...',
      mask: true
   });
   IsSumbit.value = true;

   // 更新表单数据

   if (from.type == 0) {
      const rucest = [];
      for (let file of imagsarr.value) {
         let res = updateOssFile(file);
         rucest.push(res);
      }

      //上传相册
      await Promise.all(rucest).then(res => {
         for (let records of res) {
            let data = JSON.parse(records?.data);
            if (data.code === 0) {
               from.imgArr.push(data.data);
            }
         }
      });
   } else if (from.type == 1 && selectedVideo.value) {
      // 上传视频
      const videoRes = await updateFileVideo({
         file: selectedVideo.value.tempFilePath,
         tagId: 9,
         width: selectedVideo.value.width,
         height: selectedVideo.value.height,
         cover: selectedVideo.value.thumbTempFilePath || '',
         durationSeconds: Math.ceil(selectedVideo.value.duration)
      });
      const Videodata = JSON.parse((videoRes.data as string) || '[]');
      console.log('Videodata', Videodata);
      if (Videodata.code === 0) {
         from.video = Videodata.data;
      }
   }

   // 准备提交数据

   try {
      const res = await addDynamic({
         title: from.title,
         context: from.context,
         imgArr: from.imgArr ? JSON.stringify(from.imgArr) : '',
         partitionId: from.partitionId,
         lat: from.lat,
         lng: from.lng,
         userId: from.userId,
         argId: from.argId,
         type: from.type,
         video: from.video ? JSON.stringify(from.video) : ''
      });
      if (res.code === 0) {
         uni.hideLoading();
         uni.showToast({
            title: '发布成功',
            icon: 'none'
         });
         // 发布成功后返回上一页
         setTimeout(() => {
            router.back();
         }, 1500);
      }
   } catch (error) {
      IsSumbit.value = false;
      uni.hideLoading();
      uni.showToast({
         title: '网络异常，请重试',
         icon: 'none'
      });
   }
}, 1000);

// 打开相册选择图片
const openAlbum = () => {
   if (from.type === 1) {
      // 选择视频
      uni.chooseVideo({
         sourceType: ['album'],
         maxDuration: 30,
         camera: 'back',
         success: res => {
            if (res.duration > 30) {
               uni.showToast({
                  title: '视频时长不能超过30秒',
                  icon: 'none'
               });
               return;
            }
            console.log('选择的视频信息:', res);

            // 将完整的视频对象赋值给selectedVideo
            selectedVideo.value = res;
            // 清空图片数组，确保只有视频
            imagsarr.value = [];
         },
         fail: err => {
            console.error('选择视频失败:', err);
         }
      });
   } else {
      // 选择图片
      // 计算还能选择的图片数量（最多9张）
      const maxCount = 9 - imagsarr.value.length;
      if (maxCount <= 0) {
         uni.showToast({
            title: '最多只能选择9张图片',
            icon: 'none'
         });
         return;
      }

      uni.chooseImage({
         count: maxCount,
         sizeType: ['original', 'compressed'],
         sourceType: ['album'],
         success: res => {
            // 将新选择的图片添加到数组中
            imagsarr.value = [...imagsarr.value, ...res.tempFilePaths];
            // 清空视频
            selectedVideo.value = null;
         },
         fail: err => {
            console.error('打开相册失败:', err);
         }
      });
   }
};

// 移除已选图片
const removeImage = (index: number) => {
   imagsarr.value.splice(index, 1);
};

// 移除已选视频
const removeVideo = () => {
   selectedVideo.value = null;
};

// 添加话题
const addTopic = () => {
   // 这里可以实现添加话题的逻辑
   // 例如弹出话题选择面板或直接在输入框中插入话题符号
   uni.showToast({
      title: '话题功能待实现',
      icon: 'none'
   });
};

// 选择表情
const selectEmoji = () => {
   // 这里可以实现表情选择的逻辑
   uni.showToast({
      title: '表情功能待实现',
      icon: 'none'
   });
};

// 切换内容类型时清空媒体
const changeContentType = (type: number) => {
   from.type = type;
   if (type === 0) {
      selectedVideo.value = null;
   } else {
      imagsarr.value = [];
   }
};

// 页面加载时获取位置信息
onMounted(() => {
   if (!location.lat || !location.lng) {
      store.setLocation();
   }
});
</script>

<style lang="scss" scoped>
.submit-dynamic-page {
   padding-top: var(--status-bar-height); /* 状态栏 */
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
   width: 100%;
   height: 100vh;
   background-color: #fff;
   display: flex;
   flex-direction: column;
}

/* 导航栏样式 */
.nav-bar {
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: var(--quyou-nav-ste-height);
   padding: 0 16rpx;
   font-size: 26rpx;
   background-color: var(--quyou-nav-bg-color);
   border-bottom: 1px solid #f0f0f0;
}

.nav-left,
.nav-right {
   font-size: 26rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.nav-center {
   flex: 1;
   text-align: center;
}

.nav-title {
   font-size: 24rpx;
   font-weight: 600;
   color: #333;
}

.publish-btn {
   font-size: 24rpx;
   padding: 0;
   min-width: auto;
   line-height: 36rpx;
   border-radius: 8rpx;
   padding: 5rpx 26rpx;
}

/* 发布按钮禁用状态 */
.publish-btn-disabled {
   background-color: #eeeeee;
   color: #898989;
}

/* 发布按钮激活状态 */
.publish-btn-active {
   background-color: #0bdaee;
   color: #000000;
}

/* 标题区域样式 */
.title-area {
   display: flex;
   align-items: center;
   justify-content: space-between;
   padding: 16rpx;
   background: var(--quyou-bg-centext-color);
   border-bottom: 1rpx solid #f0f0f0;
}

.title-input {
   flex: 1;
   font-size: 28rpx;
   color: #333;
   padding: 0;
   margin-right: 16rpx;
}

.title-placeholder {
   color: #999;
}

.title-count {
   font-size: 24rpx;
   color: #999;
}

/* 内容类型切换样式 */
.content-type-switch {
   display: flex;
   padding: 16rpx;
   background: var(--quyou-bg-centext-color);
   justify-content: space-around;
   border-bottom: 1rpx solid #f0f0f0;
}

.type-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   flex: 1;
   padding: 10rpx;
   border-radius: 8rpx;
   border: 1rpx solid #f0f0f0;
}

.type-item.active {
   background-color: #fff;
}

.type-item text {
   font-size: 24rpx;
   margin-top: 6rpx;
}

/* 分区选择样式 */
.partition-select {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 16rpx;
   background: var(--quyou-bg-centext-color);
   border-bottom: 1rpx solid #f0f0f0;
}

.partition-label {
   // flex: 1;
   // height: auto;
   height: 100%;
   display: flex;
   align-items: start;
   flex-direction: column;
   justify-content: space-around;
   & > view:nth-child(2) {
      font-size: 0.9em;
   }
}

.partition-label text {
   font-size: 28rpx;
   color: #333;
   margin-left: 10rpx;
}

.partition-value {
   display: flex;
   align-items: center;

   & > image {
      --w: 120rpx;
      width: var(--w);
      height: var(--w);
      margin-right: 20rpx;
      border-radius: 15rpx;
   }
}

.partition-value text {
   font-size: 28rpx;
   color: #333;
   margin-right: 10rpx;
}

.placeholder-text {
   color: #999;
}

/* 输入区域样式 */
.input-area {
   padding: 16rpx;
   flex: 1;
   background: var(--quyou-bg-centext-color);
}

.content-textarea {
   width: 100%;
   min-height: 120rpx;
   font-size: 28rpx;
   color: #333;
   line-height: 1.5;
   padding: 0;
}

/* 视频预览样式 */
.selected-video {
   padding: 16rpx;
   background: var(--quyou-bg-centext-color);
}

.video-item {
   width: 100%;
   height: 400rpx;
   position: relative;
   border-radius: 12rpx;
   overflow: hidden;
   background-color: #f5f5f5;
}

.selected-video-preview {
   width: 100%;
   height: 100%;
}

/* 分区选择弹窗样式 */
.partition-popup {
   padding: 20rpx;
   background-color: #fff;
   border-radius: 20rpx 20rpx 0 0;
}

.popup-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 20rpx 0;
   border-bottom: 1rpx solid #f0f0f0;
}

.popup-title {
   font-size: 32rpx;
   font-weight: bold;
   color: #333;
}

.popup-close {
   padding: 10rpx;
}

.partition-list {
   max-height: 600rpx;
   overflow-y: auto;
   padding: 20rpx 0;
}

.partition-item {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 24rpx 0;
   border-bottom: 1rpx solid #f5f5f5;
}

.partition-item:last-child {
   border-bottom: none;
}

.partition-item.active {
   color: #0bdaee;
}

.placeholder {
   color: #999;
}

/* 固定顶部布局 */
.fixed-top-layout {
   position: sticky;
   top: 0;
   z-index: 10;
   background-color: #fff;
   border-top: 1px solid #f0f0f0;
   padding: 12rpx 16rpx;
}

/* 话题行样式 */
.topic-line {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 12rpx;
   height: 56rpx;
   font-size: 26rpx;
}

.location-info {
   display: flex;
   font-size: 26rpx;
   align-items: center;
}

.location-text {
   color: #999;
   margin-left: 4rpx;
}

.add-topic {
   display: flex;
   align-items: center;
   padding: 6rpx 12rpx;
   background-color: #f5f5f5;
   border-radius: 16rpx;
   font-size: 26rpx;
}

.topic-text {
   font-size: 26rpx;
   color: #0bdaee;
   margin-left: 4rpx;
}

/* 功能行样式 */
.function-line {
   display: flex;
   padding: 8rpx 0;
}

.function-item {
   font-size: 42rpx;
   display: flex;
   margin: 0 12rpx;
   align-items: center;
   justify-content: center;
}

/* 已选图片样式 */
.selected-images {
   display: flex;
   flex-wrap: wrap;
   padding: 0 16rpx 16rpx;
   gap: 8rpx;
}

.image-item {
   width: 120rpx;
   height: 120rpx;
   position: relative;
}

.selected-image {
   width: 100%;
   height: 100%;
   border-radius: 8rpx;
}

.remove-btn {
   position: absolute;
   top: -8rpx;
   right: -8rpx;
   width: 32rpx;
   height: 32rpx;
   background-color: rgba(0, 0, 0, 0.5);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
}

/* 底部空间 */
.bottom-space {
   height: 20rpx;
}
</style>
