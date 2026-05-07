<template>
   <view class="page">
      <!-- 顶部导航 -->
      <view class="header">
         <view class="back-btn" @click="goBack">
            <text class="back-icon">←</text>
         </view>
         <text class="header-title">订单列表</text>
         <view class="placeholder"></view>
      </view>

      <!-- 状态切换 -->
      <view class="status-bar">
         <view
            class="status-item"
            v-for="(item, index) in statusList"
            :key="index"
            :class="{ active: currentStatus === item.value }"
            @click="switchStatus(item.value)">
            <text class="status-text">{{ item.label }}</text>
         </view>
      </view>

      <!-- 订单列表 -->
      <scroll-view scroll-y class="order-list" @scrolltolower="loadMore">
         <view class="order-card" v-for="order in orderList" :key="order.id">
            <!-- 订单头部 -->
            <view class="order-header">
               <text class="order-title">{{ order.jobTitle }}</text>
               <text
                  class="order-status"
                  :class="getStatusClass(order.orderStatus)">
                  {{ getStatusText(order.orderStatus) }}
               </text>
            </view>

            <!-- 订单内容 -->
            <view class="order-content">
               <image
                  :src="order.coverImage"
                  class="order-image"
                  mode="aspectFill"></image>
               <view class="order-info">
                  <text class="job-desc">{{ order.jobDescription }}</text>
                  <view class="info-row">
                     <text class="company-name">{{ order.companyName }}</text>
                     <text
                        class="work-type"
                        :class="
                           order.workType === '线上' ? 'online' : 'offline'
                        ">
                        {{ order.workType }}
                     </text>
                  </view>
                  <view class="info-row">
                     <text class="location">
                        <text class="icon">📍</text>
                        {{ order.location }}
                     </text>
                     <text class="time">
                        <text class="icon">🕐</text>
                        {{ order.workTime }}
                     </text>
                  </view>
                  <view class="info-row money-row">
                     <view class="deposit">
                        <text class="label">押金：</text>
                        <text class="value">¥{{ order.deposit }}</text>
                     </view>
                     <view class="amount">
                        <text class="label">金额：</text>
                        <text class="value highlight">¥{{ order.amount }}</text>
                     </view>
                  </view>
               </view>
            </view>

            <!-- 操作按钮 -->
            <view class="order-actions">
               <view
                  v-if="order.orderStatus === 1"
                  class="action-btn cancel"
                  @click="cancelOrder(order.id)">
                  <text>取消</text>
               </view>
               <view
                  v-if="order.orderStatus === 1"
                  class="action-btn primary"
                  @click="payDeposit(order)">
                  <text>同意并支付押金</text>
               </view>

               <view
                  v-if="order.orderStatus === 2"
                  class="action-btn"
                  @click="contactMerchant(order.id)">
                  <text>联系商家</text>
               </view>
               <view
                  v-if="order.orderStatus === 2"
                  class="action-btn danger"
                  @click="openDispute(order.id)">
                  <text>异议申请</text>
               </view>
               <view
                  v-if="order.orderStatus === 2"
                  class="action-btn primary"
                  @click="confirmComplete(order.id)">
                  <text>确认完成</text>
               </view>

               <view
                  v-if="order.orderStatus === 3"
                  class="action-btn"
                  @click="viewDetail(order.id)">
                  <text>查看详情</text>
               </view>
               <view
                  v-if="order.orderStatus === 3"
                  class="action-btn danger"
                  @click="openDispute(order.id)">
                  <text>异议申请</text>
               </view>
               <view
                  v-if="order.orderStatus === 3"
                  class="action-btn primary"
                  @click="applyAgain(order.id)">
                  <text>再次应聘</text>
               </view>
            </view>
         </view>

         <!-- 加载更多 -->
         <view class="load-more" v-if="loading">
            <text>加载中...</text>
         </view>
         <view class="load-more" v-else-if="!hasMore">
            <text>已加载全部</text>
         </view>
      </scroll-view>

      <!-- 支付押金弹窗 -->
      <view
         class="modal-overlay"
         v-if="showPaymentModal"
         @click="closePaymentModal">
         <view class="modal-content payment-modal" @click.stop>
            <text class="modal-title">支付押金</text>
            <view class="payment-amount">
               <text class="amount-value">¥{{ paymentOrder?.deposit }}</text>
               <text class="amount-label">押金</text>
            </view>
            <view class="payment-info">
               <text class="info-title">{{ paymentOrder?.jobTitle }}</text>
               <text class="info-company">{{ paymentOrder?.companyName }}</text>
            </view>
            <text class="payment-tip">押金将在完成兼职后原路退回</text>
            <view class="payment-methods">
               <view class="method-item active">
                  <text class="method-icon">💳</text>
                  <text class="method-name">微信支付</text>
                  <text class="check-icon">✓</text>
               </view>
               <view class="method-item">
                  <text class="method-icon">📱</text>
                  <text class="method-name">支付宝</text>
               </view>
            </view>
            <button class="confirm-btn" @click="confirmPayment">
               确认支付
            </button>
         </view>
      </view>

      <!-- 支付成功弹窗 -->
      <view
         class="modal-overlay"
         v-if="showPaymentSuccessModal"
         @click="closePaymentSuccessModal">
         <view class="modal-content success-modal" @click.stop>
            <view class="success-icon">✓</view>
            <text class="modal-title">支付成功</text>
            <text class="success-desc">押金已支付，订单状态已更新为进行中</text>
            <button class="confirm-btn" @click="closePaymentSuccessModal">
               我知道了
            </button>
         </view>
      </view>

      <!-- 异议申请弹窗 -->
      <view
         class="modal-overlay"
         v-if="showDisputeModal"
         @click="closeDisputeModal">
         <view class="modal-content dispute-modal" @click.stop>
            <text class="modal-title">异议申请</text>
            <view class="form-item">
               <text class="form-label">异议类型</text>
               <picker
                  :value="disputeTypeIndex"
                  :range="disputeTypes"
                  @change="onDisputeTypeChange">
                  <view class="picker-value">
                     {{ disputeTypes[disputeTypeIndex] || '请选择异议类型' }}
                     <text class="picker-arrow">›</text>
                  </view>
               </picker>
            </view>
            <view class="form-item">
               <text class="form-label">异议描述</text>
               <textarea
                  class="dispute-textarea"
                  v-model="disputeDescription"
                  placeholder="请详细描述您的异议"></textarea>
            </view>
            <view class="form-item">
               <text class="form-label">上传凭证（选填）</text>
               <view class="upload-area">
                  <text class="upload-icon">📷</text>
                  <text class="upload-text">点击上传凭证</text>
               </view>
               <text class="upload-tip">支持图片格式，大小不超过 5MB</text>
            </view>
            <view class="dispute-actions">
               <button class="cancel-btn" @click="closeDisputeModal">
                  取消
               </button>
               <button class="submit-btn" @click="submitDispute">
                  提交申请
               </button>
            </view>
         </view>
      </view>

      <!-- 异议提交成功弹窗 -->
      <view
         class="modal-overlay"
         v-if="showDisputeSuccessModal"
         @click="closeDisputeSuccessModal">
         <view class="modal-content success-modal" @click.stop>
            <view class="success-icon">✓</view>
            <text class="modal-title">提交成功</text>
            <text class="success-desc">异议申请已提交，我们将尽快处理</text>
            <button class="confirm-btn" @click="closeDisputeSuccessModal">
               我知道了
            </button>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

// 状态列表
const statusList = ref([
   { label: '全部', value: 0 },
   { label: '已发起', value: 1 },
   { label: '进行中', value: 2 },
   { label: '已结算', value: 3 }
]);

const currentStatus = ref(0);

// 订单列表
interface Order {
   id: number;
   jobTitle: string;
   orderStatus: number;
   coverImage: string;
   jobDescription: string;
   companyName: string;
   workType: string;
   location: string;
   workTime: string;
   deposit: number;
   amount: string;
}

const orderList = ref<Order[]>([]);
const pageNum = ref(1);
const pageSize = ref(10);
const loading = ref(false);
const hasMore = ref(true);

// 弹窗状态
const showPaymentModal = ref(false);
const showPaymentSuccessModal = ref(false);
const showDisputeModal = ref(false);
const showDisputeSuccessModal = ref(false);
const paymentOrder = ref<Order | null>(null);
const currentOrderId = ref(0);

// 异议申请数据
const disputeTypes = ['薪资问题', '工作内容不符', '工作时间问题', '其他问题'];
const disputeTypeIndex = ref(0);
const disputeDescription = ref('');

// 切换状态
const switchStatus = (status: number) => {
   currentStatus.value = status;
   pageNum.value = 1;
   hasMore.value = true;
   orderList.value = [];
   fetchOrderList();
};

// 获取状态文本
const getStatusText = (status: number) => {
   const statusMap: Record<number, string> = {
      1: '已发起',
      2: '进行中',
      3: '已结算'
   };
   return statusMap[status] || '未知';
};

// 获取状态样式类
const getStatusClass = (status: number) => {
   const classMap: Record<number, string> = {
      1: 'warning',
      2: 'primary',
      3: 'gray'
   };
   return classMap[status] || 'gray';
};

// 获取订单列表
const fetchOrderList = async () => {
   if (loading.value) return;

   loading.value = true;
   try {
      // 模拟数据
      const mockData: Order[] = [
         {
            id: 1,
            jobTitle: '短视频剪辑兼职',
            orderStatus: 1,
            coverImage: 'https://picsum.photos/seed/job1/100/100',
            jobDescription: '短视频剪辑兼职 在家可做 日结',
            companyName: '北京创想文化传媒有限公司',
            workType: '线上',
            location: '线上办公',
            workTime: '2026-04-10',
            deposit: 50,
            amount: '150'
         },
         {
            id: 2,
            jobTitle: '图文排版兼职',
            orderStatus: 2,
            coverImage: 'https://picsum.photos/seed/job2/100/100',
            jobDescription: '图文排版兼职 简单易做 时间自由',
            companyName: '上海创意设计有限公司',
            workType: '线下',
            location: '上海市静安区',
            workTime: '2026-04-08 至 2026-04-15',
            deposit: 30,
            amount: '120/天'
         },
         {
            id: 3,
            jobTitle: '促销活动兼职',
            orderStatus: 3,
            coverImage: 'https://picsum.photos/seed/job3/100/100',
            jobDescription: '促销活动兼职 日结 包午餐',
            companyName: '广州市场营销有限公司',
            workType: '线下',
            location: '广州市天河区',
            workTime: '2026-04-01',
            deposit: 0,
            amount: '180'
         }
      ];

      // 筛选数据
      let filteredData = mockData;
      if (currentStatus.value > 0) {
         filteredData = mockData.filter(
            item => item.orderStatus === currentStatus.value
         );
      }

      // 模拟分页
      const start = (pageNum.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      const paginatedData = filteredData.slice(start, end);

      if (pageNum.value === 1) {
         orderList.value = paginatedData;
      } else {
         orderList.value = [...orderList.value, ...paginatedData];
      }

      hasMore.value = paginatedData.length >= pageSize.value;
   } catch (error) {
      console.error('获取订单列表失败:', error);
   } finally {
      loading.value = false;
   }
};

// 加载更多
const loadMore = () => {
   if (hasMore.value && !loading.value) {
      pageNum.value++;
      fetchOrderList();
   }
};

// 取消订单
const cancelOrder = (id: number) => {
   uni.showModal({
      title: '确认取消',
      content: '确定要取消该订单吗？',
      success: res => {
         if (res.confirm) {
            uni.showToast({ title: '订单已取消', icon: 'success' });
         }
      }
   });
};

// 支付押金
const payDeposit = (order: Order) => {
   paymentOrder.value = order;
   showPaymentModal.value = true;
};

// 关闭支付弹窗
const closePaymentModal = () => {
   showPaymentModal.value = false;
};

// 确认支付
const confirmPayment = () => {
   showPaymentModal.value = false;
   setTimeout(() => {
      showPaymentSuccessModal.value = true;
   }, 300);
};

// 关闭支付成功弹窗
const closePaymentSuccessModal = () => {
   showPaymentSuccessModal.value = false;
   // 更新订单状态
   if (paymentOrder.value) {
      const index = orderList.value.findIndex(
         item => item.id === paymentOrder.value!.id
      );
      if (index > -1) {
         orderList.value[index].orderStatus = 2;
      }
   }
};

// 联系商家
const contactMerchant = (id: number) => {
   uni.showToast({ title: '联系商家功能开发中', icon: 'none' });
};

// 异议申请
const openDispute = (id: number) => {
   currentOrderId.value = id;
   disputeTypeIndex.value = 0;
   disputeDescription.value = '';
   showDisputeModal.value = true;
};

// 关闭异议弹窗
const closeDisputeModal = () => {
   showDisputeModal.value = false;
};

// 异议类型选择
const onDisputeTypeChange = (e: any) => {
   disputeTypeIndex.value = e.detail.value;
};

// 提交异议申请
const submitDispute = () => {
   if (!disputeTypes[disputeTypeIndex.value]) {
      uni.showToast({ title: '请选择异议类型', icon: 'none' });
      return;
   }
   if (
      !disputeDescription.value ||
      disputeDescription.value.trim().length < 10
   ) {
      uni.showToast({
         title: '请详细描述您的异议，至少10个字符',
         icon: 'none'
      });
      return;
   }
   showDisputeModal.value = false;
   setTimeout(() => {
      showDisputeSuccessModal.value = true;
   }, 300);
};

// 关闭异议成功弹窗
const closeDisputeSuccessModal = () => {
   showDisputeSuccessModal.value = false;
};

// 确认完成
const confirmComplete = (id: number) => {
   uni.showModal({
      title: '确认完成',
      content: '确定已完成该兼职工作吗？',
      success: res => {
         if (res.confirm) {
            uni.showToast({ title: '订单已完成', icon: 'success' });
            const index = orderList.value.findIndex(item => item.id === id);
            if (index > -1) {
               orderList.value[index].orderStatus = 3;
            }
         }
      }
   });
};

// 查看详情
const viewDetail = (id: number) => {
   uni.showToast({ title: '查看详情功能开发中', icon: 'none' });
};

// 再次应聘
const applyAgain = (id: number) => {
   uni.showToast({ title: '再次应聘功能开发中', icon: 'none' });
};

// 返回
const goBack = () => {
   uni.navigateBack();
};

// 页面加载
onMounted(() => {
   fetchOrderList();
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

.status-bar {
   background-color: #fff;
   padding: 20rpx 0;
   display: flex;
   border-bottom: 1rpx solid #f1f5f9;
}

.status-item {
   flex: 1;
   text-align: center;
   padding: 16rpx 0;
   border-bottom: 4rpx solid transparent;
   transition: all 0.3s;

   &.active {
      border-bottom-color: #3b82f6;
      background-color: rgba(59, 130, 246, 0.05);
      .status-text {
         color: #3b82f6;
         font-weight: 600;
      }
   }
}

.status-text {
   font-size: 28rpx;
   color: #64748b;
}

.order-list {
   height: calc(100vh - 200rpx);
   padding: 24rpx;
}

.order-card {
   background-color: #fff;
   border-radius: 16rpx;
   margin-bottom: 24rpx;
   overflow: hidden;
}

.order-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 20rpx 24rpx;
   border-bottom: 1rpx solid #f1f5f9;
}

.order-title {
   font-size: 28rpx;
   font-weight: 500;
   color: #1e293b;
}

.order-status {
   font-size: 22rpx;
   padding: 6rpx 16rpx;
   border-radius: 6rpx;

   &.warning {
      background-color: #fef3c7;
      color: #d97706;
   }

   &.primary {
      background-color: #eff6ff;
      color: #3b82f6;
   }

   &.gray {
      background-color: #f8fafc;
      color: #64748b;
   }
}

.order-content {
   display: flex;
   gap: 20rpx;
   padding: 24rpx;
}

.order-image {
   width: 160rpx;
   height: 160rpx;
   border-radius: 12rpx;
}

.order-info {
   flex: 1;
   display: flex;
   flex-direction: column;
   gap: 12rpx;
}

.job-desc {
   font-size: 26rpx;
   color: #1e293b;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
   overflow: hidden;
}

.info-row {
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.company-name {
   font-size: 22rpx;
   color: #94a3b8;
}

.work-type {
   font-size: 20rpx;
   padding: 4rpx 12rpx;
   border-radius: 4rpx;

   &.online {
      background-color: #eff6ff;
      color: #3b82f6;
   }

   &.offline {
      background-color: #d1fae5;
      color: #10b981;
   }
}

.location,
.time {
   font-size: 22rpx;
   color: #94a3b8;
   display: flex;
   align-items: center;
   gap: 4rpx;
}

.icon {
   font-size: 20rpx;
}

.money-row {
   margin-top: 8rpx;
}

.deposit,
.amount {
   font-size: 22rpx;
   display: flex;
   align-items: center;

   .label {
      color: #94a3b8;
   }

   .value {
      color: #1e293b;
      font-weight: 500;

      &.highlight {
         color: #10b981;
      }
   }
}

.order-actions {
   display: flex;
   justify-content: flex-end;
   gap: 16rpx;
   padding: 20rpx 24rpx;
   border-top: 1rpx solid #f1f5f9;
}

.action-btn {
   padding: 12rpx 24rpx;
   border-radius: 8rpx;
   border: 2rpx solid #e2e8f0;
   transition: all 0.2s;

   text {
      font-size: 24rpx;
      color: #64748b;
   }

   &.primary {
      background-color: #3b82f6;
      border-color: #3b82f6;
      text {
         color: #fff;
      }
   }

   &.danger {
      border-color: #ef4444;
      text {
         color: #ef4444;
      }
   }

   &.cancel {
      text {
         color: #64748b;
      }
   }

   &:active {
      opacity: 0.8;
      transform: scale(0.95);
   }
}

.load-more {
   padding: 32rpx;
   text-align: center;
   font-size: 26rpx;
   color: #94a3b8;
}

/* 弹窗 */
.modal-overlay {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   bottom: 0;
   background-color: rgba(0, 0, 0, 0.5);
   display: flex;
   align-items: center;
   justify-content: center;
   z-index: 1000;
}

.modal-content {
   width: 85%;
   background-color: #fff;
   border-radius: 24rpx;
   padding: 40rpx;

   &.payment-modal,
   &.dispute-modal {
      max-height: 80vh;
      overflow-y: auto;
   }

   &.success-modal {
      text-align: center;
   }
}

.modal-title {
   font-size: 36rpx;
   font-weight: 600;
   color: #1e293b;
   text-align: center;
   margin-bottom: 32rpx;
}

.payment-amount {
   display: flex;
   align-items: baseline;
   justify-content: center;
   gap: 8rpx;
   margin-bottom: 24rpx;
}

.amount-value {
   font-size: 56rpx;
   font-weight: 700;
   color: #ef4444;
}

.amount-label {
   font-size: 28rpx;
   color: #94a3b8;
}

.payment-info {
   background-color: #f8fafc;
   border-radius: 12rpx;
   padding: 20rpx;
   margin-bottom: 16rpx;
}

.info-title {
   font-size: 28rpx;
   color: #1e293b;
   display: block;
   margin-bottom: 8rpx;
}

.info-company {
   font-size: 24rpx;
   color: #94a3b8;
}

.payment-tip {
   font-size: 24rpx;
   color: #94a3b8;
   text-align: center;
   margin-bottom: 24rpx;
}

.payment-methods {
   margin-bottom: 32rpx;
}

.method-item {
   display: flex;
   align-items: center;
   padding: 24rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   margin-bottom: 16rpx;

   &.active {
      border-color: #3b82f6;
   }
}

.method-icon {
   font-size: 40rpx;
   margin-right: 16rpx;
}

.method-name {
   flex: 1;
   font-size: 28rpx;
   color: #1e293b;
}

.check-icon {
   font-size: 28rpx;
   color: #3b82f6;
}

.confirm-btn {
   width: 100%;
   height: 88rpx;
   background-color: #3b82f6;
   border: none;
   border-radius: 12rpx;
   font-size: 30rpx;
   color: #fff;
   font-weight: 500;
   transition: all 0.2s;

   &:active {
      opacity: 0.8;
      transform: scale(0.98);
   }
}

.success-icon {
   width: 120rpx;
   height: 120rpx;
   background-color: #d1fae5;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   margin: 0 auto 24rpx;
   font-size: 60rpx;
   color: #10b981;
}

.success-desc {
   font-size: 28rpx;
   color: #64748b;
   display: block;
   margin-bottom: 32rpx;
}

/* 异议申请表单 */
.form-item {
   margin-bottom: 24rpx;
}

.form-label {
   font-size: 26rpx;
   color: #1e293b;
   font-weight: 500;
   display: block;
   margin-bottom: 12rpx;
}

.picker-value {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 24rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
}

.picker-arrow {
   font-size: 32rpx;
   color: #94a3b8;
}

.dispute-textarea {
   width: 100%;
   height: 200rpx;
   padding: 20rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #1e293b;
   box-sizing: border-box;
}

.upload-area {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   padding: 48rpx;
   border: 2rpx dashed #cbd5e1;
   border-radius: 12rpx;
}

.upload-icon {
   font-size: 64rpx;
   margin-bottom: 16rpx;
}

.upload-text {
   font-size: 28rpx;
   color: #94a3b8;
}

.upload-tip {
   font-size: 24rpx;
   color: #94a3b8;
   display: block;
   margin-top: 12rpx;
}

.dispute-actions {
   display: flex;
   gap: 20rpx;
   margin-top: 32rpx;
}

.cancel-btn {
   flex: 1;
   height: 88rpx;
   border: 2rpx solid #e2e8f0;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #64748b;
   background-color: #fff;
   transition: all 0.2s;

   &:active {
      opacity: 0.8;
      transform: scale(0.98);
   }
}

.submit-btn {
   flex: 1;
   height: 88rpx;
   background-color: #ef4444;
   border: none;
   border-radius: 12rpx;
   font-size: 28rpx;
   color: #fff;
   transition: all 0.2s;

   &:active {
      opacity: 0.8;
      transform: scale(0.98);
   }
}
</style>
