<template>
   <DefaultContainer>
      <div class="title">应聘信息管理</div>

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
         <ElTableColumn label="应聘时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.createdAt) }}
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
         <ElTableColumn fixed="right" width="120" label="操作">
            <template #default="{ row }">
               <ElButton type="success" link @click="onApprove(row)"> 同意应聘 </ElButton>
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

      <ElDialog v-model="approveDialogVisible" title="输入冻结金额" width="400px" :close-on-click-modal="false">
         <div class="freeze-dialog">
            <p class="freeze-tip">请输入冻结金额</p>
            <p class="freeze-range">
               薪资范围：¥{{ selectedOrder?.salaryMin || 0 }} ~ ¥{{ selectedOrder?.salaryMax || 0 }}
            </p>
            <div class="freeze-input">
               <ElInputNumber
                  v-model="freezeAmount"
                  :min="selectedOrder?.salaryMin || 0"
                  :max="selectedOrder?.salaryMax || 999999" />
               <span class="freeze-unit">元</span>
            </div>
            <p class="freeze-hint">冻结后将从可用余额转入冻结金额</p>
         </div>
         <template #footer>
            <el-button @click="approveDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmFreeze">确定冻结</el-button>
         </template>
      </ElDialog>
   </DefaultContainer>
</template>

<script setup>
import { ElAvatar, ElDialog, ElInputNumber, ElMessage, ElMessageBox } from 'element-plus';
import { onMounted, ref } from 'vue';
import * as apis from '@/api/ypgl/ypgl.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const tableData = ref([]);
const approveDialogVisible = ref(false);
const selectedOrder = ref(null);
const freezeAmount = ref(0);

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
   loadApplications();
}

async function loadApplications() {
   loading.value = true;
   try {
      const res = await apis.getAllApplications();
      if (res.code === 0) {
         tableData.value = res.data || [];
         pagination.value.total = res.data?.length || 0;
      }
   } catch (error) {
      console.error('加载应聘信息失败:', error);
      ElMessage.error('加载应聘信息失败');
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

function onApprove(row) {
   const salaryMin = row.salaryMin || 0;
   const salaryMax = row.salaryMax || 0;
   const userName = row.intention?.realName || row.user?.username;

   selectedOrder.value = row;
   freezeAmount.value = salaryMin;

   if (salaryMin === salaryMax) {
      ElMessageBox.confirm(`确定要同意 "${userName}" 的应聘吗？冻结金额：¥${salaryMin}`, '提示', {
         confirmButtonText: '确定',
         cancelButtonText: '取消',
         type: 'info'
      })
         .then(() => {
            confirmFreeze();
         })
         .catch(() => {});
   } else {
      approveDialogVisible.value = true;
   }
}

function confirmFreeze() {
   if (!selectedOrder.value || !freezeAmount.value) return;

   const salaryMin = selectedOrder.value.salaryMin || 0;
   const salaryMax = selectedOrder.value.salaryMax || 0;

   if (freezeAmount.value < salaryMin || freezeAmount.value > salaryMax) {
      ElMessage.error('冻结金额必须在 ¥' + salaryMin + ' ~ ¥' + salaryMax + ' 范围内');
      return;
   }

   apis
      .approveApplication({ orderId: selectedOrder.value.orderId, freezeAmount: freezeAmount.value })
      .then(res => {
         if (res.code === 0) {
            ElMessage.success('已同意应聘，冻结金额：¥' + freezeAmount.value);
            approveDialogVisible.value = false;
            loadApplications();
         } else {
            ElMessage.error(res.message || '操作失败');
         }
      })
      .catch(() => {
         ElMessage.error('操作失败');
      });
}

onMounted(() => {
   loadApplications();
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

.freeze-dialog {
   text-align: center;
   padding: 10px 0;

   .freeze-tip {
      font-size: 16px;
      color: #303133;
      margin-bottom: 10px;
   }

   .freeze-range {
      font-size: 14px;
      color: #909399;
      margin-bottom: 20px;
   }

   .freeze-input {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-bottom: 15px;
   }

   .freeze-unit {
      font-size: 14px;
      color: #606266;
   }

   .freeze-hint {
      font-size: 12px;
      color: #67c23a;
   }
}
</style>
