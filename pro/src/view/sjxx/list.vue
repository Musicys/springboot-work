<template>
   <DefaultContainer>
      <div class="title">商家详情列表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="商家ID：" prop="id">
                        <ElInput v-model="searchForm.id" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="商家名称：" prop="companyName">
                        <ElInput v-model="searchForm.companyName" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="状态：" prop="status">
                        <ElSelect v-model="searchForm.status" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="正常" value="1" />
                           <ElOption label="封禁" value="0" />
                           <ElOption label="注销" value="-1" />
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
         <ElTableColumn prop="id" label="商家ID" width="100" />
         <ElTableColumn prop="companyName" label="商家名称" min-width="200" />
         <ElTableColumn prop="contactPhone" label="联系电话" width="150" />
         <ElTableColumn prop="legalPerson" label="法人姓名" width="120" />
         <ElTableColumn prop="status" label="状态" width="100">
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
import { ElCol, ElForm, ElFormItem, ElMessage, ElRow, ElSelect, ElOption } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import { usePagination } from '@/util/hooks.ts';

const { pagination } = usePagination();
const router = useRouter();

const tableData = ref([
   {
      id: 1,
      companyName: '科技有限公司',
      contactPhone: '13800138000',
      legalPerson: '张三',
      status: 1,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      id: 2,
      companyName: '贸易公司',
      contactPhone: '13900139000',
      legalPerson: '李四',
      status: 1,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      id: 3,
      companyName: '餐饮公司',
      contactPhone: '13700137000',
      legalPerson: '王五',
      status: 0,
      createdAt: '2026-01-03 12:00:00'
   },
   {
      id: 4,
      companyName: '教育培训机构',
      contactPhone: '13600136000',
      legalPerson: '赵六',
      status: 1,
      createdAt: '2026-01-04 13:00:00'
   },
   {
      id: 5,
      companyName: '医疗科技公司',
      contactPhone: '13500135000',
      legalPerson: '钱七',
      status: 1,
      createdAt: '2026-01-05 14:00:00'
   },
   {
      id: 6,
      companyName: '房地产开发公司',
      contactPhone: '13400134000',
      legalPerson: '孙八',
      status: -1,
      createdAt: '2026-01-06 15:00:00'
   },
   {
      id: 7,
      companyName: '物流运输公司',
      contactPhone: '13300133000',
      legalPerson: '周九',
      status: 1,
      createdAt: '2026-01-07 16:00:00'
   },
   {
      id: 8,
      companyName: '金融服务公司',
      contactPhone: '13200132000',
      legalPerson: '吴十',
      status: 0,
      createdAt: '2026-01-08 17:00:00'
   }
]);

const loading = ref(false);
const searchForm = ref({
   id: null,
   companyName: null,
   status: null
});

function getStatusText(status) {
   switch (status) {
      case 1:
         return '正常';
      case 0:
         return '封禁';
      case -1:
         return '注销';
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
   router.push({ path: '/pages/sjxx/detail', query: { id: row.id } });
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
