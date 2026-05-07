<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <text class="back-icon">←</text>
         </view>
         <text class="header-title">兼职详情</text>
         <view class="share-btn" @click="shareJob">
            <text class="share-icon">↗️</text>
         </view>
      </view>

      <!-- 核心内容区 -->
      <scroll-view scroll-y class="content-scroll">
         <!-- 图片轮播区 -->
         <view class="banner-section">
            <swiper
               class="banner-swiper"
               :indicator-dots="true"
               :autoplay="true"
               :interval="3000"
               :circular="true"
               indicator-color="rgba(255,255,255,0.5)"
               indicator-active-color="#fff">
               <swiper-item v-for="(img, idx) in bannerImages" :key="idx">
                  <image
                     :src="img"
                     class="banner-img"
                     mode="aspectFill"></image>
               </swiper-item>
            </swiper>
            <view class="banner-tags">
               <text class="banner-tag urgent">急招</text>
               <text class="banner-tag high-salary">高薪</text>
            </view>
            <view class="collect-btn" @click="toggleCollect">
               <text class="collect-icon">{{ isCollected ? '❤️' : '🤍' }}</text>
            </view>
         </view>

         <!-- 核心信息区 -->
         <view class="info-card" v-if="jobDetail">
            <text class="job-title">{{ jobDetail.title }}</text>
            <view class="salary-row">
               <view class="salary-wrap">
                  <text class="salary-num"
                     >¥{{
                        jobDetail.salaryMin || jobDetail.salaryMax || 0
                     }}</text
                  >
                  <text class="salary-unit">/天</text>
               </view>
               <view class="salary-tags">
                  <text class="salary-tag">{{
                     jobDetail.jobTypeDesc || '其他'
                  }}</text>
                  <text class="salary-tag green">{{
                     jobDetail.settlementCycleDesc || '结算'
                  }}</text>
                  <text class="salary-tag purple">学生可做</text>
               </view>
            </view>
            <view class="base-info-grid">
               <view class="base-info-item">
                  <view class="info-icon-wrap">
                     <text class="info-icon">⏰</text>
                  </view>
                  <text class="info-text"
                     >工作时间：{{ jobDetail.workTimeDesc || '不限' }}</text
                  >
               </view>
               <view class="base-info-item">
                  <view class="info-icon-wrap">
                     <text class="info-icon">📍</text>
                  </view>
                  <text class="info-text"
                     >工作地点：{{ jobDetail.regionName || '线上办公' }}</text
                  >
               </view>
               <view class="base-info-item">
                  <view class="info-icon-wrap">
                     <text class="info-icon">👥</text>
                  </view>
                  <text class="info-text"
                     >招聘人数：{{ jobDetail.recruitNum || 1 }}人</text
                  >
               </view>
               <view class="base-info-item">
                  <view class="info-icon-wrap">
                     <text class="info-icon">📅</text>
                  </view>
                  <text class="info-text"
                     >有效期：{{ jobDetail.remainingValidDays || 30 }}天</text
                  >
               </view>
            </view>
            <view class="publish-info">
               <text class="publish-text"
                  >发布于：{{ formatDate(jobDetail.publishTime) }} | 已被浏览
                  1258 次</text
               >
            </view>
         </view>

         <!-- 岗位描述 -->
         <view class="info-card">
            <view class="section-header">
               <view class="section-icon-wrap blue">
                  <text class="section-icon">📄</text>
               </view>
               <text class="section-title">岗位描述</text>
            </view>
            <view class="job-desc-content">
               <text class="desc-item" v-if="jobDetail?.description">{{
                  jobDetail.description
               }}</text>
               <text class="desc-item" v-else>暂无岗位描述</text>
            </view>
            <view class="job-desc-images" v-if="jobDescImages.length">
               <image
                  v-for="(img, idx) in jobDescImages"
                  :key="idx"
                  :src="img"
                  class="desc-img"
                  mode="aspectFill"></image>
            </view>
         </view>

         <!-- 任职要求 -->
         <view class="info-card">
            <view class="section-header">
               <view class="section-icon-wrap green">
                  <text class="section-icon">📋</text>
               </view>
               <text class="section-title">任职要求</text>
            </view>
            <view class="job-desc-content">
               <text class="desc-item"
                  >1. 熟练使用剪映、PR、AE等剪辑软件，有相关剪辑经验；</text
               >
               <text class="desc-item"
                  >2. 具备良好的审美能力，能独立完成视频剪辑和制作；</text
               >
               <text class="desc-item"
                  >3. 工作认真负责，有耐心，能按时完成任务；</text
               >
               <text class="desc-item"
                  >4. 无需坐班，时间自由，按量结算薪资；</text
               >
               <text class="desc-item"
                  >5. 有短视频剪辑作品集优先（可附链接/图片）。</text
               >
            </view>
         </view>

         <!-- 薪资说明 -->
         <view class="info-card">
            <view class="section-header">
               <view class="section-icon-wrap yellow">
                  <text class="section-icon">💰</text>
               </view>
               <text class="section-title">薪资说明</text>
            </view>
            <view class="salary-desc-content">
               <view class="salary-box">
                  <text class="salary-box-title">薪资构成</text>
                  <text class="salary-box-item"
                     >• 基础薪资：180元/天（完成基础剪辑任务）</text
                  >
                  <text class="salary-box-item"
                     >• 提成奖励：根据视频播放量额外奖励（10-50元/条）</text
                  >
                  <text class="salary-box-item"
                     >• 全勤奖励：月完成20天以上额外奖励500元</text
                  >
               </view>
               <text class="desc-item">• 结算方式：日结，次日18点前到账</text>
               <text class="desc-item">• 结算平台：微信/支付宝（二选一）</text>
            </view>
         </view>

         <!-- 企业信息 -->
         <view class="info-card">
            <view class="section-header">
               <view class="section-icon-wrap purple">
                  <text class="section-icon">🏢</text>
               </view>
               <text class="section-title">企业信息</text>
            </view>
            <view class="company-info">
               <image
                  :src="companyInfo.logo"
                  class="company-logo"
                  mode="aspectFill"></image>
               <view class="company-details">
                  <text class="company-name">{{ companyInfo.name }}</text>
                  <view class="company-tags">
                     <text class="company-tag">已认证</text>
                     <text class="company-tag gray">文化传媒</text>
                     <text class="company-tag gray">50-100人</text>
                  </view>
                  <text class="company-address">{{ companyInfo.address }}</text>
               </view>
               <view class="follow-btn" @click="toggleFollow">
                  <text class="follow-text">{{
                     isFollowed ? '✓ 已关注' : '+ 关注'
                  }}</text>
               </view>
            </view>
            <view class="company-cert">
               <text class="cert-title">企业认证资质</text>
               <view class="cert-images">
                  <image
                     v-for="(img, idx) in companyCertImages"
                     :key="idx"
                     :src="img"
                     class="cert-img"
                     mode="aspectFill"></image>
               </view>
            </view>
         </view>
      </scroll-view>

      <!-- 底部固定操作栏 -->
      <view class="bottom-bar">
         <view class="bottom-actions">
            <view class="action-item" @click="toggleCollect">
               <text class="action-icon">{{ isCollected ? '❤️' : '🤍' }}</text>
               <text class="action-text">收藏</text>
            </view>
            <view class="action-item" @click="goToConsult">
               <text class="action-icon">💬</text>
               <text class="action-text">咨询</text>
            </view>
         </view>
         <button class="apply-btn" @click="handleApply">立即应聘</button>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import * as apis from '@/api/job';

const jobDetail = ref<any>(null);
const bannerImages = ref<string[]>([]);
const jobDescImages = ref<string[]>([]);
const companyCertImages = ref<string[]>([]);
const companyInfo = ref({
   name: '',
   logo: '',
   address: ''
});

const isCollected = ref(false);
const isFollowed = ref(false);

// 获取兼职详情
const fetchJobDetail = async () => {
   try {
      // 获取URL参数中的兼职ID
      const pages = getCurrentPages();
      const currentPage = pages[pages.length - 1];
      const options = (currentPage as any).$page?.options || {};
      const jobId = options.id || 1;

      const res = await apis.getJobDetail(Number(jobId));
      if (res.code === 0 && res.data) {
         jobDetail.value = res.data;

         // 处理封面图片
         if (res.data.coverImages && Array.isArray(res.data.coverImages)) {
            bannerImages.value = res.data.coverImages;
         } else {
            bannerImages.value = [
               'https://picsum.photos/seed/jobdefault/750/400'
            ];
         }

         // 处理岗位描述图片
         if (res.data.jobDescCover) {
            jobDescImages.value = [res.data.jobDescCover];
         } else {
            jobDescImages.value = [
               'https://picsum.photos/seed/jobdesc1/200/200',
               'https://picsum.photos/seed/jobdesc2/200/200',
               'https://picsum.photos/seed/jobdesc3/200/200'
            ];
         }

         // 处理企业信息
         if (res.data.merchantInfo) {
            companyInfo.value = {
               name: res.data.merchantInfo.merchantName || '未知企业',
               logo:
                  res.data.merchantInfo.avatarUrl ||
                  'https://picsum.photos/seed/companylogo/100/100',
               address: res.data.merchantInfo.address || '地址未知'
            };
            // 企业认证图片
            companyCertImages.value = [
               'https://picsum.photos/seed/company1/100/100',
               'https://picsum.photos/seed/company2/100/100'
            ];
         }
      }
   } catch (error) {
      console.error('获取兼职详情失败:', error);
   }
};

onMounted(() => {
   fetchJobDetail();
});

// 格式化日期
const formatDate = (dateStr: string) => {
   if (!dateStr) return '未知时间';
   return dateStr.replace('T', ' ').substring(0, 16);
};

const goBack = () => {
   uni.navigateBack();
};

const shareJob = () => {
   uni.showToast({
      title: '分享功能开发中',
      icon: 'none'
   });
};

const toggleCollect = () => {
   isCollected.value = !isCollected.value;
   uni.showToast({
      title: isCollected.value ? '已收藏该兼职' : '已取消收藏',
      icon: 'none'
   });
};

const toggleFollow = () => {
   isFollowed.value = !isFollowed.value;
   uni.showToast({
      title: isFollowed.value ? '已关注该企业' : '已取消关注',
      icon: 'none'
   });
};

const goToConsult = () => {
   uni.showToast({
      title: '咨询功能开发中',
      icon: 'none'
   });
};

const handleApply = () => {
   uni.showModal({
      title: '确认应聘',
      content: '确定要应聘该兼职吗？企业将在1个工作日内联系你。',
      success: res => {
         if (res.confirm) {
            uni.showToast({
               title: '应聘成功！',
               icon: 'success'
            });
         }
      }
   });
};
</script>

<style lang="scss" scoped>
.page {
   min-height: 100vh;
   background-color: #f8fafc;
   padding-bottom: 160rpx;
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

.share-btn {
   width: 72rpx;
   height: 72rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.share-icon {
   font-size: 36rpx;
}

.content-scroll {
   height: calc(100vh - 120rpx);
}

.banner-section {
   position: relative;
   width: 100%;
   height: 480rpx;
}

.banner-swiper {
   width: 100%;
   height: 100%;
}

.banner-img {
   width: 100%;
   height: 100%;
}

.banner-tags {
   position: absolute;
   top: 24rpx;
   left: 24rpx;
   display: flex;
   gap: 12rpx;
}

.banner-tag {
   font-size: 22rpx;
   padding: 8rpx 16rpx;
   border-radius: 8rpx;
   color: #fff;

   &.urgent {
      background-color: #ef4444;
   }

   &.high-salary {
      background-color: #10b981;
   }
}

.collect-btn {
   position: absolute;
   top: 24rpx;
   right: 24rpx;
   width: 80rpx;
   height: 80rpx;
   background-color: rgba(255, 255, 255, 0.8);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;

   &:active {
      opacity: 0.8;
   }
}

.collect-icon {
   font-size: 40rpx;
}

.info-card {
   background-color: #fff;
   padding: 32rpx;
   margin-bottom: 24rpx;
}

.job-title {
   font-size: 40rpx;
   font-weight: 700;
   color: #1e293b;
   display: block;
   margin-bottom: 24rpx;
}

.salary-row {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 32rpx;
}

.salary-wrap {
   display: flex;
   align-items: baseline;
}

.salary-num {
   font-size: 48rpx;
   font-weight: 700;
   color: #10b981;
}

.salary-unit {
   font-size: 28rpx;
   color: #64748b;
   margin-left: 8rpx;
}

.salary-tags {
   display: flex;
   gap: 12rpx;
}

.salary-tag {
   font-size: 22rpx;
   padding: 8rpx 16rpx;
   border-radius: 8rpx;
   background-color: #eff6ff;
   color: #3b82f6;

   &.green {
      background-color: #f0fdf4;
      color: #16a34a;
   }

   &.purple {
      background-color: #faf5ff;
      color: #9333ea;
   }
}

.base-info-grid {
   display: grid;
   grid-template-columns: 1fr 1fr;
   gap: 24rpx;
}

.base-info-item {
   display: flex;
   align-items: center;
}

.info-icon-wrap {
   width: 64rpx;
   height: 64rpx;
   background-color: #f1f5f9;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-right: 16rpx;
}

.info-icon {
   font-size: 28rpx;
}

.info-text {
   font-size: 26rpx;
   color: #64748b;
}

.publish-info {
   margin-top: 24rpx;
   padding-top: 24rpx;
   border-top: 1rpx solid #f1f5f9;
}

.publish-text {
   font-size: 22rpx;
   color: #94a3b8;
}

.section-header {
   display: flex;
   align-items: center;
   margin-bottom: 24rpx;
}

.section-icon-wrap {
   width: 64rpx;
   height: 64rpx;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-right: 16rpx;

   &.blue {
      background-color: #eff6ff;
   }

   &.green {
      background-color: #f0fdf4;
   }

   &.yellow {
      background-color: #fef9c3;
   }

   &.purple {
      background-color: #faf5ff;
   }
}

.section-icon {
   font-size: 28rpx;
}

.section-title {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
}

.job-desc-content {
   display: flex;
   flex-direction: column;
   gap: 12rpx;
}

.desc-item {
   font-size: 26rpx;
   color: #64748b;
   line-height: 1.6;
}

.job-desc-images {
   display: grid;
   grid-template-columns: repeat(3, 1fr);
   gap: 16rpx;
   margin-top: 24rpx;
}

.desc-img {
   width: 100%;
   height: 160rpx;
   border-radius: 12rpx;
}

.salary-desc-content {
   display: flex;
   flex-direction: column;
   gap: 16rpx;
}

.salary-box {
   background-color: #fef9c3;
   border-radius: 12rpx;
   padding: 24rpx;
}

.salary-box-title {
   font-size: 26rpx;
   font-weight: 600;
   color: #ca8a04;
   display: block;
   margin-bottom: 12rpx;
}

.salary-box-item {
   font-size: 24rpx;
   color: #64748b;
   display: block;
   margin-bottom: 8rpx;
}

.company-info {
   display: flex;
   align-items: flex-start;
}

.company-logo {
   width: 128rpx;
   height: 128rpx;
   border-radius: 50%;
   border: 1rpx solid #f1f5f9;
   margin-right: 24rpx;
}

.company-details {
   flex: 1;
}

.company-name {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
   display: block;
   margin-bottom: 12rpx;
}

.company-tags {
   display: flex;
   gap: 12rpx;
   margin-bottom: 12rpx;
}

.company-tag {
   font-size: 22rpx;
   padding: 4rpx 16rpx;
   border-radius: 6rpx;
   background-color: #eff6ff;
   color: #3b82f6;

   &.gray {
      background-color: #f8fafc;
      color: #64748b;
   }
}

.company-address {
   font-size: 22rpx;
   color: #94a3b8;
}

.follow-btn {
   padding: 12rpx 24rpx;
   border: 2rpx solid #3b82f6;
   border-radius: 24rpx;

   &:active {
      opacity: 0.8;
   }
}

.follow-text {
   font-size: 24rpx;
   color: #3b82f6;
}

.company-cert {
   margin-top: 32rpx;
}

.cert-title {
   font-size: 22rpx;
   color: #94a3b8;
   display: block;
   margin-bottom: 16rpx;
}

.cert-images {
   display: flex;
   gap: 16rpx;
   overflow-x: auto;
}

.cert-img {
   width: 160rpx;
   height: 160rpx;
   border-radius: 12rpx;
   flex-shrink: 0;
}

.bottom-bar {
   position: fixed;
   bottom: 0;
   left: 0;
   right: 0;
   background-color: #fff;
   padding: 24rpx 32rpx;
   padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
   display: flex;
   align-items: center;
   justify-content: space-between;
   border-top: 1rpx solid #e2e8f0;
   box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.bottom-actions {
   display: flex;
   gap: 48rpx;
}

.action-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 8rpx;

   &:active {
      opacity: 0.7;
   }
}

.action-icon {
   font-size: 40rpx;
}

.action-text {
   font-size: 22rpx;
   color: #64748b;
}

.apply-btn {
   flex: 1;
   height: 88rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   font-size: 30rpx;
   color: #fff;
   font-weight: 600;
   margin-left: 32rpx;

   &:active {
      opacity: 0.9;
   }
}
</style>
