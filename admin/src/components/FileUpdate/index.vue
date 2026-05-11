<template>
   <el-dialog v-model="dialogVisible" title="图片管理" width="900px" height="600px" @close="handleClose">
      <div class="file-update-container">
         <div class="header">
            <div class="search-upload">
               <el-input
                  v-model="searchForm.title"
                  placeholder="请输入图片标题"
                  class="search-input"
                  @keyup.enter="handleSearch">
                  <template #append>
                     <el-button @click="handleSearch" icon="Search" type="primary" />
                  </template>
               </el-input>
               <div class="button-group">
                  <el-upload
                     class="upload-btn"
                     :before-upload="beforeUpload"
                     :on-success="handleUploadSuccess"
                     :on-error="handleUploadError"
                     :show-file-list="false"
                     action=""
                     :http-request="customUpload">
                     <el-button type="success" icon="Plus">添加</el-button>
                  </el-upload>
                  <el-button
                     type="danger"
                     icon="Delete"
                     :disabled="selectedImages.length === 0"
                     @click="handleBatchDelete"
                     >删除</el-button
                  >
                  <el-button
                     type="primary"
                     icon="Check"
                     :disabled="selectedImages.length !== 1"
                     @click="handleConfirmSelect"
                     >选中</el-button
                  >
               </div>
            </div>
         </div>

         <div class="main-content">
            <div class="image-content">
               <div class="image-list">
                  <div
                     v-for="(image, index) in imageList"
                     :key="image.id || index"
                     class="image-item"
                     :class="{ selected: selectedImages.includes(image.delname) }"
                     @click="handleImageClick(image)">
                     <div class="image-wrapper">
                        <img :src="image.url" :alt="image.title" />
                        <div class="image-checkbox" v-if="selectedImages.includes(image.delname)">
                           <el-icon><Check /></el-icon>
                        </div>
                     </div>
                     <div class="image-info">
                        <p class="image-title">{{ image.title }}</p>
                        <p class="image-size">{{ formatSize(image.size) }}</p>
                     </div>
                  </div>
                  <div v-if="imageList.length === 0" class="empty-state">
                     <el-empty description="暂无图片" />
                  </div>
               </div>

               <div class="pagination">
                  <el-pagination
                     v-model:current-page="pagination.current"
                     v-model:page-size="pagination.pageSize"
                     :page-sizes="[10, 20, 50, 100]"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="pagination.total"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange" />
               </div>
            </div>
         </div>
      </div>
   </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { ElMessage, ElDialog, ElEmpty } from 'element-plus';
import { Check } from '@element-plus/icons-vue';
import { ImgsList, ImgDel, ImgUpload } from '@/api/imgs';
import type { Img, ListImgRequest } from '@/api/imgs';

const props = defineProps<{
   visible?: boolean;
}>();

const emit = defineEmits<{
   'update:visible': (value: boolean) => void;
   select: (image: Img) => void;
   close: () => void;
}>();

const dialogVisible = ref(false);

const searchForm = ref<ListImgRequest>({
   current: 1,
   pageSize: 10,
   title: ''
});

const imageList = ref<Img[]>([]);
const selectedImages = ref<string[]>([]);

const pagination = ref({
   current: 1,
   pageSize: 10,
   total: 0
});

watch(
   () => props.visible,
   newVal => {
      dialogVisible.value = newVal || false;
   }
);

watch(dialogVisible, newVal => {
   emit('update:visible', newVal);
});

const loadImageList = async () => {
   try {
      const response = await ImgsList({
         current: pagination.value.current,
         pageSize: pagination.value.pageSize,
         title: searchForm.value.title
      });

      if (response.code === 0 && response.data) {
         imageList.value = response.data.records || [];
         pagination.value.total = response.data.total || 0;
      }
   } catch (error) {
      ElMessage.error('获取图片列表失败');
      console.error('获取图片列表失败:', error);
   }
};

const handleSearch = () => {
   pagination.value.current = 1;
   loadImageList();
};

const handleSizeChange = (size: number) => {
   pagination.value.pageSize = size;
   loadImageList();
};

const handleCurrentChange = (current: number) => {
   pagination.value.current = current;
   loadImageList();
};

const customUpload = async (options: any) => {
   const { file } = options;
   const formData = new FormData();
   formData.append('file', file);
   try {
      const response = await ImgUpload(formData);
      if (response.code === 0) {
         options.onSuccess(response);
         ElMessage.success('上传成功');
         loadImageList();
      } else {
         options.onError(response);
      }
   } catch (error) {
      options.onError(error);
   }
};

const beforeUpload = (file: File) => {
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

const handleUploadSuccess = () => {};

const handleUploadError = () => {
   ElMessage.error('上传失败');
};

const handleBatchDelete = async () => {
   if (selectedImages.value.length === 0) {
      ElMessage.warning('请先选择要删除的图片');
      return;
   }

   try {
      const response = await ImgDel(selectedImages.value);
      if (response.code === 0) {
         ElMessage.success('删除成功');
         loadImageList();
         selectedImages.value = [];
      }
   } catch (error) {
      ElMessage.error('删除失败');
      console.error('删除图片失败:', error);
   }
};

const handleImageClick = (image: Img) => {
   const index = selectedImages.value.indexOf(image.delname);
   if (index > -1) {
      selectedImages.value.splice(index, 1);
   } else {
      selectedImages.value.push(image.delname);
   }
};

const handleConfirmSelect = () => {
   if (selectedImages.value.length !== 1) {
      ElMessage.warning('请选择一张图片');
      return;
   }

   const selectedImage = imageList.value.find(img => img.delname === selectedImages.value[0]);
   if (selectedImage) {
      emit('select', selectedImage);
      dialogVisible.value = false;
      selectedImages.value = [];
   }
};

const handleClose = () => {
   selectedImages.value = [];
   emit('close');
};

const formatSize = (bytes: number) => {
   if (bytes === 0) return '0 Bytes';
   const k = 1024;
   const sizes = ['Bytes', 'KB', 'MB', 'GB'];
   const i = Math.floor(Math.log(bytes) / Math.log(k));
   return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

onMounted(() => {
   loadImageList();
});

watch(
   () => props.visible,
   newVal => {
      dialogVisible.value = newVal || false;
      if (newVal) {
         loadImageList();
      }
   }
);
</script>

<style lang="scss" scoped>
.file-update-container {
   height: 100%;
   display: flex;
   flex-direction: column;
   padding: 10px;
}

.header {
   margin-bottom: 16px;
}

.search-upload {
   display: flex;
   gap: 16px;
   align-items: center;
   justify-content: space-between;
   flex-wrap: wrap;
}

.search-input {
   width: 300px;
   flex-shrink: 0;
}

.button-group {
   display: flex;
   gap: 10px;
}

.main-content {
   flex: 1;
   overflow: hidden;
}

.image-content {
   height: 100%;
   display: flex;
   flex-direction: column;
   overflow: hidden;
}

.image-list {
   display: grid;
   grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
   gap: 16px;
   flex: 1;
   overflow-y: auto;
   padding: 10px;
   border: 1px solid #e4e7ed;
   border-radius: 4px;
   background: #fff;
}

.empty-state {
   grid-column: 1 / -1;
   display: flex;
   align-items: center;
   justify-content: center;
   height: 200px;
}

.image-item {
   border: 2px solid #e4e7ed;
   border-radius: 6px;
   padding: 10px;
   cursor: pointer;
   transition: all 0.3s;
   background: #fff;
}

.image-item:hover {
   border-color: #409eff;
   box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
   transform: translateY(-2px);
}

.image-item.selected {
   border-color: #409eff;
   background-color: #ecf5ff;
}

.image-wrapper {
   position: relative;
   width: 100%;
   height: 120px;
   margin-bottom: 10px;
   overflow: hidden;
   border-radius: 4px;
   background: #f5f7fa;
}

.image-wrapper img {
   width: 100%;
   height: 100%;
   object-fit: cover;
   transition: transform 0.3s;
}

.image-item:hover img {
   transform: scale(1.05);
}

.image-checkbox {
   position: absolute;
   top: 8px;
   right: 8px;
   width: 20px;
   height: 20px;
   background: rgba(255, 255, 255, 0.9);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   color: #409eff;
   font-size: 14px;
}

.image-info {
   text-align: center;
}

.image-title {
   font-size: 14px;
   margin-bottom: 5px;
   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
   font-weight: 500;
}

.image-size {
   font-size: 12px;
   color: #909399;
}

.pagination {
   display: flex;
   justify-content: flex-end;
   margin-top: 16px;
   padding: 10px 0;
   border-top: 1px solid #e4e7ed;
}

.image-list::-webkit-scrollbar {
   width: 8px;
   height: 8px;
}

.image-list::-webkit-scrollbar-track {
   background: #f1f1f1;
   border-radius: 4px;
}

.image-list::-webkit-scrollbar-thumb {
   background: #c0c4cc;
   border-radius: 4px;
}

.image-list::-webkit-scrollbar-thumb:hover {
   background: #909399;
}

@media (max-width: 768px) {
   .search-upload {
      flex-direction: column;
      align-items: stretch;
   }

   .search-input {
      width: 100%;
   }

   .button-group {
      justify-content: center;
   }

   .image-list {
      grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
   }
}
</style>
