<template>
   <DefaultContainer>
      <Title is-back>
         {{ id && show ? '查看数据' : id ? '编辑数据' : '添加数据' }}
         <template #right>
            <ElButton v-if="!show" type="primary" :loading="submitLoading" @click="onSave"> 提交 </ElButton>
         </template>
      </Title>
      <SjglForm :id="id" ref="sjglFormRef" :show="show" />
   </DefaultContainer>
</template>

<script setup>
import { ElButton, ElMessage } from 'element-plus';
import { computed, ref, useTemplateRef } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DefaultContainer from '@/components/DefaultContainer/index.vue';
import Title from '@/components/Title/index.vue';
import SjglForm from './components/sjgl-form.vue';

const route = useRoute();
const router = useRouter();
const id = computed(() => route.query?.id || undefined);
const show = computed(() => route.query?.show || false);
const sjglFormRef = useTemplateRef('sjglFormRef');

const submitLoading = ref(false);

async function onSave() {
   if (submitLoading.value) return;
   submitLoading.value = true;
   try {
      await sjglFormRef.value.onSave();
      ElMessage.success('保存成功');
      router.replace('/pages/sjgl/sjgllist');
   } catch (error) {
      console.log(error);
   } finally {
      submitLoading.value = false;
   }
}
</script>
