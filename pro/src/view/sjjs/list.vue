<template>
   <DefaultContainer>
      <div class="title">结算订单列表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="订单ID：" prop="orderId">
                        <ElInput v-model="searchForm.orderId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="商家ID：" prop="merchantId">
                        <ElInput v-model="searchForm.merchantId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="订单状态：" prop="orderStatus">
                        <ElSelect v-model="searchForm.orderStatus" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="待入职" value="1" />
                           <ElOption label="进行中" value="2" />
                           <ElOption label="完成待结算" value="3" />
                           <ElOption label="纠纷中" value="4" />
                           <ElOption label="已结款" value="5" />
                           <ElOption label="用户爽约" value="6" />
                           <ElOption label="异常终止" value="7" />
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
      <ElTable v-loading="loading" :data="tableData" border max-height="550" @row-dblclick="onRowDbClick">
         <ElTableColumn prop="orderId" label="订单ID" width="120" />
         <ElTableColumn prop="merchantId" label="商家ID" width="100" />
         <ElTableColumn prop="userId" label="用户ID" width="100" />
         <ElTableColumn prop="jobId" label="兼职ID" width="100" />
         <ElTableColumn prop="orderStatus" label="订单状态" width="120">
            <template #default="scope">
               <span>{{ getOrderStatusText(scope.row.orderStatus) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="amount" label="结算金额" width="120">
            <template #default="scope">
               <span>¥{{ scope.row.amount }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="createdAt" label="创建时间" width="180" />
         <ElTableColumn fixed="right" width="320" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onView(row)"> 查看 </ElButton>
               <ElButton v-if="row.orderStatus === 3" type="success" link @click="onSettle(row)"> 结算 </ElButton>
               <ElButton
                  v-if="row.orderStatus === 2 || row.orderStatus === 3"
                  type="warning"
                  link
                  @click="onDispute(row)">
                  异议申请
               </ElButton>
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
import { ElCol, ElForm, ElFormItem, ElMessage, ElMessageBox, ElRow, ElSelect, ElOption } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();
const router = useRouter();

const tableData = ref([
   {
      orderId: 1001,
      merchantId: 1,
      userId: 101,
      jobId: 201,
      orderStatus: 3,
      amount: 100.0,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      orderId: 1002,
      merchantId: 1,
      userId: 102,
      jobId: 202,
      orderStatus: 5,
      amount: 150.0,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      orderId: 1003,
      merchantId: 2,
      userId: 103,
      jobId: 203,
      orderStatus: 3,
      amount: 200.0,
      createdAt: '2026-01-03 12:00:00'
   },
   {
      orderId: 1004,
      merchantId: 3,
      userId: 104,
      jobId: 204,
      orderStatus: 3,
      amount: 120.0,
      createdAt: '2026-01-04 13:00:00'
   },
   {
      orderId: 1005,
      merchantId: 4,
      userId: 105,
      jobId: 205,
      orderStatus: 5,
      amount: 180.0,
      createdAt: '2026-01-05 14:00:00'
   },
   {
      orderId: 1006,
      merchantId: 5,
      userId: 106,
      jobId: 206,
      orderStatus: 3,
      amount: 220.0,
      createdAt: '2026-01-06 15:00:00'
   },
   {
      orderId: 1007,
      merchantId: 6,
      userId: 107,
      jobId: 207,
      orderStatus: 4,
      amount: 160.0,
      createdAt: '2026-01-07 16:00:00'
   },
   {
      orderId: 1008,
      merchantId: 7,
      userId: 108,
      jobId: 208,
      orderStatus: 5,
      amount: 250.0,
      createdAt: '2026-01-08 17:00:00'
   }
]);

const loading = ref(false);
const searchForm = ref({
   orderId: null,
   merchantId: null,
   orderStatus: null
});

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

function onRowDbClick(row) {
   onView(row);
}

function onView(row) {
   router.push({ path: '/pages/sjjs/detail', query: { id: row.orderId } });
}

function onSettle(row) {
   ElMessageBox({
      title: '提示',
      type: 'info',
      message: `确定要结算该订单吗？金额：¥${row.amount}`,
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            setTimeout(() => {
               ElMessage.success('结算成功');
               onSearch();
               done();
            }, 500);
         } else {
            done();
         }
      }
   });
}

function onDispute(row) {
   ElMessageBox({
      title: '异议申请',
      type: 'warning',
      message: '确定要对该订单提出异议吗？',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            setTimeout(() => {
               ElMessage.success('异议申请成功');
               onSearch();
               done();
            }, 500);
         } else {
            done();
         }
      }
   });
}

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
</style>
