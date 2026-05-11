<template>
   <DefaultContainer>
      <div class="title">待入职管理</div>

      <ElTable v-loading="loading" :data="tableData" border max-height="550">
         <ElTableColumn prop="orderId" label="订单ID" width="100" />
         <ElTableColumn prop="jobTitle" label="兼职岗位" min-width="150" show-overflow-tooltip />
         <ElTableColumn label="用户信息" min-width="250">
            <template #default="{ row }">
               <div v-if="row.user" class="user-cell">
                  <ElAvatar :size="40" :src="row.user?.avatarUrl" />
                  <div class="user-info">
                     <div class="user-name">{{ row.intention?.realName || row.user?.username || '-' }}</div>
                     <div class="user-phone">{{ row.intention?.phone || '-' }}</div>
                  </div>
                  <div class="credit-badge-small">
                     <span>{{ row.user?.creditCode || 0 }}</span>
                  </div>
               </div>
               <span v-else>-</span>
            </template>
         </ElTableColumn>
         <ElTableColumn label="应聘时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.createdAt) }}
            </template>
         </ElTableColumn>
         <ElTableColumn label="状态" width="120">
            <template #default="{ row }">
               <ElButton type="warning" text @click="showDetail(row)">查看</ElButton>
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

      <!-- 详情弹窗 -->
      <ElDialog v-model="dialogVisible" title="求职者详情" width="500px" :close-on-click-modal="false">
         <div v-if="selectedRow" class="dialog-content">
            <div class="dialog-header">
               <ElAvatar :size="80" :src="selectedRow.user?.avatarUrl" />
               <div class="dialog-user-info">
                  <div class="dialog-name">{{ selectedRow.intention?.realName || selectedRow.user?.username }}</div>
                  <div class="dialog-phone">{{ selectedRow.intention?.phone || '' }}</div>
               </div>
               <div class="dialog-credit">
                  <span class="credit-label">信誉分</span>
                  <span class="credit-value">{{ selectedRow.user?.creditCode || 0 }}</span>
               </div>
            </div>
            <div class="dialog-divider"></div>
            <div class="dialog-body">
               <div class="info-row">
                  <span class="info-label">学号：</span>
                  <span class="info-value">{{ selectedRow.intention?.studentId || '-' }}</span>
               </div>
               <div class="info-row">
                  <span class="info-label">专业：</span>
                  <span class="info-value">{{ selectedRow.intention?.profession || '-' }}</span>
               </div>
               <div class="info-row">
                  <span class="info-label">年龄：</span>
                  <span class="info-value">{{ selectedRow.intention?.age || '-' }}岁</span>
               </div>
               <div class="info-row">
                  <span class="info-label">性别：</span>
                  <span class="info-value">{{
                     selectedRow.intention?.gender === 1 ? '男' : selectedRow.intention?.gender === 2 ? '女' : '未知'
                  }}</span>
               </div>
               <div class="info-row">
                  <span class="info-label">个人简介：</span>
                  <span class="info-value">{{ selectedRow.intention?.introduction || '-' }}</span>
               </div>
               <div class="info-row">
                  <span class="info-label">意向标签：</span>
                  <span class="info-value">{{ selectedRow.intention?.tagName || '-' }}</span>
               </div>
               <div class="info-row">
                  <span class="info-label">应聘岗位：</span>
                  <span class="info-value">{{ selectedRow.jobTitle || '-' }}</span>
               </div>
            </div>
         </div>
      </ElDialog>
   </DefaultContainer>
</template>

<script setup>
import { ElAvatar, ElButton, ElDialog, ElMessage } from 'element-plus';
import { onMounted, ref } from 'vue';
import * as apis from '@/api/ypgl/ypgl.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const tableData = ref([]);
const dialogVisible = ref(false);
const selectedRow = ref(null);

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
      const res = await apis.getPendingEntryApplications();
      if (res.code === 0) {
         tableData.value = res.data || [];
         pagination.value.total = res.data?.length || 0;
      }
   } catch (error) {
      console.error('加载待入职信息失败:', error);
      ElMessage.error('加载待入职信息失败');
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

function showDetail(row) {
   selectedRow.value = row;
   dialogVisible.value = true;
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

/* 弹窗样式 */
.dialog-content {
   .dialog-header {
      display: flex;
      align-items: center;
      gap: 16px;
      padding-bottom: 16px;
   }

   .dialog-user-info {
      flex: 1;
   }

   .dialog-name {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 4px;
   }

   .dialog-phone {
      font-size: 14px;
      color: #909399;
   }

   .dialog-credit {
      text-align: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 10px 16px;
      border-radius: 24px;
      min-width: 80px;
   }

   .dialog-credit .credit-label {
      display: block;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.8);
   }

   .dialog-credit .credit-value {
      display: block;
      font-size: 20px;
      font-weight: 600;
      color: #fff;
   }

   .dialog-divider {
      height: 1px;
      background: #f0f0f0;
      margin: 16px 0;
   }

   .dialog-body {
      .info-row {
         display: flex;
         padding: 8px 0;
         font-size: 14px;
      }

      .info-label {
         color: #909399;
         width: 80px;
         flex-shrink: 0;
      }

      .info-value {
         color: #606266;
         flex: 1;
         line-height: 1.5;
      }
   }
}
</style>
