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
import { useUserStore } from '@/store/user';

const userStore = useUserStore();

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
      const pages = getCurrentPages();
      const currentPage = pages[pages.length - 1];
      const options = (currentPage as any).$page?.options || {};
      const jobId = options.id || 1;

      const res = await apis.getJobDetail(Number(jobId));
      if (res.code === 0 && res.data) {
         jobDetail.value = res.data;

         if (res.data.coverImages && Array.isArray(res.data.coverImages)) {
            bannerImages.value = res.data.coverImages;
         } else {
            bannerImages.value = [
               'https://picsum.photos/seed/jobdefault/750/400'
            ];
         }

         if (res.data.jobDescCover) {
            jobDescImages.value = [res.data.jobDescCover];
         } else {
            jobDescImages.value = [
               'https://picsum.photos/seed/jobdesc1/200/200',
               'https://picsum.photos/seed/jobdesc2/200/200',
               'https://picsum.photos/seed/jobdesc3/200/200'
            ];
         }

         if (res.data.merchantInfo) {
            companyInfo.value = {
               name: res.data.merchantInfo.merchantName || '未知企业',
               logo:
                  res.data.merchantInfo.avatarUrl ||
                  'https://picsum.photos/seed/companylogo/100/100',
               address: res.data.merchantInfo.address || '地址未知'
            };
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

const handleApply = async () => {
   const pages = getCurrentPages();
   const currentPage = pages[pages.length - 1];
   const options = (currentPage as any).$page?.options || {};
   const jobId = options.id;

   if (!jobId) {
      uni.showToast({
         title: '获取兼职信息失败',
         icon: 'none'
      });
      return;
   }

   const currentUserId = userStore.userInfo?.userId;
   if (!currentUserId) {
      uni.showToast({
         title: '请先登录',
         icon: 'none'
      });
      return;
   }

   uni.showModal({
      title: '确认应聘',
      content: '确定要应聘该兼职吗？企业将在1个工作日内联系你。',
      success: async res => {
         if (res.confirm) {
            try {
               const result = await apis.applyJob({
                  jobId: Number(jobId),
                  userId: currentUserId
               });
               if (result.code === 0) {
                  uni.showToast({
                     title: '应聘成功！',
                     icon: 'success'
                  });
               } else {
                  uni.showToast({
                     title: result.message || '应聘失败',
                     icon: 'none'
                  });
               }
            } catch (error) {
               console.error('应聘失败:', error);
            }
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
   padding: 0 24rpx;
}

.banner-section {
   position: relative;
   margin-top: 24rpx;
}

.banner-swiper {
   width: 100%;
   height: 360rpx;
   border-radius: 16rpx;
   overflow: hidden;
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
   padding: 8rpx 16rpx;
   border-radius: 8rpx;
   font-size: 22rpx;
   font-weight: 500;
}

.banner-tag.urgent {
   background: linear-gradient(135deg, #ff6b6b, #ee5a5a);
   color: #fff;
}

.banner-tag.high-salary {
   background: linear-gradient(135deg, #ffa502, #ff7f00);
   color: #fff;
}

.collect-btn {
   position: absolute;
   top: 24rpx;
   right: 24rpx;
   width: 72rpx;
   height: 72rpx;
   background: rgba(255, 255, 255, 0.9);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.collect-icon {
   font-size: 36rpx;
}

.info-card {
   background: #fff;
   border-radius: 16rpx;
   padding: 32rpx;
   margin-top: 24rpx;
}

.job-title {
   font-size: 36rpx;
   font-weight: 600;
   color: #1e293b;
   line-height: 1.4;
}

.salary-row {
   display: flex;
   align-items: center;
   justify-content: space-between;
   margin-top: 20rpx;
}

.salary-wrap {
   display: flex;
   align-items: baseline;
}

.salary-num {
   font-size: 48rpx;
   font-weight: 700;
   color: #ff6b6b;
}

.salary-unit {
   font-size: 24rpx;
   color: #999;
   margin-left: 4rpx;
}

.salary-tags {
   display: flex;
   gap: 8rpx;
}

.salary-tag {
   padding: 6rpx 12rpx;
   background: #f0f9ff;
   color: #0077ff;
   border-radius: 6rpx;
   font-size: 20rpx;
}

.salary-tag.green {
   background: #f0fdf4;
   color: #16a34a;
}

.salary-tag.purple {
   background: #faf5ff;
   color: #9333ea;
}

.base-info-grid {
   margin-top: 24rpx;
   display: flex;
   flex-direction: column;
   gap: 16rpx;
}

.base-info-item {
   display: flex;
   align-items: center;
   gap: 12rpx;
}

.info-icon-wrap {
   width: 48rpx;
   height: 48rpx;
   background: #f8fafc;
   border-radius: 12rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.info-icon {
   font-size: 24rpx;
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
   gap: 12rpx;
   margin-bottom: 24rpx;
}

.section-icon-wrap {
   width: 56rpx;
   height: 56rpx;
   border-radius: 14rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.section-icon-wrap.blue {
   background: #eff6ff;
}

.section-icon-wrap.green {
   background: #f0fdf4;
}

.section-icon-wrap.yellow {
   background: #fefce8;
}

.section-icon-wrap.purple {
   background: #faf5ff;
}

.section-icon {
   font-size: 28rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: 600;
   color: #1e293b;
}

.job-desc-content {
   display: flex;
   flex-direction: column;
   gap: 16rpx;
}

.desc-item {
   font-size: 26rpx;
   color: #475569;
   line-height: 1.6;
}

.job-desc-images {
   display: flex;
   gap: 16rpx;
   margin-top: 24rpx;
   flex-wrap: wrap;
}

.desc-img {
   width: 200rpx;
   height: 200rpx;
   border-radius: 12rpx;
}

.salary-desc-content {
   display: flex;
   flex-direction: column;
   gap: 16rpx;
}

.salary-box {
   background: #fff7ed;
   border-radius: 12rpx;
   padding: 24rpx;
   display: flex;
   flex-direction: column;
   gap: 12rpx;
}

.salary-box-title {
   font-size: 28rpx;
   font-weight: 600;
   color: #9a3412;
}

.salary-box-item {
   font-size: 24rpx;
   color: #7c2d12;
   line-height: 1.5;
}

.company-info {
   display: flex;
   align-items: flex-start;
   gap: 20rpx;
}

.company-logo {
   width: 100rpx;
   height: 100rpx;
   border-radius: 12rpx;
}

.company-details {
   flex: 1;
   display: flex;
   flex-direction: column;
   gap: 8rpx;
}

.company-name {
   font-size: 30rpx;
   font-weight: 600;
   color: #1e293b;
}

.company-tags {
   display: flex;
   gap: 8rpx;
   flex-wrap: wrap;
}

.company-tag {
   padding: 4rpx 12rpx;
   background: #f0fdf4;
   color: #16a34a;
   border-radius: 6rpx;
   font-size: 20rpx;
}

.company-tag.gray {
   background: #f8fafc;
   color: #64748b;
}

.company-address {
   font-size: 22rpx;
   color: #94a3b8;
}

.follow-btn {
   padding: 12rpx 24rpx;
   background: #fff;
   border: 2rpx solid #e2e8f0;
   border-radius: 10rpx;
}

.follow-text {
   font-size: 24rpx;
   color: #64748b;
}

.company-cert {
   margin-top: 32rpx;
   padding-top: 32rpx;
   border-top: 1rpx solid #f1f5f9;
}

.cert-title {
   font-size: 26rpx;
   color: #64748b;
   margin-bottom: 16rpx;
}

.cert-images {
   display: flex;
   gap: 16rpx;
}

.cert-img {
   width: 160rpx;
   height: 160rpx;
   border-radius: 12rpx;
}

.bottom-bar {
   position: fixed;
   bottom: 0;
   left: 0;
   right: 0;
   background: #fff;
   padding: 16rpx 32rpx;
   padding-bottom: calc(16rpx + constant(safe-area-inset-bottom));
   padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
   display: flex;
   align-items: center;
   gap: 24rpx;
   box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.bottom-actions {
   display: flex;
   gap: 32rpx;
}

.action-item {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 4rpx;
}

.action-icon {
   font-size: 36rpx;
}

.action-text {
   font-size: 20rpx;
   color: #64748b;
}

.apply-btn {
   flex: 1;
   height: 88rpx;
   background: linear-gradient(135deg, #ff6b6b, #ee5a5a);
   color: #fff;
   border-radius: 44rpx;
   font-size: 30rpx;
   font-weight: 600;
   display: flex;
   align-items: center;
   justify-content: center;
   border: none;
}
</style>
