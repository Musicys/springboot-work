<template>
   <DefaultContainer>
      <div class="title">商家信息</div>
      <div class="profile-container">
         <el-form ref="formRef" :model="form" label-width="140" label-position="left" class="profile-form">
            <el-form-item label="企业名称：" prop="companyName">
               <el-input v-model="form.companyName" placeholder="请输入企业名称" clearable />
            </el-form-item>
            <el-form-item label="营业执照URL：" prop="licenseUrl">
               <el-input v-model="form.licenseUrl" placeholder="请输入营业执照URL" clearable />
            </el-form-item>
            <el-form-item label="联系电话：" prop="contactPhone">
               <el-input v-model="form.contactPhone" placeholder="请输入联系电话" clearable />
            </el-form-item>
            <el-form-item label="商家位置：" prop="location">
               <el-input v-model="form.location" placeholder="请输入商家位置" clearable />
            </el-form-item>
            <el-form-item label="注册成本：" prop="registeredCapital">
               <el-input-number
                  v-model="form.registeredCapital"
                  :precision="4"
                  :step="10000"
                  :min="0"
                  placeholder="请输入注册成本" />
            </el-form-item>
            <el-form-item label="公司地址：" prop="companyAddress">
               <el-input v-model="form.companyAddress" placeholder="请输入公司地址" clearable />
            </el-form-item>
            <el-form-item label="公司简介：" prop="companyIntro">
               <el-input v-model="form.companyIntro" type="textarea" :rows="4" placeholder="请输入公司简介" clearable />
            </el-form-item>

            <el-divider content-position="left">认证信息（不可修改）</el-divider>

            <el-form-item label="法人姓名：">
               <el-input :model-value="form.legalPerson" disabled />
            </el-form-item>
            <el-form-item label="法人身份证号：">
               <el-input :model-value="form.legalIdCard" disabled />
            </el-form-item>
            <el-form-item label="账号类型：">
               <el-tag :type="form.isSubAccount === 0 ? 'success' : 'warning'">
                  {{ form.isSubAccount === 0 ? '主账号' : '子账号' }}
               </el-tag>
            </el-form-item>
            <el-form-item label="创建时间：">
               <el-input :model-value="form.createdAt" disabled />
            </el-form-item>

            <el-form-item class="form-actions">
               <el-button type="primary" :loading="loading" @click="handleUpdate">保存修改</el-button>
               <el-button @click="handleReset">重置</el-button>
            </el-form-item>
         </el-form>
      </div>
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElDivider, ElForm, ElFormItem, ElInput, ElInputNumber, ElMessage, ElTag } from 'element-plus';
import { onMounted, ref } from 'vue';
import { getMerchantProfile, updateMerchantProfile } from '@/api/login';
import DefaultContainer from '@/components/DefaultContainer/index.vue';

const loading = ref(false);
const formRef = ref(null);

const form = ref({
   userId: null,
   companyName: '',
   licenseUrl: '',
   contactPhone: '',
   location: '',
   legalPerson: '',
   legalIdCard: '',
   isSubAccount: 0,
   companyImages: '',
   registeredCapital: null,
   companyAddress: '',
   companyIntro: '',
   createdAt: ''
});

const originalForm = ref({});

const loadProfile = async () => {
   try {
      loading.value = true;
      const res = await getMerchantProfile();
      if (res.code === 0 && res.data) {
         form.value = { ...res.data };
         originalForm.value = { ...res.data };
      }
   } catch (error) {
      ElMessage.error('获取商家信息失败');
   } finally {
      loading.value = false;
   }
};

const handleUpdate = async () => {
   try {
      loading.value = true;
      const res = await updateMerchantProfile(form.value);
      if (res.code === 0) {
         ElMessage.success('修改成功');
         loadProfile();
      }
   } catch (error) {
      ElMessage.error('修改失败');
   } finally {
      loading.value = false;
   }
};

const handleReset = () => {
   form.value = { ...originalForm.value };
};

onMounted(() => {
   loadProfile();
});
</script>

<style lang="scss" scoped>
.title {
   font-size: 20px;
   font-weight: 600;
   margin-bottom: 20px;
   color: #303133;
}

.profile-container {
   background: #fff;
   padding: 24px;
   border-radius: 8px;
}

.profile-form {
   max-width: 800px;

   :deep(.el-input.is-disabled .el-input__wrapper) {
      background-color: #f5f7fa;
      cursor: not-allowed;
   }

   :deep(.el-tag) {
      font-size: 14px;
   }
}

.form-actions {
   margin-top: 24px;

   :deep(.el-form-item__content) {
      justify-content: flex-start;
   }
}
</style>
