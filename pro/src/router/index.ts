import { Rote } from '@/access/config';
import { createRouter, createWebHashHistory } from 'vue-router';
import Layout from '@/layout/index.vue';

import jzfb from './module/jzfb';
import sjxx from './module/sjxx';
import sjjs from './module/sjjs';
import sjnt from './module/sjnt';
// import { createAsyncComponent } from '@/util/index';
const pagesconfig = [
   // yhgl, //用户
   // sjgl //商家管理中心
   jzfb,
   sjxx,
   sjjs,
   sjnt
];

// 2. 配置路由
const routes = [
   {
      path: '/',
      redirect: '/login'
   },
   {
      path: '/login',
      name: 'loginFrom',
      component: () => import('@/view/login/login-from.vue')
   },
   {
      path: '/register',
      name: 'registerFrom',
      component: () => import('@/view/register/register-from.vue')
   },

   {
      path: '/404',
      name: '404',
      component: () => import('@/view/notfound/PageNoView.vue')
   },
   {
      path: '/401',
      name: '401',
      component: () => import('@/view/notfound/PageNoRole.vue')
   },
   {
      path: '/pages',
      name: 'pages',
      component: Layout,
      children: [
         {
            path: '',
            redirect: 'home'
         },
         {
            path: 'home',
            name: 'home',
            component: () => import('@/view/home-charts/index.vue'),
            meta: {
               icon: 'Monitor', //图标
               tabConfig: {
                  hideClose: true, // 隐藏关闭按钮
                  keepAlive: true, // 启用缓存
                  name: '商家数据概述',
                  access: Rote.user
               }
            }
         },
         ...pagesconfig
      ]
   }
];

const router = createRouter({
   history: createWebHashHistory(),
   routes
});

// 3导出路由   然后去 main.ts 注册 router.ts
export default router;
