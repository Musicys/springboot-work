import { Rote } from '@/access/config';

export default {
   path: 'yydd',
   name: 'yydd',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Document',
      setup: true,
      tabConfig: {
         hideClose: true,
         keepAlive: true,
         name: '订单管理中心',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'yyddlist',
         name: 'yyddList',
         component: () => import('@/view/yydd/yydd/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true,
               keepAlive: true,
               name: '订单列表'
            }
         }
      },
      {
         path: 'yyddadd',
         name: 'yyddAdd',
         component: () => import('@/view/yydd/yydd/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '编辑订单'
            }
         }
      }
   ]
};
