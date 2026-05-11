<template>
   <DefaultContainer>
      <div class="title">兼职进行中管理</div>

      <ElTable v-loading="loading" :data="tableData" border :row-key="(row: any) => row.orderId">
         <ElTableColumn type="expand" width="60">
            <template #default="{ row }">
               <div class="expand-content">
                  <div class="expand-item">
                     <span class="expand-label">学号：</span>
                     <span class="expand-value">{{ row.intention?.studentId || '-' }}</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">专业：</span>
                     <span class="expand-value">{{ row.intention?.profession || '-' }}</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">年龄：</span>
                     <span class="expand-value">{{ row.intention?.age || '-' }}岁</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">性别：</span>
                     <span class="expand-value">{{
                        row.intention?.gender === 1 ? '男' : row.intention?.gender === 2 ? '女' : '未知'
                     }}</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">个人简介：</span>
                     <span class="expand-value">{{ row.intention?.introduction || '-' }}</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">意向标签：</span>
                     <span class="expand-value">{{ row.intention?.tagName || '-' }}</span>
                  </div>
                  <div class="expand-item">
                     <span class="expand-label">冻结金额：</span>
                     <span class="expand-value">¥{{ row.frozenAmount || 0 }}</span>
                  </div>
               </div>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="orderId" label="订单ID" width="100" />
         <ElTableColumn prop="jobTitle" label="兼职岗位" min-width="120" show-overflow-tooltip />
         <ElTableColumn label="薪资范围" width="120">
            <template #default="{ row }">
               <span class="salary-text">¥{{ row.salaryMin || 0 }}~{{ row.salaryMax || 0 }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn label="用户信息" min-width="200">
            <template #default="{ row }">
               <div class="user-cell">
                  <ElAvatar :size="40" :src="row.user?.avatarUrl" />
                  <div class="user-info">
                     <div class="user-name">{{ row.intention?.realName || row.user?.username || '-' }}</div>
                     <div class="user-phone">{{ row.intention?.phone || '-' }}</div>
                  </div>
                  <div class="credit-badge-small">
                     <span>{{ row.user?.creditCode || 0 }}</span>
                  </div>
               </div>
            </template>
         </ElTableColumn>
         <ElTableColumn label="冻结金额" width="100">
            <template #default="{ row }">
               <span class="frozen-amount">¥{{ row.frozenAmount || 0 }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn label="开始时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.startTime) }}
            </template>
         </ElTableColumn>
         <ElTableColumn label="是否退押" width="100">
            <template #default="{ row }">
               <el-tag :type="row.isDepositRefunded === 1 ? 'success' : 'info'" size="small">
                  {{ row.isDepositRefunded === 1 ? '已退押' : '未退押' }}
               </el-tag>
            </template>
         </ElTableColumn>
         <ElTableColumn label="是否结款" width="100">
            <template #default="{ row }">
               <el-tag :type="row.isSettled === 1 ? 'success' : 'info'" size="small">
                  {{ row.isSettled === 1 ? '已结款' : '未结款' }}
               </el-tag>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="200" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onSettle(row)">结款</ElButton>
               <template v-if="row.isDepositRefunded === 1">
                  <span class="disabled-text">已退押</span>
               </template>
               <ElButton v-else type="success" link @click="onRefund(row)">退押</ElButton>
               <ElButton type="danger" link @click="onDispute(row)">异议申请</ElButton>
            </template>
         </ElTableColumn>
      </ElTable>
      <div class="pagination">
         <ElPagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
            layout="total,prev,pager,next,sizes,jumper"
            :total="pagination.total"
            @size-change="onSizeChange"
            @current-change="onSearch" />
      </div>

      <ElDialog v-model="settleDialogVisible" title="结款确认" width="400px" :close-on-click-modal="false">
         <div class="settle-dialog">
            <p class="settle-tip">确认给用户结款？</p>
            <p class="settle-amount">结款金额：¥{{ selectedOrder?.frozenAmount || 0 }}</p>
            <p class="settle-hint">结款后将从冻结金额转入用户余额</p>
         </div>
         <template #footer>
            <el-button @click="settleDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmSettle">确认结款</el-button>
         </template>
      </ElDialog>

      <ElDialog v-model="refundDialogVisible" title="退押确认" width="400px" :close-on-click-modal="false">
         <div class="refund-dialog">
            <p class="refund-tip">确认退还押金？</p>
            <p class="refund-amount">退还金额：¥{{ selectedOrder?.frozenAmount || 0 }}</p>
            <p class="refund-hint">退押后将解冻用户冻结金额</p>
         </div>
         <template #footer>
            <el-button @click="refundDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmRefund">确认退押</el-button>
         </template>
      </ElDialog>
   </DefaultContainer>
</template>

<script setup>
import { ElAvatar, ElDialog, ElMessage, ElMessageBox } from 'element-plus';
import { onMounted, ref } from 'vue';
import * as apis from '@/api/ypgl/in-progress.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const tableData = ref([]);
const settleDialogVisible = ref(false);
const refundDialogVisible = ref(false);
const selectedOrder = ref(null);

const pagination = ref({
   pageNum: 1,
   pageSize: 10,
   total: 0
});

function onSizeChange(pageSize) {
   pagination.value.pageSize = pageSize;
   pagination.value.pageNum = 1;
}

function onSearch() {
   loadOrders();
}

async function loadOrders() {
   loading.value = true;
   try {
      const res = await apis.getInProgressOrders();
      if (res.code === 0) {
         tableData.value = res.data || [];
         pagination.value.total = res.data?.length || 0;
      }
   } catch (error) {
      console.error('加载进行中订单失败:', error);
      ElMessage.error('加载进行中订单失败');
   } finally {
      loading.value = false;
   }
}

function formatDate(dateStr) {
   if (!dateStr) return '-';
   const date = new Date(dateStr);
   return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
   });
}

function onSettle(row) {
   selectedOrder.value = row;
   settleDialogVisible.value = true;
}

function confirmSettle() {
   if (!selectedOrder.value) return;

   apis
      .settleOrder({ orderId: selectedOrder.value.orderId })
      .then(res => {
         if (res.code === 0) {
            ElMessage.success('结款成功');
            settleDialogVisible.value = false;
            loadOrders();
         } else {
            ElMessage.error(res.message || '结款失败');
         }
      })
      .catch(() => {
         ElMessage.error('结款失败');
      });
}

function onRefund(row) {
   selectedOrder.value = row;
   refundDialogVisible.value = true;
}

function confirmRefund() {
   if (!selectedOrder.value) return;

   apis
      .refundDeposit({ orderId: selectedOrder.value.orderId })
      .then(res => {
         if (res.code === 0) {
            ElMessage.success('退押成功');
            refundDialogVisible.value = false;
            loadOrders();
         } else {
            ElMessage.error(res.message || '退押失败');
         }
      })
      .catch(() => {
         ElMessage.error('退押失败');
      });
}

function onDispute(row) {
   ElMessageBox.confirm('确定要发起异议申请吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   })
      .then(() => {
         apis
            .applyDispute({ orderId: row.orderId })
            .then(res => {
               if (res.code === 0) {
                  ElMessage.success('异议申请已提交');
                  loadOrders();
               } else {
                  ElMessage.error(res.message || '提交失败');
               }
            })
            .catch(() => {
               ElMessage.error('提交失败');
            });
      })
      .catch(() => {});
}

onMounted(() => {
   loadOrders();
});
</script>

<style lang="scss" scoped>
.title {
   font-size: 18px;
   font-weight: 600;
   margin-bottom: 20px;
}

.salary-text {
   color: #67c23a;
   font-weight: 600;
}

.frozen-amount {
   color: #e6a23c;
   font-weight: 600;
}

.pagination {
   margin-top: 15px;
   display: flex;
   justify-content: flex-end;
}

.user-cell {
   display: flex;
   align-items: center;
   gap: 10px;
   padding: 4px 0;

   .user-info {
      flex: 1;
      min-width: 0;
   }

   .user-name {
      font-size: 13px;
      font-weight: 600;
      color: #303133;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
   }

   .user-phone {
      font-size: 12px;
      color: #909399;
   }

   .credit-badge-small {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;
   }
}

.expand-content {
   padding: 12px 20px;
   background: #f8f9fa;
   border-radius: 8px;
   margin: 8px 0;

   .expand-item {
      display: inline-flex;
      align-items: center;
      margin-right: 30px;
      margin-bottom: 8px;
      font-size: 13px;
   }

   .expand-label {
      color: #909399;
      margin-right: 4px;
   }

   .expand-value {
      color: #606266;
   }
}

.disabled-text {
   color: #909399;
   font-size: 12px;
   margin: 0 8px;
}

.settle-dialog,
.refund-dialog {
   text-align: center;
   padding: 10px 0;

   .settle-tip,
   .refund-tip {
      font-size: 16px;
      color: #303133;
      margin-bottom: 10px;
   }

   .settle-amount,
   .refund-amount {
      font-size: 24px;
      color: #67c23a;
      font-weight: 600;
      margin-bottom: 15px;
   }

   .settle-hint,
   .refund-hint {
      font-size: 12px;
      color: #909399;
   }
}
</style>
