<template>
   <view class="page">
      <wd-tabs v-model="tab" swipeable>
         <block v-for="item in tabs" :key="item.value">
            <wd-tab :title="`${item.title}`" :name="item.value">
               <view class="content" v-if="item.value == '0'">
                  <Alluser v-if="tab == 0" :data="fromAlluser"></Alluser> </view
               ><view class="content" v-if="item.value == '1'">
                  <Dicuser v-if="tab == 1" :data="fromDicuser"></Dicuser>
               </view>
               <view class="content" v-if="item.value == '2'">
                  <ChatList v-if="tab == 2" :data="fromChatList"></ChatList>
               </view>
            </wd-tab>
         </block>
      </wd-tabs>
      <view class="but" v-if="tab == 0">
         <wd-icon @click="show = true" name="setting"></wd-icon>
      </view>
      <view v-if="show" class="popup-overlay" @click="handleClose"></view>
      <view v-if="show" class="popup-container">
         <view class="popup-content">
            <view class="popup-header">
               <text class="popup-title">筛选条件</text>
               <view class="popup-close" @click="handleClose">
                  <text>×</text>
               </view>
            </view>

            <view class="filter-section">
               <text class="filter-label">性别</text>
               <view class="gender-options">
                  <view
                     v-for="option in genderOptions"
                     :key="option.value"
                     class="gender-option"
                     :class="{ active: fromAlluser.gender === option.value }"
                     @click="selectGender(option.value)">
                     <text class="gender-text">{{ option.label }}</text>
                  </view>
               </view>
            </view>

            <view class="filter-section">
               <text class="filter-label">年龄范围</text>
               <view class="picker-container">
                  <picker
                     mode="selector"
                     :range="ageOptions[0]"
                     :value="getAgeIndex"
                     @change="handleAgeChange"
                     class="native-picker">
                     <view class="picker-display">
                        {{ fromAlluser.age || '选择年龄范围' }}
                        <text class="picker-arrow">▼</text>
                     </view>
                  </picker>
               </view>
            </view>

            <view class="filter-section">
               <view
                  style="
                     display: flex;
                     align-items: center;
                     margin-bottom: 20rpx;
                  ">
                  <text class="filter-label" style="margin-bottom: 0; flex: 1"
                     >地区</text
                  >
                  <view class="location-refresh" @click="refreshLocation">
                     <wd-icon
                        name="refresh"
                        size="24px"
                        color="#ff69b4"></wd-icon>
                  </view>
               </view>
               <view class="picker-container">
                  <picker
                     mode="selector"
                     :range="provinceOptions[0]"
                     :value="getProvinceIndex"
                     @change="handleProvinceChange"
                     class="native-picker">
                     <view class="picker-display">
                        {{ fromAlluser.province || '选择地区' }}
                        <text class="picker-arrow">▼</text>
                     </view>
                  </picker>
               </view>
            </view>

            <view class="popup-footer">
               <button @click="resetFilter" class="reset-btn">重置</button>
               <button @click="submit" class="submit-btn">确定</button>
            </view>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import Alluser from '@/components/home-list/Alluser.vue';
import Dicuser from '@/components/home-list/Dicuser.vue';
import ChatList from '@/components/home-list/ChatList.vue';
import { UserUpdateLongitude } from '@/api/user';
const show = ref(false);
const fromAlluser = ref({
   gender: null, //性别
   age: '', //年龄范围
   province: '' //地区
});
const fromDicuser = ref({});
const fromChatList = ref({
   sach: ''
});
const genderOptions = ref([
   { label: '不限', value: null },
   { label: '男', value: 1 },
   { label: '女', value: 0 }
]);
const ageOptions = ref([
   ['不限', '18-22岁', '23-26岁', '27-30岁', '31-35岁', '36岁以上']
]);

const tabs = ref([
   { title: '全部', value: '0' },
   { title: '附近', value: '1' },
   { title: '趣味群', value: '2' }
]);
const tab = ref(0);

const provinceOptions = ref([
   [
      '不限',
      '北京',
      '上海',
      '天津',
      '重庆',
      '河北',
      '山西',
      '辽宁',
      '吉林',
      '黑龙江',
      '江苏',
      '浙江',
      '安徽',
      '福建',
      '江西',
      '山东',
      '河南',
      '湖北',
      '湖南',
      '广东',
      '广西',
      '海南',
      '四川',
      '贵州',
      '云南',
      '西藏',
      '陕西',
      '甘肃',
      '青海',
      '宁夏',
      '新疆',
      '香港',
      '澳门',
      '台湾'
   ]
]);
const getAgeIndex = computed(() => {
   const index = ageOptions.value[0].findIndex(
      option => option === fromAlluser.value.age
   );
   return index !== -1 ? index : 0;
});
const getProvinceIndex = computed(() => {
   const index = provinceOptions.value[0].findIndex(
      option => option === fromAlluser.value.province
   );
   return index !== -1 ? index : 0;
});

const selectGender = (gender: string) => {
   fromAlluser.value.gender = gender;
};
const handleAgeChange = (e: any) => {
   fromAlluser.value.age = ageOptions.value[0][e.detail.value];
};

const handleProvinceChange = (e: any) => {
   fromAlluser.value.province = provinceOptions.value[0][e.detail.value];
};

const resetFilter = () => {
   fromAlluser.value = {
      gender: '',
      age: '',
      province: ''
   };
};

const submit = () => {
   if (tab.value == 0) {
      if (fromAlluser.value.province == '不限') {
         fromAlluser.value.province = '';
      }
      fromAlluser.value = { ...fromAlluser.value };
      // 这里可以添加筛选逻辑
   } else if (tab.value == 1) {
      fromDicuser.value = { ...fromDicuser.value };
   } else if (tab.value == 2) {
      fromChatList.value = { ...fromChatList.value };
   }

   // 关闭弹窗
   show.value = false;
};
// 关闭弹窗
const handleClose = () => {
   show.value = false;
};
// 刷新位置 - 确保此方法可在模板中访问
const refreshLocation = () => {
   // 调用uni.getLocation获取用户位置
   uni.getLocation({
      type: 'wgs84',
      geocode: true,
      success: function (res) {
         // #ifdef APP-PLUS
         fromAlluser.value.province = res.address.province;
         // #endif
         // #ifdef H5
         UserUpdateLongitude({
            lat: res.latitude,
            lng: res.longitude
         }).then(res => {
            if (res.code == 0) {
               //更新成功

               fromAlluser.value.province = res.data.province;
            }
         });
         // #endif
      },
      fail: function (err) {
         console.error('获取位置失败:', err);
         uni.showToast({
            title: '获取位置失败，请检查定位权限',
            icon: 'error',
            duration: 2000
         });

         // 引导用户打开定位权限
         uni.openSetting({
            success: res => {
               console.log('设置结果:', res);
            }
         });
      }
   });
};
</script>

<style lang="scss" scoped>
:deep() {
   .wd-tabs__nav-container {
      width: 350rpx;
   }
   .is-active {
      font-size: 36rpx !important;
   }
   .wd-tabs__nav {
      position: fixed;
      top: var(--status-bar-height);
      z-index: 1;
      background: var(--quyou-nav-bg-color);
      height: $quyou-nav-height;
   }
   .zp-page-top {
      z-index: -1 !important;
   }
}

.page {
   position: relative;
}

.content {
   height: 95vh;
   background: linear-gradient(160deg, #f2e9d2, #fff);
}

.but {
   position: fixed;
   z-index: 9999 !important;
   top: var(--status-bar-height);
   z-index: 99;
   transform: translateY(50%);
   right: 50rpx;
   display: flex;
   justify-items: center;
   align-items: center;
   font-size: 1.2em;
   color: rgba(0, 0, 0, 0.5);
}

/* 弹出层样式 */
.popup-overlay {
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background-color: rgba(0, 0, 0, 0.5);
   z-index: 998;
}

.popup-container {
   position: fixed;
   bottom: 130rpx;
   left: 0;
   width: 100%;
   height: 70vh;
   background-color: #fff;
   border-radius: 30rpx 30rpx 0 0;
   z-index: 999;
   display: flex;
   flex-direction: column;
}

.popup-content {
   height: 100%;
   display: flex;
   flex-direction: column;
   padding: 0 30rpx;
}

.popup-header {
   display: flex;
   justify-content: center;
   align-items: center;
   padding: 30rpx 0;
   border-bottom: 1px solid #eee;
   position: relative;
   margin-bottom: 0;
}

.popup-title {
   font-size: 36rpx;
   font-weight: bold;
   color: #333;
}

.popup-close {
   position: absolute;
   right: 0;
   width: 60rpx;
   height: 60rpx;
   display: flex;
   justify-content: center;
   align-items: center;
   font-size: 48rpx;
   color: #999;
}

/* 筛选区域样式 */
.filter-section {
   padding: 30rpx 0;
   border-bottom: 1px solid #f5f5f5;
   margin-bottom: 0;
}

.filter-label {
   display: block;
   font-size: 28rpx;
   color: #666;
   margin-bottom: 20rpx;
}

/* 性别选择样式 */
.gender-options {
   display: flex;
   flex-wrap: wrap;
   gap: 20rpx;
}

.gender-option {
   padding: 20rpx 40rpx;
   border: 1px solid #ddd;
   border-radius: 30rpx;
   background-color: #fff;
   transition: all 0.3s ease;
}

.gender-option.active {
   border-color: #ff69b4;
   background-color: #fff0f8;
}

.gender-option.active .gender-text {
   color: #ff69b4;
}

.gender-text {
   font-size: 28rpx;
   color: #333;
}

/* 选择器样式 */
.picker-container {
   background-color: #f5f5f5;
   border-radius: 8rpx;
   overflow: hidden;
}

.native-picker {
   width: 100%;
}

.picker-display {
   padding: 24rpx;
   display: flex;
   justify-content: space-between;
   align-items: center;
   font-size: 28rpx;
   color: #333;
}

.picker-arrow {
   color: #999;
   font-size: 24rpx;
}

/* 底部按钮区域 */
.popup-footer {
   display: flex;
   gap: 20rpx;
   padding: 30rpx 0;
   margin-top: auto;
   border-top: 1rpx solid #f0f0f0;
}

.reset-btn {
   flex: 1;
   height: 90rpx;
   line-height: 90rpx;
   background-color: #f5f5f5;
   color: #666;
   font-size: 32rpx;
   border: none;
   border-radius: 45rpx;
}

.submit-btn {
   flex: 1;
   height: 90rpx;
   line-height: 90rpx;
   background-color: #ff69b4;
   color: #fff;
   font-size: 32rpx;
   border: none;
   border-radius: 45rpx;
   box-shadow: 0 4rpx 12rpx rgba(255, 107, 129, 0.3);
}

/* 响应式设计 */
@media screen and (max-width: 375px) {
   .popup-title {
      font-size: 34rpx;
   }

   .filter-label {
      font-size: 26rpx;
   }

   .gender-text {
      font-size: 26rpx;
   }

   .picker-display {
      font-size: 26rpx;
   }

   .reset-btn,
   .submit-btn {
      height: 84rpx;
      line-height: 84rpx;
      font-size: 30rpx;
   }
}
</style>
