<template>
   <view
      class="personal-info-container"
      :style="{ backgroundColor: '#f5f5f5' }">
      <!-- 步骤指示器 -->
      <view class="step-indicator">
         <view :class="['step-dot', step >= 1 ? 'active' : '']"></view>
         <view :class="['step-line', step >= 2 ? 'active' : '']"></view>
         <view :class="['step-dot', step >= 2 ? 'active' : '']"></view>
         <view :class="['step-line', step >= 3 ? 'active' : '']"></view>
         <view :class="['step-dot', step >= 3 ? 'active' : '']"></view>
      </view>

      <!-- 步骤内容区域 -->
      <view class="content-area">
         <!-- 第一步：基本信息 -->
         <view v-if="step === 1" class="step-content">
            <view class="step-title">完善基本信息</view>

            <!-- 头像上传 -->
            <view class="avatar-upload spaced">
               <wd-img-cropper
                  v-model="show"
                  :img-src="src"
                  @confirm="handleConfirm"
                  @cancel="handleCancel">
               </wd-img-cropper>
               <view class="profile">
                  <view v-if="!imgSrc" class="img" @click="upload">
                     <wd-icon
                        name="fill-camera"
                        custom-class="img-icon"></wd-icon>
                  </view>
                  <wd-img
                     v-if="imgSrc"
                     round
                     width="200px"
                     height="200px"
                     :src="imgSrc"
                     mode="aspectFit"
                     custom-class="profile-img"
                     @click="upload" />
                  <view style="font-size: 14px" v-if="!imgSrc"
                     >点击上传头像</view
                  >
               </view>
            </view>

            <view
               @click="randAvatar"
               class="form-item spaced"
               style="
                  text-align: center;
                  padding: 30rpx 0;
                  font-size: 32rpx;
                  justify-content: space-between;
                  /* 值可以是 px、em、rem、normal 等 */
               ">
               <text style="margin-right: 15rpx">随机头像</text>
               <wd-icon name="refresh1"></wd-icon>
            </view>

            <!-- 名称输入 -->
            <view class="form-item spaced">
               <wd-input
                  label="用户名称"
                  v-model="userInfo.name"
                  placeholder="请输入您的名称"
                  @clicksuffixicon="randedit"
                  suffix-icon="refresh1" />
            </view>

            <!-- 年龄选择 -->
            <view class="form-item spaced">
               <wd-picker
                  label="年龄选择"
                  v-model="userInfo.age"
                  :columns="agecolumns"
                  title="年龄" />
            </view>

            <!-- 性别选择 -->
            <view class="spaced">
               <view class="gender-selector">
                  <view
                     :class="[
                        'gender-option',
                        userInfo.gender === 0 ? 'selected' : ''
                     ]"
                     @click="userInfo.gender = 0">
                     <text>女</text>
                  </view>
                  <view
                     :class="[
                        'gender-option',
                        userInfo.gender === 1 ? 'selected' : ''
                     ]"
                     @click="userInfo.gender = 1">
                     <text>男</text>
                  </view>
               </view>
            </view>
         </view>

         <!-- 第二步：选择标签 -->
         <view v-if="step === 2" class="step-content">
            <view class="step-title">选择您的兴趣标签</view>
            <view class="tags-container spaced">
               <view
                  v-for="tag in availableTags"
                  :key="tag"
                  :class="[
                     'tag-item',
                     selectedTags.includes(tag) ? 'selected' : ''
                  ]"
                  @click="toggleTag(tag)"
                  :style="getTagStyle(tag)">
                  <text>{{ tag }}</text>
               </view>
            </view>
         </view>

         <!-- 第三步：上传相册和简介 -->
         <view v-if="step === 3" class="step-content">
            <view class="step-title">完善个人展示</view>

            <!-- 相册上传 -->

            <view class="album-upload spaced">
               <wd-upload
                  :file-list="imagsarr"
                  multiple
                  :before-remove="removeImage"
                  :upload-method="updateFileAarrhandleChange"
                  action="https://mockapi.eolink.com/zhTuw2P8c29bc981a741931bdd86eb04dc1e8fd64865cb5/upload" />
            </view>

            <!-- 个人简介 -->

            <view class="form-item spaced">
               <textarea
                  v-model="userData.introductory"
                  placeholder="请输入交友介绍"
                  placeholder-class="placeholder"
                  class="textarea-field"
                  rows="4"></textarea>
            </view>
         </view>
      </view>

      <!-- 底部按钮区域 -->
      <view class="bottom-area">
         <wd-button
            v-if="step < 3"
            @click="nextStep"
            class="next-btn"
            type="default"
            :style="{ backgroundColor: '#0BDAEE', color: '#fff' }">
            下一步
         </wd-button>
         <wd-button
            v-else
            @click="complete"
            class="next-btn"
            type="default"
            :style="{ backgroundColor: '#0BDAEE', color: '#fff' }">
            开始趣友
         </wd-button>
      </view>

      <!-- 右上角跳过按钮 -->
      <!-- <view v-if="step < 3" class="skip-btn" @click="skipStep">跳过</view> -->
      <!-- 返回按钮 (从第二步开始显示) -->
      <view v-if="step > 1" class="back-button-container" @click="goBack">
         上一步
      </view>
      <wd-toast />
   </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'uni-mini-router';
import defaultAvatar from '@/static/imgs/bg.png';
import { UserExit, UserUpdateLongitude } from '@/api/user';
import { agecolumns, getRandomNickname, getRandomAvatar } from './index';
import { SystemTag } from '@/api/system';
import { updateOssFile } from '@/api/file';
import { useStore } from '@/store/user';

const store = useStore();
const router = useRouter();
const src = ref<string>('');
const imgSrc = ref<string>('');
const show = ref<boolean>(false);
// 基本信息
const userInfo = reactive({
   name: '',
   avatarUrl: null,
   age: null,
   gender: 0
});
// 可用标签列表
const availableTags = ref<string[]>([]);
// 当前步骤
const step = ref(1);

// 选择的标签
const selectedTags = ref<string[]>([]);
const avatarUrl = ref<string>('');
const imagsarr = ref<Array<{ url: string; thumb: string }>>([]);

// 个人数据
const userData = reactive({
   tag: [] as string[],
   imagsarr: [] as string[],
   introductory: ''
});
onMounted(() => {
   SystemTag({
      current: 1,
      pageSize: 50,
      sortField: '',
      sortOrder: '',
      tagName: ''
   }).then(res => {
      if (res.code === 0) {
         availableTags.value = res.data.records.map(item => {
            return item.tagname;
         });
      }
   });
});

const randAvatar = async () => {
   let res = await getRandomAvatar();
   console.log('数据', res);

   if (res.code == 200) {
      userInfo.avatarUrl = res.data;
      imgSrc.value = res.data;
   } else {
      uni.showToast({
         title: '随机头像失败',
         icon: 'none'
      });
   }
};
function upload() {
   uni.chooseImage({
      count: 1,
      success: res => {
         const tempFilePaths = res.tempFilePaths[0];
         src.value = tempFilePaths;
         show.value = true;
      }
   });
}
function handleConfirm(event) {
   const { tempFilePath } = event;
   imgSrc.value = tempFilePath;
   // 直接保存临时文件路径，不转换为File对象
   avatarUrl.value = tempFilePath;
}
function imgLoaderror(res) {
   console.log('加载失败', res);
}
function imgLoaded(res) {
   console.log('加载成功', res);
}
function handleCancel(event) {
   console.log('取消', event);
}

const randedit = () => {
   userInfo.name = getRandomNickname();
};
// 移除图片
function removeImage(index: number) {
   imagsarr.value.splice(index, 1);
   userData.imagsarr.splice(index, 1);
   console.log(userData.imagsarr);
}
//照片墙上传
const updateFileAarrhandleChange = (file, formData, options) => {
   // 在APP中使用更兼容的方式处理
   if (file && file.url) {
      options?.onSuccess(
         {
            data: {
               url: file.url
            }
         },
         file,
         formData
      );

      imagsarr.value.push(file);
   }
};

// 切换标签选择
function toggleTag(tag: string) {
   const index = selectedTags.value.indexOf(tag);
   if (index > -1) {
      selectedTags.value.splice(index, 1);
   } else {
      selectedTags.value.push(tag);
   }
}

// 获取标签样式（实现飘动效果）
function getTagStyle(tag: string) {
   const index = availableTags.value.indexOf(tag);
   const randomTop = Math.sin(index) * 10;
   const animationDelay = index * 0.1;

   return {
      transform: `translateY(${randomTop}px)`,
      animationDelay: `${animationDelay}s`
   };
}

// 返回上一步
function goBack() {
   if (step.value > 1) {
      step.value--;
   }
}

// 下一步
function nextStep() {
   if (step.value === 1) {
      if (!userInfo.name) {
         uni.showToast({
            title: '请输入姓名',
            icon: 'none'
         });
         return;
      }
      if (!userInfo.age) {
         uni.showToast({
            title: '请选择年龄',
            icon: 'none'
         });
         return;
      }

      // 保存基本信息
   } else if (step.value === 2) {
      // 保存选择的标签
      userData.tag = [...selectedTags.value];
   }

   step.value++;
}

// 跳过步骤
function skipStep() {
   if (step.value === 1) {
      if (!userInfo.name) {
         uni.showToast({
            title: '请输入姓名',
            icon: 'none'
         });
         return;
      }
      if (!userInfo.age) {
         uni.showToast({
            title: '请选择年龄',
            icon: 'none'
         });
         return;
      }
   }
   step.value++;
}

// 完成所有步骤
async function complete() {
   //前端数据请求时，显示加载提示弹框
   uni.showLoading({
      title: '初始化数据中...',
      mask: true
   });

   //上传相册
   if (imagsarr.value.length > 0) {
      const uploadPromises = [];

      for (let file of imagsarr.value) {
         if (file && file.thumb) {
            uploadPromises.push(updateOssFile(file.thumb));
         }
      }

      //上传相册
      if (uploadPromises.length > 0) {
         try {
            const results = await Promise.all(uploadPromises);
            for (let records of results) {
               try {
                  let data =
                     typeof records?.data === 'string'
                        ? JSON.parse(records.data)
                        : records.data;
                  if (data && data.code === 0 && data.data) {
                     userData.imagsarr.push(data.data);
                  }
               } catch (parseError) {
                  console.error('解析上传结果失败:', parseError);
               }
            }
         } catch (uploadError) {
            console.error('上传相册失败:', uploadError);
         }
      }
   }

   //上传头像
   if (avatarUrl.value) {
      try {
         let res = await uni.uploadFile({
            url: '/api/api/file',
            filePath: avatarUrl.value,
            name: 'file',
            formData: {
               tagId: 9
            }
         });
         if (res?.data) {
            let data = JSON.parse(res.data);
            if (data.code === 0) {
               userInfo.avatarUrl = data.data;
            }
         }
      } catch (error) {
         console.error('上传头像失败:', error);
      }
   }

   let res = await UserExit({
      ...userInfo,
      ...userData,
      tag: userData.tag.length > 0 ? JSON.stringify(userData.tag) : null,
      imagsarr:
         userData.imagsarr.length > 0 ? JSON.stringify(userData.imagsarr) : null
   });

   if (res.code === 0) {
      uni.hideLoading();
      store.setUserInfo(res.data);
      store.setLocation();
      router.pushTab({ name: 'tabar' });
   } else {
      uni.hideLoading();
   }
}
</script>

<style lang="scss" scoped>
.personal-info-container {
   width: 100vw;
   height: 100vh;
   display: flex;
   flex-direction: column;
   padding: 30rpx;
   padding-top: var(--status-bar-height);
   box-sizing: border-box;
}

.step-indicator {
   display: flex;
   align-items: center;
   justify-content: center;
   margin: 30rpx 0;
   gap: 30rpx;
}

.step-dot {
   width: 20rpx;
   height: 20rpx;
   border-radius: 50%;
   background-color: #ddd;
   transition: all 0.3s;
}

.step-dot.active {
   background-color: #0bdaee;
   transform: scale(1.2);
}

.step-line {
   width: 100rpx;
   height: 4rpx;
   background-color: #ddd;
   transition: all 0.3s;
}

.step-line.active {
   background-color: #0bdaee;
}

.content-area {
   flex: 1;
   overflow-y: auto;
}

.step-content {
   width: 100%;
   height: 100%;
}

.step-title {
   font-size: 40rpx;
   font-weight: bold;
   text-align: center;
   margin-bottom: 60rpx;
   color: #92cefe;
}

/* 分散布局样式 */
.spaced {
   margin-bottom: 60rpx;
}

/* 返回按钮容器样式 */
.back-button-container {
   position: absolute;
   top: var(--status-bar-height);
   padding-top: 20rpx;
   left: 30rpx;
   font-size: 28rpx;
   color: #999;
}

/* 第一步样式 */
.avatar-upload {
   display: flex;
   justify-content: center;
   margin-bottom: 60rpx;
}

.avatar-preview,
.avatar-placeholder {
   width: 150rpx;
   height: 150rpx;
   border-radius: 50%;
   overflow: hidden;
   border: 2rpx solid #eee;
   display: flex;
   align-items: center;
   justify-content: center;
}

.avatar-preview image {
   width: 100%;
   height: 100%;
}

.avatar-placeholder {
   background-color: #f8f8f8;
}

.avatar-placeholder text {
   font-size: 24rpx;
   color: #999;
}

.avatar-input {
   display: none;
}

.form-item {
   margin-bottom: 40rpx;
   background: #fff;
   border-radius: 10rpx;
   overflow: hidden;
}

.input-field {
   width: 100%;
   height: 88rpx;
   border: 2rpx solid #eee;
   border-radius: 10rpx;
   padding: 0 20rpx;
   box-sizing: border-box;
   font-size: 30rpx;
}

.gender-selector {
   display: flex;
   gap: 30rpx;
}

.gender-option {
   flex: 1;
   height: 88rpx;
   border: 2rpx solid #eee;
   background: #fff;
   border-radius: 10rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 30rpx;
   color: #333;
   transition: all 0.3s;
}

.gender-option.selected {
   border-color: #0bdaee;
   color: #0bdaee;
   background-color: rgba(11, 218, 238, 0.1);
}

/* 第二步样式 */
.tags-container {
   display: flex;
   flex-wrap: wrap;
   gap: 20rpx;
   justify-content: space-between;
   padding: 20rpx;
}

.tag-item {
   padding: 20rpx 16rpx;
   border-radius: 40rpx;
   background-color: #fff;
   border: 2rpx solid #eee;
   font-size: 22rpx;
   color: #333;
   transition: all 0.3s;
   animation: float 4s ease-in-out infinite;
}

@keyframes float {
   0%,
   100% {
      transform: translateY(0);
   }
   50% {
      transform: translateY(-10px);
   }
}

.tag-item.selected {
   background-color: #0bdaee;
   color: black;
   border-color: #0bdaee;
}

/* 第三步样式 */
.album-upload {
   display: flex;
   flex-wrap: wrap;
   gap: 20rpx;

   background: #fff;
   padding: 20rpx 20rpx;
}

.album-item {
   width: 180rpx;
   height: 180rpx;
   position: relative;
   border-radius: 10rpx;
   overflow: hidden;
   border: 2rpx solid #eee;
}

.album-item image {
   width: 100%;
   height: 100%;
}

.remove-btn {
   position: absolute;
   top: 10rpx;
   right: 10rpx;
   width: 40rpx;
   height: 40rpx;
   background-color: rgba(0, 0, 0, 0.5);
   border-radius: 50%;
   color: #fff;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 36rpx;
   line-height: 1;
}

.add-album-btn {
   width: 180rpx;
   height: 180rpx;
   border: 2rpx dashed #ddd;
   border-radius: 10rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 60rpx;
   color: #999;
}

.album-input {
   display: none;
}

.textarea-field {
   width: 100%;
   border: 2rpx solid #eee;
   border-radius: 10rpx;
   padding: 20rpx;
   box-sizing: border-box;
   font-size: 30rpx;
   line-height: 1.5;
   resize: none;
}

/* 底部按钮区域 */
.bottom-area {
   padding: 30rpx 0;
}

.next-btn {
   width: 100%;
   height: 96rpx;
   font-size: 32rpx;
   border-radius: 48rpx;
}

/* 跳过按钮 */
.skip-btn {
   position: absolute;
   top: 45rpx;
   right: 30rpx;
   font-size: 28rpx;
   color: #999;
}

/* 过渡动画 */
.slide-enter-active,
.slide-leave-active {
   transition: transform 0.3s;
}

.slide-enter-from {
   transform: translateX(100%);
}

.slide-leave-to {
   transform: translateX(-100%);
}

.placeholder {
   color: #999;
}
.profile {
   width: 250rpx;
   height: 250rpx;
   background: #fff;
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: center;
   overflow: hidden;
   border-radius: 50%;
}
</style>
