import { Rote } from '@/access/config';

export default {
   path: 'sjnt',
   name: 'sjnt',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Bell',
      setup: true,
      tabConfig: {
         hideClose: false,
         keepAlive: true,
         name: '商家通知管理',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'list',
         name: 'sjntList',
         component: () => import('@/view/sjnt/list.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '接单通知列表'
            }
         }
      },
      {
         path: 'detail',
         name: 'sjntDetail',
         component: () => import('@/view/sjnt/detail.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true,
               keepAlive: false,
               name: '通知详情'
            }
         }
      }
   ]
};
