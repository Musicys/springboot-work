<template>
   <view class="search-page">
      <!-- 顶部搜索区域 -->
      <view class="search-header">
         <view class="search-bar">
            <view class="back-btn" @click="handleBack">
               <wd-icon name="thin-arrow-left" size="22px"></wd-icon>
            </view>
            <view class="search-input-container">
               <uni-icons
                  type="search"
                  size="20"
                  color="#999"
                  class="search-icon"></uni-icons>

               <input
                  type="search"
                  v-model="searchKeyword"
                  placeholder="搜索"
                  @change="handleSearchChange"
                  placeholder-class="search-placeholder"
                  @focus="showHistory = true"
                  class="search-input" />
            </view>
            <view class="search-btn" @click="handleSearch">
               <text class="search-btn-text">搜索</text>
            </view>
         </view>
      </view>
      <!-- 搜索历史区域 -->
      <SrearchCentet v-if="isFirstSearch" :data="searchKeyword"></SrearchCentet>
      <template v-else>
         <!-- 搜索历史区域 -->
         <view v-if="showHistory" class="history-section">
            <view class="section-header">
               <text class="section-title">搜索历史</text>
               <view class="clear-btn" @click="clearHistory">
                  <uni-icons type="trash" size="18" color="#999"></uni-icons>
               </view>
            </view>
            <view class="history-tags">
               <view
                  v-for="(tag, index) in searchHistory"
                  :key="index"
                  class="history-tag"
                  @click="handleTagSearch(tag)">
                  <text class="tag-text">{{ tag }}</text>
               </view>
            </view>
         </view>

         <!-- 热SOUL区域 -->
         <view class="hot-section">
            <view class="section-header">
               <view class="hot-title">
                  <uni-icons
                     type="flame"
                     size="20"
                     color="#ff7a45"
                     class="hot-icon"></uni-icons>
                  <text class="section-title">热Hot</text>
               </view>
            </view>
            <view class="hot-list">
               <view
                  v-for="(item, index) in hotList"
                  :key="index"
                  class="hot-item"
                  @click="handleHotItemClick(item.title)">
                  <view class="hot-rank" :class="getRankClass(index)">
                     <text class="rank-text">{{ index + 1 }}</text>
                  </view>
                  <text class="hot-content">{{ item.title }}</text>
                  <text class="hot-value">{{ item.hotValue }}</text>
               </view>
            </view>
         </view>
      </template>
   </view>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import SrearchCentet from './components/SrearchCentet.vue';
const router = useRouter();

// 搜索关键词
const searchKeyword = ref('');
// 是否显示搜索历史
const showHistory = ref(true);

//是否第一次搜素
const isFirstSearch = ref(false);
// 搜索历史数据
const searchHistory = ref([
   '英魂',
   '英魂之刃',
   '南昌找对象',
   '南昌',
   '南昌交通学院'
]);
// 热门内容数据
const hotList = ref([
   { title: '南昌八一广场游玩攻略', hotValue: '9.8万' },
   { title: '冬日穿搭推荐，保暖又时尚', hotValue: '7.5万' },
   { title: '英雄联盟S13全球总决赛', hotValue: '6.2万' },
   { title: '江西旅游必去十大景点', hotValue: '5.9万' },
   { title: '冬日火锅推荐，暖心又暖胃', hotValue: '4.7万' },
   { title: '南昌大学最新校园活动', hotValue: '3.8万' },
   { title: '2024年最值得期待的电影', hotValue: '3.5万' },
   { title: '职场新人必备技能', hotValue: '2.9万' },
   { title: '春节回家攻略大全', hotValue: '2.7万' },
   { title: '学习英语的10个实用技巧', hotValue: '2.3万' }
]);

// 返回上一页
const handleBack = () => {
   router.back();
};

// 执行搜索
const handleSearch = () => {
   if (searchKeyword.value.trim()) {
      // 添加到搜索历史

      if (!isFirstSearch.value) {
         isFirstSearch.value = true;
      } else {
         searchKeyword.value = searchKeyword.value.trim();
      }

      if (!searchHistory.value.includes(searchKeyword.value.trim())) {
         searchHistory.value.unshift(searchKeyword.value.trim());
         // 限制历史记录数量
         if (searchHistory.value.length > 10) {
            searchHistory.value.pop();
         }
      }
      // 这里可以添加实际的搜索逻辑，比如调用API

      showHistory.value = false;
   }
};

// 点击历史标签搜索
const handleTagSearch = (tag: string) => {
   searchKeyword.value = tag;
   handleSearch();
};

// 清空搜索历史
const clearHistory = () => {
   uni.showModal({
      title: '提示',
      content: '确定要清空搜索历史吗？',
      success: res => {
         if (res.confirm) {
            searchHistory.value = [];
         }
      }
   });
};

// 点击热门内容
const handleHotItemClick = (title: string) => {
   searchKeyword.value = title;
   handleSearch();
};

// 获取排名样式类
const getRankClass = (index: number) => {
   if (index === 0) return 'rank-first';
   if (index === 1) return 'rank-second';
   if (index === 2) return 'rank-third';
   return 'rank-normal';
};

// 监听回车键搜索
const handleKeydown = (e: KeyboardEvent) => {
   if (e.key === 'Enter') {
      handleSearch();
   }
};

const handleSearchChange = e => {
   console.log(e);
};
// 组件挂载时添加键盘事件监听
onMounted(() => {
   window.addEventListener('keydown', handleKeydown);
});

// 组件卸载时移除事件监听
onUnmounted(() => {
   window.removeEventListener('keydown', handleKeydown);
});
</script>

<style lang="scss" scoped>
.search-page {
   background-color: #fff;
   min-height: 100vh;
   padding-bottom: 30rpx;
   padding-top: var(--status-bar-height);
}

/* 顶部搜索区域 */
.search-header {
   padding: 20rpx 30rpx;
   border-bottom: 1px solid #f0f0f0;
   background-color: #fff;
   position: sticky;
   top: 0;
   z-index: 10;
}

.search-bar {
   display: flex;
   align-items: center;
   gap: 20rpx;
}

.back-btn {
   width: 50rpx;
   height: 50rpx;
   display: flex;
   align-items: center;
   justify-content: center;
}

.search-input-container {
   flex: 1;
   height: 70rpx;
   background-color: #f5f5f5;
   border-radius: 35rpx;
   display: flex;
   align-items: center;
   padding: 0 30rpx;
}

.search-icon {
   margin-right: 15rpx;
}

.search-input {
   flex: 1;
   height: 100%;
   font-size: 28rpx;
   color: #333;

   background: transparent;
}

.search-placeholder {
   color: #999;
}

.search-btn {
   padding: 0 20rpx;
}

.search-btn-text {
   font-size: 30rpx;
   color: #ff69b4;
   font-weight: 500;
}

/* 搜索历史区域 */
.history-section {
   padding: 30rpx;
}

.section-header {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-bottom: 25rpx;
}

.section-title {
   font-size: 32rpx;
   font-weight: 600;
   color: #333;
}

.clear-btn {
   padding: 5rpx 10rpx;
}

.history-tags {
   display: flex;
   flex-wrap: wrap;
   gap: 20rpx;
}

.history-tag {
   padding: 15rpx 30rpx;
   background-color: #f5f5f5;
   border-radius: 30rpx;
   transition: all 0.3s ease;
}

.history-tag:active {
   background-color: #e8e8e8;
   transform: scale(0.95);
}

.tag-text {
   font-size: 28rpx;
   color: #333;
}

/* 热SOUL区域 */
.hot-section {
   padding: 0 30rpx;
}

.hot-title {
   display: flex;
   align-items: center;
   gap: 10rpx;
}

.hot-icon {
   animation: pulse 1.5s infinite;
}

@keyframes pulse {
   0% {
      transform: scale(1);
   }
   50% {
      transform: scale(1.1);
   }
   100% {
      transform: scale(1);
   }
}

.hot-list {
   margin-top: 20rpx;
}

.hot-item {
   display: flex;
   align-items: center;
   padding: 25rpx 0;
   border-bottom: 1px solid #f5f5f5;
   transition: background-color 0.2s ease;
}

.hot-item:last-child {
   border-bottom: none;
}

.hot-item:active {
   background-color: #f9f9f9;
}

.hot-rank {
   width: 44rpx;
   height: 44rpx;
   border-radius: 22rpx;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-right: 20rpx;
}

.rank-first {
   background-color: #ff4757;
}

.rank-second {
   background-color: #ff7f50;
}

.rank-third {
   background-color: #ffd700;
}

.rank-normal {
   background-color: #f5f5f5;
}

.rank-text {
   font-size: 24rpx;
   font-weight: 600;
   color: #fff;
}

.rank-normal .rank-text {
   color: #999;
}

.hot-content {
   flex: 1;
   font-size: 28rpx;
   color: #333;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
}

.hot-value {
   font-size: 24rpx;
   color: #999;
   margin-left: 15rpx;
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .search-header {
      padding: 15rpx 20rpx;
   }

   .search-input-container {
      padding: 0 20rpx;
   }

   .section-title {
      font-size: 30rpx;
   }

   .tag-text,
   .hot-content {
      font-size: 26rpx;
   }

   .hot-value {
      font-size: 22rpx;
   }
}
</style>
