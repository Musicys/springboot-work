import { Rote } from '@/access/config';

export default {
   path: 'tszc',
   name: 'tszc',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Warning',
      setup: true,
      tabConfig: {
         hideClose: false,
         keepAlive: true,
         name: '投诉仲裁管理',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'list',
         name: 'tszcList',
         component: () => import('@/view/tszc/tszc/list.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '投诉仲裁列表'
            }
         }
      },
      {
         path: 'detail',
         name: 'tszcDetail',
         component: () => import('@/view/tszc/tszc/detail.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true,
               keepAlive: false,
               name: '投诉仲裁详情'
            }
         }
      }
   ]
};
