<template>
   <DefaultContainer>
      <div class="title">订单主表管理</div>

      <!-- 搜索栏 -->
      <div class="search-bar">
         <ElInput v-model="searchForm.orderId" placeholder="订单ID" class="search-input" @keyup.enter="onSearch" />
         <ElSelect v-model="searchForm.orderStatus" placeholder="订单状态" class="search-select">
            <ElOption label="全部" :value="-1" />
            <ElOption label="待商家审核" :value="0" />
            <ElOption label="待入职" :value="1" />
            <ElOption label="进行中" :value="2" />
            <ElOption label="已完成" :value="3" />
            <ElOption label="纠纷中" :value="4" />
            <ElOption label="已结款" :value="5" />
            <ElOption label="用户爽约" :value="6" />
            <ElOption label="异常终止" :value="7" />
         </ElSelect>
         <ElSelect v-model="searchForm.tradeMode" placeholder="交易模式" class="search-select">
            <ElOption label="全部" :value="-1" />
            <ElOption label="平台担保" :value="1" />
            <ElOption label="线下交易" :value="2" />
         </ElSelect>
         <ElButton type="primary" @click="onSearch">搜索</ElButton>
         <ElButton @click="resetSearch">重置</ElButton>
      </div>

      <ElTable v-loading="loading" :data="tableData" border :row-key="(row: any) => row.id">
         <ElTableColumn prop="id" label="订单ID" width="120" />
         <ElTableColumn prop="applicationId" label="申请ID" width="100" />
         <ElTableColumn prop="jobId" label="兼职ID" width="100" />
         <ElTableColumn prop="jobTitle" label="兼职名称" width="150" show-overflow-tooltip />
         <ElTableColumn prop="jobRegionName" label="工作地点" width="120" />
         <ElTableColumn label="薪资范围" width="140">
            <template #default="{ row }"> ¥{{ row.jobSalaryMin || 0 }} - ¥{{ row.jobSalaryMax || 0 }} </template>
         </ElTableColumn>
         <ElTableColumn prop="userId" label="用户ID" width="100" />
         <ElTableColumn prop="realName" label="用户姓名" width="100" />
         <ElTableColumn prop="studentId" label="学号" width="120" />
         <ElTableColumn prop="phone" label="联系电话" width="130" />
         <ElTableColumn prop="profession" label="职业" width="100" />
         <ElTableColumn prop="creditScore" label="信誉分" width="100" />
         <ElTableColumn prop="merchantId" label="商家ID" width="100" />
         <ElTableColumn label="交易模式" width="100">
            <template #default="{ row }">
               <el-tag :type="row.tradeMode === 1 ? 'success' : 'info'" size="small">
                  {{ row.tradeMode === 1 ? '平台担保' : '线下交易' }}
               </el-tag>
            </template>
         </ElTableColumn>
         <ElTableColumn label="订单状态" width="120">
            <template #default="{ row }">
               <el-tag :type="getStatusType(row.orderStatus)" size="small">
                  {{ getStatusText(row.orderStatus) }}
               </el-tag>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="depositFrozenId" label="冻结记录ID" width="120" />
         <ElTableColumn prop="penaltyAmount" label="违约金" width="100">
            <template #default="{ row }"> ¥{{ row.penaltyAmount || 0.0 }} </template>
         </ElTableColumn>
         <ElTableColumn label="开始时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.startTime) }}
            </template>
         </ElTableColumn>
         <ElTableColumn label="结束时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.endTime) }}
            </template>
         </ElTableColumn>
         <ElTableColumn label="完成时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.completedAt) }}
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
         <ElTableColumn label="创建时间" width="180">
            <template #default="{ row }">
               {{ formatDate(row.createdAt) }}
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
   </DefaultContainer>
</template>

<script setup>
import { ElInput, ElSelect, ElOption, ElButton, ElMessage } from 'element-plus';
import { onMounted, reactive, ref } from 'vue';
import * as apis from '@/api/ypgl/orders.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const tableData = ref([]);

const searchForm = reactive({
   orderId: '',
   orderStatus: -1,
   tradeMode: -1
});

const pagination = reactive({
   pageNum: 1,
   pageSize: 10,
   total: 0
});

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
}

function onSearch() {
   loadOrders();
}

function resetSearch() {
   searchForm.orderId = '';
   searchForm.orderStatus = -1;
   searchForm.tradeMode = -1;
   pagination.pageNum = 1;
   loadOrders();
}

async function loadOrders() {
   loading.value = true;
   try {
      const params = {
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize,
         orderId: searchForm.orderId,
         orderStatus: searchForm.orderStatus >= 0 ? searchForm.orderStatus : undefined,
         tradeMode: searchForm.tradeMode >= 0 ? searchForm.tradeMode : undefined
      };
      const res = await apis.getOrders(params);
      if (res.code === 0) {
         tableData.value = res.data?.records || [];
         pagination.total = res.data?.total || 0;
      }
   } catch (error) {
      console.error('加载订单失败:', error);
      ElMessage.error('加载订单失败');
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

function getStatusText(status) {
   const statusMap = {
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
}

function getStatusType(status) {
   const typeMap = {
      0: 'info',
      1: 'warning',
      2: 'primary',
      3: 'success',
      4: 'danger',
      5: 'success',
      6: 'gray',
      7: 'danger'
   };
   return typeMap[status] || 'gray';
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

.search-bar {
   display: flex;
   gap: 12px;
   align-items: center;
   margin-bottom: 20px;
   flex-wrap: wrap;

   .search-input {
      width: 200px;
   }

   .search-select {
      width: 160px;
   }
}

.pagination {
   margin-top: 15px;
   display: flex;
   justify-content: flex-end;
}
</style>
