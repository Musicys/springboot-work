<template>
   <view class="page">
      <view class="taber">
         <view class="taber-left">
            <view class="select-tab">找朋友</view>
            <view class="unselect-tab">速交友</view>
            <view class="unselect-tab">趣味群</view>
         </view>
         <view class="taber-right">
            <wd-icon @click="show = true" name="setting"></wd-icon>
         </view>
      </view>
      <!-- 公告 -->
      <Alluser></Alluser>

      <!-- 轮播图 -->
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
.page {
   position: relative;
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
}
.taber {
   position: fixed;
   top: 0;
   height: 155rpx;
   z-index: 99;
   width: 100vw;
   display: flex;
   justify-content: space-between;
   background: #e8efff;

   padding: var(--status-bar-height) 20rpx 50rpx 20rpx; /* 状态栏 */

   .taber-left {
      display: flex;
      justify-content: start;
      flex: 1;
      align-items: center;
      margin-right: 30rpx;
      // padding: 20rpx 0;
      margin-top: 32rpx;
      font-size: 36rpx;
      & > view:nth-child(2n) {
         margin: 20rpx;
      }
   }
   .taber-right {
      width: 200rpx;

      display: flex;
      margin-top: 32rpx;
      font-size: 32rpx;
      justify-content: center;
      align-items: center;
   }
}
.select-tab {
   font-weight: bold;
   color: #000000;
}
.unselect-tab {
   font-weight: normal;
   color: #93aeb4;
}
</style>
