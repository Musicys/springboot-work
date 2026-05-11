<template>
   <DefaultContainer>
      <div class="title">商家详情查看</div>
      <div class="content">
         <ElCard shadow="hover">
            <template #header>
               <div class="card-header">
                  <span>基本信息</span>
                  <el-button type="primary" size="small" @click="handleEdit">编辑</el-button>
               </div>
            </template>
            <el-form label-width="120" label-position="left">
               <el-row :gutter="20">
                  <el-col :span="12">
                     <el-form-item label="商家ID：">{{ merchantInfo.userId }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="企业名称：">{{ merchantInfo.companyName }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="联系电话：">{{ merchantInfo.contactPhone }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="商家位置：">{{ merchantInfo.location }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="法人姓名：">{{ merchantInfo.legalPerson }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="法人身份证：">{{ merchantInfo.legalIdCard }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="注册成本：">{{ merchantInfo.registeredCapital }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="账号类型：">
                        <el-tag :type="merchantInfo.isSubAccount === 0 ? 'success' : 'warning'">
                           {{ merchantInfo.isSubAccount === 0 ? '主账号' : '子账号' }}
                        </el-tag>
                     </el-form-item>
                  </el-col>
                  <el-col :span="24">
                     <el-form-item label="公司地址：">{{ merchantInfo.companyAddress }}</el-form-item>
                  </el-col>
                  <el-col :span="24">
                     <el-form-item label="公司简介：">{{ merchantInfo.companyIntro }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="创建时间：">{{ merchantInfo.createdAt }}</el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="可用余额：">
                        <span class="balance-value">¥ {{ formatMoney(merchantInfo.balance) }}</span>
                     </el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="冻结金额：">
                        <span class="frozen-value">¥ {{ formatMoney(merchantInfo.frozenBalance) }}</span>
                     </el-form-item>
                  </el-col>
               </el-row>
            </el-form>
         </ElCard>
         <ElCard shadow="hover" style="margin-top: 20px">
            <template #header>
               <div class="card-header">
                  <span>公司图片</span>
                  <el-button type="primary" size="small" @click="handleEditImages">编辑图片</el-button>
               </div>
            </template>
            <div class="image-list" v-if="companyImages.length > 0">
               <div v-for="(image, index) in companyImages" :key="index" class="image-item">
                  <ElImage
                     :src="image"
                     fit="cover"
                     style="width: 200px; height: 150px"
                     :preview-src-list="companyImages"
                     preview-teleported />
               </div>
            </div>
            <el-empty v-else description="暂无公司图片" />
         </ElCard>
      </div>
      <div class="footer">
         <ElButton @click="router.back()"> 返回 </ElButton>
      </div>

      <el-dialog v-model="dialogVisible" title="编辑商家信息" width="600px" @close="handleDialogClose">
         <el-form ref="editFormRef" :model="editForm" label-width="120" label-position="left">
            <el-form-item label="企业名称：" prop="companyName">
               <el-input v-model="editForm.companyName" placeholder="请输入企业名称" />
            </el-form-item>
            <el-form-item label="营业执照URL：" prop="licenseUrl">
               <el-input v-model="editForm.licenseUrl" placeholder="请输入营业执照URL" />
            </el-form-item>
            <el-form-item label="联系电话：" prop="contactPhone">
               <el-input v-model="editForm.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="商家位置：" prop="location">
               <el-input v-model="editForm.location" placeholder="请输入商家位置" />
            </el-form-item>
            <el-form-item label="注册成本：" prop="registeredCapital">
               <el-input-number
                  v-model="editForm.registeredCapital"
                  :precision="4"
                  :step="10000"
                  :min="0"
                  style="width: 100%" />
            </el-form-item>
            <el-form-item label="公司地址：" prop="companyAddress">
               <el-input v-model="editForm.companyAddress" placeholder="请输入公司地址" />
            </el-form-item>
            <el-form-item label="公司简介：" prop="companyIntro">
               <el-input v-model="editForm.companyIntro" type="textarea" :rows="3" placeholder="请输入公司简介" />
            </el-form-item>
         </el-form>
         <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
         </template>
      </el-dialog>

      <el-dialog v-model="imageDialogVisible" title="编辑公司图片" width="800px" @close="handleImageDialogClose">
         <div class="image-edit-container">
            <div class="image-upload-area">
               <el-upload
                  class="upload-btn"
                  :before-upload="beforeUpload"
                  :show-file-list="false"
                  action=""
                  :http-request="customUpload">
                  <el-button type="success" icon="Plus">上传图片</el-button>
               </el-upload>
               <span class="upload-tip">支持 JPG、PNG 格式，最大 10MB</span>
            </div>
            <div class="image-edit-list" v-if="editImages.length > 0">
               <div v-for="(image, index) in editImages" :key="index" class="edit-image-item">
                  <img :src="image" alt="" />
                  <div class="edit-image-actions">
                     <el-button type="danger" size="small" icon="Delete" @click="handleRemoveImage(index)"
                        >删除</el-button
                     >
                  </div>
               </div>
            </div>
            <el-empty v-else description="暂无图片，请上传" />
         </div>
         <template #footer>
            <el-button @click="imageDialogVisible = false">取消</el-button>
            <el-button type="primary" :loading="imageSubmitLoading" @click="handleSaveImages">保存</el-button>
         </template>
      </el-dialog>
   </DefaultContainer>
</template>

<script setup>
import {
   ElButton,
   ElCard,
   ElCol,
   ElEmpty,
   ElForm,
   ElFormItem,
   ElImage,
   ElInput,
   ElInputNumber,
   ElMessage,
   ElRow,
   ElTag,
   ElUpload
} from 'element-plus';
import { Delete as IconDelete } from '@element-plus/icons-vue';
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getMerchantProfile, updateMerchantProfile } from '@/api/login';
import { ImgUpload } from '@/api/imgs';
import DefaultContainer from '@/components/DefaultContainer/index.vue';

const router = useRouter();

const merchantInfo = ref({});
const editForm = ref({});
const dialogVisible = ref(false);
const imageDialogVisible = ref(false);
const submitLoading = ref(false);
const imageSubmitLoading = ref(false);
const editFormRef = ref(null);
const editImages = ref([]);

const companyImages = computed(() => {
   if (!merchantInfo.value.companyImages) return [];
   try {
      const images = JSON.parse(merchantInfo.value.companyImages);
      return Array.isArray(images) ? images : [];
   } catch {
      return [];
   }
});

const loadMerchantInfo = async () => {
   try {
      const res = await getMerchantProfile();
      if (res.code === 0 && res.data) {
         merchantInfo.value = res.data;
      }
   } catch (error) {
      ElMessage.error('获取商家信息失败');
   }
};

const formatMoney = value => {
   if (!value || value === null || value === undefined) {
      return '0.00';
   }
   return Number(value).toFixed(2);
};

const handleEdit = () => {
   editForm.value = {
      companyName: merchantInfo.value.companyName,
      licenseUrl: merchantInfo.value.licenseUrl,
      contactPhone: merchantInfo.value.contactPhone,
      location: merchantInfo.value.location,
      registeredCapital: merchantInfo.value.registeredCapital,
      companyAddress: merchantInfo.value.companyAddress,
      companyIntro: merchantInfo.value.companyIntro
   };
   dialogVisible.value = true;
};

const handleDialogClose = () => {
   editForm.value = {};
   editFormRef.value?.resetFields();
};

const handleSubmit = async () => {
   try {
      submitLoading.value = true;
      const res = await updateMerchantProfile(editForm.value);
      if (res.code === 0) {
         ElMessage.success('修改成功');
         dialogVisible.value = false;
         await loadMerchantInfo();
      }
   } catch (error) {
      ElMessage.error('修改失败');
   } finally {
      submitLoading.value = false;
   }
};

const handleEditImages = () => {
   editImages.value = [...companyImages.value];
   imageDialogVisible.value = true;
};

const handleImageDialogClose = () => {
   editImages.value = [];
};

const beforeUpload = file => {
   const isImage = /^image\//.test(file.type);
   if (!isImage) {
      ElMessage.error('只能上传图片文件');
      return false;
   }
   const isLt10M = file.size / 1024 / 1024 < 10;
   if (!isLt10M) {
      ElMessage.error('图片大小不能超过 10MB');
      return false;
   }
   return true;
};

const customUpload = async options => {
   const { file } = options;
   const formData = new FormData();
   formData.append('file', file);
   try {
      const res = await ImgUpload(formData);
      if (res.code === 0) {
         editImages.value.push(res.data.url);
         ElMessage.success('上传成功');
      } else {
         ElMessage.error('上传失败');
      }
   } catch (error) {
      ElMessage.error('上传失败');
   }
};

const handleRemoveImage = index => {
   editImages.value.splice(index, 1);
};

const handleSaveImages = async () => {
   try {
      imageSubmitLoading.value = true;
      const profile = {
         companyName: merchantInfo.value.companyName,
         licenseUrl: merchantInfo.value.licenseUrl,
         contactPhone: merchantInfo.value.contactPhone,
         location: merchantInfo.value.location,
         registeredCapital: merchantInfo.value.registeredCapital,
         companyAddress: merchantInfo.value.companyAddress,
         companyIntro: merchantInfo.value.companyIntro,
         companyImages: JSON.stringify(editImages.value)
      };
      const res = await updateMerchantProfile(profile);
      if (res.code === 0) {
         ElMessage.success('保存成功');
         imageDialogVisible.value = false;
         await loadMerchantInfo();
      }
   } catch (error) {
      ElMessage.error('保存失败');
   } finally {
      imageSubmitLoading.value = false;
   }
};

onMounted(() => {
   loadMerchantInfo();
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

.content {
   padding: 20px 0;
}

.card-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
}

.image-list {
   display: flex;
   flex-wrap: wrap;
   gap: 10px;
}

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}

.balance-value {
   color: #67c23a;
   font-size: 18px;
   font-weight: 600;
}

.frozen-value {
   color: #e6a23c;
   font-size: 18px;
   font-weight: 600;
}

.image-edit-container {
   padding: 10px;
}

.image-upload-area {
   display: flex;
   align-items: center;
   gap: 16px;
   margin-bottom: 20px;
   padding-bottom: 20px;
   border-bottom: 1px solid #eee;
}

.upload-tip {
   color: #909399;
   font-size: 12px;
}

.image-edit-list {
   display: flex;
   flex-wrap: wrap;
   gap: 16px;
}

.edit-image-item {
   position: relative;
   width: 200px;

   img {
      width: 100%;
      height: 150px;
      object-fit: cover;
      border-radius: 4px;
      border: 1px solid #eee;
   }

   .edit-image-actions {
      display: flex;
      justify-content: center;
      margin-top: 8px;
   }
}
</style>
