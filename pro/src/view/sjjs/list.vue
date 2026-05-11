<template>
   <DefaultContainer>
      <div class="title">交易流水表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="关键词搜索：" prop="keyword">
                        <ElInput
                           v-model="searchForm.keyword"
                           clearable
                           placeholder="请输入订单ID或描述"
                           style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="交易类型：" prop="type">
                        <ElSelect v-model="searchForm.type" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="押金收入" value="DEPOSIT_IN" />
                           <ElOption label="薪资发放" value="SALARY_PAY" />
                           <ElOption label="违约金支出" value="PENALTY_OUT" />
                           <ElOption label="退款" value="REFUND" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <div style="margin-top: 10px">
                  <ElButton :loading="searchLoading" type="primary" @click="onSearch"> 查询 </ElButton>
                  <ElButton @click="resetSearchForm(searchFormRef)"> 重置 </ElButton>
               </div>
            </ElForm>
         </div>
      </div>
      <ElTable v-loading="loading" :data="tableData" border max-height="550">
         <ElTableColumn prop="id" label="ID" width="80" />
         <ElTableColumn prop="realName" label="用户姓名" width="100" />
         <ElTableColumn prop="phone" label="用户电话" width="120" />
         <ElTableColumn prop="studentId" label="学号" width="120" />
         <ElTableColumn prop="type" label="交易类型" width="120">
            <template #default="scope">
               <span>{{ getTypeText(scope.row.type) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="amount" label="交易金额" width="140">
            <template #default="scope">
               <span :class="getAmountClass(scope.row)">
                  {{ getAmountSign(scope.row.type) }}¥{{ formatAmount(scope.row.amount) }}
               </span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="balanceBefore" label="变动前余额" width="120">
            <template #default="scope">
               <span>¥{{ scope.row.balanceBefore }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="balanceAfter" label="变动后余额" width="120">
            <template #default="scope">
               <span>¥{{ scope.row.balanceAfter }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="relatedOrderId" label="关联订单ID" width="120">
            <template #default="scope">
               <span>{{ scope.row.relatedOrderId || '-' }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="description" label="描述" min-width="200" show-overflow-tooltip />
         <ElTableColumn prop="createdAt" label="创建时间" width="180">
            <template #default="scope">
               <span>{{ formatDate(scope.row.createdAt) }}</span>
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
import {
   ElButton,
   ElCol,
   ElForm,
   ElFormItem,
   ElImage,
   ElInput,
   ElMessage,
   ElMessageBox,
   ElOption,
   ElRow,
   ElSelect,
   ElTable,
   ElTableColumn,
   ElPagination
} from 'element-plus';
import { onMounted, ref, reactive } from 'vue';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import * as transactionApi from '@/api/sjjs/transaction.ts';

const loading = ref(false);
const searchLoading = ref(false);
const searchFormRef = ref(null);

const pagination = reactive({
   pageNum: 1,
   pageSize: 10,
   total: 0
});

const searchForm = reactive({
   keyword: '',
   type: ''
});

const tableData = ref([]);

function getTypeText(type) {
   const map = {
      DEPOSIT_IN: '押金收入',
      SALARY_PAY: '薪资发放',
      PENALTY_OUT: '违约金支出',
      REFUND: '退款'
   };
   return map[type] || type || '-';
}

function getAmountClass(row) {
   const type = row.type;
   if (type === 'DEPOSIT_IN' || type === 'SALARY_PAY' || type === 'REFUND') {
      return 'amount-positive';
   } else if (type === 'PENALTY_OUT') {
      return 'amount-negative';
   }
   return 'amount-negative';
}

function formatAmount(amount) {
   if (!amount && amount !== 0) return '-';
   // 确保金额是正数显示
   const num = Math.abs(Number(amount));
   return num.toFixed(2);
}

function getAmountSign(type) {
   if (type === 'DEPOSIT_IN' || type === 'SALARY_PAY' || type === 'REFUND') {
      return '+';
   } else if (type === 'PENALTY_OUT') {
      return '-';
   }
   return '';
}

function formatDate(dateStr) {
   if (!dateStr) return '-';
   const date = new Date(dateStr);
   const year = date.getFullYear();
   const month = String(date.getMonth() + 1).padStart(2, '0');
   const day = String(date.getDate()).padStart(2, '0');
   const hours = String(date.getHours()).padStart(2, '0');
   const minutes = String(date.getMinutes()).padStart(2, '0');
   const seconds = String(date.getSeconds()).padStart(2, '0');
   return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

async function getTransactionList() {
   try {
      loading.value = true;
      const params = {
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize
      };
      if (searchForm.keyword) params.keyword = searchForm.keyword;
      if (searchForm.type) params.type = searchForm.type;

      const res = await transactionApi.getTransactionList(params);

      if (res.code === 0 && res.data) {
         tableData.value = res.data.records || [];
         pagination.total = Number(res.data.total) || 0;
      }
   } catch (err) {
      console.error(err);
      ElMessage.error('获取数据失败');
   } finally {
      loading.value = false;
   }
}

async function onSearch() {
   searchLoading.value = true;
   await getTransactionList();
   searchLoading.value = false;
}

async function onView(row) {
   try {
      const res = await transactionApi.getTransactionDetail({ id: row.id });
      if (res.code === 0 && res.data) {
         ElMessageBox({
            title: '交易详情',
            message: `
               <div style="text-align:left;">
                  <p><strong>ID：</strong>${res.data.id}</p>
                  <p><strong>用户ID：</strong>${res.data.userId}</p>
                  <p><strong>交易类型：</strong>${getTypeText(res.data.type)}</p>
                  <p><strong>交易金额：</strong>${res.data.amount}</p>
                  <p><strong>变动前余额：</strong>¥${res.data.balanceBefore}</p>
                  <p><strong>变动后余额：</strong>¥${res.data.balanceAfter}</p>
                  <p><strong>关联订单ID：</strong>${res.data.relatedOrderId || '-'}</p>
                  <p><strong>关联冻结ID：</strong>${res.data.relatedFreezeId || '-'}</p>
                  <p><strong>描述：</strong>${res.data.description || '-'}</p>
                  <p><strong>创建时间：</strong>${formatDate(res.data.createdAt)}</p>
               </div>
            `,
            confirmButtonText: '关闭'
         });
      }
   } catch (error) {
      ElMessage.error('获取详情失败');
   }
}

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function resetSearchForm(formEl) {
   if (formEl) {
      formEl.resetFields();
   }
   searchForm.keyword = '';
   searchForm.type = '';
   onSearch();
}

onMounted(() => {
   onSearch();
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

.search-box {
   padding: 20px 0;
   border-bottom: 1px solid rgb(0 0 0 / 6%);
}

.pagination {
   display: flex;
   justify-content: end;
   margin-top: 20px;
}

.amount-positive {
   color: #67c23a;
   font-weight: 600;
}

.amount-negative {
   color: #f56c6c;
   font-weight: 600;
}
</style>
