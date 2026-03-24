<template>
   <view class="exituser-page">
      <!-- 顶部导航栏 -->
      <view class="nav-bar">
         <view class="nav-left" @click="handleBack">
            <wd-icon name="arrow-left" size="36rpx" color="#333"></wd-icon>
         </view>
         <view class="nav-center">
            <text class="nav-title">个人资料</text>
         </view>
         <view class="nav-right">
            <text class="save-btn" @click="handleSave">保存</text>
         </view>
      </view>

      <!-- 内容区域 -->
      <scroll-view scroll-y class="content">
         <!-- 头像上传区域 -->
         <view class="avatar-section">
            <view class="avatar-container">
               <image
                  :src="form.avatarUrl"
                  mode="aspectFill"
                  class="avatar"></image>
               <view class="avatar-upload-btn" @click="handleAvatarUpload">
                  <wd-icon name="camera" size="28rpx" color="#fff"></wd-icon>
               </view>
            </view>
            <text class="username">{{ form.username }}</text>
         </view>

         <!-- 个人相册区域 -->
         <view class="album-section">
            <view class="section-title">个人相册</view>
            <view class="album-grid">
               <!-- 已上传的照片 -->
               <view
                  v-for="(img, index) in albumImages"
                  :key="index"
                  class="album-item">
                  <image
                     :src="img"
                     mode="aspectFill"
                     class="album-image"></image>
                  <view class="delete-btn" @click="handleDeleteImage(index)">
                     <wd-icon name="close" size="20rpx" color="#fff"></wd-icon>
                  </view>
               </view>
               <!-- 添加照片按钮 -->
               <view class="album-item add-btn" @click="handleAddImage">
                  <wd-icon name="add" size="40rpx" color="#999"></wd-icon>
                  <text class="add-text">添加</text>
               </view>
            </view>
         </view>

         <!-- 基本信息区域 -->
         <view class="info-section">
            <view class="section-title">基本信息</view>

            <!-- 昵称 -->
            <view class="info-item" @click="handleEditInfo('username')">
               <text class="info-label">昵称</text>
               <view class="info-value">
                  <text class="info-text">{{ form.username || '未设置' }}</text>
                  <wd-icon
                     name="arrow-right"
                     size="28rpx"
                     color="#ccc"></wd-icon>
               </view>
            </view>

            <!-- 性别 -->
            <view class="info-item" @click="handleEditInfo('gender')">
               <text class="info-label">性别</text>
               <view class="info-value">
                  <text class="info-text">{{
                     form.gender === 1
                        ? '男'
                        : form.gender === 2
                          ? '女'
                          : '未设置'
                  }}</text>
                  <wd-icon
                     name="arrow-right"
                     size="28rpx"
                     color="#ccc"></wd-icon>
               </view>
            </view>

            <!-- 年龄 -->
            <view class="info-item" @click="handleEditInfo('age')">
               <text class="info-label">年龄</text>
               <view class="info-value">
                  <text class="info-text">{{ form.age || '未设置' }}岁</text>
                  <wd-icon
                     name="arrow-right"
                     size="28rpx"
                     color="#ccc"></wd-icon>
               </view>
            </view>

            <!-- 个人简介 -->
            <view class="info-item" @click="handleEditInfo('introductory')">
               <text class="info-label">交友宣言</text>
               <view class="info-value">
                  <text class="info-text">{{
                     form.introductory && form.introductory.length > 5
                        ? form.introductory.substring(0, 5) + '...'
                        : form.introductory || '未设置'
                  }}</text>
                  <wd-icon
                     name="arrow-right"
                     size="28rpx"
                     color="#ccc"></wd-icon>
               </view>
            </view>

            <!-- 用户标签 -->
            <view class="info-item" @click="handleEditInfo('tags')">
               <text class="info-label">用户标签</text>
               <view class="info-value">
                  <view class="tags">
                     <text
                        v-for="(tag, index) in tagList"
                        :key="index"
                        class="tag"
                        >{{ tag }}</text
                     >
                     <text v-if="tagList.length === 0" class="info-text"
                        >未设置</text
                     >
                  </view>
                  <wd-icon
                     name="arrow-right"
                     size="28rpx"
                     color="#ccc"></wd-icon>
               </view>
            </view>
         </view>
      </scroll-view>

      <!-- 自定义编辑弹窗 -->
      <view
         v-if="showEditDialog"
         class="custom-popup-overlay"
         @click="closeDialog">
         <view class="custom-popup" @click.stop>
            <!-- 弹窗头部 -->
            <view class="dialog-header">
               <text class="dialog-title">{{ editTitle }}</text>
               <view class="close-btn" @click="closeDialog">
                  <text class="close-icon">×</text>
               </view>
            </view>

            <!-- 弹窗内容 -->
            <view class="popup-content">
               <!-- 文本输入类型 -->
               <view v-if="editType === 'username' || editType === 'email'">
                  <input
                     v-model="editValue"
                     :placeholder="editPlaceholder"
                     type="text"
                     :maxlength="editType === 'introductory' ? 200 : 50"
                     class="custom-input"
                     :class="{ textarea: editType === 'introductory' }" />
                  <text v-if="editType === 'introductory'" class="word-limit"
                     >{{ editValue.length }}/200</text
                  >
               </view>
               <view v-else-if="editType === 'introductory'">
                  <textarea
                     v-model="editValue"
                     :placeholder="editPlaceholder"
                     type="text"
                     :maxlength="editType === 'introductory' ? 200 : 50"
                     class="custom-input"
                     :class="{ textarea: editType === 'introductory' }" />
                  <text v-if="editType === 'introductory'" class="word-limit"
                     >{{ editValue.length }}/200</text
                  >
               </view>
               <!-- 年龄输入 -->
               <view v-else-if="editType === 'age'">
                  <input
                     v-model="editValue"
                     placeholder="请输入年龄"
                     type="number"
                     maxlength="3"
                     class="custom-input" />
               </view>

               <!-- 性别选择 -->
               <view v-else-if="editType === 'gender'">
                  <view class="gender-options">
                     <view
                        class="gender-option"
                        :class="{ active: editValue === 1 }"
                        @click="editValue = 1">
                        <text>男</text>
                        <view v-if="editValue === 1" class="success-icon"
                           >✓</view
                        >
                     </view>
                     <view
                        class="gender-option"
                        :class="{ active: editValue === 2 }"
                        @click="editValue = 2">
                        <text>女</text>
                        <view v-if="editValue === 2" class="success-icon"
                           >✓</view
                        >
                     </view>
                  </view>
               </view>

               <!-- 标签编辑 -->
               <view v-else-if="editType === 'tags'">
                  <view class="tags-edit">
                     <view
                        v-for="(tag, index) in editTags"
                        :key="index"
                        class="edit-tag">
                        <text>{{ tag }}</text>
                        <view class="tag-close" @click="removeTag(index)"
                           >×</view
                        >
                     </view>
                     <view class="add-tag">
                        <input
                           v-model="newTag"
                           placeholder="添加标签"
                           type="text"
                           maxlength="10"
                           class="custom-input tag-input" />
                        <view class="add-tag-btn" @click="addTag">添加</view>
                     </view>
                  </view>
               </view>
            </view>

            <!-- 确认按钮 -->
            <view class="confirm-btn" @click="confirmEdit">确定</view>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { useStore } from '@/store/user';
import { useRouter } from 'uni-mini-router';
import { UserEdit, UserExit, UserGetInfo } from '@/api/user';
import { updateOssFile } from '@/api/file';
// 导入wot ui组件

const { userInfo, getEiditUser, setUserInfo, setTokens } = useStore();
const router = useRouter();

// 表单数据
const form = reactive({
   username: '',
   avatarUrl: '',
   gender: 0,
   age: 0,
   email: '',
   introductory: '',
   tags: '',
   imagsarr: ''
});

// 相册图片数组
const albumImages = ref<string[]>([]);

// 标签列表
const tagList = computed(() => {
   try {
      return form.tags ? JSON.parse(form.tags) : [];
   } catch {
      return [];
   }
});

// 编辑弹窗相关
const showEditDialog = ref(false);
const editType = ref('');
const editValue = ref('');
const editTitle = ref('');
const editPlaceholder = ref('');
const editTags = ref<string[]>([]);
const newTag = ref('');

// 初始化数据
onMounted(() => {
   if (userInfo) {
      // 复制用户信息到表单
      form.username = userInfo.username || '';
      form.avatarUrl =
         userInfo.avatarUrl ||
         'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png';
      form.gender = userInfo.gender || 0;
      form.age = userInfo.age || 0;
      form.email = userInfo.email || '';
      form.introductory = userInfo.introductory || '';
      form.tags = userInfo.tags || '[]';
      form.imagsarr = userInfo.imagsarr || '[]';

      // 解析相册图片
      try {
         if (userInfo.imagsarr) {
            albumImages.value = JSON.parse(userInfo.imagsarr);
         }
      } catch (error) {
         albumImages.value = [];
      }
   }
});

// 返回上一页
const handleBack = () => {
   router.back();
};

// 保存资料
const handleSave = async () => {
   // 这里可以调用保存接口

   uni.showLoading({
      title: '修改信息中...',
      mask: true
   });
   //上传图片

   // 保存用户信息

   let res = await UserEdit(getEiditUser(form));
   if (res.code == 0) {
      // 重新获取用户信息存储
      let res = await UserGetInfo();
      console.log(res);

      if (res.code == 0) {
         setUserInfo(res.data);
         console.log('修改后', userInfo);

         setTokens(res.data);
         uni.hideLoading();
         uni.showToast({
            title: '修改成功',
            icon: 'none'
         });
         setTimeout(() => {
            router.back();
         }, 1000);
      }
   } else {
      uni.hideLoading();
      uni.showToast({
         title: '修改失败',
         icon: 'none'
      });
   }
   // 模拟保存成功
};

// 头像上传
const handleAvatarUpload = () => {
   uni.chooseImage({
      count: 1,
      success: res => {
         form.avatarUrl = res.tempFilePaths[0];
         // 这里可以调用上传文件接口
      }
   });
};

// 添加相册图片
const handleAddImage = () => {
   if (albumImages.value.length >= 8) {
      uni.showToast({
         title: '最多只能添加8张照片',
         icon: 'none'
      });
      return;
   }

   uni.chooseImage({
      count: 9 - albumImages.value.length,
      success: res => {
         updateOssFile(res.tempFilePaths[0]).then(res => {
            if (res.statusCode == 200) {
               const reson = JSON.parse(res.data);
               if (reson.code == 0) {
                  albumImages.value = [...albumImages.value, reson.data];
                  form.imagsarr = JSON.stringify(albumImages.value);
                  console.log('添加', form.imagsarr);
               } else {
                  uni.showToast({
                     title: reson.msg,
                     icon: 'none'
                  });
               }
            }
         });

         // 更新imagsarr
      }
   });
};

// 删除相册图片
const handleDeleteImage = (index: number) => {
   uni.showModal({
      title: '提示',
      content: '确定要删除这张照片吗？',
      success: res => {
         if (res.confirm) {
            albumImages.value.splice(index, 1);
            form.imagsarr = JSON.stringify(albumImages.value);
            console.log('删除相册图片', form.imagsarr);
         }
      }
   });
};

// 打开编辑弹窗
const handleEditInfo = (type: string) => {
   console.log('打开编辑弹窗，类型:', type);

   editType.value = type;

   switch (type) {
      case 'username':
         editTitle.value = '编辑昵称';
         editValue.value = form.username;
         editPlaceholder.value = '请输入昵称';
         break;
      case 'gender':
         editTitle.value = '选择性别';
         editValue.value = form.gender || 0;
         break;
      case 'age':
         editTitle.value = '编辑年龄';
         editValue.value = form.age && form.age > 0 ? form.age.toString() : '';
         break;
      case 'email':
         editTitle.value = '编辑邮箱';
         editValue.value = form.email;
         editPlaceholder.value = '请输入邮箱';
         break;
      case 'introductory':
         editTitle.value = '编辑交友宣言';
         editValue.value = form.introductory;
         editPlaceholder.value = '请输入交友宣言';
         break;
      case 'tags':
         editTitle.value = '编辑标签';
         editTags.value = [...tagList.value];
         newTag.value = '';
         break;
   }

   // 强制更新视图并显示弹窗
   setTimeout(() => {
      showEditDialog.value = true;
      console.log('弹窗状态设置为:', showEditDialog.value);
   }, 0);
};

// 关闭弹窗
const closeDialog = () => {
   showEditDialog.value = false;
};

// 确认编辑
const confirmEdit = () => {
   switch (editType.value) {
      case 'username':
         form.username = editValue.value;
         break;
      case 'gender':
         form.gender = editValue.value;
         break;
      case 'age':
         form.age = Number(editValue.value) || 0;
         break;
      case 'email':
         form.email = editValue.value;
         break;
      case 'introductory':
         form.introductory = editValue.value;
         break;
      case 'tags':
         form.tags = JSON.stringify(editTags.value);
         break;
   }

   showEditDialog.value = false;
};

// 添加标签
const addTag = () => {
   if (
      newTag.value &&
      !editTags.value.includes(newTag.value) &&
      editTags.value.length < 10
   ) {
      editTags.value.push(newTag.value);
      newTag.value = '';
   } else if (editTags.value.length >= 10) {
      uni.showToast({
         title: '最多添加10个标签',
         icon: 'none'
      });
   }
};

// 删除标签
const removeTag = (index: number) => {
   editTags.value.splice(index, 1);
};
</script>

<style lang="scss" scoped>
// 主色调
$primary-color: #ffe088;
$light-gray: #f5f5f5;
$border-color: #e0e0e0;
$text-primary: #333;
$text-secondary: #666;
$text-tertiary: #999;

.exituser-page {
   background-color: #f3f3f3;
   min-height: 100vh;
}

// 顶部导航栏
.nav-bar {
   margin-top: var(--status-bar-height);
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: 92rpx;
   padding: 0 30rpx;
   background-color: #fff;
   border-bottom: 1rpx solid $border-color;
   position: fixed;
   width: 100%;
   top: 0;
   z-index: 999;
}

.nav-left {
   width: 80rpx;
   display: flex;
   align-items: center;
}

.nav-center {
   flex: 1;
   text-align: center;
}

.nav-title {
   font-size: 36rpx;
   font-weight: 600;
   color: $text-primary;
}

.nav-right {
   width: 80rpx;
   text-align: right;
}

.save-btn {
   font-size: 32rpx;
   color: #000;
   font-weight: 500;
}

// 内容区域
.content {
   min-height: calc(100vh - 92rpx);
   margin-top: 44px;
}

// 头像区域
.avatar-section {
   display: flex;
   flex-direction: column;
   align-items: center;
   padding: 40rpx 0;

   margin-bottom: 20rpx;
}

.avatar-container {
   position: relative;

   margin-bottom: 20rpx;
}

.avatar {
   width: 180rpx;
   height: 180rpx;
   border-radius: 50%;
}

.avatar-upload-btn {
   position: absolute;
   right: 0;
   bottom: 0;
   width: 56rpx;
   height: 56rpx;
   border-radius: 50%;
   background-color: $primary-color;
   display: flex;
   align-items: center;
   justify-content: center;
   border: 3rpx solid #fff;
}

.username {
   font-size: 36rpx;
   font-weight: 600;
   color: $text-primary;
}

// 相册区域
.album-section {
   background-color: #fff;
   padding: 30rpx;
   width: 95%;
   margin: 0 auto;
   border-radius: 12rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: 600;
   color: $text-primary;
   margin-bottom: 30rpx;
}

.album-grid {
   display: grid;
   grid-template-columns: repeat(4, 1fr);
   gap: 20rpx;
}

.album-item {
   position: relative;
   aspect-ratio: 1;
   border-radius: 12rpx;
   overflow: hidden;
   background-color: $light-gray;
}

.album-image {
   width: 100%;
   height: 100%;
}

.delete-btn {
   position: absolute;
   top: 10rpx;
   right: 10rpx;
   width: 36rpx;
   height: 36rpx;
   border-radius: 50%;
   background-color: rgba(0, 0, 0, 0.5);
   display: flex;
   align-items: center;
   justify-content: center;
}

.add-btn {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   border: 1rpx dashed $border-color;
}

.add-text {
   font-size: 24rpx;
   color: $text-tertiary;
   margin-top: 10rpx;
}

// 信息区域
.info-section {
   background-color: #fff;
   padding: 30rpx 30rpx;
   width: 95%;
   margin: 40rpx auto;
   border-radius: 12rpx;
}

.info-item {
   display: flex;
   align-items: center;
   justify-content: space-between;
   padding: 0 30rpx;
   height: 100rpx;

   border-bottom: 1rpx solid $border-color;
}

.info-item:last-child {
   border-bottom: none;
}

.info-label {
   font-size: 24rpx;
   color: $text-secondary;
}

.info-value {
   display: flex;
   align-items: flex-start;
   flex: 1;
   justify-content: flex-end;
   min-height: 44rpx;
}

.info-text {
   font-size: 26rpx;
   color: $text-primary;
   margin-right: 10rpx;
   text-align: right;
   overflow: hidden;
   white-space: nowrap;
   text-overflow: ellipsis;
}

// 移除多行显示样式，使用单行显示并截取前5个字符

.tags {
   display: flex;
   flex-wrap: wrap;
   justify-content: flex-end;
   margin-right: 10rpx;
   flex: 1;
   text-align: right;
}

.tag {
   padding: 6rpx 20rpx;
   background-color: $light-gray;
   border-radius: 20rpx;
   font-size: 24rpx;
   color: $text-secondary;
   margin-left: 10rpx;
   margin-bottom: 10rpx;
}

// 自定义弹窗样式
.custom-popup-overlay {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   bottom: 0;
   background-color: rgba(0, 0, 0, 0.5);
   display: flex;
   align-items: flex-end;
   justify-content: center;
   z-index: 1000;
}

.custom-popup {
   background-color: #fff;
   width: 100%;
   border-top-left-radius: 30rpx;
   border-top-right-radius: 30rpx;
   padding: 0;
   max-height: 80vh;
   animation: slide-up 0.3s ease-out;
}

@keyframes slide-up {
   from {
      transform: translateY(100%);
   }
   to {
      transform: translateY(0);
   }
}

.dialog-header {
   display: flex;
   align-items: center;
   justify-content: space-between;
   padding: 30rpx;
   border-bottom: 1rpx solid $border-color;
}

.dialog-title {
   font-size: 36rpx;
   font-weight: 600;
   color: $text-primary;
}

.close-btn {
   width: 50rpx;
   height: 50rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.close-icon {
   font-size: 48rpx;
   color: $text-tertiary;
   line-height: 1;
}

.popup-content {
   padding: 30rpx;
   min-height: 200rpx;
}

// 输入框样式
.custom-input {
   width: 100%;
   padding: 20rpx;
   border: 1rpx solid $border-color;
   border-radius: 12rpx;
   font-size: 32rpx;
   height: auto !important;
   box-sizing: border-box;
}

.custom-input.textarea {
   min-height: 180rpx;
   padding: 20rpx;
   resize: none;
}

.word-limit {
   display: block;
   text-align: right;
   font-size: 24rpx;
   color: $text-tertiary;
   margin-top: 10rpx;
}

// 性别选择样式
.gender-options {
   display: flex;
   gap: 40rpx;
   margin-bottom: 40rpx;
}

.gender-option {
   flex: 1;
   display: flex;
   flex-direction: column;
   align-items: center;
   padding: 30rpx;
   border: 1rpx solid $border-color;
   border-radius: 12rpx;
   transition: all 0.3s;
}

.gender-option.active {
   border-color: $primary-color;
   color: $primary-color;
}

.gender-option text {
   font-size: 32rpx;
   margin-bottom: 15rpx;
}

.success-icon {
   width: 36rpx;
   height: 36rpx;
   border-radius: 50%;
   background-color: $primary-color;
   color: #fff;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 24rpx;
}

// 标签编辑样式
.tags-edit {
   margin-bottom: 20rpx;
}

.edit-tag {
   display: inline-flex;
   align-items: center;
   padding: 10rpx 20rpx;
   background-color: $light-gray;
   border-radius: 20rpx;
   margin-right: 15rpx;
   margin-bottom: 15rpx;
   font-size: 28rpx;
   color: $text-secondary;
}

.tag-close {
   margin-left: 10rpx;
   font-size: 30rpx;
   color: $text-tertiary;
}

.add-tag {
   display: flex;
   gap: 20rpx;
   margin-top: 20rpx;
   align-items: center;
}

.tag-input {
   flex: 1;
}

.add-tag-btn {
   padding: 20rpx 30rpx;
   background-color: $primary-color;
   color: #fff;
   border-radius: 12rpx;
   font-size: 28rpx;
   white-space: nowrap;
}

// 确认按钮样式
.confirm-btn {
   margin: 0 30rpx 30rpx;
   padding: 28rpx;
   background-color: $primary-color;
   color: #000;
   border-radius: 12rpx;
   text-align: center;
   font-size: 32rpx;
   font-weight: 500;
}
</style>
