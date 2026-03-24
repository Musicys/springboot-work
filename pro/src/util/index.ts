// utils/asyncComponent.ts
import { defineAsyncComponent, Component } from 'vue';
import Loding from '@/components/Loding.vue';
import Error from '@/components/Error.vue';
import loginFrom from '@/view/login/login-from.vue';
/**
 * 创建带状态管理的异步组件
 * @param componentPath 组件路径
 * @param options 可选配置（loading、error 等）
 */
export function createAsyncComponent() {
   return defineAsyncComponent({
      loader: async () => {
         await new Promise(resolve => setTimeout(resolve, 1000));
         return loginFrom;
      },
      loadingComponent: Loding,
      errorComponent: Error
   });
}
