<template>
   <div class="register-container">
      <div class="register-form-wrapper">
         <h2 class="register-title">商家注册</h2>
         <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px" class="register-form">
            <el-form-item label="用户名" prop="username">
               <el-input v-model="formData.username" placeholder="请输入用户名" />
            </el-form-item>

            <el-form-item label="密码" prop="password">
               <el-input v-model="formData.password" type="password" placeholder="请输入密码" />
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
               <el-input v-model="formData.confirmPassword" type="password" placeholder="请确认密码" />
            </el-form-item>

            <el-form-item label="企业名称" prop="companyName">
               <el-input v-model="formData.companyName" placeholder="请输入企业名称" />
            </el-form-item>

            <el-form-item label="联系电话" prop="contactPhone">
               <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>

            <el-form-item label="商家位置" prop="location">
               <el-input v-model="formData.location" placeholder="请输入商家位置" />
            </el-form-item>

            <el-form-item label="法人姓名" prop="legalPerson">
               <el-input v-model="formData.legalPerson" placeholder="请输入法人姓名" />
            </el-form-item>

            <el-form-item label="法人身份证号" prop="legalIdCard">
               <el-input v-model="formData.legalIdCard" placeholder="请输入法人身份证号" />
            </el-form-item>

            <el-form-item label="公司地址" prop="companyAddress">
               <el-input v-model="formData.companyAddress" placeholder="请输入公司地址" />
            </el-form-item>

            <el-form-item label="公司简介" prop="companyIntro">
               <el-input v-model="formData.companyIntro" type="textarea" placeholder="请输入公司简介" :rows="3" />
            </el-form-item>

            <el-form-item>
               <el-button type="primary" @click="submitForm" :loading="loading">提交注册</el-button>
               <el-button @click="resetForm">重置</el-button>
            </el-form-item>
         </el-form>
      </div>
   </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
import { httpPost } from '@/util/http';

const router = useRouter();
const formRef = ref();
const loading = ref(false);

const formData = reactive({
   username: '',
   password: '',
   confirmPassword: '',
   companyName: '',
   contactPhone: '',
   location: '',
   legalPerson: '',
   legalIdCard: '',
   companyAddress: '',
   companyIntro: ''
});

const rules = {
   username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
   ],
   password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
   ],
   confirmPassword: [
      { required: true, message: '请确认密码', trigger: 'blur' },
      {
         validator: (rule, value, callback) => {
            if (value !== formData.password) {
               callback(new Error('两次输入密码不一致'));
            } else {
               callback();
            }
         },
         trigger: 'blur'
      }
   ],
   companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
   contactPhone: [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
   ],
   legalPerson: [{ required: true, message: '请输入法人姓名', trigger: 'blur' }],
   legalIdCard: [{ required: true, message: '请输入法人身份证号', trigger: 'blur' }]
};

const submitForm = async () => {
   if (!formRef.value) return;

   try {
      await formRef.value.validate();
      loading.value = true;

      // 准备注册数据
      const registerData = {
         username: formData.username,
         password: formData.password,
         companyName: formData.companyName,
         contactPhone: formData.contactPhone,
         location: formData.location,
         legalPerson: formData.legalPerson,
         legalIdCard: formData.legalIdCard,
         companyAddress: formData.companyAddress,
         companyIntro: formData.companyIntro
      };

      // 调用注册接口
      const res = await httpPost('/pro/register', registerData);

      if (res.code === 0) {
         // 注册成功，显示审核提示
         await ElMessageBox.alert(
            '注册提交成功！我们将在3个工作日内完成审核，请耐心等待。审核结果将通过短信通知您。',
            '注册成功',
            {
               confirmButtonText: '确定',
               type: 'success'
            }
         );

         // 跳转到登录页面
         router.push('/login');
      } else {
         ElMessage.error(res.message || '注册失败');
      }
   } catch (error: any) {
      if (error.message) {
         ElMessage.error(error.message);
      }
   } finally {
      loading.value = false;
   }
};

const resetForm = () => {
   if (!formRef.value) return;
   formRef.value.resetFields();
};
</script>

<style scoped>
.register-container {
   display: flex;
   justify-content: center;
   align-items: center;
   min-height: 100vh;
   background-color: #f5f7fa;
}

.register-form-wrapper {
   width: 100%;
   max-width: 600px;
   padding: 40px;
   background-color: #fff;
   border-radius: 8px;
   box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-title {
   text-align: center;
   margin-bottom: 30px;
   color: #303133;
}

.register-form {
   margin-top: 20px;
}

.el-form-item {
   margin-bottom: 20px;
}

.el-button {
   width: 100%;
   margin-bottom: 10px;
}
</style>
