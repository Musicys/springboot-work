<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="navbar">
         <view class="navbar-content">
            <!-- 左侧位置筛选 -->
            <view class="location" @click="showLocationPicker = true">
               <tn-icon name="map-marker" size="24" color="#3B82F6"></tn-icon>
               <text class="location-text">{{ currentLocation }}</text>
               <tn-icon name="chevron-down" size="18" color="#64748B"></tn-icon>
            </view>

            <!-- 中间搜索框 -->
            <view class="search-box" @click="handleSearch">
               <tn-icon name="search" size="20" color="#9CA3AF"></tn-icon>
               <text class="search-placeholder">搜索兼职</text>
            </view>

            <!-- 右侧用户头像 -->
            <image
               class="user-avatar"
               :src="
                  userStore.userInfo?.avatarUrl ||
                  'https://picsum.photos/seed/user/64/64'
               "
               mode="aspectFill"
               @click="goToMine"></image>
         </view>
      </view>

      <!-- 轮播横幅 -->
      <swiper
         :indicator-dots="true"
         :autoplay="true"
         :interval="4000"
         :circular="true"
         indicator-color="rgba(255,255,255,0.5)"
         indicator-active-color="#fff"
         class="banner-swiper">
         <swiper-item v-for="(item, index) in bannerList" :key="index">
            <view class="banner-item">
               <image
                  :src="item.image"
                  mode="aspectFill"
                  class="banner-img"></image>
               <view class="banner-overlay">
                  <text class="banner-title">{{ item.title }}</text>
                  <text class="banner-subtitle">{{ item.subtitle }}</text>
               </view>
            </view>
         </swiper-item>
      </swiper>

      <!-- 分类导航 -->
      <view class="category-section">
         <view class="category-grid">
            <view
               class="category-item"
               v-for="item in categories"
               :key="item.id"
               @click="goToPartTime(item.id)">
               <view
                  class="category-icon"
                  :style="{ backgroundColor: item.bgColor }">
                  <tn-icon
                     :name="item.icon"
                     :size="36"
                     :color="item.color"></tn-icon>
               </view>
               <text class="category-name">{{ item.name }}</text>
            </view>
         </view>
      </view>

      <!-- 热门兼职 -->
      <view class="section">
         <view class="section-header">
            <text class="section-title">热门兼职</text>
            <view class="section-more" @click="goToPartTimeList">
               <text class="more-text" @tap="router.push({ name: 'prartlst' })"
                  >更多</text
               >
               <tn-icon
                  name="chevron-right"
                  size="18"
                  color="#3B82F6"></tn-icon>
            </view>
         </view>
         <view class="job-list">
            <view
               class="job-card"
               v-for="job in hotJobs"
               :key="job.id"
               @click="goToJobDetail(job.id)">
               <image
                  :src="job.image"
                  mode="aspectFill"
                  class="job-image"></image>
               <view class="job-info">
                  <view class="job-header">
                     <text class="job-title">{{ job.title }}</text>
                     <text class="job-salary"
                        >¥{{ job.salary }}/{{ job.salaryUnit }}</text
                     >
                  </view>
                  <view class="job-tags">
                     <view class="job-tag" :class="job.type">{{
                        job.typeText
                     }}</view>
                     <view class="job-tag">{{ job.timeText }}</view>
                  </view>
                  <text class="job-desc">{{ job.description }}</text>
               </view>
            </view>
         </view>
      </view>

      <!-- 底部占位 -->
      <view class="bottom-space"></view>
   </view>
</template>

<script setup lang="ts">
import router from '@/router';
import { ref, onMounted } from 'vue';
import * as apis from '@/api/job';
import { useUserStore } from '@/store/user';

// 创建用户store
const userStore = useUserStore();

// 当前位置
const currentLocation = ref('附近');
const showLocationPicker = ref(false);

// 轮播数据
const bannerList = ref([
   {
      title: '暑期兼职专场',
      subtitle: '海量岗位，薪资日结',
      image: 'https://picsum.photos/seed/job5/750/300'
   },
   {
      title: '新人专享福利',
      subtitle: '首单立减20元',
      image: 'https://picsum.photos/seed/job9/750/300'
   },
   {
      title: '技能认证专区',
      subtitle: '认证享优先推荐',
      image: 'https://picsum.photos/seed/job10/750/300'
   }
]);

// 分类数据
const categories = ref([
   {
      id: '1',
      name: '线上兼职',
      icon: 'monitor',
      color: '#3B82F6',
      bgColor: '#DBEAFE'
   },
   {
      id: '2',
      name: '线下兼职',
      icon: 'building',
      color: '#10B981',
      bgColor: '#D1FAE5'
   },
   {
      id: '3',
      name: '校园兼职',
      icon: 'school',
      color: '#F59E0B',
      bgColor: '#FEF3C7'
   },
   {
      id: '4',
      name: '校外兼职',
      icon: 'star',
      color: '#8B5CF6',
      bgColor: '#EDE9FE'
   }
]);

// 热门兼职数据
const hotJobs = ref<any[]>([]);

// 获取兼职列表
const fetchJobList = async () => {
   try {
      const res = await apis.getJobList({ pageNum: 1, pageSize: 5 });
      if (res.code === 0 && res.data && res.data.records) {
         hotJobs.value = res.data.records.map((item: any) => ({
            id: item.id,
            title: item.title,
            salary: item.salaryMin,
            salaryUnit: '天',
            type: item.jobType === 1 ? 'online' : 'offline',
            typeText: item.jobTypeDesc || '其他',
            timeText: item.workTimeDesc || '不限时间',
            description: item.briefIntro || item.description || '',
            image:
               item.coverImages && item.coverImages.length > 0
                  ? item.coverImages[0]
                  : 'https://picsum.photos/seed/job/100/100'
         }));
      }
   } catch (error) {
      console.error('获取兼职列表失败:', error);
   }
};

onMounted(() => {
   fetchJobList();
});

// 处理搜索
const handleSearch = () => {
   uni.navigateTo({
      url: '/pages/tabar/home/prartlst/index'
   });
};

// 跳转兼职列表
const goToPartTime = (type: string) => {
   uni.navigateTo({
      url: `/pages/tabar/home/prartlst/index?jobType=${type}`
   });
};

// 跳转兼职列表页
const goToPartTimeList = () => {
   uni.navigateTo({
      url: '/pages/tabar/home/parttime/index'
   });
};

// 跳转兼职详情
const goToJobDetail = (id: number) => {
   uni.navigateTo({
      url: `/pages/tabar/home/details/index?id=${id}`
   });
};

// 跳转我的页面
const goToMine = () => {
   uni.switchTab({
      url: '/pages/tabar/index'
   });
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
   padding-top: calc(var(--status-bar-height) + 90rpx);
}

.navbar {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   z-index: 999;
   background-color: #fff;
   padding-top: var(--status-bar-height);
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.navbar-content {
   display: flex;
   align-items: center;
   justify-content: space-between;
   padding: 16rpx 24rpx;
}

.location {
   display: flex;
   align-items: center;
   gap: 8rpx;
   padding: 12rpx 16rpx;
   background-color: #f1f5f9;
   border-radius: 24rpx;
}

.location-text {
   font-size: 26rpx;
   color: #1e293b;
   font-weight: 500;
}

.search-box {
   flex: 1;
   display: flex;
   align-items: center;
   gap: 12rpx;
   margin: 0 20rpx;
   padding: 16rpx 24rpx;
   background-color: #f1f5f9;
   border-radius: 32rpx;
}

.search-placeholder {
   font-size: 26rpx;
   color: #9ca3af;
}

.user-avatar {
   width: 64rpx;
   height: 64rpx;
   border-radius: 50%;
   border: 2rpx solid #eff6ff;
}

.banner-swiper {
   width: 100%;
   height: 300rpx;
}

.banner-item {
   position: relative;
   width: 100%;
   height: 100%;
}

.banner-img {
   width: 100%;
   height: 100%;
}

.banner-overlay {
   position: absolute;
   bottom: 0;
   left: 0;
   right: 0;
   padding: 24rpx;
   background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
}

.banner-title {
   display: block;
   font-size: 36rpx;
   font-weight: bold;
   color: #fff;
   margin-bottom: 8rpx;
}

.banner-subtitle {
   font-size: 24rpx;
   color: rgba(255, 255, 255, 0.8);
}

.category-section {
   background-color: #fff;
   padding: 24rpx;
   margin-top: 16rpx;
}

.category-grid {
   display: grid;
   grid-template-columns: repeat(4, 1fr);
   gap: 24rpx;
}

.category-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 12rpx;
}

.category-icon {
   width: 80rpx;
   height: 80rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   border-radius: 50%;
}

.category-name {
   font-size: 24rpx;
   color: #475569;
}

.section {
   background-color: #fff;
   margin-top: 16rpx;
   padding: 24rpx;
}

.section-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 20rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: bold;
   color: #1e293b;
}

.section-more {
   display: flex;
   align-items: center;
   gap: 8rpx;
}

.more-text {
   font-size: 24rpx;
   color: #3b82f6;
}

.job-list {
   display: flex;
   flex-direction: column;
   gap: 20rpx;
}

.job-card {
   display: flex;
   gap: 20rpx;
   padding: 20rpx;
   background-color: #fafbfc;
   border-radius: 16rpx;
   border: 1rpx solid #e2e8f0;
}

.job-image {
   width: 160rpx;
   height: 160rpx;
   border-radius: 12rpx;
}

.job-info {
   flex: 1;
   display: flex;
   flex-direction: column;
   justify-content: space-between;
}

.job-header {
   display: flex;
   justify-content: space-between;
   align-items: flex-start;
}

.job-title {
   font-size: 28rpx;
   font-weight: 600;
   color: #1e293b;
   flex: 1;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.job-salary {
   font-size: 28rpx;
   font-weight: bold;
   color: #10b981;
   margin-left: 12rpx;
}

.job-tags {
   display: flex;
   gap: 12rpx;
}

.job-tag {
   padding: 6rpx 16rpx;
   background-color: #eff6ff;
   border-radius: 8rpx;
   font-size: 22rpx;
   color: #3b82f6;

   &.online {
      background-color: #eff6ff;
      color: #3b82f6;
   }

   &.offline {
      background-color: #d1fae5;
      color: #10b981;
   }
}

.job-desc {
   font-size: 24rpx;
   color: #94a3b8;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
   overflow: hidden;
}

.bottom-space {
   height: 120rpx;
}
</style>
