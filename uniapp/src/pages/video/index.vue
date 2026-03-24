<template>
   <view class="video-container">
      <!-- 视频滑动容器（使用swiper实现上下滑动） -->

      <swiper
         class="video-swiper"
         vertical
         :current="currentIndex"
         :autoplay="false"
         :circular="false"
         :duration="300"
         @change="onSwiperChange"
         @transition="onSwiperTransition"
         @animationfinish="onSwiperAnimationFinish">
         <!-- 视频列表 -->
         <swiper-item
            v-for="(video, index) in videoList"
            :key="video.id"
            class="video-item"
            :circular="true"
            :id="`video-item-${index}`">
            <!-- 视频播放器 -->
            <video
               class="video-player"
               :src="video.src"
               :id="`video-${index}`"
               :autoplay="currentIndex === index"
               loop
               controls
               :enable-progress-gesture="true"
               object-fit="cover"
               @play="onVideoPlay(index)"
               @pause="onVideoPause(index)">
               <cover-view class="video-info" @click="togglePlayPause(index)">
                  <!-- 视频标题和作者 -->
                  <cover-view class="box" @click="router.back()">
                     {{ '<' }}
                  </cover-view>
                  <cover-view class="video-header">
                     <cover-view class="video-title">{{
                        video.title.slice(0, 15)
                     }}</cover-view>
                     <cover-view class="video-author"
                        >@{{ video.author }}</cover-view
                     >
                  </cover-view>
                  <cover-view class="video-description">
                     <cover-view class="action-item">
                        <cover-image
                           :src="video.avatarUrl"
                           class="img"></cover-image>
                     </cover-view>
                     <cover-view class="action-item" @click="likeVideo(index)">
                        <cover-view class="action-icon">{{
                           video.liked ? '❤️' : '🤍'
                        }}</cover-view>
                        <cover-view class="action-count">{{
                           video.likes
                        }}</cover-view>
                     </cover-view>
                     <cover-view
                        class="action-item"
                        @click="showComments(index)">
                        <cover-view class="action-icon">💬</cover-view>
                        <cover-view class="action-count">{{
                           video.comments
                        }}</cover-view>
                     </cover-view>
                     <cover-view class="action-item" @click="shareVideo(index)">
                        <cover-view class="action-icon">🔗</cover-view>
                        <cover-view class="action-count">分享</cover-view>
                     </cover-view>
                     <cover-view class="action-item" @click="showMore(index)">
                        <cover-view class="action-icon">•••</cover-view>
                     </cover-view>
                  </cover-view>
               </cover-view>
            </video>

            <!-- 视频菜单覆盖层 -->

            <!-- 视频信息（悬浮在视频上） -->
         </swiper-item>
      </swiper>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { getDynamicList, getDynamicOne } from '@/api/dynamic';
import { useRouter, useRoute } from 'uni-mini-router';
import ChatVdieo from '@/webview/ChatVdieo.vue';
const route = useRoute();

const router = useRouter();

const form = reactive({
   page: 1,
   pageSize: 10
});
//加载完毕
const isLoad = ref(false);
const initList = async () => {
   if (isLoad.value) {
      return;
   }
   const res = await getDynamicList({
      lat: 39.9042,
      lng: 116.4074,
      page: form.page,
      pageSize: form.pageSize,
      province: '',
      argId: '',
      type: 1
   });
   if (res.code === 0) {
      if (res.data.length !== form.pageSize) {
         isLoad.value = true;
      }
      const arr = res.data.map(item => {
         const video = JSON.parse(item.video);

         return {
            id: item.id,
            title: item.context,
            author: item.username,
            src: video.filePath,
            likes: item.love,
            comments: item.count || 0,
            liked: item.isLove == 1,
            avatarUrl: item.avatarUrl || ''
         };
      });
      form.page = form.page + 1;
      videoList.value = [...videoList.value, ...arr];
      console.log(videoList.value);
   }
};

// 视频数据列表（实际项目中可替换为API请求）
const videoList = ref([]);
// 当前播放的视频索引
const currentIndex = ref(0);
// 窗口高度
const windowHeight = ref(0);
// 上一个播放的视频索引
const lastIndex = ref(0);

// 页面加载时获取系统信息
onMounted(async () => {
   // 获取窗口高度
   console.log(route.query.id);

   if (route.query.id) {
      const res = await getDynamicOne(route.query.id);
      if (res.code === 0) {
         const video = JSON.parse(res.data.video);

         videoList.value.push({
            id: res.data.id,
            title: res.data.context,
            author: res.data.username,
            src: video.filePath,
            likes: res.data.love,
            comments: res.data.count || 0,
            liked: res.data.isLove == 1,
            avatarUrl: res.data.avatarUrl || ''
         });
      }
   }
   initList();
   uni.getSystemInfo({
      success: res => {
         windowHeight.value = res.windowHeight;
      }
   });

   // 初始化视频播放
   nextTick(() => {
      playVideo(currentIndex.value);
   });
});

//是否播放
const isPlaying = ref(true);
// 暂停 开始播放
const togglePlayPause = (index: number) => {
   const videoContext = uni.createVideoContext(`video-${index}`);
   if (isPlaying.value) {
      videoContext.pause();

      isPlaying.value = false;
   } else {
      videoContext.play();
      isPlaying.value = true;
   }
};

// 播放视频
const playVideo = (index: number) => {
   const videoContext = uni.createVideoContext(`video-${index}`);
   if (videoContext) {
      videoContext.play();
   }
};

// 暂停视频
const pauseVideo = (index: number) => {
   const videoContext = uni.createVideoContext(`video-${index}`);
   if (videoContext) {
      videoContext.pause();
   }
};

// Swiper切换事件处理
const onSwiperChange = (e: any) => {
   const newIndex = e.detail.current;
   console.log(isLoad.value, videoList.value.length - 1, newIndex);

   if (newIndex === videoList.value.length - 1) {
      if (!isLoad.value) {
         initList();
      } else {
         uni.showToast({
            title: '没有更多数据了',
            icon: 'none'
         });
      }
   }
   // 只有在索引真正变化时才处理
   if (newIndex !== currentIndex.value) {
      // 暂停上一个视频
      pauseVideo(currentIndex.value);
      // 记录上一个索引
      lastIndex.value = currentIndex.value;
      // 更新当前索引
      currentIndex.value = newIndex;
   }
};

// Swiper过渡动画事件处理
const onSwiperTransition = (e: any) => {
   // 可以在这里添加过渡动画中的处理逻辑
};

// Swiper动画结束事件处理
const onSwiperAnimationFinish = (e: any) => {
   const newIndex = e.detail.current;
   // 播放当前视频
   playVideo(newIndex);
};

// 视频播放事件
const onVideoPlay = (index: number) => {
   currentIndex.value = index;
};

// 视频暂停事件
const onVideoPause = (index: number) => {
   // 可以在这里处理暂停逻辑
};

// 点赞视频
const likeVideo = (index: number) => {
   const video = videoList.value[index];
   video.liked = !video.liked;
   video.likes += video.liked ? 1 : -1;
};

// 显示评论
const showComments = (index: number) => {
   uni.showToast({
      title: `查看第${index + 1}个视频的评论`,
      icon: 'none'
   });
};

// 分享视频
const shareVideo = (index: number) => {
   uni.showToast({
      title: `分享第${index + 1}个视频`,
      icon: 'none'
   });
};

// 更多操作
const showMore = (index: number) => {
   uni.showToast({
      title: `更多操作`,
      icon: 'none'
   });
};
</script>

<style lang="scss" scoped>
.video-container {
   width: 100%;
   height: 100vh;
   background-color: #000;
   overflow: hidden;
}
.box {
   position: fixed;
   left: 35rpx;
   top: var(--status-bar-height);
   padding-top: 20rpx;
   width: 50rpx;
   height: 100rpx;

   color: white;
   z-index: 9999;
}

/* Swiper 容器样式 */
.video-swiper {
   width: 100%;
   height: 100vh;
}

/* Swiper 轮播项样式 */
.video-swiper .uni-swiper-item {
   width: 100%;
   height: 100vh;
}

.video-item {
   position: relative;
   width: 100%;
   height: 100vh;
   background-color: #000;
}

.video-player {
   width: 100%;
   height: 100%;
   object-fit: cover;
}

.video_menu {
   position: absolute;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);
   z-index: 10;
   color: white;
   cover-view-align: center;
}

.video-info {
   position: absolute;
   bottom: 0;
   left: 0;
   width: 100%;
   padding: 40rpx;
   color: #fff;
   background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
   box-sizing: border-box;
   display: flex;
   flex-direction: column;
   justify-content: space-between;
   height: 100%;
   z-index: 999999999999999;
   padding-top: 200rpx;
}

.video-header {
   margin-bottom: 40rpx;
   flex: 1;
   display: flex;
   flex-direction: column;
   justify-content: flex-end;

   .video-title {
      font-size: 36rpx;
      font-weight: bold;
      margin-bottom: 20rpx;
      line-height: 50rpx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      cover-view-overflow: ellipsis;
   }

   .video-author {
      font-size: 28rpx;
      opacity: 0.8;
      margin-bottom: 10rpx;
   }
}

.video-actions {
   display: flex;
   flex-direction: column;
   align-items: flex-end;
   gap: 40rpx;
   position: absolute;
   right: 40rpx;
   bottom: 200rpx;

   .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10rpx;
      cursor: pointer;
      // margin-bottom: ;
      margin: 1em 0;
      transition: transform 0.2s;

      &:active {
         transform: scale(1.2);
      }
   }
}
.action-icon {
   font-size: 64rpx;
}

.action-count {
   font-size: 32rpx;
   text-align: center;
   opacity: 0.9;
}

/* 视频菜单覆盖层样式 */
.video_menu {
   position: absolute;
   top: 200rpx;
   left: 50%;
   transform: translateX(-50%);
   z-index: 100;
   color: white;
   cover-view-align: center;
   background-color: rgba(0, 0, 0, 0.5);
   padding: 20rpx 40rpx;
   border-radius: 10rpx;
}

.video_menu .button {
   background-color: rgba(255, 255, 255, 0.3);
   padding: 10rpx 20rpx;
   border-radius: 5rpx;
}

/* 视频菜单覆盖层样式 */
.video_menu {
   position: absolute;
   top: 200rpx;
   left: 50%;
   transform: translateX(-50%);
   z-index: 100;
   color: white;
   cover-view-align: center;
   background-color: rgba(0, 0, 0, 0.5);
   padding: 20rpx 40rpx;
   border-radius: 10rpx;
}

.video_menu .button {
   background-color: rgba(255, 255, 255, 0.3);
   padding: 10rpx 20rpx;
   border-radius: 5rpx;
}

/* 视频菜单覆盖层样式 */
.video_menu {
   position: absolute;
   top: 200rpx;
   left: 50%;
   transform: translateX(-50%);
   z-index: 100;
   color: white;
   cover-view-align: center;
   background-color: rgba(0, 0, 0, 0.5);
   padding: 20rpx 40rpx;
   border-radius: 10rpx;
}

.video_menu .button {
   background-color: rgba(255, 255, 255, 0.3);
   padding: 10rpx 20rpx;
   border-radius: 5rpx;
}
.video-description {
   position: fixed;
   right: 0;
   width: 100rpx;
   height: 50vh;
   font-size: 1.2em;
   bottom: 0;
}
.img {
   width: 80rpx;
   height: 80rpx;
   border-radius: 50%;
}
</style>
