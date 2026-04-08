<template>
   <DefaultContainer>
      <div class="title">信誉分记录</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="用户ID：" prop="userId">
                        <ElInput v-model="searchForm.userId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="订单ID：" prop="orderId">
                        <ElInput v-model="searchForm.orderId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="变动类型：" prop="reasonType">
                        <ElSelect v-model="searchForm.reasonType" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="完成订单" value="COMPLETE_JOB" />
                           <ElOption label="用户爽约" value="NO_SHOW" />
                           <ElOption label="中途逃逸" value="ESCAPE" />
                           <ElOption label="手动调整" value="MANUAL_ADJUST" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <div style="margin-top: 10px">
                  <ElButton :loading="sarchLoading" type="primary" @click="onSearch"> 查询 </ElButton>
                  <ElButton @click="resetSearchForm(searchFormRef)"> 重置 </ElButton>
               </div>
            </ElForm>
         </div>
      </div>
      <ElTable v-loading="loading" :data="tableData" border max-height="550">
         <ElTableColumn prop="id" label="记录ID" width="100" />
         <ElTableColumn prop="userId" label="用户ID" width="100" />
         <ElTableColumn prop="orderId" label="订单ID" width="120" />
         <ElTableColumn prop="changeAmount" label="信誉分变动" width="120">
            <template #default="scope">
               <span :class="scope.row.changeAmount > 0 ? 'text-success' : 'text-danger'">
                  {{ scope.row.changeAmount > 0 ? '+' : '' }}{{ scope.row.changeAmount }}
               </span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="balanceAfter" label="变动后分数" width="120" />
         <ElTableColumn prop="reasonType" label="变动原因" width="120">
            <template #default="scope">
               <span>{{ getReasonTypeText(scope.row.reasonType) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="description" label="备注" min-width="200" />
         <ElTableColumn prop="createdAt" label="创建时间" width="180" />
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
import { ElCol, ElForm, ElFormItem, ElRow, ElSelect, ElOption } from 'element-plus';
import { onMounted, ref } from 'vue';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();

const tableData = ref([
   {
      id: 1,
      userId: 101,
      orderId: 1001,
      changeAmount: 5,
      reasonType: 'COMPLETE_JOB',
      description: '完成订单，增加信誉分',
      balanceAfter: 85,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      id: 2,
      userId: 102,
      orderId: 1002,
      changeAmount: -5,
      reasonType: 'NO_SHOW',
      description: '用户爽约，扣除信誉分',
      balanceAfter: 75,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      id: 3,
      userId: 103,
      orderId: 1003,
      changeAmount: 5,
      reasonType: 'COMPLETE_JOB',
      description: '完成订单，增加信誉分',
      balanceAfter: 85,
      createdAt: '2026-01-03 12:00:00'
   },
   {
      id: 4,
      userId: 104,
      orderId: 1004,
      changeAmount: -10,
      reasonType: 'ESCAPE',
      description: '中途逃逸，扣除信誉分',
      balanceAfter: 70,
      createdAt: '2026-01-04 13:00:00'
   },
   {
      id: 5,
      userId: 105,
      orderId: 1005,
      changeAmount: 5,
      reasonType: 'COMPLETE_JOB',
      description: '完成订单，增加信誉分',
      balanceAfter: 85,
      createdAt: '2026-01-05 14:00:00'
   },
   {
      id: 6,
      userId: 106,
      orderId: 1006,
      changeAmount: 5,
      reasonType: 'COMPLETE_JOB',
      description: '完成订单，增加信誉分',
      balanceAfter: 90,
      createdAt: '2026-01-06 15:00:00'
   },
   {
      id: 7,
      userId: 107,
      orderId: 1007,
      changeAmount: -5,
      reasonType: 'NO_SHOW',
      description: '用户爽约，扣除信誉分',
      balanceAfter: 75,
      createdAt: '2026-01-07 16:00:00'
   },
   {
      id: 8,
      userId: 108,
      orderId: 1008,
      changeAmount: 5,
      reasonType: 'COMPLETE_JOB',
      description: '完成订单，增加信誉分',
      balanceAfter: 85,
      createdAt: '2026-01-08 17:00:00'
   }
]);

const loading = ref(false);
const searchForm = ref({
   userId: null,
   orderId: null,
   reasonType: null
});

function getReasonTypeText(reasonType) {
   switch (reasonType) {
      case 'COMPLETE_JOB':
         return '完成订单';
      case 'NO_SHOW':
         return '用户爽约';
      case 'ESCAPE':
         return '中途逃逸';
      case 'MANUAL_ADJUST':
         return '手动调整';
      default:
         return '未知';
   }
}

async function onSearch() {
   loading.value = true;
   // 模拟数据加载
   setTimeout(() => {
      loading.value = false;
   }, 500);
}

onMounted(() => {
   onSearch();
});

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function resetSearchForm(formEl) {
   if (!formEl) return;
   formEl.resetFields();
   onSearch();
}
</script>

<style lang="scss" scoped>
.title {
   height: 40px;
   border-bottom: 1px solid rgb(0 0 0 / 6%);
   color: rgb(0 0 0 / 88%);
   font-weight: 600;
   font-size: 20px;
}

.search-box {
   padding: 20px 0;
   border-bottom: 1px solid rgb(0 0 0 / 6%);
}

.pagination {
   display: flex;
   justify-content: end;
   margin-top: 20px;
}

.text-success {
   color: #67c23a;
}

.text-danger {
   color: #f56c6c;
}
</style>
