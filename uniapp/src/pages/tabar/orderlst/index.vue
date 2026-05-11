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
               <view class="order-tags">
                  <text
                     v-if="order.isDepositRefunded !== undefined"
                     class="refund-tag"
                     :class="{ refunded: order.isDepositRefunded === 1 }">
                     {{ order.isDepositRefunded === 1 ? '已退押' : '未退押' }}
                  </text>
                  <text
                     class="order-status"
                     :class="getStatusClass(order.orderStatus)">
                     {{ getStatusText(order.orderStatus) }}
                  </text>
               </view>
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
                        <text class="label">薪资：</text>
                        <text class="value highlight">¥{{ order.amount }}</text>
                     </view>
                  </view>
               </view>
            </view>

            <!-- 操作按钮 -->
            <view class="order-actions">
               <view v-if="order.orderStatus === 0" class="waiting-text">
                  <text>待商家同意招聘</text>
               </view>

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
                  @click="contactMerchant(order)">
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
                  :class="{ disabled: order.countdown && !order.canComplete }"
                  @click="order.canComplete && confirmComplete(order.id)">
                  <text v-if="order.countdown"
                     >倒计时 {{ order.countdown }}</text
                  >
                  <text v-else-if="order.canComplete">确认完成</text>
                  <text v-else>确认完成</text>
               </view>

               <view
                  v-if="order.orderStatus === 3"
                  class="action-btn"
                  @click="viewDetail(order.id)">
                  <text>查看详情</text>
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
                  <text class="method-icon">💰</text>
                  <text class="method-name">余额支付</text>
                  <text class="check-icon">✓</text>
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
               <text class="form-label">异议描述</text>
               <textarea
                  class="dispute-textarea"
                  v-model="disputeDescription"
                  placeholder="请详细描述您的异议"></textarea>
            </view>
            <view class="form-item">
               <text class="form-label">上传证据（必填，最多5张）</text>
               <view class="upload-images">
                  <view
                     v-for="(image, index) in disputeImages"
                     :key="index"
                     class="image-item">
                     <image
                        :src="image"
                        class="preview-image"
                        mode="aspectFill"></image>
                     <view class="delete-btn" @click="removeImage(index)">
                        <text class="delete-icon">×</text>
                     </view>
                  </view>
                  <view
                     v-if="disputeImages.length < 5"
                     class="upload-btn"
                     @click="chooseImage">
                     <text class="upload-icon">+</text>
                     <text class="upload-text">添加</text>
                  </view>
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

      <!-- 联系商家弹窗 -->
      <view
         class="modal-overlay"
         v-if="showContactModal"
         @click="closeContactModal">
         <view class="modal-content contact-modal" @click.stop>
            <text class="modal-title">联系商家</text>
            <view class="contact-info">
               <view class="contact-item">
                  <text class="contact-label">企业名称</text>
                  <text class="contact-value">{{
                     contactOrder?.companyName
                  }}</text>
               </view>
               <view class="contact-item">
                  <text class="contact-label">联系电话</text>
                  <view class="phone-row">
                     <text class="contact-value phone">{{
                        contactOrder?.contactPhone
                     }}</text>
                     <view
                        class="copy-btn"
                        v-if="contactOrder?.contactPhone"
                        @click="copyPhone(contactOrder.contactPhone)">
                        <text class="copy-icon">📋</text>
                        <text class="copy-text">复制</text>
                     </view>
                  </view>
               </view>
            </view>
            <view class="contact-tip">
               <text>请在工作时间内联系商家</text>
            </view>
            <button class="confirm-btn" @click="closeContactModal">
               我知道了
            </button>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import * as apis from '@/api/job';

// 状态列表
const statusList = ref([
   { label: '全部', value: -1 },
   { label: '待商家审核', value: 0 },
   { label: '待入职', value: 1 },
   { label: '进行中', value: 2 },
   { label: '已完成', value: 3 }
]);

const currentStatus = ref(-1);

// 订单列表
interface Order {
   id: number;
   jobTitle: string;
   orderStatus: number;
   coverImage: string;
   jobDescription: string;
   companyName: string;
   contactPhone: string;
   workType: string;
   location: string;
   workTime: string;
   deposit: number;
   amount: string;
   expireTime: string;
   countdown: string;
   canComplete: boolean;
   isDepositRefunded?: number;
   isSettled?: number;
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
const showContactModal = ref(false);
const paymentOrder = ref<Order | null>(null);
const currentOrderId = ref(0);
const contactOrder = ref<Order | null>(null);

// 异议申请数据
const disputeDescription = ref('');
const disputeImages = ref<string[]>([]); // 存储选择的图片路径

// 选择图片
const chooseImage = () => {
   uni.chooseImage({
      count: 5 - disputeImages.value.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: res => {
         disputeImages.value = [...disputeImages.value, ...res.tempFilePaths];
      },
      fail: () => {
         uni.showToast({ title: '选择图片失败', icon: 'none' });
      }
   });
};

// 删除图片
const removeImage = (index: number) => {
   disputeImages.value.splice(index, 1);
};

// 计算倒计时和是否可完成
const calculateCountdown = (expireTime: string) => {
   if (!expireTime) {
      return { countdown: '', canComplete: false };
   }

   const expireDate = new Date(expireTime);
   const now = new Date();
   const diff = expireDate.getTime() - now.getTime();

   if (diff <= 0) {
      return { countdown: '', canComplete: true };
   }

   const days = Math.floor(diff / (1000 * 60 * 60 * 24));
   const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
   const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
   const seconds = Math.floor((diff % (1000 * 60)) / 1000);

   let countdown = '';
   if (days > 0) {
      countdown = `${days}天${hours}时${minutes}分${seconds}秒`;
   } else if (hours > 0) {
      countdown = `${hours}时${minutes}分${seconds}秒`;
   } else if (minutes > 0) {
      countdown = `${minutes}分${seconds}秒`;
   } else {
      countdown = `${seconds}秒`;
   }

   return { countdown, canComplete: false };
};

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
      0: '待商家审核',
      1: '待入职',
      2: '进行中',
      3: '已完成',
      4: '纠纷中',
      5: '已结款',
      6: '用户爽约',
      7: '异常终止'
   };
   return statusMap[status] || '未知';
};

// 获取状态样式类
const getStatusClass = (status: number) => {
   const classMap: Record<number, string> = {
      0: 'info',
      1: 'warning',
      2: 'primary',
      3: 'success',
      4: 'danger',
      5: 'gray',
      6: 'gray',
      7: 'danger'
   };
   return classMap[status] || 'gray';
};

// 获取订单列表
const fetchOrderList = async () => {
   if (loading.value) return;

   loading.value = true;
   try {
      const params: any = {
         pageNum: pageNum.value,
         pageSize: pageSize.value
      };

      // 添加状态筛选条件
      if (currentStatus.value >= 0) {
         params.orderStatus = currentStatus.value;
      }

      const res = await apis.getOrderList(params);

      if (res.code === 0 && res.data && res.data.records) {
         const records = res.data.records as any[];
         const orders: Order[] = records.map(item => {
            const expireTime = item.expireTime || '';
            const { countdown, canComplete } = calculateCountdown(expireTime);
            return {
               id: item.id || 0,
               jobTitle: item.jobTitle || '未知岗位',
               orderStatus: item.orderStatus || 0,
               coverImage:
                  item.jobCover || 'https://picsum.photos/seed/job/100/100',
               jobDescription: item.jobDescription || item.jobTitle || '',
               companyName: item.companyName || '未知企业',
               contactPhone: item.contactPhone || '',
               workType: item.jobTypeDesc || '其他',
               location: item.regionName || '线上办公',
               workTime: item.workTimeDesc || '',
               deposit: item.depositAmount || 0,
               amount: item.salary ? `¥${item.salary}` : '',
               expireTime,
               countdown,
               canComplete,
               isDepositRefunded: item.isDepositRefunded,
               isSettled: item.isSettled
            };
         });

         if (pageNum.value === 1) {
            orderList.value = orders;
         } else {
            orderList.value = [...orderList.value, ...orders];
         }

         hasMore.value = records.length >= pageSize.value;
      } else {
         hasMore.value = false;
      }
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
const cancelOrder = async (id: number) => {
   uni.showModal({
      title: '确认取消',
      content: '确定要取消该订单吗？',
      success: async res => {
         if (res.confirm) {
            try {
               const res = await apis.cancelOrder(id);
               if (res.code === 0) {
                  uni.showToast({ title: '订单已取消', icon: 'success' });
                  // 刷新订单列表
                  pageNum.value = 1;
                  hasMore.value = true;
                  orderList.value = [];
                  fetchOrderList();
               } else {
                  uni.showToast({
                     title: res.message || '取消失败',
                     icon: 'none'
                  });
               }
            } catch (error) {
               console.error('取消订单失败:', error);
               uni.showToast({ title: '取消失败', icon: 'none' });
            }
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
const confirmPayment = async () => {
   if (!paymentOrder.value) return;

   uni.showLoading({ title: '处理中...' });

   try {
      const res = await apis.confirmOrder(paymentOrder.value.id);
      if (res.code === 0) {
         showPaymentModal.value = false;
         setTimeout(() => {
            showPaymentSuccessModal.value = true;
         }, 300);
      } else {
         uni.showToast({
            title: res.message || '支付失败',
            icon: 'none'
         });
      }
   } catch (error) {
      console.error('支付押金失败:', error);
      uni.showToast({ title: '支付失败', icon: 'none' });
   } finally {
      uni.hideLoading();
   }
};

// 关闭支付成功弹窗
const closePaymentSuccessModal = () => {
   showPaymentSuccessModal.value = false;
   // 刷新订单列表
   pageNum.value = 1;
   hasMore.value = true;
   orderList.value = [];
   fetchOrderList();
};

// 联系商家
const contactMerchant = (order: Order) => {
   contactOrder.value = order;
   showContactModal.value = true;
};

// 关闭联系商家弹窗
const closeContactModal = () => {
   showContactModal.value = false;
   contactOrder.value = null;
};

// 复制电话
const copyPhone = (phone: string) => {
   uni.setClipboardData({
      data: phone,
      success: () => {
         uni.showToast({ title: '复制成功', icon: 'success' });
      },
      fail: () => {
         uni.showToast({ title: '复制失败', icon: 'none' });
      }
   });
};

// 异议申请
const openDispute = (id: number) => {
   currentOrderId.value = id;
   disputeDescription.value = '';
   disputeImages.value = [];
   showDisputeModal.value = true;
};

// 关闭异议弹窗
const closeDisputeModal = () => {
   showDisputeModal.value = false;
   disputeImages.value = [];
};

// 上传图片到服务器
const uploadImages = async (images: string[]): Promise<string[]> => {
   const uploadedUrls: string[] = [];
   for (const imagePath of images) {
      try {
         const res = await apis.uploadFile(imagePath);
         if (res.code === 0 && res.data && res.data.url) {
            uploadedUrls.push(res.data.url);
         }
      } catch (e) {
         console.error('上传图片失败:', e);
      }
   }
   return uploadedUrls;
};

// 提交异议申请
const submitDispute = async () => {
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
   if (disputeImages.value.length === 0) {
      uni.showToast({ title: '请至少上传一张证据图片', icon: 'none' });
      return;
   }

   uni.showLoading({ title: '提交中...' });

   try {
      // 先上传图片
      const uploadedUrls = await uploadImages(disputeImages.value);

      const res = await apis.applyArbitration({
         orderId: currentOrderId.value,
         userEvidenceSummary: disputeDescription.value,
         userEvidenceImages: uploadedUrls
      });

      if (res.code === 0) {
         showDisputeModal.value = false;
         setTimeout(() => {
            showDisputeSuccessModal.value = true;
            // 刷新订单列表
            pageNum.value = 1;
            hasMore.value = true;
            orderList.value = [];
         }, 300);
      } else {
         uni.showToast({
            title: res.message || '提交失败',
            icon: 'none'
         });
      }
   } catch (error) {
      console.error('提交异议申请失败:', error);
      uni.showToast({ title: '提交失败', icon: 'none' });
   } finally {
      uni.hideLoading();
   }
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

// 更新倒计时
const updateCountdowns = () => {
   orderList.value.forEach(order => {
      if (order.orderStatus === 2 && order.expireTime) {
         const { countdown, canComplete } = calculateCountdown(
            order.expireTime
         );
         order.countdown = countdown;
         order.canComplete = canComplete;
      }
   });
};

// 页面加载
onMounted(() => {
   fetchOrderList();
   // 每秒更新倒计时
   setInterval(updateCountdowns, 1000);
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

.order-tags {
   display: flex;
   align-items: center;
   gap: 12rpx;
}

.refund-tag {
   font-size: 22rpx;
   padding: 6rpx 16rpx;
   border-radius: 6rpx;
   background-color: #fef3c7;
   color: #d97706;

   &.refunded {
      background-color: #dcfce7;
      color: #16a34a;
   }
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

   &.info {
      background-color: #f0f9ff;
      color: #0ea5e9;
   }

   &.success {
      background-color: #d1fae5;
      color: #10b981;
   }

   &.danger {
      background-color: #fee2e2;
      color: #ef4444;
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

.refund-status {
   display: flex;
   align-items: center;
   font-size: 22rpx;
   margin-top: 8rpx;

   .label {
      color: #94a3b8;
   }

   .value {
      font-weight: 500;

      &.refunded {
         color: #10b981;
      }

      &.not-refunded {
         color: #f59e0b;
      }
   }
}

.deposit,
.amount {
   font-size: 24rpx;
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

      &.disabled {
         background-color: #cbd5e1;
         border-color: #cbd5e1;
         text {
            color: #94a3b8;
         }
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

.waiting-text {
   padding: 12rpx 24rpx;
   text {
      font-size: 24rpx;
      color: #909399;
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

.upload-images {
   display: flex;
   flex-wrap: wrap;
   gap: 16rpx;
}

.image-item {
   position: relative;
   width: 180rpx;
   height: 180rpx;
   border-radius: 12rpx;
   overflow: hidden;
}

.preview-image {
   width: 100%;
   height: 100%;
}

.delete-btn {
   position: absolute;
   top: 8rpx;
   right: 8rpx;
   width: 40rpx;
   height: 40rpx;
   background-color: rgba(0, 0, 0, 0.6);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
}

.delete-icon {
   font-size: 28rpx;
   color: #fff;
}

.upload-btn {
   width: 180rpx;
   height: 180rpx;
   border: 2rpx dashed #cbd5e1;
   border-radius: 12rpx;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.upload-icon {
   font-size: 48rpx;
   margin-bottom: 8rpx;
}

.upload-text {
   font-size: 24rpx;
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

/* 联系商家弹窗 */
.contact-modal {
   padding: 40rpx;
}

.contact-info {
   margin-top: 32rpx;
}

.contact-item {
   display: flex;
   justify-content: space-between;
   align-items: center;
   padding: 20rpx 0;
   border-bottom: 2rpx solid #f1f5f9;

   &:last-child {
      border-bottom: none;
   }
}

.contact-label {
   font-size: 28rpx;
   color: #64748b;
}

.contact-value {
   font-size: 28rpx;
   color: #1e293b;
   font-weight: 500;

   &.phone {
      color: #3b82f6;
      font-size: 32rpx;
      font-weight: 600;
   }
}

.phone-row {
   display: flex;
   align-items: center;
   gap: 20rpx;
}

.copy-btn {
   display: flex;
   align-items: center;
   gap: 8rpx;
   padding: 12rpx 20rpx;
   background-color: #eff6ff;
   border-radius: 8rpx;

   &:active {
      opacity: 0.8;
   }
}

.copy-icon {
   font-size: 28rpx;
}

.copy-text {
   font-size: 24rpx;
   color: #3b82f6;
}

.contact-tip {
   margin-top: 32rpx;
   padding: 20rpx;
   background-color: #fffbeb;
   border-radius: 12rpx;
   text-align: center;

   text {
      font-size: 24rpx;
      color: #d97706;
   }
}
</style>
