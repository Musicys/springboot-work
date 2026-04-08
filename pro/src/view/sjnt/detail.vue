<template>
   <DefaultContainer>
      <div class="title">通知详情</div>
      <div class="content">
         <ElCard shadow="hover">
            <template #header>
               <div class="card-header">
                  <span>通知信息</span>
               </div>
            </template>
            <ElRow :gutter="20">
               <ElCol :span="12">
                  <ElDescriptions title="通知基本信息" border>
                     <ElDescriptionsItem label="通知ID">{{ notificationInfo.id }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="商家ID">{{ notificationInfo.merchantId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="用户ID">{{ notificationInfo.userId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="兼职ID">{{ notificationInfo.jobId }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="通知内容">{{ notificationInfo.content }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="是否已读">{{
                        notificationInfo.isRead ? '已读' : '未读'
                     }}</ElDescriptionsItem>
                     <ElDescriptionsItem label="创建时间">{{ notificationInfo.createdAt }}</ElDescriptionsItem>
                  </ElDescriptions>
               </ElCol>
               <ElCol :span="12">
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
                  <span>兼职信息</span>
               </div>
            </template>
            <ElDescriptions title="兼职详情" border>
               <ElDescriptionsItem label="兼职ID">{{ jobInfo.id }}</ElDescriptionsItem>
               <ElDescriptionsItem label="岗位标题">{{ jobInfo.title }}</ElDescriptionsItem>
               <ElDescriptionsItem label="商家ID">{{ jobInfo.merchantId }}</ElDescriptionsItem>
               <ElDescriptionsItem label="分类ID">{{ jobInfo.categoryId }}</ElDescriptionsItem>
               <ElDescriptionsItem label="薪资范围"
                  >{{ jobInfo.salaryMin }} - {{ jobInfo.salaryMax }}</ElDescriptionsItem
               >
               <ElDescriptionsItem label="押金金额">{{ jobInfo.depositAmount }}</ElDescriptionsItem>
               <ElDescriptionsItem label="状态">{{ getJobStatusText(jobInfo.status) }}</ElDescriptionsItem>
               <ElDescriptionsItem label="交易模式">{{ getTradeModeText(jobInfo.tradeMode) }}</ElDescriptionsItem>
               <ElDescriptionsItem label="创建时间" :span="2">{{ jobInfo.createdAt }}</ElDescriptionsItem>
            </ElDescriptions>
         </ElCard>
      </div>
      <div class="footer">
         <ElButton @click="router.back()"> 返回 </ElButton>
      </div>
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElCard, ElCol, ElDescriptions, ElDescriptionsItem, ElRow } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';

const router = useRouter();
const route = useRoute();
const id = route.query.id;

const notificationInfo = ref({
   id: 1,
   merchantId: 1,
   userId: 101,
   jobId: 201,
   content: '用户101已接单，兼职ID：201',
   isRead: false,
   createdAt: '2026-01-01 10:00:00'
});

const userInfo = ref({
   id: 101,
   username: 'user1',
   avatarUrl:
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20user%20avatar&image_size=square',
   regionCode: '110000',
   userType: 1,
   status: 1,
   createdAt: '2026-01-01 08:00:00'
});

const jobInfo = ref({
   id: 201,
   merchantId: 1,
   title: '兼职服务员',
   categoryId: 1,
   salaryMin: 100,
   salaryMax: 150,
   depositAmount: 50,
   status: 2,
   tradeMode: 1,
   createdAt: '2026-01-01 09:00:00'
});

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

function getJobStatusText(status) {
   switch (status) {
      case 1:
         return '发布中';
      case 2:
         return '进行中';
      case 3:
         return '已完成';
      case 4:
         return '已关闭';
      case 5:
         return '强制下架';
      default:
         return '未知';
   }
}

function getTradeModeText(mode) {
   switch (mode) {
      case 1:
         return '平台担保';
      case 2:
         return '允许线下';
      default:
         return '未知';
   }
}

onMounted(() => {
   // 模拟获取数据
   console.log('获取通知详情:', id);
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

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}
</style>
