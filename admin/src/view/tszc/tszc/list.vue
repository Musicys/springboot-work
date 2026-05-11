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
         <ElTableColumn prop="id" label="案件ID" width="100" />
         <ElTableColumn prop="orderId" label="订单ID" width="120" />
         <ElTableColumn prop="userId" label="用户ID" width="100" />
         <ElTableColumn prop="userUsername" label="用户姓名" width="120" />
         <ElTableColumn prop="userCreditCode" label="用户信誉分" width="120" />
         <ElTableColumn prop="shopId" label="商家ID" width="100" />
         <ElTableColumn prop="companyName" label="企业名称" width="150" show-overflow-tooltip />
         <ElTableColumn prop="contactPhone" label="联系电话" width="130" />
         <ElTableColumn prop="faultParty" label="出错方" width="130">
            <template #default="scope">
               <span>{{ getFaultPartyDesc(scope.row.faultParty) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="deductedScore" label="扣除信誉分" width="120" />
         <ElTableColumn prop="rulingResult" label="裁决结果" width="120">
            <template #default="scope">
               <span>{{ getRulingResultDesc(scope.row.rulingResult) }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="status" label="状态" width="100">
            <template #default="scope">
               <ElTag :type="scope.row.status === 1 ? 'warning' : 'success'" size="small">
                  {{ getStatusText(scope.row.status) }}
               </ElTag>
            </template>
         </ElTableColumn>
         <ElTableColumn prop="createdAt" label="创建时间" width="180">
            <template #default="scope">
               {{ formatDate(scope.row.createdAt) }}
            </template>
         </ElTableColumn>
         <ElTableColumn prop="closedAt" label="结案时间" width="180">
            <template #default="scope">
               {{ formatDate(scope.row.closedAt) }}
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="120" label="操作">
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
import * as arbitrationApi from '@/api/tszc/tszc.ts';

const { pagination } = usePagination();
const router = useRouter();

const tableData = ref([]);
const loading = ref(false);
const searchForm = ref({
   id: null,
   orderId: null,
   status: null
});

function getStatusText(status) {
   switch (status) {
      case 1:
         return '审理中';
      case 2:
         return '已结案';
      default:
         return '未知';
   }
}

function getRulingResultDesc(result) {
   switch (result) {
      case 1:
         return '全额结款';
      case 2:
         return '部分结款';
      case 3:
         return '不结款';
      case 4:
         return '退还押金';
      default:
         return '-';
   }
}

function getFaultPartyDesc(party) {
   switch (party) {
      case 0:
         return '用户';
      case 1:
         return '商家';
      case 3:
         return '无错/无法判定';
      default:
         return '-';
   }
}

function formatDate(dateStr) {
   if (!dateStr) return '-';
   try {
      const date = new Date(dateStr);
      return date.toLocaleString('zh-CN', {
         year: 'numeric',
         month: '2-digit',
         day: '2-digit',
         hour: '2-digit',
         minute: '2-digit',
         second: '2-digit'
      });
   } catch {
      return dateStr;
   }
}

async function onSearch() {
   loading.value = true;
   try {
      const params = {
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize
      };
      if (searchForm.value.id !== null && searchForm.value.id !== undefined) {
         params.id = searchForm.value.id;
      }
      if (searchForm.value.orderId !== null && searchForm.value.orderId !== undefined) {
         params.orderId = searchForm.value.orderId;
      }
      if (searchForm.value.status !== null && searchForm.value.status !== undefined) {
         params.status = searchForm.value.status;
      }

      const response = await arbitrationApi.getList(params);
      if (response.data) {
         tableData.value = response.data.records;
         pagination.total = response.data.total;
      }
   } catch (error) {
      console.error('查询仲裁案件列表失败:', error);
      tableData.value = [];
   } finally {
      loading.value = false;
   }
}

onMounted(() => {
   onSearch();
});

function onRowDbClick(row) {
   onView(row);
}

function onView(row) {
   router.push({ path: '/admin/arbitration/detail', query: { id: row.id } });
}

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function resetSearchForm(formEl) {
   if (!formEl) return;
   formEl.resetFields();
   pagination.pageNum = 1;
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
