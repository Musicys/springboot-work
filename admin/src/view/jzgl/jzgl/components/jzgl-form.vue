<template>
   <ElForm ref="formRef" :model="form" label-position="top" :rules="rules">
      <!-- 基本信息板块 -->
      <Title>基本信息</Title>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="岗位标题" prop="title">
               <ElInput v-model="form.title" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="商家ID" prop="merchantId">
               <ElInput v-model="form.merchantId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="分类ID" prop="categoryId">
               <ElInput v-model="form.categoryId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="地区代码" prop="regionCode">
               <ElInput v-model="form.regionCode" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="最低薪资" prop="salaryMin">
               <ElInput v-model="form.salaryMin" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="最高薪资" prop="salaryMax">
               <ElInput v-model="form.salaryMax" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="押金金额" prop="depositAmount">
               <ElInput v-model="form.depositAmount" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="状态" prop="status">
               <ElSelect v-model="form.status" :disabled="show" placeholder="请选择" style="width: 100%">
                  <ElOption label="发布中" value="1" />
                  <ElOption label="进行中" value="2" />
                  <ElOption label="已完成" value="3" />
                  <ElOption label="已关闭" value="4" />
                  <ElOption label="强制下架" value="5" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="交易模式" prop="tradeMode">
               <ElSelect v-model="form.tradeMode" :disabled="show" placeholder="请选择" style="width: 100%">
                  <ElOption label="平台担保" value="1" />
                  <ElOption label="允许线下" value="2" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="发布时间" prop="publishTime">
               <ElDatePicker
                  v-model="form.publishTime"
                  :disabled="show"
                  type="datetime"
                  placeholder="请选择"
                  style="width: 100%" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="过期时间" prop="expireTime">
               <ElDatePicker
                  v-model="form.expireTime"
                  :disabled="show"
                  type="datetime"
                  placeholder="请选择"
                  style="width: 100%" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="24">
            <ElFormItem label="岗位描述" prop="description">
               <ElInput v-model="form.description" :disabled="show" type="textarea" :rows="4" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
   </ElForm>
</template>

<script setup>
import { ElCol, ElForm, ElFormItem, ElInput, ElRow, ElSelect, ElOption, ElDatePicker } from 'element-plus';
import { computed, onMounted, reactive, ref, useTemplateRef } from 'vue';
import * as apis from '@/api/jzgl/jzgl.ts';
import Title from '@/components/Title/index.vue';

const props = defineProps({
   id: {
      default: ''
   },
   show: {
      type: Boolean,
      default: false
   }
});

const form = ref({
   title: '',
   merchantId: '',
   categoryId: '',
   regionCode: '',
   salaryMin: '',
   salaryMax: '',
   depositAmount: '',
   status: '1',
   tradeMode: '1',
   publishTime: '',
   expireTime: '',
   description: ''
});

const formRef = useTemplateRef('formRef');

const baseRules = reactive({
   title: [{ required: true, message: '请输入岗位标题', trigger: 'change' }],
   merchantId: [{ required: true, message: '请输入商家ID', trigger: 'change' }],
   categoryId: [{ required: true, message: '请输入分类ID', trigger: 'change' }],
   regionCode: [{ required: true, message: '请输入地区代码', trigger: 'change' }]
});

const rules = computed(() => {
   return props.show ? {} : baseRules;
});

async function saveStaff1() {
   try {
      await apis.createData(form.value);
      return form.value;
   } catch (error) {
      return Promise.reject(new Error(error));
   }
}

async function updateFormDetaill() {
   try {
      await apis.updateData(form.value);
      return form.value;
   } catch (error) {
      return Promise.reject(new Error(error));
   }
}

async function onDetail(id) {
   try {
      const res = await apis.getDetail({ id });
      if (res.data) {
         form.value = res.data;
      }
   } catch (error) {
      console.error('获取详情失败:', error);
   }
}

onMounted(() => {
   if (props.id) {
      onDetail(props.id);
   }
});

defineExpose({
   onSave: async () => {
      try {
         await formRef.value.validate();
      } catch {
         return Promise.reject(new Error('表单校验失败'));
      }
      try {
         if (!props.id) {
            await saveStaff1();
            return Promise.resolve();
         } else {
            await updateFormDetaill();
            return Promise.resolve();
         }
      } catch (error) {
         return Promise.reject(new Error(error));
      }
   }
});
</script>

<style lang="scss" scoped>
:deep(.el-input__wrapper),
:deep(.el-select),
:deep(.el-date-editor) {
}
</style>
