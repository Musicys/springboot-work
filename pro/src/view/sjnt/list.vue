<template>
   <DefaultContainer>
      <div class="title">接单通知列表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="通知ID：" prop="id">
                        <ElInput v-model="searchForm.id" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="商家ID：" prop="merchantId">
                        <ElInput v-model="searchForm.merchantId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="用户ID：" prop="userId">
                        <ElInput v-model="searchForm.userId" clearable placeholder="请输入" style="width: 100%" />
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
         <ElTableColumn prop="id" label="通知ID" width="100" />
         <ElTableColumn prop="merchantId" label="商家ID" width="100" />
         <ElTableColumn prop="userId" label="用户ID" width="100" />
         <ElTableColumn prop="jobId" label="兼职ID" width="100" />
         <ElTableColumn prop="content" label="通知内容" min-width="200" />
         <ElTableColumn prop="isRead" label="是否已读" width="100">
            <template #default="scope">
               <span>{{ scope.row.isRead ? '已读' : '未读' }}</span>
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
import { ElCol, ElForm, ElFormItem, ElRow } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();
const router = useRouter();

const tableData = ref([
   {
      id: 1,
      merchantId: 1,
      userId: 101,
      jobId: 201,
      content: '用户101已接单，兼职ID：201',
      isRead: false,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      id: 2,
      merchantId: 1,
      userId: 102,
      jobId: 202,
      content: '用户102已接单，兼职ID：202',
      isRead: true,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      id: 3,
      merchantId: 2,
      userId: 103,
      jobId: 203,
      content: '用户103已接单，兼职ID：203',
      isRead: false,
      createdAt: '2026-01-03 12:00:00'
   },
   {
      id: 4,
      merchantId: 3,
      userId: 104,
      jobId: 204,
      content: '用户104已接单，兼职ID：204',
      isRead: false,
      createdAt: '2026-01-04 13:00:00'
   },
   {
      id: 5,
      merchantId: 4,
      userId: 105,
      jobId: 205,
      content: '用户105已接单，兼职ID：205',
      isRead: true,
      createdAt: '2026-01-05 14:00:00'
   },
   {
      id: 6,
      merchantId: 5,
      userId: 106,
      jobId: 206,
      content: '用户106已接单，兼职ID：206',
      isRead: false,
      createdAt: '2026-01-06 15:00:00'
   },
   {
      id: 7,
      merchantId: 6,
      userId: 107,
      jobId: 207,
      content: '用户107已接单，兼职ID：207',
      isRead: true,
      createdAt: '2026-01-07 16:00:00'
   },
   {
      id: 8,
      merchantId: 7,
      userId: 108,
      jobId: 208,
      content: '用户108已接单，兼职ID：208',
      isRead: false,
      createdAt: '2026-01-08 17:00:00'
   }
]);

const loading = ref(false);
const searchForm = ref({
   id: null,
   merchantId: null,
   userId: null
});

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
   router.push({ path: '/pages/sjnt/detail', query: { id: row.id } });
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
