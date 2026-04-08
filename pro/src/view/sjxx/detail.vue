<template>
   <DefaultContainer>
      <div class="title">商家详情查看</div>
      <div class="content">
         <ElCard shadow="hover">
            <template #header>
               <div class="card-header">
                  <span>基本信息</span>
               </div>
            </template>
            <ElRow :gutter="20">
               <ElCol :span="8">
                  <ElDescriptions title="商家信息" border>
                     <ElDescriptionsItem label="商家ID">{{ merchantInfo.id }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="商家名称">{{ merchantInfo.companyName }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="联系电话">{{ merchantInfo.contactPhone }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="法人姓名">{{ merchantInfo.legalPerson }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="法人身份证">{{ merchantInfo.legalIdCard }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="公司地址">{{ merchantInfo.companyAddress }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="公司简介">{{ merchantInfo.companyIntro }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="注册成本">{{ merchantInfo.registeredCapital }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="状态">{{ getStatusText(merchantInfo.status) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ merchantInfo.createdAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
               <ElCol :span="16">
                  <ElDescriptions title="用户信息" border>
                     <ElDescriptionsItem label="用户ID">{{ userInfo.id }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户名">{{ userInfo.username }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="头像">{{ userInfo.avatarUrl }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="地区代码">{{ userInfo.regionCode }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户类型">{{ getUserTypeText(userInfo.userType) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="状态">{{ getStatusText(userInfo.status) }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ userInfo.createdAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
            </ElRow>
         </ElCard>
         <ElCard shadow="hover" style="margin-top: 20px">
            <template #header>
               <div class="card-header">
                  <span>公司图片</span>
               </div>
            </template>
            <div class="image-list">
               <ElImage
                  v-for="(image, index) in merchantInfo.companyImages"
                  :key="index"
                  :src="image"
                  fit="cover"
                  style="width: 200px; height: 150px; margin-right: 10px; margin-bottom: 10px" />
            </div>
         </ElCard>
      </div>
      <div class="footer">
         <ElButton @click="router.back()"> 返回 </ElButton>
      </div>
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElCard, ElCol, ElDescriptions, ElDescriptionsItem, ElImage, ElRow } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const router = useRouter();
const route = useRoute();
const id = route.query.id;

const merchantInfo = ref({
   id: 1,
   companyName: '科技有限公司',
   contactPhone: '13800138000',
   legalPerson: '张三',
   legalIdCard: '110101199001011234',
   companyAddress: '北京市朝阳区某某大厦',
   companyIntro: '这是一家科技公司，主要从事软件开发业务。公司成立于2020年，拥有员工500人，年营业额超过1亿元。',
   registeredCapital: '1000万',
   companyImages: [
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20office%20building%20exterior&image_size=landscape_4_3',
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20office%20interior%20with%20computers&image_size=landscape_4_3',
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=company%20team%20meeting&image_size=landscape_4_3',
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=software%20development%20workspace&image_size=landscape_4_3'
   ],
   status: 1,
   createdAt: '2026-01-01 10:00:00'
});

const userInfo = ref({
   id: 1,
   username: 'merchant1',
   avatarUrl: 'https://example.com/avatar.jpg',
   regionCode: '110000',
   userType: 2,
   status: 1,
   createdAt: '2026-01-01 10:00:00'
});

function getStatusText(status) {
   switch (status) {
      case 1:
         return '正常';
      case 0:
         return '封禁';
      case -1:
         return '注销';
      default:
         return '未知';
   }
}

function getUserTypeText(userType) {
   switch (userType) {
      case 1:
         return '求职者';
      case 2:
         return '商家';
      case 3:
         return '管理员';
      default:
         return '未知';
   }
}

onMounted(() => {
   // 模拟获取数据
   console.log('获取商家详情:', id);
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
}

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}
</style>
