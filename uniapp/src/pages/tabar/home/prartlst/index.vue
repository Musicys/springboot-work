<template>
   <view class="page">
      <!-- 顶部导航：返回 + 标题 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <text class="back-icon">←</text>
         </view>
         <text class="header-title">兼职列表</text>
         <view class="placeholder"></view>
      </view>

      <!-- 搜索 -->
      <view class="search-bar">
         <view class="search-wrap">
            <text class="search-icon">🔍</text>
            <input
               type="text"
               v-model="searchKeyword"
               placeholder="搜索兼职/企业/关键词"
               class="search-input" />
         </view>
      </view>

      <!-- 筛选 -->
      <view class="filter-bar">
         <view class="filter-tabs">
            <text
               class="filter-tab"
               :class="{ active: currentTab === 'comprehensive' }"
               @click="switchTab('comprehensive')"
               >综合</text
            >
            <text
               class="filter-tab"
               :class="{ active: currentTab === 'salary' }"
               @click="switchTab('salary')"
               >薪资</text
            >
            <text
               class="filter-tab"
               :class="{ active: currentTab === 'distance' }"
               @click="switchTab('distance')"
               >距离</text
            >
         </view>
         <view class="filter-btn" @click="showFilter">
            <text class="filter-icon">☰</text>
            <text class="filter-text">筛选</text>
         </view>
      </view>

      <!-- 兼职列表 -->
      <scroll-view scroll-y class="job-list" @scrolltolower="loadMore">
         <view
            class="job-card"
            v-for="job in jobList"
            :key="job.id"
            @click="goToDetail(job.id)">
            <view class="job-content">
               <image
                  :src="job.coverImage"
                  class="job-image"
                  mode="aspectFill"></image>
               <view class="job-info">
                  <text class="job-title">{{ job.title }}</text>
                  <text class="job-salary"
                     >¥{{ job.salaryMin }}-{{ job.salaryMax }}/天</text
                  >
                  <text class="job-desc">{{ job.briefIntro }}</text>
                  <view class="job-tags">
                     <text
                        class="tag tag-type"
                        :class="
                           job.jobTypeDesc === '线上'
                              ? 'tag-online'
                              : 'tag-offline'
                        "
                        >{{ job.jobTypeDesc }}</text
                     >
                     <text class="tag tag-settle">{{
                        job.settlementCycleDesc
                     }}</text>
                  </view>
               </view>
            </view>
         </view>

         <!-- 加载更多 -->
         <view class="load-more" v-if="loading">
            <text class="load-text">加载中...</text>
         </view>
         <view class="load-more" v-else-if="!hasMore">
            <text class="load-text">已加载全部</text>
         </view>
      </scroll-view>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import * as apis from '@/api/job';
import { onLoad } from '@dcloudio/uni-app';

interface Job {
   id: number;
   title: string;
   salaryMin: number;
   salaryMax: number;
   briefIntro: string;
   jobTypeDesc: string;
   settlementCycleDesc: string;
   coverImage: string;
}

const searchKeyword = ref('');
const currentTab = ref('comprehensive');
const jobList = ref<Job[]>([]);
const pageNum = ref(1);
const pageSize = ref(10);
const loading = ref(false);
const hasMore = ref(true);
const jobType = ref<number | null>(null);

let searchTimer: number | null = null;

// 节流搜索
const throttleSearch = () => {
   if (searchTimer) {
      clearTimeout(searchTimer);
   }
   searchTimer = setTimeout(() => {
      pageNum.value = 1;
      fetchJobList(false);
   }, 500) as unknown as number;
};

// 监听搜索关键词变化
watch(searchKeyword, () => {
   throttleSearch();
});

// 获取URL参数
onLoad((options: any) => {
   if (options.jobType) {
      jobType.value = Number(options.jobType);
   }
});

// 获取兼职列表
const fetchJobList = async (isLoadMore = false) => {
   if (loading.value) return;

   loading.value = true;
   try {
      const params: any = {
         pageNum: pageNum.value,
         pageSize: pageSize.value
      };

      // 添加jobType筛选条件
      if (jobType.value) {
         params.jobType = jobType.value;
      }

      // 添加搜索关键词
      if (searchKeyword.value) {
         params.keyword = searchKeyword.value;
      }

      const res = await apis.getJobList(params);

      if (res.code === 0 && res.data && res.data.records) {
         const records = res.data.records as any[];
         const jobs: Job[] = records.map(item => ({
            id: item.id,
            title: item.title || '',
            salaryMin: item.salaryMin || 0,
            salaryMax: item.salaryMax || 0,
            briefIntro: item.briefIntro || item.description || '',
            jobTypeDesc: item.jobTypeDesc || '其他',
            settlementCycleDesc: item.settlementCycleDesc || '结算',
            coverImage:
               item.coverImage || 'https://picsum.photos/seed/job/200/200'
         }));

         if (isLoadMore) {
            jobList.value = [...jobList.value, ...jobs];
         } else {
            jobList.value = jobs;
         }

         // 判断是否还有更多数据
         hasMore.value = records.length >= pageSize.value;
      } else {
         hasMore.value = false;
      }
   } catch (error) {
      console.error('获取兼职列表失败:', error);
   } finally {
      loading.value = false;
   }
};

// 加载更多
const loadMore = () => {
   if (hasMore.value && !loading.value) {
      pageNum.value++;
      fetchJobList(true);
   }
};

const goBack = () => {
   uni.navigateBack();
};

const switchTab = (tab: string) => {
   currentTab.value = tab;
   pageNum.value = 1;
   fetchJobList(false);
};

const showFilter = () => {
   uni.showToast({
      title: '筛选功能开发中',
      icon: 'none'
   });
};

const goToDetail = (id: number) => {
   uni.navigateTo({
      url: `/pages/tabar/home/details/index?id=${id}`
   });
};

onMounted(() => {
   fetchJobList(false);
});
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
}

.header {
   position: sticky;
   top: 0;
   z-index: 100;
   background-color: #fff;
   padding: 24rpx 32rpx;
   display: flex;
   align-items: center;
   justify-content: space-between;
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.back-btn {
   width: 72rpx;
   height: 72rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.back-icon {
   font-size: 40rpx;
   color: #333;
}

.header-title {
   font-size: 34rpx;
   font-weight: 600;
   color: #1e293b;
}

.placeholder {
   width: 72rpx;
}

.search-bar {
   background-color: #fff;
   padding: 24rpx 32rpx;
}

.search-wrap {
   position: relative;
   display: flex;
   align-items: center;
}

.search-icon {
   position: absolute;
   left: 24rpx;
   font-size: 28rpx;
}

.search-input {
   width: 100%;
   height: 72rpx;
   padding: 0 24rpx 0 72rpx;
   background-color: #f1f5f9;
   border-radius: 36rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;
}

.filter-bar {
   background-color: #fff;
   padding: 16rpx 32rpx;
   display: flex;
   justify-content: space-between;
   align-items: center;
   border-top: 1rpx solid #f1f5f9;
}

.filter-tabs {
   display: flex;
   gap: 32rpx;
}

.filter-tab {
   font-size: 28rpx;
   color: #64748b;

   &.active {
      color: #3b82f6;
      font-weight: 600;
   }
}

.filter-btn {
   display: flex;
   align-items: center;
   gap: 8rpx;
}

.filter-icon {
   font-size: 24rpx;
}

.filter-text {
   font-size: 28rpx;
   color: #64748b;
}

.job-list {
   height: calc(100vh - 300rpx);
   padding: 24rpx;
}

.job-card {
   background-color: #fff;
   border-radius: 20rpx;
   padding: 24rpx;
   margin-bottom: 24rpx;
   box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

   &:active {
      opacity: 0.9;
   }
}

.job-content {
   display: flex;
   gap: 20rpx;
}

.job-image {
   width: 160rpx;
   height: 160rpx;
   border-radius: 16rpx;
}

.job-info {
   flex: 1;
   display: flex;
   flex-direction: column;
   min-width: 0;
}

.job-title {
   font-size: 28rpx;
   font-weight: 500;
   color: #1e293b;
   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
}

.job-salary {
   font-size: 28rpx;
   font-weight: 600;
   color: #3b82f6;
   margin-top: 8rpx;
}

.job-desc {
   font-size: 22rpx;
   color: #94a3b8;
   margin-top: 8rpx;
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
}

.job-tags {
   display: flex;
   gap: 12rpx;
   margin-top: 12rpx;
}

.tag {
   font-size: 22rpx;
   padding: 4rpx 16rpx;
   border-radius: 6rpx;

   &.tag-online {
      background-color: #eff6ff;
      color: #3b82f6;
   }

   &.tag-offline {
      background-color: #f0fdf4;
      color: #16a34a;
   }

   &.tag-settle {
      background-color: #f8fafc;
      color: #64748b;
   }
}
.load-more {
   padding: 32rpx;
   text-align: center;
}

.load-text {
   font-size: 26rpx;
   color: #94a3b8;
}
</style>
