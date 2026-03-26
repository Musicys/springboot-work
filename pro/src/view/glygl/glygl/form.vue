<template>
   <DefaultContainer>
      <div class="title">{{ show ? '管理员详情' : id ? '编辑管理员' : '新增管理员' }}</div>
      <div class="content">
         <glygl-form ref="formRef" :id="id" :show="show" />
      </div>
      <div class="footer" v-if="!show">
         <ElButton @click="router.back()"> 取消 </ElButton>
         <ElButton type="primary" @click="onSave"> 确定 </ElButton>
      </div>
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElMessage } from 'element-plus';
import { ref, getCurrentInstance, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import DefaultContainer from '@/components/DefaultContainer/index.vue';
import glyglForm from './components/glygl-form.vue';

const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const id = route.query.id;
const show = route.query.show === 'true';

const formRef = ref();

async function onSave() {
   try {
      await formRef.value.onSave();
      ElMessage.success('保存成功');
      router.push('/pages/glygl/glygllist');
   } catch (error) {
      console.error('保存失败:', error);
      ElMessage.error('保存失败，请稍后重试');
   }
}
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

.footer {
   display: flex;
   justify-content: flex-end;
   margin-top: 20px;
   padding-top: 20px;
   border-top: 1px solid rgb(0 0 0 / 6%);
}
</style>
