<template>
   <DefaultContainer>
      <div class="title">投诉仲裁详情</div>
      <div class="content">
         <ElCard shadow="hover">
            <template #header>
               <div class="card-header">
                  <span>基本信息</span>
               </div>
            </template>
            <ElRow :gutter="20">
               <ElCol :span="12">
                  <ElDescriptions title="投诉信息" border>
                     <ElDescriptionsItem label="投诉ID">{{ complaintInfo.id }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="订单ID">{{ complaintInfo.orderId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="投诉人ID">{{ complaintInfo.complainantId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="被投诉人ID">{{ complaintInfo.defendantId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="被投诉类型">{{
                        getTargetTypeText(complaintInfo.targetType)
                     }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="投诉原因">{{ complaintInfo.reasonCode }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="详细描述">{{ complaintInfo.description }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="状态">{{ getStatusText(complaintInfo.status) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="处理结果">{{ complaintInfo.resultAction }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ complaintInfo.createdAt }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="处理时间">{{ complaintInfo.closedAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
               <ElCol :span="12">
                  <ElDescriptions title="订单信息" border>
                     <ElDescriptionsItem label="订单ID">{{ orderInfo.orderId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="商家ID">{{ orderInfo.merchantId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户ID">{{ orderInfo.userId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="兼职ID">{{ orderInfo.jobId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="订单状态">{{
                        getOrderStatusText(orderInfo.orderStatus)
                     }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="交易模式">{{
                        getTradeModeText(orderInfo.tradeMode)
                     }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ orderInfo.createdAt }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="完成时间">{{ orderInfo.completedAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
            </ElRow>
         </ElCard>
         <ElCard shadow="hover" style="margin-top: 20px">
            <template #header>
               <div class="card-header">
                  <span>证据信息</span>
               </div>
            </template>
            <div class="evidence-list">
               <ElImage
                  v-for="(evidence, index) in complaintInfo.evidenceUrls"
                  :key="index"
                  :src="evidence"
                  fit="cover"
                  style="width: 200px; height: 150px; margin-right: 10px; margin-bottom: 10px" />
            </div>
         </ElCard>
         <ElCard shadow="hover" style="margin-top: 20px">
            <template #header>
               <div class="card-header">
                  <span>仲裁信息</span>
               </div>
            </template>
            <ElDescriptions title="仲裁详情" border>
               <ElDescriptionsItem label="仲裁ID">{{ arbitrationInfo.id }}</ElDescriptionsItem>
               <ElDescriptionsItem label="订单ID">{{ arbitrationInfo.orderId }}</ElDescriptionsItem>
               <ElDescriptionsItem label="申请人ID">{{ arbitrationInfo.initiatorId }}</ElDescriptionsItem>
               <ElDescriptionsItem label="用户证据摘要">{{ arbitrationInfo.userEvidenceSummary }}</ElDescriptionsItem>
               <ElDescriptionsItem label="商家证据摘要">{{
                  arbitrationInfo.merchantEvidenceSummary
               }}</ElDescriptionsItem>
               <ElDescriptionsItem label="管理员判词">{{ arbitrationInfo.adminComment }}</ElDescriptionsItem>
               <ElDescriptionsItem label="裁决结果">{{
                  getRulingResultText(arbitrationInfo.rulingResult)
               }}</ElDescriptionsItem>
               <ElDescriptionsItem label="最终结算金额">{{ arbitrationInfo.finalSettlementAmount }}</ElDescriptionsItem>
               <ElDescriptionsItem label="处理管理员ID">{{ arbitrationInfo.handledByAdminId }}</ElDescriptionsItem>
               <ElDescriptionsItem label="仲裁状态">{{
                  getArbitrationStatusText(arbitrationInfo.status)
               }}</ElDescriptionsItem>
               <ElDescriptionsItem label="创建时间">{{ arbitrationInfo.createdAt }}</ElDescriptionsItem>
               <ElDescriptionsItem label="结案时间">{{ arbitrationInfo.closedAt }}</ElDescriptionsItem>
            </ElDescriptions>
         </ElCard>
      </div>
      <div class="footer">
         <ElButton @click="router.back()"> 返回 </ElButton>
      </div>
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElCard, ElCol, ElDescriptions, ElDescriptionsItem, ElImage, ElRow } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const router = useRouter();
const route = useRoute();
const id = route.query.id;

const complaintInfo = ref({
   id: 1,
   orderId: 1001,
   complainantId: 101,
   defendantId: 1,
   targetType: 2,
   reasonCode: '服务态度差',
   description: '商家服务态度恶劣，对员工不尊重，工作环境脏乱差',
   evidenceUrls: [
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=workplace%20environment%20poor%20condition&image_size=landscape_4_3',
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bad%20service%20attitude&image_size=landscape_4_3'
   ],
   status: 2,
   resultAction: '警告商家，要求整改',
   createdAt: '2026-01-01 10:00:00',
   closedAt: '2026-01-02 15:00:00'
});

const orderInfo = ref({
   orderId: 1001,
   merchantId: 1,
   userId: 101,
   jobId: 201,
   orderStatus: 4,
   tradeMode: 1,
   createdAt: '2026-01-01 09:00:00',
   completedAt: '2026-01-01 18:00:00'
});

const arbitrationInfo = ref({
   id: 1,
   orderId: 1001,
   initiatorId: 101,
   userEvidenceSummary: '商家服务态度恶劣，工作环境脏乱差',
   merchantEvidenceSummary: '用户工作态度不认真，多次迟到',
   adminComment: '经调查，双方都存在问题，商家服务态度确实存在问题，用户也有迟到情况。最终裁决商家退还部分薪资。',
   rulingResult: 2,
   finalSettlementAmount: 80.0,
   handledByAdminId: 1001,
   status: 2,
   createdAt: '2026-01-01 11:00:00',
   closedAt: '2026-01-03 10:00:00'
});

function getTargetTypeText(type) {
   switch (type) {
      case 1:
         return '用户';
      case 2:
         return '商家';
      default:
         return '未知';
   }
}

function getStatusText(status) {
   switch (status) {
      case 1:
         return '待处理';
      case 2:
         return '处理中';
      case 3:
         return '成功(处罚)';
      case 4:
         return '失败(证据不足)';
      default:
         return '未知';
   }
}

function getOrderStatusText(status) {
   switch (status) {
      case 1:
         return '待入职';
      case 2:
         return '进行中';
      case 3:
         return '完成待结算';
      case 4:
         return '纠纷中';
      case 5:
         return '已结款';
      case 6:
         return '用户爽约';
      case 7:
         return '异常终止';
      default:
         return '未知';
   }
}

function getTradeModeText(mode) {
   switch (mode) {
      case 1:
         return '平台担保';
      case 2:
         return '线下交易';
      default:
         return '未知';
   }
}

function getRulingResultText(result) {
   switch (result) {
      case 1:
         return '全额结款';
      case 2:
         return '部分结款';
      case 3:
         return '不结款';
      case 4:
         return '退还押金';
      default:
         return '未知';
   }
}

function getArbitrationStatusText(status) {
   switch (status) {
      case 1:
         return '审理中';
      case 2:
         return '已结案';
      default:
         return '未知';
   }
}

onMounted(() => {
   // 模拟获取数据
   console.log('获取投诉仲裁详情:', id);
});
</script>

<style lang="scss" scoped>
.title {
   height: 40px;
   border-bottom: 1px solid rgb(0 0 0 / 6%);
   color: rgb(0 0 0 / 88%);
   font-weight: 600;
   font-size: 20px;
}

.content {
   padding: 20px 0;
}

.card-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.evidence-list {
   display: flex;
   flex-wrap: wrap;
}

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}
</style>
