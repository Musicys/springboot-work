<template>
   <DefaultContainer>
      <div class="title">订单管理</div>
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
                     <ElFormItem label="用户ID：" prop="userId">
                        <ElInput v-model="searchForm.userId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="商家ID：" prop="merchantId">
                        <ElInput v-model="searchForm.merchantId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="兼职ID：" prop="jobId">
                        <ElInput v-model="searchForm.jobId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="订单状态：" prop="orderStatus">
                        <ElSelect v-model="searchForm.orderStatus" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="待入职" :value="1" />
                           <ElOption label="进行中" :value="2" />
                           <ElOption label="已完成" :value="3" />
                           <ElOption label="纠纷中" :value="4" />
                           <ElOption label="已结款" :value="5" />
                           <ElOption label="用户爽约" :value="6" />
                           <ElOption label="异常终止" :value="7" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="交易模式：" prop="tradeMode">
                        <ElSelect v-model="searchForm.tradeMode" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="平台担保" :value="1" />
                           <ElOption label="线下交易" :value="2" />
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
      <div class="menu-box">
         <div class="right">
            <ElButton :loading="exportLoading" @click="exportHandler">
               <i class="iconfont icon-Download" style="margin-right: 6px" />
               导出
            </ElButton>
         </div>
      </div>
      <ElTable v-loading="loading" :data="tableData" border max-height="550" @row-dblclick="onRowDbClick">
         <ElTableColumn
            v-for="column in columns"
            :key="column.prop"
            :align="column.align || 'center'"
            :min-width="column.minWidth"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
            :show-overflow-tooltip="column.tooltip">
            <template #default="scope">
               <span v-if="column.prop === 'orderStatus'">
                  {{ getOrderStatusText(scope.row.orderStatus) }}
               </span>
               <span v-else-if="column.prop === 'tradeMode'">
                  {{ getTradeModeText(scope.row.tradeMode) }}
               </span>
               <span v-else-if="column.prop === 'penaltyAmount'">
                  {{ scope.row.penaltyAmount ? `¥${scope.row.penaltyAmount}` : '-' }}
               </span>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="240" label="操作">
            <template #default="{ row }">
               <ElButton v-if="row.orderStatus === 2" type="success" link @click="onSettleOrder(row)"> 结款 </ElButton>
               <ElButton
                  v-if="row.orderStatus === 2 && row.depositFrozenId"
                  type="warning"
                  link
                  @click="onRefundDeposit(row)">
                  退押金
               </ElButton>
               <ElButton
                  v-if="row.orderStatus === 2 || row.orderStatus === 4"
                  type="danger"
                  link
                  @click="onRefundOrder(row)">
                  退款
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
import * as apis from '@/api/yydd/yydd.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();
const router = useRouter();

const columns = ref([
   { prop: 'id', label: '订单ID', width: '100' },
   { prop: 'applicationId', label: '投递ID', width: '100' },
   { prop: 'jobId', label: '兼职ID', width: '100' },
   { prop: 'realName', label: '用户姓名', width: '100' },
   { prop: 'phone', label: '用户电话', width: '120' },
   { prop: 'studentId', label: '学号', width: '120' },
   { prop: 'creditScore', label: '信誉分', width: '100' },
   { prop: 'tagName', label: '求职标签', width: '150', tooltip: true },
   { prop: 'companyName', label: '商家名称', width: '180', tooltip: true },
   { prop: 'contactPhone', label: '商家电话', width: '120' },
   { prop: 'tradeMode', label: '交易模式', width: '120' },
   { prop: 'orderStatus', label: '订单状态', width: '120' },
   { prop: 'penaltyAmount', label: '违约金', width: '120' },
   { prop: 'startTime', label: '开始时间', width: '180', tooltip: true },
   { prop: 'createdAt', label: '创建时间', width: '180', tooltip: true }
]);
const tableData = ref([]);
const loading = ref(false);
const searchForm = ref({
   orderId: null,
   userId: null,
   merchantId: null,
   jobId: null,
   orderStatus: null,
   tradeMode: null
});

function getOrderStatusText(status) {
   switch (status) {
      case 1:
         return '待入职';
      case 2:
         return '进行中';
      case 3:
         return '已完成';
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

async function getPersonPage(fun = () => {}) {
   try {
      loading.value = true;
      const queryParams = {
         ...searchForm.value
      };
      const res = await apis.getList({
         page: {
            pageNum: pagination.pageNum,
            pageSize: pagination.pageSize
         },
         params: queryParams
      });

      tableData.value = res.data.records;
      pagination.total = Number(res.data.total);
   } catch (err) {
      console.log(err);
   } finally {
      fun();
      loading.value = false;
   }
}

const sarchLoading = ref(false);
async function onSearch() {
   sarchLoading.value = true;
   getPersonPage(() => {
      sarchLoading.value = false;
   });
}

onMounted(() => {
   onSearch();
});

function onRowDbClick(row) {
   router.push({
      path: '/pages/yydd/yyddadd',
      query: {
         id: row.id,
         show: true
      }
   });
}

function onDeleteList(row) {
   ElMessageBox({
      title: '警告',
      type: 'warning',
      message: '确定要删除该订单吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .deleteData({ id: row.id })
               .then(() => {
                  ElMessage.success('删除成功');
                  onSearch();
                  done();
               })
               .catch(() => {
                  ElMessage.error('删除失败');
               });
         } else {
            done();
         }
      }
   });
}

function onRefundDeposit(row) {
   ElMessageBox({
      title: '提示',
      type: 'warning',
      message: '确定要退还该订单的押金吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .refundDeposit({ id: row.id })
               .then(() => {
                  ElMessage.success('退押金成功');
                  onSearch();
                  done();
               })
               .catch(() => {
                  ElMessage.error('退押金失败');
               });
         } else {
            done();
         }
      }
   });
}

function onSettleOrder(row) {
   ElMessageBox({
      title: '提示',
      type: 'info',
      message: '确定要结款该订单吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .settleOrder({ id: row.id })
               .then(() => {
                  ElMessage.success('结款成功');
                  onSearch();
                  done();
               })
               .catch(() => {
                  ElMessage.error('结款失败');
               });
         } else {
            done();
         }
      }
   });
}

function onRefundOrder(row) {
   ElMessageBox({
      title: '警告',
      type: 'warning',
      message: '确定要退款该订单吗? 退款后订单将异常终止',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .refundOrder({ id: row.id })
               .then(() => {
                  ElMessage.success('退款成功');
                  onSearch();
                  done();
               })
               .catch(() => {
                  ElMessage.error('退款失败');
               });
         } else {
            done();
         }
      }
   });
}

const searchFormRef = ref();

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function onEdit(row) {
   const id = row.id;
   router.push({
      path: '/pages/yydd/yyddadd',
      query: {
         id
      }
   });
}

function resetSearchForm(formEl) {
   if (!formEl) return;
   formEl.resetFields();
   onSearch();
}

const exportLoading = ref(false);

async function exportHandler() {
   try {
      exportLoading.value = true;
      const response = await apis.exportData();
      const url = window.URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = url;
      link.download = `订单列表${new Date().getTime()}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      ElMessage.success('导出成功');
   } catch (error) {
      console.error('导出失败:', error);
      ElMessage.error('导出失败，请稍后重试');
   } finally {
      exportLoading.value = false;
   }
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

.menu-box {
   display: flex;
   justify-content: space-between;
   margin: 24px 0 16px;
}

.pagination {
   display: flex;
   justify-content: end;
   margin-top: 20px;
}

.right {
   display: flex;
   justify-content: center;
   align-items: center;
}
</style>
