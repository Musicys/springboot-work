import { createSSRApp } from 'vue';
import App from './App.vue';
import '@/static/css/index.css';
import router from './router';
import '@/access/index'; // 权限控制
import * as Pinia from 'pinia';
import '@/util/push';
import { createUnistorage } from 'pinia-plugin-unistorage';

export function createApp() {
   const app = createSSRApp(App);
   const store = Pinia.createPinia();
   store.use(createUnistorage());
   app.use(router);
   app.use(store);
   return {
      app,
      Pinia
   };
}
