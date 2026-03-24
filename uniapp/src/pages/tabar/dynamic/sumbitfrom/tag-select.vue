<template>
   <view class="tag-select-container">
      <view class="navtop">
         <tn-icon
            style="margin-right: 20rpx"
            name="close"
            @click="emit('closeSelect')"></tn-icon>
      </view>
      <!-- 搜索框 -->
      <view class="search-box">
         <input
            v-model="sach"
            type="text"
            placeholder="搜索频道"
            class="search-input"
            @input="handleSearch" />
      </view>

      <!-- 频道列表 -->
      <view class="tag-list">
         <view
            v-for="tag in filteredTags"
            :key="tag.id"
            class="tag-item"
            @click="selectTag(tag)">
            <image :src="tag.url" class="tag-image" mode="aspectFill"></image>
            <view class="tag-info">
               <text class="tag-name">{{ tag.categoryname }}</text>
               <text class="tag-desc">{{ tag.introductory }}</text>
            </view>
         </view>
      </view>
      <!-- 无数据提示 -->
      <view v-if="filteredTags.length === 0" class="no-data">
         <text>暂无相关频道</text>
      </view>
   </view>
   <view class="popu"> </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { DynamciTagList } from '@/api/dynamictag';
import { TagType } from '@/types/dynamic';
const emit = defineEmits(['tagSelect', 'closeSelect']);
const sach = ref('');
const tagList = ref<TagType[]>([]);
const tagFrom = reactive({
   page: 1,
   pageSize: 10
});

// 过滤后的频道列表
const filteredTags = computed(() => {
   if (!sach.value.trim()) {
      return tagList.value;
   }
   const searchText = sach.value.toLowerCase().trim();
   return tagList.value.filter(
      tag =>
         tag.categoryname.toLowerCase().includes(searchText) ||
         tag.introductory.toLowerCase().includes(searchText)
   );
});

// 处理搜索
const handleSearch = () => {
   // 搜索逻辑已通过computed属性实现
};

// 选择频道
const selectTag = (tag: TagType) => {
   emit('tagSelect', tag);
   emit('closeSelect');
};

// 加载频道列表
onMounted(() => {
   DynamciTagList(tagFrom).then(res => {
      if (res.code === 0) {
         tagList.value = res.data.records;
      }
   });
});
</script>

<style lang="scss" scoped>
.popu {
   width: 100%;
   height: 100%;
   position: absolute;
   top: 0;
   left: 0;
   background: rgba(0, 0, 0, 0.5);
   z-index: 9;
}
.tag-select-container {
   padding: 20rpx;
   overflow-y: auto;
   position: fixed;
   width: 100%;
   height: 60vh;
   bottom: 0;
   background: #fff;
   z-index: 10;
   padding-top: 60rpx;
   border-radius: 15rpx 15rpx 0 0;
}
.navtop {
   position: absolute;
   top: 0;
   left: 0;
   height: 60rpx;
   width: 100%;
   display: flex;
   justify-content: flex-end;

   color: black;
}

.search-box {
   margin-bottom: 20rpx;
   padding: 0 20rpx;
}

.search-input {
   width: 100%;
   height: 70rpx;
   border-radius: 35rpx;
   background-color: #f5f5f5;
   padding: 0 30rpx;
   font-size: 28rpx;
   color: #333;
}

.tag-list {
   display: flex;
   flex-direction: column;
   gap: 20rpx;
   height: 40vh;
}

.tag-item {
   display: flex;
   align-items: center;
   padding: 20rpx;
   background-color: #fff;
   border-radius: 12rpx;
   box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
   transition: all 0.3s ease;

   &:active {
      background-color: #f0f9ff;
   }
}

.tag-image {
   width: 100rpx;
   height: 100rpx;
   border-radius: 8rpx;
   margin-right: 20rpx;
}

.tag-info {
   flex: 1;
}

.tag-name {
   font-size: 32rpx;
   font-weight: 600;
   color: #333;
   display: block;
   margin-bottom: 8rpx;
}

.tag-desc {
   font-size: 26rpx;
   color: #666;
   display: -webkit-box;
   -webkit-line-clamp: 2;
   -webkit-box-orient: vertical;
   overflow: hidden;
   text-overflow: ellipsis;
}

.no-data {
   text-align: center;
   padding: 60rpx 0;
   color: #999;
   font-size: 28rpx;
}
</style>
