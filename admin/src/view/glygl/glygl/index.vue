<template>
   <DefaultContainer>
      <div class="title">管理员管理</div>
      <div class="search-box">
         <div style="padding: 15px 8px">
            <ElForm ref="searchFormRef" :model="searchForm" label-width="120" label-position="left">
               <ElRow :gutter="20">
                  <!-- 第一行：搜索字段 -->
                  <ElCol :span="8">
                     <ElFormItem label="用户名：" prop="username">
                        <ElInput v-model="searchForm.username" clearable placeholder="请输入" style="width: 100%" />
                     </ElFormItem>
                  </ElCol>
                  <ElCol :span="8">
                     <ElFormItem label="地区代码：" prop="regionCode">
                        <ElInput v-model="searchForm.regionCode" clearable placeholder="请输入" style="width: 100%" />
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

      <!-- 功能按钮 -->
      <div class="menu-box">
         <div class="right">
            <ElButton type="primary" style="margin-right: 10px" @click="router.push(`/pages/glygl/glygladd`)">
               新增
            </ElButton>
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
               <!-- 状态格式化 -->
               <span v-if="column.prop === 'status'">
                  {{ getStatusText(scope.row.status) }}
               </span>
               <!-- 头像渲染 -->
               <ElAvatar v-else-if="column.prop === 'avatarUrl'" :size="40" :src="scope.row.avatarUrl" />
               <!-- 用户类型格式化 -->
               <span v-else-if="column.prop === 'userType'">
                  {{ getUserTypeText(scope.row.userType) }}
               </span>
               <!-- 角色格式化 -->
               <span v-else-if="column.prop === 'userRote'">
                  {{ getUserRoteText(scope.row.userRote) }}
               </span>
            </template>
         </ElTableColumn>
         <ElTableColumn fixed="right" width="180" label="操作">
            <template #default="{ row }">
               <ElButton type="primary" link @click="onEdit(row)"> 编辑 </ElButton>
               <ElButton type="primary" link @click="onDeleteList(row)"> 删除 </ElButton>
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
import { ElAvatar, ElCol, ElForm, ElFormItem, ElMessage, ElMessageBox, ElRow, ElSelect, ElOption } from 'element-plus';
import { getCurrentInstance, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import * as apis from '@/api/glygl/glygl.ts';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

import { usePagination } from '@/util/hooks.ts';

const { proxy } = getCurrentInstance();
const { pagination } = usePagination();
const router = useRouter();

const columns = ref([
   { prop: 'id', label: 'ID', width: '80' },
   { prop: 'username', label: '用户名', minWidth: '200', tooltip: true },
   { prop: 'userType', label: '用户类型', width: '120' },
   { prop: 'userRote', label: '角色', width: '120' },
   { prop: 'avatarUrl', label: '头像', width: '100' },
   { prop: 'regionCode', label: '地区代码', width: '150' },
   { prop: 'status', label: '状态', width: '100' },
   { prop: 'createdAt', label: '创建时间', width: '180', tooltip: true },
   { prop: 'updatedAt', label: '更新时间', width: '180', tooltip: true }
]);
const tableData = ref([]);
const loading = ref(false);
const searchForm = ref({
   username: null,
   regionCode: null,
   status: null
});

// 获取状态文本
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

// 获取用户类型文本
function getUserTypeText(userType) {
   switch (userType) {
      case 1:
         return '求职者';
      case 2:
         return '商家';
      case 3:
         return '管理员';
      default:
         return '未知';
   }
}

// 获取角色文本
function getUserRoteText(userRote) {
   switch (userRote) {
      case 0:
         return '普通管理员';
      case 1:
         return '超级管理员';
      default:
         return '未知';
   }
}

async function getPersonPage(fun = () => {}) {
   try {
      loading.value = true;
      // 构建查询参数
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
      pagination.total = res.data.total;
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
      path: '/pages/glygl/glygladd',
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
      message: '确定要删除该管理员吗?',
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

const searchFormRef = ref();

function onSizeChange(pageSize) {
   pagination.pageSize = pageSize;
   pagination.pageNum = 1;
   onSearch();
}

function onEdit(row) {
   const id = row.id;
   router.push({
      path: '/pages/glygl/glygladd',
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
// 导出loading
const exportLoading = ref(false);

/** 导出按钮操作 */
async function exportHandler() {
   try {
      exportLoading.value = true;
      console.log('导出');

      const response = await apis.exportData();

      // 创建下载链接
      const url = window.URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = url;
      link.download = `管理员列表${new Date().getTime()}.xlsx`;
      document.body.appendChild(link);
      link.click();
      // 清理资源
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
