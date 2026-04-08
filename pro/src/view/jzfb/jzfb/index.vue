<template>
   <DefaultContainer>
      <div class="title">兼职发布管理</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <!-- 第一行：搜索字段 -->
                  <ElCol :span="8">
                     <ElFormItem label="岗位标题：" prop="title">
                        <ElInput v-model="searchForm.title" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="分类ID：" prop="categoryId">
                        <ElInput v-model="searchForm.categoryId" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="地区代码：" prop="regionCode">
                        <ElInput v-model="searchForm.regionCode" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="状态：" prop="status">
                        <ElSelect v-model="searchForm.status" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="未审核" value="0" />
                           <ElOption label="已审核" value="1" />
                           <ElOption label="进行中" value="2" />
                           <ElOption label="已完成" value="3" />
                           <ElOption label="已关闭" value="4" />
                           <ElOption label="强制下架" value="5" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="交易模式：" prop="tradeMode">
                        <ElSelect v-model="searchForm.tradeMode" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="平台担保" value="1" />
                           <ElOption label="允许线下" value="2" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="是否结款：" prop="settled">
                        <ElSelect v-model="searchForm.settled" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="已结款" value="1" />
                           <ElOption label="未结款" value="0" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="最低薪资：" prop="salaryMin">
                        <ElInput
                           v-model="searchForm.salaryMin"
                           type="number"
                           clearable
                           placeholder="请输入"
                           style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="最高薪资：" prop="salaryMax">
                        <ElInput
                           v-model="searchForm.salaryMax"
                           type="number"
                           clearable
                           placeholder="请输入"
                           style="width: 100%" />
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

      <!-- 功能按钮 -->
      <div class="menu-box">
         <div class="right">
            <ElButton type="primary" style="margin-right: 10px" @click="router.push(`/pages/jzfb/jzfbadd`)">
               发布新兼职
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
               <!-- 状态格式化 -->
               <span v-if="column.prop === 'status'">
                  {{ getStatusText(scope.row.status) }}
               </span>
               <!-- 交易模式格式化 -->
               <span v-else-if="column.prop === 'tradeMode'">
                  {{ getTradeModeText(scope.row.tradeMode) }}
               </span>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="240" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onEdit(row)"> 编辑 </ElButton>
               <ElButton type="success" link @click="onSettle(row)"> 下架 </ElButton>
               <!-- <ElButton type="info" link @click="onRefundDeposit(row)"> 退押金 </ElButton> -->
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
import { ElButton, ElMessage, ElMessageBox } from 'element-plus';
import { getCurrentInstance, onMounted, ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import * as apis from '@/api/jzfb/jzfb.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

import { usePagination } from '@/util/hooks.ts';

const { proxy } = getCurrentInstance();
const { pagination } = usePagination();
const router = useRouter();
const searchForm = reactive({
   title: '',
   categoryId: '',
   regionCode: '',
   salaryMin: '',
   salaryMax: '',
   status: '',
   tradeMode: '',
   settled: ''
});
const columns = ref([
   { prop: 'id', label: 'ID', width: '80' },
   { prop: 'title', label: '岗位标题', minWidth: '200', tooltip: true },
   { prop: 'categoryId', label: '分类ID', width: '100' },
   { prop: 'regionCode', label: '地区代码', width: '120' },
   { prop: 'salaryMin', label: '最低薪资', width: '120' },
   { prop: 'salaryMax', label: '最高薪资', width: '120' },
   { prop: 'depositAmount', label: '押金金额', width: '120' },
   { prop: 'status', label: '状态', width: '100' },
   { prop: 'tradeMode', label: '交易模式', width: '100' },
   { prop: 'createdAt', label: '创建时间', width: '180', tooltip: true }
]);
const tableData = ref([
   {
      id: 1,
      title: '兼职服务员',
      categoryId: 1,
      regionCode: '110000',
      salaryMin: 100,
      salaryMax: 150,
      depositAmount: 50,
      status: 0,
      tradeMode: 1,
      createdAt: '2026-01-01 10:00:00'
   },
   {
      id: 2,
      title: '兼职家教',
      categoryId: 2,
      regionCode: '110000',
      salaryMin: 200,
      salaryMax: 300,
      depositAmount: 100,
      status: 1,
      tradeMode: 1,
      createdAt: '2026-01-02 11:00:00'
   },
   {
      id: 3,
      title: '兼职发传单',
      categoryId: 3,
      regionCode: '110000',
      salaryMin: 80,
      salaryMax: 100,
      depositAmount: 0,
      status: 0,
      tradeMode: 2,
      createdAt: '2026-01-03 12:00:00'
   }
]);
const loading = ref(false);

// 获取状态文本
function getStatusText(status) {
   switch (status) {
      case 0:
         return '未审核';
      case 1:
         return '已审核';
      case 2:
         return '进行中';
      case 3:
         return '已完成';
      case 4:
         return '已关闭';
      case 5:
         return '强制下架';
      default:
         return '未知';
   }
}

// 获取交易模式文本
function getTradeModeText(tradeMode) {
   switch (tradeMode) {
      case 1:
         return '平台担保';
      case 2:
         return '允许线下';
      default:
         return '未知';
   }
}

async function getPersonPage(fun = () => {}) {
   try {
      loading.value = true;
      const res = await apis.getList({
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize,
         params: { ...searchForm }
      });

      // tableData.value = res.data.records;
      // pagination.total = Number(res.data.total);
   } catch (err) {
      console.log(err);
   } finally {
      fun();
      loading.value = false;
   }
}

// 搜索loding
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
      path: '/pages/jzfb/jzfbadd',
      query: {
         id: row.id,
         show: true
      }
   });
}

function onEdit(row) {
   const id = row.id;
   router.push({
      path: '/pages/jzfb/jzfbadd',
      query: {
         id
      }
   });
}

function onSettle(row) {
   ElMessageBox({
      title: '确认结款',
      type: 'warning',
      message: '确定要为该兼职岗位结款吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .settleJob({ id: row.id })
               .then(() => {
                  ElMessage.success('结款成功');
                  onSearch();
                  done();
               })
               .catch(() => {
                  ElMessage.error('结款失败');
               })
               .finally(() => {});
         } else {
            done();
         }
      }
   });
}

function onRefundDeposit(row) {
   ElMessageBox({
      title: '确认退押金',
      type: 'warning',
      message: '确定要为该兼职岗位退还押金吗?',
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
               })
               .finally(() => {});
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

function resetSearchForm(formRef) {
   if (formRef) {
      formRef.resetFields();
   }
   // 重置搜索表单
   Object.keys(searchForm).forEach(key => {
      searchForm[key] = '';
   });
   // 重新搜索
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
