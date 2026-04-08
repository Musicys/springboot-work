<template>
   <DefaultContainer>
      <div class="title">结算详情</div>
      <div class="content">
         <ElCard shadow="hover">
            <template #header>
               <div class="card-header">
                  <span>订单信息</span>
               </div>
            </template>
            <ElRow :gutter="20">
               <ElCol :span="12">
                  <ElDescriptions title="订单基本信息" border>
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
                     <ElDescriptionsItem label="结算金额">{{ orderInfo.amount }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="违约金">{{ orderInfo.penaltyAmount }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ orderInfo.createdAt }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="开始时间">{{ orderInfo.startTime }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="结束时间">{{ orderInfo.endTime }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="完成时间">{{ orderInfo.completedAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
               <ElCol :span="12">
                  <ElDescriptions title="用户信息" border>
                     <ElDescriptionsItem label="用户ID">{{ userInfo.id }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户名">{{ userInfo.username }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="头像">{{ userInfo.avatarUrl }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="地区代码">{{ userInfo.regionCode }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户类型">{{ getUserTypeText(userInfo.userType) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="状态">{{ getStatusText(userInfo.status) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ userInfo.createdAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
            </ElRow>
         </ElCard>
         <ElCard shadow="hover" style="margin-top: 20px">
            <template #header>
               <div class="card-header">
                  <span>结算记录</span>
               </div>
            </template>
            <ElTable :data="settleRecords" border>
               <ElTableColumn prop="id" label="记录ID" width="100" />
               <ElTableColumn prop="amount" label="金额" width="120">
                  <template #default="scope">
                     <span>¥{{ scope.row.amount }}</span>
                  </template>
               </ElTableColumn>
               <ElTableColumn prop="status" label="状态" width="100">
                  <template #default="scope">
                     <span>{{ getSettleStatusText(scope.row.status) }}</span>
                  </template>
               </ElTableColumn>
               <ElTableColumn prop="createdAt" label="结算时间" width="180" />
               <ElTableColumn prop="description" label="备注" min-width="200" />
            </ElTable>
         </ElCard>
      </div>
      <div class="footer">
         <ElButton v-if="orderInfo.orderStatus === 3" type="primary" @click="onSettle"> 立即结算 </ElButton>
         <ElButton @click="router.back()"> 返回 </ElButton>
      </div>
   </DefaultContainer>
</template>

<script setup>
import {
   ElButton,
   ElCard,
   ElCol,
   ElDescriptions,
   ElDescriptionsItem,
   ElMessage,
   ElMessageBox,
   ElRow,
   ElTable,
   ElTableColumn
} from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const router = useRouter();
const route = useRoute();
const id = route.query.id;

const orderInfo = ref({
   orderId: 1001,
   merchantId: 1,
   userId: 101,
   jobId: 201,
   orderStatus: 3,
   tradeMode: 1,
   amount: '¥100.00',
   penaltyAmount: '¥0.00',
   createdAt: '2026-01-01 10:00:00',
   startTime: '2026-01-02 09:00:00',
   endTime: '2026-01-02 18:00:00',
   completedAt: '2026-01-02 18:00:00'
});

const userInfo = ref({
   id: 101,
   username: 'user1',
   avatarUrl:
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20user%20avatar&image_size=square',
   regionCode: '110000',
   userType: 1,
   status: 1,
   createdAt: '2026-01-01 08:00:00'
});

const settleRecords = ref([
   {
      id: 1,
      amount: 100.0,
      status: 1,
      createdAt: '2026-01-03 10:00:00',
      description: '订单结算'
   },
   {
      id: 2,
      amount: 50.0,
      status: 1,
      createdAt: '2026-01-04 10:00:00',
      description: '额外补贴'
   }
]);

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

function getUserTypeText(userType) {
   switch (userType) {
      case 1:
         return '求职者';
      case 2:
         return '商家';
      case 3:
         return '管理员';
      default:
         return '未知';
   }
}

function getStatusText(status) {
   switch (status) {
      case 1:
         return '正常';
      case 0:
         return '封禁';
      case -1:
         return '注销';
      default:
         return '未知';
   }
}

function getSettleStatusText(status) {
   switch (status) {
      case 1:
         return '已结算';
      case 0:
         return '待结算';
      default:
         return '未知';
   }
}

function onSettle() {
   ElMessageBox({
      title: '提示',
      type: 'info',
      message: `确定要结算该订单吗？金额：${orderInfo.value.amount}`,
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            setTimeout(() => {
               ElMessage.success('结算成功');
               orderInfo.value.orderStatus = 5;
               settleRecords.value.push({
                  id: 2,
                  amount: 100.0,
                  status: 1,
                  createdAt: new Date().toLocaleString(),
                  description: '手动结算'
               });
               done();
            }, 500);
         } else {
            done();
         }
      }
   });
}

onMounted(() => {
   // 模拟获取数据
   console.log('获取结算详情:', id);
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

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}
</style>
