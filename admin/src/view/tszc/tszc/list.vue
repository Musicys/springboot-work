<template>
   <DefaultContainer>
      <div class="title">投诉仲裁列表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="投诉ID：" prop="id">
                        <ElInput v-model="searchForm.id" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="订单ID：" prop="orderId">
                        <ElInput v-model="searchForm.orderId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="状态：" prop="status">
                        <ElSelect v-model="searchForm.status" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="待处理" value="1" />
                           <ElOption label="处理中" value="2" />
                           <ElOption label="成功(处罚)" value="3" />
                           <ElOption label="失败(证据不足)" value="4" />
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
         <ElTableColumn prop="id" label="投诉ID" width="100" />
         <ElTableColumn prop="orderId" label="订单ID" width="120" />
         <ElTableColumn prop="complainantId" label="投诉人ID" width="120" />
         <ElTableColumn prop="defendantId" label="被投诉人ID" width="120" />
         <ElTableColumn prop="targetType" label="被投诉类型" width="120">
            <template #default="scope">
               <span>{{ getTargetTypeText(scope.row.targetType) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="reasonCode" label="投诉原因" width="120" />
         <ElTableColumn prop="status" label="状态" width="120">
            <template #default="scope">
               <span>{{ getStatusText(scope.row.status) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="createdAt" label="创建时间" width="180" />
         <ElTableColumn fixed="right" width="180" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onView(row)"> 查看 </ElButton>
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
import { ElCol, ElForm, ElFormItem, ElRow, ElSelect, ElOption } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();
const router = useRouter();

const tableData = ref([
   {
      id: 1,
      orderId: 1001,
      complainantId: 101,
      defendantId: 1,
      targetType: 2,
      reasonCode: '服务态度差',
      status: 1,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      id: 2,
      orderId: 1002,
      complainantId: 1,
      defendantId: 102,
      targetType: 1,
      reasonCode: '用户爽约',
      status: 2,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      id: 3,
      orderId: 1003,
      complainantId: 103,
      defendantId: 2,
      targetType: 2,
      reasonCode: '薪资不符',
      status: 3,
      createdAt: '2026-01-03 12:00:00'
   },
   {
      id: 4,
      orderId: 1004,
      complainantId: 104,
      defendantId: 3,
      targetType: 2,
      reasonCode: '工作环境差',
      status: 4,
      createdAt: '2026-01-04 13:00:00'
   },
   {
      id: 5,
      orderId: 1005,
      complainantId: 2,
      defendantId: 105,
      targetType: 1,
      reasonCode: '中途逃逸',
      status: 2,
      createdAt: '2026-01-05 14:00:00'
   },
   {
      id: 6,
      orderId: 1006,
      complainantId: 106,
      defendantId: 4,
      targetType: 2,
      reasonCode: '虚假信息',
      status: 1,
      createdAt: '2026-01-06 15:00:00'
   }
]);

const loading = ref(false);
const searchForm = ref({
   id: null,
   orderId: null,
   status: null
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
   router.push({ path: '/pages/tszc/tszc/detail', query: { id: row.id } });
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
