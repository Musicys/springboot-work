<template>
   <DefaultContainer>
      <div class="title">已发布兼职列表</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <ElCol :span="12">
                     <ElFormItem label="关键词搜索：" prop="keyword">
                        <ElInput
                           v-model="searchForm.keyword"
                           clearable
                           placeholder="请输入岗位标题关键词"
                           style="width: 100%" />
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
                  {{ getStatusText(scope.row.status) }}
               </span>
               <span v-else-if="column.prop === 'tradeMode'">
                  {{ getTradeModeText(scope.row.tradeMode) }}
               </span>
               <span v-else-if="column.prop === 'jobStatus'">
                  {{ getJobStatusText(scope.row.jobStatus) }}
               </span>
               <span v-else-if="column.prop === 'coverImages'">
                  <ElImage
                     v-if="getCoverImages(scope.row.coverImages).length > 0"
                     :src="getCoverImages(scope.row.coverImages)[0]"
                     fit="cover"
                     style="width: 60px; height: 60px"
                     :preview-src-list="getCoverImages(scope.row.coverImages)"
                     preview-teleported />
                  <span v-else>-</span>
               </span>
               <span v-else-if="column.prop === 'salaryMin' || column.prop === 'salaryMax'">
                  {{ scope.row[column.prop] ? `¥${scope.row[column.prop]}` : '-' }}
               </span>
               <span v-else>{{ scope.row[column.prop] || '-' }}</span>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="160" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="handleView(row)"> 查看详情 </ElButton>
               <ElButton v-if="row.status !== 4" type="warning" link @click="onClose(row)"> 下架 </ElButton>
               <ElButton v-else type="success" link @click="onOpen(row)"> 上架 </ElButton>
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

      <ElDialog v-model="detailVisible" title="兼职详情" width="900px">
         <div v-if="detailData" class="detail-content">
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">岗位标题：</span>
                     <span class="value">{{ detailData.title }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">分类ID：</span>
                     <span class="value">{{ detailData.categoryId }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">地区：</span>
                     <span class="value">{{ detailData.regionName || detailData.regionCode }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">薪资范围：</span>
                     <span class="value">
                        {{ detailData.salaryMin ? `¥${detailData.salaryMin}` : '-' }} -
                        {{ detailData.salaryMax ? `¥${detailData.salaryMax}` : '-' }}
                     </span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">押金金额：</span>
                     <span class="value">{{ detailData.depositAmount ? `¥${detailData.depositAmount}` : '-' }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">招聘人数：</span>
                     <span class="value">{{ detailData.recruitNum || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">交易模式：</span>
                     <span class="value">{{ getTradeModeText(detailData.tradeMode) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">结算周期：</span>
                     <span class="value">{{ getSettlementCycleText(detailData.settlementCycle) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">岗位类型：</span>
                     <span class="value">{{ getJobTypeText(detailData.jobType) }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <div class="detail-item">
                     <span class="label">工作时间：</span>
                     <span class="value">{{ getWorkTimeTypeText(detailData.workTimeType) }}</span>
                  </div>
               </ElCol>
               <ElCol :span="16">
                  <div class="detail-item">
                     <span class="label">工作时间描述：</span>
                     <span class="value">{{ detailData.workTimeDesc || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">简介：</span>
                     <span class="value">{{ detailData.briefIntro || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">岗位描述：</span>
                     <span class="value">{{ detailData.description || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">详细描述：</span>
                     <span class="value">{{ detailData.jobDetail || '-' }}</span>
                  </div>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <div class="detail-item">
                     <span class="label">封面图：</span>
                     <div class="cover-list">
                        <ElImage
                           v-for="(img, index) in getCoverImages(detailData.coverImages)"
                           :key="index"
                           :src="img"
                           fit="cover"
                           style="width: 120px; height: 80px; margin-right: 10px"
                           :preview-src-list="getCoverImages(detailData.coverImages)"
                           preview-teleported />
                        <span v-if="getCoverImages(detailData.coverImages).length === 0">-</span>
                     </div>
                  </div>
               </ElCol>
            </ElRow>
         </div>
         <template #footer>
            <ElButton @click="detailVisible = false">关闭</ElButton>
         </template>
      </ElDialog>
   </DefaultContainer>
</template>

<script setup>
import {
   ElButton,
   ElCol,
   ElDialog,
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
import { ref, reactive, onMounted } from 'vue';
import * as apis from '@/api/jzfb/jzfb.ts';
import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const searchLoading = ref(false);
const searchFormRef = ref(null);
const detailVisible = ref(false);
const detailData = ref(null);

const pagination = reactive({
   pageNum: 1,
   pageSize: 10,
   total: 0
});

const searchForm = reactive({
   keyword: ''
});

const tableData = ref([]);

const columns = ref([
   { prop: 'id', label: 'ID', width: '80' },
   { prop: 'coverImages', label: '封面', width: '100' },
   { prop: 'title', label: '岗位标题', minWidth: '150', tooltip: true },
   { prop: 'categoryId', label: '分类ID', width: '80' },
   { prop: 'regionName', label: '地区', width: '100' },
   { prop: 'salaryMin', label: '最低薪资', width: '100' },
   { prop: 'salaryMax', label: '最高薪资', width: '100' },
   { prop: 'status', label: '岗位状态', width: '100' },
   { prop: 'tradeMode', label: '交易模式', width: '100' },
   { prop: 'createdAt', label: '创建时间', width: '160' }
]);

function getStatusText(status) {
   const map = { 1: '发布中', 2: '进行中', 3: '已完成', 4: '已关闭', 5: '强制下架' };
   return map[status] || '未知';
}

function getJobStatusText(jobStatus) {
   const map = { 0: '待审核', 1: '审核通过' };
   return map[jobStatus] || '未知';
}

function getTradeModeText(tradeMode) {
   return tradeMode === 1 ? '平台担保' : tradeMode === 2 ? '允许线下' : '未知';
}

function getSettlementCycleText(cycle) {
   const map = { 1: '日结', 2: '周结', 3: '月结', 4: '完工结' };
   return map[cycle] || '未知';
}

function getJobTypeText(type) {
   const map = { 1: '线上', 2: '线下', 3: '校园兼职', 4: '校外兼职' };
   return map[type] || '未知';
}

function getWorkTimeTypeText(type) {
   const map = { 1: '周末', 2: '周一至周五', 3: '自定义时间范围' };
   return map[type] || '未知';
}

function getCoverImages(coverImagesStr) {
   if (!coverImagesStr) return [];
   try {
      const imgs = JSON.parse(coverImagesStr);
      return Array.isArray(imgs) ? imgs : [];
   } catch {
      return [];
   }
}

async function getPublishedPage() {
   try {
      loading.value = true;
      const params = {
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize
      };
      if (searchForm.keyword) params.keyword = searchForm.keyword;

      const res = await apis.getPublishedList(params);

      if (res.code === 0 && res.data) {
         tableData.value = res.data.records || [];
         pagination.total = Number(res.data.total);
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
   await getPublishedPage();
   searchLoading.value = false;
}

async function handleView(row) {
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

function onClose(row) {
   ElMessageBox({
      title: '确认下架',
      type: 'warning',
      message: '确定要下架该兼职岗位吗？',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .closeJob({ id: row.id })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('下架成功');
                     onSearch();
                  }
               })
               .catch(() => {
                  ElMessage.error('下架失败');
               })
               .finally(() => done());
         } else {
            done();
         }
      }
   });
}

function onOpen(row) {
   ElMessageBox({
      title: '确认上架',
      type: 'warning',
      message: '确定要上架该兼职岗位吗？',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .openJob({ id: row.id })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('上架成功');
                     onSearch();
                  }
               })
               .catch(() => {
                  ElMessage.error('上架失败');
               })
               .finally(() => done());
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
   searchForm.keyword = '';
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

.pagination {
   display: flex;
   justify-content: end;
   margin-top: 20px;
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

   .cover-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-top: 8px;
   }
}
</style>
