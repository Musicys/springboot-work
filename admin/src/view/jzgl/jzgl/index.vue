<template>
   <DefaultContainer>
      <div class="title">兼职岗位管理</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="6">
                     <ElFormItem label="ID：" prop="id">
                        <ElInputNumber v-model="searchForm.id" :min="1" placeholder="请输入ID" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="6">
                     <ElFormItem label="岗位标题：" prop="title">
                        <ElInput v-model="searchForm.title" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="6">
                     <ElFormItem label="商家ID：" prop="merchantId">
                        <ElInputNumber
                           v-model="searchForm.merchantId"
                           :min="1"
                           placeholder="请输入"
                           style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="6">
                     <ElFormItem label="分类ID：" prop="categoryId">
                        <ElInputNumber
                           v-model="searchForm.categoryId"
                           :min="1"
                           placeholder="请输入"
                           style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <ElFormItem label="地区代码：" prop="regionCode">
                        <ElInput v-model="searchForm.regionCode" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="岗位状态：" prop="status">
                        <ElSelect v-model="searchForm.status" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="发布中" value="1" />
                           <ElOption label="进行中" value="2" />
                           <ElOption label="已完成" value="3" />
                           <ElOption label="已关闭" value="4" />
                           <ElOption label="强制下架" value="5" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="审核状态：" prop="jobStatus">
                        <ElSelect v-model="searchForm.jobStatus" clearable placeholder="请选择" style="width: 100%">
                           <ElOption label="待审核" value="0" />
                           <ElOption label="审核通过" value="1" />
                        </ElSelect>
                     </ElFormItem>
                  </ElCol>
               </ElRow>
               <div style="margin-top: 10px">
                  <ElButton :loading="sarchLoading" type="primary" @click="onSearch"> 查询 </ElButton>
                  <ElButton @click="resetSearchForm(searchFormRef)"> 重置 </ElButton>
                  <ElButton :loading="exportLoading" style="margin-left: 10px" @click="exportHandler">
                     <i class="iconfont icon-Download" style="margin-right: 6px" />
                     导出
                  </ElButton>
               </div>
            </ElForm>
         </div>
      </div>

      <div class="menu-box">
         <div class="right">
            <ElButton type="primary" style="margin-right: 10px" @click="router.push(`/pages/jzgl/jzgladd`)">
               新增
            </ElButton>
         </div>
      </div>
      <ElTable v-loading="loading" :data="tableData" border max-height="550" @row-dblclick="onRowDbClick">
         <ElTableColumn prop="coverImages" label="封面图" width="100" align="center">
            <template #default="scope">
               <div v-if="scope.row.coverImages" class="cover-image">
                  <img :src="getFirstImage(scope.row.coverImages)" alt="封面图" class="img-thumbnail" />
               </div>
               <span v-else>-</span>
            </template>
         </ElTableColumn>
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
               <span v-if="column.prop === 'status'">
                  <ElTag :type="getStatusTagType(scope.row.status)">{{ getStatusText(scope.row.status) }}</ElTag>
               </span>
               <span v-else-if="column.prop === 'jobStatus'">
                  <ElTag :type="scope.row.jobStatus === 1 ? 'success' : 'warning'">{{
                     getJobStatusText(scope.row.jobStatus)
                  }}</ElTag>
               </span>
               <span v-else-if="column.prop === 'tradeMode'">
                  {{ getTradeModeText(scope.row.tradeMode) }}
               </span>
               <span
                  v-else-if="
                     column.prop === 'salaryMin' || column.prop === 'salaryMax' || column.prop === 'depositAmount'
                  ">
                  {{ scope.row[column.prop] ? `¥${scope.row[column.prop]}` : '-' }}
               </span>
               <span v-else>{{ scope.row[column.prop] || '-' }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="280" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onView(row)"> 查看 </ElButton>
               <ElButton v-if="row.jobStatus === 0" type="success" link @click="onApprove(row)"> 审核通过 </ElButton>
               <ElButton v-if="row.status !== 4 && row.status !== 5" type="danger" link @click="onForceClose(row)">
                  强制下架
               </ElButton>
               <ElButton v-if="row.status === 4 || row.status === 5" type="warning" link @click="onReOpen(row)">
                  重新上架
               </ElButton>
               <ElButton type="danger" link @click="onDeleteList(row)"> 删除 </ElButton>
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

      <ElDialog v-model="detailVisible" title="兼职详情" width="1000px">
         <div v-if="detailData" class="detail-content">
            <h3 style="margin-bottom: 20px; color: #333">兼职岗位信息</h3>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">岗位标题：</span>
                     <span class="value">{{ detailData.job?.title }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">商家ID：</span>
                     <span class="value">{{ detailData.job?.merchantId }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">分类ID：</span>
                     <span class="value">{{ detailData.job?.categoryId }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">地区：</span>
                     <span class="value">{{ detailData.job?.regionName || detailData.job?.regionCode }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">薪资范围：</span>
                     <span class="value">
                        {{ detailData.job?.salaryMin ? `¥${detailData.job.salaryMin}` : '-' }} -
                        {{ detailData.job?.salaryMax ? `¥${detailData.job.salaryMax}` : '-' }}
                     </span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">押金金额：</span>
                     <span class="value">{{
                        detailData.job?.depositAmount ? `¥${detailData.job.depositAmount}` : '-'
                     }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">招聘人数：</span>
                     <span class="value">{{ detailData.job?.recruitNum || '-' }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">岗位状态：</span>
                     <span class="value">{{ getStatusText(detailData.job?.status) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">审核状态：</span>
                     <span class="value">{{ getJobStatusText(detailData.job?.jobStatus) }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">交易模式：</span>
                     <span class="value">{{ getTradeModeText(detailData.job?.tradeMode) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">结算周期：</span>
                     <span class="value">{{ getSettlementCycleText(detailData.job?.settlementCycle) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">岗位类型：</span>
                     <span class="value">{{ getJobTypeText(detailData.job?.jobType) }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">简介：</span>
                     <span class="value">{{ detailData.job?.briefIntro || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">岗位描述：</span>
                     <span class="value">{{ detailData.job?.description || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">详细描述：</span>
                     <span class="value">{{ detailData.job?.jobDetail || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>

            <h3 style="margin: 30px 0 20px; color: #333">商家档案信息</h3>
            <div v-if="detailData.merchantProfile">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">企业名称：</span>
                        <span class="value">{{ detailData.merchantProfile.companyName || '-' }}</span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">法人姓名：</span>
                        <span class="value">{{ detailData.merchantProfile.legalPerson || '-' }}</span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">法人身份证：</span>
                        <span class="value">{{ detailData.merchantProfile.legalIdCard || '-' }}</span>
                     </div>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">联系电话：</span>
                        <span class="value">{{ detailData.merchantProfile.contactPhone || '-' }}</span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">公司地址：</span>
                        <span class="value">{{ detailData.merchantProfile.companyAddress || '-' }}</span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">商家位置：</span>
                        <span class="value">{{ detailData.merchantProfile.location || '-' }}</span>
                     </div>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">注册资本：</span>
                        <span class="value">{{
                           detailData.merchantProfile.registeredCapital
                              ? `¥${detailData.merchantProfile.registeredCapital}`
                              : '-'
                        }}</span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">账号类型：</span>
                        <span class="value">{{
                           detailData.merchantProfile.isSubAccount === 1 ? '子账号' : '主账号'
                        }}</span>
                     </div>
                  </ElCol>
               </ElRow>
               <ElRow :gutter="20">
                  <ElCol :span="24">
                     <div class="detail-item">
                        <span class="label">公司简介：</span>
                        <span class="value">{{ detailData.merchantProfile.companyIntro || '-' }}</span>
                     </div>
                  </ElCol>
               </ElRow>
            </div>
            <div v-else style="color: #999; padding: 10px">暂无商家档案信息</div>

            <h3 style="margin: 30px 0 20px; color: #333">商家钱包信息</h3>
            <div v-if="detailData.wallet">
               <ElRow :gutter="20">
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">可用余额：</span>
                        <span class="value" style="color: #67c23a; font-weight: bold">
                           ¥{{ detailData.wallet.balance || '0.00' }}
                        </span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">冻结金额：</span>
                        <span class="value" style="color: #e6a23c; font-weight: bold">
                           ¥{{ detailData.wallet.frozenBalance || '0.00' }}
                        </span>
                     </div>
                  </ElCol>
                  <ElCol :span="8">
                     <div class="detail-item">
                        <span class="label">累计收入：</span>
                        <span class="value" style="color: #409eff; font-weight: bold">
                           ¥{{ detailData.wallet.totalIncome || '0.00' }}
                        </span>
                     </div>
                  </ElCol>
               </ElRow>
            </div>
            <div v-else style="color: #999; padding: 10px">暂无钱包信息</div>
         </div>
         <template #footer>
            <ElButton @click="detailVisible = false">关闭</ElButton>
         </template>
      </ElDialog>
   </DefaultContainer>
</template>

<script setup>
import {
   ElCol,
   ElForm,
   ElFormItem,
   ElInputNumber,
   ElMessage,
   ElMessageBox,
   ElRow,
   ElSelect,
   ElOption,
   ElTag,
   ElDialog
} from 'element-plus';
import { getCurrentInstance, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import * as apis from '@/api/jzgl/jzgl.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

import { usePagination } from '@/util/hooks.ts';

const { proxy } = getCurrentInstance();
const { pagination } = usePagination();
const router = useRouter();

const columns = ref([
   { prop: 'id', label: 'ID', width: '80' },
   { prop: 'title', label: '岗位标题', minWidth: '200', tooltip: true },
   { prop: 'merchantId', label: '商家ID', width: '120' },
   { prop: 'categoryId', label: '分类ID', width: '100' },
   { prop: 'regionCode', label: '地区代码', width: '120' },
   { prop: 'salaryMin', label: '最低薪资', width: '120' },
   { prop: 'salaryMax', label: '最高薪资', width: '120' },
   { prop: 'depositAmount', label: '押金金额', width: '120' },
   { prop: 'status', label: '岗位状态', width: '100' },
   { prop: 'jobStatus', label: '审核状态', width: '100' },
   { prop: 'tradeMode', label: '交易模式', width: '100' },
   { prop: 'createdAt', label: '创建时间', width: '180', tooltip: true }
]);
const tableData = ref([]);
const loading = ref(false);
const detailVisible = ref(false);
const detailData = ref(null);
const searchForm = ref({
   id: null,
   title: null,
   merchantId: null,
   categoryId: null,
   regionCode: null,
   status: null,
   jobStatus: 0,
   tradeMode: null
});

function getStatusText(status) {
   const map = { 1: '发布中', 2: '进行中', 3: '已完成', 4: '已关闭', 5: '强制下架' };
   return map[status] || '未知';
}

function getStatusTagType(status) {
   const map = { 1: 'info', 2: 'success', 3: 'primary', 4: 'warning', 5: 'danger' };
   return map[status] || 'default';
}

function getJobStatusText(jobStatus) {
   const map = { 0: '待审核', 1: '审核通过' };
   return map[jobStatus] || '未知';
}

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

function getFirstImage(coverImages) {
   try {
      const images = typeof coverImages === 'string' ? JSON.parse(coverImages) : coverImages;
      if (Array.isArray(images) && images.length > 0) {
         return images[0];
      }
      return '';
   } catch (e) {
      return '';
   }
}

function getSettlementCycleText(cycle) {
   const map = { 1: '日结', 2: '周结', 3: '月结', 4: '完工结' };
   return map[cycle] || '未知';
}

function getJobTypeText(type) {
   const map = { 1: '线上', 2: '线下', 3: '校园兼职', 4: '校外兼职' };
   return map[type] || '未知';
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
      path: '/pages/jzgl/jzgladd',
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
      message: '确定要删除该兼职岗位吗?',
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
               })
               .finally(() => {});
         } else {
            done();
         }
      }
   });
}

function onApprove(row) {
   ElMessageBox({
      title: '提示',
      type: 'info',
      message: '确定要审核通过该兼职岗位吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .approveJob({ id: row.id })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('审核通过成功');
                     onSearch();
                  } else {
                     ElMessage.error(res.message || '审核通过失败');
                  }
                  done();
               })
               .catch(() => {
                  ElMessage.error('审核通过失败');
                  done();
               });
         } else {
            done();
         }
      }
   });
}

function onForceClose(row) {
   ElMessageBox({
      title: '警告',
      type: 'warning',
      message: '确定要强制下架该兼职岗位吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .forceCloseJob({ id: row.id })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('强制下架成功');
                     onSearch();
                  } else {
                     ElMessage.error(res.message || '强制下架失败');
                  }
                  done();
               })
               .catch(() => {
                  ElMessage.error('强制下架失败');
                  done();
               });
         } else {
            done();
         }
      }
   });
}

function onReOpen(row) {
   ElMessageBox({
      title: '提示',
      type: 'info',
      message: '确定要重新上架该兼职岗位吗?',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .updateStatus({ jobId: row.id, status: 1 })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('重新上架成功');
                     onSearch();
                  } else {
                     ElMessage.error(res.message || '重新上架失败');
                  }
                  done();
               })
               .catch(() => {
                  ElMessage.error('重新上架失败');
                  done();
               });
         } else {
            done();
         }
      }
   });
}

async function onView(row) {
   try {
      const res = await apis.getDetail({ id: row.id });
      if (res.code === 0 && res.data) {
         detailData.value = res.data;
         detailVisible.value = true;
      }
   } catch (error) {
      ElMessage.error('获取详情失败');
   }
}

const searchFormRef = ref();

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function resetSearchForm(formEl) {
   if (!formEl) return;
   formEl.resetFields();
   searchForm.value.jobStatus = 0;
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
      link.download = `兼职岗位列表${new Date().getTime()}.xlsx`;
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

.detail-content {
   .detail-item {
      margin-bottom: 16px;

      .label {
         font-weight: 600;
         color: #666;
      }

      .value {
         color: #333;
      }
   }
}

.cover-image {
   .img-thumbnail {
      width: 60px;
      height: 60px;
      object-fit: cover;
      border-radius: 4px;
   }
}
</style>
