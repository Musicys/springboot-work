<template>
   <view class="comment-list-container">
      <!-- 顶部用户信息 -->
      <view class="user-info-section" v-if="dynamicDetail">
         <view class="user-main-info">
            <wd-icon
               @click="onBack"
               name="arrow-left"
               size="16px"
               class="!mr-1.5"
               color="#333"></wd-icon>

            <image
               @click="
                  router.push({
                     name: 'preinfo',
                     params: { userId: dynamicDetail.userId }
                  })
               "
               class="user-avatar"
               :src="dynamicDetail.avatarUrl?.trim()"
               mode="aspectFill"></image>
            <view class="user-details">
               <text class="user-name">{{
                  dynamicDetail.username || '匿名用户'
               }}</text>
               <text class="post-time" selectable>{{
                  dynamicDetail.formattedTime
               }}</text>
            </view>
            <view class="user-actions">
               <button v-if="isSelf" class="follow-btn">自己</button>
               <button v-else class="follow-btn" @click="handleFollow">
                  {{ dynamicDetail.isFocus == '0' ? '关注' : '已关注' }}
               </button>
               <view class="share-icon">
                  <wd-icon name="share" size="24rpx" />
               </view>
            </view>
         </view>
      </view>

      <!-- 动态内容 -->
      <view class="dynamic-content" v-if="dynamicDetail">
         <view class="dynamic-text">
            <rich-text
               :selectable="true"
               space
               :nodes="dynamicDetail.context" />
            <!-- <text selectable>{{}}</text> -->
         </view>

         <!-- 动态图片 -->
         <view class="dynamic-images" v-if="imgarr.length">
            <tn-photo-album :data="imgarr" :column="3" />
         </view>

         <!-- 定位信息 -->
         <view
            class="location-info"
            v-if="dynamicDetail.province && dynamicDetail.district">
            <wd-icon name="location" size="22rpx" />
            <text
               >{{ dynamicDetail.district }}, {{ dynamicDetail.province }}</text
            >
         </view>

         <!-- 互动数据 -->
         <view class="interaction-stats">
            <view class="stat-item">
               <tn-icon name="message" size="36" bold />
               <text>{{ dynamicDetail.count }} 评论</text>
            </view>
            <view class="stat-item" @click="handleLike">
               <tn-icon
                  :color="dynamicDetail.isLove ? '#FF4D4F' : '#999'"
                  :name="dynamicDetail.isLove ? 'like-fill' : 'like'"
                  size="36"
                  bold />
               <text>{{ dynamicDetail.love }} 点赞</text>
            </view>
         </view>
      </view>

      <!-- 评论列表 -->
      <view class="comment-list" v-if="commentList.length > 0">
         <text class="comment-list-title"
            >评论 ({{ dynamicDetail.count }})</text
         >
         <comment
            v-for="item in commentList"
            :key="item.id"
            :comment="item"
            @reply="handleReply" />
         <view class="no-comments" style="padding-top: 0">
            <text>已经到底了</text>
         </view>
      </view>

      <!-- 没有评论时的提示 -->
      <view
         class="no-comments"
         v-else-if="commentList.length === 0 && dynamicDetail">
         <text>暂无评论，发表你的意见把！</text>
      </view>

      <!-- 加载中 -->
      <view class="loading" v-if="loading">
         <text>加载中...</text>
      </view>

      <!-- 底部互动区域 -->
      <view class="interaction-section">
         <!-- 已选图片预览区域 -->
         <view class="selected-images-preview" v-if="selectedImages.length > 0">
            <view class="preview-list">
               <view
                  v-for="(img, index) in selectedImages"
                  :key="index"
                  class="preview-item">
                  <image
                     :src="img"
                     mode="aspectFill"
                     class="preview-image"></image>
                  <view class="delete-btn" @click.stop="deleteImage(index)">
                     <wd-icon name="close" size="24rpx" color="#fff" />
                  </view>
               </view>
            </view>
         </view>
         <view class="comment-input-area">
            <input
               class="input-title"
               v-model="commentText"
               :placeholder="
                  replyingTo ? `回复 @${replyingTo.username}：` : '说点什么...'
               " />
            <view class="input-actions">
               <view
                  v-if="replyingTo"
                  class="cancel-reply"
                  @click="cancelReply">
                  <text>取消</text>
               </view>
               <view class="photo-btn" @click="chooseImages">
                  <wd-icon name="image" size="28rpx" color="#666" />
               </view>
               <button
                  class="send-btn"
                  @click="submitComment"
                  :disabled="
                     !commentText.trim() && selectedImages.length === 0
                  ">
                  发送
               </button>
            </view>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'uni-mini-router';
import { useStore } from '@/store/user';
import {
   getDynamicDetail,
   getReplyList,
   getDynamicOne,
   addComment
} from '@/api/dynamic';
import Comment from './components/Comment.vue';
import { LoveAdd, LoveDel } from '@/api/love';
import { updateOssFile } from '@/api/file';

const router = useRouter();
const store = useStore();
const route = useRoute();

const commentList = ref<any[]>([]);
const commentText = ref('');
const loading = ref(false);
const dynamicDetail = ref<any>(null);
const selectedImages = ref<string[]>([]);
//是否是自己的动态
const isSelf = computed(() => {
   return dynamicDetail.value?.userId === store.userInfo?.id;
});

// 当前回复的用户信息
const replyingTo = ref<{ id: number; username: string } | null>(null);

const imgarr = computed(() => {
   try {
      return dynamicDetail.value?.imgarr
         ? JSON.parse(dynamicDetail.value?.imgarr)
         : [];
   } catch (error) {
      return [];
   }
});

//点赞 取消点赞
const handleLike = async () => {
   if (dynamicDetail.value?.isLove) {
      //取消点赞
      let res = await LoveDel({
         commentsId: '',
         dynamicId: dynamicDetail.value?.id
      });
      if (res.code == 0) {
         dynamicDetail.value.isLove = false;

         dynamicDetail.value.love -= 1;
      }
   } else {
      //点赞

      let res = await LoveAdd({
         commentsId: '',
         dynamicId: dynamicDetail.value?.id
      });
      if (res.code == 0) {
         dynamicDetail.value.isLove = true;

         dynamicDetail.value.love += 1;
      }
   }
};
// 格式化时间
const formatTime = (timeString: string) => {
   const time = new Date(timeString);
   const now = new Date();
   const diff = now.getTime() - time.getTime();

   const minutes = Math.floor(diff / 60000);
   const hours = Math.floor(diff / 3600000);
   const days = Math.floor(diff / 86400000);

   if (minutes < 1) return '刚刚';
   if (minutes < 60) return `${minutes}分钟前`;
   if (hours < 24) return `${hours}小时前`;
   if (days < 7) return `${days}天前`;

   return `${time.getFullYear()}-${String(time.getMonth() + 1).padStart(2, '0')}-${String(time.getDate()).padStart(2, '0')}`;
};

// 获取动态详情
const getDynamicDetails = async () => {
   console.log('==>', route.query.dynamicId);

   if (!route.query.dynamicId) return;
   loading.value = true;
   try {
      const res = await getDynamicOne(Number(route.query.dynamicId));

      if (res.code === 0 && res.data) {
         // 处理时间格式化
         dynamicDetail.value = {
            ...res.data,
            formattedTime: formatTime(res.data.createTime)
         };
         console.log(dynamicDetail.value);

         // 同时获取评论列表
         await getCommentList();
      }
   } catch (error) {
      console.error('获取动态详情失败:', error);
   } finally {
      loading.value = false;
   }
};

// 获取评论列表
const getCommentList = async () => {
   if (!route.query.dynamicId) return;

   try {
      const res = await getDynamicDetail({
         dynamicId: Number(route.query.dynamicId),
         page: 1,
         pageSize: 20,
         type: 1
      });

      if (res.code === 0 && res.data) {
         // 处理时间格式化
         const formattedComments = res.data.map((comment: any) => ({
            ...comment,
            formattedTime: formatTime(comment.createTime)
         }));
         commentList.value = formattedComments;
      }
   } catch (error) {
      console.error('获取评论列表失败:', error);
   }
};

// 从相册选择图片
const chooseImages = async () => {
   try {
      const result = await uni.chooseImage({
         count: 9, // 最多选择9张图片
         sizeType: ['original', 'compressed'],
         sourceType: ['album']
      });

      // 检查是否已达到上传限制（1张）
      if (selectedImages.value.length >= 1) {
         uni.showToast({
            title: '最多只能上传1张图片',
            icon: 'none'
         });
         return;
      }

      // 限制只能选择一张图片
      const imagesToAdd = result.tempFilePaths.slice(
         0,
         1 - selectedImages.value.length
      );
      selectedImages.value = [...selectedImages.value, ...imagesToAdd];
   } catch (error) {
      console.error('选择图片失败:', error);
   }
};

// 删除已选择的图片
const deleteImage = (index: number) => {
   selectedImages.value.splice(index, 1);
};

// 处理回复事件
const handleReply = (comment: any) => {
   // 设置回复状态
   replyingTo.value = {
      id: comment.id,
      username: comment.username
   };

   // 自动聚焦到输入框
   // 在实际项目中可能需要使用ref来操作输入框
   setTimeout(() => {
      const input = document.querySelector('.input-title') as HTMLInputElement;
      input?.focus();
   }, 100);
};

// 取消回复
const cancelReply = () => {
   replyingTo.value = null;
   from.replyUserId = null;
};

// 提交评论
const submitComment = async () => {
   if (
      (!commentText.value.trim() && selectedImages.value.length === 0) ||
      !route.query.dynamicId
   ) {
      return;
   }
   console.log('点击了', selectedImages.value[0]);
   let fileUrl = null;
   if (selectedImages.value[0]) {
      const result = await updateOssFile(selectedImages.value[0]);

      const res = JSON.parse((result.data as string) || []);
      console.log(res);

      if (res.code === 0) {
         fileUrl = res.data;
      }
   }

   // 准备提交的数据
   const commentData = {
      context: commentText.value,
      imgArr: fileUrl ? JSON.stringify([fileUrl]) : '',
      replyId: Number(route.query.dynamicId),
      replyUserId: replyingTo.value?.id || null
   };

   let res = await addComment(commentData);

   // 模拟提交成功
   if (res.code === 0) {
      uni.showToast({
         title: res.data,
         icon: 'none'
      });
      getDynamicDetails();
      // 重置状态
      commentText.value = '';
      selectedImages.value = []; // 清空已选择的图片
      replyingTo.value = null;
   }
};
const onBack = () => {
   router.back();
};

onMounted(() => {
   getDynamicDetails();
});
</script>

<style lang="scss" scoped>
.comment-list-container {
   background-color: #fff;
   min-height: 100vh;
   padding-top: var(--status-bar-height); // 状态栏
   padding-bottom: env(safe-area-inset-bottom); // 底部安全区
}

.user-info-section {
   padding: 20rpx;
   position: fixed;
   background: #fff;
   z-index: 999;
   top: 0;
   padding-top: var(--status-bar-height);
   left: 0;
   right: 0;

   border-bottom: 1rpx solid #f0f0f0;
}

.user-main-info {
   display: flex;
   align-items: center;
}

.user-avatar {
   width: 80rpx;
   height: 80rpx;
   border-radius: 50%;
   margin-right: 20rpx;
}

.user-details {
   flex: 1;
}

.user-name {
   font-size: 30rpx;
   font-weight: bold;
   color: #000;
   display: block;
   margin-bottom: 8rpx;
}

.post-time {
   font-size: 24rpx;
   color: #999;
}

.user-actions {
   display: flex;
   align-items: center;
}

.follow-btn {
   background-color: #0bdaee;
   color: #fff;
   border-radius: 20rpx;

   font-size: 24rpx;
   margin-right: 20rpx;
}

.share-icon {
   padding: 10rpx;
}

/* 动态内容 */
.dynamic-content {
   padding: 20rpx;
   margin-top: 150rpx;
   margin-bottom: 20rpx;
}

.dynamic-text {
   font-size: 26rpx;
   color: #333;
   line-height: 50rpx;
   margin-bottom: 20rpx;
   word-break: break-all;
}

.dynamic-images {
   display: flex;
   gap: 10rpx;
   margin-bottom: 20rpx;
   flex-wrap: wrap;
}

.dynamic-image {
   width: 100%;
   height: auto;
   border-radius: 15rpx;
}

.location-info {
   display: flex;
   align-items: center;
   gap: 8rpx;
   font-size: 26rpx;
   color: #0bdaee;
   margin-bottom: 20rpx;
}

.interaction-stats {
   display: flex;
   gap: 40rpx;
   padding: 10rpx 0;
   border-top: 1rpx solid #f0f0f0;
   border-bottom: 1rpx solid #f0f0f0;
}

.stat-item {
   display: flex;
   align-items: center;
   gap: 8rpx;
   font-size: 26rpx;
   color: #666;
}

/* 评论列表 */
.comment-list {
   padding: 20rpx;
   padding-bottom: 150rpx;
}

.comment-list-title {
   font-size: 28rpx;
   font-weight: bold;
   color: #000;
   margin-bottom: 20rpx;
   display: block;
}

.no-comments {
   text-align: center;
   color: #999;
   font-size: 28rpx;
}

.loading {
   padding: 60rpx 0;
   text-align: center;
   color: #999;
   font-size: 28rpx;
}

/* 已选图片预览区域 */
.selected-images-preview {
   padding: 20rpx;
   background-color: #fff;
   margin-bottom: 200rpx;
   position: absolute;
   top: -110%;
   right: 15%;
}

.preview-title {
   font-size: 26rpx;
   color: #666;
   margin-bottom: 15rpx;
}

.preview-list {
   display: flex;
   gap: 10rpx;
   flex-wrap: wrap;
   position: absolute;
}

.preview-item {
   width: 100rpx;
   height: 100rpx;
   position: relative;
   border-radius: 10rpx;
   overflow: hidden;
}

.preview-image {
   width: 100%;
   height: 100%;
}

.delete-btn {
   position: absolute;
   top: 10rpx;
   right: 10rpx;
   width: 40rpx;
   height: 40rpx;
   background-color: rgba(0, 0, 0, 0.5);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
}

/* 底部互动区域 */
.interaction-section {
   padding: 20rpx;
   position: fixed;
   bottom: 0;
   left: 0;
   right: 0;
   background: #fff;
   border-top: 1rpx solid #f0f0f0;
}

.comment-input-area {
   display: flex;
   gap: 20rpx;
   align-items: center;
}

.input-title {
   flex: 1;
   background: #f4f4f4;
   height: 100%;
   border-radius: 15rpx;
   padding: 15rpx 20rpx;
   font-size: 28rpx;
   color: #333;
   text-align: left;
}

.input-actions {
   display: flex;
   align-items: center;
   gap: 15rpx;
}

.photo-btn {
   width: 60rpx;
   height: 60rpx;
   border-radius: 50%;
   background-color: #f4f4f4;
   display: flex;
   align-items: center;
   justify-content: center;
}

.send-btn {
   background-color: #0bdaee;
   color: #fff;
   border-radius: 20rpx;
   font-size: 26rpx;
   padding: 0 30rpx;
   height: 60rpx;
   line-height: 60rpx;
}

.send-btn:disabled {
   background-color: #ccc;
   color: #fff;
}
/* 取消回复按钮样式 */
.cancel-reply {
   font-size: 26rpx;
   color: #0bdaee;
   padding: 0 10rpx;
}
</style>
