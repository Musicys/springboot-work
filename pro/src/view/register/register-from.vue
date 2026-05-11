<template>
   <div class="register-container">
      <div class="register-form-wrapper">
         <div class="back-btn" @click="goBack">
            <i class="el-icon-arrow-left"></i>
            <span>返回登录</span>
         </div>
         <h2 class="register-title">商家注册</h2>
         <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px" class="register-form">
            <div class="form-row">
               <el-form-item label="用户名" prop="username" class="form-item">
                  <el-input v-model="formData.username" placeholder="请输入用户名" class="full-width-input" />
               </el-form-item>
               <el-form-item label="密码" prop="password" class="form-item">
                  <el-input
                     v-model="formData.password"
                     type="password"
                     placeholder="请输入密码"
                     class="full-width-input" />
               </el-form-item>
            </div>

            <div class="form-row">
               <el-form-item label="确认密码" prop="confirmPassword" class="form-item">
                  <el-input
                     v-model="formData.confirmPassword"
                     type="password"
                     placeholder="请确认密码"
                     class="full-width-input" />
               </el-form-item>
               <el-form-item label="联系电话" prop="contactPhone" class="form-item">
                  <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" class="full-width-input" />
               </el-form-item>
            </div>

            <div class="form-row">
               <el-form-item label="用户头像" prop="avatarUrl" class="form-item upload-item">
                  <el-upload
                     class="upload-component"
                     :http-request="handleAvatarUpload"
                     :file-list="avatarFileList"
                     :on-success="handleAvatarSuccess"
                     :on-error="handleUploadError"
                     :on-progress="handleAvatarProgress"
                     :on-remove="handleAvatarRemove"
                     :before-upload="beforeAvatarUpload"
                     :auto-upload="true"
                     :limit="1"
                     accept="image/*">
                     <template #default>
                        <div v-if="avatarFileList.length > 0" class="upload-preview">
                           <img :src="avatarFileList[0].url" class="preview-image" />
                           <div class="preview-overlay">
                              <i class="el-icon-delete" @click.stop="removeAvatar"></i>
                           </div>
                        </div>
                        <div v-else class="upload-placeholder">
                           <i class="el-icon-plus"></i>
                           <span>上传头像</span>
                        </div>
                     </template>
                  </el-upload>
                  <div v-if="avatarProgress > 0" class="progress-wrapper">
                     <el-progress :percentage="avatarProgress" :show-text="true" />
                  </div>
                  <div class="upload-hint">支持 jpg/jpeg/png，≤2MB</div>
               </el-form-item>

               <el-form-item label="营业执照" prop="licenseUrl" class="form-item upload-item">
                  <el-upload
                     class="upload-component"
                     :http-request="handleLicenseUpload"
                     :file-list="licenseFileList"
                     :on-success="handleLicenseSuccess"
                     :on-error="handleUploadError"
                     :on-progress="handleLicenseProgress"
                     :on-remove="handleLicenseRemove"
                     :before-upload="beforeLicenseUpload"
                     :auto-upload="true"
                     :limit="1"
                     accept="image/*">
                     <template #default>
                        <div v-if="licenseFileList.length > 0" class="upload-preview license-preview">
                           <img :src="licenseFileList[0].url" class="preview-image" />
                           <div class="preview-overlay">
                              <i class="el-icon-delete" @click.stop="removeLicense"></i>
                           </div>
                        </div>
                        <div v-else class="upload-placeholder">
                           <i class="el-icon-plus"></i>
                           <span>上传营业执照</span>
                        </div>
                     </template>
                  </el-upload>
                  <div v-if="licenseProgress > 0" class="progress-wrapper">
                     <el-progress :percentage="licenseProgress" :show-text="true" />
                  </div>
                  <div class="upload-hint">支持 jpg/jpeg/png，≤10MB</div>
               </el-form-item>
            </div>

            <div class="form-row">
               <el-form-item label="法人身份证正面" prop="legalIdCardFront" class="form-item upload-item">
                  <el-upload
                     class="upload-component"
                     :http-request="handleIdCardFrontUpload"
                     :file-list="idCardFrontFileList"
                     :on-success="handleIdCardFrontSuccess"
                     :on-error="handleUploadError"
                     :on-progress="handleIdCardFrontProgress"
                     :on-remove="handleIdCardFrontRemove"
                     :before-upload="beforeIdCardUpload"
                     :auto-upload="true"
                     :limit="1"
                     accept="image/*">
                     <template #default>
                        <div v-if="idCardFrontFileList.length > 0" class="upload-preview">
                           <img :src="idCardFrontFileList[0].url" class="preview-image" />
                           <div class="preview-overlay">
                              <i class="el-icon-delete" @click.stop="removeIdCardFront"></i>
                           </div>
                        </div>
                        <div v-else class="upload-placeholder">
                           <i class="el-icon-plus"></i>
                           <span>身份证正面</span>
                        </div>
                     </template>
                  </el-upload>
                  <div v-if="idCardFrontProgress > 0" class="progress-wrapper">
                     <el-progress :percentage="idCardFrontProgress" :show-text="true" />
                  </div>
                  <div class="upload-hint">支持 jpg/jpeg/png，≤5MB</div>
               </el-form-item>

               <el-form-item label="法人身份证反面" prop="legalIdCardBack" class="form-item upload-item">
                  <el-upload
                     class="upload-component"
                     :http-request="handleIdCardBackUpload"
                     :file-list="idCardBackFileList"
                     :on-success="handleIdCardBackSuccess"
                     :on-error="handleUploadError"
                     :on-progress="handleIdCardBackProgress"
                     :on-remove="handleIdCardBackRemove"
                     :before-upload="beforeIdCardUpload"
                     :auto-upload="true"
                     :limit="1"
                     accept="image/*">
                     <template #default>
                        <div v-if="idCardBackFileList.length > 0" class="upload-preview">
                           <img :src="idCardBackFileList[0].url" class="preview-image" />
                           <div class="preview-overlay">
                              <i class="el-icon-delete" @click.stop="removeIdCardBack"></i>
                           </div>
                        </div>
                        <div v-else class="upload-placeholder">
                           <i class="el-icon-plus"></i>
                           <span>身份证反面</span>
                        </div>
                     </template>
                  </el-upload>
                  <div v-if="idCardBackProgress > 0" class="progress-wrapper">
                     <el-progress :percentage="idCardBackProgress" :show-text="true" />
                  </div>
                  <div class="upload-hint">支持 jpg/jpeg/png，≤5MB</div>
               </el-form-item>
            </div>

            <div class="form-row">
               <el-form-item label="企业名称" prop="companyName" class="form-item">
                  <el-input v-model="formData.companyName" placeholder="请输入企业名称" class="full-width-input" />
               </el-form-item>
               <el-form-item label="注册资本" prop="registeredCapital" class="form-item">
                  <el-input
                     v-model="formData.registeredCapital"
                     type="number"
                     placeholder="请输入注册资本"
                     class="full-width-input" />
               </el-form-item>
            </div>

            <div class="form-row">
               <el-form-item label="商家位置" prop="location" class="form-item">
                  <el-input v-model="formData.location" placeholder="请输入商家位置" class="full-width-input" />
               </el-form-item>
               <el-form-item label="法人姓名" prop="legalPerson" class="form-item">
                  <el-input v-model="formData.legalPerson" placeholder="请输入法人姓名" class="full-width-input" />
               </el-form-item>
            </div>

            <el-form-item label="公司地址" prop="companyAddress">
               <el-input v-model="formData.companyAddress" placeholder="请输入公司地址" class="full-width-input" />
            </el-form-item>

            <el-form-item label="公司简介" prop="companyIntro">
               <el-input
                  v-model="formData.companyIntro"
                  type="textarea"
                  placeholder="请输入公司简介"
                  :rows="3"
                  class="full-width-input" />
            </el-form-item>

            <el-form-item>
               <el-button type="primary" @click="submitForm" :loading="loading" class="submit-btn">提交注册</el-button>
               <el-button @click="resetForm" class="reset-btn">重置</el-button>
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
import { ImgUpload } from '@/api/imgs';

const router = useRouter();
const formRef = ref();
const loading = ref(false);

// 当前上传的文件类型
const currentUploadType = ref('');

const goBack = () => {
   router.push('/login');
};

const formData = reactive({
   username: '',
   password: '',
   confirmPassword: '',
   avatarUrl: '',
   companyName: '',
   contactPhone: '',
   location: '',
   legalPerson: '',
   legalIdCardFront: '',
   legalIdCardBack: '',
   licenseUrl: '',
   registeredCapital: '',
   companyAddress: '',
   companyIntro: ''
});

const avatarFileList = ref([]);
const idCardFrontFileList = ref([]);
const idCardBackFileList = ref([]);
const licenseFileList = ref([]);

const avatarProgress = ref(0);
const idCardFrontProgress = ref(0);
const idCardBackProgress = ref(0);
const licenseProgress = ref(0);

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
   legalIdCardFront: [{ required: true, message: '请上传法人身份证正面', trigger: 'change' }],
   legalIdCardBack: [{ required: true, message: '请上传法人身份证反面', trigger: 'change' }],
   licenseUrl: [{ required: true, message: '请上传营业执照', trigger: 'change' }]
};

const handleAvatarUpload = async (options: any) => {
   const uploadFormData = new FormData();
   uploadFormData.append('file', options.file);
   try {
      avatarProgress.value = 30;
      const result = await ImgUpload(uploadFormData);
      if (result.code === 0) {
         formData.avatarUrl = result.data.url;
         avatarFileList.value = [{ url: result.data.url }];
         avatarProgress.value = 0;
         ElMessage.success('头像上传成功');
         options.onSuccess(result.data, options.file);
      } else {
         ElMessage.error(result.message || '头像上传失败');
         avatarProgress.value = 0;
         options.onError(new Error(result.message));
      }
   } catch (error) {
      ElMessage.error('头像上传失败，请重试');
      avatarProgress.value = 0;
      options.onError(error);
   }
};

const handleAvatarSuccess = (response: any) => {
   // 空实现，实际逻辑在handleAvatarUpload中
};

const handleIdCardFrontUpload = async (options: any) => {
   const uploadFormData = new FormData();
   uploadFormData.append('file', options.file);
   try {
      idCardFrontProgress.value = 30;
      const result = await ImgUpload(uploadFormData);
      if (result.code === 0) {
         formData.legalIdCardFront = result.data.url;
         idCardFrontFileList.value = [{ url: result.data.url }];
         idCardFrontProgress.value = 0;
         ElMessage.success('身份证正面上传成功');
         options.onSuccess(result.data, options.file);
      } else {
         ElMessage.error(result.message || '身份证正面上传失败');
         idCardFrontProgress.value = 0;
         options.onError(new Error(result.message));
      }
   } catch (error) {
      ElMessage.error('身份证正面上传失败，请重试');
      idCardFrontProgress.value = 0;
      options.onError(error);
   }
};

const handleIdCardFrontSuccess = (response: any) => {
   // 空实现，实际逻辑在handleIdCardFrontUpload中
};

const handleIdCardBackUpload = async (options: any) => {
   const uploadFormData = new FormData();
   uploadFormData.append('file', options.file);
   try {
      idCardBackProgress.value = 30;
      const result = await ImgUpload(uploadFormData);
      if (result.code === 0) {
         formData.legalIdCardBack = result.data.url;
         idCardBackFileList.value = [{ url: result.data.url }];
         idCardBackProgress.value = 0;
         ElMessage.success('身份证反面上传成功');
         options.onSuccess(result.data, options.file);
      } else {
         ElMessage.error(result.message || '身份证反面上传失败');
         idCardBackProgress.value = 0;
         options.onError(new Error(result.message));
      }
   } catch (error) {
      ElMessage.error('身份证反面上传失败，请重试');
      idCardBackProgress.value = 0;
      options.onError(error);
   }
};

const handleIdCardBackSuccess = (response: any) => {
   // 空实现，实际逻辑在handleIdCardBackUpload中
};

const handleLicenseUpload = async (options: any) => {
   const uploadFormData = new FormData();
   uploadFormData.append('file', options.file);
   try {
      licenseProgress.value = 30;
      const result = await ImgUpload(uploadFormData);
      if (result.code === 0) {
         formData.licenseUrl = result.data.url;
         licenseFileList.value = [{ url: result.data.url }];
         licenseProgress.value = 0;
         ElMessage.success('营业执照上传成功');
         options.onSuccess(result.data, options.file);
      } else {
         ElMessage.error(result.message || '营业执照上传失败');
         licenseProgress.value = 0;
         options.onError(new Error(result.message));
      }
   } catch (error) {
      ElMessage.error('营业执照上传失败，请重试');
      licenseProgress.value = 0;
      options.onError(error);
   }
};

const handleLicenseSuccess = (response: any) => {
   // 空实现，实际逻辑在handleLicenseUpload中
};

const handleUploadError = (error: any) => {
   ElMessage.error('上传失败，请重试');
   avatarProgress.value = 0;
   idCardFrontProgress.value = 0;
   idCardBackProgress.value = 0;
   licenseProgress.value = 0;
};

const handleAvatarProgress = (event: any) => {
   avatarProgress.value = Math.round((event.loaded / event.total) * 100);
};

const handleIdCardFrontProgress = (event: any) => {
   idCardFrontProgress.value = Math.round((event.loaded / event.total) * 100);
};

const handleIdCardBackProgress = (event: any) => {
   idCardBackProgress.value = Math.round((event.loaded / event.total) * 100);
};

const handleLicenseProgress = (event: any) => {
   licenseProgress.value = Math.round((event.loaded / event.total) * 100);
};

const handleAvatarRemove = () => {
   formData.avatarUrl = '';
   avatarFileList.value = [];
};

const handleIdCardFrontRemove = () => {
   formData.legalIdCardFront = '';
   idCardFrontFileList.value = [];
};

const handleIdCardBackRemove = () => {
   formData.legalIdCardBack = '';
   idCardBackFileList.value = [];
};

const handleLicenseRemove = () => {
   formData.licenseUrl = '';
   licenseFileList.value = [];
};

const removeAvatar = () => {
   ElMessageBox.confirm('确定要删除头像吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   })
      .then(() => {
         handleAvatarRemove();
         ElMessage.success('头像已删除');
      })
      .catch(() => {
         ElMessage.info('已取消删除');
      });
};

const removeIdCardFront = () => {
   ElMessageBox.confirm('确定要删除身份证正面吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   })
      .then(() => {
         handleIdCardFrontRemove();
         ElMessage.success('身份证正面已删除');
      })
      .catch(() => {
         ElMessage.info('已取消删除');
      });
};

const removeIdCardBack = () => {
   ElMessageBox.confirm('确定要删除身份证反面吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   })
      .then(() => {
         handleIdCardBackRemove();
         ElMessage.success('身份证反面已删除');
      })
      .catch(() => {
         ElMessage.info('已取消删除');
      });
};

const removeLicense = () => {
   ElMessageBox.confirm('确定要删除营业执照吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   })
      .then(() => {
         handleLicenseRemove();
         ElMessage.success('营业执照已删除');
      })
      .catch(() => {
         ElMessage.info('已取消删除');
      });
};

const beforeAvatarUpload = (file: File) => {
   const isImage = file.type.indexOf('image') !== -1;
   const isLt2M = file.size / 1024 / 1024 < 2;

   if (!isImage) {
      ElMessage.error('请上传图片格式');
   }
   if (!isLt2M) {
      ElMessage.error('图片大小不能超过 2MB');
   }
   return isImage && isLt2M;
};

const beforeIdCardUpload = (file: File) => {
   const isImage = file.type.indexOf('image') !== -1;
   const isLt5M = file.size / 1024 / 1024 < 5;

   if (!isImage) {
      ElMessage.error('请上传图片格式');
   }
   if (!isLt5M) {
      ElMessage.error('图片大小不能超过 5MB');
   }
   return isImage && isLt5M;
};

const beforeLicenseUpload = (file: File) => {
   const isImage = file.type.indexOf('image') !== -1;
   const isLt10M = file.size / 1024 / 1024 < 10;

   if (!isImage) {
      ElMessage.error('请上传图片格式');
   }
   if (!isLt10M) {
      ElMessage.error('图片大小不能超过 10MB');
   }
   return isImage && isLt10M;
};

const submitForm = async () => {
   if (!formRef.value) return;

   try {
      await formRef.value.validate();
      loading.value = true;

      const registerData = {
         username: formData.username,
         password: formData.password,
         companyName: formData.companyName,
         contactPhone: formData.contactPhone,
         location: formData.location,
         legalPerson: formData.legalPerson,
         legalIdCard: JSON.stringify([formData.legalIdCardFront, formData.legalIdCardBack]),
         companyImages: '',
         registeredCapital: formData.registeredCapital,
         companyAddress: formData.companyAddress,
         companyIntro: formData.companyIntro,
         avatarUrl: formData.avatarUrl,
         licenseUrl: formData.licenseUrl
      };

      const res = await httpPost('/pro/register', registerData);

      if (res.code === 0) {
         await ElMessageBox.alert(
            '注册提交成功！我们将在3个工作日内完成审核，请耐心等待。审核结果将通过短信通知您。',
            '注册成功',
            {
               confirmButtonText: '确定',
               type: 'success'
            }
         );

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
   formData.avatarUrl = '';
   formData.legalIdCardFront = '';
   formData.legalIdCardBack = '';
   formData.licenseUrl = '';
   avatarFileList.value = [];
   idCardFrontFileList.value = [];
   idCardBackFileList.value = [];
   licenseFileList.value = [];
};
</script>

<style scoped>
.register-container {
   display: flex;
   justify-content: center;
   align-items: center;
   min-height: 100vh;
   background-color: #f5f7fa;
   padding: 20px;
}

.register-form-wrapper {
   position: relative;
   width: 100%;
   max-width: 900px;
   padding: 40px;
   background-color: #fff;
   border-radius: 8px;
   box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.back-btn {
   position: absolute;
   top: 20px;
   left: 20px;
   display: flex;
   align-items: center;
   gap: 6px;
   padding: 8px 12px;
   color: #606266;
   font-size: 14px;
   cursor: pointer;
   transition: all 0.2s ease;
   border-radius: 4px;
}

.back-btn:hover {
   color: #409eff;
   background-color: #f0f5ff;
}

.back-btn i {
   font-size: 16px;
}

.register-title {
   text-align: center;
   margin-bottom: 30px;
   color: #303133;
   font-size: 22px;
   font-weight: 600;
}

.register-form {
   margin-top: 10px;
}

.form-row {
   display: flex;
   gap: 24px;
   margin-bottom: 16px;
}

.form-item {
   flex: 1;
   margin-bottom: 0 !important;
}

.form-item:last-child {
   margin-right: 0;
}

.full-width-input {
   width: 100%;
}

.el-form-item {
   margin-bottom: 16px;
}

.el-form-item__label {
   font-weight: 500;
   color: #606266;
   font-size: 14px;
}

:deep(.register-form .el-input__wrapper) {
   border-radius: 6px;
   transition: all 0.2s ease;
   width: 100%;
}

:deep(.register-form .el-input__wrapper:hover) {
   border-color: #c0c4cc;
}

:deep(.register-form .el-input__wrapper.is-focus) {
   border-color: #409eff;
   box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

:deep(.register-form .el-textarea__inner) {
   border-radius: 6px;
   transition: all 0.2s ease;
   width: 100%;
}

:deep(.register-form .el-textarea__inner:hover) {
   border-color: #c0c4cc;
}

:deep(.register-form .el-textarea__inner:focus) {
   border-color: #409eff;
   box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.submit-btn {
   width: calc(50% - 8px);
   height: 42px;
   font-size: 15px;
   border-radius: 6px;
   margin-right: 16px;
   transition: all 0.2s ease;
}

.submit-btn:hover {
   transform: translateY(-1px);
   box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.reset-btn {
   width: calc(50% - 8px);
   height: 42px;
   font-size: 15px;
   border-radius: 6px;
   transition: all 0.2s ease;
}

.reset-btn:hover {
   background-color: #f5f7fa;
}

.upload-component {
   display: block;
   width: 100%;
}

:deep(.upload-component .el-upload) {
   display: block;
   width: 100%;
}

.upload-placeholder {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   width: 100%;
   min-height: 140px;
   height: 140px;
   border: 1px dashed #d9d9d9;
   border-radius: 6px;
   cursor: pointer;
   transition: all 0.2s ease;
   background-color: #fafafa;
   box-sizing: border-box;
}

.upload-placeholder:hover {
   border-color: #409eff;
   background-color: #f0f5ff;
}

.upload-placeholder i {
   font-size: 28px;
   color: #8c939d;
   margin-bottom: 6px;
}

.upload-placeholder span {
   font-size: 13px;
   color: #8c939d;
}

.upload-preview {
   position: relative;
   width: 100%;
   height: 140px;
   border-radius: 6px;
   overflow: hidden;
   border: 1px solid #e8e8e8;
}

.preview-image {
   width: 100%;
   height: 100%;
   object-fit: cover;
}

.preview-overlay {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background-color: rgba(0, 0, 0, 0);
   display: flex;
   align-items: center;
   justify-content: center;
   opacity: 0;
   transition: all 0.2s ease;
}

.upload-preview:hover .preview-overlay {
   background-color: rgba(0, 0, 0, 0.5);
   opacity: 1;
}

.preview-overlay i {
   font-size: 28px;
   color: #fff;
   cursor: pointer;
   transition: color 0.2s ease;
}

.preview-overlay i:hover {
   color: #ff4d4f;
}

.license-preview {
   height: 140px;
}

.license-preview .preview-image {
   object-fit: contain;
}

.upload-hint {
   margin-top: 6px;
   font-size: 12px;
   color: #909399;
}

.progress-wrapper {
   margin-top: 8px;
   width: 100%;
}

.el-progress {
   height: 6px;
}

.el-progress-bar__outer {
   border-radius: 3px;
}

.el-progress-bar__inner {
   border-radius: 3px;
}

@media screen and (max-width: 768px) {
   .form-row {
      flex-direction: column;
      gap: 16px;
   }

   .form-item {
      width: 100%;
   }

   .register-form-wrapper {
      padding: 24px;
   }

   .register-title {
      font-size: 18px;
      margin-bottom: 20px;
   }

   .submit-btn,
   .reset-btn {
      width: 100%;
      margin-right: 0;
      margin-bottom: 10px;
   }
}
</style>
