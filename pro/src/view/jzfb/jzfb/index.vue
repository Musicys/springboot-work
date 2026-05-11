<template>
   <DefaultContainer>
      <div class="title"></div>
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

      <div class="menu-box">
         <div class="right">
            <ElButton type="primary" style="margin-right: 10px" @click="handleAdd"> 发布新兼职 </ElButton>
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
         <ElTableColumn fixed="right" width="240" label="操作">
            <template #default="{ row }">
               <ElButton type="danger" link @click="onDelete(row)"> 删除 </ElButton>
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

      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="900px" @close="handleDialogClose">
         <ElForm ref="formRef" :model="form" label-position="top" :rules="formRules">
            <ElRow :gutter="20">
               <ElCol :span="12">
                  <ElFormItem label="岗位标题" prop="title">
                     <ElInput v-model="form.title" placeholder="请输入岗位标题" clearable />
                  </ElFormItem>
               </ElCol>
               <ElCol :span="12">
                  <ElFormItem label="分类ID" prop="categoryId">
                     <ElInputNumber v-model="form.categoryId" :min="1" placeholder="请输入分类ID" style="width: 100%" />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="12">
                  <ElFormItem label="地区代码" prop="regionCode">
                     <ElInput v-model="form.regionCode" placeholder="请输入地区代码" clearable />
                  </ElFormItem>
               </ElCol>
               <ElCol :span="12">
                  <ElFormItem label="地区名称" prop="regionName">
                     <ElInput v-model="form.regionName" placeholder="请输入地区名称" clearable />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <ElFormItem label="最低薪资" prop="salaryMin">
                     <ElInputNumber
                        v-model="form.salaryMin"
                        :min="0"
                        :precision="2"
                        placeholder="最低薪资"
                        style="width: 100%" />
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="最高薪资" prop="salaryMax">
                     <ElInputNumber
                        v-model="form.salaryMax"
                        :min="0"
                        :precision="2"
                        placeholder="最高薪资"
                        style="width: 100%" />
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="押金金额" prop="depositAmount">
                     <ElInputNumber
                        v-model="form.depositAmount"
                        :min="0"
                        :precision="2"
                        placeholder="押金金额"
                        style="width: 100%" />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <ElFormItem label="交易模式" prop="tradeMode">
                     <ElSelect v-model="form.tradeMode" placeholder="请选择" style="width: 100%">
                        <ElOption label="平台担保" :value="1" />
                        <ElOption label="允许线下" :value="2" />
                     </ElSelect>
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="结算周期" prop="settlementCycle">
                     <ElSelect v-model="form.settlementCycle" placeholder="请选择" style="width: 100%">
                        <ElOption label="日结" :value="1" />
                        <ElOption label="周结" :value="2" />
                        <ElOption label="月结" :value="3" />
                        <ElOption label="完工结" :value="4" />
                     </ElSelect>
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="招聘人数" prop="recruitNum">
                     <ElInputNumber v-model="form.recruitNum" :min="1" placeholder="招聘人数" style="width: 100%" />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <ElFormItem label="岗位类型" prop="jobType">
                     <ElSelect v-model="form.jobType" placeholder="请选择" style="width: 100%">
                        <ElOption label="线上" :value="1" />
                        <ElOption label="线下" :value="2" />
                        <ElOption label="校园兼职" :value="3" />
                        <ElOption label="校外兼职" :value="4" />
                     </ElSelect>
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="工作时间类型" prop="workTimeType">
                     <ElSelect v-model="form.workTimeType" placeholder="请选择" style="width: 100%">
                        <ElOption label="周末" :value="1" />
                        <ElOption label="周一至周五" :value="2" />
                        <ElOption label="自定义时间范围" :value="3" />
                     </ElSelect>
                  </ElFormItem>
               </ElCol>
               <ElCol :span="8">
                  <ElFormItem label="过期时间" prop="expireTime">
                     <ElDatePicker
                        v-model="form.expireTime"
                        type="datetime"
                        placeholder="请选择过期时间"
                        style="width: 100%" />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <ElFormItem label="工作时间描述" prop="workTimeDesc">
                     <ElInput v-model="form.workTimeDesc" placeholder="请输入工作时间描述" clearable />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <ElFormItem label="简介" prop="briefIntro">
                     <ElInput v-model="form.briefIntro" type="textarea" :rows="2" placeholder="请输入简介" clearable />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <ElFormItem label="岗位描述" prop="description">
                     <ElInput
                        v-model="form.description"
                        type="textarea"
                        :rows="4"
                        placeholder="请输入岗位描述"
                        clearable />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <ElFormItem label="岗位详细描述" prop="jobDetail">
                     <ElInput
                        v-model="form.jobDetail"
                        type="textarea"
                        :rows="4"
                        placeholder="请输入岗位详细描述"
                        clearable />
                  </ElFormItem>
               </ElCol>
            </ElRow>
            <ElRow :gutter="20">
               <ElCol :span="24">
                  <ElFormItem label="封面图">
                     <div class="cover-images">
                        <div v-for="(img, index) in coverImages" :key="index" class="cover-item">
                           <img :src="img" alt="" />
                           <div class="cover-actions">
                              <ElButton type="danger" size="small" @click="removeCoverImage(index)">删除</ElButton>
                           </div>
                        </div>
                        <div class="cover-add" @click="showImagePicker = true">
                           <ElIcon><Plus /></ElIcon>
                           <span>添加封面图</span>
                        </div>
                     </div>
                  </ElFormItem>
               </ElCol>
            </ElRow>
         </ElForm>
         <template #footer>
            <ElButton @click="dialogVisible = false">取消</ElButton>
            <ElButton type="primary" :loading="submitLoading" @click="handleSubmit">确定</ElButton>
         </template>
      </ElDialog>

      <FileUpdate v-model:visible="showImagePicker" @select="handleImageSelect" />
   </DefaultContainer>
</template>

<script setup>
import {
   ElButton,
   ElCol,
   ElDatePicker,
   ElDialog,
   ElForm,
   ElFormItem,
   ElIcon,
   ElImage,
   ElInput,
   ElInputNumber,
   ElMessage,
   ElMessageBox,
   ElOption,
   ElRow,
   ElSelect,
   ElTable,
   ElTableColumn,
   ElPagination
} from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { ref, reactive, onMounted, computed } from 'vue';
import * as apis from '@/api/jzfb/jzfb.ts';
import DefaultContainer from '@/components/DefaultContainer/index.vue';
import FileUpdate from '@/components/FileUpdate/index.vue';

const loading = ref(false);
const searchLoading = ref(false);
const searchFormRef = ref(null);
const formRef = ref(null);
const dialogVisible = ref(false);
const submitLoading = ref(false);
const showImagePicker = ref(false);
const isEdit = ref(false);
const editId = ref(null);

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
   { prop: 'jobStatus', label: '审核状态', width: '100' },
   { prop: 'tradeMode', label: '交易模式', width: '100' },
   { prop: 'createdAt', label: '创建时间', width: '160' }
]);

const form = reactive({
   title: '',
   categoryId: null,
   regionCode: '',
   regionName: '',
   salaryMin: null,
   salaryMax: null,
   depositAmount: null,
   tradeMode: 1,
   settlementCycle: 1,
   expireTime: '',
   description: '',
   jobType: 1,
   workTimeType: 1,
   workTimeDesc: '',
   briefIntro: '',
   jobDetail: '',
   recruitNum: 1
});

const coverImages = ref([]);

const dialogTitle = computed(() => (isEdit.value ? '编辑兼职岗位' : '发布新兼职'));

const formRules = reactive({
   title: [{ required: true, message: '请输入岗位标题', trigger: 'change' }],
   categoryId: [{ required: true, message: '请输入分类ID', trigger: 'change' }],
   regionCode: [{ required: true, message: '请输入地区代码', trigger: 'change' }]
});

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

function getCoverImages(coverImagesStr) {
   if (!coverImagesStr) return [];
   try {
      const imgs = JSON.parse(coverImagesStr);
      return Array.isArray(imgs) ? imgs : [];
   } catch {
      return [];
   }
}

async function getPersonPage() {
   try {
      loading.value = true;
      const params = {
         pageNum: pagination.pageNum,
         pageSize: pagination.pageSize
      };
      if (searchForm.keyword) params.keyword = searchForm.keyword;

      const res = await apis.getList(params);

      if (res.code === 0 && res.data) {
         tableData.value = res.data.records || [];
         pagination.total = Number(res.data.total) || 0;
         console.log(pagination.total);
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
   await getPersonPage();
   searchLoading.value = false;
}

function handleAdd() {
   isEdit.value = false;
   editId.value = null;
   resetForm();
   dialogVisible.value = true;
}

async function handleEdit(row) {
   isEdit.value = true;
   editId.value = row.id;
   try {
      const res = await apis.getDetail({ id: row.id });
      if (res.code === 0 && res.data) {
         Object.keys(form).forEach(key => {
            if (res.data[key] !== undefined && res.data[key] !== null) {
               if (key === 'expireTime' && res.data[key]) {
                  form[key] = new Date(res.data[key]);
               } else {
                  form[key] = res.data[key];
               }
            }
         });
         coverImages.value = getCoverImages(res.data.coverImages);
         dialogVisible.value = true;
      }
   } catch (error) {
      ElMessage.error('获取详情失败');
   }
}

function handleDialogClose() {
   resetForm();
}

function resetForm() {
   Object.keys(form).forEach(key => {
      if (key === 'tradeMode' || key === 'settlementCycle' || key === 'jobType' || key === 'workTimeType') {
         form[key] = 1;
      } else if (key === 'recruitNum' || key === 'categoryId') {
         form[key] = null;
      } else if (key === 'salaryMin' || key === 'salaryMax' || key === 'depositAmount') {
         form[key] = null;
      } else {
         form[key] = '';
      }
   });
   coverImages.value = [];
   formRef.value?.resetFields();
}

function handleImageSelect(image) {
   coverImages.value.push(image.url);
   showImagePicker.value = false;
}

function removeCoverImage(index) {
   coverImages.value.splice(index, 1);
}

async function handleSubmit() {
   try {
      await formRef.value.validate();
   } catch {
      return;
   }

   try {
      submitLoading.value = true;
      const submitData = { ...form };
      if (coverImages.value.length > 0) {
         submitData.coverImages = JSON.stringify(coverImages.value);
      }
      submitData.expireTime = submitData.expireTime ? new Date(submitData.expireTime).toISOString() : null;

      let res;
      if (isEdit.value) {
         submitData.id = editId.value;
         res = await apis.updateData(submitData);
      } else {
         res = await apis.createData(submitData);
      }

      if (res.code === 0) {
         ElMessage.success(isEdit.value ? '修改成功' : '创建成功');
         dialogVisible.value = false;
         await onSearch();
      }
   } catch (error) {
      ElMessage.error(isEdit.value ? '修改失败' : '创建失败');
   } finally {
      submitLoading.value = false;
   }
}

function onDelete(row) {
   ElMessageBox({
      title: '确认删除',
      type: 'warning',
      message: '确定要删除该兼职岗位吗？',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      beforeClose: (action, instance, done) => {
         if (action === 'confirm') {
            apis
               .deleteJob({ id: row.id })
               .then(res => {
                  if (res.code === 0) {
                     ElMessage.success('删除成功');
                     onSearch();
                  }
               })
               .catch(() => {
                  ElMessage.error('删除失败');
               })
               .finally(() => done());
         } else {
            done();
         }
      }
   });
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

.cover-images {
   display: flex;
   flex-wrap: wrap;
   gap: 10px;
}

.cover-item {
   position: relative;
   width: 120px;

   img {
      width: 100%;
      height: 80px;
      object-fit: cover;
      border-radius: 4px;
      border: 1px solid #eee;
   }

   .cover-actions {
      display: flex;
      justify-content: center;
      margin-top: 4px;
   }
}

.cover-add {
   width: 120px;
   height: 80px;
   border: 2px dashed #ddd;
   border-radius: 4px;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   cursor: pointer;
   color: #999;
   transition: all 0.3s;

   &:hover {
      border-color: #409eff;
      color: #409eff;
   }

   .el-icon {
      font-size: 24px;
      margin-bottom: 4px;
   }

   span {
      font-size: 12px;
   }
}
</style>
