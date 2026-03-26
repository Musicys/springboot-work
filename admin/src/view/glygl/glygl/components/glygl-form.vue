<template>
   <ElForm ref="formRef" :model="form" label-position="top" :rules="rules">
      <!-- 基本信息板块 -->
      <Title>基本信息</Title>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="用户名" prop="username">
               <ElInput v-model="form.username" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="密码" prop="passwordHash">
               <ElInput v-model="form.passwordHash" :disabled="show" type="text" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="用户类型">
               <ElSelect v-model="form.userType" disabled placeholder="请选择">
                  <ElOption label="管理员" value="3" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="角色" prop="userRote">
               <ElSelect v-model="form.userRote" :disabled="show" placeholder="请选择">
                  <ElOption label="普通管理员" value="0" />
                  <ElOption label="超级管理员" value="1" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="头像URL" prop="avatarUrl">
               <ElInput v-model="form.avatarUrl" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="地区代码" prop="regionCode">
               <ElInput v-model="form.regionCode" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="状态" prop="status">
               <ElSelect v-model="form.status" :disabled="show" placeholder="请选择">
                  <ElOption label="正常" value="1" />
                  <ElOption label="封禁" value="0" />
                  <ElOption label="注销" value="-1" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
      </ElRow>
   </ElForm>
</template>

<script setup>
import { ElCol, ElForm, ElFormItem, ElInput, ElRow, ElSelect, ElOption } from 'element-plus';
import { computed, onMounted, reactive, ref, useTemplateRef } from 'vue';
import * as apis from '@/api/glygl/glygl.ts';
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
   username: '',
   passwordHash: '',
   userType: '3',
   avatarUrl: '',
   regionCode: '',
   status: '1'
});

const formRef = useTemplateRef('formRef');

const baseRules = reactive({
   username: [{ required: true, message: '请输入用户名', trigger: 'change' }],
   passwordHash: [{ required: !props.id, message: '请输入密码', trigger: 'change' }],
   status: [{ required: true, message: '请选择状态', trigger: 'change' }]
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
