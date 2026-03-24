<template>
   <view class="tabar">
      <view class="router">
         <home v-if="currentTabbar === 0"></home>
         <dynamic v-if="currentTabbar === 1"></dynamic>
         <message v-if="currentTabbar === 3"></message>
         <mine v-if="currentTabbar === 4"></mine>
      </view>

      <tn-tabbar
         v-model="currentTabbar"
         :animation="true"
         :safeAreaInsetBottom="true"
         icon-size="54"
         font-size="24"
         height="120rpx"
         :before-switch="beforeSwitch"
         fixed>
         <tn-tabbar-item
            v-for="(item, index) in tabbarData"
            :key="index"
            :icon="item.inactiveIcon"
            :active-icon="item.activeIcon"
            :text="item.title"
            :bulge="index === 2"
            :badge="item.badge ? scoke.massgecout : false"
            bulge-bg-color="tn-gradient__cool-6" />
      </tn-tabbar>

      <!--#ifdef H5 -->
      <view class="download-float-wrapper">
         <view
            class="download-float"
            :class="{ expanded: isDownloadExpanded }"
            @click="toggleDownload">
            <text style="color: white" v-if="!isDownloadExpanded">App</text>
            <view style="display: flex; align-items: center" v-else>
               <view class="download-icon">
                  <tn-icon name="download" size="40rpx" color="#fff"></tn-icon>
               </view>
               <view class="download-text">去下载</view>
            </view>
         </view>
      </view>
      <!--#endif -->
   </view>
   <wd-message-box></wd-message-box>
</template>

<script setup lang="ts">
import Dynamic from './dynamic/index.vue';
import Home from './home/index.vue';
import Mine from './mine/index.vue';
import { useRouter } from 'uni-mini-router';
import Message from './message/index.vue';
import { sockeStore } from '@/store/socke';
import {
   useMessage,
   useToast,
   useIcon
} from '../../../node_modules/wot-design-uni';
import { useStore } from '@/store/user';
import { debounce, throttle } from '@/util';
const store = useStore();
const message = useMessage();
const toast = useToast();
const scoke = sockeStore();
const router = useRouter();
const currentTabbar = ref(0);

const isDownloadExpanded = ref(false);

// 检测H5环境
onMounted(() => {});

// 切换下载按钮展开/收起状态
const toggleDownload = () => {
   if (isDownloadExpanded.value) {
      window.open('http://101.42.172.99:88');
      return;
   }
   isDownloadExpanded.value = !isDownloadExpanded.value;
   if (isDownloadExpanded.value) {
      // 模拟下载操作，可以替换为实际的下载链接
      setTimeout(() => {
         // 这里可以添加实际的APP下载链接跳转逻辑

         isDownloadExpanded.value = false;
      }, 2000);
   }
};
watch(
   () => scoke.isConnect,
   newVal => {
      if (!newVal) {
         message
            .alert({
               title: '连接已断开，请重新连接',
               confirmButtonText: '重新连接'
            })
            .then(() => {
               uni.showLoading({
                  title: '连接中',
                  mask: true
               });
               setTimeout(() => {
                  scoke.websocke(store.userInfo.id);
               }, 1000);
            });
      } else {
         message.close();
         uni.hideLoading();
         uni.showToast({
            title: '连接成功',
            icon: 'none'
         });
      }
   },
   {
      deep: true
   }
);

// 导航栏数据
const tabbarData = [
   {
      title: '首页',
      activeIcon: '../../static/tabbar/home_tnnew.png',
      inactiveIcon: '../../static/tabbar/home_tn.png'
   },
   {
      title: '动态',
      activeIcon: '../../static/tabbar/circle_tnnew.png',
      inactiveIcon: '../../static/tabbar/circle_tn.png'
   },
   {
      title: '发布',
      activeIcon: 'menu-circle',
      inactiveIcon: 'rocket',
      activeIconColor: '#FFFFFF',
      inactiveIconColor: '#FFFFFF',
      iconSize: 50,
      out: true
   },
   {
      title: '消息',
      activeIcon: '../../static/tabbar/preferred_tnnew.png',
      inactiveIcon: '../../static/tabbar/preferred_tn.png',
      badge: scoke.massgecout
   },
   {
      title: '我的',
      activeIcon: '../../static/tabbar/mine_tnnew.png',
      inactiveIcon: '../../static/tabbar/mine_tn.png'
   }
];
const beforeSwitch = (index: number, name: string | number) => {
   if (index === 2) {
      router.push({ name: 'sumbitfrom' });
      return false;
   }
   return true;
};
onLoad(() => {
   uni.hideTabBar();
   uni.removeStorageSync('selectedIndex');
});
</script>

<style lang="scss" scoped>
.router {
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
   width: 100vw;
   height: 100vw;
   background: var(--quyou-bg-centext-color);
}
:deep() {
   .tn-tabbar__placeholder {
      display: none !important;
   }
}
.masge {
   position: fixed;
   z-index: 99999;
   width: 100%;
   left: 0;
   top: 25rpx;
}

/* H5下载悬浮图标样式 */
.download-float-wrapper {
   position: fixed;
   right: -20rpx;
   top: 500rpx;
   z-index: 99998;
   display: flex;
   justify-content: flex-end;
   transition: all 0.3s ease;
}

.download-float {
   display: flex;
   align-items: center;
   background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
   border-radius: 50rpx;
   padding: 0 20rpx;
   height: 96rpx;
   box-shadow: 0 10rpx 30rpx rgba(102, 126, 234, 0.4);
   cursor: pointer;
   transition: all 0.3s ease;
   overflow: hidden;
   width: 96rpx;
   justify-content: center;

   &:active {
      transform: scale(0.95);
   }

   &.expanded {
      width: 240rpx;
      padding: 0 30rpx;
      justify-content: space-between;
   }
}

.download-icon {
   width: 64rpx;
   height: 64rpx;
   background: rgba(255, 255, 255, 0.2);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   transition: all 0.3s ease;
}

.download-text {
   color: #fff;
   font-size: 32rpx;
   font-weight: 600;
   margin-left: 20rpx;
   opacity: 0;
   visibility: hidden;
   transform: translateX(10rpx);
   transition: all 0.3s ease;
   white-space: nowrap;

   .download-float.expanded & {
      opacity: 1;
      visibility: visible;
      transform: translateX(0);
   }
}

/* 响应式设计 */
@media (max-width: 768px) {
   .download-float-wrapper {
      bottom: 180rpx;
   }

   .download-float {
      height: 88rpx;
      width: 88rpx;

      &.expanded {
         width: 220rpx;
      }
   }

   .download-icon {
      width: 56rpx;
      height: 56rpx;
   }

   .download-text {
      font-size: 28rpx;
   }
}
</style>
