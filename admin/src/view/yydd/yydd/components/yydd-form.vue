<template>
   <ElForm ref="formRef" :model="form" label-position="top" :rules="rules">
      <Title>订单信息</Title>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="投递ID" prop="applicationId">
               <ElInput v-model="form.applicationId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="兼职ID" prop="jobId">
               <ElInput v-model="form.jobId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="用户ID" prop="userId">
               <ElInput v-model="form.userId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="商家ID" prop="merchantId">
               <ElInput v-model="form.merchantId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="交易模式" prop="tradeMode">
               <ElSelect v-model="form.tradeMode" :disabled="show" placeholder="请选择">
                  <ElOption label="平台担保" :value="1" />
                  <ElOption label="线下交易" :value="2" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="订单状态" prop="orderStatus">
               <ElSelect v-model="form.orderStatus" :disabled="show" placeholder="请选择">
                  <ElOption label="待入职" :value="1" />
                  <ElOption label="进行中" :value="2" />
                  <ElOption label="已完成" :value="3" />
                  <ElOption label="纠纷中" :value="4" />
                  <ElOption label="已结款" :value="5" />
                  <ElOption label="用户爽约" :value="6" />
                  <ElOption label="异常终止" :value="7" />
               </ElSelect>
            </ElFormItem>
         </ElCol>
      </ElRow>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="押金冻结ID" prop="depositFrozenId">
               <ElInput v-model="form.depositFrozenId" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="违约金金额" prop="penaltyAmount">
               <ElInput v-model="form.penaltyAmount" :disabled="show" placeholder="请输入" />
            </ElFormItem>
         </ElCol>
      </ElRow>
      <Title>时间信息</Title>
      <ElRow :gutter="20">
         <ElCol :span="8">
            <ElFormItem label="开始时间" prop="startTime">
               <ElDatePicker
                  v-model="form.startTime"
                  :disabled="show"
                  type="datetime"
                  placeholder="请选择"
                  style="width: 100%" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="结束时间" prop="endTime">
               <ElDatePicker
                  v-model="form.endTime"
                  :disabled="show"
                  type="datetime"
                  placeholder="请选择"
                  style="width: 100%" />
            </ElFormItem>
         </ElCol>
         <ElCol :span="8">
            <ElFormItem label="完成时间" prop="completedAt">
               <ElDatePicker
                  v-model="form.completedAt"
                  :disabled="show"
                  type="datetime"
                  placeholder="请选择"
                  style="width: 100%" />
            </ElFormItem>
         </ElCol>
      </ElRow>
   </ElForm>
</template>

<script setup>
import { ElCol, ElDatePicker, ElForm, ElFormItem, ElInput, ElRow, ElSelect, ElOption } from 'element-plus';
import { computed, onMounted, reactive, ref, useTemplateRef } from 'vue';
import * as apis from '@/api/yydd/yydd.ts';
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
   applicationId: '',
   jobId: '',
   userId: '',
   merchantId: '',
   tradeMode: null,
   orderStatus: null,
   depositFrozenId: '',
   penaltyAmount: '',
   startTime: '',
   endTime: '',
   completedAt: ''
});

const formRef = useTemplateRef('formRef');

const baseRules = reactive({
   applicationId: [{ required: true, message: '请输入投递ID', trigger: 'change' }],
   jobId: [{ required: true, message: '请输入兼职ID', trigger: 'change' }],
   userId: [{ required: true, message: '请输入用户ID', trigger: 'change' }],
   merchantId: [{ required: true, message: '请输入商家ID', trigger: 'change' }],
   tradeMode: [{ required: true, message: '请选择交易模式', trigger: 'change' }],
   orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }]
});

const rules = computed(() => {
   return props.show ? {} : baseRules;
});

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
      const res = await apis.getDetail({ id: Number(id) });
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
         await updateFormDetaill();
         return Promise.resolve();
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
